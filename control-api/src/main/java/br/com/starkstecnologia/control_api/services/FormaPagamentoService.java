package br.com.starkstecnologia.control_api.services;

import br.com.starkstecnologia.control_api.dto.DadosFormaPagamentoDTO;
import br.com.starkstecnologia.control_api.entity.FormaPagamento;
import br.com.starkstecnologia.control_api.repository.FormaPagamentoRepository;
import br.com.starkstecnologia.control_api.repository.MatrizRepository;
import br.com.starkstecnologia.control_api.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FormaPagamentoService {

    @Autowired
    FormaPagamentoRepository formaPagamentoRepository;

    @Autowired
    MatrizRepository matrizRepository;

    public void salvarFormaPagamento(DadosFormaPagamentoDTO dados) {
        FormaPagamento formaPagamento;
        if (dados.getIdFormaPagamento() == null) {
            formaPagamento = new FormaPagamento();
            formaPagamento.setAtivo(true);
            formaPagamento.setDescricao(dados.getDescricao());
            formaPagamento.setMatriz(matrizRepository.getReferenceById(StringUtils.idMatrizUsuarioAutenticado()));
        } else {
            formaPagamento = formaPagamentoRepository.getReferenceById(dados.getIdFormaPagamento());
            formaPagamento.setDescricao(dados.getDescricao());
            formaPagamento.setAtivo(dados.isAtivo());
        }
        formaPagamentoRepository.save(formaPagamento);
    }

    public void editarFormaPagamento(Long idFormaPagamento, DadosFormaPagamentoDTO dados) {
        FormaPagamento formaPagamento = formaPagamentoRepository.getReferenceById(idFormaPagamento);
        formaPagamento.setDescricao(dados.getDescricao());
        formaPagamento.setAtivo(dados.isAtivo());

        formaPagamentoRepository.save(formaPagamento);
    }

    public List<DadosFormaPagamentoDTO> listarTodos() {
        Long idMatrizAutenticado = StringUtils.idMatrizUsuarioAutenticado();
        List<FormaPagamento> formaPagamentos = formaPagamentoRepository.findAll();

        return formaPagamentos.stream()
                .filter(formaPagamento -> formaPagamento.getMatriz().getIdMatriz().equals(idMatrizAutenticado))
                .map(DadosFormaPagamentoDTO::new)
                .collect(Collectors.toList());
    }

    public List<DadosFormaPagamentoDTO> listarTodosAtivos() {
        return listarTodos().stream().filter(DadosFormaPagamentoDTO::isAtivo).collect(Collectors.toList());
    }
}
