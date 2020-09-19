package com.gps.atuacao.enums;

public enum Equipe {
	
	GPS("GPS"),
	URA("URA"),
	ATENDIMENTO("Atendimento");
	
	private String valor;

	private Equipe(final String valor) {

		this.valor = valor;
	}

	public String getValor() {

		return this.valor;
	}

}
