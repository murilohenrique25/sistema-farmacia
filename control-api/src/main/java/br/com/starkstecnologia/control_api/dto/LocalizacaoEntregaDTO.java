package br.com.starkstecnologia.control_api.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class LocalizacaoEntregaDTO {

    @NotNull
    private Long idEntrega;

    @NotNull
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    private Double latitude;

    @NotNull
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private Double longitude;
    private String timestamp;

    public Long getIdEntrega() {
        return idEntrega;
    }

    public void setIdEntrega(Long idEntrega) {
        this.idEntrega = idEntrega;
    }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
