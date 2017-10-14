package data.series.grouping;

import data.series.DoubleSeries;
import data.series.IntSeries;

/**
 * Created by galafit on 14/10/17.
 */
public class DoubleGroupedSeries implements DoubleSeries {
    private DoubleSeries series;
    private DoubleGroupingFunction groupingFunction;
    private IntSeries groupIndexes;

    public DoubleGroupedSeries(DoubleSeries series, DoubleGroupingFunction groupingFunction, int groupingIndexInterval) {
        this.groupingFunction = groupingFunction;
        this.series = series;
        groupIndexes = new IntSeries() {
            @Override
            public int size() {
                return series.size() / groupingIndexInterval;
            }

            @Override
            public int get(int index) {
                return index * groupingIndexInterval;
            }
        };
    }

    public DoubleGroupedSeries(DoubleSeries series, DoubleGroupingFunction groupingFunction, IntSeries groupIndexes) {
        this.groupingFunction = groupingFunction;
        this.groupIndexes = groupIndexes;
        this.series = series;
    }

    @Override
    public int size() {
        return groupIndexes.size() - 1;
    }

    @Override
    public double get(int index) {
        return groupingFunction.group(series, groupIndexes.get(index), groupIndexes.get(index + 1) - groupIndexes.get(index));
    }
}
