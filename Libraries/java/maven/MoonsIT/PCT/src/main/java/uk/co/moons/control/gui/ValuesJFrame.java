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
package uk.co.moons.control.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import uk.co.moonsit.utils.MoonsString;

/**
 *
 * @author rupert
 */
public class ValuesJFrame extends javax.swing.JFrame {

    private static final Logger LOG = Logger.getLogger(ValuesJFrame.class.getName());

    private DefaultListModel<DisplayValue> listModel;
    private int x = 0;
    private int y = 0;
    private int width = 100;
    private int height = 50;
    private int fontSize = 20;
    //private int places = 3;
    private String alignment = "horizontal";
    //private final SwingPropertyChangeSupport propChangeSupport = new SwingPropertyChangeSupport(this);

    /**
     * Creates new form ValuesJFrame
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param fontSize
     * @param size
     * @param align
     */
    public ValuesJFrame(int x, int y, int width, int height, int fontSize, int size,  String align) {
        this.x=x;
        this.y=y;
        if (align != null) {
            this.alignment = align;
        }
        if (alignment.equalsIgnoreCase("vertical")) {
            this.width = width;
            this.height = height * size;
        } else {
            this.width = width * size;
            this.height = height;
        }
        this.fontSize = fontSize;
        //this.places = places;
        initComponents();
    }

    public Dimension getDimension() {
        return new Dimension(width, height);
    }

    public Point getPoint() {
        return new Point(x, y);
    }

   

    public void initList(List<DisplayValue> list) {
        listModel = new DefaultListModel<>();
        if (alignment.equalsIgnoreCase("vertical")) {
            jPanel1.setLayout(new GridLayout(list.size(), 1));
        } else {
            jPanel1.setLayout(new GridLayout(1, list.size()));
        }
        for (DisplayValue dv : list) {
            listModel.addElement(dv);
            jPanel1.add(valuePanel(dv));
        }

        listModel.addListDataListener(new MyListDataListener());
    }

    private JPanel valuePanel(DisplayValue dv) {
        //JPanel panel = new JPanel(new GridLayout(2, 1));
        JPanel panel = new JPanel(new BorderLayout());
        panel.setName("Name");
        //panel.setSize(300, 300);
        Border panelBorder = BorderFactory.createLineBorder(Color.DARK_GRAY, 2);
        Border labelBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);

        panel.setBorder(panelBorder);

        JLabel name = new JLabel(dv.getName());
        Font nameFont = new Font("Courier", Font.PLAIN, fontSize / 2);
        name.setFont(nameFont);
        name.setBorder(labelBorder);
        name.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(name, BorderLayout.PAGE_START);

        JLabel value = new JLabel(String.valueOf(dv.getValue()));
        value.setBorder(labelBorder);
        Font valueFont = new Font("Courier", Font.PLAIN, fontSize);
        value.setFont(valueFont);
        value.setAlignmentX(Component.CENTER_ALIGNMENT);
        value.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(value, BorderLayout.CENTER);

        return panel;
    }

    class MyListDataListener implements ListDataListener {

        @Override
        public void contentsChanged(ListDataEvent e) {
            int index = e.getIndex0();
            //LOG.log(Level.INFO, "contentsChanged: {0}, {1}", new Object[]{e.getIndex0(), e.getIndex1()});
            JPanel panel = (JPanel) jPanel1.getComponent(index);
            JLabel label = (JLabel) panel.getComponent(1);
            DisplayValue dv = (DisplayValue) listModel.get(index);
            //label.setText(String.valueOf(dv.getValue()));
            //label.setText(MoonsString.formatPlaces(dv.getValue(),4));
            label.setText(MoonsString.formatStringPlaces(dv.getValue(), dv.getPlaces()));
        }

        @Override
        public void intervalAdded(ListDataEvent e) {
            LOG.log(Level.INFO, "intervalAdded: {0}, {1}", new Object[]{e.getIndex0(), e.getIndex1()});
        }

        @Override
        public void intervalRemoved(ListDataEvent e) {
            LOG.log(Level.INFO, "intervalRemoved: {0}, {1}", new Object[]{e.getIndex0(), e.getIndex1()});
        }
    }

    public void changeValue(int i, double d) {
        DisplayValue dv = (DisplayValue) listModel.get(i);
        dv.setValue(d);
        listModel.set(i, dv);
    }

    /*
    public void addValue(String name, double d) {
        DisplayValue dv = new DisplayValue(name, d);
        listModel.addElement(dv);
    }*/
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        setLocation(getPoint());
        setPreferredSize(getDimension());

        jPanel1.setLayout(null);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ValuesJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        ValuesJFrame vf = new ValuesJFrame(100, 100, 400, 150, 50, 2,  "vertical");
        List<DisplayValue> list = new ArrayList<>();
        list.add(new DisplayValue("fred", 1.0, 4));
        list.add(new DisplayValue("joe", 20.0, 3));
        vf.initList(list);

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                vf.setVisible(true);

            }
        });

        for (int i = 0; i < 5; i++) {
            vf.changeValue(0, i + 100 + 0.123456789);
            vf.changeValue(1, i + 50);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ValuesJFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //vf.addValue("chuck", 50.0);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ValuesJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        vf.setVisible(false);
        vf.dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
