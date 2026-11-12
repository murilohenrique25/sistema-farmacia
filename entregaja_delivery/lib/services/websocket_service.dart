import 'dart:convert';
import 'package:flutter/widgets.dart';
import 'package:web_socket_channel/web_socket_channel.dart';
import 'package:get/get.dart';

class WebSocketService extends GetxService {
  WebSocketChannel? _channel;
  final RxBool isConnected = false.obs;
  int? _idEntrega;

  /// Conecta ao WebSocket do servidor.
  /// A conexão é considerada ativa assim que o canal é estabelecido —
  /// não enviamos ping pois o servidor espera apenas JSON de localização.
  void connect(String token, int idEntrega) {
    // Desconecta sessão anterior se houver
    disconnect();

    _idEntrega = idEntrega;

    try {
      final uri = Uri(
        scheme: 'wss',
        host: 'tomcat.vps6912.panel.icontainer.net',
        port: 8443,
        path: '/control-api/ws/entregas',
        queryParameters: {'token': token},
      );

      debugPrint('🔌 Conectando ao WebSocket: $uri');

      _channel = WebSocketChannel.connect(uri);

      // Marca como conectado imediatamente após o canal ser criado.
      // O canal WebSocket faz o handshake HTTP→WS internamente;
      // assim que o objeto é criado, a conexão está em andamento.
      isConnected.value = true;
      debugPrint('✅ WebSocket conectado');

      _channel!.stream.listen(
        (message) {
          debugPrint('📨 Mensagem recebida: $message');
        },
        onError: (error) {
          debugPrint('❌ Erro no WebSocket: $error');
          isConnected.value = false;
          _channel = null;
        },
        onDone: () {
          debugPrint('🔌 WebSocket desconectado');
          isConnected.value = false;
          _channel = null;
        },
        cancelOnError: true,
      );
    } catch (e) {
      debugPrint('❌ Erro ao conectar WebSocket: $e');
      isConnected.value = false;
      _channel = null;
    }
  }

  /// Envia a localização atual ao servidor via WebSocket.
  void sendLocation(double lat, double lng) {
    if (_channel == null || !isConnected.value) {
      debugPrint('⚠️ WebSocket não conectado, localização descartada');
      return;
    }

    try {
      _channel!.sink.add(
        jsonEncode({
          'idEntrega': _idEntrega,
          'latitude': lat,
          'longitude': lng,
          'timestamp': DateTime.now().toIso8601String(),
        }),
      );
      debugPrint('📤 Localização enviada: $lat, $lng');
    } catch (e) {
      debugPrint('❌ Erro ao enviar localização: $e');
      isConnected.value = false;
    }
  }

  void disconnect() {
    try {
      _channel?.sink.close();
    } catch (_) {}
    _channel = null;
    isConnected.value = false;
  }

  @override
  void onClose() {
    disconnect();
    super.onClose();
  }
}
