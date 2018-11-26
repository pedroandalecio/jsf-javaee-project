package com.ph.projectbasic.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ph.projectbasic.util.jsf.FacesUtil;

//Bean controlador da pagina /Login.xhtml

@Named
@ViewScoped
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FacesContext facesContext;
	
	@Inject
	private HttpServletRequest request;
	
	@Inject
	private HttpServletResponse response;
	
	private String email;
	
	public void preRender() { 
		
		if ("true".equals(request.getParameter("invalid"))) {
				FacesUtil.addErrorMessage("Falha de Autenticação!");
				FacesUtil.addErrorMessage("Usuário/Senha inválidos ou Usuário está Inativo!");
		}
	}
	
	public void login() throws ServletException, IOException {
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/Login.xhtml");
		dispatcher.forward(request, response);
		
		facesContext.responseComplete();
		
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}