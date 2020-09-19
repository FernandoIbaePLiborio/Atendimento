package com.gps.atuacao.enums;

public enum TipoAtuacao {
	
	DESENVOLVIMENTO_GPS("Desenvolvimento GPS"),
	DESENVOLVIMENTO_URA("Desenvolvimento URA"),
	FUNCIONAL("Funcional"),
	AMBOS("Ambos"),
	URA("URA"),
	ATENDIMENTO("Atendimento"),
	OUTRAS_ESQUIPES("Outras Equipes");

	private String valor;

	private TipoAtuacao(final String valor) {

		this.valor = valor;
	}

	public String getValor() {

		return this.valor;
	}

}

