import 'package:flutter/scheduler.dart';
import 'package:flutter/widgets.dart';
import 'package:get/get.dart';
import '../services/api_service.dart';
import '../models/storage_service.dart';
import '../routes/app_pages.dart';
import '../models/entrega_model.dart';

class HomeController extends GetxController with WidgetsBindingObserver {
  final ApiService _apiService = ApiService();
  final isLoading = false.obs;

  /// true enquanto a verificação inicial de entregas ativas está em andamento.
  /// Bloqueia os botões de ação até sabermos se há entregas pendentes.
  final isVerificando = true.obs;

  final data = <Map<String, int>>[].obs;
  final errorMessage = ''.obs;

  @override
  void onInit() {
    super.onInit();
    WidgetsBinding.instance.addObserver(this);
    fetchData();
    _verificarEntregasAtivas();
  }

  @override
  void onClose() {
    WidgetsBinding.instance.removeObserver(this);
    super.onClose();
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    if (state == AppLifecycleState.resumed) {
      fetchData();
    }
  }

  /// Verifica entregas ativas e redireciona sem empilhar sobre a home.
  /// Usa offNamed para que o botão "voltar" não retorne à home enquanto
  /// há entregas em andamento.
  Future<void> _verificarEntregasAtivas() async {
    try {
      final response = await _apiService.getEntregasSelecionadas();
      if (!response['success']) return;

      final List<dynamic> rawData = response['data'];
      final todas = rawData.map((e) => EntregaModel.fromJson(e)).toList();

      final emRota = todas.where((e) => e.isEmRota).toList();
      final selecionadas = todas.where((e) => e.isSelecionada).toList();

      if (emRota.isNotEmpty || selecionadas.isNotEmpty) {
        SchedulerBinding.instance.addPostFrameCallback((_) {
          if (emRota.isNotEmpty) {
            // Entrega em rota: vai direto para detalhes substituindo a home
            Get.offNamed(AppPages.entregasDetalhes, arguments: emRota.first);
          } else {
            // Só selecionadas: vai para a lista substituindo a home
            Get.offNamed(AppPages.entregasSelecionadas);
          }
        });
      }
    } catch (_) {
      // Silencioso — não bloquear a home em caso de erro de rede
    } finally {
      isVerificando.value = false;
    }
  }

  Future<void> fetchData() async {
    isLoading.value = true;
    errorMessage.value = '';
    data.clear();

    try {
      final List<Map<String, int>> response = await _apiService.getData();
      debugPrint('[HomeController] dados: $response');
      data.assignAll(response);
    } catch (e) {
      errorMessage.value = 'Erro ao buscar dados: ${e.toString()}';
    } finally {
      isLoading.value = false;
    }
  }

  Future<void> refreshDadosEntregador() async {
    final storage = Get.find<StorageService>();
    final usuario = storage.getUsuarioLogado();
    if (usuario == null) return;

    try {
      final dados = await _apiService.getDadosEntregador(usuario.usuario);
      if (dados != null) {
        final Map<String, dynamic> atualizado = {
          'usuario': usuario.usuario,
          'token': usuario.token,
          'dadosEntregadorApp': dados,
        };
        storage.saveUsuarioLogado(atualizado);
      }
    } catch (_) {}
  }

  /// Navega para entregas disponíveis do tipo "Venda Balcão".
  /// Bloqueia se houver entregas selecionadas/em rota pendentes.
  Future<void> executeAction1() async {
    if (isVerificando.value) return;
    if (await _temEntregasPendentes()) return;
    await _navegarParaEntregas('Venda Balcão');
  }

  /// Navega para entregas disponíveis do tipo "Manipulação".
  /// Bloqueia se houver entregas selecionadas/em rota pendentes.
  Future<void> executeAction2() async {
    if (isVerificando.value) return;
    if (await _temEntregasPendentes()) return;
    await _navegarParaEntregas('Manipulação');
  }

  /// Retorna true e exibe aviso se há entregas selecionadas ou em rota.
  Future<bool> _temEntregasPendentes() async {
    try {
      final response = await _apiService.getEntregasSelecionadas();
      if (!response['success']) return false;

      final List<dynamic> rawData = response['data'];
      final todas = rawData.map((e) => EntregaModel.fromJson(e)).toList();
      final pendentes = todas.where((e) => e.isEmRota || e.isSelecionada);

      if (pendentes.isNotEmpty) {
        Get.snackbar(
          'Entregas pendentes',
          'Finalize suas entregas selecionadas antes de pegar novas.',
          snackPosition: SnackPosition.BOTTOM,
          duration: const Duration(seconds: 3),
        );
        // Redireciona para a lista de pendentes
        Get.offNamed(AppPages.entregasSelecionadas);
        return true;
      }
    } catch (_) {}
    return false;
  }

  Future<void> _navegarParaEntregas(String tipo) async {
    isLoading.value = true;
    try {
      final response = await _apiService.getEntregas(tipo);
      if (response['success']) {
        final List<EntregaModel> entregas =
            (response['data'] as List)
                .map((item) => EntregaModel.fromJson(item))
                .toList();
        Get.toNamed(AppPages.entregas, arguments: entregas);
      } else {
        Get.snackbar(
          'Erro',
          'Falha ao buscar entregas',
          snackPosition: SnackPosition.BOTTOM,
        );
      }
    } catch (e) {
      Get.snackbar(
        'Erro',
        'Erro ao buscar entregas: ${e.toString()}',
        snackPosition: SnackPosition.BOTTOM,
      );
    } finally {
      isLoading.value = false;
    }
  }

  int getQuantidadePorTipo(String tipo) {
    return data.firstWhere(
      (element) => element.containsKey(tipo),
      orElse: () => {tipo: 0},
    )[tipo]!;
  }
}
