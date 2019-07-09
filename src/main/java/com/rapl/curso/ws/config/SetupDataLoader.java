package com.rapl.curso.ws.config;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import com.rapl.curso.ws.domain.Regras;
import com.rapl.curso.ws.domain.Usuario;
import com.rapl.curso.ws.repository.RegrasRepository;
import com.rapl.curso.ws.repository.UsuarioRepository;
import com.rapl.curso.ws.repository.VerificacaoTokenRepository;
import com.rapl.curso.ws.security.utils.PasswordUtils;

// Esta classe é inicializada TODA vez que se inicia o Spring Boot (a aplicação)

@Configuration
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
    UsuarioRepository usuarioRepository;
	
	@Autowired
	RegrasRepository regrasRepository;
	
	@Autowired
    private VerificacaoTokenRepository verificacaoTokenRepository;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		
		regrasRepository.deleteAll();
		usuarioRepository.deleteAll();
		verificacaoTokenRepository.deleteAll();
		
		Regras roleAdmin = createRegrasIfNotFound("ROLE_ADMIN");
        Regras roleUser = createRegrasIfNotFound("ROLE_USER");

        Usuario joao = new Usuario("João", "Luiz", "joao@gmail.com");
        Usuario maria = new Usuario("Maria", "Carla", "maria@gmail.com");
                
        joao.setRoles(Arrays.asList(roleAdmin));
        joao.setPassword(PasswordUtils.gerarBCrypt("123"));
        joao.setEnabled(true);
        
        maria.setRoles(Arrays.asList(roleUser));
        maria.setPassword(PasswordUtils.gerarBCrypt("123"));
        maria.setEnabled(true);

        createUserIfNotFound(joao);
        createUserIfNotFound(maria);		
	}
	
	private Usuario createUserIfNotFound(final Usuario user) {
        Optional<Usuario> obj = usuarioRepository.findByEmail(user.getEmail());
        if(obj.isPresent()) {
            return obj.get();
        }
        return usuarioRepository.save(user);
    }

	private Regras createRegrasIfNotFound(String name){
		Optional<Regras> regras = regrasRepository.findByName(name);
		if (regras.isPresent()){
			return regras.get();
		}
		return regrasRepository.save(new Regras(name));
	}

}
