import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/*
 * Ląd jest wielokątem.
 */
public class Land extends Polygon {

    /*
     * Prywatna lista miast przypisanych do lądu.
     */
    private final List<City> cities = new ArrayList<>();

    public Land(List<Point> points) {
        super(points);
    }

    /*
     * Dodaje miasto tylko wtedy, gdy jego środek leży na tym lądzie.
     * W przeciwnym razie rzuca RuntimeException z nazwą miasta.
     */
    public void addCity(City city) {
        if (!inside(city.center)) {
            throw new RuntimeException(city.getName());
        }

        city.updatePortStatus(this);
        cities.add(city);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ");

        for (City city : cities) {
            joiner.add(city.toString());
        }

        return joiner.toString();
    }

    public List<City> getCities() {
        return List.copyOf(cities);
    }
}