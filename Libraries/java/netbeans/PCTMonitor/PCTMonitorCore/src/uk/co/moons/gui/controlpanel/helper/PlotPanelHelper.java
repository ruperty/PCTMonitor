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

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import uk.co.moons.control.ControlHierarchy;
import uk.co.moons.gui.components.charting.GridPlot2d;
import uk.co.moonsit.utils.Environment;
import uk.co.moonsit.gui.controlpanel.core.PlotPanelTopComponent;

/**
 *
 * @author Rupert Young Moon's Information Technology
 *
 */
@SuppressWarnings("unchecked")
public class PlotPanelHelper {

    private static final Logger logger = Logger.getLogger(PlotPanelHelper.class.getName());
    private List<GridPlot2d> gp2ds = null;
    private PlotPanelTopComponent plotJPanel = null;
    private boolean clearConfig = true;

    public PlotPanelHelper() {
        gp2ds = new ArrayList<>();
    }

    public boolean isClearConfig() {
        return clearConfig;
    }

    public void setClearConfig(boolean clearConfig) {
        this.clearConfig = clearConfig;
    }

    private List<String> sortKeys(Properties props) {
        List<String> list = new ArrayList<>();
        Set<Object> set = props.keySet();
        for (Object name : set) {
            list.add((String) name);
        }
        Collections.sort(list);
        return list;
    }

    public void loadPlotsResource() {
        Properties props = getProperties();
        if (props != null) {
            List<String> sorted = sortKeys(props);
            for (String key : sorted) {
                int iKey = Integer.parseInt(key);
                if (iKey > gp2ds.size()) {
                    plotJPanel.addGraphButton(key);
                    addGraph();
                }
            }
            for (String key : sorted) {
                int iKey = Integer.parseInt(key);
                String list = props.getProperty(key);
                String[] cons = list.split(",");
                for (String con : cons) {
                    String[] funcs = con.split(":");
                    for (int j = 1; j < funcs.length; j++) {
                        add2dDataSet(funcs[0], funcs[j], iKey);
                    }
                }

            }
        }

    }

    public void saveConfig(HashMap<String, List<String>> configMap) throws IOException {
        File pdir = new File(getPropertiesDir());
        if (!pdir.exists()) {
            pdir.mkdir();
        }

        File dir = new File(getPropertiesSubDir());
        if (!dir.exists()) {
            dir.mkdir();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("# Controller:function,Controller:function \n\n");

        for (String key : configMap.keySet()) {
            sb.append(key).append("=");
            int ctr = 0;
            List<String> list = configMap.get(key);
            for (String function : list) {
                sb.append(function);
                if (ctr++ < list.size()) {
                    sb.append(",");
                }
            }
            sb.append("\n");
        }

        File file = new File(getPropertiesFileName());
        try (FileOutputStream fout = new FileOutputStream(file)) {
            fout.write(sb.toString().getBytes());
        }

        logger.info("Saved properties to " + file);
        logger.info(sb.toString());
    }

    private String getPropertiesDir() {
        String path = Environment.getInstance().getFilePath() + File.separator + "properties";

        return path;
    }

    private String getPropertiesSubDir() {
        String path = Environment.getInstance().getFilePath() + File.separator + "properties" + File.separator
                + (Environment.getInstance().getFileRoot().charAt(0) < 63 ? Environment.getInstance().getFileRoot().substring(0, 7) : Environment.getInstance().getFileRoot())
                + File.separator;

        return path;
    }

    private String getPropertiesFileName() {
        String path = getPropertiesSubDir();
        String fname = path + Environment.getInstance().getFileRoot() + "-plots.properties";

        return fname;
    }

    public Properties getProperties() {

        String fname = getPropertiesFileName();
        Properties props = null;
        try {
            props = new Properties();
            props.load(new FileInputStream(new File(fname)));
        } catch (IOException ex) {
            logger.warning(ex.toString());
            //Logger.getLogger(PlotPanelHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return props;
    }

    public void clear() {
        for (GridPlot2d gpd : gp2ds) {
            gpd.clear();
        }
    }

    public void addPlotPanel(PlotPanelTopComponent pjp, ControlPanelHelper cph) {

        plotJPanel = pjp;
        addGraphPrivate();
        updateControllersListModel(cph);
        loadPlotsResource();

        //TopComponent win = WindowManager.getDefault().findTopComponent("");
        if (!pjp.isShowing()) {
            pjp.open();
            pjp.requestActive();
        }
        /*
         JFrame plotFrame = new JFrame("Plot");
         plotFrame.add(plotJPanel);
         plotFrame.pack();
         plotFrame.setVisible(true);
         */
    }

    public void updateControllersListModel(ControlPanelHelper cph) {
        getControllersList().setModel(cph.getControllersListModel());
    }

    synchronized public void updatePlots(ControlHierarchy ch, Integer size) throws Exception {
        //logger.info("updatePlots");

        for (GridPlot2d gpd : gp2ds) {
            gpd.updatePlot(ch, size);
        }
    }

    public void empty() {
        for (GridPlot2d gpd : gp2ds) {
            gpd.empty();
        }
    }

    synchronized public void add2dDataSet(String controller, String function, int i) {
        //logger.log(Level.INFO, "+++ add dataset {0} {1} {2}", new Object[]{controller, function, i});
        GridPlot2d gp2d = gp2ds.get(i - 1);
        gp2d.addDataSet(controller, function);
    }

    synchronized private void addGraphPrivate() {
        GridPlot2d gp2d = new GridPlot2d();
        getPlotPanel().add(gp2d.getPlotPanel());
        gp2ds.add(gp2d);
    }

    synchronized public void addGraph() {
        GridPlot2d gp2d = new GridPlot2d();
        getPlotPanel().add(gp2d.getPlotPanel());
        gp2ds.add(gp2d);
    }

    synchronized public void removeGraph() {
        int index = gp2ds.size() - 1;
        gp2ds.remove(index);
        getPlotPanel().remove(index);
    }

    private JPanel getPlotPanel() {
        JPanel panel1 = (JPanel) plotJPanel.getComponent(0);
        JPanel panel3 = (JPanel) panel1.getComponent(0);

        /*
         Component [] comps = plotJPanel.getComponents();
         for(Component comp:comps){
         logger.info("+++ comp "+comp.getName());
         }*/
        JPanel plotPanel = (JPanel) panel3.getComponent(1);
        //logger.info("--- " + plotPanel.getName());
        return plotPanel;
    }

    private JList<String> getControllersList() {
        JPanel panel1 = (JPanel) plotJPanel.getComponent(0);
        JPanel panel3 = (JPanel) panel1.getComponent(0);
        JPanel panel2 = (JPanel) panel3.getComponent(0);
        //JScrollPane pane1 = (JScrollPane) getComponentByName(panel2, "jScrollPane1");

        JScrollPane pane1 = (JScrollPane) panel2.getComponent(0);

        JList<String> list = (JList<String>) pane1.getViewport().getComponent(0);

        //Component[] comps = pane1.getViewport().getComponents();
        /*
         String[] strs = new String[comps.length];
         for (int i = 0; i < comps.length; i++) {
         strs[i] = comps[i].getName();
         }
         JList<String> list = new JList<String>(strs);
         */
        //JList<String> list = new JList<String>(strs);
//logger.info("--- " + list.getName());
        return list;
    }

    private Component getComponentByName(JPanel panel, String name) {
        int size = panel.getComponentCount();
        for (int i = 0; i < size; i++) {
            Component comp = panel.getComponent(i);
            if (name.equals(comp.getName())) {
                return comp;
            }
        }
        return null;
    }
}
