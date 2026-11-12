package br.com.starkstecnologia.control_api.repository;

import br.com.starkstecnologia.control_api.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByMatrizIdMatrizOrderByNomeAsc(Long idMatriz);

    boolean existsByCodigoAndMatrizIdMatriz(String codigo, Long idMatriz);

    boolean existsByCodigoAndMatrizIdMatrizAndIdProdutoNot(String codigo, Long idMatriz, Long idProduto);
}
