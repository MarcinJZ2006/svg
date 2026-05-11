public class Polygon {

    private Point[] points;

    public Polygon(Point[] points) {

        this.points = new Point[points.length];

        for(int i=0;i<points.length;i++){
            this.points[i] = new Point(points[i]);
        }
    }

    // konstruktor kopiujący (głęboka kopia)
    public Polygon(Polygon p){

        this.points = new Point[p.points.length];

        for(int i=0;i<p.points.length;i++){
            this.points[i] = new Point(p.points[i]);
        }
    }

    public String toString(){

        String s = "";

        for(Point p : points){
            s += p + "\n";
        }

        return s;
    }

    public String toSvg(){

        String s = "<polygon points=\"";

        for(Point p : points){
            s += p.getX() + "," + p.getY() + " ";
        }

        s += "\" fill=\"none\" stroke=\"black\"/>";

        return s;
    }

    public BoundingBox boundingBox(){

        double minX = points[0].getX();
        double minY = points[0].getY();
        double maxX = points[0].getX();
        double maxY = points[0].getY();

        for(Point p : points){

            if(p.getX() < minX) minX = p.getX();
            if(p.getY() < minY) minY = p.getY();
            if(p.getX() > maxX) maxX = p.getX();
            if(p.getY() > maxY) maxY = p.getY();
        }

        return new BoundingBox(minX,minY,maxX-minX,maxY-minY);
    }
}