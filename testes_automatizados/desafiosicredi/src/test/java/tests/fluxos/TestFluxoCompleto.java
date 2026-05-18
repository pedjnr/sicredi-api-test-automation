package tests.fluxos;

import static helper.ServiceHelper.matchesJsonSchema;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import constants.Endpoints;
import datafactory.DynamicFactory;
import helper.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import models.Simulacao;

@Epic("FLUXOS")
@Feature("TESTE DE FLUXO COMPLETO DESDE VERIFICAÇÃO DE RESTRIÇÃO E CADASTRO ATÉ EXCLUSÃO DE SIMULAÇÃO")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestFluxoCompleto extends BaseTest {
	
	private static String cpfFluxo;
	private static Simulacao simuFluxo;
	private static String idParaExcluir;
	
	@DisplayName("Teste não deve mostrar que o CPF possui restrição")
	@Test
	@Order(1)
	public void naoDeveMostrarQuePossuiRestricao() {
		Allure.label("parentSuite", "FLUXOS");
        Allure.suite("TESTE DE FLUXO COMPLETO DESDE VERIFICAÇÃO DE RESTRIÇÃO E CADASTRO ATÉ EXCLUSÃO DE SIMULAÇÃO");
		
		do {
        	cpfFluxo = faker.number().digits(11);
        } while (cpfsComRestricao.contains(cpfFluxo));
		
        
		response = rest.getCpf(Endpoints.RESTRICAO_GET_CPF, cpfFluxo);
		assertThat(response.statusCode(), is(204));
	}
	
	@DisplayName("Teste deve cadastrar com sucesso")
	@Test
	@Order(2)
	public void deveCadastrarSimulacaoComSucesso() {
		Allure.label("parentSuite", "FLUXOS");
        Allure.suite("TESTE DE FLUXO COMPLETO DESDE VERIFICAÇÃO DE RESTRIÇÃO E CADASTRO ATÉ EXCLUSÃO DE SIMULAÇÃO");
        
		simuFluxo = DynamicFactory.generateRandomSimulationCPF(cpfFluxo);
		response = rest.post(Endpoints.SIMULACAO, simuFluxo);
		assertThat(response.statusCode(), is(201));
		assertThat(response.asString(), matchesJsonSchema("simulacoes", "post", 201));
		assertThat(response.jsonPath().getString("nome"), equalTo(simuFluxo.getNome()));
		assertThat(response.jsonPath().getString("email"), equalTo(simuFluxo.getEmail()));
		assertThat(response.jsonPath().getDouble("valor"), equalTo(simuFluxo.getValor()));
		assertThat(response.jsonPath().getInt("parcelas"), equalTo(simuFluxo.getParcelas()));
		assertThat(response.jsonPath().getBoolean("seguro"), equalTo(simuFluxo.isSeguro()));		
	}
	
	@DisplayName("Teste deve listar simulações cadastradas e encontrar a que foi cadastrada pelo teste anterior")
	@Test
	@Order (3)
	public void deveListarTodasSimulacoes() {
		Allure.label("parentSuite", "FLUXOS");
        Allure.suite("TESTE DE FLUXO COMPLETO DESDE VERIFICAÇÃO DE RESTRIÇÃO E CADASTRO ATÉ EXCLUSÃO DE SIMULAÇÃO");
		
		response = rest.get(Endpoints.SIMULACAO);
		
		assertThat(response.asString(), matchesJsonSchema("simulacoes", "get", 200));
		assertThat(response.statusCode(), is(200));
		assertTrue(response.asString().contains(simuFluxo.getCpf()));
		assertTrue(response.asString().contains(simuFluxo.getNome()));
		assertTrue(response.asString().contains(simuFluxo.getEmail()));
		assertTrue(response.asString().contains(String.valueOf(simuFluxo.getValor())));
		assertTrue(response.asString().contains(String.valueOf(simuFluxo.getParcelas())));
		assertTrue(response.asString().contains(String.valueOf(simuFluxo.isSeguro())));
	}
	
	@DisplayName("Teste deve mostrar a simulação específica previamente cadastrada pelo usuário")
	@Test
	@Order (4)
	public void deveMostrarSimulacaoEspecifica() {
		Allure.label("parentSuite", "FLUXOS");
        Allure.suite("TESTE DE FLUXO COMPLETO DESDE VERIFICAÇÃO DE RESTRIÇÃO E CADASTRO ATÉ EXCLUSÃO DE SIMULAÇÃO");
		
		response = rest.getCpf(Endpoints.SIMULACAO_CPF, cpfFluxo);
		assertThat(response.asString(), matchesJsonSchema("simulacoes", "get_cpf", 200));
		assertThat(response.jsonPath().getString("nome"), equalTo(simuFluxo.getNome()));
		assertThat(response.jsonPath().getString("email"), equalTo(simuFluxo.getEmail()));
		assertThat(response.jsonPath().getDouble("valor"), equalTo(simuFluxo.getValor()));
		assertThat(response.jsonPath().getInt("parcelas"), equalTo(simuFluxo.getParcelas()));
		assertThat(response.jsonPath().getBoolean("seguro"), equalTo(simuFluxo.isSeguro()));
	}
	
	@DisplayName("Teste deve atualizar o cadastro com sucesso")
	@Test
	@Order (5)
	public void deveAtualizarCadastroComSucesso() {
		Allure.label("parentSuite", "FLUXOS");
        Allure.suite("TESTE DE FLUXO COMPLETO DESDE VERIFICAÇÃO DE RESTRIÇÃO E CADASTRO ATÉ EXCLUSÃO DE SIMULAÇÃO");
		
		simuFluxo = DynamicFactory.generateRandomSimulation();
		Simulacao simuInterna = simulacaoService.getSimulacao(cpfFluxo);
		response = rest.put(Endpoints.SIMULACAO_CPF, simuFluxo, cpfFluxo);
		cpfFluxo = response.jsonPath().getString("cpf");
		
		assertThat(response.statusCode(), is(200));
		assertThat(response.jsonPath().getInt("id"), equalTo(simuInterna.getId()));
		assertThat(response.jsonPath().getString("nome"), equalTo(simuFluxo.getNome()));
		assertThat(response.jsonPath().getString("email"), equalTo(simuFluxo.getEmail()));
		assertThat(response.jsonPath().getInt("parcelas"), equalTo(simuFluxo.getParcelas()));
		assertThat(response.jsonPath().getBoolean("seguro"), equalTo(simuFluxo.isSeguro()));	
	}
	
	@DisplayName("Teste deve excluir a simulação com sucesso")
	@Test
	@Order (6)
	public void deveExcluirSimulacaoEspecifica() {
		Allure.label("parentSuite", "FLUXOS");
        Allure.suite("TESTE DE FLUXO COMPLETO DESDE VERIFICAÇÃO DE RESTRIÇÃO E CADASTRO ATÉ EXCLUSÃO DE SIMULAÇÃO");
		
		simuFluxo = simulacaoService.getSimulacao(cpfFluxo);
		idParaExcluir = String.valueOf(simuFluxo.getId());
		response = rest.delete(Endpoints.SIMULACAO_DELETE, idParaExcluir);
		assertThat(response.asString(), equalTo("OK"));
	}
}
