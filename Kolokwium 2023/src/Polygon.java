import java.util.ArrayList;
import java.util.List;

/*
 * Wielokąt opisany listą punktów.
 *
 * Krawędzie tworzą się między kolejnymi punktami,
 * a ostatni punkt łączy się z pierwszym.
 */
public class Polygon {

    private final List<Point> points;

    public Polygon(List<Point> points) {
        this.points = new ArrayList<>(points);
    }

    /*
     * Sprawdza, czy punkt znajduje się wewnątrz wielokąta.
     *
     * Zaimplementowany jest algorytm z treści zadania:
     * - dla każdej krawędzi liczymy przecięcie z promieniem poziomym,
     * - jeśli liczba przecięć jest nieparzysta, punkt jest w środku.
     */
    public boolean inside(Point point) {

        int counter = 0;

        for (int i = 0; i < points.size(); i++) {

            Point pa = points.get(i);
            Point pb = points.get((i + 1) % points.size());

            if (pa.y > pb.y) {
                Point tmp = pa;
                pa = pb;
                pb = tmp;
            }

            if (pa.y < point.y && point.y < pb.y) {

                double d = pb.x - pa.x;
                double x;

                if (Double.compare(d, 0.0) == 0) {
                    x = pa.x;
                } else {
                    double a = (pb.y - pa.y) / d;
                    double b = pa.y - a * pa.x;
                    x = (point.y - b) / a;
                }

                if (x < point.x) {
                    counter++;
                }
            }
        }

        return counter % 2 != 0;
    }
}