package tests.fluxos;

import static helper.ServiceHelper.matchesJsonSchema;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
@Feature("TESTE DE FLUXO COM CPF VAZIO SENDO ENVIADO")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestFluxoFalho_CpfVazio extends BaseTest {
	
	protected static String cpfFluxo = " ";
	private static Simulacao simuFluxo;
	private static String idParaExcluir;
	
	@DisplayName("Teste deve mostrar que o CPF está vazio")
	@Test
	@Order(1)
	public void deveInformarCpfInvalidoRestricoes() {
		Allure.label("parentSuite", "FLUXOS");
        Allure.suite("TESTE DE FLUXO COM CPF VAZIO SENDO ENVIADO");
		
        
		response = rest.getCpf(Endpoints.RESTRICAO_GET_CPF, cpfFluxo);
		assertThat(response.statusCode(), not(204));
	}
	
	@DisplayName("Teste não deve cadastrar com sucesso")
	@Test
	@Order(2)
	public void naoDeveCadastrarSimulacaoComSucesso() {
		Allure.label("parentSuite", "FLUXOS");
        Allure.suite("TESTE DE FLUXO COM CPF VAZIO SENDO ENVIADO");
        
		simuFluxo = DynamicFactory.generateRandomSimulationCPF(cpfFluxo);
		response = rest.post(Endpoints.SIMULACAO, simuFluxo);
		assertThat(response.statusCode(), not(201));	
	}
	
	@DisplayName("Teste deve listar simulações cadastradas e não encontrar a que foi cadastrada pelo teste anterior")
	@Test
	@Order (3)
	public void deveListarTodasSimulacoesMasNaoEncontrarDadosDasimuQueNaoDeveSerCadastrada() {
		Allure.label("parentSuite", "FLUXOS");
        Allure.suite("TESTE DE FLUXO COM CPF VAZIO SENDO ENVIADO");
		
		response = rest.get(Endpoints.SIMULACAO);
		
		assertThat(response.asString(), matchesJsonSchema("simulacoes", "get", 200));
		assertThat(response.statusCode(), is(200));
		assertFalse(response.asString().contains(simuFluxo.getCpf()));
		assertFalse(response.asString().contains(simuFluxo.getNome()));
		assertFalse(response.asString().contains(simuFluxo.getEmail()));
		assertFalse(response.asString().contains(String.valueOf(simuFluxo.getValor())));
		assertFalse(response.asString().contains(String.valueOf(simuFluxo.getParcelas())));
		assertFalse(response.asString().contains(String.valueOf(simuFluxo.isSeguro())));
	}
	
	@DisplayName("Teste não deve mostrar a simulação específica previamente cadastrada pelo usuário")
	@Test
	@Order (4)
	public void naoDeveMostrarSimulacaoEspecificaPoisElaNaoDeveExistir() {
		Allure.label("parentSuite", "FLUXOS");
        Allure.suite("TESTE DE FLUXO COM CPF VAZIO SENDO ENVIADO");
		
		response = rest.getCpf(Endpoints.SIMULACAO_CPF, cpfFluxo);
		assertThat(response.jsonPath().getString("nome"), not(equalTo(simuFluxo.getNome())));
		assertThat(response.jsonPath().getString("email"), not(equalTo(simuFluxo.getEmail())));
		assertThat(response.jsonPath().getDouble("valor"), not(equalTo(simuFluxo.getValor())));
		assertThat(response.jsonPath().getInt("parcelas"), not(equalTo(simuFluxo.getParcelas())));
		assertThat(response.jsonPath().getBoolean("seguro"), not(equalTo(simuFluxo.isSeguro())));
	}
	
	@DisplayName("Teste não deve atualizar o cadastro com sucesso")
	@Test
	@Order (5)
	public void naoDeveAtualizarCadastroComSucessoPoisElaNaoDeveExistir() {
		Allure.label("parentSuite", "FLUXOS");
        Allure.suite("TESTE DE FLUXO COM CPF VAZIO SENDO ENVIADO");
		
		simuFluxo = DynamicFactory.generateRandomSimulation();
		Simulacao simuInterna = simulacaoService.getSimulacao(cpfFluxo);
		response = rest.put(Endpoints.SIMULACAO_CPF, simuFluxo, cpfFluxo);
		cpfFluxo = response.jsonPath().getString("cpf");
		
		assertThat(response.statusCode(), not(200));
		assertThat(response.jsonPath().getInt("id"), not(equalTo(simuInterna.getId())));
		assertThat(response.jsonPath().getString("nome"), not(equalTo(simuFluxo.getNome())));
		assertThat(response.jsonPath().getString("email"), not(equalTo(simuFluxo.getEmail())));
		assertThat(response.jsonPath().getInt("parcelas"), not(equalTo(simuFluxo.getParcelas())));
		assertThat(response.jsonPath().getBoolean("seguro"), not(equalTo(simuFluxo.isSeguro())));	
	}
	
	@DisplayName("Teste não deve excluir a simulação com sucesso pois ela deve ser inexistente")
	@Test
	@Order (6)
	public void naoDeveExcluirSimulacaoEspecificaPoisElaNaoDeveExistir() {
		Allure.label("parentSuite", "FLUXOS");
        Allure.suite("TESTE DE FLUXO COM CPF VAZIO SENDO ENVIADO");
		
		simuFluxo = simulacaoService.getSimulacao(cpfFluxo);
		idParaExcluir = String.valueOf(simuFluxo.getId());
		response = rest.delete(Endpoints.SIMULACAO_DELETE, idParaExcluir);
		assertThat(response.asString(), not(equalTo("OK")));
	}
	
}
