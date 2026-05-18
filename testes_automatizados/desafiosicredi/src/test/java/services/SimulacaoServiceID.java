package services;

import java.util.List;

import constants.Endpoints;

import models.SimulacaoID;

public class SimulacaoServiceID extends BaseRestID {
	
	public SimulacaoID getSimulacao(String cpf) {
		return getCpf(Endpoints.SIMULACAO_CPF, cpf).as(SimulacaoID.class);
	}
	
	public List<SimulacaoID> getSimulacoes() {
		return get(Endpoints.SIMULACAO).jsonPath().getList("$", SimulacaoID.class);
	}
	
}
