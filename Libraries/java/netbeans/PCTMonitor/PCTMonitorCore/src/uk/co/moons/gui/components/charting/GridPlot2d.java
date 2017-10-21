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

import java.awt.geom.Rectangle2D;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.math.plot.Plot2DPanel;
import org.math.plot.plots.LinePlot;
import uk.co.moons.control.ControlHierarchy;
import uk.co.moons.control.functions.BaseControlFunction;
import uk.co.moonsit.utils.timing.Time;

/**
 *
 * @author Rupert Young Moon's Information Technology
 *
 */
public class GridPlot2d {

    private static final Logger LOG = Logger.getLogger(GridPlot2d.class.getName());
    private Plot2DPanel plot = null;
    private GridPlot2dData gpd = null;
    //private int count = 1;
    private final boolean invokeOneLater = true;

    private final double SMALL = 0.0000001;

    public GridPlot2d() {
        plot = new Plot2DPanel();
        plot.addLegend("SOUTH");
        gpd = new GridPlot2dData();
    }

    public Plot2DPanel getPlotPanel() {
        return plot;
    }

    public void clear() {
        gpd.clear();
        //plot.removeLegend();
    }

    public void addDataSet(String controller, String function) {
        GridPlot2dDataSet gpds = new GridPlot2dDataSet(controller, function);
        gpd.put(gpds.getTitle(), gpds);
    }

    public void addDataSet(String title) {
        GridPlot2dDataSet gpds = new GridPlot2dDataSet(title);
        gpd.put(gpds.getTitle(), gpds);
    }

    public void addData(String title, double x, double y, int limit) {
        GridPlot2dDataSet gpds = gpd.get(title);
        gpds.updateData(x, y, limit);
    }

    @SuppressWarnings("empty-statement")
    public void updatePlot(ControlHierarchy ch, Integer size) throws Exception {
        Double minx = null;
        Double miny = null;
        Double maxx = null;
        Double maxy = null;
        //logger.info("updatePlot");
        Set<String> set = gpd.keySet();
        for (String title : set) {
            GridPlot2dDataSet gpds = gpd.get(title);
            BaseControlFunction function = ch.getControllerFunction(/* gpds.getController(), */gpds.getFunction().trim());
            if (function == null) {
                throw new Exception("Function " + title + " not present in controller " + gpds.getController());
            }
            double y = function.getValue();
            if (Math.abs(y) == Double.POSITIVE_INFINITY) {
                y = 0;
            }
            Double x;
            if (ch.getType() == 2) {
                Time t = new Time(ch.getTime());
                x = t.getTime();
            } else {
                x = function.getX();
            }
            if (x != null) {
                gpds.updateData(x, y, size);
            }

            if (minx == null || x < minx) {
                minx = x;
            }
            if (miny == null || y < miny) {
                miny = y;
            }
            if (maxx == null || x > maxx) {
                maxx = x;
            }
            if (maxy == null || y > maxy) {
                maxy = y;
            }
        }
        boolean zeroHeight = false;
        if (minx == null || miny == null || maxx == null || maxy == null) {
            ;
        } else {
            double width = maxx - minx;
            double height = maxy - miny;
            if (width == 0) {
                width = Math.abs(minx * SMALL);
            }
            if (height == 0) {
                height = Math.abs(miny * SMALL);
                zeroHeight = true;
            }
            final Rectangle2D.Double rect = new Rectangle2D.Double(minx, miny, width, height);
            final boolean zh = zeroHeight;
            if (invokeOneLater) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        redisplay(rect, zh);
                    }
                });
            } else {
                redisplay(rect, zeroHeight);
            }
        }
    }

    private int getDisplayRate(ControlHierarchy ch) throws Exception {
        int rtn = 0;
        BaseControlFunction function = ch.getControllerFunction("DisplayRate");
        if (function != null) {
            rtn = (int) function.getValue();
        }
        return rtn;
    }

    public void empty() {
        Set<String> set = gpd.keySet();
        for (String title : set) {
            GridPlot2dDataSet gpds = gpd.get(title);
            gpds.empty();
        }
    }

    private void redisplay() {
        plot.removeAllPlots();
        Set<String> set = gpd.keySet();
        for (String title : set) {
            GridPlot2dDataSet gpds = gpd.get(title);
            double[] x = gpds.getX();
            if (x.length > 0) {
                //logger.info("***** "+  SwingUtilities.isEventDispatchThread());
                plot.addLinePlot(title, x, gpds.getY());
            }
        }
    }

    private double removeSmall(double d) {
        if (Math.abs(d) < SMALL) {
            d = 0;
        }
        return d;
    }

    private void redisplay(Rectangle2D.Double newRect, boolean zeroHeight) {

        Rectangle2D.Double currentRect = plot.getPlotRectangle();
        Rectangle2D.Double currentRectAdjusted = new Rectangle2D.Double(currentRect.getX(), 0, currentRect.getWidth(), currentRect.getHeight());
        double yAdjusted;
        if (zeroHeight) {
            yAdjusted = removeSmall(currentRect.getMaxY() - newRect.getMaxY() + newRect.getHeight());
        } else {
            yAdjusted = currentRect.getMaxY() - newRect.getMaxY();
        }
        Rectangle2D.Double newRectAdjusted = new Rectangle2D.Double(newRect.getX(), yAdjusted, newRect.getWidth(), newRect.getHeight());
        //LOG.log(Level.INFO, "Plots {0} {1} {2} {3}", new Object[]{currentRect, currentRectAdjusted, newRect, newRectAdjusted});

        if (!currentRectAdjusted.contains(newRectAdjusted)) {
            //LOG.log(Level.INFO, "Remove all plots {0} {1} {2} {3}", new Object[]{currentRect, currentRectAdjusted, newRect, newRectAdjusted});

            if (invokeOneLater) {
                plot.removeAllPlots();
            } else {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        plot.removeAllPlots();
                    }
                });
            }

            Set<String> set = gpd.keySet();
            for (final String title : set) {
                final GridPlot2dDataSet gpds = gpd.get(title);
                final double[] x = gpds.getX();
                if (x.length > 0) {
                    //logger.info("***** "+  SwingUtilities.isEventDispatchThread());

                    plot.addLinePlot(title, x, gpds.getY());
                }
            }
        } else {
            //logger.info("Add data");
            Set<String> set = gpd.keySet();
            for (String title : set) {
                GridPlot2dDataSet gpds = gpd.get(title);
                double x = gpds.getLastX();
                double y = gpds.getLastY();
                LinePlot lp = plot.getLinePlot(title);
                if (lp != null) {
                    lp.addData(x, y);
                }

            }

            if (invokeOneLater) {
                plot.repaint();
                plot.invalidate();
            } else {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        plot.repaint();
                        plot.invalidate();
                    }
                });
            }
        }

    }

    /*
    public void displayPlot() {
        Set<String> set = gpd.keySet();
        for (String title : set) {
            GridPlot2dDataSet gpds = gpd.get(title);
            double[] x = gpds.getX();
            if (x.length > 0) {
                plot.addLinePlot(title, x, gpds.getY());
            }
        }
    }
*/
}
