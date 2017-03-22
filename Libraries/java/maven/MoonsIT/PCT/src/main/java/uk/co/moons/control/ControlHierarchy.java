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
package uk.co.moons.control;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import pct.moons.co.uk.schema.layers.Layers;
import uk.co.moons.control.functions.BaseControlFunction;
import uk.co.moons.control.neural.BaseNeuralFunction;
import uk.co.moonsit.utils.Environment;
import uk.co.moonsit.utils.timing.Time;
import uk.co.moons.config.BaseControlBuild;
import uk.co.moons.config.ControlBuildFactory;
import uk.co.moons.config.ODGConfig;
import uk.co.moons.config.XmlConfig;

public class ControlHierarchy extends BaseControlHierarchy {

    private static final Logger logger = Logger.getLogger(ControlHierarchy.class.getName());
    //protected BaseControlConfig controlConfig;
    private List<String> listOutputFunctions = null;
    protected List<String> orderedControllers = null;
    protected HashMap<String, Controller> hmControllers = null;
    protected HashMap<String, BaseControlFunction> hmControls = null;
    private BaseControlBuild controlBuild = null;
    protected boolean print = false;
    private long runTime = 0;
    private long runIter = 0;
    private final boolean debug = false;

    public ControlHierarchy() {
        super();
        layers = new ControlLayer[1];
        layers[0] = new ControlLayer();
        //listOutputFunctions = Environment.getInstance().getListOutputFunctions();
    }

    public ControlHierarchy(BaseControlBuild controlBuild) throws Exception {
        super();
        this.layers = controlBuild.getLayers();
        this.hmControllers = controlBuild.getHmControllers();
        this.hmControls = controlBuild.getHmControls();
        this.orderedControllers = controlBuild.getOrderedControllers();

        List<String> lof = Environment.getInstance().getListOutputFunctions();
        if (lof == null) {
            listOutputFunctions = new ArrayList<>();
        } else {
            listOutputFunctions = lof;
        }
    }

    
    public ControlHierarchy(String config) throws Exception {
        super();

        configPath = processODG(config);
        fileNamePrefix = configPath.substring(configPath.lastIndexOf(File.separator) + 1, configPath.lastIndexOf("."));
        controlBuild = ControlBuildFactory.getControlBuildFunction(configPath);
        this.layers = controlBuild.getLayers();
        this.hmControllers = controlBuild.getHmControllers();
        this.hmControls = controlBuild.getHmControls();
        this.orderedControllers = controlBuild.getOrderedControllers();

        List<String> lof = Environment.getInstance().getListOutputFunctions();
        if (lof == null) {
            listOutputFunctions = new ArrayList<>();
        } else {
            listOutputFunctions = lof;
        }
    }

    public ControlHierarchy(int num) throws Exception {
        super();
        controlBuild = ControlBuildFactory.getControlBuildFunction(num);
        this.hmControllers = controlBuild.getHmControllers();
        this.hmControls = controlBuild.getHmControls();
        this.orderedControllers = controlBuild.getOrderedControllers();
        this.layers = controlBuild.getLayers();
    }

    
    private String processODG(String config) throws Exception {
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
    }

    
    public void setListOutputFunctions(List<String> listOutputFunctions) {
        this.listOutputFunctions = listOutputFunctions;
    }

    public BaseControlBuild getControlBuild() {
        return controlBuild;
    }

    public void setControlBuild(BaseControlBuild controlBuild) {
        this.controlBuild = controlBuild;
    }

    //public Controller addController(int layer, BaseNeuralFunction[] neurals) {
    //  return controlBuild.addController(layer, neurals);
    //}
    @Override
    public void updateTime() {

    }

    public void setPrint(boolean print) {
        this.print = print;
    }

    public DefaultListModel<String> getControllersListModel() {
        DefaultListModel<String> lm = new DefaultListModel<>();

        Set<String> set = hmControllers.keySet();
        for (String str : set) {
            lm.addElement(str);
        }

        return lm;
    }

    public Layers getXmlLayers() {
        Layers lys = null;
        if (controlBuild.getControlConfig() instanceof XmlConfig) {
            lys = ((XmlConfig) controlBuild.getControlConfig()).getLayers();
        }
        return lys;
    }

    @Override
    public ControlLayer[] getLayers() {
        return layers;
    }

    private void orderedInit() throws Exception {

        for (String con : orderedControllers) {
            Controller cont = hmControllers.get(con);
            if (cont == null) {
                throw new Exception("+++ controller " + con + " doesn't exist");
            }
            cont.init();
        }
    }

    @Override
    public void init() throws Exception {
        if (orderedControllers != null && orderedControllers.size() > 0) {
            orderedInit();
        } else {
            for (String name : hmControls.keySet()) {
                initNeuralFunction(name);
            }
        }
    }

    protected boolean initNeuralFunction(String function) throws Exception {
        boolean rtn = false;
        BaseControlFunction bcf = hmControls.get(function);
        if (bcf != null) {
            BaseNeuralFunction nf = bcf.getNeural();
            nf.init();
            bcf.setValue(nf.getOutput());
            rtn = true;
        }
        return rtn;
    }

    public void post() throws Exception {
    }

    public void setLayers(ControlLayer[] layers) {
        this.layers = layers;
    }

    public void setRunTime(long runTime) {
        this.runTime = runTime;
    }

    public void setRunIter(long runIter) {
        this.runIter = runIter;
    }

    @Override
    public void run() throws NoSuchMethodError, Exception {
        boolean run = true;
        int iter = 0;
        long end = 0;
        if (runTime > 0) {
            end = System.currentTimeMillis() + runTime;
        }

        setRunningFlag(true);
        logger.log(Level.INFO, "+++ print is {0}", print);
        logger.log(Level.INFO, "+++ isRunning() is {0}", isRunning());
        Environment.getInstance().setMark(System.currentTimeMillis());

        while (run) {
            iterate();
            if (layers[0].isEmpty()) {
                throw new Exception("Layers empty");
            }
            rate();
            if (print) {
                print("+++ iter " + iter + " " + Environment.getInstance().getRate() + " ");
            }
            if (output) {
                write(outputFile);
            }
            iter++;
            if (runTime > 0) {
                if (System.currentTimeMillis() > end) {
                    run = false;
                    setRunningFlag(false);
                    logger.info("+++ run time ended");
                }
            }
            if (iter == runIter) {
                run = false;
                setRunningFlag(false);
                logger.info("+++ run iters ended");
            }
            if (!isRunning()) {
                run = false;
            }
        }
        logger.info("+++ run ended");

    }

    public void rate() {
        //mark ctr rateSum rate
        long now = System.currentTimeMillis();
        long rate = now - Environment.getInstance().getMark();
        Environment.getInstance().setRate(rate);
        Environment.getInstance().setMark(now);
        //display(rates);
    }

    /*
     public void rate(long[] rates) {
     //mark ctr rateSum rate
     long now = System.currentTimeMillis();
     rates[3] = now - rates[0];
     //logger.info("+++ rate " + rates[3]);
     rates[0] = now;
     Environment.getInstance().setRate(rates[3]);
     display(rates);
     }
     */
    private void display(long[] rates) {
        //mark ctr rateSum rate
        rates[2] += rates[3];
        if (rates[1] == 100) {
            //double displayRate = 0;
            rates[1] = 1;
            //displayRate = rates[2] / 100.0;
            rates[2] = 0;
            //System.out.println(displayRate);
        }
        rates[1]++;
    }

    @Override
    public void orderedIterate() throws Exception {

        for (String con : orderedControllers) {
            Controller cont = hmControllers.get(con);
            if (cont == null) {
                throw new Exception("+++ controller " + con + " doesn't exist");
            }
            cont.iterate();
        }
    }

    @Override
    public void iterate() throws Exception {
        updateTime();
        if (orderedControllers != null && orderedControllers.size() > 0) {
            orderedIterate();
        } else {
            layeredIterate();
        }

    }

    @Override
    public void layeredIterate() throws Exception {
        for (ControlLayer layer : layers) {
            ListIterator<Controller> li = layer.listIterator();
            while (li.hasNext()) {
                Controller con = li.next();
                con.iterate();
            }
        }
    }

    /*
     public String getType() {
     ControlLayer layer = layers[0];
     return layer.getType();
     }
     */
    public BaseNeuralFunction getNeuralFunction(String name) {
        BaseControlFunction bcf = hmControls.get(name);
        if (bcf == null) {
            return null;
        }
        return bcf.getNeural();
    }

    protected String formatTime(String dt, boolean decimal) {
        String time = dt;

        if (dt.length() > 12) {
            time = dt.substring(11, dt.length());
        }

        if (decimal) {
            Time t = new Time(time);
            time = String.format("%8.6f", t.getTime());
        }

        return time;
    }

    @Override
    public void setControllerParameter(String controller, String pars) {
        BaseControlFunction cont = hmControls.get(controller);
        if (cont != null) {
            cont.getNeural().setParameter(pars);
        }
    }

    @Override
    public void setControllerParameter(String controller, String parameter, String value) {
        BaseControlFunction cont = hmControls.get(controller);
        if (cont != null) {
            cont.getNeural().setParameter(parameter + ":" + value);
        }
    }

    public BaseControlFunction getControllerFunction(String function) throws Exception {
        return hmControls.get(function);
    }

    /*
     public BaseControlFunction getControllerFunction(String controller, String function) throws Exception {
     Controller cont = hmControllers.get(controller);
     if (cont == null) {
     throw new Exception("Controller " + controller + " not present in hmControllers");
     }
     {
     ControlFunctionCollection func = cont.getInputFunction1();
     if (func != null) {
     if (func.getMainFunction().getName().equals(function)) {
     return func.getMainFunction();
     }
     if (func.getTransferFunctions() != null) {
     for (BaseControlFunction transfer : func.getTransferFunctions()) {
     if (transfer != null && transfer.getName().equals(function)) {
     return transfer;
     }
     }
     }
     }
     }

     {
     ControlFunctionCollection func = cont.getErrorFunction1();
     if (func != null) {
     if (func.getMainFunction().getName().equals(function)) {
     return func.getMainFunction();
     }
     if (func.getTransferFunctions() != null) {
     for (BaseControlFunction transfer : func.getTransferFunctions()) {
     if (transfer != null && transfer.getName().equals(function)) {
     return transfer;
     }
     }
     }
     }
     }
     {
     ControlFunctionCollection func = cont.getReferenceFunction1();
     if (func != null) {
     if (func.getMainFunction().getName().equals(function)) {
     return func.getMainFunction();
     }
     if (func.getTransferFunctions() != null) {
     for (BaseControlFunction transfer : func.getTransferFunctions()) {
     if (transfer != null && transfer.getName().equals(function)) {
     return transfer;
     }
     }
     }
     }
     }
     {
     ControlFunctionCollection func = cont.getOutputFunction1();
     if (func != null) {
     if (func.getMainFunction().getName().equals(function)) {
     return func.getMainFunction();
     }
     if (func.getTransferFunctions() != null) {
     for (BaseControlFunction transfer : func.getTransferFunctions()) {
     if (transfer != null && transfer.getName().equals(function)) {
     return transfer;
     }
     }
     }
     }
     }

     return null;
     }
     */
    public List<String> getControllerFunctions(String controller) {
        List<String> list = new ArrayList<String>();

        Controller cont = hmControllers.get(controller);

        ControlFunctionCollection func = cont.getInputFunction1();
        if (func != null) {
            list.add(func.getMainFunction().getName());
            if (cont.getInputFunction1().getTransferFunctions() != null) {
                for (BaseControlFunction transfer : cont.getInputFunction1().getTransferFunctions()) {
                    if (transfer != null) {
                        list.add(transfer.getName());
                    }
                }
            }
        }

        func = cont.getErrorFunction1();
        if (func != null) {
            list.add(func.getMainFunction().getName());
            if (cont.getErrorFunction1().getTransferFunctions() != null) {
                for (BaseControlFunction transfer : cont.getErrorFunction1().getTransferFunctions()) {
                    if (transfer != null) {
                        list.add(transfer.getName());
                    }
                }
            }
        }

        func = cont.getReferenceFunction1();
        if (func != null) {
            list.add(func.getMainFunction().getName());
            if (cont.getReferenceFunction1().getTransferFunctions() != null) {
                for (BaseControlFunction transfer : cont.getReferenceFunction1().getTransferFunctions()) {
                    if (transfer != null) {
                        list.add(transfer.getName());
                    }
                }
            }
        }

        func = cont.getOutputFunction1();
        if (func != null) {
            list.add(func.getMainFunction().getName());
            if (cont.getOutputFunction1().getTransferFunctions() != null) {
                for (BaseControlFunction transfer : cont.getOutputFunction1().getTransferFunctions()) {
                    if (transfer != null) {
                        list.add(transfer.getName());
                    }
                }
            }
        }

        return list;
    }

    public String getControllerParameter(String controller, String parameter) {
        BaseControlFunction cont = hmControls.get(controller);
        if (cont == null) {
            return null;
        }
        return cont.getNeural().getConfigParameter(parameter);
    }

    public void write(String file) throws IOException {
        if (orderedControllers == null || orderedControllers.isEmpty()) {
            writeUnordered(file);
        } else {
            writeOrdered(file);
        }

    }

    public void writeOrdered(String file) throws IOException {
        //System.out.println(file);
        if (file == null) {
            return;
        }
        File f = new File(file);
        FileWriter fstream = null;
        if (!f.exists()) {
            try {
                f.getParentFile().mkdirs();
                f.createNewFile();
                fstream = new FileWriter(file, false);
                writeHeaders(fstream);
            } catch (IOException ex) {
                //Logger.getLogger(ControlHierarchy.class.getName()).log(Level.SEVERE, null, ex);
                throw new IOException("Exception when trying to create file " + f.getAbsolutePath());
            } finally {
                if (fstream != null) {
                    fstream.close();
                }
            }
        }

        fstream = new FileWriter(file, true);
        StringBuilder sb = new StringBuilder();
        String tm = getTime();
        sb.append("\"").append(formatTime(tm, false)).append("\",");
        sb.append(formatTime(tm, true)).append(",");

        for (String con : orderedControllers) {
            Controller controller = hmControllers.get(con);
            getOutputString(controller, sb);
        }

        BufferedWriter out = new BufferedWriter(fstream);
        out.write(sb.toString() + "\n");
        //Close the output stream
        out.close();
        fstream.close();
    }

    public void writeUnordered(String file) throws IOException {
        //System.out.println(file);
        if (file == null) {
            return;
        }
        File f = new File(file);
        FileWriter fstream = null;
        if (!f.exists()) {
            try {
                f.getParentFile().mkdirs();
                f.createNewFile();
                fstream = new FileWriter(file, false);
                writeHeaders(fstream);
            } catch (IOException ex) {
                //Logger.getLogger(ControlHierarchy.class.getName()).log(Level.SEVERE, null, ex);
                throw new IOException("Exception when trying to create file " + f.getAbsolutePath());
            } finally {
                if (fstream != null) {
                    fstream.close();
                }
            }
        }

        fstream = new FileWriter(file, true);
        StringBuilder sb = new StringBuilder();

        String tm = getTime();
        sb.append("\"").append(formatTime(tm, false)).append("\",");
        sb.append(formatTime(tm, true)).append(",");

        for (int layer = 0; layer < layers.length; layer++) {
            int systems = layers[layer].size();
            for (int system = 0; system < systems; system++) {
                Controller controller = getController(layer, system);
                getOutputString(controller, sb);
            }
        }

        BufferedWriter out = new BufferedWriter(fstream);
        out.write(sb.toString() + "\n");
        //Close the output stream
        out.close();
        fstream.close();
    }

    private void getOutputString(Controller controller, StringBuilder sb) {
        if (controller.getReferenceFunction1() != null) {
            List<BaseControlFunction> transfers = controller.getReferenceFunction1().getTransferFunctions();
            if (transfers != null) {
                for (BaseControlFunction transfer : transfers) {
                    if (listOutputFunctions.isEmpty() || listOutputFunctions.contains(transfer.getName())) {
                        sb.append(transfer.getValue()).append(",");
                    }
                }
            }
            if (listOutputFunctions.isEmpty() || listOutputFunctions.contains(controller.getReferenceFunction1().getMainFunction().getName())) {
                sb.append(controller.getReferenceFunction1().getMainFunction().getValue()).append(",");
            }
        }
        if (controller.getInputFunction1() != null) {
            List<BaseControlFunction> transfers = controller.getInputFunction1().getTransferFunctions();
            if (transfers != null) {
                for (BaseControlFunction transfer : transfers) {
                    if (listOutputFunctions.isEmpty() || listOutputFunctions.contains(transfer.getName())) {
                        sb.append(transfer.getValue()).append(",");
                    }
                }
            }
            if (listOutputFunctions.isEmpty() || listOutputFunctions.contains(controller.getInputFunction1().getMainFunction().getName())) {
                sb.append(controller.getInputFunction1().getMainFunction().getValue()).append(",");
            }
        }
        if (controller.getErrorFunction1() != null) {
            if (listOutputFunctions.isEmpty() || listOutputFunctions.contains(controller.getErrorFunction1().getMainFunction().getName())) {
                sb.append(controller.getErrorFunction1().getMainFunction().getValue()).append(",");
            }
            List<BaseControlFunction> transfers = controller.getErrorFunction1().getTransferFunctions();
            if (transfers != null) {
                for (BaseControlFunction transfer : transfers) {
                    if (listOutputFunctions.isEmpty() || listOutputFunctions.contains(transfer.getName())) {
                        sb.append(transfer.getValue()).append(",");
                    }
                }
            }
        }
        if (controller.getOutputFunction1() != null) {
            if (listOutputFunctions.isEmpty() || listOutputFunctions.contains(controller.getOutputFunction1().getMainFunction().getName())) {
                sb.append(controller.getOutputFunction1().getMainFunction().getValue()).append(",");
            }
            List<BaseControlFunction> transfers = controller.getOutputFunction1().getTransferFunctions();
            if (transfers != null) {
                for (BaseControlFunction transfer : transfers) {
                    if (listOutputFunctions.isEmpty() || listOutputFunctions.contains(transfer.getName())) {
                        sb.append(transfer.getValue()).append(",");
                    }
                }
            }
        }

    }

    private void writeHeaders(FileWriter fstream) throws IOException {

        if (orderedControllers == null || orderedControllers.size() == 0) {
            writeHeadersUnordered(fstream);
        } else {
            writeHeadersOrdered(fstream);
        }
    }

    private void writeHeadersOrdered(FileWriter fstream) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedWriter out = new BufferedWriter(fstream);
        sb.append("time,dtime,");

        for (String con : orderedControllers) {
            Controller controller = hmControllers.get(con);
            getOutputHeaderString(controller, sb);
        }

        out.write(sb.toString() + "\n");
        //Close the output stream
        out.close();
    }

    private void getOutputHeaderString(Controller controller, StringBuilder sb) {
        if (controller.getReferenceFunction1() != null) {
            List<BaseControlFunction> transfers = controller.getReferenceFunction1().getTransferFunctions();
            if (transfers != null) {
                for (BaseControlFunction transfer : transfers) {
                    if (listOutputFunctions.isEmpty() || listOutputFunctions.contains(transfer.getName())) {
                        sb.append("tr").append(transfer.getName()).append(",");
                    }
                }
            }
            if (listOutputFunctions.isEmpty() || listOutputFunctions.contains(controller.getReferenceFunction1().getMainFunction().getName())) {
                sb.append("ref").append(controller.getReferenceFunction1().getMainFunction().getName()).append(",");
            }
        }

        if (controller.getInputFunction1() != null) {
            List<BaseControlFunction> transfers = controller.getInputFunction1().getTransferFunctions();
            if (transfers != null) {
                for (BaseControlFunction transfer : transfers) {
                    if (listOutputFunctions.isEmpty() || listOutputFunctions.contains(transfer.getName())) {
                        sb.append("tr").append(transfer.getName()).append(",");
                    }
                }
            }
            if (listOutputFunctions.isEmpty() || listOutputFunctions.contains(controller.getInputFunction1().getMainFunction().getName())) {
                sb.append("in").append(controller.getInputFunction1().getMainFunction().getName()).append(",");
            }
        }

        if (controller.getErrorFunction1() != null) {
            if (listOutputFunctions.isEmpty() || listOutputFunctions.contains(controller.getErrorFunction1().getMainFunction().getName())) {
                sb.append("err").append(controller.getErrorFunction1().getMainFunction().getName()).append(",");
            }
            List<BaseControlFunction> transfers = controller.getErrorFunction1().getTransferFunctions();
            if (transfers != null) {
                for (BaseControlFunction transfer : transfers) {
                    if (listOutputFunctions.isEmpty() || listOutputFunctions.contains(transfer.getName())) {
                        sb.append("tr").append(transfer.getName()).append(",");
                    }
                }
            }
        }

        if (controller.getOutputFunction1() != null) {
            if (listOutputFunctions.isEmpty() || listOutputFunctions.contains(controller.getOutputFunction1().getMainFunction().getName())) {
                sb.append("out").append(controller.getOutputFunction1().getMainFunction().getName()).append(",");
            }
            List<BaseControlFunction> transfers = controller.getOutputFunction1().getTransferFunctions();
            if (transfers != null) {
                for (BaseControlFunction transfer : transfers) {
                    if (listOutputFunctions.isEmpty() || listOutputFunctions.contains(transfer.getName())) {
                        sb.append("tr").append(transfer.getName()).append(",");
                    }
                }
            }
        }
    }

    private void writeHeadersUnordered(FileWriter fstream) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedWriter out = new BufferedWriter(fstream);
        sb.append("time,dtime,");
        for (int layer = 0; layer < layers.length; layer++) {
            int systems = layers[layer].size();
            for (int system = 0; system < systems; system++) {
                Controller controller = getController(layer, system);
                getOutputHeaderString(controller, sb);
            }
        }

        out.write(sb.toString() + "\n");
        //Close the output stream
        out.close();
    }

    private String getFunctionList() throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int layer = 0; layer < layers.length; layer++) {
            int systems = layers[layer].size();
            for (int system = 0; system < systems; system++) {
                Controller cf = getController(layer, system);
                if (cf.getInputFunction1() != null) {
                    List<BaseControlFunction> transfers = getController(layer, system).getInputFunction1().getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            sb.append("tr").append(transfer.getName()).append("\n");
                        }
                    }
                    sb.append("in").append(cf.getInputFunction1().getMainFunction().getName()).append("\n");
                }

                if (cf.getReferenceFunction1() != null) {
                    List<BaseControlFunction> transfers = getController(layer, system).getReferenceFunction1().getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            sb.append("tr").append(transfer.getName()).append("\n");
                        }
                    }
                    sb.append("ref").append(cf.getReferenceFunction1().getMainFunction().getName()).append("\n");
                }

                if (cf.getErrorFunction1() != null) {
                    sb.append("err").append(cf.getErrorFunction1().getMainFunction().getName()).append("\n");
                    List<BaseControlFunction> transfers = getController(layer, system).getErrorFunction1().getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            sb.append("tr").append(transfer.getName()).append("\n");
                        }
                    }
                }

                if (cf.getOutputFunction1() != null) {
                    sb.append("out").append(cf.getOutputFunction1().getMainFunction().getName()).append("\n");
                    List<BaseControlFunction> transfers = getController(layer, system).getOutputFunction1().getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            sb.append("tr").append(transfer.getName()).append("\n");
                        }
                    }
                }
            }
        }

        return sb.toString();
    }

    public void print(String prefix) {
        System.out.print(prefix);
        System.out.print(getDetailString());
        System.out.println();
    }

    public String getDetailString() {
        StringBuilder rtn = new StringBuilder();
        for (int layer = 0; layer < layers.length; layer++) {
            String prefix = " ly " + layer;
            rtn.append(prefix);
            int systems = layers[layer].size();
            for (int system = 0; system < systems; system++) {

                if (getController(layer, system).getInputFunction1() != null) {
                    List<BaseControlFunction> transfers = getController(layer, system).getInputFunction1().getTransferFunctions();
                    rtn.append(" * ");
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            rtn.append(String.format(" TR %6.3f", transfer.getValue()));
                        }
                    }
                    rtn.append(String.format(" IN %6.3f", getController(layer, system).getInputFunction1().getMainFunction().getValue()));
                }
                if (getController(layer, system).getReferenceFunction1() != null) {
                    List<BaseControlFunction> transfers = getController(layer, system).getReferenceFunction1().getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            rtn.append(String.format(" TR %6.3f", transfer.getValue()));
                        }
                    }
                    rtn.append(String.format(" RF %6.3f", getController(layer, system).getReferenceFunction1().getMainFunction().getValue()));
                }
                if (getController(layer, system).getErrorFunction1() != null) {
                    rtn.append(String.format(" ER %6.3f", getController(layer, system).getErrorFunction1().getMainFunction().getValue()));
                    List<BaseControlFunction> transfers = getController(layer, system).getErrorFunction1().getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            rtn.append(String.format(" TR %6.3f", transfer.getValue()));
                        }
                    }
                }
                if (getController(layer, system).getOutputFunction1() != null) {
                    rtn.append(String.format(" OT %6.3f", getController(layer, system).getOutputFunction1().getMainFunction().getValue()));
                    List<BaseControlFunction> transfers = getController(layer, system).getOutputFunction1().getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            rtn.append(String.format(" TR %6.3f", transfer.getValue()));
                        }
                    }
                }
            }
        }
        return rtn.toString();
    }

    @Override
    public void specificProcessing() throws IOException, Exception {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void pause() throws Exception {
        for (String name : hmControls.keySet()) {
            //logger.log(Level.INFO, "Pausing {0}", name);
            pauseNeuralFunction(name);
        }
    }

    @Override
    public void close() throws Exception {
        long start = System.currentTimeMillis();
        if (orderedControllers != null && orderedControllers.size() > 0) {
            orderedClose();
        } else {
            layeredClose();
        }
        //for (String name : hmControls.keySet()) {
        //    closeNeuralFunction(name);
        //}
        logger.log(Level.INFO, "---> Close {0}", (System.currentTimeMillis() - start));
    }

    @Override
    public void stop() throws Exception {
        runningFlag = false;
        long start = System.currentTimeMillis();
        if (orderedControllers != null && orderedControllers.size() > 0) {
            orderedStop();
        } else {
            layeredStop();
        }
        //for (String name : hmControls.keySet()) {
        //    closeNeuralFunction(name);
        //}
        logger.log(Level.INFO, "---> Stopping {0}", (System.currentTimeMillis() - start));
    }

    private void layeredClose() throws Exception {

        for (ControlLayer layer : layers) {
            ListIterator<Controller> li = layer.listIterator();
            while (li.hasNext()) {
                Controller con = li.next();
                con.close();

            }
        }
    }

    private void layeredStop() throws Exception {

        for (ControlLayer layer : layers) {
            ListIterator<Controller> li = layer.listIterator();
            while (li.hasNext()) {
                Controller con = li.next();
                con.stop();

            }
        }
    }

    private void orderedStop() throws Exception {

        for (String con : orderedControllers) {
            Controller cont = hmControllers.get(con);
            if (cont == null) {
                throw new Exception("+++ controller " + con + " doesn't exist");
            }
            cont.stop();
        }
    }

    private void orderedClose() throws Exception {

        for (String con : orderedControllers) {
            Controller cont = hmControllers.get(con);
            if (cont == null) {
                throw new Exception("+++ controller " + con + " doesn't exist");
            }
            cont.close();
        }
    }

    public void closeNeuralFunction(String function) throws Exception {
        BaseControlFunction bcf = hmControls.get(function);
        if (bcf != null) {
            BaseNeuralFunction nf = bcf.getNeural();
            if (debug) {
                logger.log(Level.INFO, "+++ closing {0}", function);
            }
            nf.close();
        }
    }

    public void pauseNeuralFunction(String function) throws Exception {
        BaseControlFunction bcf = hmControls.get(function);
        if (bcf != null) {
            BaseNeuralFunction nf = bcf.getNeural();
            nf.pause();
        }
    }

}
