package helper;

import java.io.InputStream;
import java.text.MessageFormat;

import io.restassured.module.jsv.JsonSchemaValidator;

public class ServiceHelper {
	
	//MÉTODO PARA CRIAR O CAMINHO PARA O ARQUIVO JSON
	public static InputStream jsonSchemaStream(String endpoint, String schema, int status) {
		String path = "/schemas/{0}/{1}/{2}.json";
		path = MessageFormat.format(path, endpoint, schema, status);
		return ServiceHelper.class.getResourceAsStream(path);
	}
	
	//VALIDADOR DE SCHEMA AUTOMATICO
	public static JsonSchemaValidator matchesJsonSchema(String endpoint, String schema, int status) {
		
		InputStream schemaToMatch = jsonSchemaStream(endpoint, schema, status);
		return JsonSchemaValidator.matchesJsonSchema(schemaToMatch);
	}
}