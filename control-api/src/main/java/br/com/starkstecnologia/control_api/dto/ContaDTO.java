package br.com.starkstecnologia.control_api.dto;

import br.com.starkstecnologia.control_api.entity.Conta;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ContaDTO implements Serializable {

    private Long idConta;
    private String tipo;
    private String descricao;
    private BigDecimal valor;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private String status;
    private String categoria;
    private String observacao;
    private String formaPagamento;
    private Long idMatriz;

    public ContaDTO() {
    }

    public ContaDTO(Conta c) {
        this.idConta = c.getIdConta();
        this.tipo = c.getTipo();
        this.descricao = c.getDescricao();
        this.valor = c.getValor();
        this.dataVencimento = c.getDataVencimento();
        this.dataPagamento = c.getDataPagamento();
        this.status = c.getStatus();
        this.categoria = c.getCategoria();
        this.observacao = c.getObservacao();
        this.formaPagamento = c.getFormaPagamento();
        this.idMatriz = c.getMatriz() != null ? c.getMatriz().getIdMatriz() : null;
    }

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

    public Long getIdMatriz() {
        return idMatriz;
    }

    public void setIdMatriz(Long idMatriz) {
        this.idMatriz = idMatriz;
    }
}
