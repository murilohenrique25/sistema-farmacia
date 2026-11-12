package br.com.starkstecnologia.control_api.web.dto;

import br.com.starkstecnologia.control_api.entity.Usuario;

import java.io.Serializable;
import java.util.Objects;

public class UsuarioResponseDto implements Serializable {

    private Long id;
    private String username;
    private String nome;
    private String role;
    private String turno;
    private String telefone;
    private boolean ativo;
    private String cpf;
    private Long idMatriz;

    public UsuarioResponseDto() {
    }

    public UsuarioResponseDto(Usuario usuario) {
        this.id = usuario.getId();
        this.username = usuario.getUsername();
        this.nome = usuario.getNome();
        this.role = usuario.getRole().getDescricao();
        this.turno = usuario.getTurno();
        this.telefone = usuario.getTelefone();
        this.ativo = usuario.isAtivo();
        this.cpf = usuario.getCpf();
        this.idMatriz = usuario.getMatriz().getIdMatriz();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Long getIdMatriz() {
        return idMatriz;
    }

    public void setIdMatriz(Long idMatriz) {
        this.idMatriz = idMatriz;
    }
}
