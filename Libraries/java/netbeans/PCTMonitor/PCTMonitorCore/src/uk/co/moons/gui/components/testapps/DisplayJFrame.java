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
package uk.co.moons.gui.components.testapps;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;


/**
 *
 * @author ReStart
 */
public class DisplayJFrame extends javax.swing.JFrame {

    /** Creates new form DisplayJFrame */
    public DisplayJFrame() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        displayJPanel1 = new uk.co.moons.gui.components.DisplayJPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("Form"); // NOI18N

        displayJPanel1.setName("displayJPanel1"); // NOI18N
        getContentPane().add(displayJPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void draw() {
        displayJPanel1.revalidate();
        displayJPanel1.repaint();
    }

    public void setTO(TestObject to) {
        displayJPanel1.setTo(to);
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DisplayJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DisplayJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DisplayJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DisplayJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                final DisplayJFrame df = new DisplayJFrame();
                df.setVisible(true);
                final TestObject to = new TestObject();
                df.setTO(to);

                int timerDelay = 100;
                final int maxCount = 20;
                new Timer(timerDelay, new ActionListener() {
                    private int count = 0;
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        if (count < maxCount) {
                            //df.repaint();
                            to.setValue(count);
                            count++;
                        } else {
                            ((Timer) evt.getSource()).stop();
                        }
                    }
                }).start();
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private uk.co.moons.gui.components.DisplayJPanel displayJPanel1;
    // End of variables declaration//GEN-END:variables
}
