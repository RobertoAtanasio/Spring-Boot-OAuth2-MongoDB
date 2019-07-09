package com.rapl.curso.ws.services.exception;

import java.io.Serializable;

public class ObjetoNaoDisponivelException extends RuntimeException implements Serializable {

	private static final long serialVersionUID = 1L;

    public ObjetoNaoDisponivelException(String mensagem) {
        super(mensagem);
    }
}
