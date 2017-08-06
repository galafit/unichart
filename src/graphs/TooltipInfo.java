package graphs;

/**
 * Created by hdablin on 02.08.17.
 */
public class TooltipInfo {
    private String string;
    private Number x,y;

    public TooltipInfo(String string, Number x, Number y) {
        this.string = string;
        this.x = x;
        this.y = y;
    }

    public String getString() {
        return string;
    }

    public Number getX() {
        return x;
    }

    public Number getY() {
        return y;
    }
}
