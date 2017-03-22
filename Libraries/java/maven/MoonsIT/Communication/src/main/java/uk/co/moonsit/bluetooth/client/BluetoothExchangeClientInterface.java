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
package uk.co.moonsit.bluetooth.client;

public interface BluetoothExchangeClientInterface {

	
	public boolean connect() ;
	
	public void close() ;
		
	public void processData() throws Exception;

	public void markRate() ;

	public void stop() ;
}
