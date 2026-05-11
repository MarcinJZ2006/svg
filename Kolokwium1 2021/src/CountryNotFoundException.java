/*
 * Wyjątek rzucany gdy państwo nie istnieje w CSV
 */
public class CountryNotFoundException extends Exception {

    private String countryName;

    public CountryNotFoundException(String countryName) {

        this.countryName = countryName;
    }

    @Override
    public String getMessage() {

        return countryName;
    }
}