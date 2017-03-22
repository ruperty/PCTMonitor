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


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;
import javax.swing.event.SwingPropertyChangeSupport;

public class ListenToTest {
   public static final String[] ITEMS = {"Sunday", "Monday", "Tuesday", "Wednesday", 
      "Thursday", "Friday", "Saturday"};
   private JPanel mainPanel = new JPanel();
   private ObserverPanel observerPanel = new ObserverPanel();
   private ListenToModel model = new ListenToModel();

   public ListenToTest() {
      observerPanel.setModel(model);

      for (String item : ITEMS) {
         model.addItem(item);
      }

      JPanel btnPanel = new JPanel();
      btnPanel.add(new JButton(new AddAction("Add")));
      btnPanel.add(new JButton(new RemoveAction("Remove")));

      mainPanel.setLayout(new BorderLayout());
      mainPanel.add(new JScrollPane(observerPanel.getMainComponent()));
      mainPanel.add(btnPanel, BorderLayout.PAGE_END);
   }

   public JComponent getMainComponent() {
      return mainPanel;
   }

   private class AddAction extends AbstractAction {
      public AddAction(String title) {
         super(title);
      }

      @Override
      public void actionPerformed(ActionEvent arg0) {
         String text = JOptionPane.showInputDialog(mainPanel, "Enter a String");
         if (text != null) {
            model.addItem(text);
         }
      }
   }

   private class RemoveAction extends AbstractAction {
      public RemoveAction(String title) {
         super(title);
      }

      @Override
      public void actionPerformed(ActionEvent arg0) {
         int index = observerPanel.getSelectedIndex();
         if (index >= 0) {
            model.removeItem(index);
         }
      }
   }

   private static void createAndShowGui() {
      ListenToTest mainPanel = new ListenToTest();

      JFrame frame = new JFrame("ListenToModelTest");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(mainPanel.getMainComponent());
      frame.pack();
      frame.setLocationByPlatform(true);
      frame.setVisible(true);
   }

   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            createAndShowGui();
         }
      });
   }
}

class ObserverPanel {
   public static final Font LABEL_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 18);
   protected static final Color SELECTED_COLOR = new Color(150, 150, 255);
   private JPanel mainPanel = new JPanel();
   private ListenToModel model;
   private GridLayout gridLayout = new GridLayout(0, 1);
   private int selectedIndex = -1;

   public ObserverPanel() {
      mainPanel.setLayout(gridLayout);
      mainPanel.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            Point p = e.getPoint();
            Component[] components = mainPanel.getComponents();
            for (int i = 0; i < components.length; i++) {
               if (mainPanel.getComponentAt(p).equals(components[i])) {
                  selectedIndex = i;
                  components[i].setBackground(SELECTED_COLOR);
               } else {
                  components[i].setBackground(null);
               }
            }
         }
      });
   }

   public int getSelectedIndex() {
      return selectedIndex;
   }

   public void setModel(ListenToModel model) {
      this.model = model;
      model.addPropertyChangeListener(new ObserverPanelListener());
   }

   public JComponent getMainComponent() {
      return mainPanel;
   }

   private class ObserverPanelListener implements PropertyChangeListener {
      public void propertyChange(PropertyChangeEvent evt) {
         if (evt.getPropertyName().equals(ListenToModel.ADD)) {
            JLabel label = createLabel(evt);
            for (Component comp : mainPanel.getComponents()) {
               comp.setBackground(null);
            }
            int index = ((IndexedPropertyChangeEvent)evt).getIndex();
            mainPanel.add(label, index);
            label.setBackground(SELECTED_COLOR);
            selectedIndex = index;
         } else if (evt.getPropertyName().equals(ListenToModel.REMOVE)) {
            int index = ((IndexedPropertyChangeEvent)evt).getIndex();
            mainPanel.remove(index);
            for (Component comp : mainPanel.getComponents()) {
               comp.setBackground(null);
            }
            selectedIndex = -1;
         } else if (evt.getPropertyName().equals(ListenToModel.REMOVE_ALL)) {
            mainPanel.removeAll();
            selectedIndex = -1;
         }
         mainPanel.revalidate();
         mainPanel.repaint();
      }

      private JLabel createLabel(PropertyChangeEvent evt) {
         String newValue = evt.getNewValue().toString();
         JLabel label = new JLabel(newValue);
         label.setFont(LABEL_FONT);
         int eb = 20;
         label.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.blue), 
               BorderFactory.createEmptyBorder(eb , eb, eb, eb)));
         label.setFocusable(true);
         label.setOpaque(true);
         return label;
      }
   }
}

class ListenToModel implements Iterable<String> {
   public static final String ADD = "add";
   public static final String REMOVE = "remove";
   public static final String REMOVE_ALL = "remove all";
   private SwingPropertyChangeSupport spcSupport = new SwingPropertyChangeSupport(
         this);
   private List<String> modelNucleus = new ArrayList<String>();

   public void addItem(String item) {
      modelNucleus.add(item);
      spcSupport.fireIndexedPropertyChange(ADD, modelNucleus.size() - 1, null,
            item);
   }

   public void addItem(int index, String item) {
      if (index < 0 || index > modelNucleus.size()) {
         // TODO: throw an exception
      } else {
         modelNucleus.add(index, item);
         spcSupport.fireIndexedPropertyChange(REMOVE, index, null, item);
      }

   }

   public void removeItem(int index) {
      if (index < 0 || index >= modelNucleus.size()) {
         // TODO: throw an exception
      } else {
         String oldValue = modelNucleus.remove(index);
         spcSupport.fireIndexedPropertyChange(REMOVE, index, oldValue, null);
      }
   }

   public void removeAll() {
      modelNucleus.clear();
      spcSupport.firePropertyChange(REMOVE_ALL, null, null);
   }

   public void addPropertyChangeListener(PropertyChangeListener listener) {
      spcSupport.addPropertyChangeListener(listener);
   }

   public void removePropertyChangeListener(PropertyChangeListener listener) {
      spcSupport.removePropertyChangeListener(listener);
   }

   @Override
   public Iterator<String> iterator() {
      return modelNucleus.iterator();
   }
}