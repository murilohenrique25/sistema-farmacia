package br.com.starkstecnologia.control_api.services;

import br.com.starkstecnologia.control_api.entity.Permissao;
import br.com.starkstecnologia.control_api.entity.Usuario;
import br.com.starkstecnologia.control_api.exception.EntityNotFoundException;
import br.com.starkstecnologia.control_api.repository.PermissaoRepository;
import br.com.starkstecnologia.control_api.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class PermissaoService {

    private final PermissaoRepository permissaoRepository;
    private final UsuarioRepository usuarioRepository;

    public PermissaoService(PermissaoRepository permissaoRepository, UsuarioRepository usuarioRepository) {
        this.permissaoRepository = permissaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<Permissao> listarTodas() {
        return permissaoRepository.findAll();
    }

    @Transactional
    public Permissao salvar(Permissao permissao) {
        return permissaoRepository.save(permissao);
    }

    @Transactional(readOnly = true)
    public List<String> buscarChavesPorUsuario(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + username));
        return usuario.getPermissoes().stream()
                .map(Permissao::getChave)
                .toList();
    }

    @Transactional
    public void vincularPermissoes(Long idUsuario, Set<Long> idsPermissoes) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuário id=" + idUsuario + " não encontrado"));

        Set<Permissao> permissoes = new java.util.HashSet<>(permissaoRepository.findAllById(idsPermissoes));
        usuario.setPermissoes(permissoes);
        usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Set<Permissao> buscarPermissoesDoUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuário id=" + idUsuario + " não encontrado"));
        return usuario.getPermissoes();
    }
}
