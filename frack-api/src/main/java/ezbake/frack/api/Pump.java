package ezbake.frack.api;

import org.apache.thrift.TBase;

public abstract class Pump extends Pipe
{
	public Pump()
	{
	}

	public abstract void process(byte [] b);
}
