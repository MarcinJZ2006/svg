import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/*
 * Abstrakcyjna klasa bazowa dla wszystkich produktów.
 *
 * Zawiera:
 * - nazwę produktu,
 * - wspólną listę wszystkich wczytanych produktów,
 * - metody do wczytywania, wyszukiwania i czyszczenia tej listy.
 *
 * Dzięki temu FoodProduct i NonFoodProduct mogą być traktowane
 * w jednolity sposób, mimo że przechowują dane w trochę innej postaci.
 */
public abstract class Product {

    /*
     * Nazwa produktu.
     * Pole prywatne, aby było dostępne wyłącznie przez getter.
     */
    private String name;

    /*
     * Wspólna lista wszystkich produktów wczytanych z plików CSV.
     * Jest statyczna, ponieważ ma istnieć jedna taka lista dla całego programu.
     */
    private static List<Product> products = new ArrayList<>();

    /*
     * Konstruktor ustawiający nazwę produktu.
     */
    public Product(String name) {
        this.name = name;
    }

    /*
     * Akcesor do nazwy produktu.
     */
    public String getName() {
        return name;
    }

    /*
     * Abstrakcyjna metoda pobierająca cenę produktu w danym miesiącu.
     * Każda klasa dziedzicząca musi ją zdefiniować po swojemu.
     */
    public abstract double getPrice(int year, int month);

    /*
     * Czyści listę produktów.
     * Przydatne, gdy chcemy wczytać dane ponownie od zera.
     */
    public static void clearProducts() {
        products.clear();
    }

    /*
     * Wczytuje wszystkie produkty z podanego katalogu.
     *
     * Parametry:
     * - loader: metoda wytwórcza, np. FoodProduct::fromCsv albo NonFoodProduct::fromCsv
     * - directory: ścieżka do katalogu z plikami CSV
     *
     * Metoda:
     * - przechodzi po plikach CSV w katalogu,
     * - tworzy z nich obiekty produktu,
     * - dodaje je do wspólnej listy products.
     */
    public static void addProducts(Function<Path, Product> loader, Path directory) throws IOException {
        /*
         * DirectoryStream trzeba zamknąć, dlatego używamy try-with-resources.
         * Dodatkowo filtrujemy tylko pliki CSV, żeby nie próbować czytać innych plików.
         */
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            for (Path file : stream) {
                if (Files.isRegularFile(file) && file.toString().toLowerCase().endsWith(".csv")) {
                    Product product = loader.apply(file);
                    products.add(product);
                }
            }
        }
    }

    /*
     * Szuka produktu po prefiksie nazwy.
     *
     * Zasady:
     * - 0 pasujących produktów -> IndexOutOfBoundsException
     * - 1 pasujący produkt -> zwracamy go
     * - więcej niż 1 -> AmbigiousProductException z listą nazw
     */
    public static Product getProducts(String prefix) throws AmbigiousProductException {
        List<Product> found = new ArrayList<>();

        for (Product product : products) {
            if (product.getName().startsWith(prefix)) {
                found.add(product);
            }
        }

        if (found.isEmpty()) {
            throw new IndexOutOfBoundsException("Nie znaleziono produktu o prefiksie: " + prefix);
        }

        if (found.size() > 1) {
            List<String> names = new ArrayList<>();
            for (Product product : found) {
                names.add(product.getName());
            }
            throw new AmbigiousProductException(names);
        }

        return found.get(0);
    }
}