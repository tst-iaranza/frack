package ezbake.frack.api;

import com.twitter.elephantbird.util.TypeRef;
	
import java.io.IOException;

import org.apache.thrift.TBase;
import org.apache.thrift.TDeserializer;

public abstract class Pump<T extends TBase<?, ?>> extends Pipe
{
	TypeRef<T> typeRef;
	public Pump(Class<T> type)
	{
		this.typeRef = new TypeRef<T>(type){};
	}

	public abstract void process(T object) throws IOException;
	
	// EXPERT USE ONLY Used by underlying implentation 
	public final void doWork(byte [] bytes) throws IOException
	{
		process(deserialize(bytes));
	}

	protected T deserialize(byte [] bytes)
	{
		try 
		{
			TDeserializer deserializer = new TDeserializer();
			T thriftStruct = typeRef.safeNewInstance();
			deserializer.deserialize(thriftStruct, bytes);
			return thriftStruct;
		} 
		catch(Throwable e)
		{
			throw new IllegalArgumentException(e);
		}
	}
}
