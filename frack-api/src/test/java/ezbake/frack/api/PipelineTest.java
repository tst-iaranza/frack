package ezbake.frack.api;

import com.google.common.collect.Queues;

import ezbake.frack.core.data.thrift.StreamEvent;

import java.io.IOException;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Deque;
import java.util.Date;

import org.junit.Test;

public class PipelineTest
{
	
	public static String getCurrentTimeStamp() 
	{
    	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
    	Date now = new Date();
    	String strDate = sdfDate.format(now);
    	return strDate;
	}

	public static class MyParserPump extends Pump<StreamEvent>
	{
		public MyParserPump()
		{
			super(StreamEvent.class);
		}
		
		@Override
		public void process(StreamEvent e)
		{
			System.out.println(e);
		}
	}

	public static class MyNozzle extends Nozzle
	{
		@Override
		public void process() throws IOException
		{
			StreamEvent e = new StreamEvent();
			e.dateTime = getCurrentTimeStamp();
			e.content = ByteBuffer.wrap("Hello World".getBytes());
			getOutputCollector().sendBetweenPipes(e);
		}
	}
	
		
	
	@Test
	public void basicPipelineTest() throws Exception
	{
		Pipeline pl = new Pipeline();
		pl.addNozzle(new MyNozzle());
		pl.addPump(new MyParserPump());
		Deque<byte []> workQueue = Queues.newArrayDeque();
		for(Pipe pipe : pl.getPipes())
		{
			if(pl.isNozzle(pipe))
			{
				Nozzle n = (Nozzle)pipe;
				n.process();
				for(byte [] work : n.getOutputCollector().getDataBetweenPipes())
				{
					workQueue.addFirst(work);
				}
			}	
			else if(pl.isPump(pipe))
			{
				Pump p = (Pump)pipe;
				byte [] work = workQueue.poll();
				while (work != null)
				{
					p.doWork(work);
					work = workQueue.poll();
				}
			}
		}
	}
}
