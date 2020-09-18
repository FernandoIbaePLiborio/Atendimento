package com.gps.atuacao.enums;

public enum Equipe {
	
	GPS("GPS"),
	URA("URA");
	
	private String valor;

	private Equipe(final String valor) {

		this.valor = valor;
	}

	public String getValor() {

		return this.valor;
	}

}
