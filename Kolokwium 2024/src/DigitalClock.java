/**
 * Zegar cyfrowy 12h / 24h
 */
public class DigitalClock extends Clock {

    public enum Mode {
        H24, H12
    }

    private Mode mode;

    public DigitalClock(City city, Mode mode) {
        super(city);
        this.mode = mode;
    }

    @Override
    public String toString() {

        if (mode == Mode.H24) {
            return super.toString();
        }

        // ===== 12H FORMAT =====
        int h = hour % 12;
        if (h == 0) h = 12;

        String suffix = (hour < 12) ? "AM" : "PM";

        return String.format("%d:%02d:%02d %s", h, minute, second, suffix);
    }
}