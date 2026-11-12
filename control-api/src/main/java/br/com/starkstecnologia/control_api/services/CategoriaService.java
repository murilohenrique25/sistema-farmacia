package br.com.starkstecnologia.control_api.services;

import br.com.starkstecnologia.control_api.dto.CategoriaDTO;
import br.com.starkstecnologia.control_api.entity.Categoria;
import br.com.starkstecnologia.control_api.exception.EntityNotFoundException;
import br.com.starkstecnologia.control_api.repository.CategoriaRepository;
import br.com.starkstecnologia.control_api.repository.MatrizRepository;
import br.com.starkstecnologia.control_api.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final MatrizRepository matrizRepository;

    public CategoriaService(CategoriaRepository categoriaRepository, MatrizRepository matrizRepository) {
        this.categoriaRepository = categoriaRepository;
        this.matrizRepository = matrizRepository;
    }

    @Transactional
    public CategoriaDTO salvar(CategoriaDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setDescricao(dto.getDescricao());
        categoria.setTipo(dto.getTipo());
        categoria.setAtivo(true);
        categoria.setMatriz(matrizRepository.getReferenceById(StringUtils.idMatrizUsuarioAutenticado()));
        return new CategoriaDTO(categoriaRepository.save(categoria));
    }

    @Transactional
    public CategoriaDTO atualizar(Long id, CategoriaDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria id=" + id + " não encontrada"));
        categoria.setDescricao(dto.getDescricao());
        categoria.setTipo(dto.getTipo());
        categoria.setAtivo(dto.isAtivo());
        return new CategoriaDTO(categoriaRepository.save(categoria));
    }

    @Transactional(readOnly = true)
    public List<CategoriaDTO> listarTodas() {
        Long idMatriz = StringUtils.idMatrizUsuarioAutenticado();
        return categoriaRepository.findByMatrizIdMatriz(idMatriz)
                .stream().map(CategoriaDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public List<CategoriaDTO> listarAtivasPorTipo(String tipo) {
        Long idMatriz = StringUtils.idMatrizUsuarioAutenticado();
        return categoriaRepository.findByMatrizIdMatrizAndTipoAndAtivo(idMatriz, tipo, true)
                .stream().map(CategoriaDTO::new).toList();
    }

    @Transactional
    public void excluir(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria id=" + id + " não encontrada"));
        categoria.setAtivo(false);
        categoriaRepository.save(categoria);
    }
}
