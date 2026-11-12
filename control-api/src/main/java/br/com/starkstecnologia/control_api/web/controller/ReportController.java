package br.com.starkstecnologia.control_api.web.controller;

import br.com.starkstecnologia.control_api.services.PdfService;
import com.itextpdf.text.DocumentException;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/relatorio")
public class ReportController {

    @Autowired
    PdfService reportService;

    @GetMapping(value = "/geral/{dataInicio}/{dataFim}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generatePdfGeral(@PathVariable("dataInicio") LocalDateTime dataInicio, @PathVariable("dataFim") LocalDateTime dataFim, @RequestParam(value = "statusEntrega", required = false) List<String> statusEntrega, @RequestParam(value = "idsEntregador", required = false) List<Long> idsEntregador) {
        try {
            byte[] pdfBytes = reportService.generatePdf(dataInicio, dataFim, idsEntregador, statusEntrega);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "relatorio.pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (IOException | TemplateException | DocumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/totalizador/{dataInicio}/{dataFim}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generatePdfTotalizador(@PathVariable("dataInicio") LocalDateTime dataInicio, @PathVariable("dataFim") LocalDateTime dataFim, @RequestParam(value = "statusEntrega", required = false) List<String> statusEntrega, @RequestParam(value = "idsEntregador", required = false) List<Long> idsEntregador) {
        try {
            byte[] pdfBytes = reportService.generatePdfTotalizador(dataInicio, dataFim, idsEntregador, statusEntrega);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "relatorio.pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (IOException | TemplateException | DocumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value = "/avulsas/{dataInicio}/{dataFim}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generatePdfEntregasAvulsas(@PathVariable("dataInicio") LocalDateTime dataInicio, @PathVariable("dataFim") LocalDateTime dataFim,  @RequestParam(value = "idsEntregador", required = false) List<Long> idsEntregador) {
        try {
            byte[] pdfBytes = reportService.generatePdfEntregasAvulsas(dataInicio, dataFim, idsEntregador);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "relatorio-avulsa.pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (IOException | TemplateException | DocumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}