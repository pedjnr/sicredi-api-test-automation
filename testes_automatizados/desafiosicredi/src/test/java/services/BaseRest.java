package services;

import helper.EnvConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseRest {
	
	private RequestSpecification requestSpec;
	
	public BaseRest() {
		requestSpec = getSpecBuilder().build();
	}
	
	//REQUISIÇÃO GET
	public Response get(String endpoint) {
		return
				
			given()
				.spec(requestSpec)
//				.log().all()
			.when()
				.get(endpoint)
			.then()
//				.log().all()
				.extract()
				.response();
	}
	
	//REQUISIÇÃO GET ESPECIFICO
	public Response getCpf(String endpoint, String cpf) {
		return
			
			given()
				.spec(requestSpec)
//				.log().all()
			.when()
				.get(endpoint, cpf)
			.then()
//				.log().all()
				.extract()
				.response();
	}
	
	//REQUISIÇÃO POST
	public Response post(String endpoint, Object payload) {
		return
			
			given()
				.spec(requestSpec)
				.body(payload)
//				.log().all()
			.when()
				.post(endpoint)
			.then()
//				.log().all()
				.extract()
				.response();
	}
	
	//REQUISIÇÃO PUT
	public Response put(String endpoint, Object payload, String cpf) {
		return
			
			given()
				.spec(requestSpec)
				.body(payload)
//				.log().all()
			.when()
				.put(endpoint, cpf)
			.then()
//				.log().all()
				.extract()
				.response();
	}
	
	//REQUISIÇÃO DELETE
	public Response delete(String endpoint, String id) {
		return
			
			given()
				.spec(requestSpec)
//				.log().all()
			.when()
				.delete(endpoint, id)
			.then()
//				.log().all()
				.extract()
				.response();
	}
	
public RequestSpecBuilder getSpecBuilder() {
		
		return new RequestSpecBuilder().setBaseUri(EnvConfig.getProperty("url", "")).setAccept(ContentType.JSON).setContentType(ContentType.JSON);
	}

}
