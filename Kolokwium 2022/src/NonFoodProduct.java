import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/*
 * Produkt nieżywnościowy.
 *
 * Dla tego typu produktu:
 * - w CSV w pierwszej kolumnie każdej linii danych znajduje się cena,
 * - nie ma podziału na województwa,
 * - ceny są przechowywane jako tablica kolejnych miesięcy.
 */
public class NonFoodProduct extends Product {

    /*
     * Tablica cen dla kolejnych miesięcy od 01.2010.
     * indeks 0 -> 01.2010
     * indeks 1 -> 02.2010
     * itd.
     */
    private Double[] prices;

    /*
     * Prywatny konstruktor.
     * Obiekty tworzymy wyłącznie przez fromCsv().
     */
    private NonFoodProduct(String name, Double[] prices) {
        super(name);
        this.prices = prices;
    }

    /*
     * Wczytuje produkt z pliku CSV.
     *
     * Struktura pliku:
     * - pierwsza linia: nazwa produktu
     * - druga linia: nagłówek
     * - dalsze linie: ceny miesięczne
     *
     * W danych ceny mogą być zapisane z przecinkiem,
     * dlatego przed parsowaniem zamieniamy przecinek na kropkę.
     */
    public static NonFoodProduct fromCsv(Path path) {
        try {
            List<String> lines = Files.readAllLines(path);

            String name = lines.get(0).trim();

            /*
             * Liczba danych miesięcznych = wszystkie linie poza:
             * - nazwą produktu
             * - nagłówkiem tabeli
             */
            Double[] prices = new Double[lines.size() - 2];

            for (int i = 2; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(";");
                String priceText = parts[0].trim().replace(",", ".");
                prices[i - 2] = Double.parseDouble(priceText);
            }

            return new NonFoodProduct(name, prices);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Zwraca cenę produktu w danym miesiącu.
     *
     * Zakres danych:
     * 01.2010 - 03.2022
     *
     * Jeżeli:
     * - miesiąc jest spoza 1..12
     * - rok jest poza zakresem
     * - data wykracza poza dostępne dane
     * to rzucamy IndexOutOfBoundsException.
     */
    @Override
    public double getPrice(int year, int month) {
        validateDate(year, month);

        int index = (year - 2010) * 12 + (month - 1);
        return prices[index];
    }

    /*
     * Sprawdza poprawność zakresu dat.
     */
    private void validateDate(int year, int month) {
        if (month < 1 || month > 12) {
            throw new IndexOutOfBoundsException("Niepoprawny miesiąc: " + month);
        }

        if (year < 2010 || year > 2022) {
            throw new IndexOutOfBoundsException("Niepoprawny rok: " + year);
        }

        /*
         * Dane kończą się na marcu 2022.
         */
        if (year == 2022 && month > 3) {
            throw new IndexOutOfBoundsException("Brak danych dla: " + month + "." + year);
        }

        /*
         * Dane zaczynają się od stycznia 2010.
         */
        if (year == 2010 && month < 1) {
            throw new IndexOutOfBoundsException("Brak danych dla: " + month + "." + year);
        }
    }
}