package br.com.starkstecnologia.control_api.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(schema = "sistema", name = "permissao")
public class Permissao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permissao_seq")
    @SequenceGenerator(name = "permissao_seq", sequenceName = "sistema.permissao_seq", allocationSize = 1)
    @Column(name = "id_permissao")
    private Long idPermissao;

    @Column(name = "chave", nullable = false, unique = true, length = 50)
    private String chave;

    @Column(name = "descricao", length = 100)
    private String descricao;

    public Permissao() {
    }

    public Permissao(String chave, String descricao) {
        this.chave = chave;
        this.descricao = descricao;
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
