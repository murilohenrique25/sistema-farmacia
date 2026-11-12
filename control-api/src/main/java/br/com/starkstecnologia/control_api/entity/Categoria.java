package br.com.starkstecnologia.control_api.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(schema = "sistema", name = "categoria")
public class Categoria implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long idCategoria;

    @Column(nullable = false, length = 100)
    private String descricao;

    @Column(nullable = false, length = 20)
    private String tipo; // PAGAR | RECEBER

    @Column(nullable = false)
    private boolean ativo = true;

    @ManyToOne
    @JoinColumn(name = "id_matriz")
    private Matriz matriz;

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Matriz getMatriz() {
        return matriz;
    }

    public void setMatriz(Matriz matriz) {
        this.matriz = matriz;
    }
}
