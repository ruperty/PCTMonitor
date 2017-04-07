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
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import org.odftoolkit.odfdom.dom.element.draw.DrawCustomShapeElement;
import org.odftoolkit.odfdom.dom.element.draw.DrawGElement;
import org.odftoolkit.odfdom.dom.element.text.TextPElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class ODGFunctionConfig {

    private Rectangle2D rectangle = null;
    private String name;
    private String type;
    private List<String[]> parametersList;

    public ODGFunctionConfig(Element elem) throws Exception {
        parametersList = new ArrayList<>();
        NodeList elemNodeList = elem.getChildNodes();
        for (int i = 0; i < elemNodeList.getLength(); i++) {
            Node node = elemNodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("draw:g")) {
                DrawGElement group = (DrawGElement) (Element) node;
                String groupTypeName = null;
                NodeList groupNodeList = group.getChildNodes();
                for (int j = 0; j < groupNodeList.getLength(); j++) {
                    Node childNode = groupNodeList.item(j);
                    if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                        String tname = childNode.getNodeName();
                        if (tname.equals("draw:custom-shape")) {
                            DrawCustomShapeElement custom = (DrawCustomShapeElement) childNode;
                            if (rectangle == null) {
                                rectangle = new Rectangle2D.Double(Utils.convertCmCoordinate(custom.getSvgXAttribute()), Utils.convertCmCoordinate(custom.getSvgYAttribute()),
                                        Utils.convertCmDimension(custom.getSvgWidthAttribute()), Utils.convertCmDimension(custom.getSvgHeightAttribute()));
                            } else {
                                Rectangle2D tmpRectangle = new Rectangle2D.Double(Utils.convertCmCoordinate(custom.getSvgXAttribute()), Utils.convertCmCoordinate(custom.getSvgYAttribute()),
                                        Utils.convertCmDimension(custom.getSvgWidthAttribute()), Utils.convertCmDimension(custom.getSvgHeightAttribute()));
                                rectangle = rectangle.createUnion(tmpRectangle);
                            }

                            if (j == 0) {
                                groupTypeName = childNode.getFirstChild().getFirstChild().getTextContent();
                            }
                            if (j == 1) {
                                switch (groupTypeName) {
                                    case "Name":
                                        name = childNode.getFirstChild().getFirstChild().getTextContent();
                                        if (name.contains("<") || name.contains(">")) {
                                            throw new Exception("Function configuration name contains invalid characters " + name);
                                        }

                                        break;
                                    case "Type":
                                        type = childNode.getFirstChild().getFirstChild().getTextContent();
                                        break;
                                }
                            }
                        } else if (tname.equals("draw:g")) {
                            if (j >= 1) {
                                switch (groupTypeName) {
                                    case "Parameters":
                                        processParameters(childNode);
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /*private void processNode(){
        
                            Node childNode = groupNodeList.item(j);
                    if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                        String tname = childNode.getNodeName();
                        if (tname.equals("draw:custom-shape")) {
                            DrawCustomShapeElement custom = (DrawCustomShapeElement) childNode;
                            if (rectangle == null) {
                                rectangle = new Rectangle2D.Double(Utils.convertCmCoordinate(custom.getSvgXAttribute()), Utils.convertCmCoordinate(custom.getSvgYAttribute()),
                                        Utils.convertCmDimension(custom.getSvgWidthAttribute()), Utils.convertCmDimension(custom.getSvgHeightAttribute()));
                            } else {
                                Rectangle2D tmpRectangle = new Rectangle2D.Double(Utils.convertCmCoordinate(custom.getSvgXAttribute()), Utils.convertCmCoordinate(custom.getSvgYAttribute()),
                                        Utils.convertCmDimension(custom.getSvgWidthAttribute()), Utils.convertCmDimension(custom.getSvgHeightAttribute()));
                                rectangle = rectangle.createUnion(tmpRectangle);
                            }

                            if (j == 0) {
                                groupTypeName = childNode.getFirstChild().getFirstChild().getTextContent();
                            }
                            if (j == 1) {
                                switch (groupTypeName) {
                                    case "Name":
                                        name = childNode.getFirstChild().getFirstChild().getTextContent();
                                        break;
                                    case "Type":
                                        type = childNode.getFirstChild().getFirstChild().getTextContent();
                                        break;
                                }
                            }
                        } else if (tname.equals("draw:g")) {
                            if (j == 1) {
                                switch (groupTypeName) {
                                    case "Parameters":
                                        processParameters(childNode);
                                        break;
                                }
                            }
                        }

    }*/
    public Point2D getLocation() {
        return new Point2D.Double(rectangle.getCenterX(), rectangle.getCenterY());
    }

    private void processParameters(Node node) {
        DrawGElement group = (DrawGElement) node;
        NodeList groupNodeList = group.getChildNodes();
        String[] parameter = new String[3];
        DrawCustomShapeElement custom = (DrawCustomShapeElement) groupNodeList.item(0);
        Rectangle2D tmpRectangle = new Rectangle2D.Double(Utils.convertCmCoordinate(custom.getSvgXAttribute()), Utils.convertCmCoordinate(custom.getSvgYAttribute()),
                Utils.convertCmDimension(custom.getSvgWidthAttribute()), Utils.convertCmDimension(custom.getSvgHeightAttribute()));
        rectangle = rectangle.createUnion(tmpRectangle);

        String pdatatype = custom.getFirstChild().getTextContent();
        custom = (DrawCustomShapeElement) groupNodeList.item(1);
        tmpRectangle = new Rectangle2D.Double(Utils.convertCmCoordinate(custom.getSvgXAttribute()), Utils.convertCmCoordinate(custom.getSvgYAttribute()),
                Utils.convertCmDimension(custom.getSvgWidthAttribute()), Utils.convertCmDimension(custom.getSvgHeightAttribute()));
        rectangle = rectangle.createUnion(tmpRectangle);

        String pname = custom.getFirstChild().getTextContent();
        custom = (DrawCustomShapeElement) groupNodeList.item(2);
        tmpRectangle = new Rectangle2D.Double(Utils.convertCmCoordinate(custom.getSvgXAttribute()), Utils.convertCmCoordinate(custom.getSvgYAttribute()),
                Utils.convertCmDimension(custom.getSvgWidthAttribute()), Utils.convertCmDimension(custom.getSvgHeightAttribute()));
        rectangle = rectangle.createUnion(tmpRectangle);

        StringBuilder pvalue = new StringBuilder();

        NodeList tNodeList = custom.getChildNodes();
        for (int i = 0; i < tNodeList.getLength(); i++) {
            Node tnode = tNodeList.item(i);
            pvalue.append(tnode.getTextContent().trim());
        }

        parameter[0] = pname;
        parameter[1] = pvalue.toString();
        parameter[2] = pdatatype;
        parametersList.add(parameter);
    }

    public String[][] getParameters() {
        String[][] list = new String[parametersList.size()][];
        int index = 0;
        for (String[] par : parametersList) {
            list[index++] = par;
        }
        return list;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isIn(Point2D p) {
        return rectangle.contains(p);
    }

    @Override
    public String toString() {
        return rectangle.toString();
    }

    public void setName(String name) {
        this.name = name;
    }

}
