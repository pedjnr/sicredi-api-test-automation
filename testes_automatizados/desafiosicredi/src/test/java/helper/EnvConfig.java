package helper;

import java.text.MessageFormat;
import java.util.Properties;

public class EnvConfig {
	
	private static Properties enviromentProperties;
	
	//MÉTODO QUE RETORNA UMA PROPRIEDADE DE AMBIENTE
	public static String getProperty(String key, String defaultValue) {
		loadConfigurationProperties();
		return System.getProperty(key, enviromentProperties.getProperty(key, defaultValue));
	}
	
	//ESSE MÉTODO CARREGA O ARQUIVO DE CONFIGURAÇÃO DE AMBIENTE
	private static void loadConfigurationProperties() {
		if (enviromentProperties == null) {
			//env DEVE SER PASSADO POR LINHA DE COMANDO
			String env = System.getProperty("env", "local");
			
			String configFile = MessageFormat.format(("/config/{0}.properties"), env);
			
			enviromentProperties = new Properties();
			
			try {
				enviromentProperties.load(EnvConfig.class.getResourceAsStream(configFile));
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Erro ao ler arquivo de configuração");
			}
		}
	}
	
}
