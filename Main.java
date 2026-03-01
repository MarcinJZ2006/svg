import java.sql.SQLOutput;

public class Main {
    public static void main(String[] args) {
        // Press Alt+Enter with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!\n");
        Point point = new Point();
        System.out.println(" " + point.x + " " + point.y);
        point.x = 30;
        point.y = 2;
        System.out.println(" " + point.x + " " + point.y);
        Point point1 = new Point();

        System.out.println(point.toString());
        System.out.println(point1.toSvg());

        System.out.println(point.translate(point.x, point1.y));
        System.out.println(point.translated(point1.x, point1.y));
        System.out.println(" " + point1.x + " " + point1.y);


    }
}