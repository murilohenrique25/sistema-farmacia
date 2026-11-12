package br.com.starkstecnologia.control_api.repository;

import br.com.starkstecnologia.control_api.entity.Matriz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatrizRepository extends JpaRepository<Matriz, Long> {

    @Query("SELECT m FROM Matriz m WHERE m.idMatrizPrincipal IS NULL")
    List<Matriz> findByMatrizes();

    @Query("SELECT m FROM Matriz m WHERE m.idMatrizPrincipal =:idMatriz")
    List<Matriz> findFiliaisByIdMatriz(Long idMatriz);

    @Query("SELECT m FROM Matriz m WHERE m.token =:token")
    Matriz findMatrizByToken(String token);
}
