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
package uk.co.moonsit.messaging;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 *
 * @author ReStart
 */
public class QMState {

    private final String type = "type";
    private final String levelCmd = "GO_TO_LEVEL";
    private final String cursorCmd = "SET_CURSOR_POINT";

    //private Double targetx = null;
    //private Double targety = null;
    private Double targetAreaStart = null;
    private Double targetAreaStop = null;
    private Double startx = null;
    private Double starty = null;
    private Double fidelity = null;
    private Double atomx = null;
    private Double simulatedTime = 0.0;
    private Double energy = null;

    private Double laserx = null;
    private Double lasery = null;
    private Double outputx = null;
    private Double outputy = null;
    private Double minx = null;
    private Double maxx = null;
    private Double miny = null;
    private Double maxy = null;

    public static final int LASERX = 0;
    public static final int LASERY = 1;
    public static final int ENERGY = 2;
    public static final int FIDELITY = 3;
    public static final int TIME = 4;
    public static final int STARTX = 5;
    public static final int STARTY = 6;
    public static final int TARGETAREASTART = 7;
    public static final int TARGETAREASTOP = 8;
    public static final int ATOMX = 9;
    public static final int LASEROUTPUTX = 10;
    public static final int LASEROUTPUTY = 11;
    public static final int LEVEL = 12;
    public static final int MINX = 13;
    public static final int MAXX = 14;

    private boolean reset = true;

    public String setCursor() throws Exception {
        if (outputx == null) {
            throw new Exception("outputx is null, has LaserOutputX been configured in a QMActuator");
        }
        if (outputy == null) {
            throw new Exception("outputy is null, has LaserOutputY been configured in a QMActuator");
        }
        JsonObject json = Json.createObjectBuilder()
                .add(type, cursorCmd)
                .add("x", outputx)
                .add("y", outputy)
                .build();

        laserx = outputx;
        lasery = outputy;
        return json.toString();
    }

    public String setReset(int ilevel) {
        reset = true;
        JsonObject json = Json.createObjectBuilder()
                .add(type, levelCmd)
                .add("level", ilevel)
                .build();

        return json.toString();
    }

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public void setConfig(String msg) {
        JsonObject json;
        try (JsonReader jsonReader = Json.createReader(new StringReader(msg))) {
            json = jsonReader.readObject();
        }
        targetAreaStart = json.getJsonNumber("targetAreaStart").doubleValue();
        targetAreaStop = json.getJsonNumber("targetAreaStop").doubleValue();
        //targetx = (targetAreaStart + targetAreaStop) / 2;
        startx = json.getJsonNumber("x").doubleValue();
        laserx = startx;
        starty = json.getJsonNumber("y").doubleValue();
        lasery = starty;
        //targety = json.getJsonNumber("y").doubleValue();
        fidelity = json.getJsonNumber("fidelity").doubleValue();
        atomx = json.getJsonNumber("expectedAtomPosition").doubleValue();
        energy = json.getJsonNumber("expectedAtomEnergy").doubleValue();
        simulatedTime = json.getJsonNumber("simulatedTime").doubleValue();
        minx = json.getJsonNumber("xMin").doubleValue();
        maxx = json.getJsonNumber("xMax").doubleValue();
        miny = json.getJsonNumber("yMin").doubleValue();
        maxy = json.getJsonNumber("yMax").doubleValue();

    }

    public void getCursor(String msg) {
        JsonObject json;
        try (JsonReader jsonReader = Json.createReader(new StringReader(msg))) {
            json = jsonReader.readObject();
        }
        atomx = json.getJsonNumber("expectedAtomPosition").doubleValue();
        fidelity = json.getJsonNumber("fidelity").doubleValue();
        simulatedTime = json.getJsonNumber("simulatedTime").doubleValue();
        energy = json.getJsonNumber("expectedAtomEnergy").doubleValue();

    }

    public double getValue(int i) {
        double rtn = 0;
        switch (i) {
            case FIDELITY:
                rtn = fidelity;
                break;
            case LASERX:
                rtn = laserx;
                break;
            case LASERY:
                rtn = lasery;
                break;
            case TARGETAREASTART:
                rtn = targetAreaStart;
                break;
            case TARGETAREASTOP:
                rtn = targetAreaStop;
                break;
            case STARTX:
                rtn = startx;
                break;
            case STARTY:
                rtn = starty;
                break;
            case ENERGY:
                rtn = energy;
                break;
            case ATOMX:
                rtn = atomx;
                break;
            case MINX:
                rtn = minx;
                break;
            case MAXX:
                rtn = maxx;
                break;
            case TIME:
                rtn = simulatedTime;
                break;
        }

        return rtn;
    }

    public void setValue(int i, double value) {
        switch (i) {
            case LASEROUTPUTX:
                outputx = applyXlimit(value);
                break;
            case LASEROUTPUTY:
                outputy = applyYlimit(value);
                break;
        }
    }

    private double applyXlimit(double value) {

        if (value > maxx) {
            return maxx;
        }
        if (value < minx) {
            return minx;
        }
        return value;
    }

    private double applyYlimit(double value) {

        if (value > maxy) {
            return maxy;
        }
        if (value < miny) {
            return miny;
        }
        return value;
    }

}
