package br.com.starkstecnologia.control_api.dto;

import br.com.starkstecnologia.control_api.entity.MovimentacaoEstoque;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovimentacaoDTO {

    private Long idMovimentacao;
    private Long idLote;
    private String numeroLote;
    private Long idProduto;
    private String nomeProduto;
    private String tipo;
    private BigDecimal quantidade;
    private BigDecimal precoCusto;
    private LocalDateTime dataHora;
    private Long idUsuarioResponsavel;
    private String motivo;
    private String origem;

    public MovimentacaoDTO() {
    }

    public MovimentacaoDTO(MovimentacaoEstoque m) {
        this.idMovimentacao = m.getIdMovimentacao();
        this.idLote = m.getLote() != null ? m.getLote().getIdLote() : null;
        this.numeroLote = m.getLote() != null ? m.getLote().getNumeroLote() : null;
        this.idProduto = m.getLote() != null && m.getLote().getProduto() != null
                ? m.getLote().getProduto().getIdProduto()
                : null;
        this.nomeProduto = m.getLote() != null && m.getLote().getProduto() != null ? m.getLote().getProduto().getNome()
                : null;
        this.tipo = m.getTipo();
        this.quantidade = m.getQuantidade();
        this.precoCusto = m.getPrecoCusto();
        this.dataHora = m.getDataHora();
        this.idUsuarioResponsavel = m.getIdUsuarioResponsavel();
        this.motivo = m.getMotivo();
        this.origem = m.getOrigem();
    }

    public Long getIdMovimentacao() {
        return idMovimentacao;
    }

    public void setIdMovimentacao(Long idMovimentacao) {
        this.idMovimentacao = idMovimentacao;
    }

    public Long getIdLote() {
        return idLote;
    }

    public void setIdLote(Long idLote) {
        this.idLote = idLote;
    }

    public String getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(String numeroLote) {
        this.numeroLote = numeroLote;
    }

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(BigDecimal precoCusto) {
        this.precoCusto = precoCusto;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Long getIdUsuarioResponsavel() {
        return idUsuarioResponsavel;
    }

    public void setIdUsuarioResponsavel(Long idUsuarioResponsavel) {
        this.idUsuarioResponsavel = idUsuarioResponsavel;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }
}
