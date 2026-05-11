import java.io.FileWriter;
import java.io.IOException;

public class SvgScene {

    private Polygon[] polygons = new Polygon[3];
    private int index = 0;

    public void addPolygon(Polygon p){

        polygons[index] = p;

        index++;

        if(index >= polygons.length){
            index = 0;
        }
    }

    public String toSvg(){

        String s = "";

        for(Polygon p : polygons){
            if(p != null){
                s += p.toSvg() + "\n";
            }
        }

        return s;
    }

    public void save(String path) throws IOException {

        double maxX = 0;
        double maxY = 0;

        for(Polygon p : polygons){

            if(p != null){

                BoundingBox b = p.boundingBox();

                maxX = Math.max(maxX, b.x() + b.width());
                maxY = Math.max(maxY, b.y() + b.height());
            }
        }

        FileWriter f = new FileWriter(path);

        f.write("<svg width=\"" + maxX + "\" height=\"" + maxY + "\" xmlns=\"http://www.w3.org/2000/svg\">\n");

        f.write(toSvg());

        f.write("</svg>");

        f.close();
    }
}