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
import java.util.Objects;
import java.util.logging.Logger;
import org.odftoolkit.odfdom.dom.element.draw.DrawConnectorElement;
import org.w3c.dom.Element;
import uk.co.moonsit.config.functions.Utils;
import uk.co.moonsit.utils.MoonsString;

/**
 *
 * @author ReStart
 */
public class ODGConnector {

    private static final Logger LOG = Logger.getLogger(ODGConnector.class.getName());

    private Point2D startPoint = null;
    private Point2D endPoint = null;
    private ODGConnectorPoint startConnectorPoint = null;
    private ODGConnectorPoint endConnectorPoint = null;
    private String type = null;

    public ODGConnector(Element elem) {
        switch (elem.getTagName()) {
            case "draw:connector": {
                DrawConnectorElement connector = (DrawConnectorElement) elem;
                startPoint = new Point2D.Double(Utils.convertCmCoordinate(connector.getSvgX1Attribute()), Utils.convertCmCoordinate(connector.getSvgY1Attribute()));
                endPoint = new Point2D.Double(Utils.convertCmCoordinate(connector.getSvgX2Attribute()), Utils.convertCmCoordinate(connector.getSvgY2Attribute()));
                break;
            }
            /*
            case "draw:path": {
                DrawPathElement connector = (DrawPathElement) elem;
                startPoint = new Point2D.Double(Utils.convertCmCoordinate(connector.getSvgX1Attribute()), Utils.convertCmCoordinate(connector.getSvgY1Attribute()));
                endPoint = new Point2D.Double(Utils.convertCmCoordinate(connector.getSvgX2Attribute()), Utils.convertCmCoordinate(connector.getSvgY2Attribute()));
                break;
            }*/
        }

        type = elem.getTextContent();
        //LOG.info("***** "+type);
    }

    public String getType() {
        return type;
    }

    public Point2D getStartPoint() {
        return startPoint;
    }

    public Point2D getEndPoint() {
        return endPoint;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(startPoint).append(" ").append(endPoint);
        if (startConnectorPoint != null) {
            sb.append(" s: ").append(startConnectorPoint.toString());
        }
        if (endConnectorPoint != null) {
            sb.append(" e: ").append(endConnectorPoint.toString());
        }
        return sb.toString();
    }

    public void setStartConnectorPoint(ODGConnectorPoint startConnectorPoint) {
        this.startConnectorPoint = startConnectorPoint;
    }

    public void setEndConnectorPoint(ODGConnectorPoint endConnectorPoint) {
        this.endConnectorPoint = endConnectorPoint;
    }

    public ODGConnectorPoint getStartConnectorPoint() {
        return startConnectorPoint;
    }

    public ODGConnectorPoint getEndConnectorPoint() {
        return endConnectorPoint;
    }

    public boolean isFunctionConfig() throws Exception {
        if (startConnectorPoint == null) {
            throw new Exception(endConnectorPoint.getName() +  " not connected at "
                    + MoonsString.formatPlaces(startPoint.getX(), 1) + " "
                    + MoonsString.formatPlaces(startPoint.getY(), 1) + " "
                    + MoonsString.formatPlaces(endPoint.getX(), 1) + " "
                    + MoonsString.formatPlaces(endPoint.getY(), 1));
        }
        return startConnectorPoint.getType().equals(ODGProcessing.FUNCTION_CONFIG);
    }

    public boolean isFunction() {
        return startConnectorPoint.getType().equals(ODGProcessing.FUNCTION);
    }

    public boolean isLoopback() {
        return (Objects.equals(endConnectorPoint.getId(), startConnectorPoint.getId()));
    }

    public boolean isFunctionLink() {
        return startConnectorPoint.getType().equals(ODGProcessing.FUNCTION) && endConnectorPoint.getType().equals(ODGProcessing.FUNCTION);
    }

    public boolean isStartController() {
        return startConnectorPoint.getId() == null;
    }

    public boolean isError(ODGController controller) {
        return startConnectorPoint.getName().equals(controller.getError().getName());
    }

    public boolean isEndController() {
        return endConnectorPoint.getId() == null;
    }

}
