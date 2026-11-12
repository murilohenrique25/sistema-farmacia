import 'package:flutter/material.dart';
import 'package:get/get.dart';

import '../models/entrega_model.dart';
import '../models/storage_service.dart';
import '../services/location_service.dart';
import '../services/websocket_service.dart';

class EntregaAtivaController extends GetxController {
  final Rxn<EntregaModel> entregaAtiva = Rxn<EntregaModel>();

  bool get temEntregaAtiva => entregaAtiva.value != null;

  /// Inicia uma entrega: pede permissão → sobe foreground service → conecta WebSocket.
  Future<void> iniciarEntrega(EntregaModel entrega) async {
    if (temEntregaAtiva) {
      Get.snackbar(
        'Atenção',
        'Você já tem uma entrega em andamento. Finalize-a antes de iniciar outra.',
        snackPosition: SnackPosition.BOTTOM,
        backgroundColor: Get.theme.colorScheme.error,
        colorText: Get.theme.colorScheme.onError,
        duration: const Duration(seconds: 4),
      );
      return;
    }

    final locationService = Get.find<LocationService>();
    final wsService = Get.find<WebSocketService>();

    // Permissão + GPS check (com diálogo de consentimento)
    final ready = await locationService.checkReady();
    if (!ready) return;

    final storage = Get.find<StorageService>();
    final usuario = storage.getUsuarioLogado();
    final token = usuario?.token ?? '';

    if (token.isEmpty) {
      Get.snackbar('Erro', 'Sessão expirada. Faça login novamente.');
      return;
    }

    entregaAtiva.value = entrega;

    // Conecta WebSocket
    wsService.connect(token, entrega.idEntrega);

    // Liga o callback de localização → WebSocket
    locationService.onLocationUpdate = (lat, lng) {
      if (wsService.isConnected.value) {
        wsService.sendLocation(lat, lng);
      }
    };

    // Inicia o foreground service via geolocator (notificação persistente)
    await locationService.startTracking();

    Get.snackbar(
      'Rastreamento ativo',
      'Sua localização está sendo compartilhada com o cliente.',
      snackPosition: SnackPosition.BOTTOM,
      backgroundColor: Colors.green.shade600,
      colorText: Colors.white,
      icon: const Icon(Icons.location_on, color: Colors.white),
      duration: const Duration(seconds: 3),
    );
  }

  /// Finaliza a entrega ativa e encerra o rastreamento.
  Future<void> finalizarEntrega() async {
    if (entregaAtiva.value == null) return;

    final locationService = Get.find<LocationService>();
    final wsService = Get.find<WebSocketService>();

    await locationService.stopTracking();
    wsService.disconnect();
    entregaAtiva.value = null;

    Get.snackbar(
      'Entrega finalizada',
      'Rastreamento encerrado.',
      snackPosition: SnackPosition.BOTTOM,
      duration: const Duration(seconds: 2),
    );
  }

  bool isEntregaAtiva(int idEntrega) {
    return entregaAtiva.value?.idEntrega == idEntrega;
  }
}
