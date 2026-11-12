package br.com.starkstecnologia.control_api.services;

import br.com.starkstecnologia.control_api.dto.ContaAlertaDTO;
import br.com.starkstecnologia.control_api.dto.ContaDTO;
import br.com.starkstecnologia.control_api.entity.Conta;
import br.com.starkstecnologia.control_api.exception.EntityNotFoundException;
import br.com.starkstecnologia.control_api.repository.ContaRepository;
import br.com.starkstecnologia.control_api.repository.MatrizRepository;
import br.com.starkstecnologia.control_api.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final MatrizRepository matrizRepository;

    public ContaService(ContaRepository contaRepository, MatrizRepository matrizRepository) {
        this.contaRepository = contaRepository;
        this.matrizRepository = matrizRepository;
    }

    @Transactional
    public ContaDTO salvar(ContaDTO dto) {
        Conta conta = toEntity(dto);
        conta.setStatus("PENDENTE");
        return new ContaDTO(contaRepository.save(conta));
    }

    @Transactional
    public ContaDTO atualizar(Long id, ContaDTO dto) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conta id=" + id + " não encontrada"));
        conta.setDescricao(dto.getDescricao());
        conta.setValor(dto.getValor());
        conta.setDataVencimento(dto.getDataVencimento());
        conta.setDataPagamento(dto.getDataPagamento());
        conta.setStatus(dto.getStatus());
        conta.setCategoria(dto.getCategoria());
        conta.setObservacao(dto.getObservacao());
        conta.setFormaPagamento(dto.getFormaPagamento());
        return new ContaDTO(contaRepository.save(conta));
    }

    @Transactional
    public ContaDTO baixar(Long id) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conta id=" + id + " não encontrada"));
        conta.setStatus("PAGO");
        conta.setDataPagamento(LocalDate.now());
        return new ContaDTO(contaRepository.save(conta));
    }

    @Transactional
    public ContaDTO cancelar(Long id) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conta id=" + id + " não encontrada"));
        conta.setStatus("CANCELADO");
        return new ContaDTO(contaRepository.save(conta));
    }

    @Transactional(readOnly = true)
    public List<ContaDTO> listarPorTipo(String tipo) {
        Long idMatriz = StringUtils.idMatrizUsuarioAutenticado();
        return contaRepository.findByMatrizIdMatrizAndTipo(idMatriz, tipo)
                .stream().map(ContaDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public List<ContaDTO> listarPorTipoPeriodo(String tipo, LocalDate inicio, LocalDate fim) {
        Long idMatriz = StringUtils.idMatrizUsuarioAutenticado();
        return contaRepository.findByMatrizIdMatrizAndTipoAndDataVencimentoBetween(idMatriz, tipo, inicio, fim)
                .stream().map(ContaDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public List<ContaAlertaDTO> buscarAlertas(int diasAntecedencia) {
        Long idMatriz = StringUtils.idMatrizUsuarioAutenticado();
        LocalDate limite = LocalDate.now().plusDays(diasAntecedencia);
        return contaRepository.findAlertasVencimento(idMatriz, limite)
                .stream().map(ContaAlertaDTO::new).toList();
    }

    private Conta toEntity(ContaDTO dto) {
        Conta conta = new Conta();
        conta.setTipo(dto.getTipo());
        conta.setDescricao(dto.getDescricao());
        conta.setValor(dto.getValor());
        conta.setDataVencimento(dto.getDataVencimento());
        conta.setCategoria(dto.getCategoria());
        conta.setObservacao(dto.getObservacao());
        conta.setFormaPagamento(dto.getFormaPagamento());
        conta.setMatriz(matrizRepository.findById(dto.getIdMatriz())
                .orElseThrow(() -> new EntityNotFoundException("Matriz não encontrada")));
        return conta;
    }
}
