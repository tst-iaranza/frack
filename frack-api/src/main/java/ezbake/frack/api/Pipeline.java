package ezbake.frack.api;

import com.google.common.collect.Lists;

import java.util.List;

public class Pipeline
{
	List<Pipe> pipes;
	
	public Pipeline()
	{
		this.pipes = Lists.newArrayList();
	}	
	
	public boolean isNozzle(Pipe p)
	{
		return p instanceof Nozzle;
	}

	public boolean isPump(Pipe p)
	{
		return p instanceof Pump;
	}
	
	public void addPump(Pump p)
	{
		pipes.add(p);
	}

	public void addNozzle(Nozzle n)
	{
		pipes.add(n);
	}
	
	List<Pipe> getPipes()
	{
		return pipes;	
	}
}
