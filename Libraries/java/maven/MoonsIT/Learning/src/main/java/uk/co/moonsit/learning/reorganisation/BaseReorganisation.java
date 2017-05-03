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
package uk.co.moonsit.learning.reorganisation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moonsit.learning.rate.BaseLearningRate;

/**
 * <b>....</b>
 *
 * <p>
 * ...
 *
 * <p>
 * The .
 *
 *
 *
 * @author Rupert Young <rupert@moonsit.co.uk>
 * @version 1.0
 */
public abstract class BaseReorganisation implements ReorganisationInterface {

    private static final Logger LOG = Logger.getLogger(BaseReorganisation.class.getName());

    public final static int HILLCLIMB = 1;
    public final static int ECOLI = 2;
    public final static int ECOLISIGN = 3;

    private final double TOLERANCE = 0.000001;

    protected double correction = 0;
    protected double delta = 0.025;
    protected boolean continuous = false;
    protected BaseLearningRate learningRate;
    protected double previousErrorResponse;
    private final String name;

    public BaseReorganisation(String name, String type, double lr, String parameters) throws Exception {
        this.name = name;
        setLRT(type, lr, parameters);
    }

    public static int getLearningType(String lt) {
        switch (lt) {
            case "HillClimb":
                return HILLCLIMB;
            case "Ecoli":
                return ECOLI;
            case "EcoliSign":
                return ECOLISIGN;

        }
        return 0;
    }

    public static String getLearningType(int lt) {
        switch (lt) {
            case HILLCLIMB:
                return "HillClimb";
            case ECOLI:
                return "Ecoli";
            case ECOLISIGN:
                return "EcoliSign";

        }
        return null;
    }

    public void reset() {
        learningRate.reset();
    }

    protected double computeCorrection(double lrate, double delta, double parameter, double parameterMA, double error) {
        double correct = lrate * delta;// * parameterMA;
        //int places = 4;

        System.out.println(String.format("%21s: %7.6f %7.6f %+11.6f %+11.6f", name, error, lrate, correct, parameter + correct));

        //System.out.println(name + ": " + MoonsString.formatPlaces(error, places) + " " + MoonsString.formatPlaces(lrate, places) + " "
        //      + MoonsString.formatPlaces(correct, places) + " " + MoonsString.formatPlaces(parameter + correct, places));
        //  LOG.log(Level.INFO, "{0} {1} {2}", new Object[]{MoonsString.formatPlaces( lrate,4), MoonsString.formatPlaces( correct,4), MoonsString.formatPlaces( parameterMA,4)});
        return correct;//* error;
    }

    protected boolean update(double errorResponse, double previousErrorResponse) {

        if (errorResponse > previousErrorResponse) {
            return true;
        }

        return Math.abs(errorResponse - previousErrorResponse) < TOLERANCE;
    }

    public double getCorrection() {
        return correction;
    }

    public void setCorrection(double correction) {
        this.correction = correction;
    }

    public void setAdaptiveFactor(Double delta) {
        this.delta = delta;
    }

    public void setContinuous(boolean continuous) {
        this.continuous = continuous;
    }

    public void setLearningRateMax(double learningRateMax) {
        learningRate.setLearningRateMax(learningRateMax);
    }

    @Override
    public void setLearningRate(double rate) {
        learningRate.setLearningRate(rate);
    }

    public double getLearningRate() {
        return learningRate.getLearningRate();
    }

    public void setLearningRateType(String type, double lr, String parameters) throws Exception {
        setLRT(type, lr, parameters);
    }

    private void setLRT(String type, double lr, String parameters) throws Exception {

        String className = "uk.co.moonsit.learning.rate." + type + "LearningRate";
        Class theClass = Class.forName(className);

        try {
            Constructor constructor = theClass.getConstructor(Double.class, String.class);
            try {
                learningRate = (BaseLearningRate) constructor.newInstance(lr, parameters);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(BaseReorganisation.class.getName()).log(Level.SEVERE, null, ex);
                throw ex;
            }

        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(BaseReorganisation.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        /*
        switch (type) {
        case "Smooth":
        learningRate = new SmoothLearningRate(lr);
        break;
        case "Additive":
        learningRate = new AdditiveLearningRate(lr);
        break;
        }*/

 /*
        switch (type) {
            case "Smooth":
                learningRate = new SmoothLearningRate(lr);
                break;
            case "Additive":
                learningRate = new AdditiveLearningRate(lr);
                break;
        }*/
    }

    public void setLearningRateParameters(String rateparameters) {
        learningRate.setLearningRateParameters(rateparameters);
    }

    public String getLearningRateParametersString() {
        return learningRate.getParametersString();
    }

}
