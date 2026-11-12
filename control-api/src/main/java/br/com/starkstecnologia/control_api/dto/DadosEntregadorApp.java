package br.com.starkstecnologia.control_api.dto;

public class DadosEntregadorApp {

    private Integer quantidadeEntrega;

    private String mediaTotal;

    public String getMediaTotal() {
        return mediaTotal;
    }

    public void setMediaTotal(String mediaTotal) {
        this.mediaTotal = mediaTotal;
    }

    public Integer getQuantidadeEntrega() {
        return quantidadeEntrega;
    }

    public void setQuantidadeEntrega(Integer quantidadeEntrega) {
        this.quantidadeEntrega = quantidadeEntrega;
    }
}
