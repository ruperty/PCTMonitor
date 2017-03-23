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
package uk.co.moons.control.neural;

/**
 * BaseNeuralFunction
 *
 * @author Rupert Young Copyright Moon's IT 2007-2008
 */
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.Link;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.functions.BaseControlFunction;
import uk.co.moons.control.links.BaseSignalLink;
import uk.co.moons.control.links.SignalLink;
import uk.co.moonsit.utils.timing.DateAndTime;
import uk.co.moons.control.neural.factory.NeuralFunctionInterface;

public abstract class BaseNeuralFunction implements NeuralFunctionInterface {

    private static final Logger LOG = Logger.getLogger(BaseNeuralFunction.class.getName());
    protected double output;
    protected BaseSignalLink links;
    private List<Link> configLinks;
    protected List<Parameters> parameters;
    protected String name = null;
    protected int posindex;
    public Double initial = null;
    public Double last = null;
    public Double reset = null;
    public Boolean disabled = false;
    private HashMap<String, String> hmParameters = null;
    private final boolean debug = false;
    private HashMap<String, BaseControlFunction> hmControlFunctions;

    public BaseNeuralFunction() {
        //links = new ArrayList<BaseSignalLink>();
        links = new SignalLink();
        hmParameters = new HashMap<>();
    }

    public BaseNeuralFunction(List<Parameters> ps, List<Link> links) {

    }

    public BaseNeuralFunction(List<Parameters> ps) {
        //links = new ArrayList<BaseSignalLink>();
        links = new SignalLink();
        parameters = ps;
        hmParameters = new HashMap<>();

        for (Parameters param : ps) {
            if (param.getName().equals("Initial")) {
                initial = Double.valueOf(param.getValue());
            }
            if (param.getName().equals("Last")) {
                last = Double.valueOf(param.getValue());
            }

            if (param.getName().equals("Reset")) {
                reset = Double.valueOf(param.getValue());
            }
            if (param.getName().equals("Disabled")) {
                disabled = Boolean.valueOf(param.getValue());
            }
        }
        if (initial != null) {
            output = initial;
        }
    }

    public Boolean isDisabled() {
        return disabled;
    }

    @Override
    public Double stop() throws Exception {
        return 0.0;
    }

    @Override
    public void deactivate() throws Exception {
        output = 0;
    }

    public Double getInitial() {
        return initial;
    }

    public void setInitial(Double initial) {
        this.initial = initial;
    }

    public int getPosindex() {
        return posindex;
    }

    public void setPosindex(int posindex) {
        this.posindex = posindex;
    }

    @Override
    public double getOutput() {
        return output;
    }

    @Override
    public void setOutput(double output) {
        this.output = output;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void close() throws Exception {
        //logger.info("+++ close");
    }

    @Override
    public void pause() throws Exception {
        if (reset != null) {
            output = reset;
            LOG.log(Level.INFO, "+++ reset output of {0} to {1}", new Object[]{getName(), reset});
        }
        // logger.log(Level.INFO, "+++ output after pause of {0} to {1}", new Object[]{getName(), output});
    }

    @Override
    public String getTime() {

        DateAndTime dat = new DateAndTime();
        return dat.HMSS();
    }

    @Override
    public String getConfigParameter(String par) {
        String rtn = null;

        if (parameters != null) {
            for (Parameters param : parameters) {
                if (param.getName().equals(par)) {
                    rtn = param.getValue();
                }
            }
        }
        return rtn;
    }

    @Override
    public void setParameter(double par) {
        throw new UnsupportedOperationException("Not supported yet  - BaseNeuralFunction.setParameter()"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getParameter() {
        throw new UnsupportedOperationException("Not supported yet - BaseNeuralFunction.getParameter()"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getParameterInt(int i) throws Exception {
        throw new UnsupportedOperationException("Not supported yet - BaseNeuralFunction.getParameter(int)"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getParametersSize() {
        throw new UnsupportedOperationException("Not supported yet - BaseNeuralFunction.getParameter(int)"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getParameter(int i) {
        throw new UnsupportedOperationException("Not supported yet - BaseNeuralFunction.getParameter(int)"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getParameterName(int i) {
        throw new UnsupportedOperationException("Not supported yet - BaseNeuralFunction.getParameter(int)"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getDataString() {
        throw new UnsupportedOperationException("Not supported yet - BaseNeuralFunction.getDataString()"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getParametersString() {
        throw new UnsupportedOperationException("Not supported yet - BaseNeuralFunction.getParametersString() " + getName() + " " + getClass().getName()); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getParameter(String f) throws NoSuchFieldException, SecurityException, IllegalAccessException {
        Class<?> c = this.getClass();
        Field field = c.getField(f);
        return field.getDouble(this);
    }

    /*
    @Override
    public int getParameter(int i, boolean b) throws Exception {
        throw new UnsupportedOperationException("Not supported yet - BaseNeuralFunction.getParameter(int, b)"); //To change body of generated methods, choose Tools | Templates.
    }*/
    @SuppressWarnings("rawtypes")
    @Override
    public void setField(Field field, String value) throws IllegalArgumentException, IllegalAccessException {
        //Type type = field.getGenericType();//.getType();

        Class myclass = field.getType();
        String type = myclass.getName();
        LOG.log(Level.INFO, "+++ type {0} value {1} name {2}", new Object[]{type, value, this.getName()});
        if (type.equals("java.lang.String")) {
            //logger.log(Level.INFO, "+++ string set to {0}", value);
            field.set(this, value);
        }

        if (type.equals("java.lang.Double")) {
            LOG.log(Level.INFO, "+++ Double set to {0}", value);
            Double dvalue;

            switch (value) {
                case "i":
                case "inf":
                    dvalue = Double.POSITIVE_INFINITY;
                    break;
                case "-i":
                case "-inf":
                    dvalue = Double.NEGATIVE_INFINITY;
                    break;
                default:
                    dvalue = new Double(value);
            }

            field.set(this, dvalue);
        }

        if (type.equals("java.lang.Integer")) {
            //logger.log(Level.INFO, "+++ Integer set to {0}", value);
            field.set(this, new Integer(value));
        }

        if (type.equals("java.lang.Boolean")) {
            //logger.log(Level.INFO, "+++ Boolean set to {0}", value);
            field.set(this, Boolean.valueOf(value));
        }

        if (type.equals("java.lang.Long")) {
            //logger.log(Level.INFO, "+++ Long set to {0}", value);
            field.set(this, Long.valueOf(value));
        }

        if (type.equals("int")) {
            //logger.log(Level.INFO, "+++ int set to {0}", value);
            field.setInt(this, Integer.parseInt(value));
        }

        if (type.equals("boolean")) {
            //logger.log(Level.INFO, "+++ boolean set to {0}", value);
            field.setBoolean(this, Boolean.valueOf(value));
        }

        if (type.equals("long")) {
            //logger.log(Level.INFO, "+++ long set to {0}", value);
            field.setLong(this, Long.valueOf(value));
        }

        if (type.equals("double")) {
            //logger.log(Level.INFO, "+++ double set to {0}", value);
            field.setDouble(this, Double.parseDouble(value));
        }

    }

    synchronized private void addParameterToHashMap(String key, String value) {
        hmParameters.put(key, value);
    }

    synchronized public void clearParametersHashMap() {
        hmParameters.clear();
    }

    synchronized public Collection<String> getParametersSet() {
        return hmParameters.values();
    }

    @Override
    public void setParameter(String par) {
        //logger.info("+++ " + par);
        String[] arr = par.split(":");
        addParameterToHashMap(arr[0], par);
        Class<?> c = this.getClass();
        try {
            Field field = c.getField(arr[0].toLowerCase());
            //Field field = c.getDeclaredField(arr[0].toLowerCase());
            setField(field, arr[1]);
        } catch (IllegalAccessException | IllegalArgumentException | SecurityException ex) {
            Logger.getLogger(BaseNeuralFunction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(BaseNeuralFunction.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }

        LOG.log(Level.INFO, "+++ values {0} {1} {2}", new Object[]{this.getName(), arr[0], arr[1]});

        if (parameters == null) {
            LOG.info("+++ parameters is null");
        } else {
            LOG.info("+++ parameters is not null");
            for (Parameters param : parameters) {
                if (param.getName().equals(arr[0])) {
                    param.setValue(arr[1]);
                }
            }
        }

        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setConfigLinks(List<Link> ls) {
        configLinks = ls;
    }

    public BaseControlFunction getExternalControlFunction(String name) {
        return hmControlFunctions.get(name);
    }

    public void setLinks(HashMap<String, BaseControlFunction> hmControls) throws Exception {

        if (hmControlFunctions == null) {
            hmControlFunctions = hmControls;
        }

        if (configLinks.isEmpty()) {
            verifyConfiguration();

            return;
        }
        // clear default links to be overridden by xml config
        links.getControlList().clear();
        for (Link link : configLinks) {
            if (debug) {
                LOG.log(Level.INFO, "+++ Adding link {0}", link.getName());
            }
            String lname = link.getName();

            BaseControlFunction control = hmControls.get(lname);
            if (control == null) {
                throw new Exception("Function " + link.getName() + " indicated by link is missing. If function exists in ODG drawing ensure it is less than 3cm from linked function.");
            }
            //control.setLinkType(link.getType());
            links.addControl(control);
            links.addType(link.getType());
        }
        verifyConfiguration();
        //addLink(signalLink);
    }

    public void verifyConfiguration() throws Exception {
    }

    public void setLink(BaseControlFunction control) throws Exception {
        //BaseSignalLink signalLink = new SignalLink();
        links.addControl(control);
        //addLink(signalLink);
    }

    public void post() throws Exception {
    }

    public void init() throws Exception {
        // fot jit compiler I think
        List<BaseControlFunction> controls = links.getControlList();
        if (controls.size() > 0) {
            controls.get(0).getValue();
        }

    }

    /*
     public void addLink(BaseSignalLink link) {
     links.add(link);
     }
     */
}
