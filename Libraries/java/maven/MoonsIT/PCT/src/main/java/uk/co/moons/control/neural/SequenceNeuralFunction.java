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
import java.util.logging.Level;
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.functions.BaseControlFunction;

/**
 *
 * @author ReStart
 */
public class SequenceNeuralFunction extends NeuralFunction {

    private static final Logger LOG = Logger.getLogger(SequenceNeuralFunction.class.getName());

    public String tolerances = null;

    private List<Double> tolerancesList = null;
    private int[] sequenceList;
    private Integer resetIndex = null;
    private boolean first = true;

    public SequenceNeuralFunction() throws Exception {
        super();
    }

    public SequenceNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();
            if (pname.equals("Tolerances")) {
                tolerances = param.getValue();
            }
        }

        if (tolerances == null) {
            throw new Exception("Tolerances missing for SequenceNeuralFunction");
        }

    }

    @Override
    public void verifyConfiguration() throws Exception {

        BaseControlFunction bcfReset = null;
        List<BaseControlFunction> controls = links.getControlList();
        for (int i = 0; i < controls.size(); i++) {
            String linkType = links.getType(i);
            if (linkType == null) {
                continue;
            }
            if (linkType.equals("Reset")) {
                bcfReset = controls.remove(i);
                links.removeType(i);
                break;
            }
        }

        if (bcfReset == null) {
            throw new Exception("There must be a Reset link in SequenceNeuralFunction");
        }

        controls.add(bcfReset);
        links.addType("Reset");
    }

    @Override
    public void init() throws Exception {
        super.init();
        setTolerances();
        sequenceList = new int[tolerancesList.size()];

        List<BaseControlFunction> controls = links.getControlList();
        int i = controls.size() - 1;
        String linkType = links.getType(i);
        if (linkType != null && linkType.equals("Reset")) {
            resetIndex = i;
        } else {
            throw new Exception("Reset must be last link in SequenceNeuralFunction");
        }
    }

    private void reset() throws Exception {
        List<BaseControlFunction> controls = links.getControlList();
        Double resetLink = null;
        if (resetIndex != null) {
            resetLink = controls.get(resetIndex).getValue();
        }

        if (resetLink != null && resetLink == 1) {
            first = true;
            setTolerances();
            for (int i = 0; i < sequenceList.length; i++) {
                sequenceList[i] = 0;
            }
        }
    }

    @Override
    public double compute() throws Exception {
        List<BaseControlFunction> controls = links.getControlList();
        reset();
        int sum = 0;
        if (first) {
            first = false;
        } else {
            for (int i = 0; i < controls.size() - 1; i++) {
                if (sum==i && sequenceList[i] == 0) {
                    if (Math.abs(controls.get(i).getValue()) < tolerancesList.get(i)) {
                        sequenceList[i] = 1;
                    }
                }

                sum += sequenceList[i];
            }
        }
        output = sum;

        return output;
    }

    private void setTolerances() throws Exception {
        if (tolerancesList == null) {
            tolerancesList = new ArrayList<>();
        }

        tolerancesList.clear();
        String[] arr = tolerances.split(",");
        if (arr.length < links.getControlList().size() - 1) {
            throw new Exception("Tolerances size in SequenceNeuralFunction is not equal number of links");
        }
        for (String tol : arr) {
            tolerancesList.add(Double.parseDouble(tol));
        }
    }

    @Override
    public void setParameter(String par) throws Exception {
        super.setParameter(par);
        String[] arr = par.split(":");
        if (arr[0].equals("Tolerances")) {
            tolerances = arr[1];
            setTolerances();
        }
    }

    @Override
    public String getParametersString() {
        StringBuilder sb = new StringBuilder();
        double prod = 1;
        sb.append("Tolerances").append(":");
        String[] arr = tolerances.split(",");
        for (String s : arr) {
            prod *= Double.parseDouble(s);
        }
        sb.append(prod);

        return sb.toString();
    }

}
