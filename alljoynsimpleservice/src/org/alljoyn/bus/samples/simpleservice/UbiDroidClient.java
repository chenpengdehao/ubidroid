package org.alljoyn.bus.samples.simpleservice;


public class UbiDroidClient {
	private String clientName;
	private String serviceName;
	
	public UbiDroidClient()
	{
		new UbiDroidClient("", "");
	}

	public UbiDroidClient(String clientName, String serviceName)
	{
		this.clientName = clientName;
		this.serviceName = serviceName;
	}
	
	public void setClientName(String name)
	{
		this.clientName = name;
	}
	
	public void setClientService(String serviceName)
	{
		this.serviceName = serviceName;
	}
	
	public String getClientName()
	{
		return clientName;
	}
	
	public String getClientService()
	{
		return serviceName;
	}
}
