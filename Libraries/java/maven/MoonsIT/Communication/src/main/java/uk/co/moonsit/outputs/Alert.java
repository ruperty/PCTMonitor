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
package uk.co.moonsit.outputs;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moonsit.outputs.impl.AlertImpl;

/**
 *
 * @author Rupert
 */
public class Alert {

    private AlertImpl alert;

    public static final int SHORTBEEP = 0;
    public static final int DOUBLEBEEP = 1;
    public static final int DESCENDINGARPEGGIO = 2;
    public static final int ASCENDINGARPEGGIO = 3;
    public static final int LONGLOWBUZZ = 4;
    public static final int BEEPSEQUENCEUP = 5;
    public static final int BEEPSEQUENCE = 6;
    public static final int BEEP = 7;
    public static final int TWOBEEPS = 8;
    public static final int BUZZ = 9;    
    public static final int LIGHT = 10;

    private int type = BEEPSEQUENCEUP;
    private boolean init = false;
    private String stype;

    public Alert(String t) {
        this.alert = null;
        this.stype = t;

    }

    public Alert() {
        alert = new AlertImpl();
    }

    private void init() throws NoSuchFieldException, IllegalAccessException {
        Class<?> c = this.getClass();
        Field field = c.getField(stype.toUpperCase());
        type = field.getInt(this);

        alert = new AlertImpl(type);
        init = true;
    }

    public void alert() throws NoSuchFieldException, IllegalAccessException {
        if (!init) {
            init();
        }
        alert.alert();
    }

    public void soundAlert(int type) {
        alert.soundAlert(type);
    }

    public void close() {
        alert.close();
    }

    public static void main(String[] args) {
        try {
            Alert a = new Alert("light");
            a.alert();
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            Logger.getLogger(Alert.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
