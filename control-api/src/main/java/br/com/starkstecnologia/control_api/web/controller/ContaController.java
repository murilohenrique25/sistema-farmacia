package br.com.starkstecnologia.control_api.web.controller;

import br.com.starkstecnologia.control_api.dto.ContaAlertaDTO;
import br.com.starkstecnologia.control_api.dto.ContaDTO;
import br.com.starkstecnologia.control_api.services.ContaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/conta")
@Tag(name = "Contas", description = "Contas a pagar e a receber")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping
    public ResponseEntity<ContaDTO> criar(@RequestBody ContaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contaService.salvar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContaDTO> atualizar(@PathVariable Long id, @RequestBody ContaDTO dto) {
        return ResponseEntity.ok(contaService.atualizar(id, dto));
    }

    @PutMapping("/{id}/baixar")
    public ResponseEntity<ContaDTO> baixar(@PathVariable Long id) {
        return ResponseEntity.ok(contaService.baixar(id));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<ContaDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(contaService.cancelar(id));
    }

    // GET /api/v1/conta?tipo=PAGAR
    @GetMapping
    public ResponseEntity<List<ContaDTO>> listar(@RequestParam String tipo) {
        return ResponseEntity.ok(contaService.listarPorTipo(tipo));
    }

    // GET /api/v1/conta/periodo?tipo=PAGAR&inicio=2024-01-01&fim=2024-01-31
    @GetMapping("/periodo")
    public ResponseEntity<List<ContaDTO>> listarPorPeriodo(
            @RequestParam String tipo,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(contaService.listarPorTipoPeriodo(tipo, inicio, fim));
    }

    // GET /api/v1/conta/alertas?dias=7
    @GetMapping("/alertas")
    public ResponseEntity<List<ContaAlertaDTO>> alertas(
            @RequestParam(defaultValue = "7") int dias) {
        return ResponseEntity.ok(contaService.buscarAlertas(dias));
    }
}
