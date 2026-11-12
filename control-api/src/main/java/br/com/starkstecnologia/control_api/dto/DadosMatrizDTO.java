package br.com.starkstecnologia.control_api.dto;

import java.io.Serializable;

public class DadosMatrizDTO implements Serializable {

    private Long idMatriz;

    private String nome;

    private String logradouro;

    private String telefone;

    private String cnpj;

    private String email;

    private String latitude;

    private String longitude;

    private Boolean freteFixo;

    private Double valorFrete;

    public Long getIdMatriz() {
        return idMatriz;
    }

    public void setIdMatriz(Long idMatriz) {
        this.idMatriz = idMatriz;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Boolean isFreteFixo() {
        return freteFixo;
    }

    public void setFreteFixo(Boolean freteFixo) {
        this.freteFixo = freteFixo;
    }

    public Double getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(Double valorFrete) {
        this.valorFrete = valorFrete;
    }
}
