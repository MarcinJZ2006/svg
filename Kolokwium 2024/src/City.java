import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Miasto + dane strefowe + geografia
 */
public class City {

    private String name;
    private double lat;
    private double lon;
    private int timezoneOffset;

    public City(String name, double lat, double lon, int offset) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.timezoneOffset = offset;
    }

    public String getName() {
        return name;
    }

    public int getTimezoneOffset() {
        return timezoneOffset;
    }

    /**
     * Parsowanie CSV
     */
    public static Map<String, City> parseFile(Path path) {

        Map<String, City> map = new HashMap<>();

        try (BufferedReader br = Files.newBufferedReader(path)) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] p = line.split(";");

                String name = p[0];
                double lat = Double.parseDouble(p[1]);
                double lon = Double.parseDouble(p[2]);
                int offset = Integer.parseInt(p[3]);

                map.put(name, new City(name, lat, lon, offset));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return map;
    }

    /**
     * Lokalny czas z poprawką długości geograficznej
     */
    public double localMeanTime(double utcHour) {
        return utcHour + lon / 15.0;
    }

    /**
     * Komparator “najgorszego dopasowania strefy”
     */
    public static Comparator<City> worstTimezoneFit = (a, b) -> {
        return Double.compare(
                Math.abs(a.lon / 15.0 - a.timezoneOffset),
                Math.abs(b.lon / 15.0 - b.timezoneOffset)
        );
    };

    @Override
    public String toString() {
        return name;
    }

    /**
     * SVG generator dla zegarów
     */
    public static void generateAnalogClocksSvg(List<City> cities, AnalogClock clock) {

        File dir = new File(clock.toString());
        dir.mkdirs();

        for (City c : cities) {
            try (PrintWriter pw = new PrintWriter(new File(dir, c.name + ".svg"))) {

                clock.setCity(c);
                pw.println(clock.toSvg());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}