package br.com.starkstecnologia.control_api;

import br.com.starkstecnologia.control_api.exception.BusinessException;
import br.com.starkstecnologia.control_api.services.NFeParserService;
import net.jqwik.api.*;
import org.springframework.mock.web.MockMultipartFile;
import static org.assertj.core.api.Assertions.*;

// Feature: estoque-modulo, Property 14: Parser rejeita XML sem namespace SEFAZ
// Feature: estoque-modulo, Property 15: Round-trip do parser NF-e
public class NFeParserServicePropertyTest {

    private final NFeParserService parser = new NFeParserService();

    // Propriedade 14: XML sem namespace SEFAZ deve ser rejeitado
    // Validates: Requirements 4.1, 4.2
    @Property(tries = 50)
    void xmlSemNamespaceSefazEhRejeitado(@ForAll("xmlsSemNamespace") String xmlContent) {
        MockMultipartFile arquivo = new MockMultipartFile(
                "arquivo", "test.xml", "application/xml", xmlContent.getBytes());
        assertThatThrownBy(() -> parser.parsear(arquivo))
                .isInstanceOf(BusinessException.class);
    }

    @Provide
    Arbitrary<String> xmlsSemNamespace() {
        return Arbitraries.of(
                "<?xml version=\"1.0\"?><root><item>test</item></root>",
                "<?xml version=\"1.0\"?><nfeProc xmlns=\"http://www.outro.namespace.com\"><NFe/></nfeProc>",
                "<?xml version=\"1.0\"?><pedido><produto>X</produto></pedido>",
                "<?xml version=\"1.0\"?><nota xmlns=\"http://www.fazenda.gov.br/nfe\"><item/></nota>");
    }

    // Propriedade 15: XML NF-e válido deve ser parseado de forma determinística
    // Validates: Requirements 4.3, 4.6
    @Property(tries = 10)
    void xmlNfeValidoEhParsadoDeterministicamente(@ForAll("quantidades") String quantidade) {
        String xml = buildNfeXml(quantidade);
        MockMultipartFile arquivo1 = new MockMultipartFile("arquivo", "nfe.xml", "application/xml", xml.getBytes());
        MockMultipartFile arquivo2 = new MockMultipartFile("arquivo", "nfe.xml", "application/xml", xml.getBytes());

        var resultado1 = parser.parsear(arquivo1);
        var resultado2 = parser.parsear(arquivo2);

        assertThat(resultado1).hasSize(resultado2.size());
        for (int i = 0; i < resultado1.size(); i++) {
            assertThat(resultado1.get(i).getQuantidade()).isEqualByComparingTo(resultado2.get(i).getQuantidade());
        }
    }

    @Provide
    Arbitrary<String> quantidades() {
        return Arbitraries.of("1.0000", "5.5000", "10.0000", "100.0000", "0.5000");
    }

    private String buildNfeXml(String quantidade) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<nfeProc xmlns=\"http://www.portalfiscal.inf.br/nfe\">" +
                "<NFe><infNFe>" +
                "<det nItem=\"1\">" +
                "<prod>" +
                "<cProd>001</cProd>" +
                "<xProd>Produto Teste</xProd>" +
                "<qCom>" + quantidade + "</qCom>" +
                "<vUnCom>10.00</vUnCom>" +
                "<nLote>LOTE001</nLote>" +
                "<dFab>2024-01-01</dFab>" +
                "<dVal>2025-12-31</dVal>" +
                "</prod>" +
                "</det>" +
                "</infNFe></NFe>" +
                "</nfeProc>";
    }
}
