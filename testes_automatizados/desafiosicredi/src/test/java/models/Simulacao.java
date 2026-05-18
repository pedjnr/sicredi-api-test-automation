package models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

@Data
public class Simulacao {
	@JsonProperty(value = "id", access = Access.WRITE_ONLY)
	private int id;
	private String nome;
	private String cpf;
	private String email;
	private Number valor;
	private int parcelas;
	@JsonFormat(shape = Shape.STRING)
	private boolean seguro;
}
