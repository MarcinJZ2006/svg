import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/*
 * Miasto jest prostokątem/kwadratem, dlatego dziedziczy po Polygon.
 */
public class City extends Polygon {

    /*
     * Środek miasta.
     */
    public final Point center;

    /*
     * Nazwa miasta.
     */
    private String name;

    /*
     * Czy miasto jest portowe.
     */
    private boolean port;

    /*
     * Własna kopia wierzchołków kwadratu.
     * Potrzebna do sprawdzenia portowości.
     */
    private final List<Point> vertices;

    /*
     * Zbiór zasobów przypisanych do miasta.
     * Dostęp pakietowy zgodnie z treścią zadania.
     */
    Set<Resource.Type> resources = EnumSet.noneOf(Resource.Type.class);

    public City(Point center, String name, double wallSide) {
        super(createSquarePoints(center, wallSide));

        this.center = center;
        this.name = name;
        this.port = false;
        this.vertices = new ArrayList<>(createSquarePoints(center, wallSide));
    }

    /*
     * Tworzy cztery wierzchołki kwadratu miasta.
     */
    private static List<Point> createSquarePoints(Point center, double wallSide) {
        double half = wallSide / 2.0;

        List<Point> points = new ArrayList<>();
        points.add(new Point(center.x - half, center.y - half));
        points.add(new Point(center.x + half, center.y - half));
        points.add(new Point(center.x + half, center.y + half));
        points.add(new Point(center.x - half, center.y + half));

        return points;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isPort() {
        return port;
    }

    /*
     * Ustawia informację, czy miasto jest portowe.
     * Miasto jest portowe, gdy choć jeden wierzchołek leży poza lądem.
     */
    void updatePortStatus(Land land) {
        boolean outside = false;

        for (Point vertex : vertices) {
            if (!land.inside(vertex)) {
                outside = true;
                break;
            }
        }

        this.port = outside;
    }

    /*
     * Dodaje typy zasobów w zasięgu miasta.
     * Ryby uwzględniamy tylko w miastach portowych.
     */
    public void addResourcesInRange(List<Resource> resourceList, double range) {
        for (Resource resource : resourceList) {

            if (resource.type == Resource.Type.Fish && !port) {
                continue;
            }

            double distance = Math.hypot(
                    center.x - resource.point.x,
                    center.y - resource.point.y
            );

            if (distance <= range) {
                resources.add(resource.type);
            }
        }
    }

    @Override
    public String toString() {
        return port ? name + "⚓" : name;
    }
}