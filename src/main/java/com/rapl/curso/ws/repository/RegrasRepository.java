package com.rapl.curso.ws.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rapl.curso.ws.domain.Regras;

public interface RegrasRepository extends MongoRepository<Regras, String>{
	
	Optional<Regras> findByName(String id);
}
