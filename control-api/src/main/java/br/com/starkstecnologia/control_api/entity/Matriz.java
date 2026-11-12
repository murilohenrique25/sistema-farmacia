package br.com.starkstecnologia.control_api.entity;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(schema = "sistema", name = "matriz")
public class Matriz implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "matriz_seq")
    @SequenceGenerator(name = "matriz_seq", sequenceName = "sistema.matriz_seq", allocationSize = 1)
    @Column(name = "id_matriz")
    private Long idMatriz;

    private String nome;

    private String logradouro;

    private String telefone;

    private String cnpj;

    private String email;

    private String latitude;

    private String longitude;

    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "queue_id")
    private Integer queueId;

    @Column(name = "base_url")
    private String baseURL;

    @Column(name = "envia_sms")
    private boolean enviaSms;

    @Column(name = "frete_fixo")
    private Boolean freteFixo = false;

    @Column(name = "valor_frete")
    private Double valorFrete;

    @Column(name = "id_matriz_principal")
    private Long idMatrizPrincipal;

    private String token;

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

    public Long getIdMatrizPrincipal() {
        return idMatrizPrincipal;
    }

    public void setIdMatrizPrincipal(Long idMatrizPrincipal) {
        this.idMatrizPrincipal = idMatrizPrincipal;
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

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Integer getQueueId() {
        return queueId;
    }

    public void setQueueId(Integer queueId) {
        this.queueId = queueId;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public boolean isEnviaSms() {
        return enviaSms;
    }

    public void setEnviaSms(boolean enviaSms) {
        this.enviaSms = enviaSms;
    }

    public Boolean isFreteFixo() {
        return freteFixo != null ? freteFixo : false;
    }

    public void setFreteFixo(Boolean freteFixo) {
        this.freteFixo = freteFixo != null ? freteFixo : false;
    }

    public Double getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(Double valorFrete) {
        this.valorFrete = valorFrete;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Matriz matriz = (Matriz) o;
        return Objects.equals(idMatriz, matriz.idMatriz) && Objects.equals(nome, matriz.nome)
                && Objects.equals(logradouro, matriz.logradouro) && Objects.equals(telefone, matriz.telefone)
                && Objects.equals(cnpj, matriz.cnpj) && Objects.equals(email, matriz.email)
                && Objects.equals(latitude, matriz.latitude) && Objects.equals(longitude, matriz.longitude)
                && Objects.equals(apiKey, matriz.apiKey) && Objects.equals(queueId, matriz.queueId)
                && Objects.equals(baseURL, matriz.baseURL)
                && Objects.equals(idMatrizPrincipal, matriz.idMatrizPrincipal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMatriz, nome, logradouro, telefone, cnpj, email, latitude, longitude, apiKey, queueId,
                baseURL, idMatrizPrincipal);
    }
}
