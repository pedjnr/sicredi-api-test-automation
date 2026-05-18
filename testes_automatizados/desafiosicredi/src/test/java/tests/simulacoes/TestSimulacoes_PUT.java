package tests.simulacoes;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import constants.Endpoints;
import datafactory.DynamicFactory;
import helper.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import models.Simulacao;

@Epic("SIMULAÇÕES")
@Feature("ATUALIZAÇÃO DE SIMULAÇÕES CADASTRADAS ATRAVÉS DO PUT")
public class TestSimulacoes_PUT extends BaseTest {
	
	@DisplayName("Teste edita uma simulação no sistema")
	@Test
	@Description("Teste verifica se é possível atualizar uma simulação com sucesso utilizando a API")
	public void deveAtualizarCadastroComSucesso() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("ATUALIZAÇÃO DE SIMULAÇÕES CADASTRADAS ATRAVÉS DO PUT");
		
		simulation = DynamicFactory.generateRandomSimulation();
		Simulacao simuInterna = simulacaoService.getSimulacao(cpfCadastrado);
		response = rest.put(Endpoints.SIMULACAO_CPF, simulation, cpfCadastrado);
		assertThat(response.statusCode(), is(200));
		assertThat(response.jsonPath().getInt("id"), equalTo(simuInterna.getId()));
		assertThat(response.jsonPath().getString("nome"), equalTo(simulation.getNome()));
		assertThat(response.jsonPath().getString("email"), equalTo(simulation.getEmail()));
		assertThat(response.jsonPath().getInt("parcelas"), equalTo(simulation.getParcelas()));
		assertThat(response.jsonPath().getBoolean("seguro"), equalTo(simulation.isSeguro()));	
	}
	
	@DisplayName("Teste edita uma simulação no sistema e verifica o campo valor")
	@Test
	@Description("Teste verifica se é possível atualizar uma simulação com sucesso utilizando a API e faz uma asserção para verificar se o campo valor está sendo alterado")
	public void deveAtualizarValorComSucesso() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("ATUALIZAÇÃO DE SIMULAÇÕES CADASTRADAS ATRAVÉS DO PUT");
        
		simulation = DynamicFactory.generateRandomSimulation();
		
		response = rest.put(Endpoints.SIMULACAO_CPF, simulation, cpfCadastrado);

		assertThat(response.statusCode(), is(200));
		
		assertThat(response.jsonPath().getDouble("valor"), equalTo(simulation.getValor()));
		
	}
	
	@DisplayName("Teste edita uma simulação no sistema deixando o campo de CPF vazio")
	@Test
	@Description("Teste verifica se é possível atualizar uma simulação utilizando a API ao deixar o campo de CPF vazio")
	public void naoDeveDeixarCpfVazio() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("ATUALIZAÇÃO DE SIMULAÇÕES CADASTRADAS ATRAVÉS DO PUT");
		
		simulation = DynamicFactory.generateRandomSimulationCPF("");
		response = rest.put(Endpoints.SIMULACAO_CPF, simulation, cpfCadastrado);
		assertThat(response.statusCode(), not(200));
	}
	
	@DisplayName("Teste edita uma simulação no sistema deixando o campo nome sem preencher")
	@Test
	@Description("Teste verifica se é possível atualizar uma simulação utilizando a API ao deixar o campo nome vazio")
	public void naoDeveDeixarNomeVazio() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("ATUALIZAÇÃO DE SIMULAÇÕES CADASTRADAS ATRAVÉS DO PUT");
		
		simulation = DynamicFactory.generateRandomSimulationNome("");
		response = rest.put(Endpoints.SIMULACAO_CPF, simulation, cpfCadastrado);
		assertThat(response.statusCode(), not(200));
	}
	
	@DisplayName("Teste edita uma simulação no sistema enviando CPF repetido e verifica status code")
	@Test
	@Description("Teste verifica se é possível atualizar uma simulação utilizando a API ao enviar um CPF previamente cadastrado no sistema e verifica se o status code devolvido é o especificado")
	public void naoDeveAtualizarSimulacaoComSucessoEnviandoCPFRepetidoERetornarStatusCodeCorreto() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("ATUALIZAÇÃO DE SIMULAÇÕES CADASTRADAS ATRAVÉS DO PUT");
		
		Simulacao simuInterna = DynamicFactory.generateRandomSimulation();
		response = rest.post(Endpoints.SIMULACAO, simuInterna);
		simulation = DynamicFactory.generateRandomSimulationCPF(simuInterna.getCpf());
		response = rest.put(Endpoints.SIMULACAO_CPF, simulation, cpfCadastrado);
		assertThat(response.statusCode(), is(409));
		assertThat(response.jsonPath().getString("mensagem"), equalTo("CPF já existente"));
	}
	
	@DisplayName("Teste edita uma simulação no sistema enviando CPF repetido e verifica mensagem de retorno")
	@Test
	@Description("Teste verifica se é possível atualizar uma simulação utilizando a API ao enviar um CPF previamente cadastrado no sistema e verifica se a mensagem de retorno devolvida é a especificada")
	public void naoDeveAtualizarSimulacaoComSucessoEnviandoCPFRepetidoERetornarMensagemCorreta() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("ATUALIZAÇÃO DE SIMULAÇÕES CADASTRADAS ATRAVÉS DO PUT");
		
		Simulacao simuInterna = DynamicFactory.generateRandomSimulation();
		response = rest.post(Endpoints.SIMULACAO, simuInterna);
		simulation = DynamicFactory.generateRandomSimulationCPF(simuInterna.getCpf());
		response = rest.put(Endpoints.SIMULACAO_CPF, simulation, cpfCadastrado);
		assertThat(response.jsonPath().getString("mensagem"), equalTo("CPF já existente"));
	}
	
	@DisplayName("Teste edita uma simulação no sistema enviando o campo ID no body")
	@Test
	@Description("Teste verifica se é possível atualizar uma simulação utilizando a API e enviando o campo ID junto dos campos alterados")
	public void deveAtualizaSimulacaoComSucessoEnviandoId() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("ATUALIZAÇÃO DE SIMULAÇÕES CADASTRADAS ATRAVÉS DO PUT");
		
		simulationID = simulacaoServiceID.getSimulacao(cpfCadastrado);
		
		simulationID.setNome(faker.name().fullName());
		simulationID.setEmail(faker.internet().emailAddress());
		simulationID.setValor(faker.number().randomDouble(2, 1000, 40000));
		simulationID.setParcelas(faker.number().numberBetween(2, 48));
		simulationID.setSeguro(faker.bool().bool());
		
		response = rest.put(Endpoints.SIMULACAO_CPF, simulationID, cpfCadastrado);
		assertThat(response.jsonPath().getString("nome"), equalTo(simulationID.getNome()));
		assertThat(response.jsonPath().getString("email"), equalTo(simulationID.getEmail()));	
		assertThat(response.jsonPath().getInt("parcelas"), equalTo(simulationID.getParcelas()));
		assertThat(response.jsonPath().getBoolean("seguro"), equalTo(simulationID.isSeguro()));
	}
	
	@DisplayName("Teste tenta cadastrar uma simulação no sistema através do PUT")
	@Test
	@Description("Teste verifica se é possível cadastrar uma simulação utilizando o PUT da API ao enviar um CPF inexistente no sistema")
	public void naoDeveCadastrarPeloPut() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("ATUALIZAÇÃO DE SIMULAÇÕES CADASTRADAS ATRAVÉS DO PUT");
		
        response = rest.get(Endpoints.SIMULACAO);
		List<String> cpfsSimulacoes = response.jsonPath().getList("cpf");
		
		String cpfInterno;
        
        do {
        	cpfInterno = faker.number().digits(11);
        } while (cpfsComRestricao.contains(cpfInterno) || cpfsSimulacoes.contains(cpfInterno));
        
        
		simulation = DynamicFactory.generateRandomSimulation();
		response = rest.put(Endpoints.SIMULACAO_CPF, simulation, cpfInterno);
		assertThat(response.statusCode(), is(404));
		assertThat(response.jsonPath().getString("mensagem"), equalTo("CPF " + cpfInterno + " não encontrado"));
	}
	
}
