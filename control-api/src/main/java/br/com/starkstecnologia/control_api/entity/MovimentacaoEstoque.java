package br.com.starkstecnologia.control_api.entity;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(schema = "sistema", name = "movimentacao_estoque")
public class MovimentacaoEstoque implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movimentacao_estoque_seq")
    @SequenceGenerator(name = "movimentacao_estoque_seq", sequenceName = "sistema.movimentacao_estoque_seq", allocationSize = 1)
    @Column(name = "id_movimentacao")
    private Long idMovimentacao;

    @ManyToOne
    @JoinColumn(name = "id_lote", nullable = false)
    private LoteProduto lote;

    @Column(nullable = false, length = 10)
    private String tipo; // ENTRADA | SAIDA

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal quantidade;

    @Column(name = "preco_custo", precision = 10, scale = 2)
    private BigDecimal precoCusto;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(name = "id_usuario_responsavel")
    private Long idUsuarioResponsavel;

    @Column(length = 255)
    private String motivo; // usado em saídas

    @Column(name = "origem", length = 20)
    private String origem; // MANUAL | NFE

    public Long getIdMovimentacao() {
        return idMovimentacao;
    }

    public void setIdMovimentacao(Long idMovimentacao) {
        this.idMovimentacao = idMovimentacao;
    }

    public LoteProduto getLote() {
        return lote;
    }

    public void setLote(LoteProduto lote) {
        this.lote = lote;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MovimentacaoEstoque that = (MovimentacaoEstoque) o;
        return Objects.equals(idMovimentacao, that.idMovimentacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMovimentacao);
    }
}
