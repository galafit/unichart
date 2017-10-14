package data.series.grouping;

import data.series.DoubleSeries;

/**
 * Created by galafit on 12/10/17.
 */
public interface DoubleGroupingFunction {
    public double group(DoubleSeries series, int from, int length);

}
