package br.com.starkstecnologia.control_api.services;

import br.com.starkstecnologia.control_api.dto.AlertaVencimentoDTO;
import br.com.starkstecnologia.control_api.dto.LoteDTO;
import br.com.starkstecnologia.control_api.entity.LoteProduto;
import br.com.starkstecnologia.control_api.entity.Produto;
import br.com.starkstecnologia.control_api.exception.BusinessException;
import br.com.starkstecnologia.control_api.exception.EntityNotFoundException;
import br.com.starkstecnologia.control_api.repository.LoteRepository;
import br.com.starkstecnologia.control_api.repository.ProdutoRepository;
import br.com.starkstecnologia.control_api.utils.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoteService {

    private final LoteRepository loteRepository;
    private final ProdutoRepository produtoRepository;

    public LoteService(LoteRepository loteRepository, ProdutoRepository produtoRepository) {
        this.loteRepository = loteRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional(readOnly = true)
    public List<LoteDTO> listarPorProduto(Long idProduto) {
        Long idMatriz = StringUtils.idMatrizUsuarioAutenticado();
        Produto produto = produtoRepository.findById(idProduto)
                .orElseThrow(() -> new EntityNotFoundException("Produto id=" + idProduto + " não encontrado"));
        if (!produto.getMatriz().getIdMatriz().equals(idMatriz)) {
            throw new BusinessException("Acesso negado", HttpStatus.FORBIDDEN);
        }
        return loteRepository.findByProdutoIdProdutoOrderByDataValidadeAsc(idProduto)
                .stream().map(LoteDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public List<AlertaVencimentoDTO> alertasVencimento(int diasPrazo) {
        Long idMatriz = StringUtils.idMatrizUsuarioAutenticado();
        LocalDate limite = LocalDate.now().plusDays(diasPrazo);
        return loteRepository.findAlertasVencimento(idMatriz, limite)
                .stream()
                .map(lote -> {
                    AlertaVencimentoDTO dto = new AlertaVencimentoDTO();
                    dto.setIdLote(lote.getIdLote());
                    dto.setNumeroLote(lote.getNumeroLote());
                    dto.setIdProduto(lote.getProduto() != null ? lote.getProduto().getIdProduto() : null);
                    dto.setNomeProduto(lote.getProduto() != null ? lote.getProduto().getNome() : null);
                    dto.setQuantidadeAtual(lote.getQuantidadeAtual());
                    dto.setDataValidade(lote.getDataValidade());
                    dto.setStatusAlerta(calcularStatusAlerta(lote, diasPrazo));
                    return dto;
                })
                .toList();
    }

    public String calcularStatusAlerta(LoteProduto lote, int diasPrazo) {
        LocalDate hoje = LocalDate.now();
        if (lote.getDataValidade() == null)
            return "ATIVO";
        if (lote.getDataValidade().isBefore(hoje))
            return "VENCIDO";
        if (!lote.getDataValidade().isAfter(hoje.plusDays(diasPrazo)))
            return "PROXIMO_VENCIMENTO";
        return "ATIVO";
    }
}
