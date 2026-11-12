package br.com.starkstecnologia.control_api.dto;

import java.math.BigDecimal;

public class SaidaResponseDTO {

    private Long idLote;
    private BigDecimal quantidadeAtual;

    public SaidaResponseDTO() {
    }

    public Long getIdLote() {
        return idLote;
    }

    public void setIdLote(Long idLote) {
        this.idLote = idLote;
    }

    public BigDecimal getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public void setQuantidadeAtual(BigDecimal quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
    }
}
