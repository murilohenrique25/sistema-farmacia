package br.com.starkstecnologia.control_api.services;

import br.com.starkstecnologia.control_api.dto.ProdutoDTO;
import br.com.starkstecnologia.control_api.entity.Produto;
import br.com.starkstecnologia.control_api.exception.BusinessException;
import br.com.starkstecnologia.control_api.exception.EntityNotFoundException;
import br.com.starkstecnologia.control_api.repository.LoteRepository;
import br.com.starkstecnologia.control_api.repository.MatrizRepository;
import br.com.starkstecnologia.control_api.repository.ProdutoRepository;
import br.com.starkstecnologia.control_api.utils.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final LoteRepository loteRepository;
    private final MatrizRepository matrizRepository;

    public ProdutoService(ProdutoRepository produtoRepository,
                          LoteRepository loteRepository,
                          MatrizRepository matrizRepository) {
        this.produtoRepository = produtoRepository;
        this.loteRepository = loteRepository;
        this.matrizRepository = matrizRepository;
    }

    @Transactional(readOnly = true)
    public List<ProdutoDTO> listar() {
        Long idMatriz = StringUtils.idMatrizUsuarioAutenticado();
        return produtoRepository.findByMatrizIdMatrizOrderByNomeAsc(idMatriz)
                .stream().map(ProdutoDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public ProdutoDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto id=" + id + " não encontrado"));
        Long idMatriz = StringUtils.idMatrizUsuarioAutenticado();
        if (!produto.getMatriz().getIdMatriz().equals(idMatriz)) {
            throw new BusinessException("Acesso negado", HttpStatus.FORBIDDEN);
        }
        return new ProdutoDTO(produto);
    }

    @Transactional
    public ProdutoDTO criar(ProdutoDTO dto) {
        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new BusinessException("Campo obrigatório ausente: nome", HttpStatus.BAD_REQUEST);
        }
        if (dto.getCodigo() == null || dto.getCodigo().isBlank()) {
            throw new BusinessException("Campo obrigatório ausente: codigo", HttpStatus.BAD_REQUEST);
        }
        if (dto.getUnidadeMedida() == null || dto.getUnidadeMedida().isBlank()) {
            throw new BusinessException("Campo obrigatório ausente: unidadeMedida", HttpStatus.BAD_REQUEST);
        }
        Long idMatriz = StringUtils.idMatrizUsuarioAutenticado();
        if (produtoRepository.existsByCodigoAndMatrizIdMatriz(dto.getCodigo(), idMatriz)) {
            throw new BusinessException(
                    "Já existe um produto com o código '" + dto.getCodigo() + "' para esta matriz",
                    HttpStatus.CONFLICT);
        }
        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setCodigo(dto.getCodigo());
        produto.setCategoria(dto.getCategoria());
        produto.setUnidadeMedida(dto.getUnidadeMedida());
        produto.setStatus(dto.getStatus() != null ? dto.getStatus() : "ATIVO");
        produto.setMatriz(matrizRepository.getReferenceById(idMatriz));
        return new ProdutoDTO(produtoRepository.save(produto));
    }

    @Transactional
    public ProdutoDTO atualizar(Long id, ProdutoDTO dto) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto id=" + id + " não encontrado"));
        Long idMatriz = StringUtils.idMatrizUsuarioAutenticado();
        if (!produto.getMatriz().getIdMatriz().equals(idMatriz)) {
            throw new BusinessException("Acesso negado", HttpStatus.FORBIDDEN);
        }
        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new BusinessException("Campo obrigatório ausente: nome", HttpStatus.BAD_REQUEST);
        }
        if (dto.getCodigo() == null || dto.getCodigo().isBlank()) {
            throw new BusinessException("Campo obrigatório ausente: codigo", HttpStatus.BAD_REQUEST);
        }
        if (dto.getUnidadeMedida() == null || dto.getUnidadeMedida().isBlank()) {
            throw new BusinessException("Campo obrigatório ausente: unidadeMedida", HttpStatus.BAD_REQUEST);
        }
        if (produtoRepository.existsByCodigoAndMatrizIdMatrizAndIdProdutoNot(dto.getCodigo(), idMatriz, id)) {
            throw new BusinessException(
                    "Já existe um produto com o código '" + dto.getCodigo() + "' para esta matriz",
                    HttpStatus.CONFLICT);
        }
        produto.setNome(dto.getNome());
        produto.setCodigo(dto.getCodigo());
        produto.setCategoria(dto.getCategoria());
        produto.setUnidadeMedida(dto.getUnidadeMedida());
        if (dto.getStatus() != null) produto.setStatus(dto.getStatus());
        return new ProdutoDTO(produtoRepository.save(produto));
    }

    @Transactional
    public void excluir(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto id=" + id + " não encontrado"));
        Long idMatriz = StringUtils.idMatrizUsuarioAutenticado();
        if (!produto.getMatriz().getIdMatriz().equals(idMatriz)) {
            throw new BusinessException("Acesso negado", HttpStatus.FORBIDDEN);
        }
        BigDecimal saldo = loteRepository.sumQuantidadeAtivaByProduto(id);
        if (saldo != null && saldo.compareTo(BigDecimal.ZERO) > 0) {
            throw new BusinessException(
                    "Não é possível excluir o produto pois possui saldo em estoque",
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }
        produtoRepository.delete(produto);
    }
}
