package br.com.starkstecnologia.control_api.dto;

import br.com.starkstecnologia.control_api.entity.Permissao;
import java.io.Serializable;

public class PermissaoDTO implements Serializable {

    private Long idPermissao;
    private String chave;
    private String descricao;

    public PermissaoDTO() {
    }

    public PermissaoDTO(Permissao p) {
        this.idPermissao = p.getIdPermissao();
        this.chave = p.getChave();
        this.descricao = p.getDescricao();
    }

    public Long getIdPermissao() {
        return idPermissao;
    }

    public void setIdPermissao(Long idPermissao) {
        this.idPermissao = idPermissao;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
