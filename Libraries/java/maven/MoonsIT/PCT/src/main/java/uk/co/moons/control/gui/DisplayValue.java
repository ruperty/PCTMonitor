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
package uk.co.moons.control.gui;

/**
 *
 * @author ReStart
 */
public class DisplayValue {

    private Double value;
    private String name;
    private int places;

    public DisplayValue(String name, Double value, int places) {
        this.value = value;
        if (name.contains(":")) {
            String[] arr = name.split(":");
            this.name = arr[0];
            this.places = Integer.parseInt(arr[1]);
        } else {
            this.name = name;
            this.places = places;
        }
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlaces() {
        return places;
    }

    public void setPlaces(int places) {
        this.places = places;
    }

}
