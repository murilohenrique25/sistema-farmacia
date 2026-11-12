package br.com.starkstecnologia.control_api.repository;

import br.com.starkstecnologia.control_api.entity.LocalizacaoEntregaAtual;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalizacaoEntregaAtualRepository
        extends JpaRepository<LocalizacaoEntregaAtual, Long> {
}