package ezbake.frack.api;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import ezbake.frack.core.config.ConfigSettings;
import ezbake.frack.core.config.Configurator;

public class Pipeline
{
	Map<String, PipeInfo> pipes;
	List<String> broadcastTopics;
    Multimap<String, String> topicsToPipes;
    private Configurator configurator;
    private ConfigSettings pipeLineConfig = null;

	public static class PipeInfo implements Serializable
	{
		final String pipeId;
		final List<String> inputs;
		final Pipe pipe;
		private ConfigSettings pipeConfig = null;
		
		/**
	    * Define a new pipe in this pipeline.
	    * @param id <code>String</code> the id of this pipe. This id is used to manage processes.
	    * @param pipe <code>Pipe</code> the pipe.
		*/
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

		/**
	    * Returns pipe object.
	    * @return pipe <code>Pipe</code> the pipe object
		*/
		public Pipe getPipe()
		{
			return pipe;
		}

		/**
	    * Returns id of this pipe object.
	    * @return pipeId <code>String</code> the pipe object
		*/
		public String getPipeId()
		{
			return pipeId;
		}

		/**
	    * Add an input
	    * @param from <code>String</code> the input name
		*/
		public void addInput(String from)
		{
			Preconditions.checkNotNull(from);
			inputs.add(from);
		}
		
		/**
	    * Define a configuration for this pipe instance. This will override default pipeline settings.
	    * INFO: Pipe will attempt to match Pipe's ID as configuration ID. 
	    * 		If none is found, default settings apply.
	    * 		Use the setConfigurationaByName method to over ride this.
	    * @param configuration <code>Configurator</code> The pipeline configuration.
		*/
		public void setConfiguration(Configurator configuration)
		{
			pipeConfig = configuration.getConfigSettings(this.getPipeId());
			
			if (pipeConfig == null) {
				ConfigSettings temp = configuration.getConfigSettings("default");
				configuration.addConfigSettings(this.getPipeId(), temp);
				pipeConfig = configuration.getConfigSettings(this.getPipeId());
			}
		}
		
		/**
	    * Define a configuration for this pipe instance. This will override default pipeline settings.
	    * @param configuration <code>Configurator</code> The pipeline configuration.
	    * @param configName <code>String</code> The name of the configuration to apply.
		*/
		public void setConfigurationaByName(Configurator configuration, String configName)
		{
			pipeConfig = configuration.getConfigSettings(configName);
		}
		
		/**
	    * Retrieve configuration of this pipe instance.
	    * @return configuration <code>ConfigSettings</code> The configuration of this pipe. This configuration is used to manage processes.
		*/
		public ConfigSettings getConfiguration()
		{
			return pipeConfig;
		}
		
		public boolean isNozzle()
		{
			return this.pipe instanceof Nozzle;
		}

		public boolean isPump()
		{
			return this.pipe instanceof Pump;
		}
	}

	public Pipeline()
	{
		this.broadcastTopics = Lists.newArrayList();
		this.pipes = Maps.newHashMap();
		this.topicsToPipes = ArrayListMultimap.create();
	}	

	/**
    * Adds a Pump to the pipeline.
    * @param id <code>String</code> The id of the pipe
    * @param p <code>Pump</code> The Pump object that needs to be checked.
	*/
	public void addPump(String id, Pump p)
	{
		addPipe(id, p);
	}

	/**
    * Adds a Nozzle to the pipeline.
    * @param id <code>String</code> The id of the pipe
    * @param p <code>Nozzle</code> The Nozzle object that needs to be checked.
	*/
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
		broadcastTopics.add(topic);		
	}	

	public List<String> getBroadcastTopics()
	{
		return broadcastTopics;
	}

	public Multimap<String, String> getTopicsToPipes()
	{
		return topicsToPipes;
	}

	/**
    * Adds a Pipe to the pipeline.
    * @param id <code>String</code> The id of the pipe
    * @param p <code>Pipe</code> The Pipe object to add to the pipeline.
	*/
	void addPipe(String id, Pipe p)
	{
		Preconditions.checkState(!pipes.containsKey(id), id + " is already taken!");
		pipes.put(id, new PipeInfo(id, p));
	}

	/**
    * Retrieves all pipes in the pipeline.
    * @return <code>List<Pipe></code> The list of Pipe objects in the pipeline.
	*/
	List<Pipe> getPipes()
	{
		List <Pipe> retVal = Lists.newArrayList();
		for(PipeInfo pi : pipes.values())
		{
			retVal.add(pi.getPipe());
		}
		return retVal;
	}
	
	public List<PipeInfo> getPipeInfos()
	{
		List <PipeInfo> retVal = Lists.newArrayList();
		for(PipeInfo pi : pipes.values())
		{
			retVal.add(pi);
		}
		return retVal;
	}
	
	/**
    * Determines whether a Pipe is of type Nozzle.
    * @param p <code>Pipe</code> The Pipe object that needs to be checked.
    * @return isNozzle <code>boolean</code> The boolean result of the check.
	*/
	public boolean isNozzle(Pipe p)
	{
		return p instanceof Nozzle;
	}

	/**
    * Determines whether a Pipe is of type Pump.
    * @param p <code>Pipe</code> The Pipe object that needs to be checked.
    * @return isPump <code>boolean</code> The boolean result of the check.
	*/
	public boolean isPump(Pipe p)
	{
		return p instanceof Pump;
	}
	
	/**
    * Set a configurator for the pipeline. This configurator will manage configurations for this pipeline and all pipes in pipelines.
    * @param configuration <code>Configurator</code> The configurator of this component. This configurator is used to manage processes.
	*/
	public void setConfigurator(Configurator configuration)
	{
		configurator = configuration;
	}
	
	/**
    * Retrieve the configurator for the pipeline.
    * @return configuration <code>Configurator</code> The configurator of this component. This configurator is used to manage processes.
	*/
	public Configurator getConfigurator()
	{
		return configurator;
	}
	
	/**
    * Retrieve configuration of pipeline.
    * @return configsettings <code>ConfigSettings</code> The configuration of the pipeline. This configuration is used to manage processes.
	*/
	public ConfigSettings getConfiguration() {
		if (pipeLineConfig == null) {
			pipeLineConfig = configurator.getConfigSettings("default");
		}
		return pipeLineConfig;
	}
}
