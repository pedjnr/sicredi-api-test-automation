package tests.simulacoes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static helper.ServiceHelper.matchesJsonSchema;
import constants.Endpoints;
import datafactory.DynamicFactory;
import helper.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import models.Simulacao;
import models.SimulacaoID;

import static org.hamcrest.Matchers.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

@Epic("SIMULAÇÕES")
@Feature("CADASTRO DE SIMULAÇÕES ATRAVÉS DO POST")
public class TestSimulacoes_POST extends BaseTest {
	
	@DisplayName("Teste cadastra simulação com sucesso")
	@Test
	@Description("Teste verifica se o cadastro de simulações devolve um schema correto e um status code como especificado e envia corretamente os dados")
	public void deveCadastrarSimulacaoComSucesso() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("CADASTRO DE SIMULAÇÕES ATRAVÉS DO POST");
		
		Simulacao simuInterna = DynamicFactory.generateRandomSimulation();
		
		response = rest.post(Endpoints.SIMULACAO, simuInterna);
		assertThat(response.statusCode(), is(201));
		assertThat(response.asString(), matchesJsonSchema("simulacoes", "post", 201));
		assertThat(response.jsonPath().getString("nome"), equalTo(simuInterna.getNome()));
		assertThat(response.jsonPath().getString("email"), equalTo(simuInterna.getEmail()));
		assertThat(response.jsonPath().getDouble("valor"), equalTo(simuInterna.getValor()));
		assertThat(response.jsonPath().getInt("parcelas"), equalTo(simuInterna.getParcelas()));
		assertThat(response.jsonPath().getBoolean("seguro"), equalTo(simuInterna.isSeguro()));		
	}
	
	@DisplayName("Teste não deve cadastrar simulação com sucesso sem enviar seguro e verifica status code")
	@Test
	@Description("Teste verifica se o cadastro de simulações devolve um schema e status code correto, bem como uma mensagem de retorno informando que o seguro não pode deixar de ser enviado")
	public void naoDeveCadastrarSimulacaoComSucessoERetornarStatusCodeCorretoComSeguroIncorreto() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("CADASTRO DE SIMULAÇÕES ATRAVÉS DO POST");
		
		String simuInterna = "{\r\n"
				+ "  \"nome\": \"Fulano de Tal\",\r\n"
				+ "  \"cpf\": 97093236014,\r\n"
				+ "  \"email\": \"email@email.com\",\r\n"
				+ "  \"valor\": 1200,\r\n"
				+ "  \"parcelas\": 3\r\n"
				+ "}";
		
		response = rest.post(Endpoints.SIMULACAO, simuInterna);
		assertThat(response.statusCode(), is(400));
		assertThat(response.asString(), matchesJsonSchema("simulacoes", "post", 400));
		assertThat(response.jsonPath().getString("erros.seguro"), equalTo("Seguro deve ser True ou False"));
	}
	
	@DisplayName("Teste não cadastra simulação com parcela menor que 2 e valor maior que 40000")
	@Test
	@Description("Teste verifica se o cadastro de simulações é bem sucedido ao enviar dados de valor e parcelas incorretamente do especificado")
	public void naoDeveCadastrarSimulacaoComSucessoERetornarMensagemCorretaDeParcelaBaixaEvalorAlto() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("CADASTRO DE SIMULAÇÕES ATRAVÉS DO POST");
		
		Simulacao simuInterna = DynamicFactory.generateRandomSimulationValorEParcelas(50000, 0);
		
		response = rest.post(Endpoints.SIMULACAO, simuInterna);
		assertThat(response.statusCode(), is(400));
		assertThat(response.asString(), matchesJsonSchema("simulacoes", "post", 400));
		assertThat(response.jsonPath().getString("erros.parcelas"), equalTo("Parcelas deve ser igual ou maior que 2"));
		assertThat(response.jsonPath().getString("erros.valor"), equalTo("Valor deve ser menor ou igual a R$ 40.000"));
	}
	
	@DisplayName("Teste não deve cadastrar com sucesso enviando valor baixo")
	@Test
	@Description("Teste verifica se o cadastro de simulações é bem sucedido ao enviar um valor abaixo do especificado de 1000")
	public void naoDeveCadastrarSimulacaoComSucessoERetornarMensagemCorretaDeValorBaixo() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("CADASTRO DE SIMULAÇÕES ATRAVÉS DO POST");
		
		Simulacao simuInterna = DynamicFactory.generateRandomSimulationValorEParcelas(0, 30);
		
		response = rest.post(Endpoints.SIMULACAO, simuInterna);
		assertThat(response.statusCode(), is(400));
		assertThat(response.asString(), matchesJsonSchema("simulacoes", "post", 400));
		assertThat(response.jsonPath().getString("erros.valor"), equalTo("Valor deve ser maior ou igual a R$ 1.000"));
	}
	
	@DisplayName("Teste não deve cadastrar com sucesso enviando parcela alta")
	@Test
	@Description("Teste verifica se o cadastro de simulações é bem sucedido ao enviar um numero de parcelas acima do especificado de 48")
	public void naoDeveCadastrarSimulacaoComSucessoERetornarMensagemCorretaDeParcelaAlta() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("CADASTRO DE SIMULAÇÕES ATRAVÉS DO POST");
		
		Simulacao simuInterna = DynamicFactory.generateRandomSimulationValorEParcelas(1200, 50);
		
		response = rest.post(Endpoints.SIMULACAO, simuInterna);
		assertThat(response.statusCode(), is(400));
		assertThat(response.asString(), matchesJsonSchema("simulacoes", "post", 400));
		assertThat(response.jsonPath().getString("erros.valor"), equalTo("Parcela deve ser menor ou igual a 48"));
	}
	
	@DisplayName("Teste não deve cadastrar com sucesso enviando nome vazio")
	@Test
	@Description("Teste verifica se o cadastro de simulações é bem sucedido ao deixar de enviar o campo nome na requisição")
	public void naoDeveCadastrarSimulacaoComSucessoEnviandoNomeVazio() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("CADASTRO DE SIMULAÇÕES ATRAVÉS DO POST");
		
		Simulacao simuInterna = DynamicFactory.generateRandomSimulationNome("");
		
		response = rest.post(Endpoints.SIMULACAO, simuInterna);
		assertThat(response.statusCode(), is(400));
		assertThat(response.asString(), matchesJsonSchema("simulacoes", "post", 400));
		assertThat(response.jsonPath().getString("erros.nome"), equalTo("Nome não pode ser vazio"));
	}
	
	@DisplayName("Teste não deve cadastrar com sucesso enviando cpf vazio")
	@Test
	@Description("Teste verifica se o cadastro de simulações é bem sucedido ao não enviar o campo CPF na requisição")
	public void naoDeveCadastrarSimulacaoComSucessoEnviandoCPFVazio() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("CADASTRO DE SIMULAÇÕES ATRAVÉS DO POST");
		
		Simulacao simuInterna = DynamicFactory.generateRandomSimulationCPF("");
		
		response = rest.post(Endpoints.SIMULACAO, simuInterna);
		assertThat(response.statusCode(), is(400));
		assertThat(response.asString(), matchesJsonSchema("simulacoes", "post", 400));
		assertThat(response.jsonPath().getString("erros.nome"), equalTo("Cpf não pode ser vazio"));
	}
	
	@DisplayName("Teste não deve cadastrar com sucesso enviando cpf curto")
	@Test
	@Description("Teste verifica se o cadastro de simulações é bem sucedido ao enviar o campo CPF com apenas 2 digitos na requisição")
	public void naoDeveCadastrarSimulacaoComSucessoEnviandoCPFCurto() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("CADASTRO DE SIMULAÇÕES ATRAVÉS DO POST");
		
		Simulacao simuInterna = DynamicFactory.generateRandomSimulationCPF(faker.number().digits(2));
		
		response = rest.post(Endpoints.SIMULACAO, simuInterna);
		assertThat(response.statusCode(), is(400));
		assertThat(response.asString(), matchesJsonSchema("simulacoes", "post", 400));
		assertThat(response.jsonPath().getString("erros.nome"), equalTo("CPF deve estar em formato correto e ter onze digitos"));
	}
	
	@DisplayName("Teste não deve cadastrar com sucesso enviando cpf com letras")
	@Test
	@Description("Teste verifica se o cadastro de simulações é bem sucedido ao enviar o campo CPF contendo letras na requisição")
	public void naoDeveCadastrarSimulacaoComSucessoEnviandoCPFComLetras() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("CADASTRO DE SIMULAÇÕES ATRAVÉS DO POST");
		
		Simulacao simuInterna = DynamicFactory.generateRandomSimulationCPF(faker.number().digits(11) + "RandomLetter");
		
		response = rest.post(Endpoints.SIMULACAO, simuInterna);
		assertThat(response.statusCode(), is(400));
		assertThat(response.asString(), matchesJsonSchema("simulacoes", "post", 400));
		assertThat(response.jsonPath().getString("erros.nome"), equalTo("CPF deve estar em formato correto e ter onze digitos"));
	}
	
	@DisplayName("Teste não deve cadastrar com sucesso enviando cpf repetido")
	@Test
	@Description("Teste verifica se o cadastro de simulações é bem sucedido ao enviar no campo CPF um número previamente cadastrado na requisição")
	public void naoDeveCadastrarSimulacaoComSucessoEnviandoCPFRepetido() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("CADASTRO DE SIMULAÇÕES ATRAVÉS DO POST");
		
		Simulacao simuInterna = DynamicFactory.generateRandomSimulationCPF(cpfCadastrado);
		
		response = rest.post(Endpoints.SIMULACAO, simuInterna);
		assertThat(response.statusCode(), not(201));
	}
	
	@DisplayName("Teste não deve cadastrar com sucesso enviando cpf repetido e retorna status code correto")
	@Test
	@Description("Teste verifica se o cadastro de simulações é bem sucedido ao enviar no campo CPF um número previamente cadastrado na requisição e verifica o status code retornado que deve ser 409")
	public void naoDeveCadastrarSimulacaoComSucessoEnviandoCPFRepetidoERetornarStatusCodeCorreto() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("CADASTRO DE SIMULAÇÕES ATRAVÉS DO POST");
		
		Simulacao simuInterna = DynamicFactory.generateRandomSimulationCPF(cpfCadastrado);
		
		response = rest.post(Endpoints.SIMULACAO, simuInterna);
		assertThat(response.statusCode(), is(409));
	}
	
	@DisplayName("Teste não deve cadastrar com sucesso enviando cpf repetido e retorna mensagem correta")
	@Test
	@Description("Teste verifica se o cadastro de simulações é bem sucedido ao enviar no campo CPF um número previamente cadastrado na requisição e verifica se a mensagem retonada está de acordo com a documentação")
	public void naoDeveCadastrarSimulacaoComSucessoEnviandoCPFRepetidoERetornarMensagemCorreta() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("CADASTRO DE SIMULAÇÕES ATRAVÉS DO POST");
		
		Simulacao simuInterna = DynamicFactory.generateRandomSimulationCPF(cpfCadastrado);
		
		response = rest.post(Endpoints.SIMULACAO, simuInterna);
		assertThat(response.jsonPath().getString("mensagem"), equalTo("CPF já existente"));
	}
	
	@DisplayName("Teste não deve atualizar um cadastro através do post")
	@Test
	@Description("Teste verifica se o POST de simulações pode ser utilizado para atualizar um cadastro no sistema")
	public void naoDeveAtualizarSimulacaoAtravesDoPost() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("CADASTRO DE SIMULAÇÕES ATRAVÉS DO POST");
		
		SimulacaoID simuInterna = simulacaoServiceID.getSimulacao(cpfCadastrado);
		
		simuInterna.setNome(faker.name().fullName());
		simuInterna.setCpf(faker.number().digits(11));
		simuInterna.setEmail(faker.internet().emailAddress());
		simuInterna.setValor(faker.number().randomDouble(2, 1000, 40000));
		simuInterna.setParcelas(faker.number().numberBetween(2, 48));
		simuInterna.setSeguro(faker.bool().bool());
		
		response = rest.post(Endpoints.SIMULACAO, simuInterna);
		assertThat(response.statusCode(), not(201));
	}
	
	@DisplayName("Teste não deve cadastrar sem enviar nenhum dado")
	@Test
	@Description("Teste verifica se o o cadastro é possível sem enviar nenhum dado no body")
	public void naoDeveCadastrarSemEnviarNenhumBody() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("CADASTRO DE SIMULAÇÕES ATRAVÉS DO POST");

		response = rest.post(Endpoints.SIMULACAO, "");
		assertThat(response.statusCode(), is(400));
	}
	
	@DisplayName("Teste não deve cadastrar sem enviar nenhum dado e devolver mensagem de erro correta")
	@Test
	@Description("Teste verifica se o o cadastro é possível sem enviar nenhum dado no body e verifica se a mensagem de resposta está de acordo com a documentação")
	public void naoDeveCadastrarSemEnviarNenhumBodyEDevolverMensagemDeErroCorreta() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("CADASTRO DE SIMULAÇÕES ATRAVÉS DO POST");
		
        Simulacao simuInterna = new Simulacao();

		response = rest.post(Endpoints.SIMULACAO, simuInterna);
		assertThat(response.asString(), matchesJsonSchema("simulacoes", "post", 400));
		assertThat(response.jsonPath().getString("erros.nome"), equalTo("Nome não pode ser vazio"));
		assertThat(response.jsonPath().getString("erros.cpf"), equalTo("CPF não pode ser vazio"));
		assertThat(response.jsonPath().getString("erros.email"), equalTo("E-mail não deve ser vazio"));
		assertThat(response.jsonPath().getString("erros.valor"), equalTo("Valor não pode ser vazio"));
		assertThat(response.jsonPath().getString("erros.parcelas"), equalTo("Parcelas deve ser igual ou maior que 2"));
	}
	
	@DisplayName("Teste não deve cadastrar sem enviar nenhum dado e devolver mensagem correta de seguro")
	@Test
	@Description("Teste verifica se o o cadastro é possível sem enviar nenhum dado no body e verifica se a mensagem de resposta informa que o seguro não pode ficar vazio")
	public void naoDeveCadastrarSemEnviarNenhumBodyEDevolverMensagemDeErroCorretaDoSeguro() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("CADASTRO DE SIMULAÇÕES ATRAVÉS DO POST");
		
        Simulacao simuInterna = new Simulacao();

		response = rest.post(Endpoints.SIMULACAO, simuInterna);
		assertThat(response.jsonPath().getString("erros.seguro"), equalTo("Seguro não pode ser vazio"));
	}
	
	@DisplayName("Teste não deve cadastrar com CPF que possui restrição")
	@Test
	@Description("Teste verifica se o o cadastro é possível com CPF que possui restrição")
	public void naoDeveCadastrarSimulacaoComSucessoEnviandoCPFQuePossuiRestricao() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("CADASTRO DE SIMULAÇÕES ATRAVÉS DO POST");
		
		String message;
		
		for (String cpf : cpfsComRestricao) {
			Simulacao simuInterna = DynamicFactory.generateRandomSimulationCPF(cpf);
			response = rest.post(Endpoints.SIMULACAO, simuInterna);
			message = "O CPF " + cpf + " possui restrição";
			assertThat(response.statusCode(), not(201));
			assertThat(response.jsonPath().getString("mensagem"), equalTo(message));
		}
	}
	
	@DisplayName("Teste não deve cadastrar simulação com ID incorreto")
	@Test
	@Description("Teste verifica se o o cadastro é possível após dois erros de cadastro no sistema e verifica se o ID criado pela API é consistente e segue a ordem correta")
	public void naoDeveCadastrarComIdErrado() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("CADASTRO DE SIMULAÇÕES ATRAVÉS DO POST");
		
		response = rest.get(Endpoints.SIMULACAO);
		List<Integer> idSimu = response.jsonPath().getList("id");
		int ultimoId = idSimu.get(idSimu.size() -1);
		Simulacao simuInterna = DynamicFactory.generateRandomSimulation();
		
		Simulacao simuInternaErro = DynamicFactory.generateRandomSimulationCPF(cpfCadastrado);
		
		rest.post(Endpoints.SIMULACAO, simuInternaErro);
		rest.post(Endpoints.SIMULACAO, simuInternaErro);
		response = rest.post(Endpoints.SIMULACAO, simuInterna);
		
		assertThat(response.jsonPath().getInt("id"), equalTo(ultimoId + 1));
	}
}
