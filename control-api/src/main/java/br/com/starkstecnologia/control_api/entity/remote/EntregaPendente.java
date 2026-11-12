package br.com.starkstecnologia.control_api.entity.remote;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "v_entregaspendentes", schema = "myintegracao")
public class EntregaPendente {

    @Id
    @Column(name = "CUPOM")
    private String cupom;

    @Column(name = "IDENTREGA")
    private Integer idEntrega;

    @Column(name = "DATAVENDA")
    private LocalDateTime dataVenda;

    @Column(name = "HORAVENDA")
    private String horaVenda;

    @Column(name = "NOMEVEND")
    private String nomeVendedor;

    @Column(name = "TIPOENTREGA")
    private String tipoEntrega;

    @Column(name = "forma_pagamento")
    private String formaPagamento;

    @Column(name = "VALORTOTAL")
    private BigDecimal valorTotal;

    @Column(name = "VALTROCOPARA")
    private BigDecimal valorTrocoPara;

    @Column(name = "ENTREG_OBS")
    private String observacaoEntrega;

    @Column(name = "TIPOVENDA")
    private String tipoVenda;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "TELEFONE")
    private String telefone;

    // Construtores
    public EntregaPendente() {}

    public EntregaPendente(String cupom, Integer idEntrega, LocalDateTime dataVenda, String horaVenda,
                           String nomeVendedor, String tipoEntrega, String formaPagamento,
                           BigDecimal valorTotal, BigDecimal valorTrocoPara, String observacaoEntrega,
                           String tipoVenda, String status, String telefone) {
        this.cupom = cupom;
        this.idEntrega = idEntrega;
        this.dataVenda = dataVenda;
        this.horaVenda = horaVenda;
        this.nomeVendedor = nomeVendedor;
        this.tipoEntrega = tipoEntrega;
        this.formaPagamento = formaPagamento;
        this.valorTotal = valorTotal;
        this.valorTrocoPara = valorTrocoPara;
        this.observacaoEntrega = observacaoEntrega;
        this.tipoVenda = tipoVenda;
        this.status = status;
        this.telefone = telefone;
    }

    // Getters e Setters
    public String getCupom() { return cupom; }
    public void setCupom(String cupom) { this.cupom = cupom; }

    public Integer getIdEntrega() { return idEntrega; }
    public void setIdEntrega(Integer idEntrega) { this.idEntrega = idEntrega; }

    public LocalDateTime getDataVenda() { return dataVenda; }
    public void setDataVenda(LocalDateTime dataVenda) { this.dataVenda = dataVenda; }

    public String getHoraVenda() { return horaVenda; }
    public void setHoraVenda(String horaVenda) { this.horaVenda = horaVenda; }

    public String getNomeVendedor() { return nomeVendedor; }
    public void setNomeVendedor(String nomeVendedor) { this.nomeVendedor = nomeVendedor; }

    public String getTipoEntrega() { return tipoEntrega; }
    public void setTipoEntrega(String tipoEntrega) { this.tipoEntrega = tipoEntrega; }

    public String getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public BigDecimal getValorTrocoPara() { return valorTrocoPara; }
    public void setValorTrocoPara(BigDecimal valorTrocoPara) { this.valorTrocoPara = valorTrocoPara; }

    public String getObservacaoEntrega() { return observacaoEntrega; }
    public void setObservacaoEntrega(String observacaoEntrega) { this.observacaoEntrega = observacaoEntrega; }

    public String getTipoVenda() { return tipoVenda; }
    public void setTipoVenda(String tipoVenda) { this.tipoVenda = tipoVenda; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    @Override
    public String toString() {
        return "EntregaPendente{" +
                "cupom='" + cupom + '\'' +
                ", idEntrega=" + idEntrega +
                ", dataVenda=" + dataVenda +
                ", nomeVendedor='" + nomeVendedor + '\'' +
                ", tipoEntrega='" + tipoEntrega + '\'' +
                ", valorTotal=" + valorTotal +
                ", status='" + status + '\'' +
                '}';
    }
}

