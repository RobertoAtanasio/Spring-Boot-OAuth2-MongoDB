package com.rapl.curso.ws.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

// package com.rapl.curso.ws.resources.exception;
// @ControllerAdvice
// public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler 
// ... a classe abaixo só funciona se a aplicação for on line, página web. Como é uma aplicação
// REST, a tela error.html não é startada por esta classe, uma vez que a classe ErrorViewResolver
// não é executada.
// Estou deixando o código aqui apenas como forma de exemplo, caso haja a necesidade de alterar para páginas web

@Component
public class MyErrorView implements ErrorViewResolver{

	@Override
	public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> map) {
		
		ModelAndView model = new ModelAndView("error");	// página a ser aberta
		model.addObject("status", status.value());
		
		switch (status.value()) {
			case 404:
				model.addObject("error", "Página não encontrada.");
				model.addObject("message", "A url para a página '" + map.get("path") + "' não existe.");
				break;
			case 500:
				model.addObject("error", "Ocorreu um erro interno no servidor.");
				model.addObject("message", "Ocorreu um erro inesperado, tente mais tarde.");
				break;
			default:
				model.addObject("error", map.get("error"));
				model.addObject("message", map.get("message"));
				break;
		}		
		return model;
	}
}
