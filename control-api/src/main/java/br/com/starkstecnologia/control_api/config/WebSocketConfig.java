package br.com.starkstecnologia.control_api.config;

import br.com.starkstecnologia.control_api.interceptor.EntregasWebSocketHandler;
import br.com.starkstecnologia.control_api.interceptor.TokenHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final EntregasWebSocketHandler entregasWebSocketHandler;
    private final TokenHandshakeInterceptor tokenHandshakeInterceptor;

    public WebSocketConfig(EntregasWebSocketHandler handler,
            TokenHandshakeInterceptor interceptor) {
        this.entregasWebSocketHandler = handler;
        this.tokenHandshakeInterceptor = interceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(entregasWebSocketHandler, "/ws/entregas")
                .addInterceptors(tokenHandshakeInterceptor)
                .setAllowedOrigins("*");
    }
}