package br.com.starkstecnologia.control_api.services;

import br.com.starkstecnologia.control_api.dto.*;
import br.com.starkstecnologia.control_api.entity.*;
import br.com.starkstecnologia.control_api.exception.EntityNotFoundException;
import br.com.starkstecnologia.control_api.exception.ServiceException;
import br.com.starkstecnologia.control_api.repository.*;
import br.com.starkstecnologia.control_api.types.StatusEntregaEnum;
import br.com.starkstecnologia.control_api.utils.StringUtils;
import jakarta.persistence.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class EntregaService {

    private static Long ID_USER_ADMIN = 1L;
    @Autowired
    EntregaRepository entregaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    EntregadorRepository entregadorRepository;

    @Autowired
    EntregaAvulsaRepository entregaAvulsaRepository;

    @Autowired
    MatrizRepository matrizRepository;

    @Autowired
    LocalizacaoEntregaService localizacaoEntregaService;

    @Transactional
    public Long salvarEntrega(DadosEntregaDTO entregaDTO) {
        Entrega entrega;
        if (entregaDTO.getIdEntrega() == null) {
            entrega = new Entrega();
        } else {
            entrega = entregaRepository.findById(entregaDTO.getIdEntrega())
                    .orElseThrow(() -> new EntityNotFoundException("Entrega não encontrada"));
        }
        Usuario caixa = usuarioRepository.findById(entregaDTO.getIdUser())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        dadosEntrega(entregaDTO, entrega, caixa);
        entrega.setStatusEntrega(StatusEntregaEnum.ABERTA.getDescricao());
        Entrega entregaSave = entregaRepository.saveAndFlush(entrega);
        return entregaSave.getIdEntrega();
    }

    @Transactional
    public Long salvarEntregaAutomatizada(DadosEntregaDTO entregaDTO) {
        Matriz matriz = matrizRepository.findMatrizByToken(entregaDTO.getToken());
        if (matriz == null) {
            throw new EntityNotFoundException("Token inválido");
        }
        Entrega entrega;

        if (entregaDTO.getIdEntrega() == null) {
            entrega = new Entrega();
        } else {
            entrega = entregaRepository.findById(entregaDTO.getIdEntrega())
                    .orElseThrow(() -> new EntityNotFoundException("Entrega não encontrada"));
        }
        Optional<Usuario> caixa = usuarioRepository.buscarPorNome(entregaDTO.getNomeAtendente()).stream().findFirst();
        dadosEntregaAutomatizada(entregaDTO, entrega, caixa, matriz);
        entrega.setStatusEntrega(StatusEntregaEnum.ABERTA.getDescricao());
        entrega.setMatriz(matriz);
        Entrega entregaSave = entregaRepository.saveAndFlush(entrega);
        return entregaSave.getIdEntrega();
    }

    private void dadosEntrega(DadosEntregaDTO entregaDTO, Entrega entrega, Usuario caixa) {
        entrega.setDataCadastroEntrega(LocalDateTime.now());
        entrega.setCupomOrcamento(entregaDTO.getCupomOrcamento());
        entrega.setFormaPagamento(entregaDTO.getFormaPagamento());
        entrega.setValorTotal(entregaDTO.getValorTotal());
        entrega.setTroco(entregaDTO.getTroco());
        entrega.setValorReceber(entregaDTO.getValorReceber());
        entrega.setTipoEntrega(entregaDTO.getTipoEntrega());
        entrega.setObservacao(entregaDTO.getObservacao());
        entrega.setNumeroTelefone(entregaDTO.getTelefone());
        Matriz matriz = matrizRepository.getReferenceById(entregaDTO.getIdMatriz());
        entrega.setMatriz(matriz);
        entrega.setValorFrete(resolverValorFrete(matriz, entregaDTO.getValorFrete()));
        entrega.setUser(caixa);
    }

    private void dadosEntregaAutomatizada(DadosEntregaDTO entregaDTO, Entrega entrega, Optional<Usuario> caixa,
            Matriz matriz) {
        entrega.setDataCadastroEntrega(LocalDateTime.now());
        entrega.setCupomOrcamento(entregaDTO.getCupomOrcamento());
        entrega.setFormaPagamento(entregaDTO.getFormaPagamento());
        entrega.setValorTotal(entregaDTO.getValorTotal());
        entrega.setTroco(entregaDTO.getTroco());
        entrega.setValorReceber(entregaDTO.getValorReceber());
        entrega.setTipoEntrega(definirTipoEntrega(entrega.getTipoEntrega()));
        entrega.setNumeroTelefone(entregaDTO.getTelefone());
        entrega.setObservacao(entregaDTO.getObservacao());
        entrega.setValorFrete(resolverValorFrete(matriz, entregaDTO.getValorFrete()));
        entrega.setAutomatizada(true);
        if (caixa.isPresent()) {
            entrega.setUser(caixa.get());
        } else {
            Usuario usuario = new Usuario();
            usuario.setMatriz(matriz);
            usuario.setUsername("user_" + entregaDTO.getNomeAtendente().toLowerCase(Locale.ROOT));
            usuario.setAtivo(true);
            usuario.setNome(entregaDTO.getNomeAtendente());
            usuario.setPassword("user123");
            Usuario userSave = usuarioRepository.save(usuario);
            entrega.setUser(userSave);
        }
    }

    private String definirTipoEntrega(String tipoEntrega) {
        if (tipoEntrega == null) {
            return "Venda Balcão";
        }
        if (tipoEntrega.startsWith("Manipu")) {
            return "Manipulação";
        }
        return "Venda Balcão";
    }

    /**
     * Define o valor do frete da entrega.
     * Se a matriz/filial possui taxa de frete fixa, usa o valor cadastrado na
     * matriz.
     * Caso contrário, usa o valor informado (opcional) no cadastro da entrega.
     */
    private Double resolverValorFrete(Matriz matriz, Double valorFreteEntrega) {
        if (matriz != null && matriz.isFreteFixo()) {
            return matriz.getValorFrete();
        }
        return valorFreteEntrega;
    }

    public void alterarEntrega(Long idEntrega, DadosEntregaAlterarDTO entregaDTO) {
        Entrega entrega = entregaRepository.findById(idEntrega)
                .orElseThrow(() -> new EntityNotFoundException("Entrega não encontrada"));
        dadosEntregaAlterar(entregaDTO, entrega);
        entregaRepository.flush();
    }

    private void dadosEntregaAlterar(DadosEntregaAlterarDTO entregaDTO, Entrega entrega) {
        entrega.setDataCadastroEntrega(LocalDateTime.now());
        entrega.setCupomOrcamento(entregaDTO.getCupomOrcamento());
        entrega.setFormaPagamento(entregaDTO.getFormaPagamento());
        entrega.setValorTotal(entregaDTO.getValorTotal());
        entrega.setTroco(entregaDTO.getTroco());
        entrega.setTipoEntrega(entregaDTO.getTipoEntrega());
        entrega.setValorReceber(entregaDTO.getValorReceber());
        entrega.setValorFrete(resolverValorFrete(entrega.getMatriz(), entregaDTO.getValorFrete()));
        entrega.setObservacao(entregaDTO.getObservacao());
        entrega.setNumeroTelefone(entregaDTO.getTelefone());
    }

    public void selecionarAtribuirAlterarEntrega(List<DadosEntregaDTO> entregasDTO, String userId) {
        Entregador entregador = entregadorRepository.findEntregadorByUsuario(userId);
        List<Entrega> entregas = entregaRepository.buscarEntregasPorId(
                entregasDTO.stream().map(DadosEntregaDTO::getIdEntrega).collect(Collectors.toList()));
        Matriz matriz = entregas.get(0).getMatriz();
        log.info("Dados da matriz: {}, {}", matriz.getIdMatriz(), matriz.isEnviaSms());
        boolean enviaSms = matriz.isEnviaSms();
        String key = matriz.getApiKey();
        Integer queueId = matriz.getQueueId();
        LocalDateTime dataAgora = LocalDateTime.now();
        for (Entrega entrega : entregas) {
            entrega.setDataSelecaoEntrega(dataAgora);
            entrega.setEntregador(entregador);
            entrega.setStatusEntrega(StatusEntregaEnum.SELECIONADA.getDescricao());
            // if (enviaSms && entrega.getNumeroTelefone() != null &&
            // !entrega.getNumeroTelefone().isBlank()) {
            // mensagemService.enviarMensagem(entrega.getNumeroTelefone(), "Olá😃, seu
            // pedido saiu para entrega🏍️.",key, queueId);
            // }
        }
    }

    @Transactional
    public void iniciarEntrega(List<Long> idsEntrega, String userId, String latitude, String longitude) {
        if (idsEntrega == null || idsEntrega.isEmpty())
            return;

        Entregador entregador = entregadorRepository.findEntregadorByUsuario(userId);
        if (entregador == null) {
            throw new ServiceException("Entregador não encontrado para o userId: " + userId);
        }

        List<Entrega> entregas = entregaRepository.buscarEntregasPorId(idsEntrega);

        for (Entrega entrega : entregas) {
            if (!StatusEntregaEnum.SELECIONADA.getDescricao().equals(entrega.getStatusEntrega())) {
                throw new ServiceException(
                        "Entrega " + entrega.getIdEntrega() +
                                " não está com status SELECIONADA. Status atual: " + entrega.getStatusEntrega());
            }
            entrega.setLatitude(latitude);
            entrega.setLongitude(longitude);
            entrega.setStatusEntrega(StatusEntregaEnum.EM_ROTA.getDescricao());
        }
    }

    public void selecionarAtribuirAlterarEntrega(DadosAtribuirFinalizarEntregaDTO entregaDTO, Long idEntrega) {
        Entregador entregador = entregadorRepository.findById(entregaDTO.getId())
                .orElseThrow(() -> new ServiceException("Entregador não encontrado"));
        Entrega entrega = entregaRepository.findById(idEntrega)
                .orElseThrow(() -> new ServiceException("Entrega não encontrada"));
        LocalDateTime dataAgora = LocalDateTime.now();
        entrega.setDataSelecaoEntrega(dataAgora);
        entrega.setEntregador(entregador);
        entrega.setStatusEntrega(StatusEntregaEnum.SELECIONADA.getDescricao());
        entregaRepository.save(entrega);
        Matriz matriz = entrega.getMatriz();
        boolean enviaSms = matriz.isEnviaSms();
        String key = matriz.getApiKey();
        Integer queueId = matriz.getQueueId();
        // if (enviaSms && entrega.getNumeroTelefone() != null &&
        // !entrega.getNumeroTelefone().isBlank()) {
        // mensagemService.enviarMensagem(entrega.getNumeroTelefone(), "Olá😃, seu
        // pedido saiu para entrega🏍️.", key, queueId);
        // }
    }

    public void alterarEntregadorEntrega(Long idEntregador, Long idEntrega) {
        Entregador entregador = entregadorRepository.findById(idEntregador)
                .orElseThrow(() -> new ServiceException("Entregador não encontrado"));
        Entrega entrega = entregaRepository.findById(idEntrega)
                .orElseThrow(() -> new ServiceException("Entrega não encontrada"));
        LocalDateTime dataAgora = LocalDateTime.now();
        entrega.setDataSelecaoEntrega(dataAgora);
        entrega.setEntregador(entregador);
        entrega.setDataAssinaturaEntrega(null);
        entrega.setDataFinalizacaoEntrega(null);
        entrega.setStatusEntrega(StatusEntregaEnum.SELECIONADA.getDescricao());
        entregaRepository.save(entrega);
    }

    public void assinarEntregaCaixa(List<Long> entregaDTOList, Long idUsuario) {
        List<Entrega> entregas = entregaRepository.buscarEntregasPorId(entregaDTOList).stream()
                .filter(entrega -> entrega.getMatriz().getIdMatriz().equals(StringUtils.idMatrizUsuarioAutenticado()))
                .toList();
        LocalDateTime dataAgora = LocalDateTime.now();
        Usuario caixa = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        for (Entrega entrega : entregas) {
            entrega.setDataAssinaturaEntrega(dataAgora);
            entrega.setUserAssinatura(caixa);
            entrega.setStatusEntrega(StatusEntregaEnum.FINALIZADA_ASSINADA.getDescricao());
        }

    }

    public void finalizarEntrega(DadosEntregaFinalizacaoDTO entregaDTO) {
        Entrega entrega = entregaRepository.findById(entregaDTO.getIdEntrega())
                .orElseThrow(() -> new EntityNotFoundException("Entrega não encontrada"));
        LocalDateTime dataAgora = LocalDateTime.now();
        entrega.setDataFinalizacaoEntrega(dataAgora);
        entrega.setLatitude(entregaDTO.getLatitude());
        entrega.setLongitude(entregaDTO.getLongitude());
        entrega.setObservacao(entregaDTO.getObservacao());
        entrega.setObservacaoEntregador(entregaDTO.getObservacaoEntregador());

        Matriz matriz = entrega.getMatriz();
        boolean enviaSms = matriz.isEnviaSms();
        String key = matriz.getApiKey();
        Integer queueId = matriz.getQueueId();
        if (entregaDTO.getObservacaoEntregador() != null
                && entregaDTO.getObservacaoEntregador().startsWith("Não Entregue")) {
            entrega.setStatusEntrega(StatusEntregaEnum.NAO_ENTREGUE.getDescricao());
            // if (enviaSms && entrega.getNumeroTelefone() != null &&
            // !entrega.getNumeroTelefone().isBlank()) {
            // mensagemService.enviarMensagem(entrega.getNumeroTelefone(), "⚠️Nosso
            // entregador não conseguiu localizar o endereço e retornou para a loja, favor
            // entrar em contato para que possamos resolver ⚠️", key, queueId);
            // }
        } else {
            entrega.setStatusEntrega(StatusEntregaEnum.FINALIZADA.getDescricao());
            // if (enviaSms && entrega.getNumeroTelefone() != null &&
            // !entrega.getNumeroTelefone().isBlank()) {
            // mensagemService.enviarMensagem(entrega.getNumeroTelefone(), "🤩Seu pedido foi
            // entregue, obrigado pela preferência🤝!", key, queueId);
            // }
        }
        localizacaoEntregaService.removerLocalizacao(entregaDTO.getIdEntrega());
        encerrarChatSistema(entrega.getNumeroTelefone(), entrega.getUser().getId());
    }

    private void encerrarChatSistema(String numeroTelefone, Long idUser) {
        log.info("Encerramento chat ----");
        if (numeroTelefone != null && ID_USER_ADMIN.equals(idUser)) {
            log.info("Iniciando encerramento do chat");
            try {
                RestTemplate restTemplate = new RestTemplate();

                Map<String, String> jsonMap = new HashMap<>();
                jsonMap.put("mensagem", "Entrega concluída");
                jsonMap.put("atendente", "Atendente");
                jsonMap.put("telefone", numeroTelefone);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<Map<String, String>> request = new HttpEntity<>(jsonMap, headers);

                ResponseEntity<String> response = restTemplate.postForEntity(
                        "https://medalhamilagrosa.atenderbem.com/webhookcapture/capture/16950ffcf2b0492aba105c57d34c63d9",
                        request,
                        String.class);
                log.info("Status: {}", response.getStatusCode());
            } catch (Exception e) {
                log.error("Erro ao enviar webhook", e);
            }
        }
    }

    public List<DadosEntregaDTO> entregasDisponiveis(String userId) {
        Entregador entregador = entregadorRepository.findEntregadorByUsuario(userId);
        List<Entrega> entregas = entregaRepository.buscarEntregasDisponiveis();
        return convertDadosEntregaEntregasDisponiveisDTO(entregas.stream()
                .filter(entrega -> entrega.getMatriz().getIdMatriz().equals(entregador.getMatriz().getIdMatriz()))
                .toList());
    }

    public List<DadosEntregaDTO> entregasDisponiveisPorTipo(String tipoEntrega, String userId) {
        Entregador entregador = entregadorRepository.findEntregadorByUsuario(userId);
        List<Entrega> entregas = entregaRepository.entregasDisponiveisPorTipo(entregador.getMatriz().getIdMatriz(),
                tipoEntrega);
        return convertDadosEntregaEntregasDisponiveisDTO(entregas);
    }

    public List<QuantidadeEntregasPorTipoDTO> quantidadeEntregasDisponiveisPorTipo(String userId) {
        Entregador entregador = entregadorRepository.findEntregadorByUsuario(userId);
        List<Tuple> list = entregaRepository.buscarQuantidadeEntregasAbertas(entregador.getMatriz().getIdMatriz());
        return convertQuantidadeEntregasDisponiveis(list);
    }

    private List<QuantidadeEntregasPorTipoDTO> convertQuantidadeEntregasDisponiveis(List<Tuple> list) {
        List<QuantidadeEntregasPorTipoDTO> dados = new ArrayList<>();

        for (Tuple tuple : list) {
            QuantidadeEntregasPorTipoDTO quantidadeEntregasPorTipoDTO = new QuantidadeEntregasPorTipoDTO();
            quantidadeEntregasPorTipoDTO.setTipo(tuple.get("tipo_entrega").toString());
            quantidadeEntregasPorTipoDTO.setQuantidade(((Long) tuple.get("total")).intValue());
            dados.add(quantidadeEntregasPorTipoDTO);
        }

        return dados;
    }

    public List<DadosEntregaDTO> entregasByIdEntregador(String userId) {
        Entregador entregador = entregadorRepository.findEntregadorByUsuario(userId);
        List<Entrega> entregas = entregaRepository.buscarEntregasByIdEntregador(entregador.getIdEntregador());
        return convertEntregasEntregador(entregas);
    }

    public static List<DadosEntregaDTO> convertDadosEntregaEntregasDisponiveisDTO(List<Entrega> result) {
        List<DadosEntregaDTO> entregaDTOS = new ArrayList<>();

        for (Entrega entrega : result) {
            if (entrega.getStatusEntrega().equals(StatusEntregaEnum.ABERTA.getDescricao())) {
                DadosEntregaDTO dados = new DadosEntregaDTO();

                dados.setIdEntrega(entrega.getIdEntrega());
                dados.setCupomOrcamento(entrega.getCupomOrcamento());
                dados.setFormaPagamento(entrega.getFormaPagamento());
                dados.setIdUser(entrega.getUser().getId());
                dados.setTroco(entrega.getTroco());
                dados.setValorReceber(entrega.getValorReceber());
                dados.setValorFrete(entrega.getValorFrete());
                dados.setValorTotal(entrega.getValorTotal());
                dados.setDataCadastroEntrega(entrega.getDataCadastroEntrega().toString());
                dados.setObservacao(entrega.getObservacao());
                dados.setStatusEntrega(entrega.getStatusEntrega());
                dados.setIdMatriz(entrega.getMatriz().getIdMatriz());
                dados.setTipoEntrega(entrega.getTipoEntrega());
                entregaDTOS.add(dados);
            }
        }
        return entregaDTOS;
    }

    public static List<DadosEntregaDTO> convertEntregasEntregador(List<Entrega> resultList) {
        List<DadosEntregaDTO> entregaDTOS = new ArrayList<>();

        for (Entrega entrega : resultList) {
            DadosEntregaDTO dados = new DadosEntregaDTO();

            dados.setIdEntrega(entrega.getIdEntrega());
            dados.setIdEntregador(entrega.getEntregador().getIdEntregador());
            dados.setCupomOrcamento(entrega.getCupomOrcamento());
            dados.setFormaPagamento(entrega.getFormaPagamento());
            dados.setIdUser(entrega.getUser().getId());
            dados.setTroco(entrega.getTroco());
            dados.setValorReceber(entrega.getValorReceber());
            dados.setValorFrete(entrega.getValorFrete());
            dados.setTipoEntrega(entrega.getTipoEntrega());
            dados.setValorTotal(entrega.getValorTotal());
            dados.setStatusEntrega(entrega.getStatusEntrega());
            dados.setObservacao(entrega.getObservacao());
            dados.setIdMatriz(entrega.getMatriz().getIdMatriz());
            if (entrega.getDataSelecaoEntrega() != null)
                dados.setDataSelecaoEntrega(entrega.getDataSelecaoEntrega().toString());
            if (entrega.getDataCadastroEntrega() != null)
                dados.setDataCadastroEntrega(entrega.getDataCadastroEntrega().toString());
            entregaDTOS.add(dados);
        }
        return entregaDTOS;
    }

    public ResponseEntity<?> buscarEntregas(Long idEntrega, LocalDateTime dataInicial, LocalDateTime dataFinal,
            List<String> statusEntrega, List<Long> idsEntregador) {

        if (idEntrega != null) {
            return ResponseEntity.ok(getDadosEntregaDTO(entregaRepository.buscarEntregaPorId(idEntrega)));
        } else {
            Long idMatriz = StringUtils.idMatrizUsuarioAutenticado();
            List<Tuple> entregas = entregaRepository.findEntregasByParams(dataInicial, dataFinal, statusEntrega,
                    idsEntregador, idMatriz);
            return convertTodasEntregas(entregas);
        }
    }

    public EntregaResponseDTO getEntregasPorIntervalor(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        Long idMatriz = StringUtils.idMatrizUsuarioAutenticado();
        List<Tuple> entregas = entregaRepository.findEntregasByParams(dataInicial, dataFinal,
                List.of("FINALIZADA_ASSINADA"), null, idMatriz);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        List<DadosEntregaMapaDTO> entregaDTOS = new ArrayList<>();

        for (Tuple entrega : entregas) {

            entregaDTOS.add(new DadosEntregaMapaDTO(
                    entrega.get("idEntrega", Long.class),
                    entrega.get("latitude", String.class),
                    entrega.get("longitude", String.class),
                    entrega.get("dataCadastroEntrega", LocalDateTime.class).toString()));
        }

        Map<LocalDate, Long> quantidade = entregaDTOS.stream()
                .collect(Collectors.groupingBy(dto -> LocalDate.parse(dto.getDataEntrega(), formatter),
                        Collectors.counting()));
        entregaDTOS.forEach(dadosEntregaMapaDTO -> dadosEntregaMapaDTO
                .setDataEntrega(LocalDate.parse(dadosEntregaMapaDTO.getDataEntrega(), formatter).toString()));
        return new EntregaResponseDTO(entregaDTOS, quantidade);
    }

    private ResponseEntity<?> convertTodasEntregas(List<Tuple> entregas) {
        List<DadosEntregaDTO> entregaDTOS = new ArrayList<>();

        for (Tuple entrega : entregas) {
            DadosEntregaDTO dados = getDadosEntregaDTO(entrega);
            entregaDTOS.add(dados);
        }
        return ResponseEntity.ok(entregaDTOS);
    }

    private static DadosEntregaDTO getDadosEntregaDTO(Tuple tuple) {
        DadosEntregaDTO dados = new DadosEntregaDTO();

        dados.setIdEntrega(tuple.get("idEntrega", Long.class));
        dados.setIdUser(tuple.get("idUser", Long.class));
        dados.setNomeUser(tuple.get("nomeUser", String.class));
        dados.setCupomOrcamento(tuple.get("cupomOrcamento", String.class));
        dados.setFormaPagamento(tuple.get("formaPagamento", String.class));
        dados.setTipoEntrega(tuple.get("tipoEntrega", String.class));
        dados.setTroco(tuple.get("troco", Double.class));
        dados.setValorReceber(tuple.get("valorReceber", Double.class));
        dados.setValorFrete(tuple.get("valorFrete", Double.class));
        dados.setValorTotal(tuple.get("valorTotal", Double.class));
        if (tuple.get("dataSelecaoEntrega") != null) {
            dados.setDataSelecaoEntrega(tuple.get("dataSelecaoEntrega", LocalDateTime.class).toString());
        }
        if (tuple.get("dataCadastroEntrega") != null) {
            dados.setDataCadastroEntrega(tuple.get("dataCadastroEntrega", LocalDateTime.class).toString());
        }
        dados.setLatitude(tuple.get("latitude", String.class));
        dados.setLongitude(tuple.get("longitude", String.class));
        dados.setObservacao(tuple.get("observacao", String.class));
        dados.setStatusEntrega(tuple.get("statusEntrega", String.class));
        if (tuple.get("dataFinalizacaoEntrega") != null) {
            dados.setDataFinalizacaoEntrega(tuple.get("dataFinalizacaoEntrega", LocalDateTime.class).toString());
        }
        if (tuple.get("dataAssinaturaEntrega") != null) {
            dados.setDataAssinaturaEntrega(tuple.get("dataAssinaturaEntrega", LocalDateTime.class).toString());
        }

        if (tuple.get("idUserAssinatura") != null) {
            dados.setIdUserAssinatura(tuple.get("idUserAssinatura", Long.class));
            dados.setNomeUserAssinatura(tuple.get("nomeUserAssinatura", String.class));
        }
        if (tuple.get("idEntregador") != null) {
            dados.setIdEntregador(tuple.get("idEntregador", Long.class));
            dados.setNomeEntregador(tuple.get("nomeEntregador", String.class));
        }
        if (tuple.get("idMatriz") != null) {
            dados.setIdMatriz(tuple.get("idMatriz", Long.class));
        }

        if (tuple.get("latitude") != null) {
            dados.setLatitude(tuple.get("latitude", String.class));
        }
        if (tuple.get("longitude") != null) {
            dados.setLongitude(tuple.get("longitude", String.class));
        }
        if (tuple.get("numeroTelefone") != null) {
            dados.setTelefone(tuple.get("numeroTelefone", String.class));
        }

        return dados;
    }

    private static DadosEntregaDTO getDadosEntregaDTO(Entrega entrega) {
        DadosEntregaDTO dados = new DadosEntregaDTO();

        dados.setIdEntrega(entrega.getIdEntrega());
        if (entrega.getUserAssinatura() != null) {
            dados.setIdUserAssinatura(entrega.getUserAssinatura().getId());
            dados.setNomeUserAssinatura(entrega.getUserAssinatura().getNome());
        }
        dados.setCupomOrcamento(entrega.getCupomOrcamento());
        dados.setFormaPagamento(entrega.getFormaPagamento());
        dados.setIdUser(entrega.getUser().getId());
        dados.setNomeUser(entrega.getUser().getNome());
        dados.setTipoEntrega(entrega.getTipoEntrega());
        if (entrega.getEntregador() != null && entrega.getEntregador().getIdEntregador() != null) {
            dados.setNomeEntregador(entrega.getEntregador().getNome());
            dados.setIdEntregador(entrega.getEntregador().getIdEntregador());
        }
        if (entrega.getMatriz() != null && entrega.getMatriz().getIdMatriz() != null) {
            dados.setIdMatriz(entrega.getMatriz().getIdMatriz());
        }
        dados.setTroco(entrega.getTroco());
        dados.setValorReceber(entrega.getValorReceber());
        dados.setValorFrete(entrega.getValorFrete());
        dados.setValorTotal(entrega.getValorTotal());
        if (entrega.getDataSelecaoEntrega() != null)
            dados.setDataSelecaoEntrega(entrega.getDataSelecaoEntrega().toString());
        if (entrega.getDataCadastroEntrega() != null)
            dados.setDataCadastroEntrega(entrega.getDataCadastroEntrega().toString());
        dados.setLatitude(entrega.getLatitude());
        dados.setLongitude(entrega.getLongitude());
        dados.setObservacao(entrega.getObservacao());
        dados.setStatusEntrega(entrega.getStatusEntrega());
        if (entrega.getDataFinalizacaoEntrega() != null)
            dados.setDataFinalizacaoEntrega(entrega.getDataFinalizacaoEntrega().toString());
        if (entrega.getDataAssinaturaEntrega() != null)
            dados.setDataAssinaturaEntrega(entrega.getDataAssinaturaEntrega().toString());
        return dados;
    }

    public void finalizarEntregaManual(Long idEntrega) {
        Entrega entrega = entregaRepository.findById(idEntrega)
                .orElseThrow(() -> new EntityNotFoundException("Entrega não encontrada"));
        LocalDateTime dataAgora = LocalDateTime.now();
        entrega.setDataFinalizacaoEntrega(dataAgora);
        Matriz matriz = matrizRepository.getReferenceById(entrega.getMatriz().getIdMatriz());
        entrega.setLatitude(matriz.getLatitude());
        entrega.setLongitude(matriz.getLongitude());
        entrega.setStatusEntrega(StatusEntregaEnum.FINALIZADA_MANUAL.getDescricao());
        localizacaoEntregaService.removerLocalizacao(idEntrega);

    }

    public void cancelarEntrega(Long idEntrega) {
        Entrega entrega = entregaRepository.findById(idEntrega)
                .orElseThrow(() -> new ServiceException("Entrega não encontrada"));
        if (StatusEntregaEnum.FINALIZADA.getDescricao().equals(entrega.getStatusEntrega()) ||
                StatusEntregaEnum.FINALIZADA_ASSINADA.getDescricao().equals(entrega.getStatusEntrega())
                || StatusEntregaEnum.FINALIZADA_MANUAL.getDescricao().equals(entrega.getStatusEntrega())) {
            throw new ServiceException("Entrega já foi assinada, não é permitido exclusão");
        }
        entrega.setStatusEntrega(StatusEntregaEnum.CANCELADA.getDescricao());
        entregaRepository.save(entrega);
        localizacaoEntregaService.removerLocalizacao(idEntrega);
    }

    public void salvarEntregaAvulsa(DadosEntregaAvulsaDTO dadosEntregaAvulsaDTO, String userId) {
        Entregador entregador = entregadorRepository.findEntregadorByUsuario(userId);
        EntregaAvulsa entregaAvulsa = new EntregaAvulsa();
        entregaAvulsa.setDataHoraInicioServico(
                StringUtils.parseStringToLocalDateTime(dadosEntregaAvulsaDTO.getDataHoraInicioServico()));
        entregaAvulsa.setDataHoraTerminoServico(
                StringUtils.parseStringToLocalDateTime(dadosEntregaAvulsaDTO.getDataHoraTerminoServico()));
        entregaAvulsa.setDescricaoServico(dadosEntregaAvulsaDTO.getDescricaoServico());
        entregaAvulsa.setObservacaoServico(dadosEntregaAvulsaDTO.getObservacaoServico());
        entregaAvulsa.setEntregador(entregador);
        entregaAvulsa.setMatriz(entregador.getMatriz());
        entregaAvulsaRepository.save(entregaAvulsa);
    }

    public List<DadosEntregaAvulsaDTO> buscarTodasEntregasAvulsas() {

        List<EntregaAvulsa> entregas = entregaAvulsaRepository
                .findAll()
                .stream()
                .filter(entregaAvulsa -> entregaAvulsa.getMatriz().getIdMatriz()
                        .equals(StringUtils.idMatrizUsuarioAutenticado()))
                .toList();
        return convertTodasEntregasAvulsas(entregas);
    }

    private List<DadosEntregaAvulsaDTO> convertTodasEntregasAvulsas(List<EntregaAvulsa> entregas) {
        List<DadosEntregaAvulsaDTO> dados = new ArrayList<>();
        for (EntregaAvulsa entrega : entregas) {
            DadosEntregaAvulsaDTO dadosEntregaAvulsaDTO = new DadosEntregaAvulsaDTO();
            dadosEntregaAvulsaDTO.setDataHoraInicioServico(entrega.getDataHoraInicioServico().toString());
            dadosEntregaAvulsaDTO.setDescricaoServico(entrega.getDescricaoServico());
            dadosEntregaAvulsaDTO.setDataHoraTerminoServico(entrega.getDataHoraTerminoServico().toString());
            dadosEntregaAvulsaDTO.setObservacaoServico(entrega.getObservacaoServico());
            dadosEntregaAvulsaDTO.setUserId(entrega.getEntregador().getNome());
            dadosEntregaAvulsaDTO.setId(entrega.getIdEntregaAvulsa());
            dados.add(dadosEntregaAvulsaDTO);
        }
        return dados;
    }

    public List<DadosEntregaAssinarDTO> buscarEntregasAssinar(List<Long> idsEntregador, LocalDateTime dataInicio,
            LocalDateTime dataTermino) {
        Long idMatriz = StringUtils.idMatrizUsuarioAutenticado();
        List<Tuple> entregas = entregaRepository.findEntregasByParamsAssinar(idsEntregador, dataInicio, dataTermino,
                idMatriz);
        List<DadosEntregaAssinarDTO> dadosEntregaAssinarDTOS = new ArrayList<>();
        for (Tuple tuple : entregas) {
            DadosEntregaAssinarDTO dadosEntregaAssinarDTO = new DadosEntregaAssinarDTO();
            dadosEntregaAssinarDTO.setIdEntrega((Long) tuple.get("idEntrega"));
            dadosEntregaAssinarDTO.setCupomOrcamento((String) tuple.get("cupomOrcamento"));
            dadosEntregaAssinarDTO.setDataFinalizacaoEntrega(
                    StringUtils.converterFormatoBrasileiro(tuple.get("dataFinalizacaoEntrega", LocalDateTime.class)));
            dadosEntregaAssinarDTO.setStatusEntrega((String) tuple.get("statusEntrega"));
            dadosEntregaAssinarDTO.setNomeUser((String) tuple.get("nomeCaixa"));
            dadosEntregaAssinarDTO.setNomeEntregador((String) tuple.get("nomeEntregador"));
            dadosEntregaAssinarDTOS.add(dadosEntregaAssinarDTO);
        }
        return dadosEntregaAssinarDTOS;
    }

    public List<DadosEntregaMesDTO> buscarTodasEntregasMes() {

        LocalDateTime dataInicial = StringUtils.retornaDataInicioAtual().withDayOfMonth(1);
        LocalDateTime dataFinal = StringUtils.retornaDataFinalAtual().with(TemporalAdjusters.lastDayOfMonth());
        List<Tuple> entregas = entregaRepository.buscarEntregaMes(dataInicial, dataFinal);
        return convertTodasEntregasMes(entregas);
    }

    private List<DadosEntregaMesDTO> convertTodasEntregasMes(List<Tuple> entregas) {
        List<DadosEntregaMesDTO> entregaMesDTOS = new ArrayList<>();
        for (Tuple tuple : entregas) {
            DadosEntregaMesDTO dto = new DadosEntregaMesDTO();
            dto.setNomeEntregador((String) tuple.get("entregadorMaisEntregas"));
            if (tuple.get("quantidadeEntregasPorEntregadorMaisEntregas") != null) {
                dto.setTotalEntregasEntregador(
                        ((Long) tuple.get("quantidadeEntregasPorEntregadorMaisEntregas")).intValue());
            }
            if (tuple.get("totalEntregasFinalizadasOuAssinadas") != null) {
                dto.setTotalEntregaMesFinalizadas(((Long) tuple.get("totalEntregasFinalizadasOuAssinadas")).intValue());
            }
            if (tuple.get("totalEntregas") != null) {
                dto.setTotalEntregasMes(((Long) tuple.get("totalEntregas")).intValue());
            }
            entregaMesDTOS.add(dto);
        }
        return entregaMesDTOS;
    }

}
