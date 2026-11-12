package br.com.starkstecnologia.control_api.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(schema = "sistema", name = "conta")
public class Conta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conta_seq")
    @SequenceGenerator(name = "conta_seq", sequenceName = "sistema.conta_seq", allocationSize = 1)
    @Column(name = "id_conta")
    private Long idConta;

    @Column(nullable = false, length = 20)
    private String tipo; // PAGAR | RECEBER

    @Column(nullable = false, length = 150)
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @Column(nullable = false, length = 20)
    private String status; // PENDENTE | PAGO | VENCIDO | CANCELADO

    @Column(length = 100)
    private String categoria;

    @Column(length = 255)
    private String observacao;

    @Column(name = "forma_pagamento", length = 30)
    private String formaPagamento; // DINHEIRO | PIX | CARTAO_DEBITO | CARTAO_CREDITO | CHEQUE | OUTROS

    @ManyToOne
    @JoinColumn(name = "id_matriz")
    private Matriz matriz;

    public Long getIdConta() {
        return idConta;
    }

    public void setIdConta(Long idConta) {
        this.idConta = idConta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public Matriz getMatriz() {
        return matriz;
    }

    public void setMatriz(Matriz matriz) {
        this.matriz = matriz;
    }
}
