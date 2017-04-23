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

import java.util.List;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.functions.BaseControlFunction;
import uk.co.moonsit.utils.MoonsString;

/**
 *
 * @author ReStart
 */
public class MarkedNeuralFunction extends NeuralFunction {


    private Integer resetIndex = null;
    private Integer dataIndex = null;
    

    public MarkedNeuralFunction() throws Exception {
        super();

    }

    public MarkedNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

       
    }

    @Override
    public void init() throws Exception {
        super.init();
        List<BaseControlFunction> controls = links.getControlList();

        for (int i = 0; i < controls.size(); i++) {
            String linkType = links.getType(i);// controls.get(i).getLinkType();
            if (linkType == null) {
                dataIndex=i;
                continue;
            }

            if (linkType.equals("Mark")) {
                resetIndex = i;
            }
            
        }
    }

   

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double newVal = controls.get(dataIndex).getValue();
        if(controls.get(resetIndex).getValue()==1){
            output=newVal;
        }


        return output;
    }

     @Override
    public String getDataString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Value").append(":");
        sb.append(MoonsString.formatStringPlaces(output, 6));

        return sb.toString();
    }
    
}
