package br.com.starkstecnologia.control_api.web.controller;


import br.com.starkstecnologia.control_api.dto.DadosAtribuirFinalizarEntregaDTO;
import br.com.starkstecnologia.control_api.dto.DadosEntregaAlterarDTO;
import br.com.starkstecnologia.control_api.dto.DadosEntregaDTO;
import br.com.starkstecnologia.control_api.services.EntregaService;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/entrega")
@Tag(name = "Entrega", description = "Operações referente as entregas")
public class EntregaEndpoint {

    @Autowired
    EntregaService entregaService;

    @PostMapping
    public ResponseEntity<?> salvarEntrega(@RequestBody DadosEntregaDTO dadosEntregaDTO){
        return ResponseEntity.status(201).body(entregaService.salvarEntrega(dadosEntregaDTO));
    }
    @PostMapping("/automatizada")
    public ResponseEntity<?> salvarEntregaAutomatizada(@RequestBody DadosEntregaDTO dadosEntregaDTO){
        return ResponseEntity.status(201).body(entregaService.salvarEntregaAutomatizada(dadosEntregaDTO));
    }

    @PutMapping("/alterar/{idEntrega}")
    public ResponseEntity<?> alterarEntrega(@PathVariable("idEntrega") Long idEntrega, @RequestBody DadosEntregaAlterarDTO dadosEntregaDTO){
        entregaService.alterarEntrega(idEntrega, dadosEntregaDTO);
        return ResponseEntity.status(200).build();
    }

    @PutMapping(value = "/assinar/{idUsuario}")
    public ResponseEntity<?> assinarEntregas(@RequestBody List<Long> entregaDTOList, @PathVariable("idUsuario") Long idUsuario){
        entregaService.assinarEntregaCaixa(entregaDTOList, idUsuario);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/atribuir/{idEntrega}")
    public ResponseEntity<?> selecionarEntrega(@RequestBody DadosAtribuirFinalizarEntregaDTO entregaDTO, @PathVariable("idEntrega") Long idEntrega){
        entregaService.selecionarAtribuirAlterarEntrega(entregaDTO, idEntrega);
        return ResponseEntity.ok().build();
    }
    @PutMapping(value = "/alterar/{idEntrega}/entregador/{idEntregador}")
    public ResponseEntity<?> alterarEntregadorEntrega(@PathVariable("idEntregador") Long idEntregador, @PathVariable("idEntrega") Long idEntrega){
        entregaService.alterarEntregadorEntrega(idEntregador, idEntrega);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/finalizar/{idEntrega}")
    public ResponseEntity<?> finalizarEntregaManual(@PathVariable("idEntrega") Long idEntrega){
        entregaService.finalizarEntregaManual(idEntrega);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/cancelar/{idEntrega}")
    public ResponseEntity<?> cancelarEntrega(@PathVariable("idEntrega") Long idEntrega){
        entregaService.cancelarEntrega(idEntrega);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> buscarEntregas(@RequestParam(required = false) Long idEntrega,
                                             @RequestParam(required = false) LocalDateTime dataInicio,
                                             @RequestParam(required = false) LocalDateTime dataTermino,
                                            @RequestParam(required = false) List<String> statusEntrega,
                                            @RequestParam(required = false) List<Long> idsEntregador
                                            ){
       return ResponseEntity.ok(entregaService.buscarEntregas(idEntrega, dataInicio, dataTermino, statusEntrega, idsEntregador));
    }
    @GetMapping("/entregas-assinar/{dataInicio}/{dataFim}")
    public ResponseEntity<?> buscarEntregasAssinar(@PathVariable("dataInicio") LocalDateTime dataInicio, @PathVariable("dataFim") LocalDateTime dataFim,
                                                   @RequestParam(value = "idsEntregador", required = false) List<Long> idsEntregador){
       return ResponseEntity.ok(entregaService.buscarEntregasAssinar(idsEntregador, dataInicio, dataFim));
    }

    @GetMapping("/avulsas")
    public ResponseEntity<?> buscarTodasEntregasAvulsas(){
        return ResponseEntity.ok(entregaService.buscarTodasEntregasAvulsas());
    }

    @GetMapping("/total-entregas")
    public ResponseEntity<?> buscarTodasEntregasMes(){
        return ResponseEntity.ok(entregaService.buscarTodasEntregasMes());
    }

    @GetMapping("/mapa-intervalo")
    public ResponseEntity<?> buscarEntregasPorIntervalor(@RequestParam(required = false) LocalDateTime dataInicio,
                                            @RequestParam(required = false) LocalDateTime dataTermino){
        return ResponseEntity.ok(entregaService.getEntregasPorIntervalor( dataInicio, dataTermino));
    }


}
