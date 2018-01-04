package data;

import data.series.FloatSeries;
import data.series.IntSeries;

import java.util.List;

/**
 * Created by galafit on 29/9/17.
 */
public class XYData implements Data {
    private BaseDataSet dataSet = new BaseDataSet();

    @Override
    public BaseDataSet getDataSet() {
        return dataSet;
    }


    public void setYGroupingType(GroupingType groupingType) {
        dataSet.setYGroupingType(groupingType, 0);
    }

    public void setXData(IntSeries data) {
        dataSet.setXData(data);
    }

    public void setXData(FloatSeries data) {
        dataSet.setXData(data);
    }

    public void setXData(List<? extends Number> data) {
        dataSet.setXData(data);
    }

    public void setXData(float[] data) {
        dataSet.setXData(data);
    }

    public void setXData(int[] data) {
        dataSet.setXData(data);
    }

    public void setXData(float startXValue, float dataInterval) {
        dataSet.setXData(startXValue, dataInterval);
    }

    public void setYData(IntSeries data) {
        removeYData();
        dataSet.addYData(data);
    }

    public void setYData(FloatSeries data) {
        removeYData();
        dataSet.addYData(data);
    }

    public void setYData(List<? extends Number> data) {
        removeYData();
        dataSet.addYData(data);
    }

    public void setYData(float[] data) {
        removeYData();
        dataSet.addYData(data);
    }

    public void setYData(int[] data) {
        removeYData();
        dataSet.addYData(data);
    }

    private void removeYData() {
        int yColumnNumber = dataSet.getYColumnsCounter();
        if(yColumnNumber > 0) {
            dataSet.removeYData(yColumnNumber - 1);
        }
    }
}
