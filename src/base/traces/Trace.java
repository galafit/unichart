package base.traces;

import base.DataSet;
import base.axis.Axis;
import base.Range;
import base.legend.LegendItem;
import base.tooltips.TooltipItem;

import java.awt.*;

/**
 * Created by galafit on 16/9/17.
 */
public abstract class Trace {
    private Axis xAxis;
    private Axis yAxis;
    private String name;
    private Color defaultColor;
    private int hoverIndex = -1;

    public Axis getXAxis() {
        return xAxis;
    }

    public void setXAxis(Axis xAxis) {
        this.xAxis = xAxis;
    }

    public Axis getYAxis() {
        return yAxis;
    }

    public void setYAxis(Axis yAxis) {
        this.yAxis = yAxis;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(Color defaultColor) {
        this.defaultColor = defaultColor;
    }

    public int getHoverIndex() {
        return hoverIndex;
    }

    /**
     *
     * @param hoverIndex
     * @return true if hover index was changed, false - otherwise
     */
    public boolean setHoverIndex(int hoverIndex) {
        boolean isIndexChanged = false;
        if(this.hoverIndex != hoverIndex) {
            isIndexChanged = true;
        }
        this.hoverIndex = hoverIndex;
        return isIndexChanged;
    }

    public abstract void setData(DataSet data);

    public abstract TooltipItem getTooltipItem();

    public abstract int findNearest(int mouseX, int mouseY);

    public abstract double getXPosition(int dataIndex);

    public abstract double getXValue(int dataIndex);

    public abstract LegendItem[] getLegendItems();

    public abstract Range getXExtremes();

    public abstract Range getYExtremes();

    public abstract void draw(Graphics2D g);
}
