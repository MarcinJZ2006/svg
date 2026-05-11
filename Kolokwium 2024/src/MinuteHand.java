import java.time.LocalTime;

/**
 * Minutowa – płynna
 */
public class MinuteHand extends ClockHand {

    @Override
    public void setTime(LocalTime time) {
        this.angle = time.getMinute() * 6.0 + time.getSecond() * 0.1;
    }

    @Override
    public String toSvg() {
        return "<line x1='50' y1='50' x2='50' y2='15' stroke='black' transform='rotate(" + angle + " 50 50)'/>";
    }
}