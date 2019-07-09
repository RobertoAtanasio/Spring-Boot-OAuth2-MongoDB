package com.rapl.curso.ws.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rapl.curso.ws.domain.Regras;
import com.rapl.curso.ws.repository.RegrasRepository;

@Service
public class RegrasService {

	@Autowired
	RegrasRepository regrasRepository;
	
	public List<Regras> findAll() {
		return regrasRepository.findAll();
	}
}
