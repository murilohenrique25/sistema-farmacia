package br.com.starkstecnologia.control_api.dto;

import java.io.Serializable;

public class DadosRelatorioEntregaAvulsaDTO implements Serializable {
    private String nome;
    private String dataHoraInicioServico;
    private String dataHoraTerminoServico;
    private String descricaoServico;
    private String observacaoServico;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

}
