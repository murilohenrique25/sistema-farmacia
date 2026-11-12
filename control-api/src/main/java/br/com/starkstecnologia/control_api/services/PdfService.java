package br.com.starkstecnologia.control_api.services;

import br.com.starkstecnologia.control_api.dto.DadosRelatorioEntregaAvulsaDTO;
import br.com.starkstecnologia.control_api.dto.DadosRelatorioTotalizadorDTO;
import br.com.starkstecnologia.control_api.dto.DadosTodasEntregasDTO;
import br.com.starkstecnologia.control_api.entity.Matriz;
import br.com.starkstecnologia.control_api.repository.EntregaRepository;
import br.com.starkstecnologia.control_api.repository.MatrizRepository;
import br.com.starkstecnologia.control_api.types.StatusEntregaEnum;
import br.com.starkstecnologia.control_api.utils.StringUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.text.pdf.PdfWriter;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PdfService {

    @Autowired
    Configuration freemarkerConfig;

    @Autowired
    EntregaRepository entregaRepository;

    @Autowired
    MatrizRepository matrizRepository;

    public byte[] generatePdf(LocalDateTime dataInicio, LocalDateTime dataFim, List<Long> idsEntregador, List<String> statusEntrega) throws IOException, TemplateException, DocumentException {

        List<String> statusEntregas = new ArrayList<>();
        for(String status : statusEntrega){
            StatusEntregaEnum entregaEnum = StatusEntregaEnum.valueOf(status);
            statusEntregas.add(entregaEnum.getDescricao());
        }
        Long idMatrizAutenticado = StringUtils.idMatrizUsuarioAutenticado();
        Map<String, Object> data = getDadosBasicoEmpresa(idMatrizAutenticado);
        List<DadosTodasEntregasDTO> listaDados = obterDados(dataInicio, dataFim, idsEntregador, statusEntregas, idMatrizAutenticado);
        data.put("nomeRelatorio", "Relatório de Entregas");
        data.put("listaDados", listaDados);
        data.put("totalEntregas", listaDados.size());
        if(!statusEntrega.isEmpty()){

            data.put("statusEntrega", String.join(", ", statusEntregas));
        }else{
            data.put("statusEntrega","");
        }


        // Carregando o template
        return retornaBytePaginas(data, "template-todas-entregas.ftl");
    }

    public byte[] generatePdfTotalizador(LocalDateTime dataInicio, LocalDateTime dataFim, List<Long> idsEntregador, List<String> statusEntrega) throws IOException, TemplateException, DocumentException {
        Long idMatrizAutenticado = StringUtils.idMatrizUsuarioAutenticado();
        Map<String, Object> data = getDadosBasicoEmpresa(idMatrizAutenticado);
        List<DadosRelatorioTotalizadorDTO> listaDados = buscarDadosEntregasPorEntregador(dataInicio, dataFim, idsEntregador, statusEntrega, idMatrizAutenticado);
        data.put("listaDados", listaDados);
        data.put("nomeRelatorio", "Totalizador");
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        data.put("intervaloDataInicio", formato.format(dataInicio));
        data.put("intervaloDataFim",formato.format(dataFim));

        if(!statusEntrega.isEmpty()){
            List<String> statusEntregas = new ArrayList<>();
            for(String status : statusEntrega){
                StatusEntregaEnum entregaEnum = StatusEntregaEnum.valueOf(status);
                statusEntregas.add(entregaEnum.getDescricao());
            }
            data.put("statusEntrega", statusEntregas);
        }else{
            data.put("statusEntrega","");
        }

        // Carregando o template
        return retornaBytePaginas(data, "template-totalizador-entregas.ftl");
    }

    public byte[] generatePdfEntregasAvulsas(LocalDateTime dataInicio, LocalDateTime dataFim, List<Long> idsEntregador) throws IOException, TemplateException, DocumentException {
        Long idMatrizAutenticado = StringUtils.idMatrizUsuarioAutenticado();
        Map<String, Object> data = getDadosBasicoEmpresa(idMatrizAutenticado);
        List<DadosRelatorioEntregaAvulsaDTO> listaDados = buscarDadosEntregasAvulsas(dataInicio, dataFim, idsEntregador, idMatrizAutenticado);
        data.put("listaDados", listaDados);
        data.put("nomeRelatorio", "Entregas Avulsas");
        data.put("totalEntregas", listaDados.size());
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        data.put("intervaloDataInicio", formato.format(dataInicio));
        data.put("intervaloDataFim",formato.format(dataFim));

        // Carregando o template
        return retornaBytePaginas(data, "template-entregas-avulsas.ftl");
    }

    private byte[] retornaBytePaginas(Map<String, Object> data, String templateFtl) throws IOException, TemplateException, DocumentException {
        Template template = freemarkerConfig.getTemplate(templateFtl);

        // Gerando o conteúdo HTML a partir do template
        StringWriter stringWriter = new StringWriter();
        template.process(data, stringWriter);
        String htmlContent = stringWriter.toString();

        // Criando o PDF a partir do HTML
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate());

        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();

        // Usar XMLWorkerHelper para processar o HTML
        XMLWorkerHelper.getInstance().parseXHtml(writer, document, new StringReader(htmlContent));

        // Rotaciona a página
        document.newPage(); // Cria uma nova página
        document.setPageSize(document.getPageSize().rotate()); // Rotaciona a página

        document.close();

        return outputStream.toByteArray();
    }

    private  Map<String, Object> getDadosBasicoEmpresa(Long idMatriz) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        String dataFormatada = formato.format(new Date());
        Matriz matriz = matrizRepository.getReferenceById(idMatriz);
        Map<String, Object> data = new HashMap<>();
        String logoPath = Objects.requireNonNull(PdfService.class.getClassLoader().getResource("static/images/logo-medalha.jpg")).getPath();
        data.put("logo", logoPath);
        data.put("nomeEmpresa", matriz.getNome());
        data.put("endereco", matriz.getLogradouro());
        data.put("telefone", matriz.getTelefone());
        data.put("dataAtual", dataFormatada);
        return data;
    }

    private List<DadosTodasEntregasDTO> obterDados(LocalDateTime dataInicio, LocalDateTime dataFim, List<Long> idsEntregador, List<String> statusEntrega, Long idMatrizAutenticado) {

        List<Tuple> results = entregaRepository.buscarDadosTodasEntregas(dataInicio, dataFim, idsEntregador, statusEntrega, idMatrizAutenticado);
        List<DadosTodasEntregasDTO> dtoList = new ArrayList<>();

        for (Tuple row : results) {
            DadosTodasEntregasDTO dto = new DadosTodasEntregasDTO();
            dto.setCupomOrcamento(row.get("cupomOrcamento", String.class));
            dto.setNomeCaixa(StringUtils.extrairDoisPrimeirosNomes(row.get("nomeCaixa", String.class)));
            dto.setNomeCaixaAssinatura(StringUtils.extrairDoisPrimeirosNomes(row.get("nomeCaixaAssinatura", String.class)));
            dto.setDataCadastro(StringUtils.converterFormatoBrasileiro(row.get("dataCadastroEntrega", LocalDateTime.class)));
            dto.setDataAssinatura(StringUtils.converterFormatoBrasileiro(row.get("dataAssinaturaEntrega", LocalDateTime.class)));
            dto.setMediaCaixa(StringUtils.convertSecondsToHHMMSS(row.get("mediaCaixa", Double.class))); // Aqui é o tempo como string
            dto.setNomeEntregador(StringUtils.extrairDoisPrimeirosNomes(row.get("nomeEntregador", String.class)));
            dto.setDataSelecao(StringUtils.converterFormatoBrasileiro(row.get("dataSelecaoEntrega", LocalDateTime.class)));
            dto.setDataFinalizacao(StringUtils.converterFormatoBrasileiro(row.get("dataFinalizacaoEntrega", LocalDateTime.class)));
            dto.setMediaEntregador(StringUtils.convertSecondsToHHMMSS(row.get("mediaEntregador", Double.class))); // Aqui é o tempo como string
            dtoList.add(dto);
        }

        return dtoList;
    }

    private List<DadosRelatorioTotalizadorDTO> buscarDadosEntregasPorEntregador(LocalDateTime dataInicio, LocalDateTime dataFim, List<Long> idsEntregador, List<String> statusEntrega, Long idMatrizAutenticado){
        List<String> statusEntregas = new ArrayList<>();
        for(String status : statusEntrega){
            StatusEntregaEnum entregaEnum = StatusEntregaEnum.valueOf(status);
            statusEntregas.add(entregaEnum.getDescricao());
        }

        List<Tuple> retorno = entregaRepository.buscarDadosEntregasPorEntregador(dataInicio, dataFim, idsEntregador, statusEntregas, idMatrizAutenticado);
        List<DadosRelatorioTotalizadorDTO> relatorioTotalizadorDTOS = new ArrayList<>();
        for (Tuple row : retorno) {
            DadosRelatorioTotalizadorDTO dados = new DadosRelatorioTotalizadorDTO();
            dados.setNome(row.get("nome", String.class));
            dados.setQuantidadeEntrega(row.get("quantidadeEntregas", Long.class));
            dados.setTempoTotal(StringUtils.convertSecondsToHHMMSS(row.get("totalTempoGasto", Double.class)));
            dados.setMediaTotal(StringUtils.convertSecondsToHHMMSS(row.get("mediaTempo", Double.class)));
            dados.setValorTotalEntrega(StringUtils.formatarParaMoedaBrasileira(row.get("valorTotalEntregas", Double.class)));
            dados.setValorTotalTroco(StringUtils.formatarParaMoedaBrasileira(row.get("totalTroco", Double.class)));
            relatorioTotalizadorDTOS.add(dados);
        }
        return relatorioTotalizadorDTOS;
    }

    private List<DadosRelatorioEntregaAvulsaDTO> buscarDadosEntregasAvulsas(LocalDateTime dataInicio, LocalDateTime dataFim, List<Long> idsEntregador, Long idMatrizAutenticado){
        List<Tuple> retorno = entregaRepository.buscarDadosEntregasAvulsas(dataInicio, dataFim, idsEntregador, idMatrizAutenticado);
        List<DadosRelatorioEntregaAvulsaDTO> relatorioAvulsasDTOs = new ArrayList<>();
        for (Tuple row : retorno) {
            DadosRelatorioEntregaAvulsaDTO dados = new DadosRelatorioEntregaAvulsaDTO();
            dados.setNome(row.get("nomeEntregador", String.class));
            dados.setDataHoraInicioServico(StringUtils.converterFormatoBrasileiro(row.get("dataHoraInicioServico", LocalDateTime.class)));
            dados.setDataHoraTerminoServico(StringUtils.converterFormatoBrasileiro(row.get("dataHoraTerminoServico", LocalDateTime.class)));
            dados.setObservacaoServico(row.get("observacaoServico", String.class));
            dados.setDescricaoServico(row.get("descricaoServico", String.class));
            relatorioAvulsasDTOs.add(dados);
        }
        return relatorioAvulsasDTOs;
    }

}
