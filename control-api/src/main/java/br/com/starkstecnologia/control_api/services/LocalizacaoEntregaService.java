package br.com.starkstecnologia.control_api.services;

import br.com.starkstecnologia.control_api.dto.LocalizacaoEntregaDTO;
import br.com.starkstecnologia.control_api.entity.LocalizacaoEntregaAtual;
import br.com.starkstecnologia.control_api.repository.EntregaRepository;
import br.com.starkstecnologia.control_api.repository.LocalizacaoEntregaAtualRepository;
import br.com.starkstecnologia.control_api.types.AtualizacaoResultado;
import br.com.starkstecnologia.control_api.types.StatusEntregaEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LocalizacaoEntregaService {

    private final LocalizacaoEntregaAtualRepository repo;
    private final EntregaRepository entregaRepository;

    @Autowired
    public LocalizacaoEntregaService(LocalizacaoEntregaAtualRepository repo, EntregaRepository entregaRepository) {
        this.repo = repo;
        this.entregaRepository = entregaRepository;
    }

    public AtualizacaoResultado atualizarLocalizacao(String userId, LocalizacaoEntregaDTO dto) {
        // 1) Confere se essa entrega está em rota
        var entrega = entregaRepository.findById(dto.getIdEntrega())
                .orElse(null);
        if (entrega == null) {
            return AtualizacaoResultado.IGNORED;
        }

        if (!StatusEntregaEnum.EM_ROTA.getDescricao().equals(entrega.getStatusEntrega())) {
            // se não estiver em rota, ignora (não mostra na web)
            return AtualizacaoResultado.IGNORED;
        }

        // 2) Atualiza tabela de posição atual
        LocalizacaoEntregaAtual loc =
                repo.findById(dto.getIdEntrega()).orElse(new LocalizacaoEntregaAtual());
        loc.setIdEntrega(dto.getIdEntrega());
        loc.setUserId(userId);
        loc.setLatitude(dto.getLatitude());
        loc.setLongitude(dto.getLongitude());
        loc.setAtualizadoEm(LocalDateTime.now());

        repo.save(loc);
        return AtualizacaoResultado.OK;
    }

    public LocalizacaoEntregaAtual buscarPorEntrega(Long idEntrega) {
        return repo.findById(idEntrega).orElse(null);
    }

    public void removerLocalizacao(Long idEntrega) {
        repo.findById(idEntrega).ifPresent(repo::delete);
    }
}
