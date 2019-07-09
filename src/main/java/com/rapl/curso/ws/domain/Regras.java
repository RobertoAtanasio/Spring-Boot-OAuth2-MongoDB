package com.rapl.curso.ws.domain;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

@Document
public class Regras implements Serializable, GrantedAuthority{

	private static final long serialVersionUID = 1L;

	@Id
    private String id;
    private String name;

    public Regras() { }

    public Regras(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
	public String getAuthority() {
		return name;
	}
}
