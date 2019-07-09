package com.rapl.curso.ws.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rapl.curso.ws.domain.Regras;
import com.rapl.curso.ws.domain.Usuario;
import com.rapl.curso.ws.repository.UsuarioRepository;
import com.rapl.curso.ws.services.exception.ObjetoNaoDisponivelException;
import com.rapl.curso.ws.services.exception.ObjetoNaoEncontradoException;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	UsuarioRepository userRepository;
	
//	@Autowired
//	private MessageSource messageSource;
	
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuario> user = userRepository.findByEmail(email);
        if (!user.isPresent()) {
            throw new ObjetoNaoEncontradoException(String.format("Usuario nao encontrado"));
        } else if (!user.get().isEnabled()) {
            throw new ObjetoNaoDisponivelException(String.format("Usuario nao liberado para acesso"));
        }
        Usuario userLido = user.get();
        return new UsuarioSistema(userLido, getPermissoes(userLido));
    }
    
    private Collection<? extends GrantedAuthority> getPermissoes(Usuario usuario) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		
		List<Regras> roles = usuario.getRoles();
		
		roles.forEach( p -> authorities.add ( new SimpleGrantedAuthority( p.getName() ) ) );
		return authorities;
	}
    
    /**
	 * Obter a mensagem de erro definida no messages.properties
	 * 
	 * @param codigoMensagem
	 * @return String
	 */
//	private String getMensagem(String codigoMensagem) {
//		return messageSource.getMessage(codigoMensagem, null, LocaleContextHolder.getLocale());
//	}
}
