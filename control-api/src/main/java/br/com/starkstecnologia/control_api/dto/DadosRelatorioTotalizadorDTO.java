package br.com.starkstecnologia.control_api.dto;

public class DadosRelatorioTotalizadorDTO {

    private String nome;

    private Long quantidadeEntrega;

    private String mediaTotal;

    private String tempoTotal;

    private String valorTotalEntrega;

    private String valorTotalTroco;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getQuantidadeEntrega() {
        return quantidadeEntrega;
    }

    public void setQuantidadeEntrega(Long quantidadeEntrega) {
        this.quantidadeEntrega = quantidadeEntrega;
    }

    public String getMediaTotal() {
        return mediaTotal;
    }

    public void setMediaTotal(String mediaTotal) {
        this.mediaTotal = mediaTotal;
    }

    public String getTempoTotal() {
        return tempoTotal;
    }

    public void setTempoTotal(String tempoTotal) {
        this.tempoTotal = tempoTotal;
    }

    public String getValorTotalEntrega() {
        return valorTotalEntrega;
    }

    public void setValorTotalEntrega(String valorTotalEntrega) {
        this.valorTotalEntrega = valorTotalEntrega;
    }

    public String getValorTotalTroco() {
        return valorTotalTroco;
    }

    public void setValorTotalTroco(String valorTotalTroco) {
        this.valorTotalTroco = valorTotalTroco;
    }
}
