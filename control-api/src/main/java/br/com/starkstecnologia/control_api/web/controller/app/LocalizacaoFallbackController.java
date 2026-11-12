package br.com.starkstecnologia.control_api.web.controller.app;

import br.com.starkstecnologia.control_api.dto.LocalizacaoEntregaDTO;
import br.com.starkstecnologia.control_api.dto.WsAckDTO;
import br.com.starkstecnologia.control_api.jwt.JwtUtils;
import br.com.starkstecnologia.control_api.services.LocalizacaoEntregaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/app/v1/localizacao")
public class LocalizacaoFallbackController {

    @Autowired
    private LocalizacaoEntregaService localizacaoEntregaService;

    @PostMapping
    public ResponseEntity<WsAckDTO> atualizarLocalizacao(
            @Valid @RequestBody LocalizacaoEntregaDTO dto,
            HttpServletRequest request) {

        String token = request.getHeader(JwtUtils.JWT_AUTHORIZATION);
        String userId = JwtUtils.getUsernameFromToken(token);

        localizacaoEntregaService.atualizarLocalizacao(userId, dto);

        WsAckDTO ack = new WsAckDTO(
                "OK",
                dto.getIdEntrega(),
                null,
                LocalDateTime.now().toString()
        );

        return ResponseEntity.ok(ack);
    }
}
