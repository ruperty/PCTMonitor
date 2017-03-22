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
public class XML_030_004_AvoidanceOffline {

    private final boolean test = true;

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
                String file = Utils.getFileName("Robot", "030-004-AvoidanceOffline.xml");
        XML_030_004_AvoidanceOffline xml = new XML_030_004_AvoidanceOffline();

        Globals globals = Globals.getInstance();
        globals.put("SmoothRate_Smoothness", "0.999");
        globals.put("SmoothRate_Initial", "0");
        globals.put("MeanRate_Initial", "0");
        globals.put("Pause_Pause", "100");

        globals.put("ProximitySensor_Port", "S2");
        globals.put("ProximityControlReference_Constant", "50");
        globals.put("ProximitySensor_Type", "NXTUltrasonicSensor");
        globals.put("ProximitySensor_Max", "210");
        globals.put("ProximityInputSmooth_Smoothness", "0.5");
        globals.put("ProximityRawSmooth_Smoothness", "0.95");

        globals.put("ProximityVelocity_NamePrefix", "ProximityVelocityWeight");

        globals.put("ProximityVelocityWeight_InScale", "0.1");
        globals.put("ProximityVelocityWeight_OutScale", "-0.5");
        globals.put("ProximityVelocityWeight_XShift", "-20");
        globals.put("ProximityVelocityWeight_YShift", "0.5");
        globals.put("ProximityVelocityWeight_InputTolerance", "0");
        globals.put("ProximityVelocityWeight_Link", "ProximityControlError");

        globals.put("ProximityVelocityDirectionReference_InScale", "0.1");
        globals.put("ProximityVelocityDirectionReference_OutScale", "100");
        globals.put("ProximityVelocityDirectionReference_XShift", "-30");
        globals.put("ProximityVelocityDirectionReference_YShift", "200");
        globals.put("ProximityVelocityDirectionReference_InputTolerance", "0");
        globals.put("ProximityVelocityDirectionReference_Link", "ProximityInputSmooth");

        globals.put("ProximityPeriodReference_Constant", "500");

        globals.put("RotationWeightConsolidated_Constant", "1");
        globals.put("VelocityTarget_Constant", "500");

        globals.put("B_Acceleration", "1000");
        globals.put("C_Acceleration", "1000");
        globals.put("Consolidated_Weights", "1,1");

        globals.put("DirectionConsolidated_Weights", "1");
        
        globals.put("ProximityInputLimiting_Infinity", "false");

        Utils.saveToXML(xml.run(), file);
        Utils.verify(file);
    }

    public XML_030_004_AvoidanceOffline() {

    }

    public Layers run() throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Basic control of value of sonic reading for obstacle avoidance, "
                + "with flipping of rotation direction");
        List layer = layers.getLayer();

        layer.add(configureLayer0("Interface"));
        layer.add(Utils.emptyLayer("ProximityControl"));
        layer.add(Utils.emptyLayer("Direction"));
        //Modules.moduleDriveMotorsOutput(layers);
        Modules.moduleProximityAvoidanceDoubleTurning(layers, test, false, false);

        return layers;
    }

    private Layer configureLayer0(String name) throws Exception {
        Layer layer = new Layer();
        layer.setName(name);
        List<Controller> controllers = layer.getController();

        Controller env = Generic.configureControllerEnv();
        controllers.add(env);
        controllers.add(configureControllerMotorConsolidation());
        ControlFunction function = Utils.configureControlFunction("RotationWeightConsolidated", "Rotation weight, 1 = no rotation", "Constant", new String[][]{{"Constant", Globals.getInstance().get("RotationWeightConsolidated_Constant"), "Double"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);

        function = Utils.configureControlFunction("VelocityTarget", "Speed target", "Constant",
                new String[][]{{"Constant", Globals.getInstance().get("VelocityTarget_Constant"), "Double"}}, null);
        Utils.addTransferFunction(env, function, Utils.INPUT);

        //controllers.add(Common.configureProximitySensor());
        return layer;
    }

    private Controller configureControllerMotorConsolidation() throws Exception {
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
        ControlFunction output = Utils.configureControlFunction("SpeedConsolidated",
                "Exclusive function of speed input signals", "WeightedMultiply",
                new String[][]{{"Weights", Globals.getInstance().get("Consolidated_Weights"), "String"}},
                new String[][]{{"ProximityVelocityWeight"}, {"VelocityTarget"}});

        outputFunctions.setOutput(output);
        {
            Layers.Layer.Controller.Functions.OutputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.OutputFunctions.Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();
            ControlFunction transfer = Utils.configureControlFunction("DirectionSpeedAdjustmentConsolidated",
                    "Exclusive function of direction input signals", "WeightedSum",
                    new String[][]{{"Weights", Globals.getInstance().get("DirectionConsolidated_Weights"), "String"}},
                    new String[][]{{"ProximityControlOutputWeighted"}});

            transfersList.add(transfer);
            outputFunctions.setTransfers(transfers);
        }
        functions.setOutputFunctions(outputFunctions);

        controller.setFunctions(functions);
        return controller;
    }

}
