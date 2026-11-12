package br.com.starkstecnologia.control_api.web.controller;

import br.com.starkstecnologia.control_api.dto.*;
import br.com.starkstecnologia.control_api.services.MovimentacaoService;
import br.com.starkstecnologia.control_api.services.NFeParserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/estoque/movimentacao")
@Tag(name = "Estoque - Movimentações", description = "Registro e consulta de movimentações de estoque")
public class MovimentacaoController {

    private final MovimentacaoService movimentacaoService;
    private final NFeParserService nfeParserService;

    public MovimentacaoController(MovimentacaoService movimentacaoService, NFeParserService nfeParserService) {
        this.movimentacaoService = movimentacaoService;
        this.nfeParserService = nfeParserService;
    }

    @PostMapping("/entrada")
    @PreAuthorize("hasAuthority('GERENCIAR_ESTOQUE')")
    public ResponseEntity<EntradaResponseDTO> registrarEntrada(@RequestBody EntradaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movimentacaoService.registrarEntrada(dto));
    }

    @PostMapping("/entrada/nfe")
    @PreAuthorize("hasAuthority('GERENCIAR_ESTOQUE')")
    public ResponseEntity<EntradaResponseDTO> importarNfe(@RequestBody EntradaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movimentacaoService.registrarEntrada(dto));
    }

    @PostMapping("/saida")
    @PreAuthorize("hasAuthority('GERENCIAR_ESTOQUE')")
    public ResponseEntity<SaidaResponseDTO> registrarSaida(@RequestBody SaidaRequestDTO dto) {
        return ResponseEntity.ok(movimentacaoService.registrarSaida(dto));
    }

    @GetMapping("/historico")
    @PreAuthorize("hasAuthority('ACESSAR_ESTOQUE') or hasAuthority('GERENCIAR_ESTOQUE')")
    public ResponseEntity<List<MovimentacaoDTO>> listarHistorico(
            @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime fim,
            @RequestParam(required = false) Long idProduto) {
        return ResponseEntity.ok(movimentacaoService.listarHistorico(inicio, fim, idProduto));
    }

    @GetMapping("/posicao")
    @PreAuthorize("hasAuthority('ACESSAR_ESTOQUE') or hasAuthority('GERENCIAR_ESTOQUE')")
    public ResponseEntity<List<PosicaoEstoqueDTO>> posicaoEstoque() {
        return ResponseEntity.ok(movimentacaoService.posicaoEstoque());
    }

    @GetMapping("/historico/csv")
    @PreAuthorize("hasAuthority('ACESSAR_ESTOQUE') or hasAuthority('GERENCIAR_ESTOQUE')")
    public ResponseEntity<byte[]> exportarHistoricoCsv(
            @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime fim) {
        List<MovimentacaoDTO> historico = movimentacaoService.listarHistorico(inicio, fim, null);
        byte[] csv = movimentacaoService.exportarCsv(historico);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"historico.csv\"");
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        return ResponseEntity.ok().headers(headers).body(csv);
    }

    @GetMapping("/posicao/csv")
    @PreAuthorize("hasAuthority('ACESSAR_ESTOQUE') or hasAuthority('GERENCIAR_ESTOQUE')")
    public ResponseEntity<byte[]> exportarPosicaoCsv() {
        List<PosicaoEstoqueDTO> posicao = movimentacaoService.posicaoEstoque();
        byte[] csv = movimentacaoService.exportarCsv(posicao);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"posicao.csv\"");
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        return ResponseEntity.ok().headers(headers).body(csv);
    }

    @PostMapping("/nfe/preview")
    @PreAuthorize("hasAuthority('GERENCIAR_ESTOQUE')")
    public ResponseEntity<List<EntradaItemDTO>> previewNfe(@RequestParam("arquivo") MultipartFile arquivo) {
        return ResponseEntity.ok(nfeParserService.parsear(arquivo));
    }
}
