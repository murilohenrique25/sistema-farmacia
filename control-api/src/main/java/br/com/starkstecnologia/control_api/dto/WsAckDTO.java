package br.com.starkstecnologia.control_api.dto;

public class WsAckDTO {

    private String status;
    private Long idEntrega;
    private String message;
    private String atualizadoEm;

    public WsAckDTO() {
    }

    public WsAckDTO(String status, Long idEntrega, String message, String atualizadoEm) {
        this.status = status;
        this.idEntrega = idEntrega;
        this.message = message;
        this.atualizadoEm = atualizadoEm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getIdEntrega() {
        return idEntrega;
    }

    public void setIdEntrega(Long idEntrega) {
        this.idEntrega = idEntrega;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(String atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }
}
