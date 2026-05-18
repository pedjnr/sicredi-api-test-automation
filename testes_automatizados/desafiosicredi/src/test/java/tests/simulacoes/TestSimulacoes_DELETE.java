package tests.simulacoes;

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

@Epic("SIMULAÇÕES")
@Feature("EXCLUSÃO DE SIMULAÇÕES CADASTRADAS ATRAVÉS DO DELETE")
public class TestSimulacoes_DELETE extends BaseTest {
	
	@DisplayName("Teste apaga uma simulação aleatória")
	@Test
	@Description("Teste realiza um GET e seleciona aleatoriamente uma simulação para fazer o DELETE dela")
	public void deveSelecionarSimulacaoAleatoriaEExcluirSimulacaoEspecifica() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("EXCLUSÃO DE SIMULAÇÕES CADASTRADAS ATRAVÉS DO DELETE");
		
		response = rest.get(Endpoints.SIMULACAO);
		List<String> idSimu = response.jsonPath().getList("id");
		
		String idEscolhido = String.valueOf(idSimu.get(rand.nextInt(idSimu.size())));
		response = rest.delete(Endpoints.SIMULACAO_DELETE, idEscolhido);
		assertThat(response.asString(), equalTo("OK"));
	}
	
	@DisplayName("Teste apaga uma simulação aleatória e verifica o status code")
	@Test
	@Description("Teste realiza um GET e seleciona aleatoriamente uma simulação para fazer o DELETE dela e faz uma verificação se está retornando o status code correto")
	public void deveExcluirSimulacaoRetornandoStatusCodeCorreto() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("EXCLUSÃO DE SIMULAÇÕES CADASTRADAS ATRAVÉS DO DELETE");
		
		response = rest.get(Endpoints.SIMULACAO);
		List<String> idSimu = response.jsonPath().getList("id");
		
		String idEscolhido = String.valueOf(idSimu.get(rand.nextInt(idSimu.size())));
		response = rest.delete(Endpoints.SIMULACAO_DELETE, idEscolhido);
		assertThat(response.statusCode(), is(204));
	}
	
	@DisplayName("Teste não exclui simulação inexistente e verifica status code")
	@Test
	@Description("Teste gera um ID inexistente nas simulações cadastradas e tenta excluir uma simulação através dele, depois verifica se o status code retornado é o especificado na documentação")
	public void naoDeveExcluirSimulacaoInformandoOIdInexistenteStatusCodeCorreto() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("EXCLUSÃO DE SIMULAÇÕES CADASTRADAS ATRAVÉS DO DELETE");
		
		response = rest.get(Endpoints.SIMULACAO);
		List<String> idSimu = response.jsonPath().getList("id");
		
		String idEscolhido;
		
		do {
        	idEscolhido = faker.number().digits(2);
        } while (idSimu.contains(idEscolhido));
		
		response = rest.delete(Endpoints.SIMULACAO_DELETE, idEscolhido);
		assertThat(response.statusCode(), is(404));
		assertThat(response.jsonPath().getString("mensagem"), equalTo("Simulação não encontrada"));
	}
	
	@DisplayName("Teste não exclui simulação inexistente e verifica mensagem de resposta")
	@Test
	@Description("Teste gera um ID inexistente nas simulações cadastradas e tenta excluir uma simulação através dele, depois verifica se a mensagem retornada é a especificada na documentação")
	public void naoDeveExcluirSimulacaoInformandoOIdInexistenteMensagemCorreta() {
		Allure.label("parentSuite", "SIMULAÇÕES");
        Allure.suite("EXCLUSÃO DE SIMULAÇÕES CADASTRADAS ATRAVÉS DO DELETE");
		
		response = rest.get(Endpoints.SIMULACAO);
		List<String> idSimu = response.jsonPath().getList("id");
		
		String idEscolhido;
		
		do {
        	idEscolhido = faker.number().digits(2);
        } while (idSimu.contains(idEscolhido));
		
		response = rest.delete(Endpoints.SIMULACAO_DELETE, idEscolhido);
		assertThat(response.asString(), equalTo("Simulação não encontrada"));

	}
	
}
