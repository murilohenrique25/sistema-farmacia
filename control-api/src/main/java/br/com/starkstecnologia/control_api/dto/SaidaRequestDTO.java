package br.com.starkstecnologia.control_api.dto;

import java.math.BigDecimal;

public class SaidaRequestDTO {

    private Long idLote;
    private BigDecimal quantidade;
    private String motivo;

    public SaidaRequestDTO() {
    }

    public Long getIdLote() {
        return idLote;
    }

    public void setIdLote(Long idLote) {
        this.idLote = idLote;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
