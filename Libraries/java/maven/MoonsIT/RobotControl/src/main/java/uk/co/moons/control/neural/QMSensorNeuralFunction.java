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

import java.util.List;
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.functions.BaseControlFunction;
import uk.co.moons.sensors.QMSensor;
import uk.co.moonsit.messaging.QMState;
import uk.co.moonsit.utils.MoonsString;

/**
 *
 * @author Rupert Young
 */
public class QMSensorNeuralFunction extends NeuralFunction {

    private static final Logger LOG = Logger.getLogger(QMSensorNeuralFunction.class.getName());

    public boolean subscribe = true;
    public String host = "localhost";
    public Integer port = 30000;
    public Integer timeout = 30000;
    public double targetpercentage = 10;
    public Double timelimit = null;

    private QMSensor sensor;
    private boolean first = true;
    // parameters subscribed from elsewhere
    public double simulatedtime;
    public double atomx;
    public double fidelity;
    public double fidelityscore;
    public double timescore;
    public double score;
    public double energy;
    public double targetx;
    public double laserx;
    public double lasery;
    public double minx;
    public double maxx;
    public double startx;
    public double starty;
    public double qmreset = 0.0;

    private String exe = "C:\\packages\\Perceptual learning-20170103T142628Z\\Perceptual learning\\Builds\\2017-01-03\\Server\\Quantum Moves.exe";
    private int x = 400;
    private int y = 0;
    private int width = 1024;
    private int height = 750;
    private int level = 0;

    private Integer qmResetIndex = null;
    private Integer qmLevelIndex = null;
    private boolean connected = true;

    public QMSensorNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {

            if (param.getName().equals("Exe")) {
                exe = param.getValue();
            }
            if (param.getName().equals("X")) {
                x = Integer.parseInt(param.getValue());
            }
            if (param.getName().equals("Y")) {
                y = Integer.parseInt(param.getValue());
            }
            if (param.getName().equals("Width")) {
                width = Integer.parseInt(param.getValue());
            }
            if (param.getName().equals("Height")) {
                height = Integer.parseInt(param.getValue());
            }
            if (param.getName().equals("Subscribe")) {
                subscribe = Boolean.parseBoolean(param.getValue());
            }

            //if (param.getName().equals("Level")) {
              //  level = Integer.parseInt(param.getValue());
            //}
            if (param.getName().equals("Host")) {
                host = param.getValue();
            }
            if (param.getName().equals("Port")) {
                port = Integer.parseInt(param.getValue());
            }
            if (param.getName().equals("Timeout")) {
                timeout = Integer.parseInt(param.getValue());
            }
            if (param.getName().equals("TargetPercentage")) {
                targetpercentage = Double.parseDouble(param.getValue());
            }
            if (param.getName().equals("TimeLimit")) {
                timelimit = Double.parseDouble(param.getValue());
            }
        }
        /*if (host != null) {
            if (level == 0) {
                throw new Exception("Missing configuration value for level");
            }
        }*/

    }

    @Override
    public void verifyConfiguration() throws Exception {
        List<BaseControlFunction> controls = links.getControlList();
        for (int i = 0; i < controls.size(); i++) {
            String linkType = links.getType(i);
            if (linkType == null) {
                continue;
            }
            if (linkType.equals("QMReset")) {
                qmResetIndex = i;
                continue;
            }
            if (linkType.equals("QMLevel")) {
                qmLevelIndex = i;
                level = (int) controls.get(qmLevelIndex).getNeural().getOutput();
            }
        }

        if (qmResetIndex == null) {
            throw new Exception("Reset link of type QMReset has not been configured");
        }
        if (qmLevelIndex == null) {
            throw new Exception("Level link of type QMLevel has not been configured");
        }
    }

    @Override
    public void init() throws Exception {
        //super.init();
        if (host == null) {
            sensor = new QMSensor();
        } else {

            sensor = new QMSensor(host, port, timeout, level, x, y, width, height, exe);
        }

        setStaticSubscribedValues();
        setDynamicSubscribedValues();
    }

    private void setStaticSubscribedValues() {
        // parameters subscribed from elsewhere

        double min = sensor.getValue(QMState.MINX);
        double max = sensor.getValue(QMState.MAXX);
        double offset = (max - min) * 0.1;//0.175;
        minx = min + offset;
        maxx = max - offset;
        startx = sensor.getValue(QMState.STARTX);
        starty = sensor.getValue(QMState.STARTY);

    }

    private double computeTargetX() throws Exception {
        double startxl = sensor.getValue(QMState.STARTX);
        double txstart = sensor.getValue(QMState.TARGETAREASTART);
        double txstop = sensor.getValue(QMState.TARGETAREASTOP);
        double targetWidth = txstop - txstart;
        double tx;

        if (startxl < txstart && startxl < txstop) {
            tx = txstart + (targetWidth) * (targetpercentage / 100);
        } else if (startxl > txstart && startxl > txstop) {
            tx = txstop - (targetWidth) * (targetpercentage / 100);
        } else {
            throw new Exception(this.getName() + " targetX TBI");
        }
        return tx;
    }

    private void setDynamicSubscribedValues() throws Exception {
        // parameters subscribed from elsewhere
        simulatedtime = sensor.getValue(QMState.TIME);
        atomx = sensor.getValue(QMState.ATOMX);
        laserx = sensor.getValue(QMState.LASERX);
        lasery = sensor.getValue(QMState.LASERY);
        energy = sensor.getValue(QMState.ENERGY);
        fidelity = sensor.getValue(QMState.FIDELITY);
        targetx = computeTargetX();
        fidelityscore = Math.pow(fidelity, 2) * 800000;
        timescore = (1 - (25 * sensor.getValue(QMState.TIME) / 20)) * 200000;
        score = fidelityscore + timescore;
    }

    @Override
    public double compute() throws Exception {
        List<BaseControlFunction> controls = links.getControlList();
        boolean qmResetExternal = controls.get(qmResetIndex).getValue() != 0;
        if (qmreset > 0 || qmResetExternal || !connected) {
            sensor.subscribe(); // discard waiting message
            level = (int) controls.get(qmLevelIndex).getValue();
            sensor.reset(level);
            first = true;
            qmreset = 0;
        }

        if (first) {
            first = false;
            setStaticSubscribedValues();
            setDynamicSubscribedValues();
        } else {
            if (subscribe) {
                connected = sensor.subscribe();
            }
            {
                setDynamicSubscribedValues();
            }
        }
        checkLimits();
        output = simulatedtime;
        return output;
    }

    private void checkLimits() {
        if (laserx > maxx || laserx < minx || /*timescore < 0 ||*/ (timelimit != null && timelimit > 0 && simulatedtime > timelimit)) {
            qmreset = 1;
        }
    }

    @Override
    public String getParametersString() {
        StringBuilder sb = new StringBuilder();

        sb.append("TargetPercentage").append(":");
        sb.append(targetpercentage);

        return sb.toString();
    }

    @Override
    public String getDataString() {
        StringBuilder sb = new StringBuilder();

        sb.append("TargetX").append(":");
        sb.append(MoonsString.formatStringPlaces(targetx, 4)).append("_");
        sb.append("TargetY").append(":");
        sb.append(0);

        return sb.toString();
    }

    /*
    @Override
    public void setParameter(String par) {
        super.setParameter(par);
        if (sensor == null) {
            return;
        }
        String[] arr = par.split(":");
        if (arr[0].equals("Variable")) {
            variable = arr[1];
            //setIvariable();
        }
    }*/
    @Override
    public void close() throws Exception {
        if (sensor != null) {
            sensor.close();
        }
    }
}
