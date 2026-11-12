package br.com.starkstecnologia.control_api.dto;

public class DadosEntregadorMesDTO {

    private String nome;
    private Integer totalEntregas;
    private Integer quantidadeTotalEntregasFinalizadas;
    private Integer quantidadeTotalFinalizadasEntregador;

    public Integer getTotalEntregas() {
        return totalEntregas;
    }

    public void setTotalEntregas(Integer totalEntregas) {
        this.totalEntregas = totalEntregas;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getQuantidadeTotalEntregasFinalizadas() {
        return quantidadeTotalEntregasFinalizadas;
    }

    public void setQuantidadeTotalEntregasFinalizadas(Integer quantidadeTotalEntregasFinalizadas) {
        this.quantidadeTotalEntregasFinalizadas = quantidadeTotalEntregasFinalizadas;
    }

    public Integer getQuantidadeTotalFinalizadasEntregador() {
        return quantidadeTotalFinalizadasEntregador;
    }

    public void setQuantidadeTotalFinalizadasEntregador(Integer quantidadeTotalFinalizadasEntregador) {
        this.quantidadeTotalFinalizadasEntregador = quantidadeTotalFinalizadasEntregador;
    }
}
