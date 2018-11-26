package com.ph.projectbasic.filter;

import java.io.Serializable;

//Filtro para pesquisa de Usuario

public class UsuarioFilter implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String nome;
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
}