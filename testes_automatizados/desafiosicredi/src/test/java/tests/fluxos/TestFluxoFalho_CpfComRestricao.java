package tests.fluxos;

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
@Feature("TESTE DE FLUXO COM CPF QUE POSSUI RESTRIÇÃO SENDO ENVIADO")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestFluxoFalho_CpfComRestricao extends BaseTest {
	
	protected static String cpfComRestricaoFluxo;
	private static Simulacao simuFluxo;
	private static String idParaExcluir;
	
	@DisplayName("Teste deve mostrar que o CPF possui restrição")
	@Test
	@Order(1)
	public void deveMostrarQuePossuiRestricao() {
		Allure.label("parentSuite", "FLUXOS");
        Allure.suite("TESTE DE FLUXO COM CPF QUE POSSUI RESTRIÇÃO SENDO ENVIADO");
		
        cpfComRestricaoFluxo = String.valueOf(cpfsComRestricao.get(rand.nextInt(cpfsComRestricao.size())));
        
		response = rest.getCpf(Endpoints.RESTRICAO_GET_CPF, cpfComRestricaoFluxo);
		assertThat(response.statusCode(), is(200));
	}
	
	@DisplayName("Teste não deve cadastrar com sucesso")
	@Test
	@Order(2)
	public void naoDeveCadastrarSimulacaoComSucesso() {
		Allure.label("parentSuite", "FLUXOS");
        Allure.suite("TESTE DE FLUXO COM CPF QUE POSSUI RESTRIÇÃO SENDO ENVIADO");
        
		simuFluxo = DynamicFactory.generateRandomSimulationCPF(cpfComRestricaoFluxo);
		response = rest.post(Endpoints.SIMULACAO, simuFluxo);
		assertThat(response.statusCode(), not(201));
	
	}
	
	@DisplayName("Teste deve listar simulações cadastradas e não encontrar a que foi cadastrada pelo teste anterior")
	@Test
	@Order (3)
	public void deveListarTodasSimulacoesMasNaoEncontrarAQueFoiCadastrada() {
		Allure.label("parentSuite", "FLUXOS");
        Allure.suite("TESTE DE FLUXO COM CPF QUE POSSUI RESTRIÇÃO SENDO ENVIADO");
		
		response = rest.get(Endpoints.SIMULACAO);

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
	public void naoDeveMostrarSimulacaoEspecifica() {
		Allure.label("parentSuite", "FLUXOS");
        Allure.suite("TESTE DE FLUXO COM CPF QUE POSSUI RESTRIÇÃO SENDO ENVIADO");
		
		response = rest.getCpf(Endpoints.SIMULACAO_CPF, cpfComRestricaoFluxo);
		assertThat(response.statusCode(), is(404));
	}
	
	@DisplayName("Teste não deve atualizar o cadastro com sucesso")
	@Test
	@Order (5)
	public void naoDeveAtualizarCadastroComSucesso() {
		Allure.label("parentSuite", "FLUXOS");
        Allure.suite("TESTE DE FLUXO COM CPF QUE POSSUI RESTRIÇÃO SENDO ENVIADO");
		
		simuFluxo = DynamicFactory.generateRandomSimulationCPF(cpfComRestricaoFluxo);
		response = rest.put(Endpoints.SIMULACAO_CPF, simuFluxo, cpfComRestricaoFluxo);
		cpfComRestricaoFluxo = response.jsonPath().getString("cpf");
		assertThat(response.statusCode(), is(404));
		assertThat(response.jsonPath().getString("mensagem"), equalTo("CPF " + "123456789" + " não encontrado"));
	}
	
	@DisplayName("Teste não deve excluir a simulação com sucesso pois ela deve ser inexistente")
	@Test
	@Order (6)
	public void naoDeveExcluirSimulacaoEspecificaPoisElaNaoExiste() {
		Allure.label("parentSuite", "FLUXOS");
        Allure.suite("TESTE DE FLUXO COM CPF QUE POSSUI RESTRIÇÃO SENDO ENVIADO");
		
		simuFluxo = simulacaoService.getSimulacao(cpfComRestricaoFluxo);
		idParaExcluir = String.valueOf(simuFluxo.getId());
		response = rest.delete(Endpoints.SIMULACAO_DELETE, idParaExcluir);
		assertThat(response.asString(), equalTo("Simulação não encontrada"));

	}
	
}
