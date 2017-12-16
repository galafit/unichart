package data;

import base.Range;
import data.series.IntSeries;

/**
 * Created by galafit on 1/11/17.
 */
public class RegularColumn implements NumberColumn{
    private double startValue;
    private double dataInterval;

    public RegularColumn(double startValue, double dataInterval) {
        this.startValue = startValue;
        this.dataInterval = dataInterval;
    }

    public double getDataInterval() {
        return dataInterval;
    }

    public RegularColumn() {
        this(0, 1);
    }

    @Override
    public int size() {
        return Integer.MAX_VALUE;
    }

    @Override
    public double getValue(int index) {
        return startValue + dataInterval * index;
    }

    @Override
    public Range getExtremes(int from, int length) {
        double min = startValue;
        double max = startValue + (size() - 1) * dataInterval;
        return new Range(min, max);
    }

    @Override
    public int upperBound(double value, int from, int length) {
        int lowerBound = lowerBound(value, from, length);
        if(value == getValue(lowerBound)) {
            return lowerBound;
        }
        return lowerBound + 1;
    }

    @Override
    public void setGroupingType(GroupingType groupingType) {
        // DO NOTHING!!!
    }

    @Override
    public int lowerBound(double value, int from, int length) {
        return (int) ((value - startValue) / dataInterval);
    }

    @Override
    public void groupByNumber(int numberOfElementsInGroup, boolean isCachingEnable) {
        dataInterval = dataInterval * numberOfElementsInGroup;
    }

    @Override
    public NumberColumn copy() {
        return new RegularColumn(startValue, dataInterval);
    }
}
