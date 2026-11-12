package br.com.starkstecnologia.control_api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PosicaoEstoqueDTO {

    private Long idProduto;
    private String nomeProduto;
    private String codigo;
    private String unidadeMedida;
    private BigDecimal quantidadeTotalAtiva;
    private int numeroDeLotesAtivos;
    private LocalDate dataValidadeMaisProxima;

    public PosicaoEstoqueDTO() {
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public BigDecimal getQuantidadeTotalAtiva() {
        return quantidadeTotalAtiva;
    }

    public void setQuantidadeTotalAtiva(BigDecimal quantidadeTotalAtiva) {
        this.quantidadeTotalAtiva = quantidadeTotalAtiva;
    }

    public int getNumeroDeLotesAtivos() {
        return numeroDeLotesAtivos;
    }

    public void setNumeroDeLotesAtivos(int numeroDeLotesAtivos) {
        this.numeroDeLotesAtivos = numeroDeLotesAtivos;
    }

    public LocalDate getDataValidadeMaisProxima() {
        return dataValidadeMaisProxima;
    }

    public void setDataValidadeMaisProxima(LocalDate dataValidadeMaisProxima) {
        this.dataValidadeMaisProxima = dataValidadeMaisProxima;
    }
}
