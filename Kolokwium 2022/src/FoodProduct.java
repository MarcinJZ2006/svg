import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Produkt żywnościowy.
 *
 * W tym typie produktu ceny są rozróżniane według województwa.
 * Dlatego przechowujemy mapę:
 * województwo -> tablica cen miesięcznych
 */
public class FoodProduct extends Product {

    /*
     * Mapa cen:
     * klucz   -> nazwa województwa
     * wartość -> ceny w kolejnych miesiącach
     */
    private Map<String, Double[]> prices;

    /*
     * Prywatny konstruktor.
     * Obiekty tworzymy wyłącznie przez fromCsv().
     */
    private FoodProduct(String name, Map<String, Double[]> prices) {
        super(name);
        this.prices = prices;
    }

    /*
     * Wczytuje produkt żywnościowy z pliku CSV.
     *
     * Struktura:
     * - pierwsza linia: nazwa produktu
     * - druga linia: nagłówek
     * - kolejne linie: województwo + ceny miesięczne
     *
     * Ceny mogą być zapisane z przecinkiem, więc zamieniamy go na kropkę.
     */
    public static FoodProduct fromCsv(Path path) {
        try {
            List<String> lines = Files.readAllLines(path);

            String name = lines.get(0).trim();
            Map<String, Double[]> prices = new HashMap<>();

            for (int i = 2; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(";");

                String province = parts[0].trim();

                /*
                 * W każdej linii:
                 * parts[0] -> województwo
                 * parts[1..] -> ceny miesięczne
                 */
                Double[] provincePrices = new Double[parts.length - 1];

                for (int j = 1; j < parts.length; j++) {
                    String priceText = parts[j].trim().replace(",", ".");
                    provincePrices[j - 1] = Double.parseDouble(priceText);
                }

                prices.put(province, provincePrices);
            }

            return new FoodProduct(name, prices);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Zwraca cenę w podanym województwie.
     * Jeżeli województwo nie istnieje albo data jest poza zakresem,
     * rzucany jest IndexOutOfBoundsException.
     */
    public double getPrice(int year, int month, String province) {
        validateDate(year, month);

        if (!prices.containsKey(province)) {
            throw new IndexOutOfBoundsException("Nieznane województwo: " + province);
        }

        int index = (year - 2010) * 12 + (month - 1);
        return prices.get(province)[index];
    }

    /*
     * Nadpisanie metody z klasy bazowej.
     * Zwraca średnią arytmetyczną cen ze wszystkich województw.
     */
    @Override
    public double getPrice(int year, int month) {
        validateDate(year, month);

        int index = (year - 2010) * 12 + (month - 1);

        double sum = 0.0;

        for (Double[] provincePrices : prices.values()) {
            sum += provincePrices[index];
        }

        return sum / prices.size();
    }

    /*
     * Wspólne sprawdzanie poprawności daty.
     */
    private void validateDate(int year, int month) {
        if (month < 1 || month > 12) {
            throw new IndexOutOfBoundsException("Niepoprawny miesiąc: " + month);
        }

        if (year < 2010 || year > 2022) {
            throw new IndexOutOfBoundsException("Niepoprawny rok: " + year);
        }

        if (year == 2022 && month > 3) {
            throw new IndexOutOfBoundsException("Brak danych dla: " + month + "." + year);
        }
    }
}