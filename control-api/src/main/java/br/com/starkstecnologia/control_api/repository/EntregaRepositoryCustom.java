package br.com.starkstecnologia.control_api.repository;

import jakarta.persistence.Tuple;

import java.time.LocalDateTime;
import java.util.List;

public interface EntregaRepositoryCustom {
    List<Tuple> buscarDadosEntregasPorEntregador(LocalDateTime dataInicio, LocalDateTime dataFim, List<Long> idsEntregador, List<String> statusEntrega, Long idMatrizAutenticado);
    List<Tuple> buscarDadosEntregasAvulsas(LocalDateTime dataInicio, LocalDateTime dataFim, List<Long> idsEntregador, Long idMatrizAutenticado);
    List<Tuple> buscarDadosTodasEntregas(LocalDateTime dataInicio, LocalDateTime dataFim, List<Long> idsEntregador, List<String> statusEntrega, Long idMatrizAutenticado);
    Tuple buscarDadosEntregasPorEntregadorApp(LocalDateTime dataInicio, LocalDateTime dataFim, String userId);
    List<Tuple> buscarDadosEntregasApp(LocalDateTime dataInicio, LocalDateTime dataFim, Long idMatriz);
    List<Tuple> findEntregasByParamsAssinar(List<Long> idsEntregador, LocalDateTime dataInicio, LocalDateTime dataFim, Long idMatriz);
    List<Tuple> buscarEntregaMes(LocalDateTime dataInicial, LocalDateTime dataFinal);
    List<Tuple> findEntregasByParams(LocalDateTime dataInicio, LocalDateTime dataTermino, List<String> statusEntrega, List<Long> idsEntregador, Long idMatriz);
    List<Tuple> buscarQuantidadeEntregasAbertas(Long idMatriz);
}
