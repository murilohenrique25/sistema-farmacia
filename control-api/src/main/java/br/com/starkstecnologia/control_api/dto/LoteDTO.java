package br.com.starkstecnologia.control_api.dto;

import br.com.starkstecnologia.control_api.entity.LoteProduto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LoteDTO {

    private Long idLote;
    private Long idProduto;
    private String nomeProduto;
    private String numeroLote;
    private BigDecimal quantidadeAtual;
    private BigDecimal precoCusto;
    private LocalDate dataEntrada;
    private LocalDate dataValidade;
    private String status;

    public LoteDTO() {
    }

    public LoteDTO(LoteProduto l) {
        this.idLote = l.getIdLote();
        this.idProduto = l.getProduto() != null ? l.getProduto().getIdProduto() : null;
        this.nomeProduto = l.getProduto() != null ? l.getProduto().getNome() : null;
        this.numeroLote = l.getNumeroLote();
        this.quantidadeAtual = l.getQuantidadeAtual();
        this.precoCusto = l.getPrecoCusto();
        this.dataEntrada = l.getDataEntrada();
        this.dataValidade = l.getDataValidade();
        this.status = l.getStatus();
    }

    public Long getIdLote() {
        return idLote;
    }

    public void setIdLote(Long idLote) {
        this.idLote = idLote;
    }

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(String numeroLote) {
        this.numeroLote = numeroLote;
    }

    public BigDecimal getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public void setQuantidadeAtual(BigDecimal quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
    }

    public BigDecimal getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(BigDecimal precoCusto) {
        this.precoCusto = precoCusto;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
