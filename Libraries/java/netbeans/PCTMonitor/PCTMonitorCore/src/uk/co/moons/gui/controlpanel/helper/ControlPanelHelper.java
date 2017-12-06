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
package uk.co.moons.gui.controlpanel.helper;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.xml.bind.JAXBException;
import pct.moons.co.uk.schema.layers.ControlFunction;
import pct.moons.co.uk.schema.layers.Layers;
import pct.moons.co.uk.schema.layers.Layers.Layer;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions;
import uk.co.moons.config.BaseControlBuild;
import uk.co.moons.config.ControlBuildFactory;
import uk.co.moons.control.ControlHierarchy;
import uk.co.moons.control.ControlLayer;
import uk.co.moons.control.ModelControlHierarchy;
import uk.co.moons.control.RemoteControlHierarchy;
import uk.co.moons.control.RobotControlHierarchy;
import uk.co.moons.gui.components.BooleanFlag;
import uk.co.moons.gui.components.ControlHierarchyEventMonitor;
import uk.co.moons.gui.components.ControlSystem;
import uk.co.moons.gui.controlpanel.display.ControlDisplayTypes;
import uk.co.moons.gui.layout.SpringUtilities;
import uk.co.moonsit.utils.Environment;
import uk.co.moonsit.utils.MoonsString;

/**
 *
 * @author ReStart
 */
public class ControlPanelHelper implements Runnable {

    private static final Logger LOG = Logger.getLogger(ControlPanelHelper.class.getName());
    private ControlHierarchy ch;
    //private JPanel layersPanel;
    //private JPanel plotPanel;
    private PlotPanelHelper plotPanelHelper;
    private JLabel timeLabel;
    private HashMap<String, ControlDisplayTypes> cdts;
    private String displayFile;
    private String outputFile;
    //private String pricesFile;
    private static final int ROBOT = 1;
    private static final int MODEL = 2;
    private int type;
    private int iter = 1;
    private int stepSize = 1;
    private Long speed;
    private boolean step = false;
    private boolean print = false;
    private boolean output = false;
    //private boolean remote = false;

    private Integer plotDataItemsLimit = null;
    private final ControlHierarchyEventMonitor monitor;
    //private boolean remoteData=false;
//private MessageClient messageClient = null;

    public ControlPanelHelper(File file, boolean p, boolean o, ControlHierarchyEventMonitor m, int pdil) throws Exception {
        plotDataItemsLimit = pdil;
        print = p;
        output = o;
        displayFile = getDisplayFile(file);
        //remote = false;
        //logger.info("bools " + print + " " + output);

        cdts = getDisplayTypes();
        String config = file.getAbsolutePath();
        String xmlconfig = ControlHierarchy.processODG(config);
        BaseControlBuild controlBuild = ControlBuildFactory.getControlBuildFunction(xmlconfig);

        type = getType(controlBuild);

        //gp2d = new GridPlot2d();
        if (output) {
            setOutputFields();
        }
        switch (type) {
            case ROBOT:
                if (output) {
                    outputFile = "C:\\tmp\\PCT\\Controllers\\Robot\\output\\out.csv";
                    File f = new File(outputFile);
                    if (f.exists()) {
                        f.delete();
                    }
                }
                ch = new RobotControlHierarchy(config);
                //speed = (long) 1000;
                break;
            case MODEL:
                ch = new ModelControlHierarchy(config);
                ch.setType(MODEL);
                if (ch.getControllerFunction("TimeRate") != null) {
                    ((ModelControlHierarchy) ch).setTimeRate(ch.getControllerFunction("TimeRate").getNeural().getOutput());
                }
                configureOutputFilePrivate(Environment.getInstance().getFileRoot());
                break;
            default:
                ch = new ControlHierarchy(config);
                configureOutputFilePrivate(Environment.getInstance().getFileRoot());
                break;
        }

        monitor = m;
        monitor.setControlHierarchy(ch);
    }

    /*private String processODG(String config) throws Exception {
        if (config.contains(".odg")) {
            String xml = config.replace(".odg", ".xml");
            File fxml = new File(xml);
            File fodg = new File(config);
            if (fodg.lastModified() > fxml.lastModified()) {
                ODGConfig odg = new ODGConfig();
                odg.processDocument(config);
                odg.saveConfig(xml);
            }
            config = xml;
        }

        return config;
    }*/
    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public ControlPanelHelper(String host, int dport, int pport, int freq, boolean p, boolean o, ControlHierarchyEventMonitor m, int pdil, String d, int timeout) throws Exception {
        plotDataItemsLimit = pdil;
        print = p;
        output = o;
        //remote = true;
        ch = new RemoteControlHierarchy(host, dport, pport, freq, true, timeout);

        String dir = d + File.separator + getSubDir() + File.separator;
        String fileName = getRemoteFilename();
        String file = dir + File.separator + fileName;
        Environment.getInstance().setFilePath(dir);
        Environment.getInstance().setFileRoot((new File(fileName)).getName());
        //String config = configAndFile.substring(endIndex);
        LOG.log(Level.INFO, "+++ file {0}", file);
        displayFile = getDisplayFile(new File(file));
        cdts = getDisplayTypes();

        configureOutputFilePrivate(fileName);

        ch.setUpOutputQueue();
        monitor = m;
        monitor.setControlHierarchy(ch);
    }

    public void shutdown() {
        if (ch instanceof RemoteControlHierarchy) {
            ((RemoteControlHierarchy) ch).shutdown();
        }
    }

    public int getStepSize() {
        return stepSize;
    }

    public void setStepSize(int stepSize) {
        this.stepSize = stepSize;
    }

    private String getSubDir() {
        String filePath = ((RemoteControlHierarchy) ch).getPath();

        int lastIndex = 0;
        int beginIndex = 0;
        if (filePath.contains("/")) {
            lastIndex = filePath.lastIndexOf("/");
            beginIndex = filePath.indexOf("Controllers") + 12;
        }
        if (filePath.contains("\\")) {
            lastIndex = filePath.lastIndexOf("\\");
            beginIndex = filePath.indexOf("Controllers") + 12;
        }
        String fileName = filePath.substring(beginIndex, lastIndex);

        return fileName;
    }

    private String getRemoteFilename() {
        String filePath = ((RemoteControlHierarchy) ch).getPath();

        int beginIndex = 0;
        if (filePath.contains("/")) {
            beginIndex = filePath.lastIndexOf("/");
        }
        if (filePath.contains("\\")) {
            beginIndex = filePath.lastIndexOf("\\");
        }
        String fileName = filePath.substring(beginIndex + 1);

        return fileName;
    }

    private void configureOutputFilePrivate(String fileName) {
        if (output) {
            setOutputFields();
            ch.setListOutputFunctions(Environment.getInstance().getListOutputFunctions());
            String home = System.getProperty("user.home");

            File dir;
            if (System.getProperty("os.name").equalsIgnoreCase("linux")) {
                dir = new File(home + "/tmp/PCT/");
                /*if (!dir.exists()) {
                    dir.mkdir();
                }*/
            } else {
                dir = new File(home + "\\tmp\\PCT\\Controllers\\");
                /*if (!dir.exists()) {
                    dir.mkdir();
                }*/

            }
            outputFile = dir + File.separator + fileName + ".csv";
            ch.setOutputFile(outputFile);
            LOG.log(Level.INFO, "Output file path {0}", outputFile);

            /*File f = new File(outputFile);
            if (f.exists()) {
                f.delete();
            }*/
        }
    }

    public String getOutputFile() {
        return outputFile;
    }

    private void setOutputFields() {

        Properties props = getProperties();
        List<String> list = new ArrayList<>();

        if (props != null && !props.isEmpty()) {
            String functionList = props.getProperty("Functions");
            String[] funcs = functionList.split(",");
            list.addAll(Arrays.asList(funcs));
        }
        Environment.getInstance().setListOutputFunctions(list);

    }

    public static Properties getProperties() {

        String path = Environment.getInstance().getFilePath() + File.separator + "properties"
                + File.separator
                + (Environment.getInstance().getFileRoot().charAt(0) < 63 ? Environment.getInstance().getFileRoot().substring(0, 7) : Environment.getInstance().getFileRoot())
                + File.separator;
        String fname = path + Environment.getInstance().getFileRoot() + "-output.properties";
        Properties props = null;
        try {
            props = new Properties();
            props.load(new FileInputStream(new File(fname)));
        } catch (IOException ex) {
            LOG.warning(ex.toString());
            //Logger.getLogger(PlotPanelHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return props;
    }

    /*
     public void add2dDataSet(String controller, String function) {
     gp2d.addDataSet(controller, function);
     }
     */
    public void add2dDataSet(String controller, String function, int i) {
        plotPanelHelper.add2dDataSet(controller, function, i);
    }

    /*
     public Plot2DPanel getPlotPanel() {
     return gp2d.getPlotPanel();
     }
     */
    public void setPlotPanelHelper(PlotPanelHelper pph) {
        this.plotPanelHelper = pph;
    }

    /*
     private String getOutputFile(File file) {
     DateAndTime dat = new DateAndTime();
     String fname = file.getAbsolutePath();
     String sep = File.separator;
     int index = fname.lastIndexOf(sep);
     return fname.substring(0, index) + sep + "output" + sep + file.getName() + "." + dat.YMD() + "-" + dat.HMS() + ".csv";
     //return fname.substring(0, index) + sep + "output" + sep + file.getName() + "." + dat.YMD() + "-" + dat.HourOfDay() + dat.MinutesInteger() + dat.SecondsInteger() + ".csv";
     }
     * 
     */

 /*
     private String getPricesOutputFile(File file) {
     DateAndTime dat = new DateAndTime();
     String fname = file.getAbsolutePath();
     String sep = File.separator;
     int index = fname.lastIndexOf(sep);
     return fname.substring(0, index) + sep + "output" + sep + "Prices." + dat.YMD() + ".csv";
     //return fname.substring(0, index) + sep + "output" + sep + file.getName() + "." + dat.YMD() + "-" + dat.HourOfDay() + dat.MinutesInteger() + dat.SecondsInteger() + ".csv";
     }
     */
    public DefaultListModel<String> getControllersListModel() {
        return ch.getControllersListModel();
    }

    public JLabel getTimeLabel() {
        return timeLabel;
    }

    public void setTimeLabel(JLabel timeLabel) {
        this.timeLabel = timeLabel;
    }

    public boolean isStep() {
        return step;
    }

    public void setStep(boolean step) {
        this.step = step;
    }

    private int getType(BaseControlBuild controlBuild) throws JAXBException, IOException, Exception {

        String stype = controlBuild.getType();
        if (stype == null) {
            throw new Exception("Type not specified");
        }

        if (stype.equals("Robot")) {
            return ROBOT;
        } else if (stype.equals("Model")) {
            return MODEL;
        }

        return 0;
    }

    private String getDisplayFile(File file) {
        String fname = file.getAbsolutePath();
        String sep = File.separator;
        int index = fname.lastIndexOf(sep);
        return fname.substring(0, index) + sep + "files" + sep + "display" + sep + file.getName() + ".display";
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /*public ControlHierarchy getCh() {
     return ch;
     }*/
    private HashMap<String, ControlDisplayTypes> getDisplayTypes() throws ClassNotFoundException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        HashMap<String, ControlDisplayTypes> cdtrtn = null;
        try {
            cdtrtn = readDisplayTypes(displayFile);
        } catch (FileNotFoundException ex) {
            cdtrtn = new HashMap<String, ControlDisplayTypes>();
        } catch (IOException ex) {
            cdtrtn = new HashMap<String, ControlDisplayTypes>();
        }
        return cdtrtn;
    }

    private HashMap<String, ControlDisplayTypes> readDisplayTypes(String path) throws FileNotFoundException, IOException, ClassNotFoundException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        HashMap<String, ControlDisplayTypes> cdtrtn = new HashMap<String, ControlDisplayTypes>();
        BufferedReader in = new BufferedReader(new FileReader(path));
        String line;
        while ((line = in.readLine()) != null) {
            if (!line.startsWith("#")) {
                String[] arr = line.split("_");
                String name = arr[0];
                ControlDisplayTypes cdt = cdtrtn.get(name);
                if (cdt == null) {
                    cdt = new ControlDisplayTypes(name);
                    cdtrtn.put(name, cdt);
                }
                cdt.setParameter(arr[1]);
            }
        }

        return cdtrtn;

    }

    /*
     public void setLayerPanel(JPanel lp) {
     this.layersPanel = lp;
     }*/
    public void constructGUI(JPanel layersPanel) {

        layersPanel.removeAll();
        //.invalidate();
        layersPanel.setLayout(new SpringLayout());
        int rows = addLayersPanels(layersPanel);

        //Lay out the panel.
        SpringUtilities.makeCompactGrid(layersPanel,
                rows, 1, //rows, cols
                6, 6, //initX, initY
                6, 6);       //xPad, yPad
    }

    public ControlHierarchy getControlHierarchy() {
        return ch;
    }

    public void setControlHierarchy(ControlHierarchy ch) {
        this.ch = ch;
    }

    private int addLayersPanels(JPanel layersPanel) {
        Layers layers = ch.getXmlLayers();
        ControlLayer[] controlLayers = ch.getLayers();
        List<Layer> list = layers.getLayer();
        int size = list.size();
        int i = size - 1;
        Collections.reverse(list);

        for (Layer layer : list) {
            JPanel layerPanel = new JPanel();
            layerPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            layerPanel.setLayout(new java.awt.BorderLayout());
            JLabel label = new JLabel("Layer " + i + " " + layer.getName());
            label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            layerPanel.add(label, java.awt.BorderLayout.PAGE_START);
            layerPanel.setBackground(new Color(177, 178, 148));
            //logger.info("+++ cont gui" + i);

            JPanel controllersPanel = new JPanel();
            //GridLayout layout = new GridLayout();
            //layout.setRows(1);
            BoxLayout layout = new BoxLayout(controllersPanel, BoxLayout.LINE_AXIS);

            controllersPanel.setLayout(layout);

            addControllersPanels(layer, controlLayers[i], controllersPanel);

            layerPanel.add(controllersPanel, java.awt.BorderLayout.CENTER);

            layersPanel.add(layerPanel);
            i--;
        }

        return size;
    }

    private int addControllersPanels(Layer layer, ControlLayer controlLayer, JPanel panel) {

        int j = 0;
        for (final pct.moons.co.uk.schema.layers.Layers.Layer.Controller cont : layer.getController()) {
            JPanel newPanel = new JPanel();
            newPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            //newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.Y_AXIS));
            newPanel.setLayout(new java.awt.BorderLayout());
            //newPanel.setSize(200, 200);
            JLabel label = new JLabel(MoonsString.shortenTextFilter(cont.getName()));
            //label.setAlignmentX(Component.CENTER_ALIGNMENT);
            label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

            newPanel.add(label, java.awt.BorderLayout.PAGE_START);
            newPanel.setBackground(new Color(177, 178, 0));
            final ControlSystem csPanel = addControllerPanel(cont, controlLayer.get(j), newPanel);
            csPanel.setVisible(false);
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    LOG.log(Level.INFO, "mouse clicked {0}", cont.getName());

                    if (csPanel.isVisible()) {
                        csPanel.setVisible(false);
                        BooleanFlag b = csPanel.getDisplay();
                        b.setFlag(false);
                        //csPanel.setDisplay(b);
                    } else {
                        csPanel.setVisible(true);
                        BooleanFlag b = csPanel.getDisplay();
                        b.setFlag(true);
                        //csPanel.setDisplay(b);
                    }

                }
            });

            panel.add(newPanel);

            j++;
        }
        return j;
    }

    private ControlSystem addControllerPanel(pct.moons.co.uk.schema.layers.Layers.Layer.Controller pcontroller, uk.co.moons.control.Controller controller, JPanel panel) {

        ControlSystem csPanel = new ControlSystem(pcontroller, controller, cdts, monitor);
        panel.add(csPanel, java.awt.BorderLayout.CENTER);

        return csPanel;
    }

    public void setPlotDataItemsLimit(Integer plotDataItemsLimit) {
        this.plotDataItemsLimit = plotDataItemsLimit;
    }

    public void init() throws Exception {
        if (ch != null) {
            ch.init();
        }
    }

    @Override
    @SuppressWarnings({"SleepWhileInLoop", "CallToPrintStackTrace"})
    public void run() {
        int stepCtr = 1;

        try {
            //if (type == ROBOT && !remote) {                ch.init();            }
            ch.setRunningFlag(true);
            Environment.getInstance().setMark(System.currentTimeMillis());
            while (ch.isRunning()) {
                //if (ch.getControllerFunction("TimeRate") != null) {
                //  ch.setTimeRate(ch.getControllerFunction("TimeRate").getNeural().getOutput());
                //}

                ch.iterate();
                ch.specificProcessing();
                rate();
                if (plotPanelHelper != null) {
                    plotPanelHelper.updatePlots(ch, plotDataItemsLimit);
                }
                if (print) {
                    ch.print("+++ " + iter + " " + ch.getTime() + " " + Environment.getInstance().getRate() + " ");
                }
                if (output) {
                    ch.write();
                }
                monitor.update();
                /*if (timeLabel != null) {
                 timeLabel.setText(ch.getTime());
                 }*/
                if (speed != null) {
                    Thread.sleep(speed);
                }
                if (step) {
                    if (stepCtr++ >= stepSize) {
                        ch.setRunningFlag(false);
                        stepCtr = 1;
                    }
                }
                iter++;
            }
            if (ch != null) {
                ch.stop();
                if (!step) {
                    LOG.info("Run stopped");
                    ch.post();
                }
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(ControlPanelHelper.class.getName()).log(Level.SEVERE, null, ex);
            //System.exit(1);
        }
        //System.exit(1);
         catch (Exception ex) {
            LOG.log(Level.SEVERE, "+++ {0}", ex.toString());
            ex.printStackTrace();
            ch.setRunningFlag(false);
            //System.exit(1);
        }
        saveDisplayTypes();
    }

    public void setPrint(boolean print) {
        this.print = print;
    }

    public void rate() {
        //mark ctr rateSum rate
        long now = System.currentTimeMillis();
        long rate = now - Environment.getInstance().getMark();
        Environment.getInstance().setRate(rate);
        Environment.getInstance().setMark(now);
        //display(rates);
    }

    public List<String> getControllerFunctions(String controller) {
        return ch.getControllerFunctions(controller);
    }

    public void close() throws Exception {
        ch.close();
        ch.setFinished(true);
    }

    public void stop() throws Exception {
        ch.stop();
        Thread.sleep(1000);
        if (type == ROBOT) {
            ch.pause();
        }
    }

    /*public void start(){
     ch.start();
     }*/
    private void saveDisplayTypes() {
        Properties props = new Properties();

        for (ControlDisplayTypes cdt : cdts.values()) {
            cdt.setProperties(props);
        }
        try {
            File file = new File(displayFile.substring(0, displayFile.lastIndexOf(File.separator, displayFile.length())));
            if (!file.exists()) {
                file.mkdirs();
            }

            props.store(new FileOutputStream(displayFile), null);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ControlPanelHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ControlPanelHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getDescription() throws Exception {
        StringBuffer sb = new StringBuffer();
        Layers layers = ch.getXmlLayers();
        sb.append(layers.getDescription()).append("\n");
        for (Layer layer : layers.getLayer()) {
            sb.append("Layer - ").append(layer.getName()).append("\n");
            for (pct.moons.co.uk.schema.layers.Layers.Layer.Controller controller : layer.getController()) {
                sb.append("Controller - ").append(controller.getName()).append("\n");
                Functions functions = controller.getFunctions();

                if (functions.getInputFunctions() != null) {
                    addFunctionDescription(sb, functions.getInputFunctions().getInput(), "In  - ");
                    pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = functions.getInputFunctions().getTransfers();
                    if (transfers != null) {
                        for (ControlFunction con : transfers.getTransfer()) {
                            addFunctionDescription(sb, con, "Tr - ");
                        }
                    }
                }
                if (functions.getReferenceFunctions() != null) {
                    addFunctionDescription(sb, functions.getReferenceFunctions().getReference(), "Ref - ");
                    pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers transfers = functions.getReferenceFunctions().getTransfers();
                    if (transfers != null) {
                        for (ControlFunction con : transfers.getTransfer()) {
                            addFunctionDescription(sb, con, "Tr - ");
                        }
                    }
                }
                if (functions.getErrorFunctions() != null) {
                    addFunctionDescription(sb, functions.getErrorFunctions().getError(), "Err - ");
                    pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.ErrorFunctions.Transfers transfers = functions.getErrorFunctions().getTransfers();
                    if (transfers != null) {
                        for (ControlFunction con : transfers.getTransfer()) {
                            addFunctionDescription(sb, con, "Tr - ");
                        }
                    }
                }
                if (functions.getOutputFunctions() != null) {
                    addFunctionDescription(sb, functions.getOutputFunctions().getOutput(), "Out - ");
                    pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = functions.getOutputFunctions().getTransfers();
                    if (transfers != null) {
                        for (ControlFunction con : transfers.getTransfer()) {
                            addFunctionDescription(sb, con, "Tr - ");
                        }
                    }
                }
            }
        }

        return sb.toString();//ch.getXmlLayers().getDescription();
    }

    private void addFunctionDescription(StringBuffer sb, ControlFunction function, String prefix) throws Exception {

        if (function != null) {
            String name = function.getName();
            String description = function.getDescription();
            if (description == null) {
                throw new Exception("Description of function " + name + " is null");
            }
            sb.append(prefix).append(name).append(" - ").append(description).append("\n");
        }
    }

    public boolean isFinished() {
        return ch.isFinished();
    }

    public void setFinished(boolean b) {
        ch.setFinished(b);
    }
}
