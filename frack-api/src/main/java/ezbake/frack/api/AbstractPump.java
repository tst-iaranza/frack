package ezbake.frack.api;

import org.apache.thrift.TBase;

public abstract class AbstractPump<T extends TBase<?,?>> extends Pump<T>
{
	public AbstractPump(Class<T> thriftObject)
	{
		super(thriftObject);
	}

	@Override
	public abstract void process(T thriftObject);
}
