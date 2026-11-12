package br.com.starkstecnologia.control_api.dto;

import java.io.Serializable;

public class DadosRetornoEntregadorDTO implements Serializable {

    private Long idEntregador;

    private String nome;

    private String usuario;

    private boolean ativo;

    private String telefone;

    private Long idMatriz;

    private String nomeMatriz;

    private String turno;

    private String cpf;

    public Long getIdEntregador() {
        return idEntregador;
    }

    public void setIdEntregador(Long idEntregador) {
        this.idEntregador = idEntregador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Long getIdMatriz() {
        return idMatriz;
    }

    public void setIdMatriz(Long idMatriz) {
        this.idMatriz = idMatriz;
    }

    public String getNomeMatriz() {
        return nomeMatriz;
    }

    public void setNomeMatriz(String nomeMatriz) {
        this.nomeMatriz = nomeMatriz;
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
}
