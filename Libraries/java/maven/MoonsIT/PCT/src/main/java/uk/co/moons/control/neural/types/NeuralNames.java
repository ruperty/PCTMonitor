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
package uk.co.moons.control.neural.types;

/**
 * 
 * @author Rupert Young Moon's Information Technology
 * 
 */
public class NeuralNames {

    public final static int USensorRead = 0;
    public final static int USensorMedian = 1;
    public final static int LightSensorRead = 2;
    public final static int SensorChange = 3;
    public final static int SensorChangeDelay = 4;
    public final static int FilteredChangeDelayed = 5;
    public final static int FilteredChange = 6;
    public final static int SensorSmooth = 7;
    public final static int MotorBInput = 8;
    public final static int MotorBDirection = 9;
    public final static int MotorCInput = 10;
    public final static int MotorCDirection = 11;
    public final static int PursuitSpeedWeightB = 12;
    public final static int SpeedReferenceB = 13;
    public final static int MotorBOutput = 14;
    public final static int PursuitSpeedWeightC = 15;
    public final static int SpeedReferenceC = 16;
    public final static int MotorCOutput = 17;
    public final static int TransitionToNegativeChange = 18;
    public final static int TransitionToPositiveChange = 19;
    public final static int NegativeTransitionControlInput = 20;
    public final static int NegativeTransitionControlReference = 21;
    public final static int NegativeTransitionControlError = 22;
    public final static int NegativeTransitionControlOutput = 23;
    public final static int PositiveTransitionControlInput = 24;
    public final static int PositiveTransitionControlReference = 25;
    public final static int PositiveTransitionControlError = 26;
    public final static int PositiveTransitionControlOutput = 27;
    public final static int PursuitDirectionFlip = 28;
    public final static int PursuitDirectionFeed = 29;
    public final static int PursuitDirectionWeight = 30;
    public final static int ReverseWeight = 31;
    public final static int MotorRotationValue = 32;
    public final static int MotorRotationValueWeight = 33;
    public final static int MotorInput = 34;
    public final static int RotationControlInput = 35;
    public final static int RotationControlReference = 36;
    public final static int RotationControlError = 37;
    public final static int RotationControlOutput = 38;
    public final static int FullRotationWeightsBOutput = 39;
    public final static int FullRotationWeightsCSign = 40;
    public final static int FullRotationWeightsCOutput = 41;
    public final static int LocationRotationSensorControlInput = 42;
    public final static int LocationRotationReferenceWeighted = 43;
    public final static int LocationRotationReferenceGreaterThan = 44;
    public final static int LocationRotationSensorControlReference = 45;
    public final static int LocationRotationSensorControlError = 46;
    public final static int LocationRotationSensorControlOutput = 47;
    public final static int PartialSequenceControlActiveReference = 48;
    public final static int PartialSequenceControlActive = 49;
    public final static int PartialSequenceControlActiveOpposite = 50;
    public final static int LocationRotationWeightBSign = 51;
    public final static int LocationRotationWeightsBOutput = 52;
    public final static int LocationRotationWeightsCOutput = 53;
    public final static int PursuitControlInput = 54;
    public final static int PursuitReferenceGreaterThan = 55;
    public final static int PursuitReferenceWeighted = 56;
    public final static int PursuitControlReference = 57;
    public final static int PursuitControlError = 58;
    public final static int PursuitControlOutput = 59;
    public final static int RotationControlLimit = 60;
    public final static int SequenceControlInput = 61;
    public final static int FullRotationReference = 62;
    public final static int FullRotationReferenceWeighted = 63;
    public final static int LocationRotationReferenceDiff = 64;
    public final static int PursuitReferenceDiff = 65;
    public final static int SequenceControlReference = 66;
    public final static int SequenceControlError = 67;
    public final static int SequenceControlOutput = 68;
    public final static int SonicSmooth = 69;
    public final static int SonicReference = 70;
    public final static int SonicError = 71;
    public final static int SonicOutput = 72;
    public final static int SonicControlSpeed = 73;
    public final static int SequenceControlSpeed = 74;
    public final static int SpeedReference = 75;
    public final static int MotorBChange = 76;
    public final static int MotorCChange = 77;
    public final static int SonicChange = 78;
    public final static int SensorSumChanges = 79;
    public final static int SmoothSensorSumChange = 80;
    public final static int SensorSumChangeReference = 81;
    public final static int SensorSumChangeError = 82;
    public final static int SensorSumChangeOutput = 83;
    public final static int SensorSumChangeHabituate = 84;
    public final static int SensorSumChangeReverseWeight = 85;
    public final static int SensorSumChangeRotationWeight = 86;
    public final static int LongMax = 87;
    public final static int ShortMax = 88;
    public final static int SonicErrorLimit = 89;
    public final static int SSWeight = 90;
    public final static int SequenceSwitchInput = 91;
    public final static int SequenceSwitchReference = 92;
    public final static int SequenceSwitchError = 93;
    public final static int SequenceSwitchOutput = 94;
    public final static int ControlSwitch = 95;
    public final static int PartialActiveChange = 96;
    public final static int PartialActiveChangeLimit = 97;
    public final static int SonicErrorSmooth = 98;
    public final static int SonicErrorSum = 99;
    public final static int SonicErrorSumLimit = 100;
    public final static int SCWeight = 101;
    public final static int SpaceControlInput = 102;
    public final static int SpaceControlReference = 103;
    public final static int SpaceControlError = 104;
    public final static int SpaceControlOutput = 105;
    public final static int SpeedConstant = 106;
    public final static int Half = 107;
    public final static int Zero = 108;
    public final static int Sleep = 109;
    public final static int Rate = 110;
    public final static int GyroSensorRead = 111;
    public final static int BobPositionInput = 112;
    public final static int SensorChangeAbsolute = 113;
    public final static int MotorBTachoCount = 114;
    public final static int RotateReferenceB = 115;
    public final static int ErrorB = 116;
    public final static int MotorBOutputProp = 117;
    public final static int MotorBRotate = 118;
    public final static int MotorCTachoCount = 119;
    public final static int RotateReferenceC = 120;
    public final static int ErrorC = 121;
    public final static int MotorCOutputProp = 122;
    public final static int MotorCRotate = 123;
//public final static int        =115;
//public final static int        =115;

    public static String getNeuralName(int n) throws Exception {
        String name = null;

        switch (n) {
            case 0:
                name = "USensorRead";
                break;
            case 1:
                name = "USensorMedian";
                break;
            case 2:
                name = "LightSensorRead";
                break;
            case 3:
                name = "SensorChange";
                break;
            case 4:
                name = "SensorChangeDelay";
                break;
            case 5:
                name = "FilteredChangeDelayed";
                break;
            case 6:
                name = "FilteredChange";
                break;
            case 7:
                name = "SensorSmooth";
                break;
            case 8:
                name = "MotorBInput";
                break;
            case 9:
                name = "MotorBDirection";
                break;
            case 10:
                name = "MotorCInput";
                break;
            case 11:
                name = "MotorCDirection";
                break;
            case 12:
                name = "PursuitSpeedWeightB";
                break;
            case 13:
                name = "SpeedReferenceB";
                break;
            case 14:
                name = "MotorBOutput";
                break;
            case 15:
                name = "PursuitSpeedWeightC";
                break;
            case 16:
                name = "SpeedReferenceC";
                break;
            case 17:
                name = "MotorCOutput";
                break;
            case 18:
                name = "TransitionToNegativeChange";
                break;
            case 19:
                name = "TransitionToPositiveChange";
                break;
            case 20:
                name = "NegativeTransitionControlInput";
                break;
            case 21:
                name = "NegativeTransitionControlReference";
                break;
            case 22:
                name = "NegativeTransitionControlError";
                break;
            case 23:
                name = "NegativeTransitionControlOutput";
                break;
            case 24:
                name = "PositiveTransitionControlInput";
                break;
            case 25:
                name = "PositiveTransitionControlReference";
                break;
            case 26:
                name = "PositiveTransitionControlError";
                break;
            case 27:
                name = "PositiveTransitionControlOutput";
                break;
            case 28:
                name = "PursuitDirectionFlip";
                break;
            case 29:
                name = "PursuitDirectionFeed";
                break;
            case 30:
                name = "PursuitDirectionWeight";
                break;
            case 31:
                name = "ReverseWeight";
                break;
            case 32:
                name = "MotorRotationValue";
                break;
            case 33:
                name = "MotorRotationValueWeight";
                break;
            case 34:
                name = "MotorInput";
                break;
            case 35:
                name = "RotationControlInput";
                break;
            case 36:
                name = "RotationControlReference";
                break;
            case 37:
                name = "RotationControlError";
                break;
            case 38:
                name = "RotationControlOutput";
                break;
            case 39:
                name = "FullRotationWeightsBOutput";
                break;
            case 40:
                name = "FullRotationWeightsCSign";
                break;
            case 41:
                name = "FullRotationWeightsCOutput";
                break;
            case 42:
                name = "LocationRotationSensorControlInput";
                break;
            case 43:
                name = "LocationRotationReferenceWeighted";
                break;
            case 44:
                name = "LocationRotationReferenceGreaterThan";
                break;
            case 45:
                name = "LocationRotationSensorControlReference";
                break;
            case 46:
                name = "LocationRotationSensorControlError";
                break;
            case 47:
                name = "LocationRotationSensorControlOutput";
                break;
            case 48:
                name = "PartialSequenceControlActiveReference";
                break;
            case 49:
                name = "PartialSequenceControlActive";
                break;
            case 50:
                name = "PartialSequenceControlActiveOpposite";
                break;
            case 51:
                name = "LocationRotationWeightBSign";
                break;
            case 52:
                name = "LocationRotationWeightsBOutput";
                break;
            case 53:
                name = "LocationRotationWeightsCOutput";
                break;
            case 54:
                name = "PursuitControlInput";
                break;
            case 55:
                name = "PursuitReferenceGreaterThan";
                break;
            case 56:
                name = "PursuitReferenceWeighted";
                break;
            case 57:
                name = "PursuitControlReference";
                break;
            case 58:
                name = "PursuitControlError";
                break;
            case 59:
                name = "PursuitControlOutput";
                break;
            case 60:
                name = "RotationControlLimit";
                break;
            case 61:
                name = "SequenceControlInput";
                break;
            case 62:
                name = "FullRotationReference";
                break;
            case 63:
                name = "FullRotationReferenceWeighted";
                break;
            case 64:
                name = "LocationRotationReferenceDiff";
                break;
            case 65:
                name = "PursuitReferenceDiff";
                break;
            case 66:
                name = "SequenceControlReference";
                break;
            case 67:
                name = "SequenceControlError";
                break;
            case 68:
                name = "SequenceControlOutput";
                break;
            case 69:
                name = "SonicSmooth";
                break;
            case 70:
                name = "SonicReference";
                break;
            case 71:
                name = "SonicError";
                break;
            case 72:
                name = "SonicOutput";
                break;
            case 73:
                name = "SonicControlSpeed";
                break;
            case 74:
                name = "SequenceControlSpeed";
                break;
            case 75:
                name = "SpeedReference";
                break;
            case 76:
                name = "MotorBChange";
                break;
            case 77:
                name = "MotorCChange";
                break;
            case 78:
                name = "SonicChange";
                break;
            case 79:
                name = "SensorSumChanges";
                break;
            case 80:
                name = "SmoothSensorSumChange";
                break;
            case 81:
                name = "SensorSumChangeReference";
                break;
            case 82:
                name = "SensorSumChangeError";
                break;
            case 83:
                name = "SensorSumChangeOutput";
                break;
            case 84:
                name = "SensorSumChangeHabituate";
                break;
            case 85:
                name = "SensorSumChangeReverseWeight";
                break;
            case 86:
                name = "SensorSumChangeRotationWeight";
                break;
            case 87:
                name = "LongMax";
                break;
            case 88:
                name = "ShortMax";
                break;
            case 89:
                name = "SonicErrorLimit";
                break;
            case 90:
                name = "SSWeight";
                break;
            case 91:
                name = "SequenceSwitchInput";
                break;
            case 92:
                name = "SequenceSwitchReference";
                break;
            case 93:
                name = "SequenceSwitchError";
                break;
            case 94:
                name = "SequenceSwitchOutput";
                break;
            case 95:
                name = "ControlSwitch";
                break;
            case 96:
                name = "PartialActiveChange";
                break;
            case 97:
                name = "PartialActiveChangeLimit";
                break;
            case 98:
                name = "SonicErrorSmooth";
                break;
            case 99:
                name = "SonicErrorSum";
                break;
            case 100:
                name = "SonicErrorSumLimit";
                break;
            case 101:
                name = "SCWeight";
                break;
            case 102:
                name = "SpaceControlInput";
                break;
            case 103:
                name = "SpaceControlReference";
                break;
            case 104:
                name = "SpaceControlError";
                break;
            case 105:
                name = "SpaceControlOutput";
                break;
            case 106:
                name = "SpeedConstant";
                break;
            case 107:
                name = "Half";
                break;
            case 108:
                name = "Zero";
                break;
            case 113:
                name = "SensorChangeAbsolute";
                break;

            case 114:
                name = "MotorBTachoCount";
                break;
            case 115:
                name = "RotateReferenceB";
                break;
            case 116:
                name = "ErrorB";
                break;
            case 117:
                name = "MotorBOutputProp";
                break;
            case 118:
                name = "MotorBRotate";
                break;
            case 119:
                name = "MotorCTachoCount";
                break;
            case 120:
                name = "RotateReferenceC";
                break;
            case 121:
                name = "ErrorC";
                break;
            case 122:
                name = "MotorCOutputProp";
                break;
            case 123:
                name = "MotorCRotate";
                break;


        }

        if (name == null) {
            String st = "NN-name " + n;
            throw new Exception(st);

        }

        return name;

    }

    public static int getNeuralNameInt(String name) throws Exception {
        int n = -1;

        if (name.equals("USensorRead")) {
            n = USensorRead;
            return n;
        }
        ;
        if (name.equals("USensorMedian")) {
            n = USensorMedian;
            return n;
        }
        if (name.equals("LightSensorRead")) {
            n = LightSensorRead;
            return n;
        }
        if (name.equals("SensorChange")) {
            n = SensorChange;
            return n;
        }
        if (name.equals("SensorChangeDelay")) {
            n = SensorChangeDelay;
            return n;
        }
        if (name.equals("FilteredChangeDelayed")) {
            n = FilteredChangeDelayed;
            return n;
        }
        if (name.equals("FilteredChange")) {
            n = FilteredChange;
            return n;
        }
        if (name.equals("SensorSmooth")) {
            n = SensorSmooth;
            return n;
        }
        if (name.equals("MotorBInput")) {
            n = MotorBInput;
            return n;
        }
        if (name.equals("MotorBDirection")) {
            n = MotorBDirection;
            return n;
        }
        if (name.equals("MotorCInput")) {
            n = MotorCInput;
            return n;
        }
        if (name.equals("MotorCDirection")) {
            n = MotorCDirection;
            return n;
        }
        if (name.equals("PursuitSpeedWeightB")) {
            n = PursuitSpeedWeightB;
            return n;
        }
        if (name.equals("SpeedReferenceB")) {
            n = SpeedReferenceB;
            return n;
        }
        if (name.equals("MotorBOutput")) {
            n = MotorBOutput;
            return n;
        }
        if (name.equals("PursuitSpeedWeightC")) {
            n = PursuitSpeedWeightC;
            return n;
        }
        if (name.equals("SpeedReferenceC")) {
            n = SpeedReferenceC;
            return n;
        }
        if (name.equals("MotorCOutput")) {
            n = MotorCOutput;
            return n;
        }
        if (name.equals("TransitionToNegativeChange")) {
            n = TransitionToNegativeChange;
            return n;
        }
        if (name.equals("TransitionToPositiveChange")) {
            n = TransitionToPositiveChange;
            return n;
        }
        if (name.equals("NegativeTransitionControlInput")) {
            n = NegativeTransitionControlInput;
            return n;
        }
        if (name.equals("NegativeTransitionControlReference")) {
            n = NegativeTransitionControlReference;
            return n;
        }
        if (name.equals("NegativeTransitionControlError")) {
            n = NegativeTransitionControlError;
            return n;
        }
        if (name.equals("NegativeTransitionControlOutput")) {
            n = NegativeTransitionControlOutput;
            return n;
        }
        if (name.equals("PositiveTransitionControlInput")) {
            n = PositiveTransitionControlInput;
            return n;
        }
        if (name.equals("PositiveTransitionControlReference")) {
            n = PositiveTransitionControlReference;
            return n;
        }
        if (name.equals("PositiveTransitionControlError")) {
            n = PositiveTransitionControlError;
            return n;
        }
        if (name.equals("PositiveTransitionControlOutput")) {
            n = PositiveTransitionControlOutput;
            return n;
        }
        if (name.equals("PursuitDirectionFlip")) {
            n = PursuitDirectionFlip;
            return n;
        }
        if (name.equals("PursuitDirectionFeed")) {
            n = PursuitDirectionFeed;
            return n;
        }
        if (name.equals("PursuitDirectionWeight")) {
            n = PursuitDirectionWeight;
            return n;
        }
        if (name.equals("ReverseWeight")) {
            n = ReverseWeight;
            return n;
        }
        if (name.equals("MotorRotationValue")) {
            n = MotorRotationValue;
            return n;
        }
        if (name.equals("MotorRotationValueWeight")) {
            n = MotorRotationValueWeight;
            return n;
        }
        if (name.equals("MotorInput")) {
            n = MotorInput;
            return n;
        }
        if (name.equals("RotationControlInput")) {
            n = RotationControlInput;
            return n;
        }
        if (name.equals("RotationControlReference")) {
            n = RotationControlReference;
            return n;
        }
        if (name.equals("RotationControlError")) {
            n = RotationControlError;
            return n;
        }
        if (name.equals("RotationControlOutput")) {
            n = RotationControlOutput;
            return n;
        }
        if (name.equals("FullRotationWeightsBOutput")) {
            n = FullRotationWeightsBOutput;
            return n;
        }
        if (name.equals("FullRotationWeightsCSign")) {
            n = FullRotationWeightsCSign;
            return n;
        }
        if (name.equals("FullRotationWeightsCOutput")) {
            n = FullRotationWeightsCOutput;
            return n;
        }
        if (name.equals("LocationRotationSensorControlInput")) {
            n = LocationRotationSensorControlInput;
            return n;
        }
        if (name.equals("LocationRotationReferenceWeighted")) {
            n = LocationRotationReferenceWeighted;
            return n;
        }
        if (name.equals("LocationRotationReferenceGreaterThan")) {
            n = LocationRotationReferenceGreaterThan;
            return n;
        }
        if (name.equals("LocationRotationSensorControlReference")) {
            n = LocationRotationSensorControlReference;
            return n;
        }
        if (name.equals("LocationRotationSensorControlError")) {
            n = LocationRotationSensorControlError;
            return n;
        }
        if (name.equals("LocationRotationSensorControlOutput")) {
            n = LocationRotationSensorControlOutput;
            return n;
        }
        if (name.equals("PartialSequenceControlActiveReference")) {
            n = PartialSequenceControlActiveReference;
            return n;
        }
        if (name.equals("PartialSequenceControlActive")) {
            n = PartialSequenceControlActive;
            return n;
        }
        if (name.equals("PartialSequenceControlActiveOpposite")) {
            n = PartialSequenceControlActiveOpposite;
            return n;
        }
        if (name.equals("LocationRotationWeightBSign")) {
            n = LocationRotationWeightBSign;
            return n;
        }
        if (name.equals("LocationRotationWeightsBOutput")) {
            n = LocationRotationWeightsBOutput;
            return n;
        }
        if (name.equals("LocationRotationWeightsCOutput")) {
            n = LocationRotationWeightsCOutput;
            return n;
        }
        if (name.equals("PursuitControlInput")) {
            n = PursuitControlInput;
            return n;
        }
        if (name.equals("PursuitReferenceGreaterThan")) {
            n = PursuitReferenceGreaterThan;
            return n;
        }
        if (name.equals("PursuitReferenceWeighted")) {
            n = PursuitReferenceWeighted;
            return n;
        }
        if (name.equals("PursuitControlReference")) {
            n = PursuitControlReference;
            return n;
        }
        if (name.equals("PursuitControlError")) {
            n = PursuitControlError;
            return n;
        }
        if (name.equals("PursuitControlOutput")) {
            n = PursuitControlOutput;
            return n;
        }
        if (name.equals("RotationControlLimit")) {
            n = RotationControlLimit;
            return n;
        }
        if (name.equals("SequenceControlInput")) {
            n = SequenceControlInput;
            return n;
        }
        if (name.equals("FullRotationReference")) {
            n = FullRotationReference;
            return n;
        }
        if (name.equals("FullRotationReferenceWeighted")) {
            n = FullRotationReferenceWeighted;
            return n;
        }
        if (name.equals("LocationRotationReferenceDiff")) {
            n = LocationRotationReferenceDiff;
            return n;
        }
        if (name.equals("PursuitReferenceDiff")) {
            n = PursuitReferenceDiff;
            return n;
        }
        if (name.equals("SequenceControlReference")) {
            n = SequenceControlReference;
            return n;
        }
        if (name.equals("SequenceControlError")) {
            n = SequenceControlError;
            return n;
        }
        if (name.equals("SequenceControlOutput")) {
            n = SequenceControlOutput;
            return n;
        }
        if (name.equals("SonicSmooth")) {
            n = SonicSmooth;
            return n;
        }
        if (name.equals("SonicReference")) {
            n = SonicReference;
            return n;
        }
        if (name.equals("SonicError")) {
            n = SonicError;
            return n;
        }
        if (name.equals("SonicOutput")) {
            n = SonicOutput;
            return n;
        }
        if (name.equals("SonicControlSpeed")) {
            n = SonicControlSpeed;
            return n;
        }
        if (name.equals("SequenceControlSpeed")) {
            n = SequenceControlSpeed;
            return n;
        }
        if (name.equals("SpeedReference")) {
            n = SpeedReference;
            return n;
        }
        if (name.equals("MotorBChange")) {
            n = MotorBChange;
            return n;
        }
        if (name.equals("MotorCChange")) {
            n = MotorCChange;
            return n;
        }
        if (name.equals("SonicChange")) {
            n = SonicChange;
            return n;
        }
        if (name.equals("SensorSumChanges")) {
            n = SensorSumChanges;
            return n;
        }
        if (name.equals("SmoothSensorSumChange")) {
            n = SmoothSensorSumChange;
            return n;
        }
        if (name.equals("SensorSumChangeReference")) {
            n = SensorSumChangeReference;
            return n;
        }
        if (name.equals("SensorSumChangeError")) {
            n = SensorSumChangeError;
            return n;
        }
        if (name.equals("SensorSumChangeOutput")) {
            n = SensorSumChangeOutput;
            return n;
        }
        if (name.equals("SensorSumChangeHabituate")) {
            n = SensorSumChangeHabituate;
            return n;
        }
        if (name.equals("SensorSumChangeReverseWeight")) {
            n = SensorSumChangeReverseWeight;
            return n;
        }
        if (name.equals("SensorSumChangeRotationWeight")) {
            n = SensorSumChangeRotationWeight;
            return n;
        }
        if (name.equals("LongMax")) {
            n = LongMax;
            return n;
        }
        if (name.equals("ShortMax")) {
            n = ShortMax;
            return n;
        }
        if (name.equals("SonicErrorLimit")) {
            n = SonicErrorLimit;
            return n;
        }
        if (name.equals("SSWeight")) {
            n = SSWeight;
            return n;
        }
        if (name.equals("SequenceSwitchInput")) {
            n = SequenceSwitchInput;
            return n;
        }
        if (name.equals("SequenceSwitchReference")) {
            n = SequenceSwitchReference;
            return n;
        }
        if (name.equals("SequenceSwitchError")) {
            n = SequenceSwitchError;
            return n;
        }
        if (name.equals("SequenceSwitchOutput")) {
            n = SequenceSwitchOutput;
            return n;
        }
        if (name.equals("ControlSwitch")) {
            n = ControlSwitch;
            return n;
        }
        if (name.equals("PartialActiveChange")) {
            n = PartialActiveChange;
            return n;
        }
        if (name.equals("PartialActiveChangeLimit")) {
            n = PartialActiveChangeLimit;
            return n;
        }
        if (name.equals("SonicErrorSmooth")) {
            n = SonicErrorSmooth;
            return n;
        }
        if (name.equals("SonicErrorSum")) {
            n = SonicErrorSum;
            return n;
        }
        if (name.equals("SonicErrorSumLimit")) {
            n = SonicErrorSumLimit;
            return n;
        }
        if (name.equals("SCWeight")) {
            n = SCWeight;
            return n;
        }
        if (name.equals("SpaceControlInput")) {
            n = SpaceControlInput;
            return n;
        }
        if (name.equals("SpaceControlReference")) {
            n = SpaceControlReference;
            return n;
        }
        if (name.equals("SpaceControlError")) {
            n = SpaceControlError;
            return n;
        }
        if (name.equals("SpaceControlOutput")) {
            n = SpaceControlOutput;
            return n;
        }
        if (name.equals("SpeedConstant")) {
            n = SpeedConstant;
            return n;
        }
        if (name.equals("Half")) {
            n = Half;
            return n;
        }
        if (name.equals("Zero")) {
            n = Zero;
            return n;
        }

        if (name.equals("Sleep")) {
            n = Sleep;
            return n;
        }
        if (name.equals("Rate")) {
            n = Rate;
            return n;
        }
        if (name.equals("GyroSensorRead")) {
            n = GyroSensorRead;
            return n;
        }

        if (name.equals("BobPositionInput")) {
            n = BobPositionInput;
            return n;
        }

        if (name.equals("SensorChangeAbsolute")) {
            n = SensorChangeAbsolute;
            return n;
        }
        if (name.equals("MotorBTachoCount")) {
            n = MotorBTachoCount;
            return n;
        }

        if (name.equals("RotateReferenceB")) {
            n = RotateReferenceB;
            return n;
        }
        if (name.equals("ErrorB")) {
            n = ErrorB;
            return n;
        }

        if (name.equals("MotorBOutputProp")) {
            n = MotorBOutputProp;
            return n;
        }
        if (name.equals("MotorBRotate")) {
            n = MotorBRotate;
            return n;
        }

        if (name.equals("MotorCTachoCount")) {
            n = MotorCTachoCount;
            return n;
        }

        if (name.equals("RotateReferenceC")) {
            n = RotateReferenceC;
            return n;
        }
        if (name.equals("ErrorC")) {
            n = ErrorC;
            return n;
        }

        if (name.equals("MotorCOutputProp")) {
            n = MotorCOutputProp;
            return n;
        }
        if (name.equals("MotorCRotate")) {
            n = MotorCRotate;
            return n;
        }

        if (n < 0) {
            String st = "NN-code " + name;
            throw new Exception(st);
        }
        return n;
    }
}
