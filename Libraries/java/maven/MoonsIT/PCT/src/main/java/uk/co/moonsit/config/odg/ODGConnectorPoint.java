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
package uk.co.moonsit.config.odg;

/**
 *
 * @author ReStart
 */
public class ODGConnectorPoint {

    private String name;
    private final String type;
    private final Integer id;
    private final Integer controllerId;
    private Integer transferId;

    public ODGConnectorPoint(String name, String type, Integer id, Integer controllerId) {
        this.type = type;
        this.name = name;
        this.id = id;
        this.controllerId = controllerId;
    }

    public Integer getTransferId() {
        return transferId;
    }

    public void setTransferId(Integer transferId) {
        this.transferId = transferId;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public Integer getControllerId() {
        return controllerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + " [" + type + " " + id + "] " + controllerId;
    }
}
