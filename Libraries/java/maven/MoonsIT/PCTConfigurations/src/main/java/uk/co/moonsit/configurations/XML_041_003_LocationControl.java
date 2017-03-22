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
package uk.co.moonsit.configurations;

import java.util.List;
import pct.moons.co.uk.schema.layers.ControlFunction;
import pct.moons.co.uk.schema.layers.Layers;
import pct.moons.co.uk.schema.layers.Layers.Layer;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.OutputFunctions;
import uk.co.moonsit.config.functions.Generic;
import uk.co.moonsit.config.functions.Globals;
import uk.co.moonsit.config.functions.Modules;
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class XML_041_003_LocationControl {

    private final boolean test = false;

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
                String file = Utils.getFileName("Robot", "041-003-LocationControl.xml");
        XML_041_003_LocationControl xml = new XML_041_003_LocationControl();

        Globals globals = Globals.getInstance();
        globals.put("SmoothRate_Smoothness", "0.999");
        globals.put("SmoothRate_Initial", "0");
        globals.put("MeanRate_Initial", "0");
        globals.put("Pause_Pause", "10");

        globals.put("ProximitySensor_Port", "S2");
        globals.put("ProximityControlReference_Constant", "50");
        globals.put("ProximitySensor_Type", "NXTUltrasonicSensor");
        globals.put("ProximitySensor_Mode", "NULL");

        globals.put("ProximitySensor_Max", "210");

        globals.put("ProximityInputSmooth_Smoothness", "0.5");
        globals.put("ProximityRawSmooth_Smoothness", "0.95");

        /*
         globals.put("ProximityVelocityReference_InScale", "0.1");
         globals.put("ProximityVelocityReference_OutScale", "-250");
         globals.put("ProximityVelocityReference_XShift", "-25");
         globals.put("ProximityVelocityReference_YShift", "300");//247
         globals.put("ProximityVelocityReference_InputTolerance", "1");
         globals.put("ProximityVelocityReference_Link", "ProximityControlError");
         */
        globals.put("ProximityVelocity_NamePrefix", "ProximityVelocityReference");

        globals.put("ProximityVelocityReference_InScale", "0.1");
        globals.put("ProximityVelocityReference_OutScale", "300");
        globals.put("ProximityVelocityReference_XShift", "-30");
        globals.put("ProximityVelocityReference_YShift", "300");
        globals.put("ProximityVelocityReference_InputTolerance", "1");
        globals.put("ProximityVelocityReference_Link", "ProximityInputSmooth");

        globals.put("ProximityVelocityDirectionReference_InScale", "0.1");
        globals.put("ProximityVelocityDirectionReference_OutScale", "100");
        globals.put("ProximityVelocityDirectionReference_XShift", "-30");
        globals.put("ProximityVelocityDirectionReference_YShift", "200");
        globals.put("ProximityVelocityDirectionReference_InputTolerance", "1");
        globals.put("ProximityVelocityDirectionReference_Link", "ProximityInputSmooth");

        globals.put("LocationDistanceControlWeight_InScale", "150000");
        globals.put("LocationDistanceControlWeight_OutScale", "500");//500
        globals.put("LocationDistanceControlWeight_XShift", "0");
        globals.put("LocationDistanceControlWeight_YShift", "0");

        globals.put("RotationWeightConsolidated_Constant", "1");

        globals.put("SpeedConsolidated_Weights", "1,0,1,1");
        globals.put("DirectionSpeedAdjustmentConsolidated_Weights", "0,1,1");

        globals.put("CompassSensor_Type", "HiTechnicCompassSensor");
        globals.put("CompassSensor_Port", "S4");
        globals.put("CompassSensor_AngleType", "AngleClockwise");
        globals.put("CompassVelocityReference_Constant", "0");
        globals.put("CompassBearingControlOutput_Gain", "-2");
        globals.put("CompassBearingControlReference_Type", "Constant");
        globals.put("CompassSensor_Interval", "5");

        globals.put("LatitudeControlOutput_Gain", "1");
        globals.put("LongitudeControlOutput_Gain", "1");
        globals.put("LatitudeControlReference_Link", "LatitudePhoneReference");
        globals.put("LongitudeControlReference_Link", "LongitudePhoneReference");

        globals.put("LatitudeSensor_Constant", "44");
        globals.put("LongitudeSensor_Constant", "15");

        globals.put("LatitudeSensor_Port", "6668");
        globals.put("LongitudeSensor_Port", "6668");

        globals.put("LatitudeReference_Constant", "44.000017");
        globals.put("LongitudeReference_Constant", "15.000017");

        globals.put("LatitudePhoneReference_Initial", "44.112979");
        globals.put("LongitudePhoneReference_Initial", "15.228926");

        globals.put("LongitudePhoneReference_Port", "6670");
        globals.put("LatitudePhoneReference_Port", "6670");

        globals.put("B_Acceleration", "1000");
        globals.put("C_Acceleration", "1000");
        globals.put("B_Sign", "1");
        globals.put("C_Sign", "1");

        globals.put("PhoneAccelerometerSensor_Port", "6669");
        globals.put("PhoneAccelerometerDirectionControlInput_Smoothness", "0.7");
        globals.put("PhoneAccelerometerSpeedControlInput_Smoothness", "0.7");

        globals.put("PhoneAccelerometerSpeedControlOutput_InScale", "0.2");
        globals.put("PhoneAccelerometerSpeedControlOutput_OutScale", "800");
        globals.put("PhoneAccelerometerSpeedControlOutput_XShift", "0");
        globals.put("PhoneAccelerometerSpeedControlOutput_YShift", "0");
        globals.put("PhoneAccelerometerSpeedControlOutput_InputTolerance", "0.1");

        globals.put("PhoneAccelerometerDirectionControlOutput_InScale", "0.3");
        globals.put("PhoneAccelerometerDirectionControlOutput_OutScale", "200");
        globals.put("PhoneAccelerometerDirectionControlOutput_XShift", "-8");
        globals.put("PhoneAccelerometerDirectionControlOutput_YShift", "197");
        globals.put("PhoneAccelerometerDirectionControlOutput_InputTolerance", "0.1");

        globals.put("ProximityPeriodReference_Constant", "500");

        //globals.put("SpeedWeight_Constant", "0");
        globals.put("VelocityTarget_Constant", "600");

        globals.put("ProximityInputLimiting_Infinity", "false");
        Utils.saveToXML(xml.run(), file);
        Utils.verify(file);

    }

    public XML_041_003_LocationControl() {

    }

    public Layers run() throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Basic control of value of sonic reading for obstacle avoidance, with flipping of rotation direction");
        List layer = layers.getLayer();

        layer.add(configureLayer0("Interface"));
        layer.add(Utils.emptyLayer("LocationControl"));
        layer.add(Utils.emptyLayer("CoordinateControl"));
        layer.add(Utils.emptyLayer("AccelerometerControl"));

        modules(layers, test);

        return layers;
    }

    public static void modules(Layers layers, boolean test) throws Exception {
        boolean sensors = false;
        if (!test) {
            Modules.moduleDriveMotorsOutput(layers, test);
            Modules.moduleMotorsMovingSensors(layers);
        }
        Modules.moduleProximityAvoidanceDoubleTurning(layers, test, sensors, true);
        Modules.moduleCompassSystems(layers, test, sensors, compassBearingReference());
        Modules.moduleLocationSystems(layers, test, sensors, 1);
        Modules.moduleCombinedSensors(layers, new String[]{"CompassSensor", "ProximitySensor", "LatitudeSensor", "LongitudeSensor"}, test);
        Modules.modulePhoneAccelerometerSystems(layers, test, 3, true);

    }

      public static Layers.Layer.Controller.Functions.ReferenceFunctions compassBearingReference() throws Exception {
        Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
        ControlFunction reference = Utils.configureControlFunction("CompassBearingControlReference",
                "Head direction reference, zero is centre", "",
                null, new String[][]{{"LocationBearingReference"}});

         {
            Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();

            ControlFunction transfer = Utils.configureControlFunction("LocationBearingReference", "Location bearing", "LocationBearing",
                    null, new String[][]{{"LongitudeControlOutput"}, {"LatitudeControlOutput"}});
            transfersList.add(transfer);

            referenceFunctions.setTransfers(transfers);
        }

        referenceFunctions.setReference(reference);
        return referenceFunctions;
    }
      
    public static Controller configureEnv(boolean test) throws Exception {
        Controller env = Generic.configureControllerEnv();
        ControlFunction function = Utils.configureControlFunction("RotationWeightConsolidated", "Rotation weight, 1 = no rotation", "Constant", new String[][]{{"Constant", Globals.getInstance().get("RotationWeightConsolidated_Constant"), "Double"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);

        //function = Utils.configureControlFunction("SpeedWeight", "Weight", "Constant", new String[][]{{"Constant", Globals.getInstance().get("SpeedWeight_Constant"), "Double"}}, null);
        //Utils.addTransferFunction(env, function, Utils.INPUT);
        function = Utils.configureControlFunction("VelocityTarget", "Speed target", "Constant",
                new String[][]{{"Constant", Globals.getInstance().get("VelocityTarget_Constant"), "Double"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);

        /*
         if (test) {
         function = Utils.configureControlFunction("LatitudeSensor", "LatitudeSensor", "Constant", new String[][]{{"Constant", Globals.getInstance().get("LatitudeSensor_Constant"), "Double"}}, null);
         Utils.addTransferFunction(env, function, Utils.INPUT);

         function = Utils.configureControlFunction("LongitudeSensor", "LongitudeSensor", "Constant", new String[][]{{"Constant", Globals.getInstance().get("LongitudeSensor_Constant"), "Double"}}, null);
         Utils.addTransferFunction(env, function, Utils.INPUT);

            
         function = Utils.configureControlFunction("LatitudePhoneReference", "LatitudePhoneReference", "Constant", new String[][]{{"Constant", Globals.getInstance().get("LatitudeReference_Constant"), "Double"}}, null);
         Utils.addTransferFunction(env, function, Utils.INPUT);

         function = Utils.configureControlFunction("LongitudePhoneReference", "LongitudePhoneReference", "Constant", new String[][]{{"Constant", Globals.getInstance().get("LongitudeReference_Constant"), "Double"}}, null);
         Utils.addTransferFunction(env, function, Utils.INPUT);
         }
         */
        return env;
    }

    private Layer configureLayer0(String name) throws Exception {
        Layer layer = new Layer();
        layer.setName(name);
        List<Controller> controllers = layer.getController();

        controllers.add(configureEnv(test));
        controllers.add(configureControllerMotorConsolidation());

        return layer;
    }

    public static Controller configureControllerMotorConsolidation() throws Exception {
        Controller controller = new Controller();
        controller.setName("MotorConsolidation");
        Functions functions = new Functions();

        String prefix = Globals.getInstance().get("ProximityVelocity_NamePrefix");

        Layers.Layer.Controller.Functions.ReferenceFunctions referenceFunctions = new Layers.Layer.Controller.Functions.ReferenceFunctions();
        ControlFunction reference = Utils.configureControlFunction(prefix, "Velocity reference, as a sigmoid function of the sonic proximity", "Sigmoid",
                new String[][]{{"InScale", Globals.getInstance().get(prefix + "_InScale"), "Double"},
                {"OutScale", Globals.getInstance().get(prefix + "_OutScale"), "Double"},
                {"XShift", Globals.getInstance().get(prefix + "_XShift"), "Double"},
                {"YShift", Globals.getInstance().get(prefix + "_YShift"), "Double"},
                {"InputTolerance", Globals.getInstance().get(prefix + "_InputTolerance"), "Double"}},
                new String[][]{{Globals.getInstance().get(prefix + "_Link")}});

        referenceFunctions.setReference(reference);
        {
            Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.ReferenceFunctions.Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();
            ControlFunction transfer = Utils.configureControlFunction("ProximityVelocityDirectionReference", "Velocity reference, as a sigmoid function of the sonic proximity", "Sigmoid",
                    new String[][]{{"InScale", Globals.getInstance().get("ProximityVelocityDirectionReference_InScale"), "Double"},
                    {"OutScale", Globals.getInstance().get("ProximityVelocityDirectionReference_OutScale"), "Double"},
                    {"XShift", Globals.getInstance().get("ProximityVelocityDirectionReference_XShift"), "Double"},
                    {"YShift", Globals.getInstance().get("ProximityVelocityDirectionReference_YShift"), "Double"},
                    {"InputTolerance", Globals.getInstance().get("ProximityVelocityDirectionReference_InputTolerance"), "Double"}},
                    new String[][]{{Globals.getInstance().get("ProximityVelocityDirectionReference_Link")}});
            transfersList.add(transfer);

            transfer = Utils.configureControlFunction("ProximityErrorSwitch", "1 if error is not zero, 0 otherwise", "LimitBanded",
                    new String[][]{{"Threshold", "0", "Double"}, {"BandWidth", "0.01", "Double"},
                    {"BandValue", "0", "Double"}, {"Upper", "1", "Double"}, {"Lower", "0", "Double"}},
                    new String[][]{{"ProximityControlError"}});
            transfersList.add(transfer);

            transfer = Utils.configureControlFunction("ProximityVelocityReferenceSwitch", "Weighted product of sonic error ratio and velocity", "WeightedMultiply",
                    new String[][]{{"Weights", "1,1", "String"}},
                    new String[][]{{"ProximityErrorSwitch"}, {"ProximityVelocityReference"}});
            transfersList.add(transfer);

            transfer = Utils.configureControlFunction("ProximityControlRatio", "Ratio of error and reference", "Division",
                    null, new String[][]{{"ProximityControlError"}, {"ProximityControlReference"}});
            transfersList.add(transfer);

            transfer = Utils.configureControlFunction("ProximityControlOutputWeighted", "Weighted product of sonic error ratio, velocity and direction", "WeightedMultiply",
                    new String[][]{{"Weights", "2,1,1", "String"}}, new String[][]{{"ProximityControlRatio"}, {"DirectionFlip"}, {"ProximityVelocityDirectionReference"}});
            transfersList.add(transfer);

            referenceFunctions.setTransfers(transfers);

        }
        functions.setReferenceFunctions(referenceFunctions);

        OutputFunctions outputFunctions = new OutputFunctions();
        ControlFunction output = Utils.configureControlFunction("SpeedConsolidated", "Exclusive function of speed input signals", "WeightedExclusive",
                new String[][]{{"Weights", Globals.getInstance().get("SpeedConsolidated_Weights"), "String"}},
                //new String[][]{{"PhoneAccelerometerSpeedControlOutput"}, {"ProximityVelocityReference"}, {"LocationDistanceControlReference"}});
                new String[][]{{"VelocityTarget"}, {"ProximityVelocityReferenceSwitch"}, {"PhoneAccelerometerSpeedControlOutput"}, {"LocationDistanceControlWeight"}});

        outputFunctions.setOutput(output);
        {
            Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();
            ControlFunction transfer = Utils.configureControlFunction("DirectionSpeedAdjustmentConsolidated", "Exclusive function of direction input signals", "WeightedExclusive",
                    new String[][]{{"Weights", Globals.getInstance().get("DirectionSpeedAdjustmentConsolidated_Weights"), "String"}},
                    //new String[][]{{"PhoneAccelerometerDirectionControlOutput"}, {"ProximityControlOutputWeighted"}, {"CompassBearingControlOutput"}});
                    new String[][]{{"ProximityControlOutputWeighted"}, {"PhoneAccelerometerDirectionControlOutput"}, {"CompassBearingControlOutput"}});

            transfersList.add(transfer);
            outputFunctions.setTransfers(transfers);
        }
        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

}
