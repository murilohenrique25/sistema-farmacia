package br.com.starkstecnologia.control_api.dto;

import java.io.Serializable;

public class DadosEntregaAvulsaDTO implements Serializable {
    private String userId;
    private String dataHoraInicioServico;
    private String dataHoraTerminoServico;
    private String descricaoServico;
    private String observacaoServico;
    private Long id;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDataHoraInicioServico() {
        return dataHoraInicioServico;
    }

    public void setDataHoraInicioServico(String dataHoraInicioServico) {
        this.dataHoraInicioServico = dataHoraInicioServico;
    }

    public String getDataHoraTerminoServico() {
        return dataHoraTerminoServico;
    }

    public void setDataHoraTerminoServico(String dataHoraTerminoServico) {
        this.dataHoraTerminoServico = dataHoraTerminoServico;
    }

    public String getDescricaoServico() {
        return descricaoServico;
    }

    public void setDescricaoServico(String descricaoServico) {
        this.descricaoServico = descricaoServico;
    }

    public String getObservacaoServico() {
        return observacaoServico;
    }

    public void setObservacaoServico(String observacaoServico) {
        this.observacaoServico = observacaoServico;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
