package ezbake.frack.api;

import ezbake.frack.core.data.thrift.StreamEvent;
import ezbake.frack.core.util.ThriftUtil;

import java.nio.ByteBuffer;

import org.junit.Test;

public class PumpTest
{
	public static class TestPump extends Pump<StreamEvent>
	{
		public TestPump()
		{
			super(StreamEvent.class);
		}

		@Override
		public void process(StreamEvent e)
		{
			System.out.println(e.toString());
		}
	}
	
	@Test
	public void testPump() throws Exception
	{
		TestPump p = new TestPump();		
		StreamEvent e = new StreamEvent();
		e.dateTime = "foo";
		e.content = ByteBuffer.wrap("bar".getBytes());
		System.out.println(e.toString());
		byte [] serial = ThriftUtil.serialize(e);
		p.process(p.deserialize(serial));
	}
}
