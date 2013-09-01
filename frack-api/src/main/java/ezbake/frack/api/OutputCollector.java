package ezbake.frack.api;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import java.util.List;

public class OutputCollector
{
	private List<byte []> dataBetweenPipes;
	private Multimap<String, byte[]> dataToBroadcast;
	
	public OutputCollector()
	{
		this.dataBetweenPipes = Lists.newArrayList();
		this.dataToBroadcast = ArrayListMultimap.create();
	}
	
	public void emitBetweenPipes(byte [] data)
	{
		Preconditions.checkNotNull(data);
		if(data.length < 1)
		{
			return;
		}
		
		dataBetweenPipes.add(data);
	}	
	
	public void broadcast(String topic, byte [] data)
	{
		Preconditions.checkNotNull(topic);
		Preconditions.checkArgument(!topic.isEmpty());
		Preconditions.checkNotNull(data);
		
		dataToBroadcast.put(topic, data);
	}
	
	public List<byte []>  getDataBetweenPipes()
	{
		return dataBetweenPipes;
	}
	
	public Iterable<String> getBroadcastTopics()
	{
		return dataToBroadcast.keySet();
	}	
	
	public Iterable<byte []> getDataToBroadcastForTopic(String topic)
	{
		Preconditions.checkNotNull(topic);
		Preconditions.checkArgument(dataToBroadcast.containsKey(topic));
		return dataToBroadcast.get(topic);
	}
}
