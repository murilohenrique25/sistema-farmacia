package br.com.starkstecnologia.control_api.entity;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(schema = "sistema",name = "entregador")
public class Entregador implements Serializable {

	@Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "entregador_seq")
    @SequenceGenerator(name = "entregador_seq", sequenceName = "sistema.entregador_seq", allocationSize = 1)
    @Column(name = "id_entregador")
    private Long idEntregador;

    private String nome;

    private String usuario;

    private boolean ativo;

    private String senha;

    private String turno;

    private String telefone;

    private String cpf;

    @ManyToOne
    @JoinColumn(name = "id_matriz")
    private Matriz matriz;

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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
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

    public Matriz getMatriz() {
        return matriz;
    }

    public void setMatriz(Matriz matriz) {
        this.matriz = matriz;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entregador that = (Entregador) o;
        return ativo == that.ativo && Objects.equals(idEntregador, that.idEntregador) && Objects.equals(nome, that.nome) && Objects.equals(usuario, that.usuario) && Objects.equals(senha, that.senha) && Objects.equals(turno, that.turno) && Objects.equals(telefone, that.telefone) && Objects.equals(cpf, that.cpf) && Objects.equals(matriz, that.matriz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEntregador, nome, usuario, ativo, senha, turno, telefone, cpf, matriz);
    }
}