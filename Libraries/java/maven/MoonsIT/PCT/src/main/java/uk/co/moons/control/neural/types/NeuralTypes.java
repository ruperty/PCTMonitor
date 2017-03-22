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
 * @author Rupert Young
 * Moon's Information Technology
 *
 */
public class NeuralTypes {
    
    public final static int Default = 0;
    public final static int Subtract = 1;
    public final static int MedianFilter = 2;
    public final static int USensor = 3;
    public final static int LightSensor = 4;
    public final static int Smooth = 5;
    public final static int Change = 6;
    public final static int DelayPeriod = 7;
    public final static int Limit = 8;
    public final static int MotorRead = 9;
    public final static int MotorWrite = 10;
    public final static int WeightedSum = 11;
    public final static int WeightedMultiply = 12;
    public final static int Flip = 13;
    public final static int Or = 14;
    public final static int TimeIntegral = 15;
    public final static int LimitBanded = 16;
    public final static int ZeroFilter = 17;
    public final static int ChangeSign = 18;
    public final static int GreaterThan = 19;
    public final static int DynamicLimit = 20;
    public final static int Constant = 21;
    public final static int Scaling = 22;
    public final static int Habituate = 23;
    public final static int Maximum = 24;
    public final static int Sleep = 25;
    public final static int Integration = 26;
    public final static int PhoneSensor = 27;
    public final static int ProportionalIntegration = 28;
    public final static int Proportional = 29;
    public final static int AcceleromterSensor = 30;
    public final static int MotorSpeed = 31;
    public final static int SoundSensor = 32;
    public final static int SmoothChange = 33;
    public final static int SineTransfer = 34;
    public final static int GyroSensor = 35;
    public final static int MotorCount = 36;
    public final static int MotorPower = 37;
    public final static int TimeChange = 38;
    public final static int Rate = 39;

public final static int  MotorRotate= 40;
    //public final static int  = 40;
    //public final static int  = 40;
    //public final static int  = 40;
    //public final static int  = 40;

    
    public static int getNeuralTypes(String name) throws Exception {
        int type = -1;

        if (name.equals("")) {
            type = Default;
            return type;
        }

        if (name.equals("Subtract")) {
            type = Subtract;
            return type;
        }

        if (name.equals("MedianFilter")) {
            type = MedianFilter;
            return type;
        }

                if (name.equals("USensor")) {
            type = USensor;
            return type;
        }
        if (name.equals("LightSensor")) {
            type = LightSensor;
            return type;
        }
        if (name.equals("Smooth")) {
            type = Smooth;
            return type;
        }
        if (name.equals("Change")) {
            type = Change;
            return type;
        }
        if (name.equals("DelayPeriod")) {
            type = DelayPeriod;
            return type;
        }
        if (name.equals("Limit")) {
            type = Limit;
            return type;
        }
        if (name.equals("MotorRead")) {
            type = MotorRead;
            return type;
        }
        if (name.equals("MotorWrite")) {
            type = MotorWrite;
            return type;
        }
        if (name.equals("WeightedMultiply")) {
            type = WeightedMultiply;
            return type;
        }
        if (name.equals("Flip")) {
            type = Flip;
            return type;
        }
        if (name.equals("Or")) {
            type = Or;
            return type;
        }
        if (name.equals("TimeIntegral")) {
            type = TimeIntegral;
            return type;
        }
        if (name.equals("LimitBanded")) {
            type = LimitBanded;
            return type;
        }
        if (name.equals("ZeroFilter")) {
            type = ZeroFilter;
            return type;
        }
        if (name.equals("ChangeSign")) {
            type = ChangeSign;
            return type;
        }
        if (name.equals("GreaterThan")) {
            type = GreaterThan;
            return type;
        }
        if (name.equals("WeightedSum")) {
            type = WeightedSum;
            return type;
        }
        if (name.equals("DynamicLimit")) {
            type = DynamicLimit;
            return type;
        }
        if (name.equals("Constant")) {
            type = Constant;
            return type;
        }
        if (name.equals("Scaling")) {
            type = Scaling;
            return type;
        }
        if (name.equals("Habituate")) {
            type = Habituate;
            return type;
        }
        if (name.equals("Maximum")) {
            type = Maximum;
            return type;
        }
        if (name.equals("Sleep")) {
            type = Sleep;
            return type;
        }
        if (name.equals("Rate")) {
            type = Rate;
            return type;
        }
        if (name.equals("Integration")) {
            type = Integration;
            return type;
        }
        if (name.equals("PhoneSensor")) {
            type = PhoneSensor;
            return type;
        }
        if (name.equals("ProportionalIntegration")) {
            type = ProportionalIntegration;
            return type;
        }
        if (name.equals("Proportional")) {
            type = Proportional;
            return type;
        }
        if (name.equals("AcceleromterSensor")) {
            type = AcceleromterSensor;
            return type;
        }
        if (name.equals("MotorSpeed")) {
            type = MotorSpeed;
            return type;
        }
        if (name.equals("SoundSensor")) {
            type = SoundSensor;
            return type;
        }
        if (name.equals("SmoothChange")) {
            type = SmoothChange;
            return type;
        }
        if (name.equals("SineTransfer")) {
            type = SineTransfer;
            return type;
        }
        if (name.equals("GyroSensor")) {
            type = GyroSensor;
            return type;
        }
        if (name.equals("MotorCount")) {
            type = MotorCount;
            return type;
        }
        if (name.equals("MotorPower")) {
            type = MotorPower;
            return type;
        }
        if (name.equals("TimeChange")) {
            type = TimeChange;
            return type;
        }
        if (name.equals("MotorRotate")) {
            type = MotorRotate;
            return type;
        }
        if (type < 0) {
            throw new Exception("NeuralTypes: no code for type " + name);
        }
        return type;
    }
}
