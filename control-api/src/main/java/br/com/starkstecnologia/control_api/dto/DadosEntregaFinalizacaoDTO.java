package br.com.starkstecnologia.control_api.dto;

import java.io.Serializable;

public class DadosEntregaFinalizacaoDTO implements Serializable {
    private Long idEntrega;
    private String latitude;
    private String longitude;
    private String observacao;
    private String observacaoEntregador;

    public Long getIdEntrega() {
        return idEntrega;
    }

    public void setIdEntrega(Long idEntrega) {
        this.idEntrega = idEntrega;
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

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getObservacaoEntregador() {
        return observacaoEntregador;
    }

    public void setObservacaoEntregador(String observacaoEntregador) {
        this.observacaoEntregador = observacaoEntregador;
    }
}
