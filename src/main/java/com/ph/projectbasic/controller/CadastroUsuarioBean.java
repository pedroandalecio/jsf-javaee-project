package com.ph.projectbasic.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DualListModel;

import com.ph.projectbasic.model.Grupo;
import com.ph.projectbasic.model.Usuario;
import com.ph.projectbasic.repository.Grupos;
import com.ph.projectbasic.security.Seguranca;
import com.ph.projectbasic.service.CadastroUsuarioService;
import com.ph.projectbasic.service.NegocioException;
import com.ph.projectbasic.util.jsf.FacesUtil;

// Bean controlador da pagina /usuarios/CadastroUsuario.xhtml

@Named
@ViewScoped
public class CadastroUsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Usuario usuario;
	private DualListModel<Grupo> listaGrupos;
	
	@Inject
	private CadastroUsuarioService cadastroUsuarioService;
	
	@Inject
	private Grupos grupos;
	
	@Inject
	private Seguranca seguranca;
	
	//sempre é executado ao iniciar ou atualizar a pagina /usuarios/CadastroUsuario.xhtml
	public void inicializar(){
		if (usuario == null) {
			limpar();
		} else {
			List<Grupo> lista = grupos.todos();
			lista.removeAll(usuario.getGrupos());
			
			listaGrupos = new DualListModel<>(lista, new ArrayList<>(usuario.getGrupos()));
		}
	}
	
	//metodo para limpar recursos
	public void limpar() {
		this.usuario = new Usuario();
		
		listaGrupos = new DualListModel<>(grupos.todos(), new ArrayList<>());
	}
	
	//metodo para salvar ou alterar um usuario
	public void salvar() throws IOException {
		try {
			usuario.setGrupos(listaGrupos.getTarget());
			Usuario usuarioSalvo = cadastroUsuarioService.salvar(usuario);
			
			listaGrupos = new DualListModel<>(grupos.todos(), new ArrayList<>());
			
			FacesUtil.addInfoMessage("Dados salvos com sucesso!");
			
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			
			seguranca.redirect("/usuarios/CadastroUsuario.xhtml?usuario="+usuarioSalvo.getId());
			
			
		} catch(NegocioException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}
	}

	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public boolean isEditando() {
		return usuario != null && usuario.getId() == null;
	}
	
	public DualListModel<Grupo> getListaGrupos() {
		return listaGrupos;
	}
	
	public void setListaGrupos(DualListModel<Grupo> listaGrupos) {
		this.listaGrupos = listaGrupos;
	}
}
