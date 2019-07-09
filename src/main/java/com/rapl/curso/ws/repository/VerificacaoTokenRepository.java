package com.rapl.curso.ws.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rapl.curso.ws.domain.Usuario;
import com.rapl.curso.ws.domain.VerificacaoToken;

public interface VerificacaoTokenRepository extends MongoRepository<VerificacaoToken, String>{

	Optional<VerificacaoToken> findByToken(String token);
	
	Optional<VerificacaoToken> findByUsuario(Usuario usuario);
}
