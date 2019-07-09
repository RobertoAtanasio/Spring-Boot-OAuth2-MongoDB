package com.rapl.curso.ws.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rapl.curso.ws.domain.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
	
	Optional<Usuario> findByEmail (String email);
}
