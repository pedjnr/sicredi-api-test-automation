package tests.paralelismo;

import static helper.ServiceHelper.matchesJsonSchema;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.Random;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import constants.Endpoints;
import datafactory.DynamicFactory;
import models.Simulacao;
import services.BaseRest;
import services.SimulacaoService;

@Epic("PARALELISMO")
@Feature("VERIFICAÇÃO DE TESTES RODANDO SIMULTANEAMENTE")
@Execution(ExecutionMode.CONCURRENT)
public class TestParalelismo {
	
	private static String idTest_1;
	private static String idTest_2;
	private static BaseRest rest = new BaseRest();
	private static SimulacaoService simulacaoService = new SimulacaoService();
	private static Simulacao simulation;
	private static String cpfCadastrado;
	private static Random rand = new Random();
	
	@BeforeAll
	public static void cadastraSimu() {
		cadastraSimulacao();
	}
	
	public static void cadastraSimulacao() {
		simulation = DynamicFactory.generateRandomSimulation();
		
		cpfCadastrado = simulation.getCpf();
		
		rest.post(Endpoints.SIMULACAO, simulation);
	}
	
	
	@DisplayName("Teste cadastra uma simulação no sistema - Teste Paralelo")
	@Test
	@Description("Teste procura cadastrar uma simulação com sucesso utilizando a API")
	public void deveCadastrarSimulacaoComSucesso() {
		Allure.label("parentSuite", "PARALELISMO");
        Allure.suite("CADASTRO DE SIMULAÇÃO");

        Response response;
        
		Simulacao simuInterna = DynamicFactory.generateRandomSimulation();
		
		response = rest.post(Endpoints.SIMULACAO, simuInterna);
		idTest_1 = response.jsonPath().getString("id");
		
		assertThat(response.statusCode(), is(201));
		assertThat(response.asString(), matchesJsonSchema("simulacoes", "post", 201));
		assertThat(response.jsonPath().getString("nome"), equalTo(simuInterna.getNome()));
		assertThat(response.jsonPath().getString("email"), equalTo(simuInterna.getEmail()));
		assertThat(response.jsonPath().getDouble("valor"), equalTo(simuInterna.getValor()));
		assertThat(response.jsonPath().getInt("parcelas"), equalTo(simuInterna.getParcelas()));
		assertThat(response.jsonPath().getBoolean("seguro"), equalTo(simuInterna.isSeguro()));
	}
	
	@DisplayName("Teste edita uma simulação cadastrada no sistema - Teste Paralelo")
	@Test
	@Description("Teste procura editar com sucesso uma simulação previamente cadastrada utilizando a API")
	public void deveAtualizarCadastroComSucesso() {
		Allure.label("parentSuite", "PARALELISMO");
        Allure.suite("EDIÇÃO DE SIMULAÇÃO CADASTRADA");
        
		Response response;
		Simulacao simulation = DynamicFactory.generateRandomSimulation();
		
		Simulacao simuInterna = simulacaoService.getSimulacao(cpfCadastrado);
		
		response = rest.put(Endpoints.SIMULACAO_CPF, simulation, cpfCadastrado);
		
		idTest_2 = response.jsonPath().getString("id");
		
		cpfCadastrado = response.jsonPath().getString("cpf");
		
		assertThat(response.statusCode(), is(200));
		assertThat(response.jsonPath().getInt("id"), equalTo(simuInterna.getId()));
		assertThat(response.jsonPath().getString("nome"), equalTo(simulation.getNome()));
		assertThat(response.jsonPath().getString("email"), equalTo(simulation.getEmail()));
		assertThat(response.jsonPath().getInt("parcelas"), equalTo(simulation.getParcelas()));
		assertThat(response.jsonPath().getBoolean("seguro"), equalTo(simulation.isSeguro()));	
	}
	
	@DisplayName("Teste exclui uma simulação cadastrada no sistema - Teste Paralelo")
	@Test
	@Description("Teste seleciona uma simulação aleatória cadastrada no sistema e tenta realizar uma exclusão com sucesso utilizando a API")
	public void deveDeletarSimulacaoComSucesso() {
		Allure.label("parentSuite", "PARALELISMO");
        Allure.suite("EXCLUSÃO DE SIMULAÇÃO");
		
		String idEscolhido;
		Response response;
		response = rest.get(Endpoints.SIMULACAO);
		List<String> idSimu = response.jsonPath().getList("id");
		
		do {
			idEscolhido = String.valueOf(idSimu.get(rand.nextInt(idSimu.size())));
		}
		while (idEscolhido.equals(idTest_1) || idEscolhido.equals(idTest_2));
		
		rest.delete(Endpoints.SIMULACAO_DELETE, idEscolhido);
		response = rest.get(Endpoints.SIMULACAO);
		assertThat(response.statusCode(), is(200));
	}
	
	@DisplayName("Teste lista todas as simulações no sistema - Teste Paralelo")
	@Test
	@Description("Teste procura visualizar com sucesso todas as simulações cadastradas no sistema utilizando a API")
	public void deveListarTodasSimulacoes() {
		Allure.label("parentSuite", "PARALELISMO");
        Allure.suite("LISTAGEM DE SIMULAÇÕES CADASTRADAS NO SISTEMA");
		
        Response response;
		response = rest.get(Endpoints.SIMULACAO);
		assertThat(response.statusCode(), is(200));
	}
	
}