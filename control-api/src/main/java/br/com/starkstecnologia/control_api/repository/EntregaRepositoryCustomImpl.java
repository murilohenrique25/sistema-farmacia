package br.com.starkstecnologia.control_api.repository;

import br.com.starkstecnologia.control_api.types.StatusEntregaEnum;
import br.com.starkstecnologia.control_api.utils.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EntregaRepositoryCustomImpl implements EntregaRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Tuple> buscarDadosEntregasPorEntregador(LocalDateTime dataInicio, LocalDateTime dataFim,
            List<Long> idsEntregador, List<String> statusEntrega, Long idMatrizAutenticado) {
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT en.nome AS nome, " +
                        "COUNT(e) AS quantidadeEntregas, " +
                        "SUM(TIMESTAMPDIFF(SECOND, e.dataSelecaoEntrega, e.dataFinalizacaoEntrega)) AS totalTempoGasto, "
                        +
                        "AVG(TIMESTAMPDIFF(SECOND, e.dataSelecaoEntrega, e.dataFinalizacaoEntrega)) AS mediaTempo, " +
                        "SUM(e.valorTotal) AS valorTotalEntregas, " +
                        "SUM(e.troco) AS totalTroco " +
                        "FROM Entrega e " +
                        "JOIN e.entregador en " +
                        "WHERE e.dataSelecaoEntrega BETWEEN :dataInicio AND :dataFim AND e.matriz.idMatriz = :idMatriz ");

        if (idsEntregador != null && !idsEntregador.isEmpty()) {
            queryBuilder.append(" AND en.id IN :idsEntregador");
        }

        if (statusEntrega != null) {
            queryBuilder.append(" AND e.statusEntrega IN (:statusEntrega)");
        }

        queryBuilder.append(" GROUP BY e.entregador.id");

        Query query = entityManager.createQuery(queryBuilder.toString(), Tuple.class);
        query.setParameter("dataInicio", dataInicio);
        query.setParameter("dataFim", dataFim);
        query.setParameter("idMatriz", idMatrizAutenticado);

        // Define o parâmetro idsEntregador apenas se a lista não estiver vazia
        if (idsEntregador != null && !idsEntregador.isEmpty()) {
            query.setParameter("idsEntregador", idsEntregador);
        }

        if (statusEntrega != null && !statusEntrega.isEmpty()) {
            query.setParameter("statusEntrega", statusEntrega);
        }

        return query.getResultList();
    }

    @Override
    public List<Tuple> buscarDadosEntregasAvulsas(LocalDateTime dataInicio, LocalDateTime dataFim,
            List<Long> idsEntregador, Long idMatrizAutenticado) {
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT e.dataHoraInicioServico AS dataHoraInicioServico, " +
                        "e.dataHoraTerminoServico AS dataHoraTerminoServico, " +
                        "e.descricaoServico AS descricaoServico, " +
                        "e.observacaoServico AS observacaoServico, " +
                        "en.nome AS nomeEntregador " +
                        "FROM EntregaAvulsa e " +
                        "JOIN Entregador en ON e.entregador.idEntregador = en.idEntregador " +
                        "WHERE e.dataHoraInicioServico BETWEEN :dataInicio AND :dataFim AND e.matriz.idMatriz = :idMatriz ");

        // Adiciona a condição de idsEntregador se a lista não estiver vazia
        if (idsEntregador != null && !idsEntregador.isEmpty()) {
            queryBuilder.append(" AND en.id IN :idsEntregador");
        }

        Query query = entityManager.createQuery(queryBuilder.toString(), Tuple.class);
        query.setParameter("dataInicio", dataInicio);
        query.setParameter("dataFim", dataFim);
        query.setParameter("idMatriz", idMatrizAutenticado);

        // Define o parâmetro idsEntregador apenas se a lista não estiver vazia
        if (idsEntregador != null && !idsEntregador.isEmpty()) {
            query.setParameter("idsEntregador", idsEntregador);
        }

        return query.getResultList();
    }

    @Override
    public List<Tuple> buscarDadosTodasEntregas(LocalDateTime dataInicio, LocalDateTime dataFim,
            List<Long> idsEntregador, List<String> statusEntrega, Long idMatrizAutenticado) {
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT e.cupomOrcamento AS cupomOrcamento, " +
                        "u.nome AS nomeCaixa, " +
                        "e.statusEntrega AS statusEntrega, " +
                        "us.nome AS nomeCaixaAssinatura, " +
                        "e.dataCadastroEntrega AS dataCadastroEntrega, " +
                        "e.dataAssinaturaEntrega AS dataAssinaturaEntrega, " +
                        "TIMESTAMPDIFF(SECOND, e.dataCadastroEntrega, e.dataAssinaturaEntrega) AS mediaCaixa, " +
                        "en.nome AS nomeEntregador, " +
                        "e.dataSelecaoEntrega AS dataSelecaoEntrega, " +
                        "e.dataFinalizacaoEntrega AS dataFinalizacaoEntrega, " +
                        "TIMESTAMPDIFF(SECOND, e.dataSelecaoEntrega, e.dataFinalizacaoEntrega) AS mediaEntregador " +
                        "FROM Entrega e " +
                        "JOIN Usuario u ON e.user.id = u.id " +
                        "LEFT JOIN Usuario us ON e.userAssinatura.id = us.id " +
                        "LEFT JOIN Entregador en ON e.entregador.idEntregador = en.idEntregador " +
                        "WHERE " +
                        "e.dataCadastroEntrega BETWEEN :dataInicio AND :dataFim AND e.matriz.idMatriz = :idMatriz ");

        if (statusEntrega != null) {
            queryBuilder.append(" AND e.statusEntrega IN(:statusEntrega)");
        }
        if (idsEntregador != null && !idsEntregador.isEmpty()) {
            queryBuilder.append(" AND en.id IN :idsEntregador");
        }

        Query query = entityManager.createQuery(queryBuilder.toString(), Tuple.class);
        query.setParameter("dataInicio", dataInicio);
        query.setParameter("dataFim", dataFim);
        query.setParameter("idMatriz", idMatrizAutenticado);

        // Define o parâmetro idsEntregador apenas se a lista não estiver vazia
        if (idsEntregador != null && !idsEntregador.isEmpty()) {
            query.setParameter("idsEntregador", idsEntregador);
        }
        if (statusEntrega != null && !statusEntrega.isEmpty()) {

            query.setParameter("statusEntrega", statusEntrega);
        }

        return query.getResultList();
    }

    @Override
    public Tuple buscarDadosEntregasPorEntregadorApp(LocalDateTime dataInicio, LocalDateTime dataFim, String userId) {
        String queryBuilder = "SELECT COUNT(e) AS quantidadeEntregas, " +
                "AVG(TIMESTAMPDIFF(SECOND, e.dataSelecaoEntrega, e.dataFinalizacaoEntrega)) AS mediaTempo " +
                "FROM Entrega e " +
                "JOIN e.entregador en " +
                "WHERE e.dataSelecaoEntrega BETWEEN :dataInicio AND :dataFim AND en.usuario = :userId " +
                "AND (e.statusEntrega = 'Finalizada' OR e.statusEntrega = 'Finalizada Manualmente' OR e.statusEntrega = 'Finalizada e Assinada' )";
        Query query = entityManager.createQuery(queryBuilder, Tuple.class);
        query.setParameter("dataInicio", dataInicio);
        query.setParameter("dataFim", dataFim);

        query.setParameter("userId", userId);
        return (Tuple) query.getSingleResult();
    }

    @Override
    public List<Tuple> buscarDadosEntregasApp(LocalDateTime dataInicio, LocalDateTime dataFim, Long idMatriz) {
        String queryBuilder = "SELECT e.cupomOrcamento, e.statusEntrega, e.dataCadastroEntrega " +
                "FROM Entrega e " +
                "WHERE e.dataCadastroEntrega BETWEEN :dataInicio AND :dataFim " +
                "AND e.statusEntrega = 'Aberta' OR e.statusEntrega = 'Em rota' AND e.matriz.idMatriz = :idMatriz";
        Query query = entityManager.createQuery(queryBuilder, Tuple.class);
        query.setParameter("dataInicio", dataInicio);
        query.setParameter("dataFim", dataFim);
        query.setParameter("idMatriz", idMatriz);

        return query.getResultList();
    }

    @Override
    public List<Tuple> findEntregasByParamsAssinar(List<Long> idsEntregador, LocalDateTime dataInicio,
            LocalDateTime dataFim, Long idMatriz) {
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT e.idEntrega AS idEntrega, " +
                        "e.statusEntrega AS statusEntrega, " +
                        "e.cupomOrcamento AS cupomOrcamento, " +
                        "u.nome AS nomeCaixa, " +
                        "e.dataFinalizacaoEntrega AS dataFinalizacaoEntrega, " +
                        "en.nome AS nomeEntregador " +
                        "FROM Entrega e " +
                        "JOIN Entregador en ON e.entregador.idEntregador = en.idEntregador " +
                        "JOIN Usuario u ON e.user.id = u.id " +
                        "WHERE e.statusEntrega IN ('Finalizada', 'Finalizada Manualmente') AND" +
                        " e.dataCadastroEntrega BETWEEN :dataInicio AND :dataFim AND e.matriz.idMatriz = :idMatriz");

        // Adiciona a condição de idsEntregador se a lista não estiver vazia
        if (idsEntregador != null && !idsEntregador.isEmpty()) {
            queryBuilder.append(" AND en.idEntregador IN (:idsEntregador)");
        }

        Query query = entityManager.createQuery(queryBuilder.toString(), Tuple.class);
        query.setParameter("dataInicio", dataInicio);
        query.setParameter("dataFim", dataFim);
        query.setParameter("idMatriz", idMatriz);

        // Define o parâmetro idsEntregador apenas se a lista não estiver vazia
        if (idsEntregador != null && !idsEntregador.isEmpty()) {
            query.setParameter("idsEntregador", idsEntregador);
        }

        return query.getResultList();
    }

    @Override
    public List<Tuple> buscarEntregaMes(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        String sql = "WITH  " +
                "TotalEntregas AS ( " +
                "    SELECT COUNT(e.idEntrega) AS totalEntregas " +
                "    FROM Entrega e " +
                "    WHERE e.dataCadastroEntrega BETWEEN :dataInicio AND :dataFim AND e.matriz.idMatriz = :idMatriz " +
                "), " +
                " " +
                "EntregasFinalizadasOuAssinadas AS ( " +
                "    SELECT COUNT(e.idEntrega) AS totalEntregasFinalizadasOuAssinadas " +
                "    FROM Entrega e " +
                "    WHERE e.statusEntrega IN ('Finalizada e Assinada')  " +
                "      AND e.dataCadastroEntrega BETWEEN :dataInicio AND :dataFim AND e.matriz.idMatriz = :idMatriz " +
                "), " +
                " " +
                "EntregadorMaisEntregas AS ( " +
                "    SELECT  " +
                "        en.nome AS nomeEntregador,  " +
                "        COUNT(*) AS totalEntregasPorEntregador " +
                "    FROM Entrega e " +
                "    JOIN Entregador en ON e.entregador.idEntregador = en.idEntregador " +
                "    WHERE e.statusEntrega IN ('Finalizada e Assinada')  " +
                "      AND e.dataCadastroEntrega BETWEEN :dataInicio AND :dataFim AND e.matriz.idMatriz = :idMatriz " +
                "    GROUP BY en.nome " +
                "    ORDER BY totalEntregasPorEntregador DESC " +
                "    LIMIT 1 " +
                ") " +
                " " +
                "SELECT  " +
                "    (SELECT totalEntregas FROM TotalEntregas) AS totalEntregas, " +
                "    (SELECT totalEntregasFinalizadasOuAssinadas FROM EntregasFinalizadasOuAssinadas) AS totalEntregasFinalizadasOuAssinadas, "
                +
                "    (SELECT nomeEntregador FROM EntregadorMaisEntregas) AS entregadorMaisEntregas, " +
                "    (SELECT totalEntregasPorEntregador FROM EntregadorMaisEntregas) AS quantidadeEntregasPorEntregadorMaisEntregas ";
        Query query = entityManager.createQuery(sql, Tuple.class);
        query.setParameter("dataInicio", dataInicial);
        query.setParameter("dataFim", dataFinal);
        query.setParameter("idMatriz", StringUtils.idMatrizUsuarioAutenticado());
        return query.getResultList();
    }

    @Override
    public List<Tuple> findEntregasByParams(LocalDateTime dataInicio, LocalDateTime dataTermino,
            List<String> statusEntrega, List<Long> idsEntregador, Long idMatriz) {
        StringBuilder queryBuilder = new StringBuilder("SELECT e.idEntrega AS idEntrega, " +
                "       u.id AS idUser, u.nome AS nomeUser, " +
                "       e.cupomOrcamento AS cupomOrcamento, " +
                "       e.formaPagamento AS formaPagamento, " +
                "       e.tipoEntrega AS tipoEntrega, " +
                "       e.troco AS troco, " +
                "       e.valorReceber AS valorReceber, " +
                "       e.valorFrete AS valorFrete, " +
                "       e.valorTotal AS valorTotal, " +
                "       e.dataSelecaoEntrega AS dataSelecaoEntrega, " +
                "       e.dataCadastroEntrega AS dataCadastroEntrega, " +
                "       e.latitude AS latitude, " +
                "       e.longitude AS longitude, " +
                "       e.observacao AS observacao, " +
                "       e.statusEntrega AS statusEntrega, " +
                "       e.dataFinalizacaoEntrega AS dataFinalizacaoEntrega, " +
                "       e.dataAssinaturaEntrega AS dataAssinaturaEntrega, " +
                "       ua.id AS idUserAssinatura, ua.nome AS nomeUserAssinatura, " +
                "       en.idEntregador AS idEntregador, en.nome AS nomeEntregador, " +
                "       e.latitude, " +
                "       e.longitude, " +
                "       e.numeroTelefone as numeroTelefone, " +
                "       m.idMatriz AS idMatriz " +
                "FROM Entrega e " +
                "LEFT JOIN e.user u " +
                "LEFT JOIN e.userAssinatura ua " +
                "LEFT JOIN e.entregador en " +
                "LEFT JOIN e.matriz m " +
                "WHERE e.dataCadastroEntrega BETWEEN :dataInicio AND :dataTermino AND m.idMatriz = :idMatriz ");

        if (idsEntregador != null && !idsEntregador.isEmpty()) {
            queryBuilder.append(" AND en.idEntregador IN (:idsEntregador)");
        }
        if (statusEntrega != null && !statusEntrega.isEmpty()) {

            queryBuilder.append(" AND e.statusEntrega IN (:statusEntrega)");
        }

        Query query = entityManager.createQuery(queryBuilder.toString(), Tuple.class);
        query.setParameter("dataInicio", dataInicio);
        query.setParameter("dataTermino", dataTermino);
        query.setParameter("idMatriz", idMatriz);

        if (idsEntregador != null && !idsEntregador.isEmpty()) {
            query.setParameter("idsEntregador", idsEntregador);
        }
        if (statusEntrega != null && !statusEntrega.isEmpty()) {
            List<String> statusEntregas = new ArrayList<>();
            for (String status : statusEntrega) {
                StatusEntregaEnum entregaEnum = StatusEntregaEnum.valueOf(status);
                statusEntregas.add(entregaEnum.getDescricao());
            }
            query.setParameter("statusEntrega", statusEntregas);
        }
        return query.getResultList();
    }

    @Override
    public List<Tuple> buscarQuantidadeEntregasAbertas(Long idMatriz) {

        String sql = "SELECT tipo_entrega, COUNT(*) AS total " +
                "FROM entrega " +
                "WHERE id_entregador IS NULL AND id_matriz = :idMatriz AND status_entrega = 'Aberta' " +
                "GROUP BY tipo_entrega";

        return entityManager.createNativeQuery(sql, Tuple.class)
                .setParameter("idMatriz", idMatriz)
                .getResultList();

    }
}
