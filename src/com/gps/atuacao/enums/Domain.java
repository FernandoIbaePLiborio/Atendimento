package com.gps.atuacao.enums;

public enum Domain {
	
	PJ_ATENDIMENTO_B2C("qa_qa_20_pj_ab2c_db"),
	REGRESSAO("qa_qa_20_reg2_db"),
	PJ_DIGITAL("qa_qa_20_dig_db"),
	SUSTENTACAO("qa_qa_20_sust_db"),
	PP_FIXA_MOVEL("qa_qa_20_pp_fmc_db"),
	PJ_TRANSF_B2C("qa_qa_20_tb2c_db"),
	PJ_OSS_B2C_FIXA("qa_qa_20_eofb2c_db"),
	PJ_EST_ONGOING_B2C("qa_qa_20_ob2c_db"),
	PJ_OG_B2B_FINANCAS("qa_qa_20_tb2b_db"),
	PJ_TRANSF_B2B_APOIO("qa_qa_2020_pj_transf_b2b_apoio"),
	AMDOCS_E2E("dev_amdocs_e2e_db");
	
	private String valor;

	private Domain(final String valor) {

		this.valor = valor;
	}

	public String getValor() {

		return this.valor;
	}

}
