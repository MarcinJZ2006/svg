import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/*
 * Parser mapy SVG.
 *
 * Odczytuje:
 * - zielone polygon -> lądy,
 * - czerwone rect -> miasta,
 * - text -> etykiety miast.
 *
 * Zasoby z circle nie są tu potrzebne do wypisu końcowego,
 * ale można je później dołożyć bez rozwalania konstrukcji.
 */
public class MapParser {

    private final List<Label> labels = new ArrayList<>();
    private final List<Land> lands = new ArrayList<>();
    private final List<City> cities = new ArrayList<>();

    public List<Land> getLands() {
        return lands;
    }

    /*
     * Główna metoda parsująca plik SVG.
     */
    public void parse(Path path) throws Exception {
        labels.clear();
        lands.clear();
        cities.clear();

        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(path.toFile());

        Element root = document.getDocumentElement();

        /*
         * Przechodzimy po całym drzewie DOM rekurencyjnie,
         * żeby nie zgubić elementów schowanych w grupach <g>.
         */
        scan(root);

        /*
         * Najpierw przypisujemy miasta do lądów,
         * potem dopasowujemy nazwy z etykiet tekstowych.
         */
        addCitiesToLands();
        matchLabelsToTowns();
    }

    /*
     * Rekurencyjne skanowanie wszystkich węzłów XML.
     */
    private void scan(Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            String tag = element.getTagName().toLowerCase();

            if (tag.endsWith("polygon") && isGreen(element)) {
                lands.add(parseLand(element));
            } else if (tag.endsWith("rect") && isRed(element)) {
                cities.add(parseCity(element));
            } else if (tag.endsWith("text")) {
                labels.add(parseText(element));
            }
        }

        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            scan(children.item(i));
        }
    }

    /*
     * Zamienia element <polygon> na obiekt Land.
     */
    private Land parseLand(Element element) {
        String pointsText = element.getAttribute("points");
        List<Point> points = parsePoints(pointsText);
        return new Land(points);
    }

    /*
     * Zamienia element <rect> na obiekt City.
     *
     * W SVG x i y oznaczają lewy górny róg prostokąta.
     * Środek miasta trzeba policzyć samodzielnie.
     */
    private City parseCity(Element element) {
        double x = parseDouble(element.getAttribute("x"));
        double y = parseDouble(element.getAttribute("y"));
        double width = parseDouble(element.getAttribute("width"));
        double height = parseDouble(element.getAttribute("height"));

        Point center = new Point(x + width / 2.0, y + height / 2.0);

        /*
         * Miasto ma być kwadratem, więc jako bok bierzemy mniejszy wymiar.
         * Jeśli SVG faktycznie ma kwadrat, width == height i nie ma różnicy.
         */
        double wallSide = Math.min(width, height);

        return new City(center, null, wallSide);
    }

    /*
     * Zamienia element <text> na etykietę.
     */
    private Label parseText(Element element) {
        double x = parseDouble(element.getAttribute("x"));
        double y = parseDouble(element.getAttribute("y"));
        String text = element.getTextContent().trim();

        return new Label(new Point(x, y), text);
    }

    /*
     * Parsowanie listy punktów z atrybutu "points".
     *
     * Obsługuje zapis:
     * - "10,20 30,40"
     * - "10 20 30 40"
     * - dowolne odstępy i nowe linie
     *
     * Wszystko sprowadzamy do listy liczb i czytamy parami.
     */
    private List<Point> parsePoints(String pointsText) {
        List<Point> points = new ArrayList<>();

        /*
         * Zamieniamy przecinki na spacje, żeby obsłużyć oba formaty SVG.
         */
        String normalized = pointsText.replace(",", " ").trim();

        /*
         * Dzielimy po dowolnych białych znakach.
         */
        String[] values = normalized.split("\\s+");

        /*
         * Czytamy parami: x, y.
         */
        for (int i = 0; i + 1 < values.length; i += 2) {
            double x = parseDouble(values[i]);
            double y = parseDouble(values[i + 1]);
            points.add(new Point(x, y));
        }

        return points;
    }

    /*
     * Pomocnicze parsowanie liczb.
     * Obsługuje również zapis z przecinkiem dziesiętnym.
     */
    private double parseDouble(String text) {
        return Double.parseDouble(text.trim().replace(",", "."));
    }

    /*
     * Kolor zielony lądu.
     * Sprawdzamy zarówno fill jak i style.
     */
    private boolean isGreen(Element element) {
        String fill = element.getAttribute("fill").toLowerCase();
        String style = element.getAttribute("style").toLowerCase();

        return fill.contains("green")
                || fill.contains("#008000")
                || style.contains("green")
                || style.contains("#008000");
    }

    /*
     * Kolor czerwony miasta.
     */
    private boolean isRed(Element element) {
        String fill = element.getAttribute("fill").toLowerCase();
        String style = element.getAttribute("style").toLowerCase();

        return fill.contains("red")
                || fill.contains("#ff0000")
                || style.contains("red")
                || style.contains("#ff0000");
    }

    /*
     * Dla każdego lądu dodajemy miasta, których środki leżą na tym lądzie.
     */
    public void addCitiesToLands() {
        for (City city : cities) {
            for (Land land : lands) {
                if (land.inside(city.center)) {
                    land.addCity(city);
                    break;
                }
            }
        }
    }

    /*
     * Dla każdego miasta przyporządkowujemy najbliższy napis <text>.
     */
    public void matchLabelsToTowns() {
        for (City city : cities) {

            Label best = null;
            double bestDistance = Double.POSITIVE_INFINITY;

            for (Label label : labels) {
                double distance = Math.hypot(
                        city.center.x - label.point.x,
                        city.center.y - label.point.y
                );

                if (distance < bestDistance) {
                    bestDistance = distance;
                    best = label;
                }
            }

            if (best != null) {
                city.setName(best.text);
            }
        }
    }

    /*
     * Pomocnicza struktura etykiety tekstowej.
     */
    private static class Label {
        private final Point point;
        private final String text;

        private Label(Point point, String text) {
            this.point = point;
            this.text = text;
        }
    }
}