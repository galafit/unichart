import base.config.traces.LineTraceConfig;
import data.XYData;
import data.series.FloatArrayList;
import data.series.IntArrayList;

import javax.swing.*;
import java.awt.*;
import java.util.Random;


/**
 * Created by hdablin on 24.03.17.
 */
public class MainFrame extends JFrame {
    FloatArrayList yData1;
    IntArrayList yData2;
    FloatArrayList xData;
    ChartPanel chartPanel;

    public MainFrame() throws HeadlessException {
        int width = 500;
        int height = 500;

        setTitle("Test chart");

        yData1 = new FloatArrayList();
        Random rand = new Random();
        for (int i = 0; i < 800 ; i++) {
            yData1.add((float) Math.sin(i));
        }

        yData2 = new IntArrayList();
        for (int i = 0; i < 1600 ; i++) {
            yData2.add(i/2);
        }

        xData = new FloatArrayList();
        for (int i = 0; i < 1600 ; i++) {
            xData.add(i/2);
        }


        XYData xyData1 = new XYData();
        xyData1.setYData(yData1);

        XYData xyData2 = new XYData();
        xyData2.setYData(yData2);
        xyData2.setXData(xData);

        XYData xyData3 = new XYData();
        xyData3.setYData(yData2);

        ChartConfig config = new ChartConfig(false);
        config.addTrace(new LineTraceConfig(), xyData1);
        config.addChartStack(5);
        config.addTrace(new LineTraceConfig(), xyData2);

       // config.setPreviewMinMax(new Range(0, 1000));
       // config.addScroll(0, 100);

       config.addPreviewTrace(new LineTraceConfig(), xyData2);
       config.addPreviewTrace(new LineTraceConfig(), xyData3);
       //config.addPreviewGroupingInterval(40);


        chartPanel = new ChartPanel(config);

        chartPanel.setPreferredSize(new Dimension(width, height));
        add(chartPanel,BorderLayout.CENTER);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addKeyListener(chartPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void update() {
        Random rand = new Random();
        for (int i = 1; i <= 800 ; i++) {
            yData1.add(rand.nextInt(500));
        }

        for (int i = 1; i <=800 ; i++) {
            float lastValue = xData.get(xData.size() - 1);
            xData.add(lastValue + 1);
        }
        for (int i = 1; i <=800 ; i++) {
            yData2.add(i);
            float lastValue = yData2.get(yData2.size() - 1);
            //yData2.add(lastValue + 1);
        }
        chartPanel.update();
    }


    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();

      /* final Timer timer = new Timer(1000, new ActionListener() {
            int counter = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                if(counter < 20) {
                    mainFrame.update();
                    counter++;
                }
            }
        });
        timer.setInitialDelay(0);
        timer.start();*/

    }

}
