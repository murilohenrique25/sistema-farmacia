package br.com.starkstecnologia.control_api.dto;

import java.time.LocalDateTime;

public class DadosTodasEntregasDTO {

    private String cupomOrcamento;
    private String nomeCaixa;
    private String nomeCaixaAssinatura;
    private String dataCadastro;
    private String dataAssinatura;
    private String mediaCaixa;
    private String nomeEntregador;
    private String dataSelecao;
    private String dataFinalizacao;
    private String mediaEntregador;
    private String statusEntrega;

    public String getCupomOrcamento() {
        return cupomOrcamento;
    }

    public void setCupomOrcamento(String cupomOrcamento) {
        this.cupomOrcamento = cupomOrcamento;
    }

    public String getNomeCaixa() {
        return nomeCaixa;
    }

    public void setNomeCaixa(String nomeCaixa) {
        this.nomeCaixa = nomeCaixa;
    }

    public String getNomeCaixaAssinatura() {
        return nomeCaixaAssinatura;
    }

    public void setNomeCaixaAssinatura(String nomeCaixaAssinatura) {
        this.nomeCaixaAssinatura = nomeCaixaAssinatura;
    }

    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getDataAssinatura() {
        return dataAssinatura;
    }

    public void setDataAssinatura(String dataAssinatura) {
        this.dataAssinatura = dataAssinatura;
    }

    public String getMediaCaixa() {
        return mediaCaixa;
    }

    public void setMediaCaixa(String mediaCaixa) {
        this.mediaCaixa = mediaCaixa;
    }

    public String getNomeEntregador() {
        return nomeEntregador;
    }

    public void setNomeEntregador(String nomeEntregador) {
        this.nomeEntregador = nomeEntregador;
    }

    public String getDataSelecao() {
        return dataSelecao;
    }

    public void setDataSelecao(String dataSelecao) {
        this.dataSelecao = dataSelecao;
    }

    public String getDataFinalizacao() {
        return dataFinalizacao;
    }

    public void setDataFinalizacao(String dataFinalizacao) {
        this.dataFinalizacao = dataFinalizacao;
    }

    public String getMediaEntregador() {
        return mediaEntregador;
    }

    public void setMediaEntregador(String mediaEntregador) {
        this.mediaEntregador = mediaEntregador;
    }

    public String getStatusEntrega() {
        return statusEntrega;
    }

    public void setStatusEntrega(String statusEntrega) {
        this.statusEntrega = statusEntrega;
    }
}
