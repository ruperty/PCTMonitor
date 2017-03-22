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
package uk.co.moons.control.neural.models;

import uk.co.moons.control.functions.BaseControlFunction;
import java.util.List;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.neural.NeuralFunction;

public class VelocityModelNeuralFunction extends NeuralFunction {

    //public Double mass = null;
    //public Double time = null;
    public Double velocity = 0.0;
    private double previousVelocity = 0;
    private double previousMass = 0;
    private double previousMomentum = 0;
    private boolean first = true;

    public VelocityModelNeuralFunction() {
        super();
    }

    public VelocityModelNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            String pname = param.getName();

            if (pname.equals("Velocity")) {
                velocity = Double.parseDouble(param.getValue());
            }
        }
        previousVelocity = velocity;
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double dtime = controls.get(0).getValue() / 1000;
        double force = controls.get(1).getValue();
        double mass = controls.get(2).getValue();

        if (first) {
            previousMass = mass;
            first = false;
        }

        double newVelocity;

        newVelocity = previousVelocity + dtime * force / mass;
        double momentum = newVelocity * mass;
        //if (mass - previousMass != 0) {
        double dVelocity = dtime * (momentum - previousMomentum) / mass;
        //System.out.println("m " + momentum + " pm " + previousMomentum);
        //System.out.println("v " + newVelocity + " pv " + previousVelocity + " dV " + dVelocity + " " + mass + " " + previousMass);
        newVelocity += dVelocity;
        //}

        //velocity = output;
        output = newVelocity;
        previousVelocity = newVelocity;
        previousMass = mass;
        previousMomentum = momentum;
        return output;
    }
}
