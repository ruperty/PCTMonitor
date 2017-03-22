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
package uk.co.moonsit.config.odg;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.odftoolkit.odfdom.dom.element.draw.DrawCustomShapeElement;
import org.odftoolkit.odfdom.dom.element.text.TextPElement;
import org.odftoolkit.odfdom.dom.element.text.TextSpanElement;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class ODGLayer {

    private  String name;
    private  int level;
    private final Rectangle2D rectangle;

    private final List<Integer> controllers;

    public ODGLayer(Element elem) {
        controllers = new ArrayList<>();
        DrawCustomShapeElement custom = (DrawCustomShapeElement) elem;
        //LOG.info("Nname " + custom.getLocalName());
        NodeList list = custom.getChildNodes();

        int textItem = 0;

        for (int i = 0; i < list.getLength(); i++) {
            if(!list.item(i).getNodeName().equals("text:p"))
                continue;
            TextPElement tp = (TextPElement) list.item(i);
            TextSpanElement tsp = (TextSpanElement) tp.getFirstChild();
            String text = tsp.getTextContent().trim();
            String[] arr = text.split(" ");

            for (String s : arr) {
                switch (textItem) {
                    case 0:

                        break;
                    case 1:
                        level = Integer.parseInt(s);
                        break;
                    case 2:
                        name = s;
                        break;
                }
                textItem++;
            }
        }
        rectangle = new Rectangle2D.Double(Utils.convertCmCoordinate(custom.getSvgXAttribute()), Utils.convertCmCoordinate(custom.getSvgYAttribute()),
                Utils.convertCmDimension(custom.getSvgWidthAttribute()), Utils.convertCmDimension(custom.getSvgHeightAttribute()));

    }

    public List<Integer> getControllers() {
        return controllers;
    }

    public void addController(Integer i) {
        controllers.add(i);
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public boolean isIn(Point2D p) {
        return rectangle.contains(p);
    }

    public void sortControllers(List<ODGController> controllerList) {
        Collections.sort(controllers, new Comparator<Integer>() {

            @Override
            public int compare(Integer o1, Integer o2) {
                ODGController con1 = controllerList.get(o1);
                ODGController con2 = controllerList.get(o2);

                if (con1.getLocation().getX() < con2.getLocation().getX()) {
                    return -1;
                }

                if (con2.getLocation().getX() < con1.getLocation().getX()) {
                    return 1;
                }

                return 0;
            }
        });

    }

}
