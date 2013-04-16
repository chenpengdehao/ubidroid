package org.gtubicomp.ubidroid;

import org.alljoyn.bus.BusException;
import org.alljoyn.bus.annotation.BusInterface;
import org.alljoyn.bus.annotation.BusMethod;

@BusInterface (name = "org.alljoyn.bus.samples.simple.SimpleInterface")
public interface SampleInterface {
	
	@BusMethod
    public String Ping(String inStr) throws BusException;   
	@BusMethod
    String GetPicture(String inStr) throws BusException; //inStr is the string
    @BusMethod
    String[] GetFeature() throws BusException; //inStr is the string
    @BusMethod
    void RegisterClient(String clientName) throws BusException;
    @BusMethod
    void UnRegisterClient(String clientName) throws BusException;
	
}
