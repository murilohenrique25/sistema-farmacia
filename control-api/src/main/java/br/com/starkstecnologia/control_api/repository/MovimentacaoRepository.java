package br.com.starkstecnologia.control_api.repository;

import br.com.starkstecnologia.control_api.entity.MovimentacaoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimentacaoRepository extends JpaRepository<MovimentacaoEstoque, Long> {

    List<MovimentacaoEstoque> findByLote_Produto_Matriz_IdMatrizAndDataHoraBetweenOrderByDataHoraDesc(
            Long idMatriz, LocalDateTime inicio, LocalDateTime fim);

    List<MovimentacaoEstoque> findByLote_Produto_IdProdutoAndDataHoraBetweenOrderByDataHoraDesc(
            Long idProduto, LocalDateTime inicio, LocalDateTime fim);
}
