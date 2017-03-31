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
package uk.co.moons.control.neural;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.functions.BaseControlFunction;
import uk.co.moons.control.gui.DisplayValue;
import uk.co.moons.control.gui.ValuesJFrame;

/**
 * <b>Display function</b>
 *
 * <p>
 * Display a set of values in a window.
 *
 * <p>
 * The configuration parameters to the function are as follows:
 * <p>
 * <b>Width</b> - width of each value panel (type Integer). <br>
 * <b>Height</b> - height of each value panel (type Integer). <br>
 * <b>Font</b> - size of value font (type Integer). <br>
 * <b>Places</b> - decimal places (type Integer). <br>
 * <b>Alignment</b> - alignment of panels, vertical or horizontal (type String).
 * <br>
 * <br>
 *
 * @author Rupert Young <rupert@moonsit.co.uk>
 * @version 1.0
 */
public class DisplayNeuralFunction extends NeuralFunction {
    
    private static final Logger LOG = Logger.getLogger(DisplayNeuralFunction.class.getName());
    
    private String alignment = null;
    private Integer x = 0;
    private Integer y = 0;
    private Integer width = null;
    private Integer height = null;
    private Integer font = null;
    private Integer places = 3;
    private ValuesJFrame vf;
    
    public DisplayNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);
        for (Parameters param : ps) {
            if (param.getName().equals("X")) {
                x = Integer.valueOf(param.getValue());
            }
            if (param.getName().equals("Y")) {
                y = Integer.valueOf(param.getValue());
            }
            if (param.getName().equals("Width")) {
                width = Integer.valueOf(param.getValue());
            }
            if (param.getName().equals("Height")) {
                height = Integer.valueOf(param.getValue());
            }
            if (param.getName().equals("Font")) {
                font = Integer.valueOf(param.getValue());
            }
            if (param.getName().equals("Places")) {
                places = Integer.valueOf(param.getValue());
            }
            if (param.getName().equals("Alignment")) {
                alignment = param.getValue();
            }
        }
    }
    
    private String extractName(String name) {
        if (name.contains(":")) {
            String[] arr = name.split(":");
            name = arr[0];
        }
        return name;
    }
    
    @Override
    public void init() throws Exception {
        List<BaseControlFunction> controls = links.getControlList();
        //controls.sort(linkAlphaComparator);
        
        List<DisplayValue> list = new ArrayList<>();
        int size = controls.size();
        for (int i = 0; i < controls.size(); i++) {
            String linkType = links.getType(i);
            if (linkType != null && linkType.equalsIgnoreCase("Indexed")) {
                size = controls.get(i).getNeural().getParametersSize();
                for (int j = 0; j < size; j++) {
                    list.add(new DisplayValue(controls.get(i).getNeural().getParameterName(j), controls.get(i).getNeural().getParameter(j), places));
                }
                break;
            }
            if (linkType == null) {
                list.add(new DisplayValue(controls.get(i).getName(), controls.get(i).getValue(), places));
            } else {
                list.add(new DisplayValue(linkType, controls.get(i).getNeural().getParameter(extractName(linkType.toLowerCase())), places));
            }
        }
        vf = new ValuesJFrame(x, y, width, height, font, size, alignment);
        vf.initList(list);

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                vf.setVisible(true);
            }
        });
    }

    /*
    @Override
    public void setParameter(String par) {
        super.setParameter(par);
        String[] arr = par.split(":");
        if (arr[0].equals("Places")) {
            vf.setPlaces(Integer.parseInt(arr[1]));
        }
    }*/
    @Override
    public double compute() throws Exception {
        List<BaseControlFunction> controls = links.getControlList();
        
        for (int i = 0; i < controls.size(); i++) {
            String linkType = links.getType(i);
            
            if (linkType != null && linkType.equalsIgnoreCase("Indexed")) {
                int size = controls.get(i).getNeural().getParametersSize();
                for (int j = 0; j < size; j++) {
                    vf.changeValue(j, controls.get(i).getNeural().getParameter(j));
                }
                break;
            }
            if (linkType == null) {
                vf.changeValue(i, controls.get(i).getValue());
                
            } else {
                vf.changeValue(i, controls.get(i).getNeural().getParameter(extractName(linkType).toLowerCase()));
            }
            
        }
        
        return output;
    }
    
    @Override
    public void close() throws Exception {
        if (vf != null) {
            vf.setVisible(false);
            vf.dispose();
        }
    }
    
}
