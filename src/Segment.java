public class Segment {

    private Point p1;
    private Point p2;

    public Segment(Point p1, Point p2) {
        this.p1 = new Point(p1);
        this.p2 = new Point(p2);
    }

    public double length() {
        return Math.sqrt(
                Math.pow(p2.getX() - p1.getX(), 2) +
                        Math.pow(p2.getY() - p1.getY(), 2)
        );
    }

    public String toString() {
        return "Segment: [" + p1 + "] -> [" + p2 + "]";
    }
}