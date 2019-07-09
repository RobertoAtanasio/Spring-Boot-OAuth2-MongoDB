package com.rapl.curso.ws.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rapl.curso.ws.domain.Usuario;
import com.rapl.curso.ws.dto.UsuarioDTO;
import com.rapl.curso.ws.resources.util.GenericResponse;
import com.rapl.curso.ws.services.UsuarioService;

@RestController
@RequestMapping("/api/public")
public class RegistrationResource {

	@Autowired
    UsuarioService usuarioService;

    @PostMapping("/registration/users")
    public ResponseEntity<String> registerUser(@RequestBody UsuarioDTO usuarioDTO){
        Usuario usuario = this.usuarioService.fromDTO(usuarioDTO);
        this.usuarioService.registrarUsuario(usuario);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/regitrationConfirm/users")
    public ResponseEntity<GenericResponse> confirmRegistrationUser(@RequestParam("token") String token){
        final Object result = this.usuarioService.validateVerificationToken(token);
        if (result == null) {
            return ResponseEntity.ok().body(new GenericResponse("Sucesso","Usuário liberado para acesso"));
        }
        return ResponseEntity.status(HttpStatus.SEE_OTHER).body(new GenericResponse(result.toString()));
    }
    
    @GetMapping(value = "/resendRegistrationToken/users")
    public ResponseEntity<String> resendRegistrationToken(@RequestParam("email") String email) {
        this.usuarioService.generateNewVerificationToken(email);
        return ResponseEntity.noContent().build();
    }
}
