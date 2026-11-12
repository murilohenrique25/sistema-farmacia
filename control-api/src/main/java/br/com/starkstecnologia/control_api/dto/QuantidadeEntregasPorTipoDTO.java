package br.com.starkstecnologia.control_api.dto;

import java.io.Serializable;

public class QuantidadeEntregasPorTipoDTO implements Serializable {

    private String tipo;

    private Integer quantidade;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
