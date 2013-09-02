package ezbake.frack.api;


public class Pipe
{
	protected OutputCollector outputCollector;
	
	public Pipe()
	{
		this.outputCollector = new OutputCollector();		
	}
	
	public OutputCollector getOutputCollector()
	{
		return outputCollector;		
	}
}
