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

import java.io.File;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.logging.Logger;
import uk.co.moons.control.functions.BaseControlFunction;
import uk.co.moonsit.utils.timing.DateAndTime;

/**
 *
 * @author ReStart
 */
public abstract class BaseControlHierarchy implements ControlHierarchyInterface {

    private static final Logger logger = Logger.getLogger(BaseControlHierarchy.class.getName());

    protected boolean runningFlag = true;
    protected boolean pauseFlag = false;
    protected String unitTestString;
    protected String outputFile;
    protected String config;
    protected boolean finished = false;
    protected ControlLayer[] layers;
    protected int type = 0;
    protected boolean output = false;
    protected double timeRate = 0;
    protected String configPath = null;
    protected String fileNamePrefix = null;

    public BaseControlHierarchy() {
        //Locale aLocale = new Locale.Builder().setLanguage("hr").setRegion("HR").build();
        Locale aLocale = new Locale.Builder().setLanguage("en").setRegion("GB").build();
        Locale.setDefault(aLocale);
    }

    public ControlLayer[] getLayers() {
        return layers;
    }

    public void setTimeRate(double timeRate) {
        this.timeRate = timeRate / 360000; // input timeRate in milliseconds
    }

    @Override
    public String getTime() {
        DateAndTime dat = new DateAndTime();
        return dat.HMSS();
    }
    
     public String getConfigPath() {
        return configPath;
    }

    public String getFileNamePrefix() {
        return fileNamePrefix;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        output = true;
        File f = new File(outputFile);
        if (f.exists()) {
            f.delete();
        }
        this.outputFile = outputFile;
    }

    public String getUnitTestString() {
        return unitTestString;
    }

    public void setUnitTestString(String unitTestString) {
        this.unitTestString = unitTestString;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    protected String getOutputFile(File file, String date) {
        DateAndTime dat = new DateAndTime();
        String fname = file.getAbsolutePath();
        String sep = File.separator;
        int index = fname.lastIndexOf(sep);
        return fname.substring(0, index) + sep + "output" + sep + file.getName() + "." + date + "-" + dat.YMD() + "-" + dat.HMS() + ".csv";
        //return fname.substring(0, index) + sep + "output" + sep + file.getName() + "." + dat.YMD() + "-" + dat.HourOfDay() + dat.MinutesInteger() + dat.SecondsInteger() + ".csv";
    }

    @Override
    public boolean isRunning() {
        return runningFlag;
    }

    @Override
    public void setRunningFlag(boolean runningFlag) {
        this.runningFlag = runningFlag;
    }

    public boolean isPaused() {
        return pauseFlag;
    }

    public void setPauseFlag(boolean pauseFlag) {
        this.pauseFlag = pauseFlag;
    }

    @Override
    public void stop() throws Exception {
        runningFlag = false;
    }

    public Controller getController(int layer, int system) {
        int ctr = 0;
        Controller con = null;
        ListIterator<Controller> li = layers[layer].listIterator();

        while (li.hasNext()) {
            con = li.next();
            if (ctr == system) {
                break;
            }
            ctr++;
        }

        return con;
    }

    public String getDelimitedString() {
        StringBuilder rtn = new StringBuilder();
        for (int layer = 0; layer < layers.length; layer++) {
            //String prefix = " ly " + layer;
            //rtn.append(prefix);
            int systems = layers[layer].size();
            for (int system = 0; system < systems; system++) {

                if (getController(layer, system).getInputFunction1() != null) {
                    List<BaseControlFunction> transfers = getController(layer, system).getInputFunction1().getTransferFunctions();
                    //rtn.append(" * ");
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            rtn.append(String.format("%6.2f|", transfer.getValue()));
                        }
                    }
                    rtn.append(String.format("%6.2f|", getController(layer, system).getInputFunction1().getMainFunction().getValue()));
                }
                if (getController(layer, system).getReferenceFunction1() != null) {
                    List<BaseControlFunction> transfers = getController(layer, system).getReferenceFunction1().getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            rtn.append(String.format("%6.2f|", transfer.getValue()));
                        }
                    }
                    rtn.append(String.format("%6.2f|", getController(layer, system).getReferenceFunction1().getMainFunction().getValue()));
                }
                if (getController(layer, system).getErrorFunction1() != null) {
                    rtn.append(String.format("%6.2f|", getController(layer, system).getErrorFunction1().getMainFunction().getValue()));
                    List<BaseControlFunction> transfers = getController(layer, system).getErrorFunction1().getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            rtn.append(String.format("%6.2f|", transfer.getValue()));
                        }
                    }
                }
                if (getController(layer, system).getOutputFunction1() != null) {
                    rtn.append(String.format("%6.2f|", getController(layer, system).getOutputFunction1().getMainFunction().getValue()));
                    List<BaseControlFunction> transfers = getController(layer, system).getOutputFunction1().getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            rtn.append(String.format("%6.2f|", transfer.getValue()));
                        }
                    }
                }
            }
        }
        return rtn.toString();
    }

    @Override
    public void setValues(String values) {
        String[] array = values.split("\\|");
        int i = 0;
        for (int layer = 0; layer < layers.length; layer++) {
            int systems = layers[layer].size();
            for (int system = 0; system < systems; system++) {

                if (getController(layer, system).getInputFunction1() != null) {
                    List<BaseControlFunction> transfers = getController(layer, system).getInputFunction1().getTransferFunctions();
                    //rtn.append(" * ");
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            transfer.setValue(Double.parseDouble(array[i++]));
                        }
                    }
                    getController(layer, system).getInputFunction1().getMainFunction().setValue(Double.parseDouble(array[i++]));
                }
                if (getController(layer, system).getReferenceFunction1() != null) {
                    List<BaseControlFunction> transfers = getController(layer, system).getReferenceFunction1().getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            transfer.setValue(Double.parseDouble(array[i++]));
                        }
                    }
                    getController(layer, system).getReferenceFunction1().getMainFunction().setValue(Double.parseDouble(array[i++]));
                }
                if (getController(layer, system).getErrorFunction1() != null) {
                    getController(layer, system).getErrorFunction1().getMainFunction().setValue(Double.parseDouble(array[i++]));
                    List<BaseControlFunction> transfers = getController(layer, system).getErrorFunction1().getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            transfer.setValue(Double.parseDouble(array[i++]));
                        }
                    }
                }
                if (getController(layer, system).getOutputFunction1() != null) {
                    getController(layer, system).getOutputFunction1().getMainFunction().setValue(Double.parseDouble(array[i++]));
                    List<BaseControlFunction> transfers = getController(layer, system).getOutputFunction1().getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            transfer.setValue(Double.parseDouble(array[i++]));
                        }
                    }
                }
            }
        }

    }

    @Override
    public void setValues(double[] array) {
        int i = 0;
        for (int layer = 0; layer < layers.length; layer++) {
            int systems = layers[layer].size();
            for (int system = 0; system < systems; system++) {

                if (getController(layer, system).getInputFunction1() != null) {
                    List<BaseControlFunction> transfers = getController(layer, system).getInputFunction1().getTransferFunctions();
                    //rtn.append(" * ");
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            transfer.setValue(array[i++]);
                        }
                    }
                    getController(layer, system).getInputFunction1().getMainFunction().setValue(array[i++]);
                }
                if (getController(layer, system).getReferenceFunction1() != null) {
                    List<BaseControlFunction> transfers = getController(layer, system).getReferenceFunction1().getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            transfer.setValue(array[i++]);
                        }
                    }
                    //if(getController(layer, system).getReferenceFunction1().getMainFunction().getName().equalsIgnoreCase("LongitudeControlReference")){
                    //  logger.info(String.valueOf(array[i]));
                    //}
                    getController(layer, system).getReferenceFunction1().getMainFunction().setValue(array[i++]);
                }
                if (getController(layer, system).getErrorFunction1() != null) {
                    getController(layer, system).getErrorFunction1().getMainFunction().setValue(array[i++]);
                    List<BaseControlFunction> transfers = getController(layer, system).getErrorFunction1().getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            transfer.setValue(array[i++]);
                        }
                    }
                }
                if (getController(layer, system).getOutputFunction1() != null) {
                    getController(layer, system).getOutputFunction1().getMainFunction().setValue(array[i++]);
                    List<BaseControlFunction> transfers = getController(layer, system).getOutputFunction1().getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            transfer.setValue(array[i++]);
                        }
                    }
                }
            }
        }

    }
}
