package com.ph.projectbasic.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import com.ph.projectbasic.filter.UsuarioFilter;
import com.ph.projectbasic.model.Usuario;
import com.ph.projectbasic.model.UsuarioGrupo;
import com.ph.projectbasic.security.UsuarioSistema;
import com.ph.projectbasic.service.NegocioException;
import com.ph.projectbasic.util.jpa.Transactional;
import com.ph.projectbasic.util.jsf.FacesUtil;

//Repositorio Usuario

public class Usuarios implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	//buscar usuario por id
	public Usuario porId(Long id) {
		return this.manager.find(Usuario.class, id);
	}
	
	//buscar permissoes do usuario
	public List<UsuarioGrupo> buscarPermissoes(Usuario usuario) {
		return this.manager.createQuery("select u from UsuarioGrupo u where u.codigo.usuario = :usuario", UsuarioGrupo.class)
				.setParameter("usuario", usuario)
				.getResultList();
	}

	//buscar usuario por email
	public Usuario porEmail(String email) {
		Usuario usuario = null;
		
		try {
			usuario = this.manager.createQuery("from Usuario where lower(email) = :email", Usuario.class)
				.setParameter("email", email.toLowerCase()).getSingleResult();
		} catch (NoResultException e) {
			// nenhum usuário encontrado com o e-mail informado
		}
		
		if(usuario != null){
			if(!usuario.isStatus()){
				return null;
			}
		}
		
		return usuario;
	}
	
	//buscar senha do usuario para uma eventual alteração na tela de edição de usuario
	public String buscarSenha(Long id) {
		return this.manager.createQuery("select senha from Usuario where id = :id", String.class)
				.setParameter("id", id)
				.getSingleResult();
	}
	
	//metodo para pesquisa de usuarios por filtro
	public List<Usuario> filtrados(UsuarioFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Usuario> criteriaQuery = builder.createQuery(Usuario.class);
		List<Predicate> predicates = new ArrayList<>();
		
		Root<Usuario> usuarioRoot = criteriaQuery.from(Usuario.class);
		
		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(builder.like(builder.lower(usuarioRoot.get("nome")), 
					"%" + filtro.getNome().toLowerCase() + "%"));
		}
		
		criteriaQuery.select(usuarioRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(usuarioRoot.get("nome")));
		
		TypedQuery<Usuario> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}
	
	//remover usuario
	@Transactional
	public void remover(Usuario usuario) throws NegocioException {
		try {
			usuario = porId(usuario.getId());
			manager.remove(usuario);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Usuário não pode ser excluído.");
		}
	}

	//guardar usuario
	public Usuario guardar(Usuario usuario) {
		return manager.merge(usuario);
	}
}