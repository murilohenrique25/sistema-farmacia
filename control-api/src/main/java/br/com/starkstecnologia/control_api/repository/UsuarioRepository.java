package br.com.starkstecnologia.control_api.repository;

import br.com.starkstecnologia.control_api.entity.Usuario;
import br.com.starkstecnologia.control_api.types.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    @Query("select u.role from Usuario u where u.username like :username")
    Role findRoleByUsername(String username);

    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nome) LIKE LOWER(CONCAT(:nomeAtendente, '%'))")
    List<Usuario> buscarPorNome(@Param("nomeAtendente") String nomeAtendente);
}
