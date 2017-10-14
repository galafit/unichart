package base.traces;

import base.DataSet;
import base.Range;
import base.config.traces.AreaTraceConfig;
import base.legend.LegendItem;
import base.tooltips.TooltipItem;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

/**
 * Created by galafit on 20/9/17.
 */
public class AreaTrace extends BaseTrace {

    public AreaTrace(AreaTraceConfig traceConfig, DataSet dataSet) {
        this.traceConfig = traceConfig;
        setData(dataSet);
    }

    public Color getFillColor() {
        return new Color(getLineColor().getRed(), getLineColor().getGreen(), getLineColor().getBlue(), 90);
    }

    @Override
    public void draw(Graphics2D g) {
        if (data == null || data.size() == 0) {
            return;
        }

        GeneralPath path = new GeneralPath();

        double x_0 = getXAxis().scale(data.getX(0));
        double y_0 = getYAxis().scale(data.getY(0));
        double x = x_0;
        double y = y_0;

        path.moveTo(x, y);
        g.setColor(getMarkColor());
        int pointRadius = traceConfig.getMarkConfig().getSize() / 2;
        g.draw(new Ellipse2D.Double(x - pointRadius,y - pointRadius, 2 * pointRadius,2 * pointRadius));
        for (int i = 1; i < data.size(); i++) {
            x = getXAxis().scale(data.getX(i));
            y = getYAxis().scale(data.getY(i));
            path.lineTo(x, y);
            g.draw(new Ellipse2D.Double(x - pointRadius,y - pointRadius, 2 * pointRadius,2 * pointRadius));
        }
        g.setColor(getLineColor());
        g.setStroke(traceConfig.getLineConfig().getStroke());
        g.draw(path);

        path.lineTo(x, getYAxis().getStart());
        path.lineTo(x_0, getYAxis().getStart());
        path.lineTo(x_0, y_0);
        g.setColor(getFillColor());
        g.fill(path);

        drawHover(g);
    }

}
