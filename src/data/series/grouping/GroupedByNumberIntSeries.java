package data.series.grouping;

import data.series.IntSeries;
import data.series.LongSeries;
import data.series.grouping.aggregation.IntAggregateFunction;
import data.series.grouping.aggregation.IntAverage;

import java.text.MessageFormat;

/**
 * This class groups data in such a way that each group
 * has equal number of elements or data points.
 * Equal frequencies [equal height binning, quantiles] grouping.
 */
public class GroupedByNumberIntSeries extends GroupedIntSeries {
    private IntAggregateFunction aggregateFunction;

    public GroupedByNumberIntSeries(IntSeries inputSeries, int numberOfPointsInEachGroup, IntAggregateFunction aggregateFunction) {
        super(inputSeries);
        this.aggregateFunction = aggregateFunction;

        groupsStartIndexes = new LongSeries() {
            @Override
            public long size() {
                return inputSeries.size() / numberOfPointsInEachGroup;
            }

            @Override
            public long get(long index) {
                return index * numberOfPointsInEachGroup;
            }
        };
    }

    @Override
    protected int getGroupedValue(long groupIndex) {
        long numberOfGroupingElements = groupsStartIndexes.get(groupIndex + 1) - groupsStartIndexes.get(groupIndex);
        if(numberOfGroupingElements > Integer.MAX_VALUE) {
            String errorMessage = "Error during calculation of grouped value. Expected: numberOfGroupingElements is integer. NumberOfGroupingElements = {0}, Integer.MAX_VALUE = {1}.";
            String formattedError = MessageFormat.format(errorMessage, numberOfGroupingElements, Integer.MAX_VALUE);
            throw new RuntimeException(formattedError);
        }
        return aggregateFunction.group(inputSeries, groupsStartIndexes.get(groupIndex), (int) numberOfGroupingElements);
    }


  //  @Override
    public int getStartBoundary(long groupIndex) {
        return inputSeries.get(groupsStartIndexes.get(groupIndex));
    }

  //  @Override
    public int getStopBoundary(long groupIndex) {
        return inputSeries.get(groupsStartIndexes.get(groupIndex + 1));
    }

}
