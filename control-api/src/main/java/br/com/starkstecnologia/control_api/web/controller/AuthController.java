package br.com.starkstecnologia.control_api.web.controller;

import br.com.starkstecnologia.control_api.jwt.JwtUserDetailsService;
import br.com.starkstecnologia.control_api.web.dto.UsuarioLoginDto;
import br.com.starkstecnologia.control_api.web.exception.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final JwtUserDetailsService detailsService;
    private final AuthenticationManager authenticationManager;

    public AuthController(JwtUserDetailsService detailsService, AuthenticationManager authenticationManager) {
        this.detailsService = detailsService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/auth")
    public ResponseEntity<?> autenticar(@RequestBody @Valid UsuarioLoginDto dto, HttpServletRequest request){
        log.info("Processo de autenticação pelo login");
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
            authenticationManager.authenticate(authenticationToken);

            return ResponseEntity.ok(detailsService.getTokenAuthenticated(dto.getUsername()));
        }catch (AuthenticationException ex){
            log.warn("Bad Credentials from username");
        }
        return ResponseEntity.badRequest().body(new ErrorMessage(request, HttpStatus.FORBIDDEN, "Credenciais inválidas"));
    }
}