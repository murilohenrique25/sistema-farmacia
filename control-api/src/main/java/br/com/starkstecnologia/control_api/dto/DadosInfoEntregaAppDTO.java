package br.com.starkstecnologia.control_api.dto;

public class DadosInfoEntregaAppDTO {

    private String cupomOrcamento;

    private String statusEntrega;

    private String dataCadastroEntrega;

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

    public String getDataCadastroEntrega() {
        return dataCadastroEntrega;
    }

    public void setDataCadastroEntrega(String dataCadastroEntrega) {
        this.dataCadastroEntrega = dataCadastroEntrega;
    }
}
