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

import java.awt.geom.Point2D;
import java.util.List;
import org.odftoolkit.odfdom.dom.element.draw.DrawFrameElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class ODGController {

    private String name;
    private int layer;
    private ODGFunction input;
    private ODGFunction reference;
    private ODGFunction error;
    private ODGFunction output;
    private Point2D location;

    public ODGController(Element elem) {
        int functions = 0;
        NodeList nodeList = elem.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element child = (Element) node;
                String tname = child.getTagName();
                if (tname.equals("draw:frame")) {
                    DrawFrameElement frame = (DrawFrameElement) child;
                    Node textbox = frame.getFirstChild();
                    NodeList tboxNodeList = textbox.getChildNodes();
                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; j < tboxNodeList.getLength(); j++) {
                        sb.append(tboxNodeList.item(j).getFirstChild().getTextContent().trim());
                    }
                    name = sb.toString();
                    double x = Utils.convertCmCoordinate(frame.getSvgXAttribute());
                    double y = Utils.convertCmCoordinate(frame.getSvgYAttribute());
                    location = new Point2D.Double(x, y);
                }
                if (tname.equals("draw:custom-shape")) {
                    switch (functions) {
                        case 0:
                            input = new ODGFunction(name + "Input", child);
                            break;
                        case 1:
                            output = new ODGFunction(name + "Output", child);
                            break;
                        case 2:
                            reference = new ODGFunction(name + "Reference", child);
                            break;
                        case 3:
                            error = new ODGFunction(name + "Error", child);
                            break;
                    }
                    functions++;
                }
            }
        }
        if (error == null) {
            error = new ODGFunction(name + "Error");
        }

    }

    public boolean isDefaultError() {
        return reference.getConfigID() != null && input.getConfigID() != null;
    }

    public ODGFunction isIn(Point2D p) {
        if (p.distance(input.getLocation()) < ODGProcessing.FUNCTION_SIZE) {
            return input;
        }
        if (p.distance(reference.getLocation()) < ODGProcessing.FUNCTION_SIZE) {
            return reference;
        }
        if (p.distance(output.getLocation()) < ODGProcessing.FUNCTION_SIZE) {
            return output;
        }
        if (error.getLocation() != null) {
            if (p.distance(error.getLocation()) < ODGProcessing.FUNCTION_SIZE) {
                return error;
            }
        }

        return null;
    }

    public ODGFunction getFunction(String functionName) {
        if (input.getName().equals(functionName)) {
            return input;
        }
        if (error.getName().equals(functionName)) {
            return error;
        }
        if (output.getName().equals(functionName)) {
            return output;
        }
        if (reference.getName().equals(functionName)) {
            return reference;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public int getLayer() {
        return layer;
    }

    public ODGFunction getInput() {
        return input;
    }

    public ODGFunction getReference() {
        return reference;
    }

    public ODGFunction getError() {
        return error;
    }

    public ODGFunction getOutput() {
        return output;
    }

    public Point2D getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return name + " " + location;
    }

    public Integer setLayer(List<ODGLayer> layerList) {
        for (ODGLayer odglayer : layerList) {
            if (odglayer.isIn(location)) {
                layer = odglayer.getLevel();
                return layer;
            }
        }
        return null;
    }
}
