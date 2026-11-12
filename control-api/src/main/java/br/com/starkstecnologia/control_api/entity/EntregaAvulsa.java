package br.com.starkstecnologia.control_api.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(schema = "sistema",name = "entrega_avulsa")
public class EntregaAvulsa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "entrega_avulsa_seq")
    @SequenceGenerator(name = "entrega_avulsa_seq", sequenceName = "sistema.entrega_avulsa_seq", allocationSize = 1)
    @Column(name = "id_entrega_avulsa")
    private Long idEntregaAvulsa;


    @Column(name = "data_hora_inicio")
    private LocalDateTime dataHoraInicioServico;

    @Column(name = "data_hora_termino")
    private LocalDateTime dataHoraTerminoServico;

    @Column(name = "descricao")
    private String descricaoServico;

    @Column(name = "observacao")
    private String observacaoServico;

    @ManyToOne
    @JoinColumn(name = "id_entregador")
    private Entregador entregador;

    @ManyToOne
    @JoinColumn(name = "id_matriz")
    private Matriz matriz;

    public Long getIdEntregaAvulsa() {
        return idEntregaAvulsa;
    }

    public void setIdEntregaAvulsa(Long idEntregaAvulsa) {
        this.idEntregaAvulsa = idEntregaAvulsa;
    }

    public LocalDateTime getDataHoraInicioServico() {
        return dataHoraInicioServico;
    }

    public void setDataHoraInicioServico(LocalDateTime dataHoraInicioServico) {
        this.dataHoraInicioServico = dataHoraInicioServico;
    }

    public LocalDateTime getDataHoraTerminoServico() {
        return dataHoraTerminoServico;
    }

    public void setDataHoraTerminoServico(LocalDateTime dataHoraTerminoServico) {
        this.dataHoraTerminoServico = dataHoraTerminoServico;
    }

    public String getDescricaoServico() {
        return descricaoServico;
    }

    public void setDescricaoServico(String descricaoServico) {
        this.descricaoServico = descricaoServico;
    }

    public String getObservacaoServico() {
        return observacaoServico;
    }

    public void setObservacaoServico(String observacaoServico) {
        this.observacaoServico = observacaoServico;
    }

    public Entregador getEntregador() {
        return entregador;
    }

    public void setEntregador(Entregador entregador) {
        this.entregador = entregador;
    }

    public Matriz getMatriz() {
        return matriz;
    }

    public void setMatriz(Matriz matriz) {
        this.matriz = matriz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntregaAvulsa that = (EntregaAvulsa) o;
        return Objects.equals(idEntregaAvulsa, that.idEntregaAvulsa) && Objects.equals(dataHoraInicioServico, that.dataHoraInicioServico) && Objects.equals(dataHoraTerminoServico, that.dataHoraTerminoServico) && Objects.equals(descricaoServico, that.descricaoServico) && Objects.equals(observacaoServico, that.observacaoServico) && Objects.equals(entregador, that.entregador) && Objects.equals(matriz, that.matriz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEntregaAvulsa, dataHoraInicioServico, dataHoraTerminoServico, descricaoServico, observacaoServico, entregador, matriz);
    }
}
