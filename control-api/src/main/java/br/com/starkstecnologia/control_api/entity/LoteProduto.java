package br.com.starkstecnologia.control_api.entity;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(schema = "sistema", name = "lote_produto", uniqueConstraints = @UniqueConstraint(columnNames = { "numero_lote",
        "id_produto" }))
public class LoteProduto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lote_produto_seq")
    @SequenceGenerator(name = "lote_produto_seq", sequenceName = "sistema.lote_produto_seq", allocationSize = 1)
    @Column(name = "id_lote")
    private Long idLote;

    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    private Produto produto;

    @Column(name = "numero_lote", nullable = false, length = 50)
    private String numeroLote;

    @Column(name = "quantidade_atual", nullable = false, precision = 12, scale = 4)
    private BigDecimal quantidadeAtual;

    @Column(name = "preco_custo", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoCusto;

    @Column(name = "data_entrada", nullable = false)
    private LocalDate dataEntrada;

    @Column(name = "data_validade")
    private LocalDate dataValidade;

    @Column(nullable = false, length = 20)
    private String status; // ATIVO | ESGOTADO

    public Long getIdLote() {
        return idLote;
    }

    public void setIdLote(Long idLote) {
        this.idLote = idLote;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public String getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(String numeroLote) {
        this.numeroLote = numeroLote;
    }

    public BigDecimal getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public void setQuantidadeAtual(BigDecimal quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
    }

    public BigDecimal getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(BigDecimal precoCusto) {
        this.precoCusto = precoCusto;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        LoteProduto that = (LoteProduto) o;
        return Objects.equals(idLote, that.idLote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idLote);
    }
}
