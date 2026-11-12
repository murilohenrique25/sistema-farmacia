package br.com.starkstecnologia.control_api.web.dto;

import java.io.Serializable;
import java.util.Objects;

public class UsuarioSenhaDto implements Serializable {

    private Long id;
    private String nome;
    private String turno;
    private String cpf;
    private Boolean ativo;
    private String telefone;
    private String password;
    private String novaSenha;
    private String confirmacaoSenha;

    public UsuarioSenhaDto(String password, String novaSenha, String confirmacaoSenha) {
        this.password = password;
        this.novaSenha = novaSenha;
        this.confirmacaoSenha = confirmacaoSenha;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getConfirmacaoSenha() {
        return confirmacaoSenha;
    }

    public void setConfirmacaoSenha(String confirmacaoSenha) {
        this.confirmacaoSenha = confirmacaoSenha;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
