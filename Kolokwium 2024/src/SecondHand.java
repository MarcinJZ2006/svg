import java.time.LocalTime;

/**
 * Wskazówka sekundowa – skokowa
 */
public class SecondHand extends ClockHand {

    @Override
    public void setTime(LocalTime time) {
        this.angle = time.getSecond() * 6.0; // 360/60
    }

    @Override
    public String toSvg() {
        return "<line x1='50' y1='50' x2='50' y2='10' stroke='red' transform='rotate(" + angle + " 50 50)'/>";
    }
}