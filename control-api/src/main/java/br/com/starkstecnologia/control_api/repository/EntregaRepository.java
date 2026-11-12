package br.com.starkstecnologia.control_api.repository;

import br.com.starkstecnologia.control_api.entity.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Long>, EntregaRepositoryCustom{
	
	@Query("SELECT e FROM Entrega e WHERE e.idEntrega IN (:idEntregas)")
	List<Entrega> buscarEntregasPorId(@Param("idEntregas") List<Long> dados);

	@Query("SELECT e FROM Entrega e WHERE e.idEntrega = :idEntrega")
	Entrega buscarEntregaPorId(@Param("idEntrega") Long idEntrega);

	@Query("SELECT e FROM Entrega e WHERE e.entregador IS NULL AND e.matriz.idMatriz = :idMatriz AND tipoEntrega = :tipoEntrega AND statusEntrega = 'Aberta'")
	List<Entrega> entregasDisponiveisPorTipo(@Param("idMatriz") Long idMatriz, @Param("tipoEntrega") String tipoEntrega);

	@Query("SELECT e FROM Entrega e WHERE e.entregador IS NULL")
	List<Entrega> buscarEntregasDisponiveis();


	@Query("SELECT e FROM Entrega e WHERE e.entregador.idEntregador =:idEntregador AND" +
			" dataFinalizacaoEntrega IS NULL AND statusEntrega != 'Cancelada'")
	List<Entrega> buscarEntregasByIdEntregador(Long idEntregador);

	@Query(value = "SELECT EXISTS (SELECT 1 FROM entrega WHERE id_entregador = :idEntregador)", nativeQuery = true)
	Boolean existeEntregaVinculadaEntregador(@Param("idEntregador") Long idEntregador);

}
