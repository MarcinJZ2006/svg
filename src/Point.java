public class Point {
    private double x;
    private double y;

    // konstruktor bezargumentowy
    public Point() {
        this.x = 0;
        this.y = 0;
    }

    // konstruktor z parametrami
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // konstruktor kopiujący
    public Point(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    // gettery
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // settery
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String toString() {
        return "x=" + x + " y=" + y;
    }

    public String toSvg() {
        return "<circle r=\"5\" cx=\"" + x + "\" cy=\"" + y + "\" fill=\"red\" />";
    }

    public void translate(double dx, double dy) {
        x += dx;
        y += dy;
    }

    public Point translated(double dx, double dy) {
        return new Point(x + dx, y + dy);
    }
}