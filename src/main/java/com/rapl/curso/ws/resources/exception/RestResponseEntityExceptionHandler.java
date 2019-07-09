package com.rapl.curso.ws.resources.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.rapl.curso.ws.services.exception.ObjetoJaExistenteException;
import com.rapl.curso.ws.services.exception.ObjetoNaoDisponivelException;
import com.rapl.curso.ws.services.exception.ObjetoNaoEncontradoException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	// error 404
	@ExceptionHandler(ObjetoNaoEncontradoException.class)
	public ResponseEntity<StandardError> objectNotFound(ObjetoNaoEncontradoException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError err = new StandardError(System.currentTimeMillis(), status.value(), "NotFound",
				e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	// error 409
	@ExceptionHandler({ ObjetoJaExistenteException.class })
    public ResponseEntity<Object> handleObjetoJaExistente(final RuntimeException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError err = new StandardError(System.currentTimeMillis(), status.value(), "UserAlreadyExist", 
        		e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    // error 401
    @ExceptionHandler(value = {ObjetoNaoDisponivelException.class})
    public ResponseEntity<Object> handleObjetoNaoDisponivel(final RuntimeException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        StandardError err = new StandardError(System.currentTimeMillis(), status.value(), "UserNotEnabled", 
        		e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
    
}
