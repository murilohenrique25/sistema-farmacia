import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import 'package:get/get.dart';
import '../services/location_service.dart';
import '../services/websocket_service.dart';
import '../services/api_service.dart';
import '../models/storage_service.dart';
import '../models/entrega_model.dart';
import '../controllers/entrega_ativa_controller.dart';
import '../controllers/entregas_selecionadas_controller.dart';
import '../controllers/home_controller.dart';
import '../routes/app_pages.dart';

class EntregasDetalhesController extends GetxController {
  final LocationService locationService = Get.find<LocationService>();
  final WebSocketService wsService = Get.find<WebSocketService>();
  final StorageService storage = Get.find<StorageService>();
  final EntregaAtivaController entregaAtivaCtrl =
      Get.find<EntregaAtivaController>();
  final ApiService apiService = ApiService();

  final RxBool isRastreando = false.obs;
  final RxBool isFinalizando = false.obs;
  final RxBool isIniciando = false.obs;

  // Última posição para exibir na UI (atualizada via callback do LocationService)
  final Rxn<Position> currentPosition = Rxn<Position>();

  Rx<EntregaModel?> entregaAtual = Rx<EntregaModel?>(null);

  @override
  void onInit() {
    super.onInit();

    if (Get.arguments != null && Get.arguments is EntregaModel) {
      entregaAtual.value = Get.arguments as EntregaModel;

      // Se a entrega já estava em rota, reconecta o rastreamento
      if (entregaAtual.value!.isEmRota) {
        isRastreando.value = true;
        _reconectarRastreamento(entregaAtual.value!);
      }
    }

    // Espelha a posição do LocationService para a UI
    ever(locationService.currentPosition, (pos) {
      if (pos != null) currentPosition.value = pos;
    });
  }

  Future<void> _reconectarRastreamento(EntregaModel entrega) async {
    try {
      final usuario = storage.getUsuarioLogado();
      if (usuario == null) return;

      entregaAtivaCtrl.entregaAtiva.value = entrega;
      wsService.connect(usuario.token, entrega.idEntrega);

      locationService.onLocationUpdate = (lat, lng) {
        if (wsService.isConnected.value) wsService.sendLocation(lat, lng);
      };

      await locationService.startTracking();
    } catch (_) {}
  }

  /// Inicia a entrega: permissão → GPS atual → API → foreground service + WebSocket
  Future<void> iniciarEntrega() async {
    final entrega = entregaAtual.value;
    if (entrega == null) {
      Get.snackbar('Erro', 'Entrega não encontrada');
      return;
    }

    if (isIniciando.value || isRastreando.value) return;

    final selecionadasCtrl = Get.find<EntregasSelecionadasController>();
    if (selecionadasCtrl.entregasEmRota.isNotEmpty) {
      final emRota = selecionadasCtrl.entregasEmRota.first;
      Get.snackbar(
        'Entrega em andamento',
        'Finalize a entrega #${emRota.idEntrega} antes de iniciar outra.',
        snackPosition: SnackPosition.BOTTOM,
        backgroundColor: Get.theme.colorScheme.error,
        colorText: Get.theme.colorScheme.onError,
        duration: const Duration(seconds: 4),
      );
      return;
    }

    if (entregaAtivaCtrl.temEntregaAtiva &&
        !entregaAtivaCtrl.isEntregaAtiva(entrega.idEntrega)) {
      Get.snackbar(
        'Atenção',
        'Você já tem uma entrega em andamento. Finalize-a primeiro.',
        snackPosition: SnackPosition.BOTTOM,
        backgroundColor: Get.theme.colorScheme.error,
        colorText: Get.theme.colorScheme.onError,
        duration: const Duration(seconds: 4),
      );
      return;
    }

    isIniciando.value = true;

    try {
      // Permissão com diálogo de consentimento
      final ready = await locationService.checkReady();
      if (!ready) return;

      // Posição atual para a API
      final position = await locationService.getCurrentPosition();
      if (position == null) {
        Get.snackbar(
          'Erro',
          'Não foi possível obter a localização. Verifique o GPS.',
          snackPosition: SnackPosition.BOTTOM,
        );
        return;
      }

      // Registra início na API
      final apiResult = await apiService.iniciarEntregas(
        [entrega.idEntrega],
        position.latitude.toString(),
        position.longitude.toString(),
      );

      if (!apiResult['success']) {
        Get.snackbar(
          'Erro',
          apiResult['message'] ?? 'Falha ao iniciar entrega na API',
          snackPosition: SnackPosition.BOTTOM,
          backgroundColor: Get.theme.colorScheme.error,
          colorText: Get.theme.colorScheme.onError,
        );
        return;
      }

      final usuario = storage.getUsuarioLogado();
      if (usuario == null) {
        Get.snackbar(
          'Erro',
          'Usuário não autenticado',
          snackPosition: SnackPosition.BOTTOM,
        );
        return;
      }

      entregaAtivaCtrl.entregaAtiva.value = entrega;

      // Conecta WebSocket
      wsService.connect(usuario.token, entrega.idEntrega);

      // Liga callback localização → WebSocket
      locationService.onLocationUpdate = (lat, lng) {
        if (wsService.isConnected.value) wsService.sendLocation(lat, lng);
      };

      // Inicia foreground service via geolocator (notificação persistente)
      await locationService.startTracking();

      isRastreando.value = true;

      Get.snackbar(
        'Entrega Iniciada',
        'Rastreamento em tempo real ativado',
        snackPosition: SnackPosition.BOTTOM,
        backgroundColor: Get.theme.colorScheme.primary,
        colorText: Get.theme.colorScheme.onPrimary,
        icon: const Icon(Icons.location_on, color: Colors.white),
        duration: const Duration(seconds: 3),
      );
    } catch (e) {
      await _pararRastreamento();
      Get.snackbar(
        'Erro',
        'Não foi possível iniciar a entrega. Tente novamente.',
        snackPosition: SnackPosition.BOTTOM,
        backgroundColor: Get.theme.colorScheme.error,
        colorText: Get.theme.colorScheme.onError,
      );
    } finally {
      isIniciando.value = false;
    }
  }

  Future<void> _pararRastreamento() async {
    await locationService.stopTracking();
    wsService.disconnect();
    entregaAtivaCtrl.entregaAtiva.value = null;
    isRastreando.value = false;
  }

  Future<void> _voltarEVerificarLista() async {
    final selecionadasCtrl = Get.find<EntregasSelecionadasController>();
    await selecionadasCtrl.buscarEntregasSelecionadas();

    final semEntregas =
        selecionadasCtrl.entregasSelecionadas.isEmpty &&
        selecionadasCtrl.entregasEmRota.isEmpty;

    if (semEntregas) {
      // Sem mais entregas: volta para home e recarrega dados disponíveis
      final homeCtrl = Get.find<HomeController>();
      await homeCtrl.fetchData();
      Get.offNamedUntil(
        AppPages.home,
        (route) => route.settings.name == AppPages.home,
      );
    } else {
      // Ainda tem entregas pendentes: volta para a lista de selecionadas
      Get.offNamed(AppPages.entregasSelecionadas);
    }
  }

  Future<void> finalizarEntrega() async {
    final entrega = entregaAtual.value;
    if (entrega == null) {
      Get.snackbar('Erro', 'Entrega não encontrada');
      return;
    }

    // Guarda duplo para evitar abrir o dialog duas vezes com cliques rápidos
    if (isFinalizando.value) return;
    isFinalizando.value = true;

    final opcao = await Get.dialog<String>(
      AlertDialog(
        title: const Text('O que deseja fazer?'),
        content: const Text('Escolha uma ação para esta entrega.'),
        actions: [
          TextButton(onPressed: () => Get.back(), child: const Text('Voltar')),
          OutlinedButton(
            onPressed: () => Get.back(result: 'cancelar'),
            style: OutlinedButton.styleFrom(foregroundColor: Colors.orange),
            child: const Text('Cancelar Entrega'),
          ),
          ElevatedButton(
            onPressed: () => Get.back(result: 'finalizar'),
            style: ElevatedButton.styleFrom(
              backgroundColor: Colors.red,
              foregroundColor: Colors.white,
            ),
            child: const Text('Finalizar'),
          ),
        ],
      ),
    );

    if (opcao == null) {
      isFinalizando.value = false;
      return;
    }

    if (opcao == 'cancelar') {
      // Pede o motivo antes de registrar como "Não Entregue" na API
      final motivo = await Get.dialog<String>(
        AlertDialog(
          title: const Text('Motivo do cancelamento'),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              _motivoItem('Endereço não encontrado'),
              _motivoItem('Cliente ausente'),
              _motivoItem('Cliente recusou a entrega'),
              _motivoItem('Outro motivo'),
            ],
          ),
          actions: [
            TextButton(
              onPressed: () => Get.back(),
              child: const Text('Voltar'),
            ),
          ],
        ),
      );

      if (motivo == null) {
        isFinalizando.value = false;
        return;
      }

      isFinalizando.value = true;
      try {
        entrega.observacaoEntregador = 'Não Entregue - $motivo';
        final response = await apiService.finalizarEntrega(entrega);

        if (response['success']) {
          await _pararRastreamento();
          entrega.observacaoEntregador = null;
          isFinalizando.value = false;
          Get.snackbar(
            'Entrega cancelada',
            'Registrado como "Não Entregue: $motivo".',
            snackPosition: SnackPosition.BOTTOM,
            backgroundColor: Colors.orange.shade700,
            colorText: Colors.white,
            duration: const Duration(seconds: 2),
          );
          await _voltarEVerificarLista();
          return;
        } else {
          Get.snackbar(
            'Erro',
            response['message'] ?? 'Falha ao cancelar entrega',
            snackPosition: SnackPosition.BOTTOM,
          );
        }
      } catch (e) {
        Get.snackbar(
          'Erro',
          'Não foi possível cancelar. Tente novamente.',
          snackPosition: SnackPosition.BOTTOM,
        );
      }
      entrega.observacaoEntregador = null;
      isFinalizando.value = false;
      return;
    }

    isFinalizando.value = true;

    try {
      final response = await apiService.finalizarEntrega(entrega);

      if (response['success']) {
        await _pararRastreamento();
        // Libera o flag ANTES de navegar para evitar atualizar
        // observable em controller já descartado pelo Get.offNamed
        isFinalizando.value = false;
        Get.snackbar(
          'Sucesso',
          'Entrega finalizada com sucesso!',
          snackPosition: SnackPosition.BOTTOM,
          backgroundColor: Get.theme.colorScheme.primary,
          colorText: Get.theme.colorScheme.onPrimary,
          duration: const Duration(seconds: 2),
        );
        await _voltarEVerificarLista();
        return; // não cai no finally após navegar
      } else {
        Get.snackbar(
          'Erro',
          response['message'] ?? 'Falha ao finalizar entrega',
          snackPosition: SnackPosition.BOTTOM,
        );
      }
    } catch (e) {
      Get.snackbar(
        'Erro',
        'Erro ao finalizar entrega. Tente novamente.',
        snackPosition: SnackPosition.BOTTOM,
      );
    }
    isFinalizando.value = false;
  }

  bool get isEntregaAtiva {
    return entregaAtivaCtrl.isEntregaAtiva(entregaAtual.value?.idEntrega ?? 0);
  }

  /// Gera um item de lista clicável para seleção de motivo no diálogo.
  Widget _motivoItem(String motivo) {
    return ListTile(
      dense: true,
      leading: const Icon(Icons.radio_button_unchecked, size: 20),
      title: Text(motivo, style: const TextStyle(fontSize: 14)),
      onTap: () => Get.back(result: motivo),
    );
  }

  @override
  Future<void> onClose() async {
    // Não para rastreamento ao fechar a tela de detalhes —
    // o foreground service continua enquanto a entrega estiver ativa
    super.onClose();
  }
}
