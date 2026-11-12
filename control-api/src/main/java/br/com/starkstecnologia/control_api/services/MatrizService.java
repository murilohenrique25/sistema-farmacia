package br.com.starkstecnologia.control_api.services;

import br.com.starkstecnologia.control_api.dto.DadosFilialDTO;
import br.com.starkstecnologia.control_api.dto.DadosMatrizDTO;
import br.com.starkstecnologia.control_api.entity.Matriz;
import br.com.starkstecnologia.control_api.exception.ServiceException;
import br.com.starkstecnologia.control_api.repository.MatrizRepository;
import br.com.starkstecnologia.control_api.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MatrizService {

    @Autowired
    MatrizRepository matrizRepository;

    public void salvarMatriz(DadosMatrizDTO matrizDTO) {
        Matriz matriz = converterDTOToEntity(matrizDTO);
        matrizRepository.save(matriz);
    }

    public void salvarFilial(DadosFilialDTO matrizDTO) {
        Matriz matriz = converterDTOToEntityFilial(matrizDTO);
        matrizRepository.save(matriz);
    }

    public DadosMatrizDTO buscarMatriz(Long idMatriz) {
        return getDadosMatrizDTO(
                matrizRepository.findById(idMatriz).orElseThrow(() -> new ServiceException("Matriz não encontrada")));
    }

    private Matriz converterDTOToEntity(DadosMatrizDTO dadosMatrizDTO) {
        Matriz matriz;
        if (dadosMatrizDTO.getIdMatriz() == null) {
            matriz = new Matriz();
        } else {
            matriz = matrizRepository.getReferenceById(dadosMatrizDTO.getIdMatriz());
        }
        matriz.setCnpj(dadosMatrizDTO.getCnpj());
        matriz.setEmail(dadosMatrizDTO.getEmail());
        matriz.setLogradouro(dadosMatrizDTO.getLogradouro());
        matriz.setNome(dadosMatrizDTO.getNome());
        matriz.setTelefone(dadosMatrizDTO.getTelefone());
        matriz.setLatitude(dadosMatrizDTO.getLatitude());
        matriz.setLongitude(dadosMatrizDTO.getLongitude());
        matriz.setFreteFixo(dadosMatrizDTO.isFreteFixo());
        matriz.setValorFrete(Boolean.TRUE.equals(dadosMatrizDTO.isFreteFixo()) ? dadosMatrizDTO.getValorFrete() : null);
        return matriz;
    }

    private Matriz converterDTOToEntityFilial(DadosFilialDTO dadosMatrizDTO) {
        Matriz matrizFilial;
        if (dadosMatrizDTO.getIdFilial() == null) {
            matrizFilial = new Matriz();
            Matriz matriz = matrizRepository.findById(dadosMatrizDTO.getIdMatriz())
                    .orElseThrow(() -> new ServiceException("Matriz não encontrada"));
            matrizFilial.setIdMatrizPrincipal(matriz.getIdMatriz());
        } else {
            matrizFilial = matrizRepository.findById(dadosMatrizDTO.getIdFilial())
                    .orElseThrow(() -> new ServiceException("Filial não encontrada"));
        }
        matrizFilial.setCnpj(dadosMatrizDTO.getCnpj());
        matrizFilial.setEmail(dadosMatrizDTO.getEmail());
        matrizFilial.setLogradouro(dadosMatrizDTO.getLogradouro());
        matrizFilial.setNome(dadosMatrizDTO.getNome());
        matrizFilial.setTelefone(dadosMatrizDTO.getTelefone());
        matrizFilial.setLatitude(dadosMatrizDTO.getLatitude());
        matrizFilial.setLongitude(dadosMatrizDTO.getLongitude());
        matrizFilial.setFreteFixo(dadosMatrizDTO.isFreteFixo());
        matrizFilial.setValorFrete(
                Boolean.TRUE.equals(dadosMatrizDTO.isFreteFixo()) ? dadosMatrizDTO.getValorFrete() : null);
        return matrizFilial;
    }

    public List<DadosMatrizDTO> buscarTodasMatriz() {
        Long idMatriz = StringUtils.idMatrizUsuarioAutenticado();
        List<Matriz> matrizes = matrizRepository.findByMatrizes();
        return converterEntityMatrizToDTO(matrizes.stream().filter(
                matriz -> idMatriz.equals(matriz.getIdMatriz()) || idMatriz.equals(matriz.getIdMatrizPrincipal()))
                .toList());
    }

    public List<DadosFilialDTO> buscarTodasFiliais(Long idMatriz) {
        List<Matriz> matrizes = matrizRepository.findFiliaisByIdMatriz(idMatriz);
        return converterEntityFilialToDTO(matrizes);
    }

    private List<DadosFilialDTO> converterEntityFilialToDTO(List<Matriz> matrizes) {
        List<DadosFilialDTO> dadosFilialDTOS = new ArrayList<>();

        for (Matriz matriz : matrizes) {
            DadosFilialDTO dadosFilialDTO = getDadosFilialDTO(matriz);
            dadosFilialDTOS.add(dadosFilialDTO);
        }

        return dadosFilialDTOS;
    }

    private static DadosFilialDTO getDadosFilialDTO(Matriz matriz) {
        DadosFilialDTO dadosFilialDTO = new DadosFilialDTO();
        dadosFilialDTO.setIdFilial(matriz.getIdMatriz());
        dadosFilialDTO.setNome(matriz.getNome());
        dadosFilialDTO.setIdMatriz(matriz.getIdMatrizPrincipal());
        dadosFilialDTO.setCnpj(matriz.getCnpj());
        dadosFilialDTO.setEmail(matriz.getEmail());
        dadosFilialDTO.setLogradouro(matriz.getLogradouro());
        dadosFilialDTO.setTelefone(matriz.getTelefone());
        dadosFilialDTO.setLatitude(matriz.getLatitude());
        dadosFilialDTO.setLongitude(matriz.getLongitude());
        dadosFilialDTO.setFreteFixo(matriz.isFreteFixo());
        dadosFilialDTO.setValorFrete(matriz.getValorFrete());
        return dadosFilialDTO;
    }

    private List<DadosMatrizDTO> converterEntityMatrizToDTO(List<Matriz> matrizes) {
        List<DadosMatrizDTO> dadosMatrizDTOS = new ArrayList<>();

        for (Matriz matriz : matrizes) {
            DadosMatrizDTO dadosMatrizDTO = getDadosMatrizDTO(matriz);
            dadosMatrizDTOS.add(dadosMatrizDTO);
        }

        return dadosMatrizDTOS;
    }

    private static DadosMatrizDTO getDadosMatrizDTO(Matriz matriz) {
        DadosMatrizDTO dadosMatrizDTO = new DadosMatrizDTO();
        dadosMatrizDTO.setIdMatriz(matriz.getIdMatriz());
        dadosMatrizDTO.setCnpj(matriz.getCnpj());
        dadosMatrizDTO.setEmail(matriz.getEmail());
        dadosMatrizDTO.setLogradouro(matriz.getLogradouro());
        dadosMatrizDTO.setTelefone(matriz.getTelefone());
        dadosMatrizDTO.setNome(matriz.getNome());
        dadosMatrizDTO.setLatitude(matriz.getLatitude());
        dadosMatrizDTO.setLongitude(matriz.getLongitude());
        dadosMatrizDTO.setFreteFixo(matriz.isFreteFixo());
        dadosMatrizDTO.setValorFrete(matriz.getValorFrete());
        return dadosMatrizDTO;
    }
}
