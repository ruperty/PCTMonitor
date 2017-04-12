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

import uk.co.moons.control.functions.BaseControlFunction;
import java.util.List;
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.math.RMath;
import uk.co.moonsit.learning.error.RMSErrorResponse;
import uk.co.moonsit.learning.reorganisation.BaseReorganisation;
import uk.co.moonsit.learning.reorganisation.EcoliReorganisation;
import uk.co.moonsit.learning.reorganisation.HillClimbReorganisation;

public class ParameterReorganisationNeuralFunction extends NeuralFunction {

    private static final Logger LOG = Logger.getLogger(ParameterReorganisationNeuralFunction.class.getName());

    public double learningrate;
    public double learningratemax;
    public double shortma;
    public double longma;
    public double parametersmoothfactor=0;
    public double parameterma;

    private double parameter;
    private String type = null;
    private Integer period;
    private Integer counter;

    private BaseReorganisation reorganisation;
    private BaseNeuralFunction parameterNeuralFunction;
    private BaseNeuralFunction errorResponseNeuralFunction;

    public Double adaptivesmoothupper = null;
    public Double adaptivesmoothlower = null;
    public Double adaptivefactor = null;
    public boolean continuous = false;

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
            if (pname.equals("LearningRateMax")) {
                learningratemax = Double.parseDouble(param.getValue());
            }
            if (pname.equals("AdaptiveSmoothUpper")) {
                adaptivesmoothupper = Double.parseDouble(param.getValue());
            }
            if (pname.equals("AdaptiveSmoothLower")) {
                adaptivesmoothlower = Double.parseDouble(param.getValue());
            }
            if (pname.equals("AdaptiveFactor")) {
                adaptivefactor = Double.parseDouble(param.getValue());
            }
             if (pname.equals("ParameterSmoothFactor")) {
                parametersmoothfactor = Double.parseDouble(param.getValue());
            }
            if (pname.equals("Type")) {
                type = param.getValue();
            }
            if (pname.equals("Continuous")) {
                continuous = Boolean.parseBoolean(param.getValue());
            }
        }

        if (type == null) {
            throw new Exception("Type null for ParameterReorganisationNeuralFunction");
        }
    }

    @Override
    public void init() throws Exception {
        List<BaseControlFunction> controls = links.getControlList();
        int errorIndex = 0;
        int parameterIndex = 1;

        for (int i = 0; i < controls.size(); i++) {
            String linkType = links.getType(i);
            if (linkType == null) {
                continue;
            }
            if (linkType.equals("ErrorResponse")) {
                errorIndex = i;
                continue;
            }
            if (linkType.equals("Parameter")) {
                parameterIndex = i;
            }
        }

        parameterNeuralFunction = controls.get(parameterIndex).getNeural();
        parameter = parameterNeuralFunction.getParameter();
        parameterma = parameter;

        errorResponseNeuralFunction = controls.get(errorIndex).getNeural();
        period = errorResponseNeuralFunction.getParameterInt(RMSErrorResponse.PERIOD);

        switch (type) {
            case "HillClimb":
                reorganisation = new HillClimbReorganisation(learningrate, learningratemax, adaptivesmoothupper, adaptivesmoothlower, adaptivefactor, continuous);
                reorganisation.reset();
                break;
            case "Ecoli":
                reorganisation = new EcoliReorganisation(learningrate, learningratemax, adaptivesmoothupper, adaptivesmoothlower, adaptivefactor, continuous);
                reorganisation.reset();
                break;
        }
    }

    @Override
    public double compute() throws Exception {
        double errorResponse = errorResponseNeuralFunction.getOutput();
        parameter = parameterNeuralFunction.getParameter();
        parameterma = RMath.smooth(parameter, parameterma, parametersmoothfactor);
        counter = errorResponseNeuralFunction.getParameterInt(RMSErrorResponse.COUNTER) - 1;

        parameter = reorganisation.correct(errorResponse, period, counter, parameter, parameterma);
        { // display parameters
            learningrate = reorganisation.getLearningRate();
            shortma = reorganisation.getShortMA();
            longma = reorganisation.getLongMA();
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

    @Override
    public void setParameter(String par) throws Exception {
        super.setParameter(par);
        String[] arr = par.split(":");
        if (arr[0].equalsIgnoreCase("learningratemax")) {
            reorganisation.setLearningRate(Double.parseDouble(arr[1]));
        }
        if (arr[0].equalsIgnoreCase("learningrate")) {
            reorganisation.setLearningRateMax(Double.parseDouble(arr[1]));
        }
        if (arr[0].equalsIgnoreCase("adaptivesmoothupper")) {
            reorganisation.setAdaptiveSmoothUpper(Double.parseDouble(arr[1]));
            reorganisation.reset();
        }
        if (arr[0].equalsIgnoreCase("adaptivesmoothlower")) {
            reorganisation.setAdaptiveSmoothLower(Double.parseDouble(arr[1]));
            reorganisation.reset();
        }
        if (arr[0].equalsIgnoreCase("adaptivefactor")) {
            reorganisation.setAdaptiveFactor(Double.parseDouble(arr[1]));
        }
        if (arr[0].equalsIgnoreCase("continuous")) {
            reorganisation.setContinuous(Boolean.parseBoolean(arr[1]));
        }
    }
}
