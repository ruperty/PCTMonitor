/* 
  *  This software is the property of Moon's Information Technology Ltd.
  * 
  *  All rights reserved.
  * 
  *  The software is only to be used for development and research purposes.
  *  Commercial use is only permitted under license or agreement.
  * 
  *  Copyright (C)  Moon's Information Technology Ltd.
  *  
  *  Author: rupert@moonsit.co.uk
  * 
  * 
 */
package uk.co.moonsit.rmi;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Rupert Young
 */
public class GraphClient extends JFrame {

    private static final Logger logger = Logger.getLogger(GraphClient.class.getName());

    private static final String title = "EV3 sampling";
    private GraphDataInterface gdi;
    private boolean running = true;
    private final int windowWidth;
    private final int windowHeight;
    private final int dataLength = 1000;
    private final XYSeriesCollection dataset = new XYSeriesCollection();
    private boolean log;
    private long start = 0;
    private long mark = 0;
    private int ctr = 1;
    private final float delay;
    private FileOutputStream out = null;
    private float previousTime = 0;

    public GraphClient(GraphDataInterface g, int windowWidth, int windowHeight, float frequency, boolean l) throws Exception {
        super(title);
        this.windowHeight = windowHeight;
        this.windowWidth = windowWidth;
        gdi = g;
        log = l;
        delay = 1000f / frequency;
    }

    public GraphClient(int windowWidth, int windowHeight, float frequency) throws Exception {
        super(title);
        this.windowHeight = windowHeight;
        this.windowWidth = windowWidth;
        delay = 1000f / frequency;
    }

    private void openFile(String labels) throws IOException {
        out = new FileOutputStream(new File("graph-out.csv"));
        out.write("time,dtime,".getBytes());
        out.write((labels + "\n").getBytes());
    }

    private void init(String config) throws IOException {
        //sp = ev3.createSampleProvider(portName, sensorClass, mode);
        String[] pars = config.split(";");
        String labels = pars[0];
        String category = pars[1];
        String units = pars[2];
        //int minValue = Integer.parseInt(pars[3]);
        //int maxValue = Integer.parseInt(pars[4]);

        if (log) {
            openFile(labels);
        }

        String[] labelsArray = labels.split(",");
        //seriesArray = new XYSeries[labelsArray.length];
        int i = 0;
        for (String label : labelsArray) {
            XYSeries series = new XYSeries(label);
            series.setMaximumItemCount(dataLength);
            dataset.addSeries(series);
            //seriesArray[i++] = series;
        }
        JFreeChart chart = ChartFactory.createXYLineChart(title, category, units, (XYDataset) dataset,
                PlotOrientation.VERTICAL, true, true, false);

        ChartPanel chartPanel = new ChartPanel(chart);
        //chartPanel.setMinimumSize(new Dimension(windowWidth, windowHeight));
        chartPanel.setPreferredSize(new Dimension(windowWidth, windowHeight));
        setContentPane(chartPanel);

        //XYPlot plot = chart.getXYPlot();
        //NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        //rangeAxis.setRange(minValue, maxValue);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack();
        setVisible(true);
    }

    @SuppressWarnings("null")
    private void processData() throws IOException {
        StringBuilder sb = null;
        if (log) {
            sb = new StringBuilder();
        }
        float[] fs = gdi.getValues();
        float time = fs[0];
        if (log) {
            sb.append(time).append(",").append(time - previousTime).append(",");
        }
        previousTime = time;
        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            float f = fs[i + 1];
            //logger.log(Level.INFO, "{0} {1}", new Object[]{time, f});
            dataset.getSeries(i).add(time, f);
            if (log) {
                sb.append(f).append(",");
            }
        }
        if (log) {
            sb.append("\n");
            out.write(sb.toString().getBytes());
            //logger.info(sb.toString());
        }

    }

    public void close() {
        running = false;
    }

    @SuppressWarnings("SleepWhileInLoop")
    public void run() throws Exception {
        start = System.currentTimeMillis();
        mark = System.currentTimeMillis();
        while (running) {
            processData();
            float rate = (System.currentTimeMillis() - start) / (float) ctr;
            long elapsed = System.currentTimeMillis() - mark;
            try {
                if (delay > elapsed) {
                    Thread.sleep((int) (delay - elapsed));
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(GraphClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (ctr % 100 == 0) {
                logger.log(Level.INFO, "{0} {1} {2}", new Object[]{rate, elapsed, delay - elapsed});
            }
            ctr++;
            mark = System.currentTimeMillis();
        }
        out.close();
    }

    public void addData(int i, double x, double y) {
        dataset.getSeries(i).add(x, y);
    }

    @SuppressWarnings("SleepWhileInLoop")
    public static void main(String args[]) {
        Properties p = new Properties();
        try {
            p.load(new FileReader("graph.properties"));
        } catch (IOException ex) {
            Logger.getLogger(GraphClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        //String[] labels = p.getProperty("labels").split(",");
        GraphClient demo=null;
        int type = 0;
        boolean log = false;
        if (args[0].equals("-l")) {
            log = true;
        }

        switch (type) {
            case 0:
                try {
                    System.setProperty("java.security.policy", "file:./client.policy");
                    if (System.getSecurityManager() == null) {
                        System.setSecurityManager(new RMISecurityManager());
                    }
                    String name = "GraphServer";
                    String host = p.getProperty("host");
                    System.out.println(host);
                    Registry registry = LocateRegistry.getRegistry(host);
                    String[] list = registry.list();
                    for (String s : list) {
                        System.out.println(s);
                    }
                    GraphDataInterface gs = null;
                    boolean bound = false;
                    while (!bound) {
                        try {
                            gs = (GraphDataInterface) registry.lookup(name);
                            bound = true;
                        } catch (NotBoundException e) {
                            System.err.println("GraphServer exception:" + e.toString());
                            Thread.sleep(500);
                        }
                    }
                    @SuppressWarnings("null")
                    String config = gs.getConfig();
                    System.out.println(config);
                    /*float[] fs = gs.getValues();
                     for (float f : fs) {
                     System.out.println(f);
                     }*/

                    demo = new GraphClient(gs, 1000, 700, Float.parseFloat(p.getProperty("frequency")), log);
                    demo.init(config);
                    demo.run();
                } catch (RemoteException e) {
                    System.err.println("GraphClient exception:" + e.toString());
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(GraphClient.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(GraphClient.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(GraphClient.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (demo != null) {
                        demo.close();
                    }
                }
                break;
            case 1:
                try {
                    demo = new GraphClient(1000, 700, Float.parseFloat(p.getProperty("frequency")));
                    demo.init("A^B|Time|Error|-360|360");

                    demo.addData(0, 1, 100);
                    demo.addData(0, 2, 200);

                    demo.addData(1, 1, 50);
                    demo.addData(1, 2, 450);

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(GraphClient.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(GraphClient.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(GraphClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
        }

    }
}
