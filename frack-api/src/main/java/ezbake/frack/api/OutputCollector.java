package ezbake.frack.api;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import ezbake.frack.core.util.ThriftUtil;

import java.io.IOException;
import java.util.List;

import org.apache.thrift.TBase;

public class OutputCollector
{
	private List<byte []> dataBetweenPipes;
	private Multimap<String, byte[]> dataToBroadcast;
	
	public OutputCollector()
	{
		this.dataBetweenPipes = Lists.newArrayList();
		this.dataToBroadcast = ArrayListMultimap.create();
	}
	
	public void sendBetweenPipes(TBase thriftObject) throws IOException
	{
		Preconditions.checkNotNull(thriftObject);
		dataBetweenPipes.add(ThriftUtil.serialize(thriftObject));
	}	
	
	public void broadcast(String topic, TBase thriftObject) throws IOException
	{
		Preconditions.checkNotNull(topic);
		Preconditions.checkArgument(!topic.isEmpty());
		Preconditions.checkNotNull(thriftObject);
		
		dataToBroadcast.put(topic, ThriftUtil.serialize(thriftObject));
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
