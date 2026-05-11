/*
 * Zasób na mapie.
 *
 * Ma pozycję i typ.
 */
public class Resource {

    public enum Type {
        Coal,
        Wood,
        Fish
    }

    public final Point point;
    public final Type type;

    public Resource(Point point, Type type) {
        this.point = point;
        this.type = type;
    }
}