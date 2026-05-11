import java.time.LocalDate;

/*
 * Państwo posiadające prowincje
 */
public class CountryWithProvinces extends Country {

    /*
     * Tablica prowincji
     */
    private Country[] provinces;

    public CountryWithProvinces(
            String name,
            Country[] provinces
    ) {

        super(name);

        this.provinces = provinces;
    }

    /*
     * Getter prowincji
     */
    public Country[] getProvinces() {
        return provinces;
    }

    @Override
    public int getConfirmedCases(LocalDate date) {

        int sum = 0;

        // Rekurencyjne sumowanie prowincji
        for (Country province : provinces) {

            sum += province.getConfirmedCases(date);
        }

        return sum;
    }

    @Override
    public int getDeaths(LocalDate date) {

        int sum = 0;

        // Rekurencyjne sumowanie prowincji
        for (Country province : provinces) {

            sum += province.getDeaths(date);
        }

        return sum;
    }
}