package br.com.starkstecnologia.control_api.dto;

import java.util.List;

public class EntradaRequestDTO {

    private List<EntradaItemDTO> itens;

    public EntradaRequestDTO() {
    }

    public List<EntradaItemDTO> getItens() {
        return itens;
    }

    public void setItens(List<EntradaItemDTO> itens) {
        this.itens = itens;
    }
}
