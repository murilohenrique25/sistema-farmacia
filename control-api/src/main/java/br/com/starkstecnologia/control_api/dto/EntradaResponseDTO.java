package br.com.starkstecnologia.control_api.dto;

import java.util.List;

public class EntradaResponseDTO {

    private int quantidadeItensProcessados;
    private List<Long> idLotesCriadosOuAtualizados;

    public EntradaResponseDTO() {
    }

    public int getQuantidadeItensProcessados() {
        return quantidadeItensProcessados;
    }

    public void setQuantidadeItensProcessados(int quantidadeItensProcessados) {
        this.quantidadeItensProcessados = quantidadeItensProcessados;
    }

    public List<Long> getIdLotesCriadosOuAtualizados() {
        return idLotesCriadosOuAtualizados;
    }

    public void setIdLotesCriadosOuAtualizados(List<Long> idLotesCriadosOuAtualizados) {
        this.idLotesCriadosOuAtualizados = idLotesCriadosOuAtualizados;
    }
}
