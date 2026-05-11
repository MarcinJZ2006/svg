import java.time.LocalTime;

/**
 * Wskazówka godzinowa – płynna
 */
public class HourHand extends ClockHand {

    @Override
    public void setTime(LocalTime time) {
        this.angle = (time.getHour() % 12) * 30.0 + time.getMinute() * 0.5;
    }

    @Override
    public String toSvg() {
        return "<line x1='50' y1='50' x2='50' y2='25' stroke='black' stroke-width='2' transform='rotate(" + angle + " 50 50)'/>";
    }
}