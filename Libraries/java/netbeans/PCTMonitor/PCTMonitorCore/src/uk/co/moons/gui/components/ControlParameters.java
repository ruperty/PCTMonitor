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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.FocusListener;
import java.beans.EventHandler;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.neural.BaseNeuralFunction;
import uk.co.moons.gui.layout.SpringUtilities;
import uk.co.moonsit.utils.MoonsString;

/**
 *
 * @author ReStart
 */
public class ControlParameters extends javax.swing.JPanel {

    /**
     * Creates new form ControlParameters
     */
    List<JPanel> parsPanels;
    private Controller pcontroller = null;
    private uk.co.moons.control.Controller controller = null;

    public ControlParameters() {
        initComponents();
    }

    public ControlParameters(Controller pcontroller, uk.co.moons.control.Controller controller) {
        this.pcontroller = pcontroller;
        this.controller = controller;
        initComponents();
        parsPanels = new ArrayList<JPanel>();
        initMore();
    }

    private void initMore() {
        addPanelPars();
    }

    private boolean dataTypesNotEmpty(List<Parameters> inputPars) {

        for (Parameters par : inputPars) {
            if (!par.getDataType().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void addPanelPars() {
        jPanelInputParameters.setLayout(new SpringLayout());
        int i = 0;

        Functions functions = pcontroller.getFunctions();

        if (functions.getInputFunctions() != null) {
            pct.moons.co.uk.schema.layers.ControlFunction func = functions.getInputFunctions().getInput();
            List<Parameters> inputPars = functions.getInputFunctions().getInput().getNeuralFunction().getParameters();
            if (inputPars.size() > 0 && dataTypesNotEmpty(inputPars)) {
                JPanel funcPanel = getFunctionPanel(func);
                for (Parameters par : inputPars) {
                    if (par.getName().equals("MotorIndex") || par.getName().equals("SensorPort")) {
                        //      i--;
                    } else if (!par.getDataType().isEmpty()) {
                        funcPanel.add(getParameterPanel(par, controller.getInputFunction1().getMainFunction().getNeural()));
                    }
                }
                i++;
                jPanelInputParameters.add(funcPanel);
            }
            if (functions.getInputFunctions().getTransfers() != null) {
                int j = 0;
                for (pct.moons.co.uk.schema.layers.ControlFunction transfer : functions.getInputFunctions().getTransfers().getTransfer()) {
                    List<Parameters> transferPars = transfer.getNeuralFunction().getParameters();
                    if (transferPars.size() > 0 && dataTypesNotEmpty(transferPars)) {
                        JPanel transPanel = getFunctionPanel(transfer);
                        for (Parameters par : transferPars) {
                            if (!par.getDataType().isEmpty()) {
                                transPanel.add(getParameterPanel(par, controller.getInputFunction1().getTransferFunctions().get(j).getNeural()));
                            }
                        }

                        i++;
                        jPanelInputParameters.add(transPanel);
                    }
                    j++;
                }
            }
        }
        if (functions.getReferenceFunctions() != null) {
            pct.moons.co.uk.schema.layers.ControlFunction func = functions.getReferenceFunctions().getReference();
            List<Parameters> refPars = func.getNeuralFunction().getParameters();
            if (refPars.size() > 0 && dataTypesNotEmpty(refPars)) {
                JPanel funcPanel = getFunctionPanel(func);
                for (Parameters par : refPars) {
                    if (!par.getDataType().isEmpty()) {
                        funcPanel.add(getParameterPanel(par, controller.getReferenceFunction1().getMainFunction().getNeural()));
                    }
                }
                //i = i + refPars.size();
                i++;
                jPanelInputParameters.add(funcPanel);
            }
            if (functions.getReferenceFunctions().getTransfers() != null) {
                int j = 0;
                for (pct.moons.co.uk.schema.layers.ControlFunction transfer : functions.getReferenceFunctions().getTransfers().getTransfer()) {
                    List<Parameters> transferPars = transfer.getNeuralFunction().getParameters();
                    if (transferPars.size() > 0 && dataTypesNotEmpty(transferPars)) {
                        JPanel transPanel = getFunctionPanel(transfer);
                        for (Parameters par : transferPars) {
                            if (!par.getDataType().isEmpty()) {
                                transPanel.add(getParameterPanel(par, controller.getReferenceFunction1().getTransferFunctions().get(j).getNeural()));
                            }
                        }
                        i++;
                        jPanelInputParameters.add(transPanel);
                    }
                    j++;
                }
            }
        }
        if (functions.getErrorFunctions() != null) {
            pct.moons.co.uk.schema.layers.ControlFunction func = functions.getErrorFunctions().getError();
            List<Parameters> errPars = functions.getErrorFunctions().getError().getNeuralFunction().getParameters();
            if (errPars.size() > 0 && dataTypesNotEmpty(errPars)) {
                JPanel funcPanel = getFunctionPanel(func);
                for (Parameters par : errPars) {
                    if (!par.getDataType().isEmpty()) {
                        funcPanel.add(getParameterPanel(par, controller.getErrorFunction1().getMainFunction().getNeural()));
                    }
                }
                //i = i + errPars.size();
                i++;
                jPanelInputParameters.add(funcPanel);
            }
            if (functions.getErrorFunctions().getTransfers() != null) {
                int j = 0;
                for (pct.moons.co.uk.schema.layers.ControlFunction transfer : functions.getErrorFunctions().getTransfers().getTransfer()) {
                    List<Parameters> transferPars = transfer.getNeuralFunction().getParameters();
                    if (transferPars.size() > 0 && dataTypesNotEmpty(transferPars)) {
                        JPanel transPanel = getFunctionPanel(transfer);
                        for (Parameters par : transferPars) {
                            if (!par.getDataType().isEmpty()) {
                                transPanel.add(getParameterPanel(par, controller.getErrorFunction1().getTransferFunctions().get(j).getNeural()));
                            }
                        }
                        i++;
                        jPanelInputParameters.add(transPanel);
                    }
                    j++;
                }
            }

        }
        if (functions.getOutputFunctions() != null) {
            pct.moons.co.uk.schema.layers.ControlFunction func = functions.getOutputFunctions().getOutput();
            List<Parameters> outputPars = functions.getOutputFunctions().getOutput().getNeuralFunction().getParameters();
            if (outputPars.size() > 0 && dataTypesNotEmpty(outputPars)) {
                JPanel funcPanel = getFunctionPanel(func);
                for (Parameters par : outputPars) {
                    if (par.getName().equals("MotorIndex")) {
                        //i--;
                    } else if (!par.getDataType().isEmpty()) {
                        funcPanel.add(getParameterPanel(par, controller.getOutputFunction1().getMainFunction().getNeural()));
                    }
                }
                //i = i + outputPars.size();
                i++;
                jPanelInputParameters.add(funcPanel);
            }
            if (functions.getOutputFunctions().getTransfers() != null) {
                int j = 0;
                for (pct.moons.co.uk.schema.layers.ControlFunction transfer : functions.getOutputFunctions().getTransfers().getTransfer()) {
                    List<Parameters> transferPars = transfer.getNeuralFunction().getParameters();
                    if (transferPars.size() > 0 && dataTypesNotEmpty(transferPars)) {
                        JPanel transPanel = getFunctionPanel(transfer);
                        for (Parameters par : transferPars) {
                            if (!par.getDataType().isEmpty()) {
                                transPanel.add(getParameterPanel(par, controller.getOutputFunction1().getTransferFunctions().get(j).getNeural()));
                            }
                        }
                        i++;
                        jPanelInputParameters.add(transPanel);
                    }
                    j++;
                }
            }
        }

        //Lay out the panel.
        SpringUtilities.makeCompactGrid(jPanelInputParameters,
                i, 1, //rows, cols
                6, 6, //initX, initY
                6, 6);       //xPad, yPad

    }

    private JPanel getFunctionPanel(pct.moons.co.uk.schema.layers.ControlFunction function) {
        JPanel newPanel = new JPanel();
        Border border = BorderFactory.createLineBorder(Color.DARK_GRAY, 2);

        newPanel.setBorder(border);
        //newPanel.setLayout(new java.awt.GridLayout());
        newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(MoonsString.shortenTextFilter(function.getName()));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        newPanel.add(label);

        return newPanel;
    }

    private JPanel getParameterPanel(Parameters par, BaseNeuralFunction neural) {
        JPanel newPanel = new JPanel();
        Font font = new Font("Courier", Font.PLAIN, 10);

        newPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        //newPanel.setLayout(new java.awt.GridLayout());
        newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(par.getName());
        label.setFont(font);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        newPanel.add(label);

        String datatype = par.getDataType();
        if (datatype == null || datatype.length() == 0) {
            JLabel label1 = new JLabel(par.getValue());
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            label.setFont(font);
            newPanel.add(label1);
        } else {
            ControlTextField field = new ControlTextField(par.getName(), 5);
            field.setText(par.getValue());
            field.addFocusListener(EventHandler.create(FocusListener.class, neural, "parameter", "source.string", "focusLost"));
            newPanel.add(field);
        }
        return newPanel;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelInputParameters = new javax.swing.JPanel();

        setName("Form"); // NOI18N

        jPanelInputParameters.setName("jPanelInputParameters"); // NOI18N

        javax.swing.GroupLayout jPanelInputParametersLayout = new javax.swing.GroupLayout(jPanelInputParameters);
        jPanelInputParameters.setLayout(jPanelInputParametersLayout);
        jPanelInputParametersLayout.setHorizontalGroup(
            jPanelInputParametersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
        );
        jPanelInputParametersLayout.setVerticalGroup(
            jPanelInputParametersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelInputParameters, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelInputParameters, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanelInputParameters;
    // End of variables declaration//GEN-END:variables
}
