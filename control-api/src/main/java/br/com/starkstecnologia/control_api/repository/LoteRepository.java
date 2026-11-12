package br.com.starkstecnologia.control_api.repository;

import br.com.starkstecnologia.control_api.entity.LoteProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoteRepository extends JpaRepository<LoteProduto, Long> {

    List<LoteProduto> findByProdutoIdProdutoOrderByDataValidadeAsc(Long idProduto);

    boolean existsByProdutoIdProdutoAndNumeroLote(Long idProduto, String numeroLote);

    @Query("SELECT l FROM LoteProduto l WHERE l.produto.matriz.idMatriz = :idMatriz " +
            "AND l.quantidadeAtual > 0 AND l.dataValidade <= :limite ORDER BY l.dataValidade ASC")
    List<LoteProduto> findAlertasVencimento(@Param("idMatriz") Long idMatriz, @Param("limite") LocalDate limite);

    @Query("SELECT SUM(l.quantidadeAtual) FROM LoteProduto l WHERE l.produto.idProduto = :idProduto AND l.quantidadeAtual > 0")
    BigDecimal sumQuantidadeAtivaByProduto(@Param("idProduto") Long idProduto);
}
