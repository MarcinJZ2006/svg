import java.util.HashMap;
import java.util.Map;

/*
 * Koszyk zakupów.
 *
 * Przechowuje:
 * - produkt
 * - liczbę sztuk danego produktu
 *
 * Dzięki temu można policzyć wartość koszyka w dowolnym miesiącu.
 */
public class Cart {

    /*
     * Mapa:
     * produkt -> liczba sztuk
     */
    private Map<Product, Integer> products = new HashMap<>();

    /*
     * Dodaje produkt do koszyka.
     *
     * Jeżeli ten sam produkt został już wcześniej dodany,
     * zwiększamy jego ilość zamiast nadpisywać.
     */
    public void addProduct(Product product, int amount) {
        products.put(product, products.getOrDefault(product, 0) + amount);
    }

    /*
     * Zwraca wartość koszyka dla konkretnego roku i miesiąca.
     */
    public double getPrice(int year, int month) {
        double sum = 0.0;

        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            sum += entry.getKey().getPrice(year, month) * entry.getValue();
        }

        return sum;
    }

    /*
     * Liczy inflację roczną między dwoma miesiącami.
     *
     * Wzór:
     * (price2 - price1) / price1 * 100 / months * 12
     *
     * gdzie months to liczba miesięcy pomiędzy datami.
     */
    public double getInflation(int year1, int month1, int year2, int month2) {
        double price1 = getPrice(year1, month1);
        double price2 = getPrice(year2, month2);

        int months = (year2 - year1) * 12 + (month2 - month1);

        return ((price2 - price1) / price1) * 100.0 / months * 12.0;
    }
}