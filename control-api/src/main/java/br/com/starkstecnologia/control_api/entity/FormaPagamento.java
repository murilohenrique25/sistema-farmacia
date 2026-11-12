package br.com.starkstecnologia.control_api.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(schema = "sistema",name = "forma_pagamento")
public class FormaPagamento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "forma_pagamento_seq")
    @SequenceGenerator(name = "forma_pagamento_seq", sequenceName = "sistema.forma_pagamento_seq", allocationSize = 1)
    @Column(name = "id_forma_pagamento")
    private Long idFormaPagamento;

    private String descricao;

    private boolean ativo;

    @ManyToOne
    @JoinColumn(name = "id_matriz")
    private Matriz matriz;

    public Long getIdFormaPagamento() {
        return idFormaPagamento;
    }

    public void setIdFormaPagamento(Long idFormaPagamento) {
        this.idFormaPagamento = idFormaPagamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormaPagamento that = (FormaPagamento) o;
        return ativo == that.ativo && Objects.equals(idFormaPagamento, that.idFormaPagamento) && Objects.equals(descricao, that.descricao) && Objects.equals(matriz, that.matriz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFormaPagamento, descricao, ativo, matriz);
    }
}
