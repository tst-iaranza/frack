package ezbake.frack.api;

import java.io.IOException;

public abstract class Nozzle extends Pipe
{
	public Nozzle()
	{
	}
	
	public abstract void process() throws IOException;
}
