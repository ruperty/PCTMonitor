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

import java.lang.reflect.Constructor;
import uk.co.moons.control.functions.BaseControlFunction;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.math.RMath;
import uk.co.moonsit.learning.error.RMSErrorResponse;
import uk.co.moonsit.learning.rate.BaseLearningRate;
import uk.co.moonsit.learning.reorganisation.BaseReorganisation;

public class ParameterReorganisationNeuralFunction extends NeuralFunction {

    private static final Logger LOG = Logger.getLogger(ParameterReorganisationNeuralFunction.class.getName());

    public double learningrate;
    public double correction;

    public double learningratemax;
    //public double shortma;
    //public double longma;
    public double parametersmoothfactor = 0;
    public double parameterma;
    public Double parametermax = null;
    public Double parametermin = null;

    private double parameter;
    public String learningtype = null;
    public String learningratetype = "Smooth";
    public String rateparameters = "0.9^0.95";
    private Integer period;
    private Integer counter;

    private BaseReorganisation reorganisation;
    private BaseNeuralFunction parameterNeuralFunction;
    private BaseNeuralFunction errorResponseNeuralFunction;

    /*public Double adaptivesmoothupper = null;
    public Double adaptivesmoothlower = null;*/
    public Double adaptivefactor = null;
    public Boolean continuous = false;
    private boolean applyCorrection = false;
    private Integer correctionIndex = null;

    public ParameterReorganisationNeuralFunction() {
        super();
    }

    public ParameterReorganisationNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();

            if (pname.equals("LearningRate")) {
                learningrate = Double.parseDouble(param.getValue());
            }
            if (pname.equals("LearningRateType")) {
                learningratetype = param.getValue();
            }

            if (pname.equals("RateParameters")) {
                rateparameters = param.getValue();
            }

            if (pname.equals("LearningRateMax")) {
                learningratemax = Double.parseDouble(param.getValue());
            }
            /*if (pname.equals("AdaptiveSmoothUpper")) {
                adaptivesmoothupper = Double.parseDouble(param.getValue());
            }
            if (pname.equals("AdaptiveSmoothLower")) {
                adaptivesmoothlower = Double.parseDouble(param.getValue());
            }*/
            if (pname.equals("AdaptiveFactor")) {
                adaptivefactor = Double.parseDouble(param.getValue());
            }
            if (pname.equals("ParameterSmoothFactor")) {
                parametersmoothfactor = Double.parseDouble(param.getValue());
            }
            if (pname.equals("ParameterMax")) {
                parametermax = Double.parseDouble(param.getValue());
            }
            if (pname.equals("ParameterMin")) {
                parametermin = Double.parseDouble(param.getValue());
            }
            if (pname.equals("LearningType")) {
                learningtype = param.getValue();
            }
            if (pname.equals("Continuous")) {
                continuous = Boolean.parseBoolean(param.getValue());
            }
        }

        if (learningtype == null) {
            throw new Exception("LearningType null for ParameterReorganisationNeuralFunction");
        }
    }

    @Override
    public void init() throws Exception {
        List<BaseControlFunction> controls = links.getControlList();
        Integer errorIndex = null;
        Integer parameterIndex = null;

        for (int i = 0; i < controls.size(); i++) {
            String linkType = links.getType(i);
            if (linkType == null) {
                continue;
            }
            if (linkType.equals("Error")) {
                errorIndex = i;
                continue;
            }
            if (linkType.equals("Parameter")) {
                parameterIndex = i;
            }
            if (linkType.equals("Update")) {
                correctionIndex = i;
            }
        }
        
        if(parameterIndex==null)
             throw new Exception("Parameter link has not been set for " + this.getName());
        if(errorIndex==null)
             throw new Exception("Error link has not been set for " + this.getName());
   
        parameterNeuralFunction = controls.get(parameterIndex).getNeural();
        parameter = parameterNeuralFunction.getParameter();
        parameterma = parameter;

        errorResponseNeuralFunction = controls.get(errorIndex).getNeural();
        if (correctionIndex == null) {
            try {
                period = errorResponseNeuralFunction.getParameterInt(RMSErrorResponse.PERIOD);
            } catch (Exception ex) {
                Logger.getLogger(ParameterReorganisationNeuralFunction.class.getName()).log(Level.SEVERE, null, ex);
                throw new Exception("Has the error link been set for " + this.getName());
            }
        }

        setLearningType();
    }

    @Override
    public String getParametersString() {
        StringBuilder sb = new StringBuilder();

        sb.append("LearningRateMax").append(":");
        sb.append(learningratemax).append("_");

        sb.append("AdaptiveFactor").append(":");
        sb.append(adaptivefactor).append("_");

        sb.append("LearningType").append(":");
        sb.append(BaseReorganisation.getLearningType(learningtype)).append("_");

        sb.append("LearningRateType").append(":");
        sb.append(BaseLearningRate.getLearningRateType(learningratetype)).append("_");
        if (reorganisation != null) {
            sb.append(reorganisation.getLearningRateParametersString());
        }

        return sb.toString();
    }

    private void setLearningType() throws Exception {

        String className = "uk.co.moonsit.learning.reorganisation." + learningtype + "Reorganisation";
        Class theClass = Class.forName(className);

        Constructor constructor = theClass.getConstructor(String.class, String.class, Double.class, String.class, Double.class, Boolean.class);

        reorganisation = (BaseReorganisation) constructor.newInstance(this.getName(), learningratetype, learningrate, rateparameters, adaptivefactor, continuous);
        reorganisation.reset();
        reorganisation.setLearningRateMax(learningratemax);

        /*
        switch (learningtype.toLowerCase()) {
            case "hillclimb":
                reorganisation = new HillClimbReorganisation(learningratetype, learningrate, rateparameters, adaptivefactor, continuous);
                reorganisation.reset();
                break;
            case "ecoli":
                reorganisation = new EcoliReorganisation(learningratetype, learningrate, rateparameters, adaptivefactor, continuous);
                reorganisation.reset();
                break;
        }*/
    }

    @Override
    public double compute() throws Exception {
        double errorResponse = errorResponseNeuralFunction.getOutput();
        parameter = parameterNeuralFunction.getParameter();
        parameterma = RMath.smooth(parameter, parameterma, parametersmoothfactor);

        applyCorrection = applyCorrection();
        parameter = applyLimits(reorganisation.correct(errorResponse, applyCorrection, parameter, parameterma));
        { // display parameters
            learningrate = reorganisation.getLearningRate();
            correction = reorganisation.getCorrection();
            //shortma = reorganisation.getShortMA();
            //longma = reorganisation.getLongMA();
        }/*
        if (previousErrorResponse != errorResponse) {
            //parameter += reorganisation.correction(errorResponse) ;
            parameter += reorganisation.correction(errorResponse) * parameter;
            previousErrorResponse = errorResponse;
        }*/
        //parameterNeuralFunction.setParameter(parameter < 0 ? 0 : parameter);
        parameterNeuralFunction.setParameter(parameter);
        output = parameter;
        return output;
    }

    private double applyLimits(double par) {

        if (parametermax != null && par > parametermax) {
            return parametermax;
        }

        if (parametermin != null && par < parametermin) {
            return parametermin;
        }
        return par;
    }

    private boolean applyCorrection() throws Exception {
        if (correctionIndex != null) {
            if (links.getControlList().get(correctionIndex).getValue() == 1) {
                return true;
            }

        } else {
            counter = errorResponseNeuralFunction.getParameterInt(RMSErrorResponse.COUNTER) - 1;
            if (counter % period == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setParameter(String par) throws Exception {
        super.setParameter(par);
        String[] arr = par.split(":");

        if (arr[0].equalsIgnoreCase("learningratemax")) {
            learningratemax = Double.parseDouble(arr[1]);
            if (reorganisation != null) {
                reorganisation.setLearningRateMax(learningratemax);
            }
        }
        if (arr[0].equalsIgnoreCase("learningrate")) {
            learningrate = Double.parseDouble(arr[1]);
            if (reorganisation != null) {
                reorganisation.setLearningRate(learningrate);
            }
        }
        if (arr[0].equalsIgnoreCase("learningratetype")) {
            learningratetype = arr[1];
            if (reorganisation != null) {
                reorganisation.setLearningRateType(learningratetype, learningrate, rateparameters);
            }
        }
        if (arr[0].equalsIgnoreCase("learningtype")) {
            learningtype = arr[1];
            if (reorganisation != null) {
                setLearningType();
            }
        }
        /*
        if (arr[0].equalsIgnoreCase("adaptivesmoothupper")) {
            adaptivesmoothupper = Double.parseDouble(arr[1]);
            if (reorganisation != null) {
                reorganisation.setAdaptiveSmoothUpper(adaptivesmoothupper);
                reorganisation.reset();
            }
        }
        if (arr[0].equalsIgnoreCase("adaptivesmoothlower")) {
            adaptivesmoothlower = Double.parseDouble(arr[1]);
            if (reorganisation != null) {
                reorganisation.setAdaptiveSmoothLower(adaptivesmoothlower);
                reorganisation.reset();
            }
        }*/
        if (arr[0].equalsIgnoreCase("adaptivefactor")) {
            adaptivefactor = Double.parseDouble(arr[1]);
            if (reorganisation != null) {
                reorganisation.setAdaptiveFactor(adaptivefactor);
            }
        }
        if (arr[0].equalsIgnoreCase("continuous")) {
            continuous = Boolean.parseBoolean(arr[1]);
            if (reorganisation != null) {
                reorganisation.setContinuous(continuous);
            }
        }

        if (arr[0].equalsIgnoreCase("rateparameters")) {
            rateparameters = arr[1];
            if (reorganisation != null) {
                reorganisation.setLearningRateParameters(rateparameters);
            }
        }

    }
}
