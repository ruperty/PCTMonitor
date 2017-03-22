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
package uk.co.moonsit.config.functions;

import uk.co.moonsit.config.functions.Utils;
import java.util.List;
import pct.moons.co.uk.schema.layers.ControlFunction;
import pct.moons.co.uk.schema.layers.Layers;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller;
import uk.co.moonsit.configurations.XML_030_001_AvoidanceProximityTurning;

/**
 *
 * @author Rupert Young
 */
public class Modules {

    static public void moduleAtomOffset(Layers layers, int level, int pos, String referenceLink) throws Exception {

        String prefix = "AtomOffsetControl";

        String tname = "AtomOffsetReference";
        String[][] links = new String[][]{{tname}, {referenceLink, "Key"}};
        ControlFunction reference = Utils.valueActivateFunction(tname + "Active", links);

        Controller atomoffset = Common.configureAtomOffsetControllerSigmoid(prefix, reference);
        Utils.addController(layers, level, atomoffset, pos);

        ControlFunction transfer = Utils.constantFunction(tname);
        Utils.addTransferFunction(atomoffset, transfer, Utils.REFERENCE);

    }

    static public void moduleAtomOffset(Layers layers, int level, int pos) throws Exception {

        String prefix = "AtomOffsetControl";
        ControlFunction reference = Utils.configureControlFunction(prefix + "Reference", "", "Constant",
                new String[][]{{"Constant", Globals.getInstance().get(prefix + "Reference_Constant"), "Double"}},
                null);
        Utils.addController(layers, level, Common.configureAtomOffsetControllerSigmoid(prefix, reference), pos);

    }

    static public void moduleLaserXPosition(Layers layers, int poslevel, int vellevel, int acclevel, String referenceLink, String activeLink, String[][] velocityReferenceLinks) throws Exception {

        String prefix = "LaserXPositionControl";
        Utils.addController(layers, poslevel, Generic.configureSigmoidController(prefix, "LaserX", referenceLink, false));

        {
            prefix = "LaserXVelocityControl";
            String[][] inputLinks = new String[][]{{"LaserXVelocity"}, {"Reset", "Reset"}, {activeLink, "Active"}};
            Controller velocity = Generic.configureProportionalController(prefix, inputLinks, prefix + "ReferenceSum");
            Utils.addController(layers, vellevel, velocity);

            ControlFunction transfer = Utils.configureControlFunction(prefix + "ReferenceSum", "...", "WeightedSum",
                    new String[][]{{"Absolute", "false", ""}, {"Weights", Globals.getInstance().get(prefix + "Reference_Weights"), ""}},
                    velocityReferenceLinks);
            Utils.addTransferFunction(velocity, transfer, Utils.REFERENCE);
        }

        {
            prefix = "LaserXAccelerationControl";
            String[][] inputLinks = new String[][]{{"LaserXAcceleration"}, {"Reset", "Reset"}, {activeLink, "Active"}};
            String[][] outputLinks = new String[][]{{prefix + "Error"}, {"Reset", "Reset"}};
            Utils.addController(layers, acclevel, Generic.configureIntegrationController(prefix, inputLinks, "LaserXVelocityControlOutput", outputLinks));
        }
    }

    static public void moduleLaserXPosition(Layers layers, int poslevel, int vellevel, int acclevel, String referenceLink, String activeLink) throws Exception {

        String prefix = "LaserXPositionControl";
        Utils.addController(layers, poslevel, Generic.configureSigmoidController(prefix, "LaserX", referenceLink, false));

        prefix = "LaserXVelocityControl";
        String[][] inputLinks = new String[][]{{"LaserXVelocity"}, {"Reset", "Reset"}, {activeLink, "Active"}};
        Utils.addController(layers, vellevel, Generic.configureProportionalController(prefix, inputLinks, "LaserXPositionControlOutput"));

        prefix = "LaserXAccelerationControl";
        inputLinks = new String[][]{{"LaserXAcceleration"}, {"Reset", "Reset"}, {activeLink, "Active"}};
        String[][] outputLinks = new String[][]{{prefix + "Error"}, {"Reset", "Reset"}};
        Utils.addController(layers, acclevel, Generic.configureIntegrationController(prefix, inputLinks, "LaserXVelocityControlOutput", outputLinks));

    }

    static public void moduleLaserXPosition(Layers layers, int poslevel, int vellevel, int acclevel, String referenceLink) throws Exception {

        String prefix = "LaserXPositionControl";
        Utils.addController(layers, poslevel, Generic.configureSigmoidController(prefix, "LaserX", referenceLink, false));

        prefix = "LaserXVelocityControl";
        String[][] inputLinks = new String[][]{{"LaserXVelocity"}, {"Reset", "Reset"}};
        Utils.addController(layers, vellevel, Generic.configureProportionalController(prefix, inputLinks, "LaserXPositionControlOutput"));

        prefix = "LaserXAccelerationControl";
        inputLinks = new String[][]{{"LaserXAcceleration"}, {"Reset", "Reset"}};
        String[][] outputLinks = new String[][]{{prefix + "Error"}, {"Reset", "Reset"}};
        Utils.addController(layers, acclevel, Generic.configureIntegrationController(prefix, inputLinks, "LaserXVelocityControlOutput", outputLinks));

    }

    static public int addJointControls(Layers layers, String side, String[] joints, String[] refLinks, String armReach, int type, boolean integration, boolean visionTargets, boolean ir) throws Exception {
        int layer = 0;
        switch (type) {
            case Generic.P:
                Generic.addPSensorActuators(layers, side, joints, refLinks, armReach);
                //Modules.addPControls(layers, side, joints, refLinks);
                layer = 1;
                break;
            case Generic.PV:
                Generic.addPVTSensorsActuators(layers, side, joints, "Velocity");
                layer = Modules.addPVControls(layers, side, joints, refLinks, integration);
                break;
            case Generic.PVA:
                Generic.addPVTSensorsActuators(layers, side, joints, "Acceleration");
                layer = Modules.addPVAControls(layers, side, joints, refLinks);
                break;
        }
        if (visionTargets) {
            Generic.addBaxterVisionSensors(layers, side, ir);
        }
        return layer;
    }

    static public int addPVAControls(Layers layers, String side, String[] joints, String[] refLinks) throws Exception {

        List layer = layers.getLayer();
        int lowerLayer = layer.size();

        layer.add(Utils.emptyLayer("Acceleration"));
        layer.add(Utils.emptyLayer("Velocity"));
        layer.add(Utils.emptyLayer("Position"));
        int i = 0;
        for (String joint : joints) {
            Utils.addController(layers, lowerLayer + 2, Generic.configurePVPositionController(side, joint, side + refLinks[i]));
            String[][] velocityReferenceLinks = new String[][]{{side + joint + "PositionControlOutput"}, {side + joint + "VelocityReference"}};
            Utils.addController(layers, lowerLayer + 1, Generic.configurePVAVelocityController(side, joint, velocityReferenceLinks));
            Utils.addController(layers, lowerLayer, Generic.configurePVAAccelerationController(side, joint, side + joint + "VelocityControlOutput"));
            i++;
        }
        return lowerLayer + 2;
    }

    static public int addPVControls(Layers layers, String side, String[] joints, String[] refLinks, boolean integration) throws Exception {

        List layer = layers.getLayer();
        int lowerLayer = layer.size();

        layer.add(Utils.emptyLayer("Velocity"));
        layer.add(Utils.emptyLayer("Position"));
        int i = 0;
        for (String joint : joints) {
            String[][] velocityReferenceLinks = new String[][]{{side + joint + "PositionControlOutput"}, {side + joint + "VelocityReference"}};
            Utils.addController(layers, lowerLayer, Generic.configurePVVelocityController(side, joint, velocityReferenceLinks, integration));
            Utils.addController(layers, lowerLayer + 1, Generic.configurePVPositionController(side, joint, side + refLinks[i]));
            i++;
        }

        return lowerLayer + 1;
    }

    static public int addPControls(Layers layers, String side, String[] joints, String[] refLinks) throws Exception {

        List layer = layers.getLayer();
        int lowerLayer = layer.size();

        layer.add(Utils.emptyLayer("Position"));
        int i = 0;
        for (String joint : joints) {
            Utils.addController(layers, lowerLayer, Generic.configurePController(side, joint, side + refLinks[i]));
            i++;
        }

        return lowerLayer + 1;
    }

    static public void baxterMemoryList(Layers layers, int layer, String[] systems, String[] leftlinks, String[] rightlinks) throws Exception {
        String[][] referenceLinks = new String[systems.length * 2][];
        for (int i = 0; i < systems.length; i++) {
            referenceLinks[i * 2] = new String[]{"Left" + systems[i] + "MemoryPerception"};
            referenceLinks[i * 2 + 1] = new String[]{"Right" + systems[i] + "MemoryPerception"};
        }

        String side = "Left";
        Controller memoryPerceptionLeft = Generic.configureMemoryPerception(side, systems, leftlinks);

        String gname = Globals.getInstance().get("GoalGeneratorName");
        ControlFunction goalgenerator = Utils.configureControlFunction(gname, "...", "MemoryCounterIncrement",
                new String[][]{
                    {"Directory", Globals.getInstance().get("Data_Directory"), ""},
                    {"Initial", Globals.getInstance().get(gname + "_Initial"), "Double"}
                }, referenceLinks);

        Utils.addTransferFunction(layers, "Env", goalgenerator, Utils.INPUT);

        Utils.addController(layers, layer, memoryPerceptionLeft);
        Utils.addController(layers, layer, Generic.configureMemoryReference(gname, side, systems, "Position"));
        Utils.addController(layers, layer, Generic.configureMemoryReference(gname, side, systems, "Weight"));

        side = "Right";
        Controller memoryPerceptionRight = Generic.configureMemoryPerception(side, systems, rightlinks);
        Utils.addController(layers, layer, memoryPerceptionRight);
        Utils.addController(layers, layer, Generic.configureMemoryReference(gname, side, systems, "Position"));
        Utils.addController(layers, layer, Generic.configureMemoryReference(gname, side, systems, "Weight"));
    }

    static public void armTopHandPitchControls(Layers layers, int layer, String handPitchName, String side) throws Exception {
        ControlFunction reference = Utils.configureControlFunction(side + handPitchName + "Reference", "...", "", null, new String[][]{{handPitchName + "ManualReference"}});
        Utils.addController(layers, layer, Generic.configureHandPitchController(handPitchName, side, reference));
    }

    static public ControlFunction armHandMovementControlsDirect(Layers layers, int layer, String name, String side, String inputLink, ControlFunction positionWeight, ControlFunction positionReference) throws Exception {

        // Position controller
        Controller positionController = Generic.configureMovementPositionControllerDirect(name, side, inputLink, positionReference);
        Utils.addController(layers, layer, positionController);

        // Velocity controller
        /*ControlFunction velocityReference = Utils.configureControlFunction(side + name + "MovementVelocityControlReference", "...", "WeightedMultiply", 
                new String[][]{{"Weights", Globals.getInstance().get(side + name + "MovementVelocityControlReference_Weights"), "Double"}}, 
                new String[][]{{side + name + "MovementPositionControlOutput"}, {side + name + "MovementVelocityWeight"}, {"GlobalSpeedWeight"}});

        Controller velocityController = Common.configureMovementVelocityController(name, side, inputLink, velocityReference);
        Utils.addTransferFunction(velocityController, velocityWeight, Utils.REFERENCE);
        Utils.addController(layers, layer, velocityController);*/
        Utils.addTransferFunction(positionController, positionWeight, Utils.OUTPUT);
        // Reference for hand system
        ControlFunction handSystemReference = Utils.configureControlFunction(side + name + "ControlReference", "...", "Integration",
                new String[][]{
                    {"Gain", Globals.getInstance().get(side + name + "ControlReference_Gain"), "Double"},
                    {"Slow", Globals.getInstance().get(side + name + "ControlReference_Slow"), "Double"},
                    {"Min", Globals.getInstance().get(side + name + "ControlReference_Min"), "Double"},
                    {"Max", Globals.getInstance().get(side + name + "ControlReference_Max"), "Double"},
                    {"Tolerance", Globals.getInstance().get(side + name + "ControlReference_Tolerance"), "Double"},
                    {"Initial", Globals.getInstance().get(side + name + "ControlReference_Initial"), "Double"}},
                new String[][]{{side + name + "MovementPositionControlOutput"}});

        return handSystemReference;
    }

    static public ControlFunction[] armHandMovementControlsDirectNamed(Layers layers, int layer, String name, String side, String inputLink) throws Exception {

        ControlFunction[] functions = new ControlFunction[2];

        if (!name.equals("ShoulderYaw")) {
            // Position controller
            Controller positionController = Generic.configureMovementPositionControllerDirectNamed(name, side, inputLink, side + name + "MemoryPositionReference");
            Utils.addController(layers, layer, positionController);
        }
        String link1;
        String link2;
        if (name.equals("ShoulderYaw")) {
            link1 = side + name + "MemoryPositionReference";
            link2 = side + name + "MemoryWeightReference";
        } else {
            link1 = side + name + "MovementPositionControlOutput";
            link2 = side + name + "MemoryWeightReference";
        }

        ControlFunction positionWeighted = Utils.configureControlFunction(side + name + "MovementPositionWeighted", "...", "WeightedMultiply",
                new String[][]{{"Weights", Globals.getInstance().get(side + name + "MovementPositionControlReference_Weights"), "Double"}},
                new String[][]{{link1}, {link2}, {"GlobalSpeedWeight"}});

        // Reference for hand system
        /*String link;
        if (name.equals("ShoulderYaw")) {
            link = side + name + "MovementPositionWeighted";
        } else {
            link = side + name + "MovementPositionControlOutput";
        }*/
        ControlFunction handSystemReference = Utils.configureControlFunction(side + name + "MovementPositionReference", "...", "Integration",
                new String[][]{
                    {"Gain", Globals.getInstance().get(side + name + "ControlReference_Gain"), "Double"},
                    {"Slow", Globals.getInstance().get(side + name + "ControlReference_Slow"), "Double"},
                    {"Tolerance", Globals.getInstance().get(side + name + "ControlReference_Tolerance"), "Double"},
                    {"Initial", Globals.getInstance().get(side + name + "ControlReference_Initial"), "Double"}},
                new String[][]{{side + name + "MovementPositionWeighted"}});
        functions[0] = handSystemReference;
        functions[1] = positionWeighted;

        return functions;
    }

    static public ControlFunction armHandMovementControls(Layers layers, int layer, String name, String side, String inputLink, ControlFunction velocityWeight, ControlFunction positionReference) throws Exception {

        // Position controller
        Controller positionController = Generic.configureMovementPositionController(name, side, inputLink, positionReference);
        Utils.addController(layers, layer, positionController);

        // Velocity controller
        ControlFunction velocityReference = Utils.configureControlFunction(side + name + "MovementVelocityControlReference", "...", "WeightedMultiply",
                new String[][]{{"Weights", Globals.getInstance().get(side + name + "MovementVelocityControlReference_Weights"), "Double"}},
                new String[][]{{side + name + "MovementPositionControlOutput"}, {side + name + "MovementVelocityWeight"}, {"GlobalSpeedWeight"}});

        Controller velocityController = Generic.configureMovementVelocityController(name, side, inputLink, velocityReference);
        Utils.addTransferFunction(velocityController, velocityWeight, Utils.REFERENCE);
        Utils.addController(layers, layer, velocityController);

        // Reference for hand system
        ControlFunction handSystemReferences = Utils.configureControlFunction(side + name + "ControlReference", "...", "Integration",
                new String[][]{
                    {"Gain", Globals.getInstance().get(side + name + "ControlReference_Gain"), "Double"},
                    {"Slow", Globals.getInstance().get(side + name + "ControlReference_Slow"), "Double"},
                    {"Tolerance", Globals.getInstance().get(side + name + "ControlReference_Tolerance"), "Double"},
                    {"Initial", Globals.getInstance().get(side + name + "ControlReference_Initial"), "Double"}},
                new String[][]{{side + name + "MovementVelocityControlOutput"}});

        return handSystemReferences;
    }

    static public void addHandControlSystems(Layers layers, int layer, String[] handSystemNames,
            ControlFunction[][] references, String side) throws Exception {
        Utils.addController(layers, layer, Generic.configureArmReachHypotenuseController(handSystemNames[0], side, references[0]));
        Utils.addController(layers, layer, Generic.configureHandElevationController(handSystemNames[1], side, references[1]));
        Utils.addController(layers, layer, Generic.configureHandPitchController(handSystemNames[2], side, references[2]));
    }

    static public void addHandControlSystemsMemory(Layers layers, int layer, String[] handSystemNames,
            ControlFunction[] references, String side) throws Exception {
        Utils.addController(layers, layer, Generic.configureArmReachHypotenuseControllerMemory(handSystemNames[0], side, references[0]));
        Utils.addController(layers, layer, Generic.configureHandElevationControllerMemory(handSystemNames[1], side, references[1]));
        Utils.addController(layers, layer, Generic.configureHandPitchControllerMemory(handSystemNames[2], side, references[2]));
        Utils.addController(layers, layer, Generic.configureShoulderYawS0ControllerMemory(handSystemNames[3], side, references[3]));
    }

    static public void addHandControlSystems(Layers layers, int layer, String[] handSystemNames,
            ControlFunction[] references, String side) throws Exception {
        Utils.addController(layers, layer, Generic.configureArmReachHypotenuseController(handSystemNames[0], side, references[0]));
        Utils.addController(layers, layer, Generic.configureHandElevationController(handSystemNames[1], side, references[1]));
        Utils.addController(layers, layer, Generic.configureHandPitchController(handSystemNames[2], side, references[2]));
    }

    static public void armRelativeHandControls(Layers layers, int layer, String[] systems, String side, String[] inputLinks, String[] referenceLinks, boolean integration) throws Exception {

//ControlFunction reference = Utils.configureControlFunction(side + armReachName + "Reference", "...", "", null, new String[][]{{armReachName + "ManualReference"}});
        //Utils.addController(layers, layer, Common.configureArmReachHypotenuseController(armReachName, side, reference));
        //reference = Utils.configureControlFunction(side + handElevationName + "Reference", "...", "", null, new String[][]{{handElevationName + "ManualReference"}});
        //Utils.addController(layers, layer, Common.configureHandElevationController(handElevationName, side, reference));
        if (Utils.getLayer(layers, side + "HandControls") == null) {
            layers.getLayer().add(Utils.emptyLayer(side + "HandControls"));
            layer = Utils.getLayerNum(layers, side + "HandControls");
        }
        int i = 0;
        Utils.addController(layers, layer, Generic.configureRelativeHandController(side, systems[i], inputLinks[i], referenceLinks[i++]));

        Utils.addController(layers, layer, Generic.configureRelativeHandController(side, systems[i], inputLinks[i], referenceLinks[i++]));

        Utils.addController(layers, layer, Generic.configureArmReachRelativeController(side, systems[i], inputLinks[i], referenceLinks[i++], integration));

        i = getSystemIndex(systems, "ShoulderX");
        if (i >= 0) {
            Utils.addController(layers, layer, Generic.configureRelativeHandController(side, systems[i], inputLinks[i], referenceLinks[i]));
        }

        i = getSystemIndex(systems, "HandPitchY");
        if (i >= 0) {
            Utils.addController(layers, layer, Generic.configureRelativeHandController(side, systems[i], inputLinks[i], referenceLinks[i]));
        }

        i = getSystemIndex(systems, "IRDepthZ");
        if (i >= 0) {
            Utils.addController(layers, layer, Generic.configureArmReachRelativeController(side, systems[i], inputLinks[i], referenceLinks[i], integration));
        }

    }

    private static int getSystemIndex(String[] systems, String system) {
        for (int i = 0; i < systems.length; i++) {
            if (systems[i].equals(system)) {
                return i;
            }
        }
        return -1;
    }

    static public void armStaticHandControls(Layers layers, int layer, String armReachName, String handElevationName, String handPitchName, String side) throws Exception {
        ControlFunction reference = Utils.configureControlFunction(side + armReachName + "Reference", "...", "", null, new String[][]{{armReachName + "ManualReference"}});
        Utils.addController(layers, layer, Generic.configureArmReachHypotenuseController(armReachName, side, reference));

        reference = Utils.configureControlFunction(side + handElevationName + "Reference", "...", "", null, new String[][]{{handElevationName + "ManualReference"}});
        Utils.addController(layers, layer, Generic.configureHandElevationController(handElevationName, side, reference));

        reference = Utils.configureControlFunction(side + handPitchName + "Reference", "...", "", null, new String[][]{{handPitchName + "ManualReference"}});
        Utils.addController(layers, layer, Generic.configureHandPitchController(handPitchName, side, reference));
    }

    static public void armJointPositionVelocityController(Layers layers, int layer, String name, String side, String joint, String publish, String tolerance, String initial, String last, String[][] referenceLinks, boolean pubPosition) throws Exception {
        Utils.addController(layers, layer, Generic.configureArmPositionVelocityController(name, side, joint, publish, tolerance, initial, last, referenceLinks, pubPosition));
    }

    static public void armJointPositionController(Layers layers, int layer, String name, String side, String joint, String[][] referenceLinks) throws Exception {
        Utils.addController(layers, layer + 1, Generic.configureArmPositionController(name, side, joint, referenceLinks));
    }

    static public void armJointPositionWithVelocityController(Layers layers, int layer, String name, String side, String joint, String[][] referenceLinks) throws Exception {
        Utils.addController(layers, layer, Generic.configureArmVelocityController(name, side, joint));
        Utils.addController(layers, layer + 1, Generic.configureArmPositionWithVelocityController(name, side, joint, referenceLinks));
    }

    static public void moduleBearingMemoryConflict(Layers layers, int layer) throws Exception {

        Utils.addController(layers, layer, Generic.configureControllerBearingMemoryConflict(), 1);

    }

    static public void moduleAvoidanceProximityTurning(Layers layers) throws Exception {

        Utils.addController(layers, 0, Generic.configureSensorProximity(), 1);

        ControlFunction function = Utils.configureControlFunction("ObjectVelocityReference", "Velocity reference", "Constant", new String[][]{{"Constant", Globals.getInstance().get("ObjectVelocityReference_Constant"), "Double"}}, null);
        Utils.addTransferFunction(layers, 0, "Env", function, Utils.INPUT);

        Utils.addController(layers, 1, "SonicControl", XML_030_001_AvoidanceProximityTurning.configureControllerProximityControl(), 1);

    }

    static public void moduleDriveMotorsOutput(Layers layers, boolean test) throws Exception {

        Utils.addController(layers, 0, Generic.configureControllerDriveMotorsOutput(test), 1);

    }

    static public void moduleMotorsMovingSensors(Layers layers) throws Exception {

        Utils.addController(layers, 0, Generic.configureControllerMotorsMovingSensors(), 1);

    }

    static public void moduleXBeeSystems(Layers layers) throws Exception {

        Utils.addController(layers, 0, Generic.configureSensorXBee(false), 1);
        Utils.addController(layers, 2, Generic.configureControllerXBeeSignalControl(), 1);
        Utils.addController(layers, 2, Generic.configureControllerXBeeChangeControl(), 1);

    }

    static public void moduleCompassSystems(Layers layers, boolean test, boolean sensors, Layers.Layer.Controller.Functions.ReferenceFunctions compassBearingReference) throws Exception {
        if (sensors) {
            Utils.addController(layers, 0, Generic.configureSensorCompass(test), 1);
        }
        Utils.addController(layers, 1, Generic.configureControllerCompassBearingControl(compassBearingReference), 1);

    }

    static public void modulePhoneAccelerometerSystems(Layers layers, boolean test, int level, boolean constant) throws Exception {

        Utils.addController(layers, 0, Generic.getPhoneAccelerometerSensor(test), 1);
        Utils.addController(layers, level, Generic.configureControllerPhoneAccelerometerSpeedControl(), 1);
        Utils.addController(layers, level, Generic.configureControllerPhoneAccelerometerDirectionControl(Generic.getAccDirReference(constant)), 1);
    }

    static public void moduleShakingControlSystems(Layers layers, boolean test, int level, boolean smooth) throws Exception {
        Utils.addController(layers, 0, Generic.getAccelerometerSensor(test, smooth), 1);
        Utils.addController(layers, level, Generic.configureControllerShakingControl(), 1);
    }

    static public void moduleCombinedSensors(Layers layers, String[] sensors, boolean test) throws Exception {

        Utils.addController(layers, 0, Generic.configureSensors(test, sensors), 1);

    }

    static public void moduleLocationSystems(Layers layers, boolean test, boolean sensors, int lowerLevel) throws Exception {

        Utils.addController(layers, lowerLevel, Generic.configureControllerLocationDistanceControl(), 1);
        //Utils.addController(layers, lowerLevel, Common.configureControllerLocationBearingControl(), 1);

        Controller lat = Generic.configureControllerLatitudeControl();
        Utils.addTransferFunction(lat, Generic.getLatitudeReference(), Utils.REFERENCE);
        Utils.addController(layers, lowerLevel + 1, lat, 1);

        Controller longi = Generic.configureControllerLongitudeControl();
        Utils.addTransferFunction(longi, Generic.getLongitudeReference(), Utils.REFERENCE);

        Utils.addController(layers, lowerLevel + 1, longi, 1);
        if (!test) {
            if (sensors) {
                moduleLocationSensors(layers);
            }
        }

    }

    static public void moduleProximityAvoidanceDoubleTurning(Layers layers, boolean test, boolean sensors, boolean errorSwitch) throws Exception {
        Utils.addController(layers, 1, Generic.configureControllerProximityControl(errorSwitch), 1);
        if (test) {
            Utils.addController(layers, 0, Generic.configureProximitySensorTest(), 1);
        } else if (sensors) {
            Utils.addController(layers, 0, Generic.configureSensorProximity(), 1);
        }
        Utils.addController(layers, 2, Generic.configureControllerProximityPeriod(), 1);
    }

    static public void moduleLocationSensors(Layers layers) throws Exception {
        Utils.addController(layers, 0, Generic.configureSensorLatitude(), 1);
        Utils.addController(layers, 0, Generic.configureSensorLongitude(), 1);
    }

    static public void moduleControlReorganisationSimulationProp(Layers layers) throws Exception {
        Utils.addController(layers, 0, Generic.configureControlReorganisationSimulationProp(), 1);
    }

    static public void moduleControlReorganisationSimulationInt(Layers layers) throws Exception {
        Utils.addController(layers, 0, Generic.configureControlReorganisationSimulationInt(), 1);
    }

}
