package informationProvider;

public class ParameterProvider {
	
	public static NodeBatteryLevel getNBL(String nodeId)
	{
		return new NodeBatteryLevel(15);
	}
	
	public static EstimatedThroughput getET(String nodeId)
	{
		return new EstimatedThroughput(15);
	}
}
