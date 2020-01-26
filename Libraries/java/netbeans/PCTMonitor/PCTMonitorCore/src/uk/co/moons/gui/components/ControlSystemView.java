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
package uk.co.moons.gui.components;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moons.control.ControlFunctionCollection;
import uk.co.moons.control.Controller;
import uk.co.moons.control.functions.BaseControlFunction;
import uk.co.moons.gui.controlpanel.display.ControlDisplayTypes;

/**
 *
 * @author ReStart
 */
public class ControlSystemView extends javax.swing.JPanel {

    private Controller controller;
    private BooleanFlag display;

    /**
     * Creates new form ControlSystemView
     */
    public ControlSystemView() {
        initComponents();
        display=new BooleanFlag();
    }

    public BooleanFlag getDisplay() {
        return display;
    }

    //public void setDisplay(BooleanFlag display) {
      //  this.display = display;
    //}

    public void setControlSystem(Controller con, HashMap<String, ControlDisplayTypes> cdts, ControlHierarchyEventMonitor monitor) {

        try {
            //HashMap<String, ControlDisplayTypes> cdts=null;
            this.controller = con;
            if (controller.getReferenceFunction() != null) {
                int index = controller.getReferenceFunction().getMainFunction() == null ? -1 : controller.getReferenceFunction().getMainFunction().getNeural().getPosindex();
                String name = controller.getReferenceFunction().getMainFunction() == null ? null : controller.getReferenceFunction().getMainFunction().getNeural().getName();
                referenceFunctionView.setControlFunction(monitor, index, name, false, display);

                setControlDisplayTypes(referenceFunctionView, controller.getReferenceFunction().getMainFunction(), cdts);
                List<BaseControlFunction> transfers = controller.getReferenceFunction().getTransferFunctions();
                if (transfers != null) {
                    for (BaseControlFunction transfer : transfers) {
                        ControlFunctionView transferFunctionView = new ControlFunctionView();
                        jPanelRefTrans.add(transferFunctionView);
                        String tname = transfer.getNeural().getName();
                        transferFunctionView.setControlFunction(monitor, transfer.getNeural().getPosindex(), tname, true, display);

                        setControlDisplayTypes(transferFunctionView, transfer, cdts);
                    }
                }
            }
            if (controller.getErrorFunction() != null) {
                int index = controller.getErrorFunction().getMainFunction() == null ? -1 : controller.getErrorFunction().getMainFunction().getNeural().getPosindex();
                String name = controller.getErrorFunction().getMainFunction() == null ? null : controller.getErrorFunction().getMainFunction().getNeural().getName();
                errorFunctionView.setControlFunction(monitor, index, name, false, display);

                setControlDisplayTypes(errorFunctionView, controller.getErrorFunction().getMainFunction(), cdts);

            }
            {
                ControlFunctionCollection inColl = controller.getInputFunction();
                if (inColl != null) {
                    BaseControlFunction main = inColl.getMainFunction();
                    String name = main == null ? null : main.getNeural().getName();
                    inputFunctionView.setControlFunction(monitor, main == null ? -1 : main.getNeural().getPosindex(), name, false, display);
       
                    setControlDisplayTypes(inputFunctionView, main, cdts);
                    List<BaseControlFunction> transfers = inColl.getTransferFunctions();
                    if (transfers != null) {
                        for (BaseControlFunction transfer : transfers) {
                            ControlFunctionView transferFunctionView = new ControlFunctionView();
                            jPanelInpTrans.add(transferFunctionView);
                            String tname = transfer.getNeural().getName();
                            transferFunctionView.setControlFunction(monitor, transfer.getNeural().getPosindex(), tname, true, display);

                            setControlDisplayTypes(transferFunctionView, transfer, cdts);
                        }
                    }
                }
            }
            if (controller.getOutputFunction() != null) {
                int index = controller.getOutputFunction().getMainFunction() == null ? -1 : controller.getOutputFunction().getMainFunction().getNeural().getPosindex();
                String name = controller.getOutputFunction().getMainFunction() == null ? null : controller.getOutputFunction().getMainFunction().getNeural().getName();
                outputFunctionView.setControlFunction(monitor, index, name, false, display);

                setControlDisplayTypes(outputFunctionView, controller.getOutputFunction().getMainFunction(), cdts);
                List<BaseControlFunction> transfers = controller.getOutputFunction().getTransferFunctions();
                if (transfers != null) {
                    for (BaseControlFunction transfer : transfers) {
                        ControlFunctionView transferFunctionView = new ControlFunctionView();
                        jPanelOutTrans.add(transferFunctionView);
                        String tname = transfer.getNeural().getName();
                        transferFunctionView.setControlFunction(monitor, transfer.getNeural().getPosindex(), tname, true, display);
                        setControlDisplayTypes(transferFunctionView, transfer, cdts);
                    }
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ControlSystemView.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(ControlSystemView.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        } catch (InstantiationException ex) {
            Logger.getLogger(ControlSystemView.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ControlSystemView.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ControlSystemView.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(ControlSystemView.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
    }

    private void setControlDisplayTypes(ControlFunctionView functionView, BaseControlFunction function, HashMap<String, ControlDisplayTypes> cdts) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        if (function != null) {
            String name = function.getName();
            ControlDisplayTypes cdt = cdts.get(name);
            if (cdt == null) {
                cdt = new ControlDisplayTypes(name);
                cdts.put(name, cdt);
            }
            functionView.setControlDisplayTypes(cdt);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jPanelRefTrans = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        referenceFunctionView = new uk.co.moons.gui.components.ControlFunctionView();
        errorFunctionView = new uk.co.moons.gui.components.ControlFunctionView();
        jPanelOutTrans = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        inputFunctionView = new uk.co.moons.gui.components.ControlFunctionView();
        jPanelInpTrans = new javax.swing.JPanel();
        outputFunctionView = new uk.co.moons.gui.components.ControlFunctionView();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setMinimumSize(new java.awt.Dimension(10, 20));
        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(10, 20));
        setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setMinimumSize(new java.awt.Dimension(20, 20));
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(20, 20));
        jPanel1.setLayout(new java.awt.GridLayout(1, 3));

        jPanelRefTrans.setMaximumSize(new java.awt.Dimension(32767, 65534));
        jPanelRefTrans.setMinimumSize(new java.awt.Dimension(20, 10));
        jPanelRefTrans.setName("jPanelRefTrans"); // NOI18N
        jPanelRefTrans.setPreferredSize(new java.awt.Dimension(20, 10));
        jPanelRefTrans.setLayout(new java.awt.GridLayout(4, 2));
        jPanel1.add(jPanelRefTrans);

        jPanel7.setMinimumSize(new java.awt.Dimension(20, 20));
        jPanel7.setName("jPanel7"); // NOI18N
        jPanel7.setPreferredSize(new java.awt.Dimension(20, 20));
        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.Y_AXIS));

        referenceFunctionView.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        referenceFunctionView.setName("referenceFunctionView"); // NOI18N
        jPanel7.add(referenceFunctionView);

        errorFunctionView.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        errorFunctionView.setName("errorFunctionView"); // NOI18N
        jPanel7.add(errorFunctionView);

        jPanel1.add(jPanel7);

        jPanelOutTrans.setMaximumSize(new java.awt.Dimension(32767, 65534));
        jPanelOutTrans.setMinimumSize(new java.awt.Dimension(20, 10));
        jPanelOutTrans.setName("jPanelOutTrans"); // NOI18N
        jPanelOutTrans.setPreferredSize(new java.awt.Dimension(20, 10));
        jPanelOutTrans.setLayout(new java.awt.GridLayout(4, 2));
        jPanel1.add(jPanelOutTrans);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.75;
        add(jPanel1, gridBagConstraints);

        jPanel2.setMinimumSize(new java.awt.Dimension(20, 20));
        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setPreferredSize(new java.awt.Dimension(60, 54));
        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        inputFunctionView.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        inputFunctionView.setName("inputFunctionView"); // NOI18N
        jPanel2.add(inputFunctionView);

        jPanelInpTrans.setMinimumSize(new java.awt.Dimension(20, 10));
        jPanelInpTrans.setName("jPanelInpTrans"); // NOI18N
        jPanelInpTrans.setPreferredSize(new java.awt.Dimension(20, 10));
        jPanelInpTrans.setLayout(new java.awt.GridLayout(4, 2));
        jPanel2.add(jPanelInpTrans);

        outputFunctionView.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        outputFunctionView.setName("outputFunctionView"); // NOI18N
        jPanel2.add(outputFunctionView);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.25;
        add(jPanel2, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private uk.co.moons.gui.components.ControlFunctionView errorFunctionView;
    private uk.co.moons.gui.components.ControlFunctionView inputFunctionView;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanelInpTrans;
    private javax.swing.JPanel jPanelOutTrans;
    private javax.swing.JPanel jPanelRefTrans;
    private uk.co.moons.gui.components.ControlFunctionView outputFunctionView;
    private uk.co.moons.gui.components.ControlFunctionView referenceFunctionView;
    // End of variables declaration//GEN-END:variables
}
