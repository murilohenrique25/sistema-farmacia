package br.com.starkstecnologia.control_api.web.controller;

import br.com.starkstecnologia.control_api.dto.ProdutoDTO;
import br.com.starkstecnologia.control_api.services.ProdutoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/estoque/produto")
@Tag(name = "Estoque - Produtos", description = "Gerenciamento de produtos do estoque")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ACESSAR_ESTOQUE') or hasAuthority('GERENCIAR_ESTOQUE')")
    public ResponseEntity<List<ProdutoDTO>> listar() {
        return ResponseEntity.ok(produtoService.listar());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ACESSAR_ESTOQUE') or hasAuthority('GERENCIAR_ESTOQUE')")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('GERENCIAR_ESTOQUE')")
    public ResponseEntity<ProdutoDTO> criar(@RequestBody ProdutoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.criar(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('GERENCIAR_ESTOQUE')")
    public ResponseEntity<ProdutoDTO> atualizar(@PathVariable Long id, @RequestBody ProdutoDTO dto) {
        return ResponseEntity.ok(produtoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('GERENCIAR_ESTOQUE')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        produtoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
