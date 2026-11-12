package br.com.starkstecnologia.control_api.dto;



import java.io.Serializable;

public class DadosEntregaAssinarDTO implements Serializable {

    private static final long serialVersionUID = 1L;
	private Long idEntrega;
    private String dataFinalizacaoEntrega;
    private String nomeUser;
    private String nomeEntregador;
    private String cupomOrcamento;
    private String statusEntrega;

    public Long getIdEntrega() {
        return idEntrega;
    }

    public void setIdEntrega(Long idEntrega) {
        this.idEntrega = idEntrega;
    }

    public String getDataFinalizacaoEntrega() {
        return dataFinalizacaoEntrega;
    }

    public void setDataFinalizacaoEntrega(String dataFinalizacaoEntrega) {
        this.dataFinalizacaoEntrega = dataFinalizacaoEntrega;
    }

    public String getCupomOrcamento() {
        return cupomOrcamento;
    }

    public void setCupomOrcamento(String cupomOrcamento) {
        this.cupomOrcamento = cupomOrcamento;
    }

    public String getStatusEntrega() {
        return statusEntrega;
    }

    public void setStatusEntrega(String statusEntrega) {
        this.statusEntrega = statusEntrega;
    }

    public String getNomeUser() {
        return nomeUser;
    }

    public void setNomeUser(String nomeUser) {
        this.nomeUser = nomeUser;
    }

    public String getNomeEntregador() {
        return nomeEntregador;
    }

    public void setNomeEntregador(String nomeEntregador) {
        this.nomeEntregador = nomeEntregador;
    }
}
