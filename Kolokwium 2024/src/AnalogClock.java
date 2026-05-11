import java.time.LocalTime;
import java.util.List;

/**
 * Zegar analogowy SVG
 */
public class AnalogClock extends Clock {

    private final List<ClockHand> hands;

    public AnalogClock(City city) {
        super(city);

        // jedna instancja każdej wskazówki
        hands = List.of(
                new SecondHand(),
                new MinuteHand(),
                new HourHand()
        );
    }

    /**
     * Rysowanie zegara w SVG
     */
    public String toSvg() {

        LocalTime t = LocalTime.of(hour, minute, second);

        for (ClockHand h : hands) {
            h.setTime(t);
        }

        StringBuilder sb = new StringBuilder();

        sb.append("<svg width='100' height='100'>");

        // tarcza
        sb.append("<circle cx='50' cy='50' r='45' stroke='black' fill='white'/>");

        // wskazówki
        for (ClockHand h : hands) {
            sb.append(h.toSvg());
        }

        sb.append("</svg>");

        return sb.toString();
    }

    @Override
    public String toString() {
        return "AnalogClock";
    }
}