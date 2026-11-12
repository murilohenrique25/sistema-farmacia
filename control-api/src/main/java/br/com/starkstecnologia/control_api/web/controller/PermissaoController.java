package br.com.starkstecnologia.control_api.web.controller;

import br.com.starkstecnologia.control_api.dto.PermissaoDTO;
import br.com.starkstecnologia.control_api.entity.Permissao;
import br.com.starkstecnologia.control_api.services.PermissaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/permissao")
@Tag(name = "Permissões", description = "Gerenciamento de permissões de acesso")
public class PermissaoController {

    private final PermissaoService permissaoService;

    public PermissaoController(PermissaoService permissaoService) {
        this.permissaoService = permissaoService;
    }

    @GetMapping
    public ResponseEntity<List<PermissaoDTO>> listarTodas() {
        List<PermissaoDTO> lista = permissaoService.listarTodas().stream()
                .map(PermissaoDTO::new)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<PermissaoDTO> criar(@RequestBody PermissaoDTO dto) {
        Permissao permissao = new Permissao(dto.getChave(), dto.getDescricao());
        return ResponseEntity.status(HttpStatus.CREATED).body(new PermissaoDTO(permissaoService.salvar(permissao)));
    }

    // Retorna permissões vinculadas a um usuário
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Set<PermissaoDTO>> buscarPorUsuario(@PathVariable Long idUsuario) {
        Set<PermissaoDTO> permissoes = permissaoService.buscarPermissoesDoUsuario(idUsuario).stream()
                .map(PermissaoDTO::new)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(permissoes);
    }

    // Vincula/substitui permissões de um usuário
    @PutMapping("/usuario/{idUsuario}")
    public ResponseEntity<Void> vincularPermissoes(@PathVariable Long idUsuario, @RequestBody Set<Long> idsPermissoes) {
        permissaoService.vincularPermissoes(idUsuario, idsPermissoes);
        return ResponseEntity.noContent().build();
    }
}
