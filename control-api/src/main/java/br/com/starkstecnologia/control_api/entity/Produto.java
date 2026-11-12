package br.com.starkstecnologia.control_api.entity;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(schema = "sistema", name = "produto", uniqueConstraints = @UniqueConstraint(columnNames = { "codigo",
        "id_matriz" }))
public class Produto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "produto_seq")
    @SequenceGenerator(name = "produto_seq", sequenceName = "sistema.produto_seq", allocationSize = 1)
    @Column(name = "id_produto")
    private Long idProduto;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 50)
    private String codigo;

    @Column(length = 100)
    private String categoria;

    @Column(name = "unidade_medida", nullable = false, length = 20)
    private String unidadeMedida;

    @Column(nullable = false, length = 20)
    private String status; // ATIVO | INATIVO

    @ManyToOne
    @JoinColumn(name = "id_matriz")
    private Matriz matriz;

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Matriz getMatriz() {
        return matriz;
    }

    public void setMatriz(Matriz matriz) {
        this.matriz = matriz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Produto produto = (Produto) o;
        return Objects.equals(idProduto, produto.idProduto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProduto);
    }
}
