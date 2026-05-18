package datafactory;

import com.github.javafaker.Faker;

import models.Simulacao;

public class DynamicFactory {
	private static Faker faker = new Faker();
	
	public static Simulacao generateRandomSimulation() {
		Simulacao simulacao = new Simulacao();
		
		simulacao.setNome(faker.name().fullName());
		simulacao.setCpf(faker.number().digits(11));
		simulacao.setEmail(faker.internet().emailAddress());
		simulacao.setValor(faker.number().randomDouble(2, 1000, 40000));
		simulacao.setParcelas(faker.number().numberBetween(2, 48));
		simulacao.setSeguro(faker.bool().bool());
		
		return simulacao;
	}
	
	public static Simulacao generateRandomSimulationValorEParcelas(Number valor, int parcelas) {
		Simulacao simulacao = new Simulacao();
		
		simulacao.setNome(faker.name().fullName());
		simulacao.setCpf(faker.number().digits(11));
		simulacao.setEmail(faker.internet().emailAddress());
		simulacao.setValor(valor);
		simulacao.setParcelas(parcelas);
		simulacao.setSeguro(faker.bool().bool());
		
		return simulacao;
	}
	
	public static Simulacao generateRandomSimulationCPF(String cpf) {
		Simulacao simulacao = new Simulacao();
		
		simulacao.setNome(faker.name().fullName());
		simulacao.setCpf(cpf);
		simulacao.setEmail(faker.internet().emailAddress());
		simulacao.setValor(faker.number().randomDouble(2, 1000, 40000));
		simulacao.setParcelas(faker.number().numberBetween(2, 48));
		simulacao.setSeguro(faker.bool().bool());
		
		return simulacao;
	}
	
	public static Simulacao generateRandomSimulationSeguro(Boolean seguro) {
		Simulacao simulacao = new Simulacao();
		
		simulacao.setNome(faker.name().fullName());
		simulacao.setCpf(faker.number().digits(11));
		simulacao.setEmail(faker.internet().emailAddress());
		simulacao.setValor(faker.number().randomDouble(2, 1000, 40000));
		simulacao.setParcelas(faker.number().numberBetween(2, 48));
		simulacao.setSeguro(seguro);
		
		return simulacao;
	}
	
	public static Simulacao generateRandomSimulationNome(String nome) {
		Simulacao simulacao = new Simulacao();
		
		simulacao.setNome(nome);
		simulacao.setCpf(faker.number().digits(11));
		simulacao.setEmail(faker.internet().emailAddress());
		simulacao.setValor(faker.number().randomDouble(2, 1000, 40000));
		simulacao.setParcelas(faker.number().numberBetween(2, 48));
		simulacao.setSeguro(faker.bool().bool());
		
		return simulacao;
	}
	
}
