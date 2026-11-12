package br.com.starkstecnologia.control_api.jwt;

import br.com.starkstecnologia.control_api.dto.DadosRetornoLoginDTO;
import br.com.starkstecnologia.control_api.entity.Usuario;
import br.com.starkstecnologia.control_api.exception.ServiceException;
import br.com.starkstecnologia.control_api.services.PermissaoService;
import br.com.starkstecnologia.control_api.services.UsuarioService;
import br.com.starkstecnologia.control_api.types.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    PermissaoService permissaoService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.buscarPorUsername(username);
        return new JwtUserDetails(usuario);
    }

    public DadosRetornoLoginDTO getTokenAuthenticated(String username) {
        Role role = usuarioService.buscarRolePorUsername(username);
        Usuario usuario = usuarioService.buscarPorUsername(username);
        if (!usuario.isAtivo()) {
            throw new ServiceException("Usuário inativado");
        }
        DadosRetornoLoginDTO dadosRetornoLoginDTO = new DadosRetornoLoginDTO();
        dadosRetornoLoginDTO.setToken(JwtUtils.createToken(username, role.getDescricao()).getToken());
        dadosRetornoLoginDTO.setNome(usuario.getNome());
        dadosRetornoLoginDTO.setIdUser(usuario.getId());
        dadosRetornoLoginDTO.setRole(role.getDescricao());
        dadosRetornoLoginDTO.setPermissoes(permissaoService.buscarChavesPorUsuario(username));
        dadosRetornoLoginDTO.setPrimeiroAcesso(usuario.isPrimeiroAcesso());
        return dadosRetornoLoginDTO;
    }
}
