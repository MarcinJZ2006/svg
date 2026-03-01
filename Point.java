public class Point {
    public double x = 5;
    public double y = 10;

    public String toString() {
        String napis = "x=" + this.x + " " + "y=" + y;
        return napis;
    }

    public String toSvg() {
        String napis = "  <circle r=\"45\" cx=\"" + x + "\" cy=\"" + y + "\" fill=\"red\" />";
        return napis;
    }


    public String translate(double dx, double dy) {
        this.x += dx;
        this.y += dy;
        String napis = "x teraz wynosi: " + x + " " + "y teraz wynosi: " + y;
        return napis;
    }
    public Point translated(double dx, double dy) {
        Point nowy = new Point();
        nowy.x = this.x + dx;
        nowy.y = this.y + dy;
        return nowy;
    }
}
