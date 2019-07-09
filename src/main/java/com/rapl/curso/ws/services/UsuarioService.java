package com.rapl.curso.ws.services;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rapl.curso.ws.domain.Regras;
import com.rapl.curso.ws.domain.Usuario;
import com.rapl.curso.ws.domain.VerificacaoToken;
import com.rapl.curso.ws.dto.UsuarioDTO;
import com.rapl.curso.ws.repository.RegrasRepository;
import com.rapl.curso.ws.repository.UsuarioRepository;
import com.rapl.curso.ws.repository.VerificacaoTokenRepository;
import com.rapl.curso.ws.security.utils.PasswordUtils;
import com.rapl.curso.ws.services.email.Mailer;
//import com.rapl.curso.ws.services.email.EmailService;
import com.rapl.curso.ws.services.exception.ObjetoJaExistenteException;
import com.rapl.curso.ws.services.exception.ObjetoNaoEncontradoException;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private RegrasRepository roleRepository;

	@Autowired
	private VerificacaoTokenRepository verificacaoTokenRepository;

//	@Autowired
//	private EmailService emailService;
	
	@Autowired
	private Mailer mailer;

	public List<Usuario> findAll() {
		return usuarioRepository.findAll();
	}

	public Usuario findById(String id) {
		Optional<Usuario> user = usuarioRepository.findById(id);
		return user.orElseThrow(() -> new ObjetoNaoEncontradoException("Objeto não encontrado!"));
	}

	public Usuario fromDTO(UsuarioDTO usuarioDTO) {
		return new Usuario(usuarioDTO);
	}

	public Usuario create(Usuario usuario) {
		usuario.setPassword(PasswordUtils.gerarBCrypt(usuario.getPassword()));

		List<Regras> regras = usuario.getRoles();
		regras.forEach(r -> {
			Regras regra = roleRepository.findByName(r.getName())
					.orElseThrow(() -> new ObjetoNaoEncontradoException("Regra não encontrada!"));
			r.setId(regra.getId());
		});

		usuario.setRoles(regras);
		return usuarioRepository.save(usuario);
	}

	public Usuario update(Usuario usuario) {
		Optional<Usuario> updateUser = usuarioRepository.findById(usuario.getId());
		return updateUser
				.map(u -> usuarioRepository.save(new Usuario(u.getId(), usuario.getFirstName(), usuario.getLastName(),
						usuario.getEmail(), usuario.getPassword(), usuario.isEnabled())))
				.orElseThrow(() -> new ObjetoNaoEncontradoException("Usuário não encontrado!"));
	}

	public void delete(String id) {
		usuarioRepository.deleteById(id);
	}

	public Usuario registrarUsuario(Usuario usuario) {
		if (emailExist(usuario.getEmail())) {
			throw new ObjetoJaExistenteException(String.format("Já extiste uma conta com esse endereço de email"));
		}
		usuario.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER").get()));
		usuario.setEnabled(false);
		usuario = this.create(usuario);
		this.mailer.enviarEmailRequisicaoConfirmacao(usuario, null);
		return usuario;
	}

	private boolean emailExist(final String email) {
		Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
		if (usuario.isPresent()) {
			return true;
		}
		return false;
	}

	public void criarVerificacaoTokenParaUsuario(Usuario usuario, String token) {
		final VerificacaoToken vToken = new VerificacaoToken(token, usuario);
		verificacaoTokenRepository.save(vToken);
	}

	public String validateVerificationToken(String token) {
		final Optional<VerificacaoToken> vToken = verificacaoTokenRepository.findByToken(token);
		if (!vToken.isPresent()) {
			return "invalidToken";
		}
		final Usuario usuario = vToken.get().getUsuario();
		final Calendar cal = Calendar.getInstance();
		if ((vToken.get().getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			return "expired";
		}
		usuario.setEnabled(true);
		this.usuarioRepository.save(usuario);
		return null;
	}

	public Usuario findByEmail(String email) {
		Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
		return usuario.orElseThrow(() -> new ObjetoNaoEncontradoException(String.format("Usuário não encontrado!")));
	}

	public VerificacaoToken generateNewVerificationToken(String email) {
		Usuario user = findByEmail(email);
		Optional<VerificacaoToken> vToken = verificacaoTokenRepository.findByUsuario(user);
		vToken.get().updateToken(UUID.randomUUID().toString());
		VerificacaoToken updateVToken = verificacaoTokenRepository.save(vToken.get());
		mailer.enviarEmailRequisicaoConfirmacao(user, updateVToken);
		return updateVToken;
	}
}
