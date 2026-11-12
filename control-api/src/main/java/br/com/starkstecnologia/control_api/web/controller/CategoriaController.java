package br.com.starkstecnologia.control_api.web.controller;

import br.com.starkstecnologia.control_api.dto.CategoriaDTO;
import br.com.starkstecnologia.control_api.services.CategoriaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categoria")
@Tag(name = "Categorias", description = "CRUD de categorias de contas")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<CategoriaDTO> criar(@RequestBody CategoriaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.salvar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> atualizar(@PathVariable Long id, @RequestBody CategoriaDTO dto) {
        return ResponseEntity.ok(categoriaService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        categoriaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> listarTodas() {
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<CategoriaDTO>> listarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(categoriaService.listarAtivasPorTipo(tipo));
    }
}
