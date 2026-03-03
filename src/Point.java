//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

public class Point {
    public double x = (double)5.0F;
    public double y = (double)10.0F;

    public String toString() {
        String napis = "x=" + this.x + " y=" + this.y;
        return napis;
    }

    public String toSvg() {
        String napis = "  <circle r=\"45\" cx=\"" + this.x + "\" cy=\"" + this.y + "\" fill=\"red\" />";
        return napis;
    }

    public String translate(double dx, double dy) {
        this.x += dx;
        this.y += dy;
        String napis = "x teraz wynosi: " + this.x + " y teraz wynosi: " + this.y;
        return napis;
    }

    public Point translated(double dx, double dy) {
        Point nowy = new Point();
        nowy.x = this.x + dx;
        nowy.y = this.y + dy;
        return nowy;
    }
}