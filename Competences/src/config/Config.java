package config;

import java.io.IOException;
import java.util.Properties;

public class Config {

	private static final Properties prop = new Properties();
	
	public static String getProperty(String key){
		try{
		if(prop.isEmpty()) {
			prop.load(ClassLoader.getSystemClassLoader().getResourceAsStream("config.properties"));
		}
		}catch(IOException e){
			System.err.println(e.getMessage());
		}
		return prop.getProperty(key);
	}
}
