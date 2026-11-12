package br.com.starkstecnologia.control_api.jwt;


import br.com.starkstecnologia.control_api.entity.Usuario;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


public class JwtUserDetails extends User {

    private Usuario usuario;

    public JwtUserDetails(Usuario usuario) {
        super(usuario.getUsername(), usuario.getPassword(), AuthorityUtils.createAuthorityList(usuario.getRole().getDescricao()));
        this.usuario = usuario;
    }

    public Long getId(){
        return this.usuario.getId();
    }

    public String getRole(){
        return this.usuario.getRole().getDescricao();
    }

    public Long getIdMatriz() {
        return this.usuario.getMatriz().getIdMatriz();
    }
}
