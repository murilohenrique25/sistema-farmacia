package br.com.starkstecnologia.control_api.web.controller;

import br.com.starkstecnologia.control_api.entity.Usuario;
import br.com.starkstecnologia.control_api.services.UsuarioService;
import br.com.starkstecnologia.control_api.web.dto.UsuarioCreateDto;
import br.com.starkstecnologia.control_api.web.dto.UsuarioResponseDto;
import br.com.starkstecnologia.control_api.web.dto.UsuarioSenhaDto;
import br.com.starkstecnologia.control_api.web.dto.mapper.UsuarioMapper;
import br.com.starkstecnologia.control_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/usuario")
@Tag(name = "Usuários", description = "Contém todas operações para cadastro, edição e leitura de um usuário")
public class UsuarioController {

        private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);
        @Autowired
        UsuarioService usuarioService;

        @Operation(summary = "Criar um novo usuário", description = "Recurso para criar novo usuário", responses = {
                        @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                        @ApiResponse(responseCode = "409", description = "Usuario email já cadastrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                        @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
        })
        @PostMapping
        public ResponseEntity<?> create(@Valid @RequestBody UsuarioCreateDto usuarioCreateDto) {
                return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.salvar(usuarioCreateDto));
        }

        @Operation(summary = "Retornar usuário pelo id", description = "Retornar usuário pelo id", responses = {
                        @ApiResponse(responseCode = "200", description = "Recurso retornado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),

                        @ApiResponse(responseCode = "404", description = "Recurso não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
        })
        @GetMapping("/{id}")
        public ResponseEntity<UsuarioResponseDto> getById(@PathVariable Long id) {
                return ResponseEntity.ok(usuarioService.buscarPorId(id));
        }

        @Operation(summary = "Recurso para atualizar usuário", description = "Recurso para atualizar usuário", responses = {
                        @ApiResponse(responseCode = "204", description = "Recurso alterado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema())),
                        @ApiResponse(responseCode = "400", description = "Senha incorreta", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                        @ApiResponse(responseCode = "404", description = "Recurso não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
        })
        @PutMapping("/{id}")
        public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody UsuarioSenhaDto dto) {
                usuarioService.editarUsuario(id, dto);
                return ResponseEntity.status(201).build();
        }

        @PutMapping("/alterar-senha")
        public ResponseEntity<?> alterarSenha(@RequestBody UsuarioSenhaDto dto) {
                try {
                        usuarioService.editarUsuarioValidacaoSenha(dto);
                        return ResponseEntity.ok().build();
                } catch (Exception e) {
                        return ResponseEntity.badRequest().body(e.getMessage());
                }
        }

        @Operation(summary = "Recurso para listar todos usuários", description = "Recurso para listar todos usuários", responses = {
                        @ApiResponse(responseCode = "200", description = "Lista com todos usuários cadastrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class)))

        })
        @GetMapping
        public ResponseEntity<List<UsuarioResponseDto>> getAll() {
                List<Usuario> usuarios = usuarioService.buscarTodos();
                return ResponseEntity.ok(UsuarioMapper.toListDto(usuarios));
        }
}
