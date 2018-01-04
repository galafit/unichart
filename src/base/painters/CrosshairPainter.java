package base.painters;

import base.BCanvas;
import base.BRectangle;
import base.BStroke;
import base.config.CrosshairConfig;

/**
 * Created by galafit on 19/8/17.
 */
public class CrosshairPainter {
    private CrosshairConfig crosshairConfig;
    private int x, y;

    public CrosshairPainter(CrosshairConfig crosshairConfig) {
        this.crosshairConfig = crosshairConfig;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(BCanvas canvas, BRectangle area){
        canvas.setStroke(new BStroke(crosshairConfig.getLineWidth()));
        canvas.setColor(crosshairConfig.getLineColor());
        canvas.drawLine(x,area.y, x,area.y + area.height);
        canvas.drawLine(area.x, y, area.x + area.width,y);
    }
}
