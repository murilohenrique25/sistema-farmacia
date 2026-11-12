package br.com.starkstecnologia.control_api.web.controller;

import br.com.starkstecnologia.control_api.repository.EntregaRepository;
import br.com.starkstecnologia.control_api.services.LocalizacaoEntregaService;
import br.com.starkstecnologia.control_api.types.StatusEntregaEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/entrega-localizacao")
public class EntregaLocalizacaoController {

    @Autowired
    private  LocalizacaoEntregaService localizacaoService;
    @Autowired
    private EntregaRepository entregaRepository;

    @GetMapping("/{idEntrega}/localizacao-atual")
    public ResponseEntity<?> getLocalizacao(@PathVariable Long idEntrega) {

        var entrega = entregaRepository.findById(idEntrega).orElse(null);
        if (entrega == null || !StatusEntregaEnum.EM_ROTA.getDescricao().equals(entrega.getStatusEntrega())) {
            // não mostra nada se não estiver em rota
            return ResponseEntity.noContent().build();
        }

        var loc = localizacaoService.buscarPorEntrega(idEntrega);
        if (loc == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(loc);
    }
}