package br.com.starkstecnologia.control_api.repository;

import br.com.starkstecnologia.control_api.entity.EntregadorSessao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EntregadorSessaoRepository extends JpaRepository<EntregadorSessao, String> {
    Optional<EntregadorSessao> findByToken(String token);
}
