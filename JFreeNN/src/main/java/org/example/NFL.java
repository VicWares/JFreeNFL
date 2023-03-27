package org.example;// Java Program to Illustrate Working of SwingWorker Class
/*****************************************************************************************
 * DL4J Iris Classificaton Example: version 230326
 *****************************************************************************************/
import com.google.common.base.MoreObjects;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;
public class NFL
{
    private static MoreObjects.ToStringHelper series;
    private final String title = "JFreeNFL";
    private static String version = "230326";
    private JFreeChart LineChart;
    ChartPanel chartPanel = new ChartPanel(LineChart);
    private Dimension frameSize = new Dimension(1500, 1500);
    private Rectangle buttonBounds = new Rectangle(40, 100, 100, 60);
    private static int epoch = 0;
    private static double accuracy = 0.0;
    public static void setSeries(MoreObjects.ToStringHelper trainingData)
    {
        NFL.series = trainingData;
    }
    public NFL()//Constructor
    {
        JFrame mainFrame = new JFrame("JFreeNFL");
        mainFrame.setSize(frameSize);
        mainFrame.add(chartPanel);
        mainFrame.setVisible(true);
        JButton btn = new JButton("Start JFreeNFL");
        btn.setBounds(buttonBounds);
        btn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("JFNFL41 Button clicked, Training started");
                startTraining();
            }
        });
        btn.setVisible(true);
        mainFrame.setVisible(true);
        chartPanel.add(btn);
        addPoint();
    }
    private void addPoint()
    {
        System.out.println("JFNN50 Adding point to chart...demmy stub");
        {
//            series.add(String.valueOf(this.epoch), this.accuracy);
//            chartPanel.repaint();
        }
    }
    private void startTraining()
    {
        SwingWorker sw1 = new SwingWorker()
        {
            @Override
            protected String doInBackground() //************************> Defining what SwingWorker thread will do here
            {
                LearningDL4J learningDL4J = new LearningDL4J();
                learningDL4J.loadData();
                return "Training completed";
            }
            @Override
            protected void done()// this method is called when the background thread finishes execution
            {
                try {
                    String statusMsg = (String) get();
                    System.out.println("\nInside done function, status => " + statusMsg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };
        // Executes the swing worker on worker thread
        sw1.execute();
    }
    public static void main(String[] args)
    {
        System.out.println("JFreeNN version: " + version);
        new NFL();
    }
    public static void setEpoch(int epoch)
    {
        NFL.epoch = epoch;
    }
    public static void setAccuracy(double accuracy)
    {
        NFL.accuracy = accuracy;
        series.add(String.valueOf(epoch), accuracy);
    }
}
