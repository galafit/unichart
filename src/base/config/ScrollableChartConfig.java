package base.config;

import base.Range;

import java.awt.*;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * Created by galafit on 2/12/17.
 */
public class ScrollableChartConfig {
    public static final int DARK_THEME = 1;
    public static final int LIGHT_THEME = 2;

    private SimpleChartConfig chartConfig = new SimpleChartConfig();
    private SimpleChartConfig previewConfig = new SimpleChartConfig();
    private ScrollConfig scrollConfig = new ScrollConfig();
    private Map<Integer, Double> scrollExtents = new Hashtable<Integer, Double>(2);

    public ScrollableChartConfig(int theme) {
        if(theme == DARK_THEME) {
            Color bgColor = new Color(20, 20, 30);
            Color marginColor = new Color(20, 20, 20);
            Color textColor = new Color(200, 200, 200);
            Color legendBorderColor = new Color(50, 50, 50);
            Color legendBgColor = new Color(30, 30, 30);
            legendBgColor = bgColor;
            chartConfig.setBackground(bgColor);
            chartConfig.setMarginColor(marginColor);
            chartConfig.getLegendConfig().setBackground(legendBgColor);
            chartConfig.getLegendConfig().setBorderWidth(1);
            chartConfig.getLegendConfig().setBorderColor(legendBorderColor);
            chartConfig.getLegendConfig().getTextStyle().setFontColor(textColor);
            chartConfig.getTitleTextStyle().setFontColor(textColor);

            previewConfig.setBackground(bgColor);
            previewConfig.setMarginColor(marginColor);
            previewConfig.getLegendConfig().setBackground(legendBgColor);
            previewConfig.getLegendConfig().setBorderWidth(1);
            previewConfig.getLegendConfig().setBorderColor(legendBorderColor);
            previewConfig.getLegendConfig().getTextStyle().setFontColor(textColor);
            previewConfig.getTitleTextStyle().setFontColor(textColor);
        }
    }

    public SimpleChartConfig getChartConfig() {
        return chartConfig;
    }

    public SimpleChartConfig getPreviewConfig() {
        return previewConfig;
    }

    public void setPreviewMinMax(Range minMax) {
        previewConfig.setXAxisMinMax(0, minMax);
        previewConfig.setXAxisMinMax(1, minMax);
    }

    public void setChartConfig(SimpleChartConfig chartConfig) {
        this.chartConfig = chartConfig;
    }

    public void setPreviewConfig(SimpleChartConfig previewConfig) {
        this.previewConfig = previewConfig;
    }

    public ScrollConfig getScrollConfig() {
        return scrollConfig;
    }

    public double getScrollExtent(int xAxisIndex) {
        return scrollExtents.get(xAxisIndex);
    }

    public double[] getScrollsExtents() {
        double[] extents = new double[scrollExtents.keySet().size()];
        int i = 0;
        for (Integer xAxisIndex : scrollExtents.keySet()) {
            extents[i] = scrollExtents.get(xAxisIndex);
            i++;
        }
        return extents;
    }

    public void addScroll(int xAxisIndex, double extent) {
        scrollExtents.put(xAxisIndex, extent);
    }

    public Set<Integer> getXAxisWithScroll() {
        return scrollExtents.keySet();
    }
}