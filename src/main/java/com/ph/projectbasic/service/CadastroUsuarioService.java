package com.ph.projectbasic.service;

import java.io.Serializable;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.ph.projectbasic.model.Usuario;
import com.ph.projectbasic.repository.Usuarios;
import com.ph.projectbasic.util.jpa.Transactional;

// Classe de serviço Usuario

public class CadastroUsuarioService implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private Usuarios usuarios;
	
	@Transactional
	public Usuario salvar(Usuario usuario) throws NegocioException {
		
		if (usuario.getId() != null && StringUtils.isBlank(usuario.getSenha())) {
			usuario.setSenha(usuarios.buscarSenha(usuario.getId()));
		}
		
		return usuarios.guardar(usuario);
	}
}
