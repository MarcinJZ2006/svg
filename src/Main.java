import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        // ===== POINT =====
        Point p1 = new Point(10, 20);
        Point p2 = new Point(30, 40);

        System.out.println("Point 1: " + p1);
        System.out.println("Point 2: " + p2);

        p1.translate(5, 5);
        System.out.println("Po przesunięciu p1: " + p1);

        // ===== SEGMENT =====
        Segment s = new Segment(p1, p2);

        System.out.println(s);
        System.out.println("Length: " + s.length());

        // sprawdzanie niewrażliwości
        p1.setX(100);

        System.out.println("Po zmianie p1:");
        System.out.println(s);
        System.out.println("Length dalej: " + s.length());

        // ===== POLYGON =====
        Point[] pts = {
                new Point(10,10),
                new Point(100,10),
                new Point(100,100),
                new Point(10,100)
        };

        Polygon poly = new Polygon(pts);

        System.out.println("\nPolygon:");
        System.out.println(poly);

        System.out.println("SVG:");
        System.out.println(poly.toSvg());

        // ===== BOUNDING BOX =====
        BoundingBox box = poly.boundingBox();

        System.out.println("\nBoundingBox:");
        System.out.println("x=" + box.x());
        System.out.println("y=" + box.y());
        System.out.println("width=" + box.width());
        System.out.println("height=" + box.height());

        // ===== SCENA SVG =====
        SvgScene scene = new SvgScene();

        scene.addPolygon(poly);

        Point[] pts2 = {
                new Point(150,50),
                new Point(200,20),
                new Point(250,80)
        };

        Polygon poly2 = new Polygon(pts2);

        scene.addPolygon(poly2);

        System.out.println("\nScene SVG:");
        System.out.println(scene.toSvg());

        // ===== ZAPIS DO PLIKU =====
        try {
            scene.save("scene.svg");
            System.out.println("\nZapisano plik scene.svg");
        } catch (IOException e) {
            System.out.println("Błąd zapisu pliku");
        }
    }
}

