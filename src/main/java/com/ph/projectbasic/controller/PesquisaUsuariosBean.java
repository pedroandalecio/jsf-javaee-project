package com.ph.projectbasic.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.ph.projectbasic.filter.UsuarioFilter;
import com.ph.projectbasic.model.Usuario;
import com.ph.projectbasic.repository.Usuarios;
import com.ph.projectbasic.service.NegocioException;
import com.ph.projectbasic.util.jsf.FacesUtil;

//Bean controlador da pagina /usuarios/PesquisaUsuarios.xhtml

@Named
@ViewScoped
public class PesquisaUsuariosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Usuarios usuarios;
	
	private UsuarioFilter filtro;
	private List<Usuario> usuariosFiltrados;
	
	private Usuario usuarioSelecionado;
	
	//construtor
	public PesquisaUsuariosBean() {
		filtro = new UsuarioFilter();
	}
	
	//metodo para excluir um usuario
	public void excluir() {
		try {
			usuarios.remover(usuarioSelecionado);
			usuariosFiltrados.remove(usuarioSelecionado);
			
			FacesUtil.addInfoMessage("Usuario " + usuarioSelecionado.getNome() 
					+ " excluído com sucesso.");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}
	
	//metodo para pesquisar
	public void pesquisar() {
		usuariosFiltrados = usuarios.filtrados(filtro);
		if(usuariosFiltrados.size() == 0){
			FacesUtil.addInfoMessage("Nenhum registro encontrado...");
		}
	}
	
	public List<Usuario> getUsuariosFiltrados() {
		return usuariosFiltrados;
	}

	public UsuarioFilter getFiltro() {
		return filtro;
	}

	public Usuario getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}
	
}