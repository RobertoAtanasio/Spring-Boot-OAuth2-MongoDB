package com.rapl.curso.ws.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class VerificacaoToken implements Serializable {

	private static final long serialVersionUID = 1L;

	private int EXPIRATION = 60*24;
	private int EXPIRATION_LOCAL = 24;
	
	@Id
    private String id;
    private String token;

    @DBRef(lazy = true)
    private Usuario usuario;
    private Date expiryDate;
    private LocalDateTime expiryDateLocal; 
    
	public VerificacaoToken() {}
	
	public VerificacaoToken(String token) {
        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
        this.expiryDateLocal = this.calcularDataExpriracao(EXPIRATION_LOCAL);
    }

    public VerificacaoToken(String token, Usuario usuario) {
        this.token = token;
        this.usuario = usuario;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
        this.expiryDateLocal = this.calcularDataExpriracao(EXPIRATION_LOCAL);
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	public LocalDateTime getExpiryDateLocal() {
		return expiryDateLocal;
	}

	public void setExpiryDateLocal(LocalDateTime expiryDateLocal) {
		this.expiryDateLocal = expiryDateLocal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VerificacaoToken other = (VerificacaoToken) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	private Date calculateExpiryDate(int expiryTimeInMinutes) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }
	
	private LocalDateTime calcularDataExpriracao (int expiryTimeInMinutes) {
		LocalDateTime dataLocal = LocalDateTime.now();
        dataLocal = dataLocal.plusHours(expiryTimeInMinutes);
        return dataLocal;
	}
    
	public void updateToken(final String token) {
        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }
}
