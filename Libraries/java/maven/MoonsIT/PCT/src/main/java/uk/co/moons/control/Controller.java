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

import java.util.List;
import java.util.logging.Logger;
import uk.co.moons.config.Activation;
import uk.co.moons.control.functions.BaseControlFunction;
import uk.co.moons.control.functions.BaseErrorFunction;
import uk.co.moons.control.functions.BaseInputFunction;
import uk.co.moons.control.functions.BaseOutputFunction;
import uk.co.moons.control.functions.BaseReferenceFunction;

public class Controller extends BaseController {

    private static final Logger logger = Logger.getLogger(Controller.class.getName());

    public Controller(BaseInputFunction i, BaseErrorFunction e, BaseOutputFunction o, BaseReferenceFunction r) {
        inputFunction = i;
        errorFunction = e;
        outputFunction = o;
        referenceFunction = r;
    }

    public Controller(ControlFunctionCollection i, ControlFunctionCollection e, ControlFunctionCollection o, ControlFunctionCollection r) {
        inputFunction1 = i;
        errorFunction1 = e;
        outputFunction1 = o;
        referenceFunction1 = r;
    }

   
    
    public void setActivation(Activation activation) {
        this.activation = activation;
    }

    public void close() throws Exception {
        if (referenceFunction1 != null) {
            List<BaseControlFunction> transfers = referenceFunction1.getTransferFunctions();
            if (transfers != null) {
                for (BaseControlFunction transfer : transfers) {
                    transfer.getNeural().close();
                }
            }
            referenceFunction1.getMainFunction().getNeural().close();
        }

        if (inputFunction1 != null) {
            List<BaseControlFunction> transfers = inputFunction1.getTransferFunctions();
            if (transfers != null) {
                for (BaseControlFunction transfer : inputFunction1.getTransferFunctions()) {
                    transfer.getNeural().close();
                }
            }
            inputFunction1.getMainFunction().getNeural().close();
        }

        if (errorFunction1 != null) {
            errorFunction1.getMainFunction().getNeural().close();
            List<BaseControlFunction> transfers = errorFunction1.getTransferFunctions();
            if (transfers != null) {
                for (BaseControlFunction transfer : errorFunction1.getTransferFunctions()) {
                    transfer.getNeural().close();
                }
            }
        }

        if (outputFunction1 != null) {            
            outputFunction1.getMainFunction().getNeural().close();
            List<BaseControlFunction> transfers = outputFunction1.getTransferFunctions();
            if (transfers != null) {
                for (BaseControlFunction transfer : outputFunction1.getTransferFunctions()) {
                    transfer.getNeural().close();
                }
            }
        }

    }

    public void init() throws Exception {
        if (referenceFunction1 != null) {
            List<BaseControlFunction> transfers = referenceFunction1.getTransferFunctions();
            if (transfers != null) {
                for (BaseControlFunction transfer : transfers) {
                    transfer.getNeural().init();
                }
            }
            referenceFunction1.getMainFunction().getNeural().init();
        }

        if (inputFunction1 != null) {
            List<BaseControlFunction> transfers = inputFunction1.getTransferFunctions();
            if (transfers != null) {
                for (BaseControlFunction transfer : inputFunction1.getTransferFunctions()) {
                    transfer.getNeural().init();
                }
            }
            inputFunction1.getMainFunction().getNeural().init();
        }

        if (errorFunction1 != null) {
            errorFunction1.getMainFunction().getNeural().init();
            List<BaseControlFunction> transfers = errorFunction1.getTransferFunctions();
            if (transfers != null) {
                for (BaseControlFunction transfer : errorFunction1.getTransferFunctions()) {
                    transfer.getNeural().init();
                }
            }
        }

        if (outputFunction1 != null) {            
            outputFunction1.getMainFunction().getNeural().init();
            List<BaseControlFunction> transfers = outputFunction1.getTransferFunctions();
            if (transfers != null) {
                for (BaseControlFunction transfer : outputFunction1.getTransferFunctions()) {
                    transfer.getNeural().init();
                }
            }
        }

    }

    
    @Override
    public void stop() throws Exception {
        if (referenceFunction1 != null) {
            List<BaseControlFunction> transfers = referenceFunction1.getTransferFunctions();
            if (transfers != null) {
                for (BaseControlFunction transfer : transfers) {
                    transfer.getNeural().stop();
                }
            }
            referenceFunction1.getMainFunction().getNeural().stop();
        }

        if (inputFunction1 != null) {
            List<BaseControlFunction> transfers = inputFunction1.getTransferFunctions();
            if (transfers != null) {
                for (BaseControlFunction transfer : inputFunction1.getTransferFunctions()) {
                    transfer.getNeural().stop();
                }
            }
            inputFunction1.getMainFunction().getNeural().stop();
        }

        if (errorFunction1 != null) {
            errorFunction1.getMainFunction().getNeural().stop();
            List<BaseControlFunction> transfers = errorFunction1.getTransferFunctions();
            if (transfers != null) {
                for (BaseControlFunction transfer : errorFunction1.getTransferFunctions()) {
                    transfer.getNeural().stop();
                }
            }
        }

        if (outputFunction1 != null) {            
            outputFunction1.getMainFunction().getNeural().stop();
            List<BaseControlFunction> transfers = outputFunction1.getTransferFunctions();
            if (transfers != null) {
                for (BaseControlFunction transfer : outputFunction1.getTransferFunctions()) {
                    transfer.getNeural().stop();
                }
            }
        }

    }

    @Override
    public double iterate() throws Exception {
        double output = 0;
        boolean deactivateInput = false;
        boolean deactivateReference = false;
        boolean deactivateError = false;
        boolean deactivateOutput = false;
        if (activation != null && !activation.getLinks().getControlList().isEmpty()) {
            BaseControlFunction control = activation.getLinks().getControlList().get(0);
            if (control.getValue() > 0) {
                if (activation.getFunctions().size() > 0) {
                    for (int i : activation.getFunctions()) {
                        switch (i) {
                            case Activation.INPUT:
                                deactivateInput = true;
                                break;
                            case Activation.OUTPUT:
                                deactivateOutput = true;
                                break;
                            case Activation.ERROR:
                                deactivateError = true;
                                break;
                            case Activation.REFERENCE:
                                deactivateReference = true;
                                break;
                        }
                    }
                } else {
                    deactivateInput = deactivateReference = deactivateError = deactivateOutput = true;
                }
            } else {
                deactivateInput = deactivateReference = deactivateError = deactivateOutput = false;
            }
        }
        try {
            if (referenceFunction1 != null) {
                List<BaseControlFunction> transfers = referenceFunction1.getTransferFunctions();
                if (transfers != null) {
                    for (BaseControlFunction transfer : transfers) {
                        transfer.update();
                    }
                }
                referenceFunction1.getMainFunction().setActive(!deactivateReference);
                referenceFunction1.getMainFunction().update();
            }

            if (inputFunction1 != null) {
                List<BaseControlFunction> transfers = inputFunction1.getTransferFunctions();
                if (transfers != null) {
                    for (BaseControlFunction transfer : inputFunction1.getTransferFunctions()) {
                        transfer.update();
                    }
                }
                inputFunction1.getMainFunction().setActive(!deactivateInput);
                inputFunction1.getMainFunction().update();
            }

            if (errorFunction1 != null) {
                errorFunction1.getMainFunction().setActive(!deactivateError);
                errorFunction1.getMainFunction().update();
                List<BaseControlFunction> transfers = errorFunction1.getTransferFunctions();
                if (transfers != null) {
                    for (BaseControlFunction transfer : errorFunction1.getTransferFunctions()) {
                        transfer.update();
                    }
                }
            }

            if (outputFunction1 != null) {
                outputFunction1.getMainFunction().setActive(!deactivateOutput);
                output = outputFunction1.getMainFunction().update();
                List<BaseControlFunction> transfers = outputFunction1.getTransferFunctions();
                if (transfers != null) {
                    for (BaseControlFunction transfer : outputFunction1.getTransferFunctions()) {
                        transfer.update();
                    }
                }
            }
        } catch (Exception e) {
            logger.info(e.toString());
            throw e;
        }

        return output;
    }

    public void dummyIterate() throws Exception {

        try {
            if (referenceFunction1 != null) {
                List<BaseControlFunction> transfers = referenceFunction1.getTransferFunctions();
                if (transfers != null) {
                    for (BaseControlFunction transfer : transfers) {
                        transfer.getNeural();
                    }
                }
                referenceFunction1.getMainFunction().getNeural();
            }

            if (inputFunction1 != null) {
                List<BaseControlFunction> transfers = inputFunction1.getTransferFunctions();
                if (transfers != null) {
                    for (BaseControlFunction transfer : inputFunction1.getTransferFunctions()) {
                        transfer.getNeural();
                    }
                }
                inputFunction1.getMainFunction().getNeural();
            }

            if (errorFunction1 != null) {
                errorFunction1.getMainFunction().getNeural();
                List<BaseControlFunction> transfers = errorFunction1.getTransferFunctions();
                if (transfers != null) {
                    for (BaseControlFunction transfer : errorFunction1.getTransferFunctions()) {
                        transfer.getNeural();
                    }
                }
            }

            if (outputFunction1 != null) {
                outputFunction1.getMainFunction().getNeural();
                List<BaseControlFunction> transfers = outputFunction1.getTransferFunctions();
                if (transfers != null) {
                    for (BaseControlFunction transfer : outputFunction1.getTransferFunctions()) {
                        transfer.getNeural();
                    }
                }
            }
        } catch (Exception e) {
            logger.info(e.toString());
            throw e;
        }

    }
}
