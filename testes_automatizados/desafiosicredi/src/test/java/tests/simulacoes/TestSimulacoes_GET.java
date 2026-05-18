package tests.simulacoes;

import static helper.ServiceHelper.matchesJsonSchema;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import constants.Endpoints;
import helper.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import models.Simulacao;

@Epic("SIMULAÇÕES")
@Feature("VERIFICAÇÃO DE SIMULAÇÕES CADASTRADAS ATRAVÉS DO GET")
public class TestSimulacoes_GET extends BaseTest {
	
	@DisplayName("Teste mostra todas as simulações")
	@Test
	@Description("Teste verifica se o GET de simulações devolve um schema correto e um status code como especificado")
	public void deveListarTodasSimulacoes() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("VERIFICAÇÃO DE SIMULAÇÕES CADASTRADAS ATRAVÉS DO GET");
        
		response = rest.get(Endpoints.SIMULACAO);
		assertThat(response.asString(), matchesJsonSchema("simulacoes", "get", 200));
		assertThat(response.statusCode(), is(200));
	}
	
	@DisplayName("Teste mostra simulação específica")
	@Test
	@Description("Teste verifica se o GET de simulações devolve corretamente as informações de uma simulação específica ao informar o CPF")
	public void deveMostrarSimulacaoEspecifica() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("VERIFICAÇÃO DE SIMULAÇÕES CADASTRADAS ATRAVÉS DO GET");
		
		List<Simulacao> listaSimulacoes = simulacaoService.getSimulacoes();
		Simulacao simulacaoAleatoria = listaSimulacoes.get(rand.nextInt(listaSimulacoes.size()));
		String cpfEscolhido = simulacaoAleatoria.getCpf();
		
		response = rest.getCpf(Endpoints.SIMULACAO_CPF, cpfEscolhido);
		assertThat(response.asString(), matchesJsonSchema("simulacoes", "get_cpf", 200));
		assertThat(response.jsonPath().getString("nome"), equalTo(simulacaoAleatoria.getNome()));
		assertThat(response.jsonPath().getString("email"), equalTo(simulacaoAleatoria.getEmail()));
		assertThat(response.jsonPath().getInt("id"), equalTo(simulacaoAleatoria.getId()));
		assertThat(response.jsonPath().getDouble("valor"), equalTo(simulacaoAleatoria.getValor()));
		assertThat(response.jsonPath().getInt("parcelas"), equalTo(simulacaoAleatoria.getParcelas()));
		assertThat(response.jsonPath().getBoolean("seguro"), equalTo(simulacaoAleatoria.isSeguro()));
	}
	
	@DisplayName("Teste não mostra nenhuma simulação")
	@Test
	@Description("Teste verifica se o GET de simulações retorna um json schema e status code correto ao informar um CPF inexistente")
	public void naoDeveMostrarSimulacaoNenhuma() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("VERIFICAÇÃO DE SIMULAÇÕES CADASTRADAS ATRAVÉS DO GET");
		
		response = rest.get(Endpoints.SIMULACAO);
		List<String> cpfsSimulacoes = response.jsonPath().getList("cpf");
		
		String cpfInterno;
        
        do {
        	cpfInterno = faker.number().digits(11);
        } while (cpfsComRestricao.contains(cpfInterno) || cpfsSimulacoes.contains(cpfInterno));
        
		response = rest.getCpf(Endpoints.SIMULACAO_CPF, cpfInterno);
		assertThat(response.asString(), matchesJsonSchema("simulacoes", "get_cpf", 404));
		assertThat(response.statusCode(), is(404));
	}
	
	@DisplayName("Teste mostra que possui restrição")
	@Test
	@Description("Teste verifica se o GET de simulações devolve um json schema, status code e mensagem correta")
	public void deveMostrarCpfComRestricao() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("VERIFICAÇÃO DE SIMULAÇÕES CADASTRADAS ATRAVÉS DO GET");
		
		String message;
		
		for (String cpf : cpfsComRestricao) {
			response = rest.getCpf(Endpoints.SIMULACAO_CPF, cpf);
			message = "O CPF " + cpf + " possui restrição";
			assertThat(response.asString(), matchesJsonSchema("simulacoes", "get_cpf", 404));
			assertThat(response.statusCode(), is(404));
			assertThat(response.jsonPath().getString("mensagem"), equalTo(message));
		}
	}
	
	@DisplayName("Teste verifica status code")
	@Test
	@Description("Teste verifica se o GET de simulações devolve um status code de 204 como indicado na documentação se não existir nenhuma simulação cadastrada")
	public void deveRetornarStatusCodeCorretoComListaVazia() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("VERIFICAÇÃO DE SIMULAÇÕES CADASTRADAS ATRAVÉS DO GET");
        
		deletaSimulacoes();
		
		response = rest.get(Endpoints.SIMULACAO);
		assertThat(response.statusCode(), is(204));
	}	
}
