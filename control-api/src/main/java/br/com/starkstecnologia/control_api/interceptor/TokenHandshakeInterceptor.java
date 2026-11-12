package br.com.starkstecnologia.control_api.interceptor;

import br.com.starkstecnologia.control_api.repository.EntregadorSessaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
public class TokenHandshakeInterceptor implements HandshakeInterceptor {

    @Autowired
    EntregadorSessaoRepository entregadorSessaoRepository;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {

        HttpServletRequest servletRequest =
                ((ServletServerHttpRequest) request).getServletRequest();

        String token = servletRequest.getParameter("token");

        if (token == null || token.isEmpty()) {
            System.out.println("❌ WebSocket sem token, bloqueando conexão");
            return false;
        }

        return entregadorSessaoRepository.findByToken(token)
                .map(sessao -> {
                    attributes.put("token", token);
                    attributes.put("userId", sessao.getUserId());
                    return true;
                })
                .orElse(false);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
        // nada por enquanto
    }
}
