package ezbake.frack.api;


public class Pipe
{
	private OutputCollector outputCollector;
	
	public Pipe()
	{
		this.outputCollector = new OutputCollector();		
	}
	
	public OutputCollector getOutputCollector()
	{
		return outputCollector;		
	}
}
