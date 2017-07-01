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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import uk.co.moons.gui.components.testapps.TestObject;

public class DisplayJPanel extends javax.swing.JPanel {

    private TestObject to = null;

    /** Creates new form DisplayJPanel */
    public DisplayJPanel() {
        initComponents();
    }

    public TestObject getTo() {
        return to;
    }

    public void setTo(final TestObject to) {
        this.to = to;

        // add listener to listen and react to changes in to value's state
        to.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (TestObject.VALUE.equals(evt.getPropertyName())) {
                    setLabel2Text(String.valueOf(to.getValue()));
                }
            }
        });
    }

    public void setLabel2Text(String text) {
        jLabel2.setText(text);
    }
    /*
    @Override
    public void paintComponent(Graphics g) {
    System.out.println("paintComponent");
    super.paintComponent(g);
    if (to != null) {
    int i = to.getValue();
    System.out.println("paintComponent " + i);
    //jLabel1.setText(String.valueOf(i));
    jLabel2.setText(String.valueOf(i + 1));
    }
    }*/

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setName("Form"); // NOI18N
        setLayout(new java.awt.BorderLayout());

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        //org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(uk.co.moons.gui.controlpanel.ControlPanelApp.class).getContext().getResourceMap(DisplayJPanel.class);
        //jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel2.setName("jLabel2"); // NOI18N
        jPanel1.add(jLabel2, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        //jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        //jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel1.setName("jLabel1"); // NOI18N
        add(jLabel1, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
