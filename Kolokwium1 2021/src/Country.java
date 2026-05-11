import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/*
 * Abstrakcyjna klasa reprezentująca państwo.
 */
public abstract class Country {

    // Nazwa państwa
    private final String name;

    // Ścieżki do plików CSV
    private static String confirmedCasesFile;
    private static String deathsFile;

    /*
     * Konstruktor ustawiający nazwę państwa
     */
    public Country(String name) {
        this.name = name;
    }

    /*
     * Getter nazwy państwa
     */
    public String getName() {
        return name;
    }

    /*
     * Ustawienie plików CSV
     */
    public static void setFiles(String confirmedFile, String deathsFile)
            throws FileNotFoundException {

        File file1 = new File(confirmedFile);
        File file2 = new File(deathsFile);

        // Sprawdzenie pierwszego pliku
        if (!file1.exists() || !file1.canRead()) {
            throw new FileNotFoundException(confirmedFile);
        }

        // Sprawdzenie drugiego pliku
        if (!file2.exists() || !file2.canRead()) {
            throw new FileNotFoundException(deathsFile);
        }

        confirmedCasesFile = confirmedFile;
        Country.deathsFile = deathsFile;
    }

    /*
     * Klasa pomocnicza przechowująca:
     * - indeks pierwszej kolumny kraju
     * - liczbę kolumn kraju
     */
    private static class CountryColumns {

        public final int firstColumnIndex;
        public final int columnCount;

        public CountryColumns(int firstColumnIndex, int columnCount) {
            this.firstColumnIndex = firstColumnIndex;
            this.columnCount = columnCount;
        }
    }

    /*
     * Szuka kraju w pierwszym wierszu CSV
     */
    private static CountryColumns getCountryColumns(
            String firstLine,
            String searchedCountry)
            throws CountryNotFoundException {

        String[] columns = firstLine.split(";");

        int firstIndex = -1;
        int count = 0;

        // Szukamy wszystkich kolumn danego państwa
        for (int i = 1; i < columns.length; i++) {

            if (columns[i].equals(searchedCountry)) {

                if (firstIndex == -1) {
                    firstIndex = i;
                }

                count++;
            }
        }

        // Jeśli nie znaleziono kraju
        if (firstIndex == -1) {
            throw new CountryNotFoundException(searchedCountry);
        }

        return new CountryColumns(firstIndex, count);
    }

    /*
     * Tworzy obiekt Country na podstawie CSV
     */
    public static Country fromCsv(String countryName)
            throws IOException, CountryNotFoundException {

        BufferedReader confirmedReader =
                new BufferedReader(new FileReader(confirmedCasesFile));

        BufferedReader deathsReader =
                new BufferedReader(new FileReader(deathsFile));

        // Pierwsze linie - nazwy państw
        String confirmedFirstLine = confirmedReader.readLine();
        String deathsFirstLine = deathsReader.readLine();

        // Drugie linie - prowincje
        String confirmedSecondLine = confirmedReader.readLine();
        String deathsSecondLine = deathsReader.readLine();

        // Szukamy kolumn kraju
        CountryColumns columns =
                getCountryColumns(confirmedFirstLine, countryName);

        Country result;

        // Jeśli kraj ma jedną kolumnę i "nan"
        if (columns.columnCount == 1) {

            String[] provinces =
                    confirmedSecondLine.split(";");

            String province =
                    provinces[columns.firstColumnIndex];

            // Brak prowincji
            if (province.equals("nan")) {

                result =
                        new CountryWithoutProvinces(countryName);
            }
            else {

                // Jedna prowincja też traktowana jako kraj z prowincjami
                Country[] provinceArray = new Country[1];

                provinceArray[0] =
                        new CountryWithoutProvinces(province);

                result =
                        new CountryWithProvinces(
                                countryName,
                                provinceArray
                        );
            }
        }
        else {

            // Kraj posiada prowincje
            String[] provinces =
                    confirmedSecondLine.split(";");

            Country[] provinceArray =
                    new Country[columns.columnCount];

            for (int i = 0; i < columns.columnCount; i++) {

                String provinceName =
                        provinces[columns.firstColumnIndex + i];

                provinceArray[i] =
                        new CountryWithoutProvinces(provinceName);
            }

            result =
                    new CountryWithProvinces(
                            countryName,
                            provinceArray
                    );
        }

        String confirmedLine;
        String deathsLine;

        // Format daty z pliku CSV
        DateTimeFormatter inputFormatter =
                DateTimeFormatter.ofPattern("M/d/yy");

        // Czytamy dane dzień po dniu
        while ((confirmedLine = confirmedReader.readLine()) != null
                && (deathsLine = deathsReader.readLine()) != null) {

            String[] confirmedParts =
                    confirmedLine.split(";");

            String[] deathsParts =
                    deathsLine.split(";");

            LocalDate date =
                    LocalDate.parse(
                            confirmedParts[0],
                            inputFormatter
                    );

            // Kraj bez prowincji
            if (result instanceof CountryWithoutProvinces) {

                int confirmed =
                        Integer.parseInt(
                                confirmedParts[columns.firstColumnIndex]
                        );

                int deaths =
                        Integer.parseInt(
                                deathsParts[columns.firstColumnIndex]
                        );

                ((CountryWithoutProvinces) result)
                        .addDailyStatistic(
                                date,
                                confirmed,
                                deaths
                        );
            }
            else {

                // Kraj z prowincjami
                CountryWithProvinces countryWithProvinces =
                        (CountryWithProvinces) result;

                Country[] provinces =
                        countryWithProvinces.getProvinces();

                for (int i = 0; i < provinces.length; i++) {

                    int confirmed =
                            Integer.parseInt(
                                    confirmedParts[
                                            columns.firstColumnIndex + i
                                            ]
                            );

                    int deaths =
                            Integer.parseInt(
                                    deathsParts[
                                            columns.firstColumnIndex + i
                                            ]
                            );

                    ((CountryWithoutProvinces) provinces[i])
                            .addDailyStatistic(
                                    date,
                                    confirmed,
                                    deaths
                            );
                }
            }
        }

        confirmedReader.close();
        deathsReader.close();

        return result;
    }

    /*
     * Wersja przeciążona - tablica nazw krajów
     */
    public static Country[] fromCsv(String[] countryNames)
            throws IOException {

        ArrayList<Country> countries =
                new ArrayList<>();

        for (String name : countryNames) {

            try {

                countries.add(fromCsv(name));

            } catch (CountryNotFoundException e) {

                // Wypisujemy nazwę brakującego państwa
                System.out.println(e.getMessage());
            }
        }

        return countries.toArray(new Country[0]);
    }

    /*
     * Liczba zakażeń danego dnia
     */
    public abstract int getConfirmedCases(LocalDate date);

    /*
     * Liczba zgonów danego dnia
     */
    public abstract int getDeaths(LocalDate date);

    /*
     * Sortowanie malejąco po liczbie zgonów
     */
    public static void sortByDeaths(
            Country[] countries,
            LocalDate startDate,
            LocalDate endDate
    ) {

        Arrays.sort(countries, (a, b) -> {

            int deathsA = 0;
            int deathsB = 0;

            LocalDate current = startDate;

            while (!current.isAfter(endDate)) {

                deathsA += a.getDeaths(current);
                deathsB += b.getDeaths(current);

                current = current.plusDays(1);
            }

            return Integer.compare(deathsB, deathsA);
        });
    }

    /*
     * Zapis statystyk do pliku
     */
    public void saveToDataFile(String path)
            throws IOException {

        BufferedWriter writer =
                new BufferedWriter(new FileWriter(path));

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("d.MM.yy");

        // Zakładamy że dane są dostępne od:
        LocalDate current =
                LocalDate.of(2020, 1, 22);

        // Przykładowy zakres końcowy
        LocalDate end =
                LocalDate.of(2021, 12, 31);

        while (!current.isAfter(end)) {

            writer.write(
                    current.format(formatter)
                            + "\t"
                            + getConfirmedCases(current)
                            + "\t"
                            + getDeaths(current)
            );

            writer.newLine();

            current = current.plusDays(1);
        }

        writer.close();
    }
}