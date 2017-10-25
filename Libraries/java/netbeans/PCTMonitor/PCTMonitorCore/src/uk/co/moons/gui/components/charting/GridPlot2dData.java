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
package uk.co.moons.gui.components.charting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Rupert Young Moon's Information Technology
 *
 */
public class GridPlot2dData extends ArrayList<GridPlot2dDataSet> {

    private final HashMap<String, Integer> titleIndex;

    public GridPlot2dData() {
        titleIndex = new HashMap<>();
    }

    public void put(String title, GridPlot2dDataSet gpds) {
        Integer index = size();
        add(gpds);
        titleIndex.put(title, index);
    }

    public GridPlot2dDataSet get(String title) {

        return get(titleIndex.get(title));

    }

    public List<String> getKeys() {
        List<String> keys =  new ArrayList<>();

        for (GridPlot2dDataSet gpds : this) {
            keys.add(gpds.getTitle());
        }

        return keys;
    }

    @Override
    public void clear() {
        super.clear();
        titleIndex.clear();
    }

}
