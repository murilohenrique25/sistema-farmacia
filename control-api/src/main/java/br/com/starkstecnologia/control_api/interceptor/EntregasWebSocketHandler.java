package br.com.starkstecnologia.control_api.interceptor;

import br.com.starkstecnologia.control_api.dto.LocalizacaoEntregaDTO;
import br.com.starkstecnologia.control_api.dto.WsAckDTO;
import br.com.starkstecnologia.control_api.services.LocalizacaoEntregaService;
import br.com.starkstecnologia.control_api.types.AtualizacaoResultado;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EntregasWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<String, WebSocketSession> sessionsByToken = new ConcurrentHashMap<>();

    @Autowired
    private LocalizacaoEntregaService localizacaoEntregaService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String token = (String) session.getAttributes().get("token");
        System.out.println("🔌 WS conectado. Token=" + token);

        if (token != null) {
            sessionsByToken.put(token, session);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception {

        String payload = message.getPayload();
        System.out.println("📨 Mensagem recebida: " + payload);

        LocalizacaoEntregaDTO dto;
        try {
            dto = objectMapper.readValue(payload, LocalizacaoEntregaDTO.class);
        } catch (IOException e) {
            System.out.println("❌ Erro ao parsear JSON do WS: " + e.getMessage());
            sendAck(session, new WsAckDTO("ERROR", null, "JSON inválido", null));
            return;
        }

        String userId = (String) session.getAttributes().get("userId");

        if (userId == null) {
            System.out.println("⚠️ Sessão WS sem userId, ignorando mensagem");
            return;
        }

        Optional<String> erroValidacao = validarPayload(dto);
        if (erroValidacao.isPresent()) {
            sendAck(session, new WsAckDTO("ERROR", null, erroValidacao.get(), null));
            return;
        }

        AtualizacaoResultado resultado = localizacaoEntregaService.atualizarLocalizacao(userId, dto);

        if (resultado == AtualizacaoResultado.IGNORED) {
            System.out.printf("⚠️ [%s] Entrega %d ignorada (não está EM_ROTA)%n", userId, dto.getIdEntrega());
            sendAck(session, new WsAckDTO("IGNORED", null, "Entrega não está em rota", null));
        } else {
            System.out.printf("📍 [%s] Entrega %d => (%.6f, %.6f)%n",
                    userId, dto.getIdEntrega(), dto.getLatitude(), dto.getLongitude());
            sendAck(session, new WsAckDTO("OK", dto.getIdEntrega(), null, LocalDateTime.now().toString()));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String token = (String) session.getAttributes().get("token");
        System.out.println("🔌 WS desconectado. Token=" + token);

        if (token != null) {
            sessionsByToken.remove(token);
        }
    }

    private Optional<String> validarPayload(LocalizacaoEntregaDTO dto) {
        if (dto.getIdEntrega() == null) return Optional.of("idEntrega é obrigatório");
        if (dto.getLatitude() == null || dto.getLatitude() < -90.0 || dto.getLatitude() > 90.0)
            return Optional.of("latitude inválida: " + dto.getLatitude());
        if (dto.getLongitude() == null || dto.getLongitude() < -180.0 || dto.getLongitude() > 180.0)
            return Optional.of("longitude inválida: " + dto.getLongitude());
        return Optional.empty();
    }

    private void sendAck(WebSocketSession session, WsAckDTO ack) {
        try {
            String json = objectMapper.writeValueAsString(ack);
            session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            System.out.println("❌ Erro ao enviar ACK: " + e.getMessage());
        }
    }
}
