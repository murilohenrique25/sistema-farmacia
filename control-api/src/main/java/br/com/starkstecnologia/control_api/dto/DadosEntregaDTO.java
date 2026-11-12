package br.com.starkstecnologia.control_api.dto;

import java.io.Serializable;

public class DadosEntregaDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idEntrega;
    private String dataCadastroEntrega;
    private String dataAssinaturaEntrega;
    private String dataSelecaoEntrega;
    private String dataFinalizacaoEntrega;
    private Long idUser;
    private Long idUserAssinatura;
    private String nomeUser;
    private String nomeUserAssinatura;
    private Long idEntregador;
    private String nomeEntregador;
    private String latitude;
    private String longitude;
    private Double valorTotal;
    private Double valorReceber;
    private Double valorFrete;
    private Double troco;
    private String cupomOrcamento;
    private String tipoEntrega;
    private String formaPagamento;
    private String observacao;
    private String statusEntrega;
    private Long idMatriz;
    private String nomeAtendente;
    private String token;
    private String telefone;

    public Long getIdEntrega() {
        return idEntrega;
    }

    public void setIdEntrega(Long idEntrega) {
        this.idEntrega = idEntrega;
    }

    public String getDataCadastroEntrega() {
        return dataCadastroEntrega;
    }

    public void setDataCadastroEntrega(String dataCadastroEntrega) {
        this.dataCadastroEntrega = dataCadastroEntrega;
    }

    public String getDataAssinaturaEntrega() {
        return dataAssinaturaEntrega;
    }

    public void setDataAssinaturaEntrega(String dataAssinaturaEntrega) {
        this.dataAssinaturaEntrega = dataAssinaturaEntrega;
    }

    public String getDataSelecaoEntrega() {
        return dataSelecaoEntrega;
    }

    public void setDataSelecaoEntrega(String dataSelecaoEntrega) {
        this.dataSelecaoEntrega = dataSelecaoEntrega;
    }

    public String getDataFinalizacaoEntrega() {
        return dataFinalizacaoEntrega;
    }

    public void setDataFinalizacaoEntrega(String dataFinalizacaoEntrega) {
        this.dataFinalizacaoEntrega = dataFinalizacaoEntrega;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Long getIdEntregador() {
        return idEntregador;
    }

    public void setIdEntregador(Long idEntregador) {
        this.idEntregador = idEntregador;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Double getValorReceber() {
        return valorReceber;
    }

    public void setValorReceber(Double valorReceber) {
        this.valorReceber = valorReceber;
    }

    public Double getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(Double valorFrete) {
        this.valorFrete = valorFrete;
    }

    public Double getTroco() {
        return troco;
    }

    public void setTroco(Double troco) {
        this.troco = troco;
    }

    public String getCupomOrcamento() {
        return cupomOrcamento;
    }

    public void setCupomOrcamento(String cupomOrcamento) {
        this.cupomOrcamento = cupomOrcamento;
    }

    public String getTipoEntrega() {
        return tipoEntrega;
    }

    public void setTipoEntrega(String tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getStatusEntrega() {
        return statusEntrega;
    }

    public void setStatusEntrega(String statusEntrega) {
        this.statusEntrega = statusEntrega;
    }

    public String getNomeUser() {
        return nomeUser;
    }

    public void setNomeUser(String nomeUser) {
        this.nomeUser = nomeUser;
    }

    public String getNomeEntregador() {
        return nomeEntregador;
    }

    public void setNomeEntregador(String nomeEntregador) {
        this.nomeEntregador = nomeEntregador;
    }

    public Long getIdMatriz() {
        return idMatriz;
    }

    public void setIdMatriz(Long idMatriz) {
        this.idMatriz = idMatriz;
    }

    public Long getIdUserAssinatura() {
        return idUserAssinatura;
    }

    public void setIdUserAssinatura(Long idUserAssinatura) {
        this.idUserAssinatura = idUserAssinatura;
    }

    public String getNomeUserAssinatura() {
        return nomeUserAssinatura;
    }

    public void setNomeUserAssinatura(String nomeUserAssinatura) {
        this.nomeUserAssinatura = nomeUserAssinatura;
    }

    public String getNomeAtendente() {
        return nomeAtendente;
    }

    public void setNomeAtendente(String nomeAtendente) {
        this.nomeAtendente = nomeAtendente;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
