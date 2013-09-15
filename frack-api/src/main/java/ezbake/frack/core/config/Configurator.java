package ezbake.frack.core.config;

import com.google.common.collect.Maps;
import com.google.common.io.Closeables;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yaml.snakeyaml.Yaml;


public class Configurator implements Iterable<Map.Entry<String,String>> {
	
	/**
	* List of configuration resources.
	*/
	private ArrayList<Resource> resources = new ArrayList<Resource>();
	
	/**
	* List of configurations.
	*/
	private Map<String, ConfigSettings> configs = Maps.newHashMap();
	private boolean beQuiet = true;
	private static final Log LOG = LogFactory.getLog(Configurator.class);
	
	public Configurator() { }
	
	public Configurator(boolean runQuietMode) { 
		beQuiet = runQuietMode;
	}
	
	// TO DO: Configurator(Configurator conf) { }

	public void addConfigSettings(String name, ConfigSettings value) {
		configs.put(name, value);
	}
	
	public ConfigSettings getConfigSettings(String name) {
		return configs.get(name);
	}
	
	/* ******************************Start Resources Region****************************** */
	
	/**
	* The resource object contains the name and location information for a given configuration resource.
	*/
	private static class Resource { 
		private final Object resource;
		private final String name;
		
		public Resource(Object resource) {
			this(resource.toString(), resource);
		}
		
		public Resource(String name, Object resource) {
			this.resource = resource;
			this.name = name;
		}
		
		 public String getName(){
			 return name;
		 }
		 
		 public Object getResource() {
			 return resource;
		 }
		 
		 @Override
		 public String toString() {
			 return name;
		 }
	}
		
	/**
	* Add the default pipeline configuration resource. 
	* @param filePath <code>String</code> of resource to be added. The local filesystem is examined directly to find the resource, without referring to the classpath.
	*/
	public void addPipeLineResource(String filePath) {
		File f = new File(filePath);
		// add a check here....
		addResourceObject(new Resource("default", f));
	}
	
	/**
	* Add a configuration resource.
	* @param id <code>String</code> of the configuration. This MUST match the configuration name for Pipes.
	* @param filePath <code>File.Path</code> of resource to be added. The local filesystem is examined directly to find the resource, without referring to the classpath.
	*/
	public void addResource(String id, File filePath) {
		addResourceObject(new Resource(id, filePath));
	}
	
	/**
	* Add a configuration resource.
	* @param id <code>String</code> of the configuration. This MUST match the configuration name for Pipes. 
	* @param filePath <code>String</code> of resource to be added. The local filesystem is examined directly to find the resource, without referring to the classpath.
	*/
	public void addResource(String id, String filePath) {
		File f = new File(filePath);
		// add a check here....
		addResourceObject(new Resource(id, f));
	}
	
	private void addResourceObject(Resource config) {
		resources.add(config);
	}
	
	/* ******************************End Resources Region****************************** */
	
	/* ******************************Start Loading Region****************************** */
	
	public void loadConfigurations() {
		for (Resource res : resources) {
			loadConfiguration(res);
		}
	}
	
	/**
	* Load configuration that is contained in the Resource object.
	* @return done <code>boolean</code> Indicates whether the resource was successfully loaded.
	*/
	private boolean loadConfiguration(Resource resource) {
		Object conf = resource.getResource();
		
		// File.Path resource
		if (conf instanceof File) {
			File file = (File)conf;
			if (file.exists()) {
				if (!beQuiet) {
				LOG.debug("Parsing file: " + file);
				}
				this.addConfigSettings(resource.getName(), loadYAML(file));
				return true;
			}
		}
		return false;
	}
	
	private ConfigSettings loadYAML(File file) {
		Yaml yaml = new Yaml();
		ConfigSettings settings = new ConfigSettings();
		Map<String,Object> result = Maps.newHashMap();
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			result = (Map<String,Object>) yaml.load(in);
			settings.addSettings(result);
		} catch (Exception e) {
			LOG.debug("Error parsing file: " + e.getStackTrace().toString());
		} finally {
			Closeables.closeQuietly(in);
		}
		return settings;
	}
	
	/* ******************************End Loading Region****************************** */

	public Iterator<Entry<String, String>> iterator() {
		// TODO Auto-generated method stub
		return null;

	}
}
