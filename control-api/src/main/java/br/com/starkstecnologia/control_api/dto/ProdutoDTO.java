package br.com.starkstecnologia.control_api.dto;

import br.com.starkstecnologia.control_api.entity.Produto;

public class ProdutoDTO {

    private Long idProduto;
    private String nome;
    private String codigo;
    private String categoria;
    private String unidadeMedida;
    private String status;
    private Long idMatriz;

    public ProdutoDTO() {
    }

    public ProdutoDTO(Produto p) {
        this.idProduto = p.getIdProduto();
        this.nome = p.getNome();
        this.codigo = p.getCodigo();
        this.categoria = p.getCategoria();
        this.unidadeMedida = p.getUnidadeMedida();
        this.status = p.getStatus();
        this.idMatriz = p.getMatriz() != null ? p.getMatriz().getIdMatriz() : null;
    }

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

    public Long getIdMatriz() {
        return idMatriz;
    }

    public void setIdMatriz(Long idMatriz) {
        this.idMatriz = idMatriz;
    }
}
