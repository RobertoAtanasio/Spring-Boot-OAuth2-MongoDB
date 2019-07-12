package com.rapl.curso.ws.resources;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rapl.curso.ws.domain.Regras;
import com.rapl.curso.ws.domain.Usuario;
import com.rapl.curso.ws.dto.UsuarioDTO;
import com.rapl.curso.ws.services.UsuarioService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = { "UsuarioResource" })
@RestController
@RequestMapping("/api")
public class UsuarioResource {

	@Autowired
    private UsuarioService usuarioService;
	
	TokenStore tokenStore = new InMemoryTokenStore();

    @Autowired
    DefaultTokenServices tokenServices = new DefaultTokenServices();
	
	@GetMapping("/users")
    public ResponseEntity<List<UsuarioDTO>> findAll() {
//        List<User> users = new ArrayList<>();
//        User joao = new User("João", "Souza", "joao@gmail.com");
//        User maria = new User("Maria", "Teixeira", "maria@gmail.com");
//        users.addAll(Arrays.asList(joao, maria));
//        return ResponseEntity.ok().body(users);
		
		List<Usuario> usarios = usuarioService.findAll();
		List<UsuarioDTO> listDTO = usarios.stream().map(x -> new UsuarioDTO(x)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDTO);
    }
	
	@ApiOperation("Retorna um especifico usuário através do seu identificador.")
	@GetMapping("/users/{id}")
    public ResponseEntity<UsuarioDTO> findById(
    		@ApiParam("Id do usuário não pode ser vazio.")
    		@PathVariable String id) {
		Usuario usuario = usuarioService.findById(id);
		return ResponseEntity.ok().body(new UsuarioDTO(usuario));
    }
	
	@ApiOperation("Cria e retorna o usuário inserido na base.")
	@PostMapping("/users")
    public ResponseEntity<UsuarioDTO> create(@RequestBody UsuarioDTO userDTO) {
		Usuario usuario = usuarioService.fromDTO(userDTO);
        return ResponseEntity.ok().body(new UsuarioDTO(usuarioService.create(usuario)));
    }
	
	@PutMapping("/users/{id}")
    public ResponseEntity<UsuarioDTO> update(@PathVariable String id, @RequestBody UsuarioDTO userDTO) {
		userDTO.setId(id);
		Usuario usuario = usuarioService.update(new Usuario(userDTO));
        return ResponseEntity.ok().body(new UsuarioDTO(usuario));
    }
	
	@DeleteMapping("/users/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

	@GetMapping("/users/{id}/roles")
    public ResponseEntity<List<Regras>> findRoles(@PathVariable String id ){
		Usuario usuario = usuarioService.findById(id);
        return ResponseEntity.ok().body(usuario.getRoles());
    }
	
	@GetMapping(value="/users/main")
    public ResponseEntity<UsuarioDTO> getUserMain(Principal principal){
        Usuario usuario = this.usuarioService.findByEmail(principal.getName());
        UsuarioDTO usuarioDTO = new UsuarioDTO(usuario);
        usuarioDTO.setPassword("");
        return ResponseEntity.ok().body(usuarioDTO);
    }
	
	@GetMapping(value = "/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String tokenValue = authHeader.replace("Bearer", "").trim();
            OAuth2AccessToken accessToken = tokenServices.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);
            tokenServices.revokeToken(String.valueOf(accessToken));
        }
        return ResponseEntity.noContent().build();
    }
}
