package br.com.starkstecnologia.control_api.services;

import br.com.starkstecnologia.control_api.dto.EntradaItemDTO;
import br.com.starkstecnologia.control_api.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class NFeParserService {

    private static final String NAMESPACE_NFE = "http://www.portalfiscal.inf.br/nfe";

    public List<EntradaItemDTO> parsear(MultipartFile arquivo) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(arquivo.getInputStream());

            Element root = doc.getDocumentElement();
            String ns = root.getNamespaceURI();
            if (ns == null || !ns.equals(NAMESPACE_NFE)) {
                throw new BusinessException("Arquivo XML não está no formato NF-e padrão SEFAZ",
                        HttpStatus.BAD_REQUEST);
            }

            NodeList detList = doc.getElementsByTagNameNS(NAMESPACE_NFE, "det");
            List<EntradaItemDTO> itens = new ArrayList<>();

            for (int i = 0; i < detList.getLength(); i++) {
                Element det = (Element) detList.item(i);
                EntradaItemDTO item = new EntradaItemDTO();

                item.setNumeroLote(getTagValue(det, "nLote"));
                item.setQuantidade(parseBigDecimal(getTagValue(det, "qCom")));
                item.setPrecoCusto(parseBigDecimal(getTagValue(det, "vUnCom")));
                item.setDataEntrada(parseLocalDate(getTagValue(det, "dFab")));
                item.setDataValidade(parseLocalDate(getTagValue(det, "dVal")));
                // cProd é o código do produto — idProduto será resolvido pelo service
                itens.add(item);
            }

            return itens;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Erro ao processar o arquivo XML: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private String getTagValue(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagNameNS(NAMESPACE_NFE, tagName);
        if (list.getLength() == 0) {
            list = parent.getElementsByTagName(tagName);
        }
        if (list.getLength() > 0 && list.item(0).getTextContent() != null) {
            return list.item(0).getTextContent().trim();
        }
        return null;
    }

    private BigDecimal parseBigDecimal(String value) {
        if (value == null || value.isBlank())
            return null;
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private LocalDate parseLocalDate(String value) {
        if (value == null || value.isBlank())
            return null;
        try {
            return LocalDate.parse(value);
        } catch (Exception e) {
            return null;
        }
    }
}
