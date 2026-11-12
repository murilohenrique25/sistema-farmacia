package br.com.starkstecnologia.control_api.dto;

import br.com.starkstecnologia.control_api.entity.FormaPagamento;

import java.io.Serializable;

public class DadosFormaPagamentoDTO implements Serializable {

    private Long idFormaPagamento;
    private String descricao;
    private boolean ativo;
    private Long idMatriz;

    public DadosFormaPagamentoDTO() {
    }

    public DadosFormaPagamentoDTO(FormaPagamento formaPagamento) {
        this.idFormaPagamento = formaPagamento.getIdFormaPagamento();
        this.descricao = formaPagamento.getDescricao();
        this.ativo = formaPagamento.isAtivo();
    }

    public Long getIdFormaPagamento() {
        return idFormaPagamento;
    }

    public void setIdFormaPagamento(Long idFormaPagamento) {
        this.idFormaPagamento = idFormaPagamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Long getIdMatriz() {
        return idMatriz;
    }

    public void setIdMatriz(Long idMatriz) {
        this.idMatriz = idMatriz;
    }
}
