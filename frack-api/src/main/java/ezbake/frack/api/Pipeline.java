package ezbake.frack.api;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import java.util.List;
import java.util.Map;

public class Pipeline
{
	Map<String, PipeInfo> pipes;
	List<String> broadcastTopics;
    Multimap<String, String> topicsToPipes;
	
	static class PipeInfo
	{
		final String pipeId;
		final List<String> inputs;
		final Pipe pipe;	
		
		public PipeInfo(String id, Pipe p)
		{
			this.pipeId = id;
			this.pipe = p;
			this.inputs = Lists.newArrayList();
		}
		
		public List<String> getInputs()
		{
			return inputs;
		}

		public Pipe getPipe()
		{
			return pipe;
		}
		
		public String getPipeId()
		{
			return pipeId;
		}

		public void addInput(String from)
		{
			Preconditions.checkNotNull(from);
			inputs.add(from);
		}
	}
	
	public Pipeline()
	{
		this.broadcastTopics = Lists.newArrayList();
		this.pipes = Maps.newHashMap();
		this.topicsToPipes = ArrayListMultimap.create();
	}	
	
	public boolean isNozzle(Pipe p)
	{
		return p instanceof Nozzle;
	}

	public boolean isPump(Pipe p)
	{
		return p instanceof Pump;
	}
	
	public void addPump(String id, Pump p)
	{
		addPipe(id, p);
	}

	public void addNozzle(String id, Nozzle n)
	{
		addPipe(id, n);
	}
	
	public void addConnection(String fromPipe, String toPipe)
	{
		// Step 1: check to see if both pipes exist
		Preconditions.checkState(pipes.containsKey(fromPipe), fromPipe + " does not exist!");
		Preconditions.checkState(pipes.containsKey(toPipe), toPipe + " does not exist!");
		
		// Step 2: Add connection to the pipe info
		PipeInfo pi = pipes.get(toPipe);
		pi.addInput(fromPipe);
		pipes.put(toPipe, pi);
	}

	public void addTopicToListenTo(final String topic, final String pipeId)
	{
		topicsToPipes.put(topic, pipeId);
	}
	
	public void addTopicToListenTo(final String topic, final Iterable<String> pipeIds)
	{
		topicsToPipes.putAll(topic, pipeIds);
	}

	public void addBroadcastTopic(final String topic)
	{
		this.broadcastTopics.add(topic);		
	}	

	void addPipe(String id, Pipe p)
	{
		Preconditions.checkState(!pipes.containsKey(id), id + " is already taken!");
		pipes.put(id, new PipeInfo(id, p));
	}
	
	
	List<Pipe> getPipes()
	{
		List <Pipe> retVal = Lists.newArrayList();
		for(PipeInfo pi : pipes.values())
		{
			retVal.add(pi.getPipe());
		}
		return retVal;
	}
}
