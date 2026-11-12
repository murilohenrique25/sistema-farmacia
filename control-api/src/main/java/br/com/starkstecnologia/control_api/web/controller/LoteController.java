package br.com.starkstecnologia.control_api.web.controller;

import br.com.starkstecnologia.control_api.dto.AlertaVencimentoDTO;
import br.com.starkstecnologia.control_api.dto.LoteDTO;
import br.com.starkstecnologia.control_api.services.LoteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/estoque/lote")
@Tag(name = "Estoque - Lotes", description = "Gerenciamento de lotes do estoque")
public class LoteController {

    private final LoteService loteService;

    public LoteController(LoteService loteService) {
        this.loteService = loteService;
    }

    @GetMapping("/produto/{idProduto}")
    @PreAuthorize("hasAuthority('ACESSAR_ESTOQUE') or hasAuthority('GERENCIAR_ESTOQUE')")
    public ResponseEntity<List<LoteDTO>> listarPorProduto(@PathVariable Long idProduto) {
        return ResponseEntity.ok(loteService.listarPorProduto(idProduto));
    }

    @GetMapping("/alertas")
    @PreAuthorize("hasAuthority('ACESSAR_ESTOQUE') or hasAuthority('GERENCIAR_ESTOQUE')")
    public ResponseEntity<List<AlertaVencimentoDTO>> alertasVencimento(
            @RequestParam(defaultValue = "30") int dias) {
        return ResponseEntity.ok(loteService.alertasVencimento(dias));
    }
}
