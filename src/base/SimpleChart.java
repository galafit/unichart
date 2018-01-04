package base;

import base.axis.Axis;
import base.button.ToggleBtn;
import base.button.BtnGroup;
import base.button.StateListener;
import base.config.SimpleChartConfig;
import base.config.general.Margin;
import base.config.traces.TraceConfig;
import base.painters.CrosshairPainter;
import base.painters.Legend;
import base.painters.TitlePainter;
import base.painters.TooltipPainter;
import base.scales.Scale;
import base.tooltips.TooltipInfo;
import base.traces.Trace;
import base.traces.TraceRegister;

import java.util.*;
import java.util.List;

/**
 * Created by hdablin on 24.03.17.
 */
public class SimpleChart {
    private BColor[] traceColors = {BColor.CYAN, BColor.MAGENTA, BColor.PINK, BColor.RED, BColor.ORANGE};

    private List<Axis> xAxisList = new ArrayList<Axis>(2);
    private List<Axis> yAxisList = new ArrayList<Axis>();
    private List<Trace> traces = new ArrayList<Trace>();
    private boolean isTicksAlignmentEnable = false;

    private List<Legend> legends = new ArrayList<Legend>();
    private TitlePainter titlePainter;
    private BRectangle fullArea;
    private BRectangle titleArea;
    private BRectangle chartArea;
    private BRectangle graphArea;
    private SimpleChartConfig chartConfig;
    private Margin margin;

    private boolean isDirty = true;


    private CrosshairPainter crosshairPainter;
    private TooltipPainter tooltipPainter;

    private int selectedTraceIndex = -1;
    private int hoverPointIndex = -1;
    private int hoverTraceIndex = -1;

    private List<DataSet> data;


    public SimpleChart(SimpleChartConfig chartConfig, List<DataSet> data, BRectangle area) {
        this.data = data;
        this.chartConfig = chartConfig;
        this.fullArea = area;
        for (int i = 0; i < chartConfig.getNumberOfXAxis(); i++) {
            xAxisList.add(new Axis(chartConfig.getXConfig(i)));
        }
        for (int i = 0; i < chartConfig.getNumberOfYAxis(); i++) {
            yAxisList.add(new Axis(chartConfig.getYConfig(i)));
        }
        for (int i = 0; i < chartConfig.getTraceCounter(); i++) {
            TraceConfig traceConfig = chartConfig.getTraceConfig(i);
            Trace trace = TraceRegister.getTrace(traceConfig, data.get(chartConfig.getTraceDataIndex(i)));
            trace.setXAxis(xAxisList.get(chartConfig.getTraceXIndex(i)));
            trace.setYAxis(yAxisList.get(chartConfig.getTraceYIndex(i)));
            trace.setDefaultColor(traceColors[traces.size() % traceColors.length]);
            trace.setName(chartConfig.getTraceName(i));
            traces.add(trace);
        }
        tooltipPainter = new TooltipPainter(chartConfig.getTooltipConfig());
        crosshairPainter = new CrosshairPainter(chartConfig.getCrosshairConfig());
        titlePainter = new TitlePainter(chartConfig.getTitle(), chartConfig.getTitleTextStyle(), chartConfig.getTitleColor());

        BtnGroup buttonGroup = new BtnGroup();
        for (int i = 0; i < yAxisList.size() / 2; i++) {
            legends.add(new Legend(chartConfig.getLegendConfig(), buttonGroup));
        }

        for (int i = 0; i < traces.size(); i++) {
            int stackIndex = getTraceYIndex(i) / 2;
            ToggleBtn legendButton = new ToggleBtn(traces.get(i).getDefaultColor(), traces.get(i).getName());
            final int traceIndex = i;
            legendButton.addListener(new StateListener() {
                @Override
                public void stateChanged(boolean isSelected) {
                    if(isSelected) {
                        selectedTraceIndex = traceIndex;
                    }
                    if(!isSelected && selectedTraceIndex == traceIndex) {
                        selectedTraceIndex = -1;
                    }
                }
            });
            legends.get(stackIndex).add(legendButton);
        }

        // set min and max for x axis
        for (int i = 0; i < xAxisList.size(); i++) {
            Range minMax = chartConfig.getXMinMax(i);
            if (minMax != null) {
                xAxisList.get(i).setMinMax(minMax);

            } else {
                autoScaleX(i);
            }
        }
        // set min and max for y axis
        for (int i = 0; i < yAxisList.size(); i++) {
            Range minMax = chartConfig.getYMinMax(i);
            if (minMax != null) {
                yAxisList.get(i).setMinMax(minMax);

            } else {
                autoScaleY(i);
            }
        }
    }



    Margin getMargin(BCanvas canvas) {
        if (margin == null) {
            calculateMarginsAndAreas(canvas, chartConfig.getMargin());
        }
        return margin;
    }

    BRectangle getGraphArea(BCanvas canvas) {
        if (graphArea == null) {
            calculateMarginsAndAreas(canvas, chartConfig.getMargin());
        }
        return graphArea;
    }

    void setMargin(BCanvas canvas, Margin margin) {
        calculateMarginsAndAreas(canvas, margin);
    }

    Scale getXAxisScale(int xAxisIndex) {
        return xAxisList.get(xAxisIndex).getScale();
    }


    void calculateMarginsAndAreas(BCanvas canvas, Margin margin) {
        int titleHeight = titlePainter.getTitleHeight(canvas, fullArea.width);
        titleArea = new BRectangle(fullArea.x, fullArea.y, fullArea.width, titleHeight);
        chartArea = new BRectangle(fullArea.x, fullArea.y + titleHeight, fullArea.width, fullArea.height - titleHeight);
        int left = -1;
        int right = -1;
        int bottom = -1;
        int top = -1;
        if (margin != null) {
            top = margin.top();
            bottom = margin.bottom();
            left = margin.left();
            right = margin.right();
        }

        // set XAxis ranges
        int xStart = fullArea.x;
        int xEnd = fullArea.x + fullArea.width;
        xAxisList.get(0).setStartEnd(xStart, xEnd);
        xAxisList.get(1).setStartEnd(xStart, xEnd);
        if (top < 0) {
            top = titleHeight + xAxisList.get(1).getThickness(canvas);

        }
        if (bottom < 0) {
            bottom = xAxisList.get(0).getThickness(canvas);
        }

        // set YAxis ranges
        BRectangle paintingArea = new BRectangle(fullArea.x, fullArea.y + top, fullArea.width, fullArea.height - top - bottom);
        for (int i = 0; i < yAxisList.size(); i++) {
            yAxisList.get(i).setStartEnd(chartConfig.getYStartEnd(i, paintingArea));
        }
        if (left < 0) {
            for (int i = 0; i < yAxisList.size() / 2; i++) {
                left = Math.max(left, yAxisList.get(i * 2).getThickness(canvas));
            }
        }
        if (right < 0) {
            for (int i = 0; i < yAxisList.size() / 2; i++) {
                right = Math.max(right, yAxisList.get(i * 2 + 1).getThickness(canvas));
            }
        }

        this.margin = new Margin(top, right, bottom, left);
        graphArea = new BRectangle(fullArea.x + left, fullArea.y + top,
                fullArea.width - left - right, fullArea.height - top - bottom);

        for (int stackIndex = 0; stackIndex < legends.size(); stackIndex++) {
            int legendAreaYStart = yAxisList.get(2 * stackIndex).getEnd();
            int legendAreaYEnd = yAxisList.get(2 * stackIndex).getStart();
            BRectangle legendArea = new BRectangle(graphArea.x, legendAreaYStart, graphArea.width, legendAreaYEnd - legendAreaYStart);
            legends.get(stackIndex).setArea(legendArea);
        }

        // adjust XAxis ranges
        xStart = graphArea.x;
        xEnd = graphArea.x + graphArea.width;
        xAxisList.get(0).setStartEnd(xStart, xEnd);
        xAxisList.get(1).setStartEnd(xStart, xEnd);
        isDirty = false;
    }


    public void draw(BCanvas canvas) {
        if (isDirty) {
            calculateMarginsAndAreas(canvas, chartConfig.getMargin());
        }

        canvas.setColor(chartConfig.getMarginColor());
        canvas.fillRect(fullArea.x, fullArea.y, fullArea.width, fullArea.height);

        canvas.setColor(chartConfig.getBackground());
        canvas.fillRect(graphArea.x, graphArea.y, graphArea.width, graphArea.height);

        canvas.enableAntiAliasAndHinting();

        int topPosition = graphArea.y;
        int bottomPosition = graphArea.y + graphArea.height;
        int leftPosition = graphArea.x;
        int rightPosition = graphArea.x + graphArea.width;

        for (int i = 0; i < xAxisList.size() / 2; i++) {
            xAxisList.get(i * 2).drawGrid(canvas, bottomPosition, graphArea.height);
            xAxisList.get(i * 2 + 1).drawGrid(canvas, topPosition, graphArea.height);
        }

        for (int i = 0; i < yAxisList.size() / 2; i++) {
            yAxisList.get(i * 2).drawGrid(canvas, leftPosition, graphArea.width);
            yAxisList.get(i * 2 + 1).drawGrid(canvas, rightPosition, graphArea.width);
        }

        for (int i = 0; i < xAxisList.size() / 2; i++) {
            xAxisList.get(i * 2).drawAxis(canvas, bottomPosition);
            xAxisList.get(i * 2 + 1).drawAxis(canvas, topPosition);
        }

        for (int i = 0; i < yAxisList.size() / 2; i++) {
            yAxisList.get(i * 2).drawAxis(canvas, leftPosition);
            yAxisList.get(i * 2 + 1).drawAxis(canvas, rightPosition);
        }

        canvas.save();
        canvas.setClip(graphArea.x, graphArea.y, graphArea.width, graphArea.height);

        for (Trace trace : traces) {
            trace.draw(canvas);
        }
        canvas.restore();

        titlePainter.draw(canvas, titleArea);
        for (Legend legend : legends) {
            legend.draw(canvas);
        }

        if (hoverTraceIndex >= 0 && hoverPointIndex >= 0) {
            crosshairPainter.draw(canvas, graphArea);
            tooltipPainter.draw(canvas, fullArea);
        }
    }

    /**
     * =======================Base methods to interact==========================
     **/

    public void setArea(BRectangle area) {
        fullArea = area;
        margin = null;
        isDirty = true;
    }

    public void setData(List<DataSet> data) {
        this.data = data;
        for (int i = 0; i < traces.size(); i++) {
            traces.get(i).setData(data.get(chartConfig.getTraceDataIndex(i)));
        }
    }

    public int getTraceCounter() {
        return traces.size();
    }

    public boolean selectTrace(int x, int y) {
        for (Legend legend : legends) {
            if(legend.toggle(x, y)) {
                return true;
            }
        }
        return false;
    }

    public boolean selectTrace(int traceIndex) {
        if(selectedTraceIndex != traceIndex) {
            selectedTraceIndex = traceIndex;
            return true;
        }
        return false;
    }

    public int getSelectedTraceIndex() {
        return selectedTraceIndex;
    }


    public int getXAxisCounter() {
        return xAxisList.size();
    }

    public int getYAxisCounter() {
        return yAxisList.size();
    }

    public void setXMinMax(int xAxisIndex, Range minMax) {
        xAxisList.get(xAxisIndex).setMinMax(minMax);
    }

    public Range getXMinMax(int xAxisIndex) {
        return new Range(xAxisList.get(xAxisIndex).getMin(), xAxisList.get(xAxisIndex).getMax());
    }

    public Range getYMinMax(int yAxisIndex) {
        return new Range(yAxisList.get(yAxisIndex).getMin(), yAxisList.get(yAxisIndex).getMax());
    }

    public void setYMinMax(int yAxisIndex, Range minMax) {
        yAxisList.get(yAxisIndex).setMinMax(minMax);
    }


    public Range getYStartEnd(int yAxisIndex) {
        return new Range(yAxisList.get(yAxisIndex).getStart(), yAxisList.get(yAxisIndex).getEnd(), true);
    }

    public int getXIndex(int x, int y) {
        if (fullArea.y + fullArea.height / 2 <= y) {
            for (int i = 0; i < traces.size(); i++) {
                if (getTraceXIndex(i) == 0) {
                    return 0; // bottom Axis
                }
            }
            return 1; // top Axis
        }
        if (fullArea.y <= y && y <= fullArea.y + fullArea.height / 2) {
            for (int i = 0; i < traces.size(); i++) {
                if (getTraceXIndex(i) == 1) {
                    return 1; // top Axis
                }
            }
            return 0; // bottom Axis
        }
        return -1;

        // Find and return X axis used by the traces belonging to the stack containing point (x, y)
      /*  for (int stackIndex = 0; stackIndex < yAxisList.size() / 2; stackIndex++) {
            Axis stackYAxis = yAxisList.get(2 * stackIndex);
            if (stackYAxis.getEnd() <= y && (stackYAxis.getEnd() + stackYAxis.getStart()) / 2 >= y) { // top half
                for (int i = 0; i < traces.size(); i++) {
                    if(getTraceXIndex(i)== 1 && (getTraceYIndex(i) == 2 * stackIndex || getTraceYIndex(i) == 2 * stackIndex + 1)) {
                        return 1; // top Axis
                    }
                }
                return 0; // bottom Axis
            }
            if ((stackYAxis.getEnd() + stackYAxis.getStart()) / 2 <= y && y <= stackYAxis.getStart()) { // bottom half
                for (int i = 0; i < traces.size(); i++) {
                    if(getTraceXIndex(i)== 0 && (getTraceYIndex(i) == 2 * stackIndex || getTraceYIndex(i) == 2 * stackIndex + 1)) {
                        return 0; // bottom Axis
                    }
                }
                return 1; // top Axis
            }
        }
        return -1;*/
    }

    /**
     * Find and return Y axis used by the traces belonging to the stack containing point (x, y)
     */
    public int getYIndex(int x, int y) {
        for (int stackIndex = 0; stackIndex < yAxisList.size() / 2; stackIndex++) {
            if (yAxisList.get(2 * stackIndex).getEnd() <= y && yAxisList.get(2 * stackIndex).getStart() >= y) {
                int stackLeftYAxisIndex = 2 * stackIndex;
                int stackRightYAxisIndex = 2 * stackIndex + 1;
                if (fullArea.x <= x && x <= fullArea.x + fullArea.width / 2) { // left half
                    for (int i = 0; i < traces.size(); i++) {
                        if (getTraceYIndex(i) == stackLeftYAxisIndex) { // if leftAxis is used by some trace
                            return stackLeftYAxisIndex;
                        }
                    }
                    return stackRightYAxisIndex;
                }
                if (fullArea.x + fullArea.width / 2 <= x && x <= fullArea.x + fullArea.width) { // right half
                    for (int i = 0; i < traces.size(); i++) {
                        if (getTraceYIndex(i) == stackRightYAxisIndex) { // if rightAxis is used by some trace
                            return stackRightYAxisIndex;
                        }
                    }
                    return stackLeftYAxisIndex;
                }
            }
        }
        return -1;
    }


    public void zoomY(int yAxisIndex, float zoomFactor) {
        yAxisList.get(yAxisIndex).zoom(zoomFactor);
    }

    public void zoomX(int xAxisIndex, float zoomFactor) {
        xAxisList.get(xAxisIndex).zoom(zoomFactor);
    }

    public void translateY(int yAxisIndex, int translation) {
        yAxisList.get(yAxisIndex).translate(translation);
    }

    public void translateX(int xAxisIndex, int translation) {
        xAxisList.get(xAxisIndex).translate(translation);
    }

    public void autoScaleX(int xAxisIndex) {
        Range xRange = null;
        for (int i = 0; i < traces.size(); i++) {
            if (getTraceXIndex(i) == xAxisIndex) {
                xRange = Range.max(xRange, traces.get(i).getXExtremes());
            }
        }
        xAxisList.get(xAxisIndex).setMinMax(xRange);
    }

    public void autoScaleY(int yAxisIndex) {
        Range yRange = null;
        for (int i = 0; i < traces.size(); i++) {
            if (getTraceYIndex(i) == yAxisIndex) {
                yRange = Range.max(yRange, traces.get(i).getYExtremes());
            }
        }
        yAxisList.get(yAxisIndex).setMinMax(yRange);
    }

    public int getTraceYIndex(int traceIndex) {
        return chartConfig.getTraceYIndex(traceIndex);
    }

    public int getTraceXIndex(int traceIndex) {
        return chartConfig.getTraceXIndex(traceIndex);
    }


    public boolean hoverOff() {
        if (hoverPointIndex >= 0) {
            hoverPointIndex = -1;
            return true;
        }
        return false;
    }

    public boolean hoverOn(int x, int y, int traceIndex) {
        if(!graphArea.contains(x, y)) {
            return false;
        }
        if(traceIndex >= 0) {
            hoverTraceIndex = traceIndex;
        } else {
            for (int stackIndex = 0; stackIndex < yAxisList.size() / 2; stackIndex++) {
                if (yAxisList.get(2 * stackIndex).getEnd() <= y && yAxisList.get(2 * stackIndex).getStart() >= y) {
                    for (int i = 0; i < traces.size(); i++) {
                        if (getTraceYIndex(i) == 2 * stackIndex || getTraceYIndex(i) == 2 * stackIndex + 1) {
                            hoverTraceIndex = i;
                            break;
                        }
                    }
                }
            }
        }

        if (hoverTraceIndex >= 0) {
            float xValue = traces.get(hoverTraceIndex).getXAxis().invert(x);
            int nearestIndex = traces.get(hoverTraceIndex).getData().findNearestData(xValue);
            if (hoverPointIndex != nearestIndex) {
                hoverPointIndex = nearestIndex;
                if (hoverPointIndex >= 0) {
                    TooltipInfo tooltipInfo = new TooltipInfo();
                    tooltipInfo.addItems(traces.get(hoverTraceIndex).getInfo(hoverPointIndex));
                    BPoint dataPosition = traces.get(hoverTraceIndex).getDataPosition(hoverPointIndex);
                    tooltipPainter.setTooltipInfo(tooltipInfo);
                    tooltipPainter.setXY(dataPosition.getX(), yAxisList.get(getTraceYIndex(hoverTraceIndex)).getEnd());
                    crosshairPainter.setXY(dataPosition.getX(), dataPosition.getY());
                }
                return true;
            }
        }
        return false;
    }
}
