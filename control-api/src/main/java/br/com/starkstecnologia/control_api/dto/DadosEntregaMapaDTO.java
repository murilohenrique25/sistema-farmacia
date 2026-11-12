package br.com.starkstecnologia.control_api.dto;

import java.time.LocalDate;

public class DadosEntregaMapaDTO {

    private Long idEntrega;
    private String latitude;
    private String longitude;
    private String dataEntrega;

    public DadosEntregaMapaDTO(Long idEntrega, String latitude, String longitude, String dataEntrega) {
        this.idEntrega = idEntrega;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dataEntrega = dataEntrega;
    }

    public Long getIdEntrega() {
        return idEntrega;
    }

    public void setIdEntrega(Long idEntrega) {
        this.idEntrega = idEntrega;
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

    public String getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(String dataEntrega) {
        this.dataEntrega = dataEntrega;
    }
}
