package br.com.starkstecnologia.control_api.dto;

import java.io.Serializable;

public class DadosEntregaMesDTO implements Serializable {
    private String nomeEntregador;
    private Integer totalEntregasEntregador;
    private Integer totalEntregasMes;
    private Integer totalEntregaMesFinalizadas;

    public String getNomeEntregador() {
        return nomeEntregador;
    }

    public void setNomeEntregador(String nomeEntregador) {
        this.nomeEntregador = nomeEntregador;
    }

    public Integer getTotalEntregasEntregador() {
        return totalEntregasEntregador;
    }

    public void setTotalEntregasEntregador(Integer totalEntregasEntregador) {
        this.totalEntregasEntregador = totalEntregasEntregador;
    }

    public Integer getTotalEntregasMes() {
        return totalEntregasMes;
    }

    public void setTotalEntregasMes(Integer totalEntregasMes) {
        this.totalEntregasMes = totalEntregasMes;
    }

    public Integer getTotalEntregaMesFinalizadas() {
        return totalEntregaMesFinalizadas;
    }

    public void setTotalEntregaMesFinalizadas(Integer totalEntregaMesFinalizadas) {
        this.totalEntregaMesFinalizadas = totalEntregaMesFinalizadas;
    }
}
