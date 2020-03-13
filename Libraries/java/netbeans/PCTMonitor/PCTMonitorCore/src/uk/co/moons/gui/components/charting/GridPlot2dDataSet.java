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
package uk.co.moons.gui.components.charting;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Rupert Young Moon's Information Technology
 *
 */
public class GridPlot2dDataSet {

    private static final Logger LOG = Logger.getLogger(GridPlot2dDataSet.class.getName());

    private List<Double> xList = null;
    private List<Double> yList = null;
    private String title = null;
    private String controller = null, function = null;
    private final boolean debug = false;

    public GridPlot2dDataSet(String controller, String function) {
        this.controller = controller;
        this.function = function;
        this.title = function;//controller.substring(0, 4) + function.substring(0, 4);
        xList = new ArrayList<>();
        yList = new ArrayList<>();
    }

    public GridPlot2dDataSet(String title) {
        this.title = title;
        xList = new ArrayList<>();
        yList = new ArrayList<>();
    }

    public String getController() {
        return controller;
    }

    public String getFunction() {
        return function;
    }

    public String getTitle() {
        return title;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void updateData(double x, double y, Integer size) {
        xList.add(x);
        yList.add(y);
        if (size != null) {
            while (xList.size() > size) {
                xList.remove(0);
                yList.remove(0);
            }
        }
    }

    public void empty() {
        xList.clear();
        yList.clear();
    }

    public double getXSize() {
        return xList.size();
    }

    public double getLastX() {
        Double rtn = null;
        try {
            rtn = xList.get(xList.size() - 1);
        } catch (Exception e) {
            if (debug) {
                LOG.warning(e.toString());
            }
        }
        return rtn;
    }

    public double getLastY() {
        return yList.get(yList.size() - 1);
    }

    public double[] getX() {
        double[] d = new double[xList.size()];
        int ctr = 0;
        try {
            for (double val : xList) {
                d[ctr++] = val;
            }
        } catch (Exception e) {
            if (debug) {
                LOG.warning(e.toString());
            }
        }
        return d;
    }

    public double[] getY() {
        double[] d = new double[yList.size()];
        try {
            try {
                int ctr = 0;
                for (double val : yList) {
                    d[ctr++] = val;
                }
            } catch (Exception e) {
               if(debug) LOG.info(e.toString());
            }
        } catch (Exception e) {
           if(debug) LOG.info(e.toString());
        }
        return d;
    }
}
