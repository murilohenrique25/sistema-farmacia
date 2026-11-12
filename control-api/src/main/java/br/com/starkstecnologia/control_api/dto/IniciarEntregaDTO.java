package br.com.starkstecnologia.control_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class IniciarEntregaDTO {

    @NotNull
    private List<Long> idsEntrega;

    @NotBlank
    private String latitude;

    @NotBlank
    private String longitude;

    public List<Long> getIdsEntrega() {
        return idsEntrega;
    }

    public void setIdsEntrega(List<Long> idsEntrega) {
        this.idsEntrega = idsEntrega;
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
}
