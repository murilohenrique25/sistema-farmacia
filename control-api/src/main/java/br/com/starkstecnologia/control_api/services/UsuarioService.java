package br.com.starkstecnologia.control_api.services;

import br.com.starkstecnologia.control_api.entity.Usuario;
import br.com.starkstecnologia.control_api.exception.EntityNotFoundException;
import br.com.starkstecnologia.control_api.exception.PasswordInvalidException;
import br.com.starkstecnologia.control_api.exception.ServiceException;
import br.com.starkstecnologia.control_api.exception.UsernameUniqueViolationException;
import br.com.starkstecnologia.control_api.repository.MatrizRepository;
import br.com.starkstecnologia.control_api.repository.UsuarioRepository;

import br.com.starkstecnologia.control_api.types.Role;
import br.com.starkstecnologia.control_api.utils.StringUtils;
import br.com.starkstecnologia.control_api.web.controller.UsuarioController;
import br.com.starkstecnologia.control_api.web.dto.UsuarioCreateDto;
import br.com.starkstecnologia.control_api.web.dto.UsuarioResponseDto;
import br.com.starkstecnologia.control_api.web.dto.UsuarioSenhaDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    private final MatrizRepository matrizRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
            MatrizRepository matrizRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.matrizRepository = matrizRepository;
    }

    @Transactional
    public Usuario salvar(UsuarioCreateDto usuarioCreateDto) {
        try {
            Usuario usuario = new Usuario();
            usuario.setTelefone(usuarioCreateDto.getTelefone());
            usuario.setTurno(usuarioCreateDto.getTurno());
            usuario.setCpf(usuarioCreateDto.getCpf());
            usuario.setNome(usuarioCreateDto.getNome());
            usuario.setAtivo(usuarioCreateDto.isAtivo());
            usuario.setUsername(usuarioCreateDto.getUsername());
            usuario.setPassword(passwordEncoder.encode(usuarioCreateDto.getPassword()));
            usuario.setPrimeiroAcesso(true);
            usuario.setMatriz(matrizRepository.findById(usuarioCreateDto.getIdMatriz())
                    .orElseThrow(() -> new ServiceException("Matriz não encontrada")));
            return usuarioRepository.save(usuario);
        } catch (DataIntegrityViolationException ex) {
            throw new UsernameUniqueViolationException(
                    String.format("Username {%s} já cadastrado", usuarioCreateDto.getUsername()));
        }
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDto buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Usuário id=%s não encontrado", id)));
        return new UsuarioResponseDto(usuario);
    }

    @Transactional
    public void editarUsuario(Long id, UsuarioSenhaDto dto) {
        Usuario user = usuarioRepository.getReferenceById(id);
        if (dto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(dto.getNovaSenha()));
            user.setPrimeiroAcesso(true);
        }
        user.setAtivo(dto.getAtivo());
        user.setCpf(dto.getCpf());
        user.setNome(dto.getNome());
        user.setTurno(dto.getTurno());
        user.setTelefone(dto.getTelefone());
    }

    @Transactional
    public Usuario editarUsuarioValidacaoSenha(UsuarioSenhaDto dto) {
        Usuario user = usuarioRepository.getReferenceById(dto.getId());
        if (dto.getNovaSenha() != null && dto.getConfirmacaoSenha() != null) {

            if (!dto.getNovaSenha().equals(dto.getConfirmacaoSenha())) {
                throw new PasswordInvalidException("Nova senha não confere com confirmação de senha.");
            }

            if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
                throw new PasswordInvalidException("Sua senha não confere.");
            }
            user.setPassword(passwordEncoder.encode(dto.getNovaSenha()));
            user.setPrimeiroAcesso(false);
        }
        if (dto.getAtivo() != null)
            user.setAtivo(dto.getAtivo());
        if (dto.getCpf() != null)
            user.setCpf(dto.getCpf());
        if (dto.getNome() != null)
            user.setNome(dto.getNome());
        if (dto.getTurno() != null)
            user.setTurno(dto.getTurno());
        if (dto.getTelefone() != null)
            user.setTelefone(dto.getTelefone());
        return user;
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
        Long idMatrizAutenticado = StringUtils.idMatrizUsuarioAutenticado();
        return usuarioRepository.findAll().stream()
                .filter(usuario -> usuario.getMatriz().getIdMatriz().equals(idMatrizAutenticado)).toList();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(String.format("Usuário com username %s não encontrado", username)));
    }

    @Transactional(readOnly = true)
    public Role buscarRolePorUsername(String username) {
        return usuarioRepository.findRoleByUsername(username);
    }
}
