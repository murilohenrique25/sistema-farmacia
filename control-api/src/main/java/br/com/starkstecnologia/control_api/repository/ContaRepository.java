package br.com.starkstecnologia.control_api.repository;

import br.com.starkstecnologia.control_api.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    List<Conta> findByMatrizIdMatrizAndTipo(Long idMatriz, String tipo);

    List<Conta> findByMatrizIdMatrizAndTipoAndDataVencimentoBetween(
            Long idMatriz, String tipo, LocalDate inicio, LocalDate fim);

    List<Conta> findByMatrizIdMatrizAndTipoAndStatus(Long idMatriz, String tipo, String status);

    // Contas prestes a vencer (próximos N dias) ou já vencidas e não pagas
    @Query("""
                SELECT c FROM Conta c
                WHERE c.matriz.idMatriz = :idMatriz
                AND c.status IN ('PENDENTE', 'VENCIDO')
                AND c.dataVencimento <= :limite
                ORDER BY c.dataVencimento ASC
            """)
    List<Conta> findAlertasVencimento(@Param("idMatriz") Long idMatriz, @Param("limite") LocalDate limite);

    List<Conta> findByMatrizIdMatriz(Long idMatriz);
}
