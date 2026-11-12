package br.com.starkstecnologia.control_api.dto;

import br.com.starkstecnologia.control_api.entity.Categoria;
import java.io.Serializable;

public class CategoriaDTO implements Serializable {

    private Long idCategoria;
    private String descricao;
    private String tipo;
    private boolean ativo;

    public CategoriaDTO() {
    }

    public CategoriaDTO(Categoria c) {
        this.idCategoria = c.getIdCategoria();
        this.descricao = c.getDescricao();
        this.tipo = c.getTipo();
        this.ativo = c.isAtivo();
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
