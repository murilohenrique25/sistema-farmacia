package br.com.starkstecnologia.control_api.dto;

public class DadosUsuarioLogadoDTO {

    private String usuario;
    private DadosEntregadorApp dadosEntregadorApp;
    private String token;


    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public DadosEntregadorApp getDadosEntregadorApp() {
        return dadosEntregadorApp;
    }

    public void setDadosEntregadorApp(DadosEntregadorApp dadosEntregadorApp) {
        this.dadosEntregadorApp = dadosEntregadorApp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

