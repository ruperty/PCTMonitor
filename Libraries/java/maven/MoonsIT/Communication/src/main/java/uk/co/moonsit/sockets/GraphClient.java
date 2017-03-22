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
package uk.co.moonsit.sockets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Rupert
 */
public class GraphClient extends JFrame {

    private static final Logger logger = Logger.getLogger(GraphClient.class.getName());

    private static final long serialVersionUID = 1L;
    private static final String title = "EV3 sampling";
    //private XYSeries[] seriesArray;
    private XYSeriesCollection[] datasets = null;// = new XYSeriesCollection();
    //private RemoteEV3 ev3;
    //private RMISampleProvider sp;
    //private final String[] labels;
    //private final float frequency;
    private SocketClient client = null;
    private boolean firstMessage = true;
    private boolean running = true;
    private final int windowWidth;
    private final int windowHeight;
    private final int dataLength;
    private final float rate;
    private String config = null;
    private int configIndex = 0;
    private boolean configChanged = false;
    //private long start = 0;
    //private long mark = 0;

    private JComboBox jComboBox;
    private JPanel mainPanel;
    private boolean record = false;
    private List<List<Float>> data;

    public GraphClient(String host, int port, int windowWidth, int windowHeight, float frequency, int dataLength, int timeout, boolean r) throws Exception {
        super(title);
        record = r;
        client = new SocketClient(host, port, timeout);
        //this.frequency = frequency;
        this.windowHeight = windowHeight;
        this.windowWidth = windowWidth;
        this.dataLength = dataLength;
        rate = 1000f / frequency;
    }

    public GraphClient(int windowWidth, int windowHeight, float frequency, int dataLength, boolean r) throws Exception {
        super(title);
        record = r;
        //this.frequency = frequency;
        this.windowHeight = windowHeight;
        this.windowWidth = windowWidth;
        this.dataLength = dataLength;
        rate = 1000f / frequency;
    }

    private void initFirstComponents() {
        logger.info("+++ initFirstComponents start ");

        mainPanel = new javax.swing.JPanel();
        mainPanel.setLayout(new java.awt.BorderLayout());

        JPanel controlsPanel = new javax.swing.JPanel();
        mainPanel.add(controlsPanel, java.awt.BorderLayout.PAGE_END);

        setContentPane(mainPanel);

        jComboBox = new javax.swing.JComboBox();

        jComboBox.setModel(new javax.swing.DefaultComboBoxModel(SocketConstants.getConfigList()));
        jComboBox.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxActionPerformed(evt);
            }
        });

        controlsPanel.add(jComboBox);

        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);

    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            logger.info("Closing");
            running = false;
            try {
                Thread.sleep(1000);
                client.close();
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(GraphClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (record) {
                try {
                    if (config != null && data.size() > 0) {
                        saveData();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GraphClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.exit(0);
        }
    }

    private void saveData() throws IOException {
        String file = "c://tmp//graph.csv";
        File f = new File(file);
        if (f.exists()) {
            f.delete();
        }
        try (FileWriter fstream = new FileWriter(file, true)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Time,");
            sb.append(config.split(";")[0]);
            sb.append("\n");

            for (List<Float> list : data) {
                for (Float fl : list) {
                    sb.append(fl);
                    sb.append(",");
                }
                sb.append("\n");
            }

            try (BufferedWriter out = new BufferedWriter(fstream)) {
                out.write(sb.toString());
                //Close the output stream
            }
        }
        logger.log(Level.INFO, "Saved data to {0}", file);
    }

    private void removeChartComponents() {

        logger.log(Level.INFO, "+++ comps {0}", mainPanel.getComponentCount());

        if (mainPanel.getComponentCount() > 1) {
            mainPanel.remove(1);
        }
        logger.log(Level.INFO, "+++ comps {0}", mainPanel.getComponentCount());
        pack();
        setVisible(true);

    }

    private void initChartComponents(JFreeChart chart) {
        ChartPanel chartPanel = new ChartPanel(chart);
        mainPanel.add(chartPanel, java.awt.BorderLayout.CENTER);

        chartPanel.setPreferredSize(new Dimension(windowWidth, windowHeight));
        pack();
        setVisible(true);

        revalidate();
        repaint();
    }

    public void setConfig(String config) {
        configChanged = true;
        this.config = config;
        if (record) {
            data = new ArrayList<>();
        }
        configIndex = getConfigIndex(config);
        logger.log(Level.INFO, "item {0} {1}", new Object[]{configIndex, config});
    }

    private int getConfigIndex(String config) {
        int rtn = -1;
        int ctr = -1;
        for (String s : SocketConstants.getConfigList()) {
            if (s.equals(config)) {
                rtn = ctr;
                break;
            }
            ctr++;
        }

        return rtn;
    }

    private void jComboBoxActionPerformed(java.awt.event.ActionEvent evt) {

        setConfig(jComboBox.getSelectedItem().toString());
        configIndex = jComboBox.getSelectedIndex() - 1;
        //config.split("'");

        logger.log(Level.INFO, "item {0} {1}", new Object[]{configIndex, config});

    }

    private JFreeChart initChart(String config) {
        logger.info("+++ initChart start ");

        String[] pars = config.split(";");
        String labels = pars[0];
        String category = pars[1];
        //String units = pars[2];

        String[] labelsArray = labels.split(",");
        //seriesArray = new XYSeries[labelsArray.length];
        datasets = new XYSeriesCollection[labelsArray.length];
        JFreeChart chart = ChartFactory.createXYLineChart(null, category, null, (XYDataset) datasets[0],
                PlotOrientation.VERTICAL, true, true, false);

        int j = 0;
        int[] axesIndex = new int[labelsArray.length];
        for (String label_ind : labelsArray) {
            String[] liArr = label_ind.split("_");
            XYSeries series = new XYSeries(liArr[0]);
            axesIndex[j] = Integer.parseInt(liArr[1]);
            series.setMaximumItemCount(dataLength);
            datasets[j] = new XYSeriesCollection();
            datasets[j].addSeries(series);
            //seriesArray[i++] = series;
            j++;
        }

        XYPlot plot = chart.getXYPlot();
        setAxes(plot, axesIndex);
        setSubtitles(chart, labelsArray);
        setSeriesColors(plot);

        return chart;
    }

    private void init(String config) {
        logger.info("+++ init start ");

        removeChartComponents();

        JFreeChart chart = initChart(config);
        initChartComponents(chart);

    }

    private void setSubtitles(JFreeChart chart, String[] labelsArray) {
        for (String label : labelsArray) {
            int axis = Integer.parseInt(label.substring(label.length() - 1, label.length()));

            TextTitle tt = new TextTitle(label);
            tt.setPaint(getAxisColor(axis - 1));

            chart.addSubtitle(tt);
        }
    }

    private void setAxes(XYPlot plot, int[] axesIndex) {

        for (int i = 0; i < datasets.length; i++) {
            plot.setRangeAxis(axesIndex[i] - 1, getTheRangeAxis(axesIndex[i] - 1));
            AxisLocation axloc;
            if (i % 2 == 0) {
                axloc = AxisLocation.BOTTOM_OR_LEFT;
                //System.out.print("L ");
            } else {
                axloc = AxisLocation.BOTTOM_OR_RIGHT;
                //System.out.print("R ");
            }
            plot.setRangeAxisLocation(i, axloc);
            plot.setDataset(i, datasets[i]);
            //System.out.println(i + " " + (axesIndex[i] - 1));
            plot.mapDatasetToRangeAxis(i, axesIndex[i] - 1);
            final StandardXYItemRenderer renderer = new StandardXYItemRenderer();
            //renderer2.setSeriesPaint(0, Color.black);
            plot.setRenderer(i, renderer);
        }
    }

    private void setSeriesColors(XYPlot plot) {
        int colorIndex = 0;

        for (int i = 0; i < plot.getDatasetCount(); i++) {
            if (plot.getDataset(i) == null) {
                continue;
            }
            for (int ii = 0; ii < plot.getDataset(i).getSeriesCount(); ii++) {
                plot.getRenderer(i).setSeriesPaint(ii, getSeriesColor(colorIndex++));
                //chart.getXYPlot().getRenderer(i).setSeriesStroke(                    ii,new BasicStroke(NORMAL_SERIES_LINE_WEIGHT, BasicStroke.CAP_BUTT,  BasicStroke.JOIN_BEVEL));
            }
        }

    }

    public void run() throws Exception {
        boolean eofError = false;
        while (running) {
            while (configChanged || !client.isConnected() || client.isClosed()) {
                try {
                    client.connect();
                    firstMessage = true;
                    configChanged = false;
                } catch (IOException e) {
                    logger.log(Level.INFO, "Connection failed {0}", e.toString());
                    //running = false;
                    //break;

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MessageClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            //logger.info("running " + running);
            //logger.info("firstMessage " + firstMessage);
            //String msg;
            try {

                if (firstMessage) {
                    client.writeInt((int) rate);
                    client.writeInt((int) configIndex);
                    int size = config.split("_").length;
                    logger.log(Level.INFO, "size {0}", size);
                    client.writeInt(size);
                    logger.log(Level.INFO, "config {0}", config);
                    init(config);
                    firstMessage = false;
                } else {
                    if (!eofError) {
                        processData();
                    }
                }

            } catch (EOFException ex) {
                Logger.getLogger(MessageClient.class.getName()).log(Level.SEVERE, null, ex);
                eofError = true; //client.close();
            } catch (IOException ex) {
                Logger.getLogger(MessageClient.class.getName()).log(Level.SEVERE, null, ex);
                firstMessage = true;
            }
        }

    }

    private NumberAxis getTheRangeAxis(int index) {//, DatasetAndAxis[] dAnda) {
        //if (dAnda[index].numberAxisObj !=null) {
        //  return dAnda[index].numberAxisObj;
        //}

        NumberAxis rangeAxis;
        Color axisColor = getAxisColor(index);
        String axisLabel = "Range";
        if (index > 0) {
            axisLabel += "-" + (index + 1);
        }
        rangeAxis = new NumberAxis(axisLabel);
        rangeAxis.setTickLabelFont(new Font("Arial", Font.PLAIN, 9));
        rangeAxis.setLabelFont(new Font("Arial", Font.PLAIN, 12));
        rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
        // rangeAxis.setRange(new Range(0,0)); // need to set a range or we get a NaN on getRange() calls
        //rangeAxis.setAutoRange(false);
        rangeAxis.setAutoRangeIncludesZero(false);

        // set colors
        rangeAxis.setLabelPaint(axisColor);
        rangeAxis.setAxisLinePaint(axisColor);
        rangeAxis.setLabelPaint(axisColor);
        rangeAxis.setTickLabelPaint(axisColor);
        rangeAxis.setTickMarkPaint(axisColor);
        //dAnda[index].numberAxisObj=rangeAxis;
        return rangeAxis;
    }

    public void addData(int i, double x, double y) {
        //System.out.println("" + i + " " + x + " " + y);
        datasets[i].getSeries(0).add(x, y);

    }

    private void processData() throws IOException {
        //System.out.println(client.isClosed() + " " + client.isConnected());
        //logger.info("+++ processData ");
        List<Float> array = null;
        float time = client.readFloat();
        if (record) {
            array = new ArrayList<>();
            array.add(time);
        }
        //System.out.print(""+time);
        for (int i = 0; i < datasets.length; i++) {
            float f = client.readFloat();
            if (record) {
                array.add(f);
            }
            //System.out.print(" "+f);
            addData(i, time, f);
        }

        if (record) {
            data.add(array);
        }
        //System.out.println();

    }

    private Color getAxisColor(int index) {
        switch (index) {
            case 0:
                return Color.BLACK;
            case 1:
                return Color.MAGENTA.darker();
            case 2:
                return Color.BLUE;
            case 3:
                return Color.RED;
            case 4:
                return Color.ORANGE.darker();

        }
        return Color.BLACK;
    }

    private Color getSeriesColor(int index) {
        if (index > 10) {
            index = index % 11;
        }
        switch (index) {
            case 0:
                return Color.BLUE.brighter().brighter();
            case 1:
                return Color.RED.brighter();
            case 2:
                return Color.GREEN.darker();
            case 3:
                return Color.CYAN.darker();
            case 4:
                return Color.ORANGE.darker();
            case 5:
                return Color.DARK_GRAY;
            case 6:
                return Color.MAGENTA;
            case 7:
                return Color.BLUE.darker().darker();
            case 8:
                return Color.CYAN.darker().darker();
            case 9:
                return Color.BLACK.brighter();
            case 10:
                return Color.RED.darker();
        }
        return Color.LIGHT_GRAY;
    }

    public String getConfig() {
        return config;
    }

    public static void main(String[] args) throws Exception {

        String host = null;
        int port = 0;
        int dataLength = 0;
        String freq = null;
        int type = 0;
        boolean record = false;
        String config = null;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-host")) {
                host = args[++i];
            }
            if (args[i].equals("-port")) {
                port = Integer.parseInt(args[++i]);
            }
            if (args[i].equals("-type")) {
                type = Integer.parseInt(args[++i]);
            }
            if (args[i].equals("-freq")) {
                freq = args[++i];
            }
            if (args[i].equals("-config")) {
                config = args[++i];
            }
            if (args[i].equals("-datal")) {
                dataLength = Integer.parseInt(args[++i]);
            }
            if (args[i].equals("-record")) {
                record = true;
            }

        }

        GraphClient demo;

        switch (type) {
            case 0:
                demo = new GraphClient(host, port, 1000, 700, Float.parseFloat(freq), dataLength, 5000, record);
                demo.initFirstComponents();
                if(config!=null)
                    demo.setConfig(config);
                while (demo.getConfig() == null) {
                    Thread.sleep(1000);
                }
                demo.run();
                logger.info("Client finished");

                break;
            case 1:
                demo = new GraphClient(1000, 700, Float.parseFloat(freq), 20, record);
                //demo.init("A_1, B_2, C_3, D_4;Time");
                demo.initFirstComponents();

                //demo.init("A_1, B_1, C_2, D_3;Time");
                while (demo.getConfig() == null) {
                    Thread.sleep(1000);
                }

                //demo.init("A_1, B_1, C_2, D_3;Time");
                demo.init(demo.getConfig());

                int y1 = 500;
                int y2 = 50;

                for (int i = 0; i < 10; i++) {
                    demo.addData(0, i, y1 + i * 10);
                    //demo.addData(0, 2, y1+100);

                    demo.addData(1, i, y2 * -2);
                    //demo.addData(1, 2, y2-10);

                    demo.addData(2, i, y2 * y1);
                    demo.addData(3, i, -10);

                    y1++;
                    y2++;
                    Thread.sleep(1000);
                }
                break;
        }

        //System.exit(0);
    }
}
