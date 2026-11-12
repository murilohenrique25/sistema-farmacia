package br.com.starkstecnologia.control_api.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class EntregaResponseDTO {

    private List<DadosEntregaMapaDTO> entregas;
    private Map<LocalDate, Long> quantidadePorData;

    public EntregaResponseDTO(List<DadosEntregaMapaDTO> entregas, Map<LocalDate, Long> quantidadePorData) {
        this.entregas = entregas;
        this.quantidadePorData = quantidadePorData;
    }

    public List<DadosEntregaMapaDTO> getEntregas() {
        return entregas;
    }

    public void setEntregas(List<DadosEntregaMapaDTO> entregas) {
        this.entregas = entregas;
    }

    public Map<LocalDate, Long> getQuantidadePorData() {
        return quantidadePorData;
    }

    public void setQuantidadePorData(Map<LocalDate, Long> quantidadePorData) {
        this.quantidadePorData = quantidadePorData;
    }
}
