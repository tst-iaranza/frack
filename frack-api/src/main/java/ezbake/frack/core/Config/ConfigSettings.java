package ezbake.frack.core.config;

import java.io.Serializable;
import java.util.Map;

import com.google.common.collect.Maps;

public class ConfigSettings  implements Serializable {
	Map<String, Object> settings = Maps.newHashMap();
	
	/**
    	* Define a new configuration map.
    	* WARNING: If configuration already exists, it will be over written. 
    	* @param map <code>Map<String,Object></code> The configuration of this component. This configuration is used to manage processes.
	*/
	public void addSettings(Map<String,Object> map) {
		if (!settings.isEmpty()){
			settings.clear();
		}
		settings.putAll(map);
	}
	
	/**
    	* Define a new configuration item.
    	* WARNING: If configuration already exists, it will be over written. 
    	* @param name <code>String</code> The key/name of the setting.
    	* @param value <code>Object</code> The value of the setting.
	*/
	public void addSetting(String name, Object value) {
		settings.put(name, value);
	}
	
	/**
    	* Retrieve configuration map.
    	* @return map <code>Map<String,Object></code> The configuration of this component. This configuration is used to manage processes.
	*/
	public Map<String, Object> getSettings() {
		return settings;
	}
	
	/**
    	* Retrieve a configuration item.
    	* @param name <code>String</code> The key/name of the setting.
    	* @return value <code>Object</code> The value of the setting.
	*/
	public Object getSetting(String name) {
		Object ret = null;
		if (settings.containsKey(name)) {
			ret = settings.get(name);
		}
		return ret;
	}
}
