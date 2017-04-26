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
package uk.co.moons.control;

//import icommand.nxt.Motor;
//import icommand.nxt.SensorPort;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.moons.handlers.MotorHandler;

/**
 * +
 *
 * @author ReStart
 */
public class RobotControlHierarchy extends ControlHierarchy {

    private static final Logger logger = Logger.getLogger(RobotControlHierarchy.class.getName());
    private MotorHandler motorHandler;

    public RobotControlHierarchy(String config, String parFile) throws Exception {
        super(config, parFile);
        motorHandler = new MotorHandler(true);
    }

    public RobotControlHierarchy(String config) throws Exception {
        super(config);
        motorHandler = new MotorHandler(true);
    }

    public RobotControlHierarchy() {
        layers = new ControlLayer[1];
        layers[0] = new ControlLayer();
    }

    public RobotControlHierarchy(int num) {
        layers = new ControlLayer[num];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = new ControlLayer();
        }
    }

    public boolean isStopFlag() {
        return motorHandler.getMotorsFlag();
    }

    public void setStopFlag(boolean stopFlag) {
        motorHandler.setMotorsFlag(stopFlag);
    }

    /*
    @Override
    public void init() throws Exception {
        initRobot();
    }*/
    @Override
    public void close() throws Exception {
        long start = System.currentTimeMillis();
        for (String name : hmControls.keySet()) {
            if (name.contains("Motor")) {
                closeNeuralFunction(name);
            }
        }
        for (String name : hmControls.keySet()) {
            if (!name.contains("Motor")) {
                closeNeuralFunction(name);
            }
        }
        logger.log(Level.INFO, "---> Close {0}", (System.currentTimeMillis() - start));
    }

    /*
    public void initRobot() throws Exception {

        if (orderedControllers != null && orderedControllers.size() > 0) {
            orderedInit();
        } else {
            for (String name : hmControls.keySet()) {
                initNeuralFunction(name);
            }
        }
     */
 /*
         initNeuralFunction("PhoneSensorRead");
         initNeuralFunction("AccSensorRead");
         initNeuralFunction("LinAccSensorRead");
         initNeuralFunction("GyroSensorRead");

         if (initNeuralFunction("AcceleromterSensorRead")) {
         isUsingNXT = true;
         }
         if (initNeuralFunction("SoundSensorRead")) {
         isUsingNXT = true;
         }
         if (initNeuralFunction("LightSensorRead")) {
         isUsingNXT = true;
         }
         if (initNeuralFunction("USensorRead")) {
         isUsingNXT = true;
         }
         if (initNeuralFunction("MotorBInput")) {
         isUsingNXT = true;
         }
         if (initNeuralFunction("MotorCInput")) {
         isUsingNXT = true;
         }
         if (initNeuralFunction("MotorBOutput")) {
         isUsingNXT = true;
         }
         if (initNeuralFunction("MotorCOutput")) {
         isUsingNXT = true;
         }
         if (initNeuralFunction("MotorBPower")) {
         isUsingNXT = true;
         }
         if (initNeuralFunction("MotorCPower")) {
         isUsingNXT = true;
         }
         if (initNeuralFunction("MotorBRotate")) {
         isUsingNXT = true;
         }
         if (initNeuralFunction("MotorCRotate")) {
         isUsingNXT = true;
         }
         if (initNeuralFunction("MotorBRotateTo")) {
         isUsingNXT = true;
         }
         if (initNeuralFunction("MotorCRotateTo")) {
         isUsingNXT = true;
         }
         if (initNeuralFunction("MotorBTachoCount")) {
         isUsingNXT = true;
         }
         if (initNeuralFunction("MotorCTachoCount")) {
         isUsingNXT = true;
         }

         if (initNeuralFunction("MotorCCountReference")) {
         isUsingNXT = true;
         }
         if (initNeuralFunction("MotorBCountReference")) {
         isUsingNXT = true;
         }
     */
 /*
         if (hmControls.get("SequenceSwitchInput") != null) {
         hmControls.get("SequenceSwitchInput").setValue(1);
         }
        
         if (hmControls.get("SpaceControlInput") != null) {
         hmControls.get("SpaceControlInput").setValue(1);
         }
     */
    //}
}
