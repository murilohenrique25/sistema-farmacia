package br.com.starkstecnologia.control_api.dto;

import br.com.starkstecnologia.control_api.entity.Conta;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ContaAlertaDTO implements Serializable {

    private Long idConta;
    private String tipo;
    private String descricao;
    private BigDecimal valor;
    private LocalDate dataVencimento;
    private String status;
    private long diasParaVencer; // negativo = já venceu

    public ContaAlertaDTO() {
    }

    public ContaAlertaDTO(Conta c) {
        this.idConta = c.getIdConta();
        this.tipo = c.getTipo();
        this.descricao = c.getDescricao();
        this.valor = c.getValor();
        this.dataVencimento = c.getDataVencimento();
        this.status = c.getStatus();
        this.diasParaVencer = ChronoUnit.DAYS.between(LocalDate.now(), c.getDataVencimento());
    }

    public Long getIdConta() {
        return idConta;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public String getStatus() {
        return status;
    }

    public long getDiasParaVencer() {
        return diasParaVencer;
    }
}
