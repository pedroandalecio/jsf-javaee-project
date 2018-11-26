package com.ph.projectbasic.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.ph.projectbasic.model.Usuario;
import com.ph.projectbasic.model.UsuarioGrupo;
import com.ph.projectbasic.repository.Usuarios;
import com.ph.projectbasic.security.UsuarioLogado;
import com.ph.projectbasic.security.UsuarioSistema;

//Bean controlador de permissões

@Named
@ViewScoped
public class MenuBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Usuarios usuarios;
	
	@Inject
	@UsuarioLogado
	private UsuarioSistema usuariologado;
	
//	@PostConstruct
//	public void init() throws IOException {
//		usuario = this.usuarios.porEmail(usuariologado.getUsuario().getEmail());
//		listaacoes = usuarios.buscarPermissoes(usuario);
//	}
//	
	//verifica se o usuario tem a permissão admin em algum grupo que participa
	public boolean verificarPermissao(){
		
		boolean verficacao = false;
		
		Usuario usuario = this.usuarios.porEmail(usuariologado.getUsuario().getEmail());
		
		if(usuario != null){
			
			List<UsuarioGrupo> listaacoes = usuarios.buscarPermissoes(usuario);
			
			if(!listaacoes.isEmpty()){
				
				for(UsuarioGrupo item : listaacoes){
					if(item.getCodigo().getGrupo().getNome().equals("ADMIN")){
						verficacao = true;
						break;
					}
				}
				
			}
		}
		
		return verficacao;
	}

	
		
}