import java.time.LocalTime;

/**
 * Abstrakcyjna klasa bazowa dla wszystkich zegarów.
 * Trzyma czas w postaci godzin, minut i sekund.
 */
public abstract class Clock {

    // ===== STAN ZEGARA =====
    protected int hour;
    protected int minute;
    protected int second;

    // Miasto przypisane do zegara (strefy czasowe)
    protected City city;

    /**
     * Konstruktor zegara – przypisujemy miasto i ustawiamy czas na systemowy
     */
    public Clock(City city) {
        this.city = city;
        setCurrentTime();
    }

    /**
     * Ustawia czas zegara na aktualny czas systemowy
     */
    public void setCurrentTime() {
        LocalTime now = LocalTime.now();
        this.hour = now.getHour();
        this.minute = now.getMinute();
        this.second = now.getSecond();
    }

    /**
     * Ustawia konkretny czas ręcznie.
     * Waliduje poprawność danych (24h clock).
     */
    public void setTime(int h, int m, int s) {

        // walidacja godzin
        if (h < 0 || h > 23)
            throw new IllegalArgumentException("Godzina poza zakresem 0-23");

        // walidacja minut
        if (m < 0 || m > 59)
            throw new IllegalArgumentException("Minuta poza zakresem 0-59");

        // walidacja sekund
        if (s < 0 || s > 59)
            throw new IllegalArgumentException("Sekunda poza zakresem 0-59");

        this.hour = h;
        this.minute = m;
        this.second = s;
    }

    /**
     * Zmiana miasta = zmiana strefy czasowej
     * (realnie przesuwamy godzinę o offset miasta)
     */
    public void setCity(City newCity) {
        if (this.city != null) {
            int diff = newCity.getTimezoneOffset() - this.city.getTimezoneOffset();
            this.hour = (this.hour + diff + 24) % 24;
        }
        this.city = newCity;
    }

    /**
     * Format HH:MM:SS
     */
    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }
}