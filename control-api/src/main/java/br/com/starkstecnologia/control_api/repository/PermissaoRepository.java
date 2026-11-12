package br.com.starkstecnologia.control_api.repository;

import br.com.starkstecnologia.control_api.entity.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissaoRepository extends JpaRepository<Permissao, Long> {
    Optional<Permissao> findByChave(String chave);

    boolean existsByChave(String chave);
}
