package com.ph.projectbasic.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ph.projectbasic.model.Grupo;
import com.ph.projectbasic.model.Permissao;
import com.ph.projectbasic.model.Usuario;
import com.ph.projectbasic.repository.Usuarios;
import com.ph.projectbasic.util.cdi.CDIServiceLocator;

// Classe auxiliar para definir autorização e autenticação de usuario

public class AppUserDetailsService implements UserDetailsService {

	//Efetuar login
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuarios usuarios = CDIServiceLocator.getBean(Usuarios.class);
		Usuario usuario = usuarios.porEmail(email);
		
		UsuarioSistema user = null;
		
		if (usuario != null) {
			user = new UsuarioSistema(usuario, getGrupos(usuario));
		} else {
			throw new UsernameNotFoundException("Usuário não encontrado.");
		}
		
		return user;
	}

	//Faz a leitura e repassa os grupos e permissões do usuário ao efetuar login
	private Collection<? extends GrantedAuthority> getGrupos(Usuario usuario) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		for (Grupo grupo : usuario.getGrupos()) {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + grupo.getNome().toUpperCase()));
			
			for(Permissao permissao: grupo.getPermissoes()) {
				authorities.add(new SimpleGrantedAuthority("ROLE_" + permissao.getNome().toUpperCase()));
			}
		}
		
		return authorities;
	}
}
