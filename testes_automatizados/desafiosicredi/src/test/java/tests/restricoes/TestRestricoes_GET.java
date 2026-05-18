package tests.restricoes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import constants.Endpoints;
import helper.BaseTest;

import static org.hamcrest.Matchers.*;

import java.util.Random;

import static helper.ServiceHelper.matchesJsonSchema;
import static org.hamcrest.MatcherAssert.assertThat;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;

@Epic("RESTRIÇÕES")
@Feature("VERIFICAÇÃO DE RESTRIÇÕES ATRAVÉS DO GET")
public class TestRestricoes_GET extends BaseTest {
	private Response response;
	
	@DisplayName("Teste mostra que possui restrição")
	@Test
	@Description("Teste verifica se o GET de restrições devolve um json schema e status code correto")
	public void deveMostrarQuePossuiRestricao() {
		Allure.label("parentSuite", "RESTRIÇÕES");
        Allure.suite("VERIFICAÇÃO DE RESTRIÇÕES ATRAVÉS DO GET");
        
		for (String cpf : cpfsComRestricao) {
			response = rest.getCpf(Endpoints.RESTRICAO_GET_CPF, cpf);
			assertThat(response.asString(), matchesJsonSchema("restricoes", "get", 200));
			assertThat(response.statusCode(), is(200));
		}
	}
	
	@DisplayName("Teste verifica mensagem recebida de resposta para o GET")
	@Test
	@Description("Teste verifica se o GET de restrições mostra corretamente que o CPF enviado possui restrição com uma mensagem de retorno")
	public void deveMostrarMensagemCorretamente() {
		Allure.label("parentSuite", "RESTRIÇÕES");
        Allure.suite("VERIFICAÇÃO DE RESTRIÇÕES ATRAVÉS DO GET");
        
		String message;
		
		for (String cpf : cpfsComRestricao) {
			response = rest.getCpf(Endpoints.RESTRICAO_GET_CPF, cpf);
			message = "O CPF " + cpf + " possui restrição";
			assertThat(response.asString(), matchesJsonSchema("restricoes", "get", 200));
			assertThat(response.statusCode(), is(200));
			assertThat(response.jsonPath().getString("mensagem"), equalTo(message));
		}
	}
	
	@DisplayName("Teste mostra que não possui restrição")
	@Test
	@Description("Teste verifica se o GET de restrições mostra corretamente que o CPF enviado não possui restrição devolvendo um status code de 204")
	public void naoDeveMostrarQuePossuiRestricao() {
		Allure.label("parentSuite", "RESTRIÇÕES");
        Allure.suite("VERIFICAÇÃO DE RESTRIÇÕES ATRAVÉS DO GET");
        
        String cpfInterno;
        
        do {
        	cpfInterno = faker.number().digits(11);
        } while (cpfsComRestricao.contains(cpfInterno));
        
		response = rest.getCpf(Endpoints.RESTRICAO_GET_CPF, cpfInterno);
		assertThat(response.statusCode(), is(204));
	}
	
	@DisplayName("Teste mostra se é possível realizar o GET com letras ao invés de números")
	@Test
	@Description("Teste verifica se o GET de restrições mostra que não é possível pesquisar utilizando outro formato de dado sem ser número de CPF enviando uma String")
	public void naoDeveAceitarPesquisarCPFSemSerNumero() {
		Allure.label("parentSuite", "RESTRIÇÕES");
        Allure.suite("VERIFICAÇÃO DE RESTRIÇÕES ATRAVÉS DO GET");
        
		String alfabeto = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String cpfTexto = "";
        Random random = new Random();

        for (int i = 0; i < 11; i++) {
            char character = alfabeto.charAt(random.nextInt(alfabeto.length()));
            cpfTexto += character;
        }
        
		response = rest.getCpf(Endpoints.RESTRICAO_GET_CPF, cpfTexto);
		assertThat(response.statusCode(), not(204));
	}
	
	@DisplayName("Teste mostra se aceita CPF com numero menor que 11 digitos")
	@Test
	@Description("Teste verifica se o GET de restrições mostra que não é possível pesquisar um CPF com menos de onze digitos")
	public void naoDeveAceitarPesquisarCPFMenorQueOnzeDigitos() {
		Allure.label("parentSuite", "RESTRIÇÕES");
        Allure.suite("VERIFICAÇÃO DE RESTRIÇÕES ATRAVÉS DO GET");
        
        String cpfInterno = faker.number().digits(8);

		response = rest.getCpf(Endpoints.RESTRICAO_GET_CPF, cpfInterno);
		assertThat(response.statusCode(), not(204));
	}
	
	@DisplayName("Teste mostra se aceita CPF com numero maior que 11 digitos")
	@Test
	@Description("Teste verifica se o GET de restrições mostra que não é possível pesquisar um CPF com mais de onze digitos")
	public void naoDeveAceitarPesquisarCPFMaiorQueOnzeDigitos() {
		Allure.label("parentSuite", "RESTRIÇÕES");
        Allure.suite("VERIFICAÇÃO DE RESTRIÇÕES ATRAVÉS DO GET");
        
        String cpfInterno = faker.number().digits(15);
        
		response = rest.getCpf(Endpoints.RESTRICAO_GET_CPF, cpfInterno);
		assertThat(response.statusCode(), not(204));
	}
}
