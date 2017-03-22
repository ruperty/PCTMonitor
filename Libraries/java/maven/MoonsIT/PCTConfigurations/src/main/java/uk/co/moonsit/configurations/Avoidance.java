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

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.ControlFunction;
import pct.moons.co.uk.schema.layers.Layers;
import pct.moons.co.uk.schema.layers.Layers.Layer;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.InputFunctions;
import pct.moons.co.uk.schema.layers.Layers.Layer.Controller.Functions.InputFunctions.Transfers;
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class Avoidance {
    
    public Avoidance() {
        
    }
    
    public Layers run() throws Exception {
        Layers layers = new Layers();
        layers.setType("Robot");
        layers.setDescription("Control of value of sonic reading for avoidance");
        List layer = layers.getLayer();
        
        layer.add(configureLayer0());
        return layers;
    }
    
    private Layer configureLayer0() throws Exception {
        Layer layer = new Layer();
        layer.setName("ThisLayer");
        List<Controller> controllers = layer.getController();
        
        controllers.add(configureControllerSonicControl());
        
        return layer;
    }
    
    private Controller configureControllerSonicControl() throws Exception {
        Controller controller = new Controller();
        controller.setName("SonicControl");
        Functions functions = new Functions();
        
        InputFunctions inputFunctions = new InputFunctions();
        
        {
            ControlFunction input = new ControlFunction();
            input.setName("SonicControlInput");
            input.setDescription("Control of smoothed sonic input");
            String[][] parameters = {{"Smoothness", "0.5", "Double"}};
            String[][] links = {{"SonicInputLimiting",""}};
            input.setNeuralFunction(Utils.configureNeuralFunction("Smooth", parameters, links));
            inputFunctions.setInput(input);
        }
        
        {
            Transfers transfers = new Transfers();
            List<ControlFunction> transfersList = transfers.getTransfer();
            ControlFunction transfer = new ControlFunction();
            transfer.setName("SonicInputLimiting");
            transfer.setDescription("Limiting input");
            String[][] parameters = {{"Max", "50", "Double"}, {"Min", "0", "Double"}};
            String[][] links = {{"SonicRaw"}};
            transfer.setNeuralFunction(Utils.configureNeuralFunction("Limit", parameters, links));
            transfersList.add(transfer);
            
            inputFunctions.setTransfers(transfers);
        }
        functions.setInputFunctions(inputFunctions);
        
        controller.setFunctions(functions);
        return controller;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        String file = "C:\\Versioning\\PCTSoftware\\Controllers\\Robot\\my.xml";
        Avoidance avoidance = new Avoidance();
        Utils.saveToXML(avoidance.run(), file);
        try {
            Utils.verify(file);
        } catch (IOException ex) {
            Logger.getLogger(Avoidance.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Avoidance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
