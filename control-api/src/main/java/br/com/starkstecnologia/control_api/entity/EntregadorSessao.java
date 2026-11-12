package br.com.starkstecnologia.control_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(schema = "sistema",name = "entregador_sessao")
public class EntregadorSessao implements Serializable {

    @Id
    private String token;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}
