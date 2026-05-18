package models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.Data;

@Data
public class SimulacaoID {
	private int id;
	private String nome;
	private String cpf;
	private String email;
	private Number valor;
	private int parcelas;
	@JsonFormat(shape = Shape.STRING)
	private boolean seguro;
}
