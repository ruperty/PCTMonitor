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
import java.util.ArrayList;
import java.util.List;
import pct.moons.co.uk.schema.layers.ControlFunction;
import pct.moons.co.uk.schema.layers.Layers;

/**
 *
 * @author Rupert Young
 */
public class Functions {

    static public List<ControlFunction> bearingMemoryFunctions() throws Exception {

        List<ControlFunction> list = new ArrayList<>();

        ControlFunction function;

        function = Utils.configureControlFunction("ProximityTransition",
                "Point at which unit turns into open space", "ZeroTransition",
                new String[][]{{"Infinity", "true", "Boolean"}, {"Direction", "Negative", "String"}},
                new String[][]{{"ProximityInputSmooth"}});
        list.add(function);

        function = Utils.configureControlFunction("BearingRecord", "Current value of sensed bearing at proximity transition point", "Product",
                null, new String[][]{{"ProximityTransition"}, {"CompassRead"}});
        list.add(function);

        function = Utils.configureControlFunction("BearingMemory", "The stored value of the bearing updated if it changes", "UpdateFilter",
                null, new String[][]{{"BearingRecord"}});
        list.add(function);

        function = Utils.configureControlFunction("BearingMemoryTimer", "Deactiavates after configured period, or if input is not 1", "BinaryTimer",
                new String[][]{{"Period", "2000", "Double"}, {"TimerOn", "1", "Double"}, {"TimerOff", "Infinity", "Double"}},
                new String[][]{{"ProximityTransition"}});
        list.add(function);

        function = Utils.configureControlFunction("BearingMemoryConflictInput", "Memory value if period is active, else inf", "Product",
                null,
                new String[][]{{"BearingMemory"}, {"BearingMemoryTimer"}});
        list.add(function);

        return list;
    }

    static public Layers.Layer.Controller.Functions.InputFunctions configureInputFunction(List<ControlFunction> list) throws Exception {

        Layers.Layer.Controller.Functions.InputFunctions inputFunctions = new Layers.Layer.Controller.Functions.InputFunctions();
        ControlFunction input = list.remove(list.size()-1);
        inputFunctions.setInput(input);

        {
            Layers.Layer.Controller.Functions.InputFunctions.Transfers transfers = new Layers.Layer.Controller.Functions.InputFunctions.Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();

            for (ControlFunction transfer : list) {
                transfersList.add(transfer);
            }

            inputFunctions.setTransfers(transfers);
        }

        return inputFunctions;
    }

}
