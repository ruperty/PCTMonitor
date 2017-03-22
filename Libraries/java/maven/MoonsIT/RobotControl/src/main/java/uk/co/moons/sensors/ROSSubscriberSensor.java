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
package uk.co.moons.sensors;

import uk.co.moons.sensors.impl.BaxterEndpointStateSubscriberSensorImpl;
import uk.co.moons.sensors.impl.BaxterIRSubscriberSensorImpl;
import uk.co.moons.sensors.impl.BaxterJointStateSubscriberSensorImpl;
import uk.co.moons.sensors.impl.ImageTargetSubscriberSensorImpl;
import uk.co.moons.sensors.states.BaseSensorState;

public class ROSSubscriberSensor extends BaseSensor {

    private String topic;

    public ROSSubscriberSensor(BaseSensorState st, String topic, String variable, String index) throws Exception {
        state = st;
        this.topic = topic;
        switch (topic) {
            case "/robot/joint_states":
                sensorImpl = new BaxterJointStateSubscriberSensorImpl(topic, variable, index);
                break;
            case "/robot/limb/right/endpoint_state":
            case "/robot/limb/left/endpoint_state":
                sensorImpl = new BaxterEndpointStateSubscriberSensorImpl(topic, variable);
                break;
            case "/robotview/left/polarcoords":
            case "/robotview/right/polarcoords":
            case "/robotview/left/cartcoords":
            case "/robotview/right/cartcoords":
                sensorImpl = new ImageTargetSubscriberSensorImpl(topic, variable);
                break;
            case "/robot/range/right_hand_range/state":
            case "/robot/range/left_hand_range/state":
                sensorImpl = new BaxterIRSubscriberSensorImpl(topic, variable);                
            break;
        }
        if (sensorImpl == null) {
            throw new Exception("Topic " + topic + " not found ");
        }
    }

    @Override
    public double getValue() {
        return sensorImpl.getValue();
    }

    public void setIndex(String j) {
        switch (topic) {
            case "/robot/joint_states":
                ((BaxterJointStateSubscriberSensorImpl) sensorImpl).setIndex(j);
                break;
        }
    }

    public void setVariable(String v) {

        switch (topic) {
            case "/robot/joint_states":
                ((BaxterJointStateSubscriberSensorImpl) sensorImpl).setVariable(v);
                break;
            case "/robot/limb/right/endpoint_state":
            case "/robot/limb/left/endpoint_state":
            case "/robotview/target":
        }

    }

    @Override
    public void close() {

        sensorImpl.close();

    }
    /*
	public void init(	ConnectedNode node){
		sensorImpl.init(node);
	}*/
}
