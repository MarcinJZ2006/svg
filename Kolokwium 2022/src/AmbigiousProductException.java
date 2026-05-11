import java.util.List;

/*
 * Wyjątek rzucany wtedy, gdy prefiks nazwy pasuje do więcej niż jednego produktu.
 *
 * W treści zadania nazwa jest zapisana jako "AmbigiousProductException",
 * więc zostawiamy dokładnie taką nazwę, mimo że po angielsku częściej spotyka się
 * "Ambiguous".
 */
public class AmbigiousProductException extends Exception {

    /*
     * Konstruktor dostaje listę nazw pasujących produktów.
     * Zamieniamy ją na tekst i przekazujemy do super(...),
     * dzięki czemu pojawi się w stack trace.
     */
    public AmbigiousProductException(List<String> products) {
        super(products.toString());
    }
}