package br.com.starkstecnologia.control_api.types;

public enum Role {

    ROLE_ADMIN("Administrador"),
    ROLE_CLIENTE("Cliente");

    private final String descricao;

    Role(String value) {
        this.descricao = value;
    }

    public String getDescricao() {
        return descricao;
    }
}
