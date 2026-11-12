package br.com.starkstecnologia.control_api.repository;

import br.com.starkstecnologia.control_api.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByMatrizIdMatrizAndAtivo(Long idMatriz, boolean ativo);

    List<Categoria> findByMatrizIdMatrizAndTipoAndAtivo(Long idMatriz, String tipo, boolean ativo);

    List<Categoria> findByMatrizIdMatriz(Long idMatriz);
}
