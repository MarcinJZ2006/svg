public class Main {
    public static void main(String[] args) {
        System.out.printf("Hello and welcome!\n");
        Point point = new Point();
        System.out.println(" " + point.x + " " + point.y);
        point.x = (double)30.0F;
        point.y = (double)2.0F;
        System.out.println(" " + point.x + " " + point.y);
        Point point1 = new Point();
        Point p2 = new Point();


        System.out.println(point.toString());
        System.out.println(point1.toSvg());
        System.out.println(point.translate(point.x, point1.y));
        System.out.println(point.translated(point1.x, point1.y));
        System.out.println(" " + point1.x + " " + point1.y);


        Segment s = new Segment(point, p2);
        System.out.println(s.length());
    }
}

