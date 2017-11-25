package data.series;

/**
 * Class is designed to store/cache a computed input data and to give quick access to them.
 * Input data could be a filter, function and so on
 */

public class CachingIntSeries implements IntSeries {
    private IntSeries inputData;
    private IntArrayList cachedData;
    private boolean isCashingEnabled = true;


    public CachingIntSeries(IntSeries inputData) {
        this.inputData = inputData;
        cachedData = new IntArrayList(inputData.size());
        cacheData();
    }

    private void cacheData() {
        if (cachedData.size()  < inputData.size()) {
            for (int i = cachedData.size(); i < inputData.size(); i++) {
                cachedData.add(inputData.get(i));
            }
        }
    }

    public void disableCashing() {
        isCashingEnabled = false;
        cachedData = null;
    }

    @Override
    public int get(int index) {
        if(isCashingEnabled) {
            return cachedData.get(index);
        }
        return inputData.get(index);
    }


    @Override
    public int size() {
        if(isCashingEnabled) {
            cacheData();
            return cachedData.size();
        }
        return inputData.size();
    }
}

