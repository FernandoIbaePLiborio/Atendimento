package com.gps.atuacao.service.exception;

import br.com.gvt.jeemodelinfra.exception.BusinessException;

public class ExternalBusinessException extends BusinessException {

	private static final long serialVersionUID = 7192276668835803549L;

	public ExternalBusinessException(String message) {

		super(message);
	}

	public ExternalBusinessException(String codigo, String menssagem) {

		super(codigo, menssagem);
	}

	public ExternalBusinessException(String message, Throwable cause) {

		super(message, cause);
	}

	public ExternalBusinessException(Throwable cause) {

		super(cause);
	}

}