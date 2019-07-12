package com.rapl.curso.ws.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.rapl.curso.ws.domain.Regras;
import com.rapl.curso.ws.domain.Usuario;

public class UsuarioDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String firstName;
	private String lastName;
	private String email;
	
	private String password;
	private boolean enabled;
	
	private List<Regras> roles = new ArrayList<>();

	public UsuarioDTO() {}

	public UsuarioDTO(Usuario usuario) {
		this.id = usuario.getId();
        this.firstName = usuario.getFirstName();
        this.lastName = usuario.getLastName();
        this.email = usuario.getEmail();
        this.enabled = usuario.isEnabled();
        this.roles = usuario.getRoles();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<Regras> getRoles() {
		return roles;
	}

	public void setRoles(List<Regras> roles) {
		this.roles = roles;
	}
}
