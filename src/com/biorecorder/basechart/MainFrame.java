package com.biorecorder.basechart;

import com.biorecorder.basechart.chart.config.traces.LineTraceConfig;
import com.biorecorder.basechart.data.XYData;
import com.biorecorder.basechart.data.FloatArrayList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Created by hdablin on 24.03.17.
 */
public class MainFrame extends JFrame {
    FloatArrayList yData1;
    FloatArrayList yData2;
    FloatArrayList xData;
    ChartPanel chartPanel;

    public MainFrame() throws HeadlessException {
        int width = 500;
        int height = 500;

        setTitle("Test chart");

        yData1 = new FloatArrayList();
        yData2 = new FloatArrayList();
        xData = new FloatArrayList();

        for (int i = 0; i < 1600 ; i++) {
            //yData1.add((float) Math.sin(i));
            yData1.add(i);
        }
        for (int i = 100; i < 1600 ; i++) {
            yData2.add(i + 100);
        }
        for (int i = 100; i < 1600 ; i++) {
            xData.add(i);
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
        //config.addChartStack(5);
        config.addTrace(new LineTraceConfig(true), xyData2);

       // config.setPreviewMinMax(new Range(0, 1000));
       // config.addScroll(0, 100);

       config.addPreviewTrace(new LineTraceConfig(), xyData2);
       config.addPreviewTrace(new LineTraceConfig(), xyData3);
       //config.addPreviewGroupingInterval(10);


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
        for (int i = 1; i <= 800 ; i++) {
            yData1.add((float) Math.sin(i));
        }


        for (int i = 1; i <=800 ; i++) {
            float lastValue = 0;
            if(xData.size() > 0) {
                lastValue = xData.get(xData.size() - 1)+1;
            }
            xData.add(lastValue + 1);
        }
        for (int i = 1; i <=800 ; i++) {
            yData2.add(i);
        }
        chartPanel.update();
    }


    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();

       final Timer timer = new Timer(1000, new ActionListener() {
            int counter = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                if(counter < 0) {
                    mainFrame.update();
                    counter++;
                }
            }
        });
        timer.setInitialDelay(0);
        timer.start();

    }
}
