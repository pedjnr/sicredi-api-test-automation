package helper;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import com.github.javafaker.Faker;

import constants.Endpoints;
import datafactory.DynamicFactory;
import io.restassured.response.Response;
import models.Simulacao;
import models.SimulacaoID;
import services.BaseRest;
import services.BaseRestID;
import services.SimulacaoService;
import services.SimulacaoServiceID;

public class BaseTest {
	
	protected static Faker faker = new Faker();
	protected static TxtFileReader leitorCpfs = new TxtFileReader();
	protected static List<String> cpfsComRestricao = leitorCpfs.lerArquivo("cpfs");
	protected static BaseRest rest;
	protected static BaseRestID restID;
	protected static SimulacaoService simulacaoService;
	protected static SimulacaoServiceID simulacaoServiceID;
	protected static Random rand;
	protected static Simulacao simulation;
	protected static SimulacaoID simulationID;
	protected static Response response;
	protected static String cpfCadastrado;
	protected static String idSimulacao;
	
	@BeforeAll
	public static void setUp() {
		rest = new BaseRest();
		restID = new BaseRestID();
		rand = new Random();
		simulacaoService = new SimulacaoService();
		simulacaoServiceID = new SimulacaoServiceID();
		Collections.shuffle(cpfsComRestricao);
	}
	
	
	//CADASTRA SIMULAÇÕES PARA USAR NOS TESTES
	public static void cadastraSimulacao() {
		simulation = DynamicFactory.generateRandomSimulation();
		
		cpfCadastrado = simulation.getCpf();
		
		response = rest.post(Endpoints.SIMULACAO, simulation);
		
		idSimulacao = response.jsonPath().getString("id");
	}
	
	//APAGA TODAS AS SIMULAÇÕES
	public static void deletaSimulacoes() {
		String idEscolhido;
		
		response = rest.get(Endpoints.SIMULACAO);
		List<String> idSimu = response.jsonPath().getList("id");
		
		if (idSimu.size() != 0) {
			do {
				idEscolhido = String.valueOf(idSimu.get(rand.nextInt(idSimu.size())));
				rest.delete(Endpoints.SIMULACAO_DELETE, idEscolhido);
				response = rest.get(Endpoints.SIMULACAO);
				idSimu = response.jsonPath().getList("id");
			}
			while (idSimu.size() != 0);
		}
	}
	
	
	@BeforeEach
	public void cadastraTudo() {
		cadastraSimulacao();
	}
	
	@AfterAll
	public static void limpaTudo() {
		deletaSimulacoes();
	}
}
