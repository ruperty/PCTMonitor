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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import org.openide.util.Exceptions;
import pct.moons.co.uk.schema.layers.ControlFunction;
import pct.moons.co.uk.schema.layers.Layers;
import pct.moons.co.uk.schema.layers.Layers.Layer;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.ControlHierarchy;
import uk.co.moons.control.ControlLayer;
import uk.co.moons.gui.components.ControlParameters;
import uk.co.moons.gui.controlpanel.display.ControlDisplayTypes;
import uk.co.moons.gui.layout.SpringUtilities;
import uk.co.moonsit.utils.timing.DateAndTime;
import uk.co.moonsit.utils.MoonsString;

/**
 *
 * @author ReStart
 */
public class ParameterslPanelHelper implements Runnable {

    private static final Logger LOG = Logger.getLogger(ParameterslPanelHelper.class.getName());
    private ControlHierarchy ch;
    private JPanel layersPanel;
    private JLabel timeLabel;
    private HashMap<String, ControlDisplayTypes> cdts;
    private String displayFile;
    //private String outputFile;
    private final int ROBOT = 1;
    private final int TRADING = 2;
    private int type;
    private final int iter = 1;
    //private Long speed;
    private boolean step = false;

    public ParameterslPanelHelper() {
    }

    public ControlHierarchy getControlHierarchy() {
        return ch;
    }

    public void setControlHierarchy(ControlHierarchy ch) {
        this.ch = ch;
    }

    private String getOutputFile(File file) {
        DateAndTime dat = new DateAndTime();
        String fname = file.getAbsolutePath();
        String sep = File.separator;
        int index = fname.lastIndexOf(sep);
        return fname.substring(0, index) + sep + "output" + sep + file.getName() + "." + dat.YMD() + "-" + dat.HMS() + ".csv";
        //return fname.substring(0, index) + sep + "output" + sep + file.getName() + "." + dat.YMD() + "-" + dat.HourOfDay() + dat.MinutesInteger() + dat.SecondsInteger() + ".csv";
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

    /*
    private int getType(String file) throws JAXBException, IOException, Exception {

        BaseControlBuild controlBuild = ControlBuildFactory.getControlBuildFunction(file);
        controlBuild.getLayers();

        String stype = controlBuild.getType();
        if (stype == null) {
            throw new Exception("Type not specified");
        }

        if (stype.equals("Trading")) {
            return this.TRADING;
        } else if (stype.equals("Robot")) {
            return this.ROBOT;
        }

        return 0;
    }

    private String getDisplayFile(File file) {
        String fname = file.getAbsolutePath();
        String sep = File.separator;
        int index = fname.lastIndexOf(sep);
        return fname.substring(0, index) + sep + "display" + sep + file.getName() + ".display";
    }
     */
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /*
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
        String line = null;
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
     */
    public void setLayerPanel(JPanel layersPanel) {
        this.layersPanel = layersPanel;
    }

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

    private int addLayersPanels(JPanel layersPanel) {
        Layers layers = ch.getXmlLayers();
        ControlLayer[] controlLayers = ch.getLayers();
        final List<Layer> list = layers.getLayer();
        int size = list.size();
        int i = size - 1;
        //Collections.reverse(list);

        for (final Layer layer : list) {
            JPanel layerPanel = new JPanel();
            layerPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            layerPanel.setLayout(new java.awt.BorderLayout());
            JLabel label = new JLabel("Layer " + i + " " + layer.getName());
            LOG.info("Layer " + i + " " + layer.getName());
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

            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    //LOG.log(Level.INFO, "mouse clicked {0}", layer.getName());

                    try {
                        saveParameters(list);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }

                }
            });

        }

        return size;
    }

    private void saveParameters(List<Layer> list) throws IOException {
        String filePath = ch.getConfigPath();
        int index = filePath.lastIndexOf(File.separator);
        int xmlindex = filePath.lastIndexOf(File.separator + "xml");
        //String path = filePath.substring(0, index);
        String xmlpath = filePath.substring(0, xmlindex);

        File dir = new File(xmlpath + File.separator + "parameters");
        if (!dir.exists()) {
            dir.mkdir();
        }

        File fileNamePath = new File(dir.getAbsolutePath() + File.separator + filePath.substring(index, filePath.length() - 3) + "pars");

        StringBuilder sb = new StringBuilder();

        for (final Layer layer : list) {
            for (pct.moons.co.uk.schema.layers.Layers.Layer.Controller cont : layer.getController()) {

                Functions functions = cont.getFunctions();
                if (functions.getInputFunctions() != null) {
                    pct.moons.co.uk.schema.layers.ControlFunction func = functions.getInputFunctions().getInput();
                    List<Parameters> inputPars = functions.getInputFunctions().getInput().getNeuralFunction().getParameters();
                    if (inputPars.size() > 0 && dataTypesNotEmpty(inputPars)) {
                        for (Parameters par : inputPars) {
                            if (!par.getDataType().isEmpty()) {
                                sb.append(func.getName()).append("_").append(par.getName()).append("=").append(par.getValue()).append("\n");
                            }
                        }
                    }
                    if (functions.getInputFunctions().getTransfers() != null) {
                        for (pct.moons.co.uk.schema.layers.ControlFunction transfer : functions.getInputFunctions().getTransfers().getTransfer()) {
                            List<Parameters> transferPars = transfer.getNeuralFunction().getParameters();
                            if (transferPars.size() > 0 && dataTypesNotEmpty(transferPars)) {
                                for (Parameters par : transferPars) {
                                    if (!par.getDataType().isEmpty()) {
                                        sb.append(transfer.getName()).append("_").append(par.getName()).append("=").append(par.getValue()).append("\n");
                                    }
                                }
                            }
                        }
                    }
                }
                if (functions.getReferenceFunctions() != null) {
                    pct.moons.co.uk.schema.layers.ControlFunction func = functions.getReferenceFunctions().getReference();
                    List<Parameters> refPars = func.getNeuralFunction().getParameters();
                    if (refPars.size() > 0 && dataTypesNotEmpty(refPars)) {
                        for (Parameters par : refPars) {
                            if (!par.getDataType().isEmpty()) {
                                sb.append(func.getName()).append("_").append(par.getName()).append("=").append(par.getValue()).append("\n");

                            }
                        }
                    }
                    if (functions.getReferenceFunctions().getTransfers() != null) {
                        for (pct.moons.co.uk.schema.layers.ControlFunction transfer : functions.getReferenceFunctions().getTransfers().getTransfer()) {
                            List<Parameters> transferPars = transfer.getNeuralFunction().getParameters();
                            if (transferPars.size() > 0 && dataTypesNotEmpty(transferPars)) {
                                for (Parameters par : transferPars) {
                                    if (!par.getDataType().isEmpty()) {
                                        sb.append(transfer.getName()).append("_").append(par.getName()).append("=").append(par.getValue()).append("\n");
                                    }
                                }
                            }
                        }
                    }
                }
                if (functions.getErrorFunctions() != null) {
                    pct.moons.co.uk.schema.layers.ControlFunction func = functions.getErrorFunctions().getError();
                    List<Parameters> errPars = functions.getErrorFunctions().getError().getNeuralFunction().getParameters();
                    if (errPars.size() > 0 && dataTypesNotEmpty(errPars)) {
                        for (Parameters par : errPars) {
                            if (!par.getDataType().isEmpty()) {
                                sb.append(func.getName()).append("_").append(par.getName()).append("=").append(par.getValue()).append("\n");
                            }
                        }
                    }
                    if (functions.getErrorFunctions().getTransfers() != null) {
                        for (pct.moons.co.uk.schema.layers.ControlFunction transfer : functions.getErrorFunctions().getTransfers().getTransfer()) {
                            List<Parameters> transferPars = transfer.getNeuralFunction().getParameters();
                            if (transferPars.size() > 0 && dataTypesNotEmpty(transferPars)) {
                                for (Parameters par : transferPars) {
                                    if (!par.getDataType().isEmpty()) {
                                        sb.append(transfer.getName()).append("_").append(par.getName()).append("=").append(par.getValue()).append("\n");
                                    }
                                }
                            }
                        }
                    }

                }
                if (functions.getOutputFunctions() != null) {
                    pct.moons.co.uk.schema.layers.ControlFunction func = functions.getOutputFunctions().getOutput();
                    List<Parameters> outputPars = functions.getOutputFunctions().getOutput().getNeuralFunction().getParameters();
                    if (outputPars.size() > 0 && dataTypesNotEmpty(outputPars)) {
                        for (Parameters par : outputPars) {
                            if (!par.getDataType().isEmpty()) {
                                sb.append(func.getName()).append("_").append(par.getName()).append("=").append(par.getValue()).append("\n");
                            }
                        }
                    }
                    if (functions.getOutputFunctions().getTransfers() != null) {
                        for (pct.moons.co.uk.schema.layers.ControlFunction transfer : functions.getOutputFunctions().getTransfers().getTransfer()) {
                            List<Parameters> transferPars = transfer.getNeuralFunction().getParameters();
                            if (transferPars.size() > 0 && dataTypesNotEmpty(transferPars)) {
                                for (Parameters par : transferPars) {
                                    if (!par.getDataType().isEmpty()) {
                                        sb.append(transfer.getName()).append("_").append(par.getName()).append("=").append(par.getValue()).append("\n");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        FileWriter fw = new FileWriter(fileNamePath);
        fw.write(sb.toString());
        fw.close();
        LOG.log(Level.INFO, "Saved parameters to {0}", fileNamePath);
    }

    private int addControllersPanels(Layer layer, ControlLayer controlLayer, JPanel panel) {

        int j = 0;
        for (pct.moons.co.uk.schema.layers.Layers.Layer.Controller cont : layer.getController()) {
            if (anyDataTypeNotEmpty(cont)) {
                JPanel newPanel = new JPanel();
                newPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                //newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.Y_AXIS));
                newPanel.setLayout(new java.awt.BorderLayout());
                //newPanel.setSize(200, 200);
                JLabel label = new JLabel(MoonsString.shortenTextFilter(cont.getName()));
                LOG.info("+++ " + controlLayer.size() + " " + j + " " + cont.getName());
                //label.setAlignmentX(Component.CENTER_ALIGNMENT);
                label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                newPanel.add(label, java.awt.BorderLayout.PAGE_START);
                newPanel.setBackground(new Color(177, 178, 0));
                addControllerPanel(cont, controlLayer.get(j), newPanel);
                panel.add(newPanel);

            }
            j++;
        }
        return j;
    }

    private boolean dataTypesNotEmpty(List<Parameters> inputPars) {

        for (Parameters par : inputPars) {
            if (!par.getDataType().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private boolean anyDataTypeNotEmpty(pct.moons.co.uk.schema.layers.Layers.Layer.Controller pcontroller) {

        {
            if (pcontroller.getFunctions().getInputFunctions() != null) {
                {
                    ControlFunction function = pcontroller.getFunctions().getInputFunctions().getInput();
                    if (dataTypesNotEmpty(function.getNeuralFunction().getParameters())) {
                        return true;
                    }
                }
                {
                    if (pcontroller.getFunctions().getInputFunctions().getTransfers() != null) {
                        for (ControlFunction function : pcontroller.getFunctions().getInputFunctions().getTransfers().getTransfer()) {
                            if (dataTypesNotEmpty(function.getNeuralFunction().getParameters())) {
                                return true;
                            }
                        }
                    }
                }

            }
        }

        {
            if (pcontroller.getFunctions().getReferenceFunctions() != null) {
                {
                    ControlFunction function = pcontroller.getFunctions().getReferenceFunctions().getReference();
                    if (dataTypesNotEmpty(function.getNeuralFunction().getParameters())) {
                        return true;
                    }
                }
                {
                    if (pcontroller.getFunctions().getReferenceFunctions().getTransfers() != null) {

                        for (ControlFunction function : pcontroller.getFunctions().getReferenceFunctions().getTransfers().getTransfer()) {
                            if (dataTypesNotEmpty(function.getNeuralFunction().getParameters())) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        {
            if (pcontroller.getFunctions().getErrorFunctions() != null) {

                {
                    ControlFunction function = pcontroller.getFunctions().getErrorFunctions().getError();
                    if (dataTypesNotEmpty(function.getNeuralFunction().getParameters())) {
                        return true;
                    }
                }
                {
                    if (pcontroller.getFunctions().getErrorFunctions().getTransfers() != null) {
                        for (ControlFunction function : pcontroller.getFunctions().getErrorFunctions().getTransfers().getTransfer()) {
                            if (dataTypesNotEmpty(function.getNeuralFunction().getParameters())) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        {
            if (pcontroller.getFunctions().getOutputFunctions() != null) {
                {
                    ControlFunction function = pcontroller.getFunctions().getOutputFunctions().getOutput();
                    if (dataTypesNotEmpty(function.getNeuralFunction().getParameters())) {
                        return true;
                    }
                }
                {
                    if (pcontroller.getFunctions().getOutputFunctions().getTransfers() != null) {
                        for (ControlFunction function : pcontroller.getFunctions().getOutputFunctions().getTransfers().getTransfer()) {
                            if (dataTypesNotEmpty(function.getNeuralFunction().getParameters())) {
                                return true;
                            }
                        }
                    }
                }
            }

        }

        return false;
    }

    private int addControllerPanel(pct.moons.co.uk.schema.layers.Layers.Layer.Controller pcontroller, uk.co.moons.control.Controller controller, JPanel panel) {
        ControlParameters cpPanel = new ControlParameters(pcontroller, controller);
        //Border border = BorderFactory.createLineBorder(Color.DARK_GRAY, 5);
        //cpPanel.setBorder(border);
        panel.add(cpPanel, java.awt.BorderLayout.CENTER);

        return 0;
    }

    public void run() {
    }

    /*
    public String getDescription() throws Exception {
    StringBuffer sb = new StringBuffer();
    Layers layers = ch.getXmlLayers();
    sb.append(layers.getDescription()).append("\n");
    for (Layer layer : layers.getLayer()) {
    sb.append("Layer - ").append(layer.getName()).append("\n");
    for (pct.moons.co.uk.schema.layers.Layers.Layer.Controller controller : layer.getController()) {
    sb.append("Controller - ").append(controller.getName()).append("\n");
    Functions functions = controller.getFunctions();
    
    addFunctionDescription(sb, functions.getInputFunctions().getInput(), "In  - ");
    addFunctionDescription(sb, functions.getReferenceFunctions().getReference(), "Ref - ");
    addFunctionDescription(sb, functions.getErrorFunctions().getError(), "Err - ");
    addFunctionDescription(sb, functions.getOutputFunctions().getOutput(), "Out - ");
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
     * 
     */
}
