package br.com.starkstecnologia.control_api.web.controller;

import br.com.starkstecnologia.control_api.dto.DadosFormaPagamentoDTO;
import br.com.starkstecnologia.control_api.services.FormaPagamentoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/forma-pagamento")
@Tag(name = "Forma Pagamento", description = "Operações referente as formas de pagamento")
public class FormaPagamentoEndpoint {

    @Autowired
    FormaPagamentoService formaPagamentoService;

    @PostMapping
    public ResponseEntity<?> salvarFormaPagamento(@RequestBody DadosFormaPagamentoDTO dados){
        formaPagamentoService.salvarFormaPagamento(dados);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/alterar/{idFormaPagamento}")
    public ResponseEntity<?> editarFormaPagamento(@PathVariable("idFormaPagamento") Long idFormaPagamento,@RequestBody DadosFormaPagamentoDTO dados){
        formaPagamentoService.editarFormaPagamento(idFormaPagamento, dados);
        return ResponseEntity.status(200).build();
    }

    @GetMapping
    public ResponseEntity<?> listarTodos(){
        return ResponseEntity.ok(formaPagamentoService.listarTodos());
    }
    @GetMapping("/ativos")
    public ResponseEntity<?> listarTodosAtivos(){
        return ResponseEntity.ok(formaPagamentoService.listarTodosAtivos());
    }
}
