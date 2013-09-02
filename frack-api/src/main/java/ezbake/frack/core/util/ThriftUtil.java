package ezbake.frack.core.util;

import java.io.IOException;

import org.apache.thrift.TBase;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TSimpleJSONProtocol;

public class ThriftUtil
{
	private static TSerializer serializer = new TSerializer();
	private static TDeserializer deserializer = new TDeserializer();

	public static byte [] serialize(TBase base) throws IOException
	{
		try 
		{
			return serializer.serialize(base);
		} 
		catch(TException e) 
		{
			throw new IOException(e);
		}
	}

	public static void deserialize(byte [] bytes, TBase base) throws IOException
	{
		try 
		{
			deserializer.deserialize(base, bytes);
		} 
		catch(TException e) 
		{
			throw new IOException(e);
		}
	}
}
