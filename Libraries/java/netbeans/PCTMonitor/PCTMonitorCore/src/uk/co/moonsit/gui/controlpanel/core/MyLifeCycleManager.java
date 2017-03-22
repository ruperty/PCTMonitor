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
package uk.co.moonsit.gui.controlpanel.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;
import org.openide.LifecycleManager;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.TopComponent;

@SuppressWarnings ("unchecked")

@ServiceProvider(service=LifecycleManager.class, position=1)
public class MyLifeCycleManager extends LifecycleManager {
    private static final Logger logger = Logger.getLogger(MyLifeCycleManager.class.getName());

   public void saveAll() {
   }

   public void exit() {
       Set<TopComponent> tcs = TopComponent.getRegistry().getOpened();
       Iterator<TopComponent> it = tcs.iterator();
       while (it.hasNext()) {
           TopComponent tc = it.next();
           if (tc instanceof PlotPanelTopComponent) {
               logger.info("+++ Closing PlotPanelTopComponent");
               tc.close();
           }
       }
       
       Collection<LifecycleManager> c = (Collection<LifecycleManager>)Lookup.getDefault().lookupAll(LifecycleManager.class);
        for (LifecycleManager lm : c) {
            if (lm != this) {
                lm.exit();
            }
        }
               
   }

}