import java.time.LocalTime;

/**
 * Wskazówka zegara (abstrakcja)
 */
public abstract class ClockHand {

    protected double angle;

    public abstract void setTime(LocalTime time);

    public abstract String toSvg();
}