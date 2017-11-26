import base.config.Config;
import base.config.traces.AreaTraceConfig;
import base.config.traces.LineTraceConfig;
import data.XYData;
import data.series.DoubleArrayList;
import data.series.IntArrayList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;


/**
 * Created by hdablin on 24.03.17.
 */
public class MainFrame extends JFrame {
    IntArrayList yData1;
    DoubleArrayList yData2;
    DoubleArrayList xData;
    ChartPanel chartPanel;

    public MainFrame() throws HeadlessException {
        int width = 500;
        int height = 500;

        setTitle("Test chart");

        yData1 = new IntArrayList();
        Random rand = new Random();
        for (int i = 0; i < 800 ; i++) {
            yData1.add(rand.nextInt(500));
        }

        yData2 = new DoubleArrayList();
        for (int i = 0; i < 800 ; i++) {
            yData2.add(i);
        }

        xData = new DoubleArrayList();
        for (int i = 0; i < 800 ; i++) {
            xData.add(i);
        }


        XYData xyData1 = new XYData();
        xyData1.setYData(yData1);

        XYData xyData2 = new XYData();
        xyData2.setYData(yData2);
        xyData2.setXData(xData);

        XYData xyData3 = new XYData();
        xyData3.setYData(yData2);

        Config config = new Config();
        config.addTrace(new LineTraceConfig(), xyData1, null, true, false);
        config.addStack(5);
        config.addTrace(new LineTraceConfig(), xyData2);

        config.addPreviewTrace(new LineTraceConfig(), xyData2);
        //config.addPreviewTrace(new LineTraceConfig(), xyData3);


        config.enablePreview(true);

        chartPanel = new ChartPanel(config);

        chartPanel.setPreferredSize(new Dimension(width, height));
        add(chartPanel,BorderLayout.CENTER);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

       /* try {
            Thread.sleep(3000);
            xyList.addPoint(150 , 150.0);
            chartPanel.update();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

      /* for (int j = 0; j < 10; j++) {
           try {
               Thread.sleep(1000);
               for (int i = 0; i < 10; i++) {
                   xyList.addPoint(150 + j*30,(i + j) * 50.0);
               }
               chartPanel.update();
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
        }*/

    }

    public void update() {
        Random rand = new Random();
        for (int i = 1; i <= 800 ; i++) {
            yData1.add(rand.nextInt(500));
        }

        for (int i = 1; i <=800 ; i++) {
            double lastValue = xData.get(xData.size() - 1);
            xData.add(lastValue + 1);
           // xData.add(lastValue + 2);
        }
        for (int i = 1; i <=800 ; i++) {
            yData2.add(i);
            double lastValue = yData2.get(yData2.size() - 1);
            //yData2.add(lastValue + 1);
        }
        chartPanel.update();
    }


    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();


        final Timer timer = new Timer(100, new ActionListener() {

            int counter = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                if(counter < 50) {
                    mainFrame.update();
                    counter++;
                }
            }
        });
        timer.setInitialDelay(0);
        timer.start();

    }

}
