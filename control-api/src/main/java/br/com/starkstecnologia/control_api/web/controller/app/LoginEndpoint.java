package br.com.starkstecnologia.control_api.web.controller.app;

import br.com.starkstecnologia.control_api.exception.EntityNotFoundException;
import br.com.starkstecnologia.control_api.services.EntregadorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app/v1/login")
@Tag(name = "Login App", description = "Operações referente ao login no APP")
public class LoginEndpoint {

    @Autowired
    EntregadorService entregadorService;

    @GetMapping()
    public ResponseEntity<?> logarViaApp(@RequestParam(value = "user_id") String userId,
            @RequestParam(value = "password") String password) throws EntityNotFoundException {
        return ResponseEntity.ok(entregadorService.logarApp(userId, password));
    }

    @GetMapping("/dados")
    public ResponseEntity<?> buscarDadosEntregador(@RequestParam(value = "user_id") String userId) {
        return ResponseEntity.ok(entregadorService.getQuantidadeEntregas(userId));
    }
}
