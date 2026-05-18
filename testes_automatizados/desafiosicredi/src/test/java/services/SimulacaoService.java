package services;

import java.util.List;

import constants.Endpoints;
import models.Simulacao;

public class SimulacaoService extends BaseRest{
	
	public Simulacao getSimulacao(String cpf) {
		return getCpf(Endpoints.SIMULACAO_CPF, cpf).as(Simulacao.class);
	}
	
	public List<Simulacao> getSimulacoes() {
		return get(Endpoints.SIMULACAO).jsonPath().getList("$", Simulacao.class);
	}
	
}
