package datafactory;

import com.github.javafaker.Faker;

import models.SimulacaoID;

public class DynamicFactoryID {
	
private static Faker faker = new Faker();
	
	public static SimulacaoID generateRandomSimulation() {
		SimulacaoID simulacao = new SimulacaoID();
		
		simulacao.setNome(faker.name().fullName());
		simulacao.setCpf(faker.number().digits(11));
		simulacao.setEmail(faker.internet().emailAddress());
		simulacao.setValor(faker.number().randomDouble(2, 1000, 40000));
		simulacao.setParcelas(faker.number().numberBetween(2, 48));
		simulacao.setSeguro(faker.bool().bool());
		
		return simulacao;
	}
	
	public static SimulacaoID generateRandomSimulationValorEParcelas(Number valor, int parcelas) {
		SimulacaoID simulacao = new SimulacaoID();
		
		simulacao.setNome(faker.name().fullName());
		simulacao.setCpf(faker.number().digits(11));
		simulacao.setEmail(faker.internet().emailAddress());
		simulacao.setValor(valor);
		simulacao.setParcelas(parcelas);
		simulacao.setSeguro(faker.bool().bool());
		
		return simulacao;
	}
	
	public static SimulacaoID generateRandomSimulationCPF(String cpf) {
		SimulacaoID simulacao = new SimulacaoID();
		
		simulacao.setNome(faker.name().fullName());
		simulacao.setCpf(cpf);
		simulacao.setEmail(faker.internet().emailAddress());
		simulacao.setValor(faker.number().randomDouble(2, 1000, 40000));
		simulacao.setParcelas(faker.number().numberBetween(2, 48));
		simulacao.setSeguro(faker.bool().bool());
		
		return simulacao;
	}
	
	public static SimulacaoID generateRandomSimulationSeguro(Boolean seguro) {
		SimulacaoID simulacao = new SimulacaoID();
		
		simulacao.setNome(faker.name().fullName());
		simulacao.setCpf(faker.number().digits(11));
		simulacao.setEmail(faker.internet().emailAddress());
		simulacao.setValor(faker.number().randomDouble(2, 1000, 40000));
		simulacao.setParcelas(faker.number().numberBetween(2, 48));
		simulacao.setSeguro(seguro);
		
		return simulacao;
	}
	
	public static SimulacaoID generateRandomSimulationNome(String nome) {
		SimulacaoID simulacao = new SimulacaoID();
		
		simulacao.setNome(nome);
		simulacao.setCpf(faker.number().digits(11));
		simulacao.setEmail(faker.internet().emailAddress());
		simulacao.setValor(faker.number().randomDouble(2, 1000, 40000));
		simulacao.setParcelas(faker.number().numberBetween(2, 48));
		simulacao.setSeguro(faker.bool().bool());
		
		return simulacao;
	}
	
}
