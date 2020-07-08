package config;

import java.util.Properties;
import java.util.ResourceBundle;

public class Config {
	
	private static Properties properties;

	private Config() {
	}

	public static String get(String key) {
		if (properties == null)
			loadProperties();
		return properties.getProperty(key);
	}

	private static void loadProperties() {
		properties = new Properties();

		//Carrega bundle base
		ResourceBundle bundle = ResourceBundle.getBundle("application");
		for (String key : bundle.keySet())
			properties.put(key, bundle.getString(key));

		//Carrega bundle de profile se disponível
		String profile = System.getProperty("spring.profiles.active");
		if (profile != null) {
			bundle = ResourceBundle.getBundle("application-" + profile);
			for (String key : bundle.keySet()) {
				properties.put(key, bundle.getString(key));
				System.out.println(key);
			}
				
		}
	}
}
