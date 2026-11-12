//package br.com.starkstecnologia.control_api.services;
//
//import br.com.starkstecnologia.control_api.dto.EnviarMensagemRequest;
//import br.com.starkstecnologia.control_api.dto.MensagemResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//
//
//@Service
//@Slf4j
//public class MensagemService {
//
//
//
//    @Value("${mensagem-api.api-key}")
//    private String apiKey;
//
//    @Value("${mensagem-api.queue-id}")
//    private Integer queueId;
//
//    public void enviarMensagem(String numero, String texto, String apiKey, Integer queueId) {
//        try {
//            log.info("Enviando mensagem para: {}", numero);
//
//            EnviarMensagemRequest request = new EnviarMensagemRequest(
//                    queueId,
//                    apiKey,
//                    numero,
//                    texto
//            );
//
//            MensagemResponse response = webClient.post()
//                    .uri("/int/enqueueMessageToSend")
//                    .bodyValue(request)
//                    .retrieve()
//                    .onStatus(HttpStatusCode::isError, clientResponse -> {
//                        log.error("Erro na API de mensagens: {}", clientResponse.statusCode());
//                        return clientResponse.bodyToMono(String.class)
//                                .map(errorBody -> new RuntimeException(
//                                        "Erro ao enviar mensagem: " + clientResponse.statusCode() + " - " + errorBody));
//                    })
//                    .bodyToMono(MensagemResponse.class)
//                    .timeout(Duration.ofSeconds(30))
//                    .doOnSuccess(resp -> log.info("Mensagem enviada com sucesso para: {}", numero))
//                    .doOnError(error -> log.error("Erro ao enviar mensagem para: {}", numero, error))
//                    .block();
//
//        } catch (Exception e) {
//            log.error("Falha ao enviar mensagem para: {}", numero, e);
//        }
//    }
//
//    public MensagemResponse enviarMensagem(String numero, String texto) {
//        try {
//            log.info("Enviando mensagem para: {}", numero);
//
//            EnviarMensagemRequest request = new EnviarMensagemRequest(
//                    queueId,
//                    apiKey,
//                    numero,
//                    texto
//            );
//
//            MensagemResponse response = webClient.post()
//                    .uri("/int/enqueueMessageToSend")
//                    .bodyValue(request)
//                    .retrieve()
//                    .onStatus(HttpStatusCode::isError, clientResponse -> {
//                        log.error("Erro na API de mensagens: {}", clientResponse.statusCode());
//                        return clientResponse.bodyToMono(String.class)
//                                .map(errorBody -> new RuntimeException(
//                                        "Erro ao enviar mensagem: " + clientResponse.statusCode() + " - " + errorBody));
//                    })
//                    .bodyToMono(MensagemResponse.class)
//                    .timeout(Duration.ofSeconds(30))
//                    .doOnSuccess(resp -> log.info("Mensagem enviada com sucesso para: {}", numero))
//                    .doOnError(error -> log.error("Erro ao enviar mensagem para: {}", numero, error))
//                    .block();
//
//            return response;
//
//        } catch (Exception e) {
//            log.error("Falha ao enviar mensagem para: {}", numero, e);
//            return null;
//        }
//    }
//
//    // Método auxiliar para enviar com parâmetros customizados
//    public MensagemResponse enviarMensagemCustomizada(Integer queueIdCustom, String numeroTelefone, String mensagem) {
//        try {
//            EnviarMensagemRequest request = new EnviarMensagemRequest(
//                    queueIdCustom != null ? queueIdCustom : queueId,
//                    apiKey,
//                    numeroTelefone,
//                    mensagem
//            );
//
//            return webClient.post()
//                    .uri("/int/enqueueMessageToSend")
//                    .bodyValue(request)
//                    .retrieve()
//                    .onStatus(HttpStatusCode::isError, clientResponse -> {
//                        return clientResponse.bodyToMono(String.class)
//                                .map(errorBody -> new RuntimeException("Erro: " + errorBody));
//                    })
//                    .bodyToMono(MensagemResponse.class)
//                    .timeout(Duration.ofSeconds(30))
//                    .block();
//
//        } catch (Exception e) {
//            log.error("Erro ao enviar mensagem customizada", e);
//            return null;
//        }
//    }
//}
