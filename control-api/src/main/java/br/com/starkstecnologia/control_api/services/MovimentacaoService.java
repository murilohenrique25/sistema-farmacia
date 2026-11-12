package br.com.starkstecnologia.control_api.services;

import br.com.starkstecnologia.control_api.dto.*;
import br.com.starkstecnologia.control_api.entity.LoteProduto;
import br.com.starkstecnologia.control_api.entity.MovimentacaoEstoque;
import br.com.starkstecnologia.control_api.entity.Produto;
import br.com.starkstecnologia.control_api.exception.BusinessException;
import br.com.starkstecnologia.control_api.exception.EntityNotFoundException;
import br.com.starkstecnologia.control_api.jwt.JwtUserDetails;
import br.com.starkstecnologia.control_api.repository.LoteRepository;
import br.com.starkstecnologia.control_api.repository.MovimentacaoRepository;
import br.com.starkstecnologia.control_api.repository.ProdutoRepository;
import br.com.starkstecnologia.control_api.utils.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovimentacaoService {

    private final MovimentacaoRepository movimentacaoRepository;
    private final LoteRepository loteRepository;
    private final ProdutoRepository produtoRepository;

    public MovimentacaoService(MovimentacaoRepository movimentacaoRepository,
            LoteRepository loteRepository,
            ProdutoRepository produtoRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.loteRepository = loteRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public EntradaResponseDTO registrarEntrada(EntradaRequestDTO dto) {
        Long idMatriz = StringUtils.idMatrizUsuarioAutenticado();
        JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long idUsuario = userDetails.getId();

        List<Long> idLotes = new ArrayList<>();

        for (EntradaItemDTO item : dto.getItens()) {
            if (item.getQuantidade() == null || item.getQuantidade().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException("A quantidade de entrada deve ser maior que zero", HttpStatus.BAD_REQUEST);
            }

            Produto produto = produtoRepository.findById(item.getIdProduto())
                    .orElseThrow(
                            () -> new EntityNotFoundException("Produto id=" + item.getIdProduto() + " não encontrado"));

            if (!produto.getMatriz().getIdMatriz().equals(idMatriz)) {
                throw new BusinessException("Acesso negado", HttpStatus.FORBIDDEN);
            }

            LoteProduto lote;
            if (loteRepository.existsByProdutoIdProdutoAndNumeroLote(item.getIdProduto(), item.getNumeroLote())) {
                lote = loteRepository.findByProdutoIdProdutoOrderByDataValidadeAsc(item.getIdProduto())
                        .stream()
                        .filter(l -> l.getNumeroLote().equals(item.getNumeroLote()))
                        .findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("Lote não encontrado"));
                lote.setQuantidadeAtual(lote.getQuantidadeAtual().add(item.getQuantidade()));
                if ("ESGOTADO".equals(lote.getStatus())) {
                    lote.setStatus("ATIVO");
                }
            } else {
                lote = new LoteProduto();
                lote.setProduto(produto);
                lote.setNumeroLote(item.getNumeroLote());
                lote.setQuantidadeAtual(item.getQuantidade());
                lote.setPrecoCusto(item.getPrecoCusto());
                lote.setDataEntrada(item.getDataEntrada());
                lote.setDataValidade(item.getDataValidade());
                lote.setStatus("ATIVO");
            }

            lote = loteRepository.save(lote);
            idLotes.add(lote.getIdLote());

            MovimentacaoEstoque mov = new MovimentacaoEstoque();
            mov.setLote(lote);
            mov.setTipo("ENTRADA");
            mov.setQuantidade(item.getQuantidade());
            mov.setPrecoCusto(item.getPrecoCusto());
            mov.setDataHora(LocalDateTime.now());
            mov.setIdUsuarioResponsavel(idUsuario);
            mov.setOrigem("MANUAL");
            movimentacaoRepository.save(mov);
        }

        EntradaResponseDTO response = new EntradaResponseDTO();
        response.setQuantidadeItensProcessados(idLotes.size());
        response.setIdLotesCriadosOuAtualizados(idLotes);
        return response;
    }

    @Transactional
    public SaidaResponseDTO registrarSaida(SaidaRequestDTO dto) {
        Long idMatriz = StringUtils.idMatrizUsuarioAutenticado();
        JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long idUsuario = userDetails.getId();

        LoteProduto lote = loteRepository.findById(dto.getIdLote())
                .orElseThrow(() -> new EntityNotFoundException("Lote id=" + dto.getIdLote() + " não encontrado"));

        if (!lote.getProduto().getMatriz().getIdMatriz().equals(idMatriz)) {
            throw new BusinessException("Acesso negado", HttpStatus.FORBIDDEN);
        }

        if (dto.getQuantidade() == null || dto.getQuantidade().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("A quantidade de saída deve ser maior que zero", HttpStatus.BAD_REQUEST);
        }

        if (dto.getQuantidade().compareTo(lote.getQuantidadeAtual()) > 0) {
            throw new BusinessException(
                    "Saldo insuficiente. Disponível: " + lote.getQuantidadeAtual() + ", solicitado: "
                            + dto.getQuantidade(),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }

        lote.setQuantidadeAtual(lote.getQuantidadeAtual().subtract(dto.getQuantidade()));
        if (lote.getQuantidadeAtual().compareTo(BigDecimal.ZERO) == 0) {
            lote.setStatus("ESGOTADO");
        }
        loteRepository.save(lote);

        MovimentacaoEstoque mov = new MovimentacaoEstoque();
        mov.setLote(lote);
        mov.setTipo("SAIDA");
        mov.setQuantidade(dto.getQuantidade());
        mov.setPrecoCusto(lote.getPrecoCusto());
        mov.setDataHora(LocalDateTime.now());
        mov.setIdUsuarioResponsavel(idUsuario);
        mov.setMotivo(dto.getMotivo());
        mov.setOrigem("MANUAL");
        movimentacaoRepository.save(mov);

        SaidaResponseDTO response = new SaidaResponseDTO();
        response.setIdLote(lote.getIdLote());
        response.setQuantidadeAtual(lote.getQuantidadeAtual());
        return response;
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoDTO> listarHistorico(LocalDateTime inicio, LocalDateTime fim, Long idProduto) {
        Long idMatriz = StringUtils.idMatrizUsuarioAutenticado();

        List<MovimentacaoEstoque> movimentacoes;
        if (idProduto != null) {
            movimentacoes = movimentacaoRepository
                    .findByLote_Produto_IdProdutoAndDataHoraBetweenOrderByDataHoraDesc(idProduto, inicio, fim);
        } else {
            movimentacoes = movimentacaoRepository
                    .findByLote_Produto_Matriz_IdMatrizAndDataHoraBetweenOrderByDataHoraDesc(idMatriz, inicio, fim);
        }

        return movimentacoes.stream().map(MovimentacaoDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public List<PosicaoEstoqueDTO> posicaoEstoque() {
        Long idMatriz = StringUtils.idMatrizUsuarioAutenticado();

        List<Produto> produtos = produtoRepository.findByMatrizIdMatrizOrderByNomeAsc(idMatriz);
        List<PosicaoEstoqueDTO> resultado = new ArrayList<>();

        for (Produto produto : produtos) {
            BigDecimal quantidadeTotal = loteRepository.sumQuantidadeAtivaByProduto(produto.getIdProduto());
            List<LoteProduto> lotes = loteRepository
                    .findByProdutoIdProdutoOrderByDataValidadeAsc(produto.getIdProduto());

            List<LoteProduto> lotesAtivos = lotes.stream()
                    .filter(l -> l.getQuantidadeAtual().compareTo(BigDecimal.ZERO) > 0)
                    .toList();

            int numeroDeLotesAtivos = lotesAtivos.size();

            java.time.LocalDate dataValidadeMaisProxima = lotesAtivos.stream()
                    .filter(l -> l.getDataValidade() != null)
                    .map(LoteProduto::getDataValidade)
                    .min(java.util.Comparator.naturalOrder())
                    .orElse(null);

            PosicaoEstoqueDTO posicao = new PosicaoEstoqueDTO();
            posicao.setIdProduto(produto.getIdProduto());
            posicao.setNomeProduto(produto.getNome());
            posicao.setCodigo(produto.getCodigo());
            posicao.setUnidadeMedida(produto.getUnidadeMedida());
            posicao.setQuantidadeTotalAtiva(quantidadeTotal != null ? quantidadeTotal : BigDecimal.ZERO);
            posicao.setNumeroDeLotesAtivos(numeroDeLotesAtivos);
            posicao.setDataValidadeMaisProxima(dataValidadeMaisProxima);

            resultado.add(posicao);
        }

        return resultado;
    }

    public byte[] exportarCsv(List<?> dados) {
        StringBuilder sb = new StringBuilder();

        if (!dados.isEmpty() && dados.get(0) instanceof MovimentacaoDTO) {
            sb.append("dataHora,tipo,nomeProduto,numeroLote,quantidade,precoCusto,idUsuarioResponsavel\n");
            for (Object obj : dados) {
                MovimentacaoDTO m = (MovimentacaoDTO) obj;
                sb.append(m.getDataHora()).append(",")
                        .append(m.getTipo()).append(",")
                        .append(m.getNomeProduto()).append(",")
                        .append(m.getNumeroLote()).append(",")
                        .append(m.getQuantidade()).append(",")
                        .append(m.getPrecoCusto()).append(",")
                        .append(m.getIdUsuarioResponsavel()).append("\n");
            }
        } else if (!dados.isEmpty() && dados.get(0) instanceof PosicaoEstoqueDTO) {
            sb.append(
                    "nomeProduto,codigo,unidadeMedida,quantidadeTotalAtiva,numeroDeLotesAtivos,dataValidadeMaisProxima\n");
            for (Object obj : dados) {
                PosicaoEstoqueDTO p = (PosicaoEstoqueDTO) obj;
                sb.append(p.getNomeProduto()).append(",")
                        .append(p.getCodigo()).append(",")
                        .append(p.getUnidadeMedida()).append(",")
                        .append(p.getQuantidadeTotalAtiva()).append(",")
                        .append(p.getNumeroDeLotesAtivos()).append(",")
                        .append(p.getDataValidadeMaisProxima()).append("\n");
            }
        }

        return sb.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }
}
