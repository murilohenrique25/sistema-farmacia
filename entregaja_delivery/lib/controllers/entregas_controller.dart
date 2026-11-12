import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../services/api_service.dart';
import '../models/entrega_model.dart';
import '../routes/app_pages.dart';

class EntregasController extends GetxController {
  final ApiService apiService = ApiService();
  final RxList<EntregaModel> entregas = <EntregaModel>[].obs;
  final RxList<EntregaModel> entregasSelecionadas = <EntregaModel>[].obs;
  final RxBool isLoading = false.obs;
  final RxString errorMessage = ''.obs;

  @override
  void onInit() {
    super.onInit();

    if (Get.arguments != null && Get.arguments is List<EntregaModel>) {
      entregas.value = Get.arguments as List<EntregaModel>;
    } else {
      errorMessage.value = 'Nenhuma entrega disponível';
    }
  }

  void toggleEntregaSelecionada(int index) {
    final entrega = entregas[index];
    entrega.selecionada = !(entrega.selecionada);

    if (entrega.selecionada == true) {
      entregasSelecionadas.add(entrega);
    } else {
      entregasSelecionadas.removeWhere((e) => e.idEntrega == entrega.idEntrega);
    }

    entregas.refresh();
    entregasSelecionadas.refresh();
  }

  void verEntregasSelecionadas() {
    if (entregasSelecionadas.isEmpty) {
      Get.snackbar(
        'Atenção',
        'Selecione pelo menos uma entrega para continuar',
        backgroundColor: Colors.amber,
        colorText: Colors.black,
        snackPosition: SnackPosition.BOTTOM,
      );
      return;
    }

    realizarConfirmacaoSelecao();
  }

  Future<void> realizarConfirmacaoSelecao() async {
    isLoading.value = true;
    errorMessage.value = '';

    try {
      final response = await apiService.selecionarEntregas(
        entregasSelecionadas.toList(),
      );

      if (response['success'] == true) {
        Get.snackbar(
          'Sucesso',
          '${entregasSelecionadas.length} entrega(s) selecionada(s)!',
          backgroundColor: Colors.green,
          colorText: Colors.white,
          snackPosition: SnackPosition.BOTTOM,
          duration: Duration(seconds: 2),
        );

        // Agora vai para a tela que busca no banco as selecionadas
        Get.toNamed(AppPages.entregasSelecionadas);
      } else {
        errorMessage.value =
            response['message'] ?? 'Falha ao selecionar entregas';
        Get.snackbar(
          'Erro',
          errorMessage.value,
          backgroundColor: Colors.red,
          colorText: Colors.white,
          snackPosition: SnackPosition.BOTTOM,
        );
      }
    } catch (e) {
      errorMessage.value = 'Erro ao selecionar entregas: ${e.toString()}';
      Get.snackbar(
        'Erro',
        errorMessage.value,
        backgroundColor: Colors.red,
        colorText: Colors.white,
        snackPosition: SnackPosition.BOTTOM,
      );
    } finally {
      isLoading.value = false;
    }
  }
}
