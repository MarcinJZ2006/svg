import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/*
 * Państwo bez prowincji
 */
public class CountryWithoutProvinces extends Country {

    /*
     * Klasa przechowująca statystyki jednego dnia
     */
    private static class DailyStatistic {

        int confirmedCases;
        int deaths;

        DailyStatistic(int confirmedCases, int deaths) {
            this.confirmedCases = confirmedCases;
            this.deaths = deaths;
        }
    }

    /*
     * Mapa:
     * data -> statystyki
     */
    private Map<LocalDate, DailyStatistic> statistics;

    public CountryWithoutProvinces(String name) {

        super(name);

        statistics = new HashMap<>();
    }

    /*
     * Dodaje statystyki dnia
     */
    public void addDailyStatistic(
            LocalDate date,
            int confirmedCases,
            int deaths
    ) {

        statistics.put(
                date,
                new DailyStatistic(
                        confirmedCases,
                        deaths
                )
        );
    }

    @Override
    public int getConfirmedCases(LocalDate date) {

        return statistics.get(date).confirmedCases;
    }

    @Override
    public int getDeaths(LocalDate date) {

        return statistics.get(date).deaths;
    }
}