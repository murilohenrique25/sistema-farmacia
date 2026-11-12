package br.com.starkstecnologia.control_api.dto;

import java.io.Serializable;

public class EnviarMensagemRequest implements Serializable {

    Integer queueId;
    String apiKey;
    String number;
    String text;

    public EnviarMensagemRequest(Integer queueId, String apiKey, String number, String text) {
        this.queueId = queueId;
        this.apiKey = apiKey;
        this.number = number;
        this.text = text;
    }

    public Integer getQueueId() {
        return queueId;
    }

    public void setQueueId(Integer queueId) {
        this.queueId = queueId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
