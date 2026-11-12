package br.com.starkstecnologia.control_api.web.controller.app;

import br.com.starkstecnologia.control_api.dto.DadosEntregaAvulsaDTO;
import br.com.starkstecnologia.control_api.dto.DadosEntregaDTO;
import br.com.starkstecnologia.control_api.dto.DadosEntregaFinalizacaoDTO;
import br.com.starkstecnologia.control_api.dto.IniciarEntregaDTO;
import br.com.starkstecnologia.control_api.services.EntregaService;
import br.com.starkstecnologia.control_api.services.EntregadorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/app/v1/entrega")
@Tag(name = "Entrega App", description = "Operações referente as entregas no APP")
public class EntregaAppEndpoint {

    @Autowired
    EntregaService entregaService;

    @Autowired
    EntregadorService entregadorService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> entregasDisponiveis(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(entregaService.entregasDisponiveis(userId));
    }

    @GetMapping("tipo/{tipo_entrega}/{userId}")
    public ResponseEntity<?> entregasDisponiveisTipo(@PathVariable("tipo_entrega") String tipoEntrega,
            @PathVariable("userId") String userId) {
        return ResponseEntity.ok(entregaService.entregasDisponiveisPorTipo(tipoEntrega, userId));
    }

    @GetMapping("quantidade_tipo/{userId}")
    public ResponseEntity<?> entregasDisponiveisPorTipo(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(entregaService.quantidadeEntregasDisponiveisPorTipo(userId));
    }

    @PutMapping("/selecionar/{userId}")
    public ResponseEntity<?> selecionarEntrega(@RequestBody List<DadosEntregaDTO> entregaDTOS,
            @PathVariable("userId") String userId) {
        entregaService.selecionarAtribuirAlterarEntrega(entregaDTOS, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/iniciar/{userId}")
    public ResponseEntity<?> iniciarEntrega(@Valid @RequestBody IniciarEntregaDTO dto,
            @PathVariable("userId") String userId) {
        entregaService.iniciarEntrega(dto.getIdsEntrega(), userId, dto.getLatitude(), dto.getLongitude());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/entregador/{userId}")
    public ResponseEntity<?> entregasByIdEntregador(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(entregaService.entregasByIdEntregador(userId));
    }

    @PutMapping("/finalizar")
    public ResponseEntity<?> finalizarEntrega(@RequestBody DadosEntregaFinalizacaoDTO entregaDTOS) {
        entregaService.finalizarEntrega(entregaDTOS);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/avulsa/{user_id}")
    public ResponseEntity<?> inserirEntregaAlvusa(@RequestBody DadosEntregaAvulsaDTO entregaDTO,
            @PathVariable("user_id") String userId) {
        entregaService.salvarEntregaAvulsa(entregaDTO, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/quantidade/{user_id}")
    public ResponseEntity<?> quantidadeEntregas(@PathVariable("user_id") String userId) {
        return ResponseEntity.ok(entregadorService.getQuantidadeEntregas(userId));
    }

    @GetMapping(value = "/abertas/{user_id}")
    public ResponseEntity<?> entregasEmAberto(@PathVariable("user_id") String userId) {
        return ResponseEntity.ok(entregadorService.buscarDadosEntregasApp(userId));
    }

}
