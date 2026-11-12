import 'package:entregaja_delivery/models/entrega_model.dart';
import 'package:entregaja_delivery/models/storage_service.dart';
import 'package:get/get.dart';
import '../services/api_service.dart';
import '../routes/app_pages.dart';

class AuthController extends GetxController {
  final ApiService _apiService = ApiService();
  final isLoading = false.obs;
  final errorMessage = ''.obs;
  final StorageService storage = Get.find<StorageService>();

  @override
  void onInit() {
    super.onInit();
    // NÃO NAVEGAR AQUI
  }

  void verificaUsuarioLogadoSistema() {
    final dados = Get.find<StorageService>().getUsuarioLogado();
    if (dados?.usuario != null) {
      Get.offAllNamed(AppPages.home);
    }
  }

  Future<void> login(String email, String password) async {
    isLoading.value = true;
    errorMessage.value = '';

    try {
      final response = await _apiService.login(email, password);

      if (response['success']) {
        Get.find<StorageService>().saveUsuarioLogado(response['dados']);

        // 🔧 CORREÇÃO: Após login, verificar se tem entregas selecionadas
        validaQuantidadeEntregasSelecionadas();
      } else {
        errorMessage.value = response['message'] ?? 'Falha na autenticação';
      }
    } catch (e) {
      errorMessage.value = 'Erro na conexão: ${e.toString()}';
    } finally {
      isLoading.value = false;
    }
  }

  // Navegar para a tela correta conforme estado das entregas
  void validaQuantidadeEntregasSelecionadas() async {
    try {
      final response = await _apiService.getEntregasSelecionadas();

      if (response['success']) {
        final List<dynamic> data = response['data'];
        final todas = data.map((item) => EntregaModel.fromJson(item)).toList();

        final emRota = todas.where((e) => e.isEmRota).toList();
        final selecionadas = todas.where((e) => e.isSelecionada).toList();

        if (emRota.isNotEmpty) {
          // Entrega em rota — abrir direto nos detalhes
          Get.offAllNamed(AppPages.entregasDetalhes, arguments: emRota.first);
        } else if (selecionadas.isNotEmpty) {
          // Entregas selecionadas — abrir lista
          Get.offAllNamed(AppPages.entregasSelecionadas);
        } else {
          Get.offAllNamed(AppPages.home);
        }
      } else {
        Get.offAllNamed(AppPages.home);
      }
    } catch (_) {
      Get.offAllNamed(AppPages.home);
    }
  }

  void logout() {
    Get.find<StorageService>().clear();
    Get.offAllNamed(AppPages.initial);
  }
}
