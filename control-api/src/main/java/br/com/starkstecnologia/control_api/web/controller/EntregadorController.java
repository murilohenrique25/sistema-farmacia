package br.com.starkstecnologia.control_api.web.controller;


import br.com.starkstecnologia.control_api.dto.DadosCaixaEntregadorDTO;
import br.com.starkstecnologia.control_api.exception.EntityNotFoundException;
import br.com.starkstecnologia.control_api.services.EntregadorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/entregador")
@Tag(name = "Entregador", description = "Operações referente as funções de Entregador")
public class EntregadorController {

    @Autowired
    EntregadorService entregadorService;

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody DadosCaixaEntregadorDTO entregadorDTO) throws EntityNotFoundException {
        entregadorService.salvar(entregadorDTO);
        return ResponseEntity.status(201).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> atualizar(@PathVariable("id") Long id, @RequestBody DadosCaixaEntregadorDTO entregadorDTO){
        return ResponseEntity.ok(entregadorService.atualizar(id, entregadorDTO));
    }

    @GetMapping
    public ResponseEntity<?> listarTodos(){
        return ResponseEntity.ok(entregadorService.listarTodos());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable("id") Long id){
        return ResponseEntity.ok(entregadorService.buscarPorId(id));
    }

    @PutMapping(value = "/desativar/{id}")
    public ResponseEntity<?> desativarEntregador(@PathVariable("id") Long idEntregador){
        return entregadorService.desativarEntregador(idEntregador);
    }

}
