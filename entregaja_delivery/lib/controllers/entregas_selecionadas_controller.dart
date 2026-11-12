import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../models/entrega_model.dart';
import '../routes/app_pages.dart';
import '../services/api_service.dart';

class EntregasSelecionadasController extends GetxController {
  final ApiService apiService = ApiService();

  final RxList<EntregaModel> entregasSelecionadas = <EntregaModel>[].obs;
  final RxList<EntregaModel> entregasEmRota = <EntregaModel>[].obs;
  final RxBool isLoading = false.obs;

  @override
  void onInit() {
    super.onInit();
    buscarEntregasSelecionadas();
  }

  Future<void> buscarEntregasSelecionadas() async {
    isLoading.value = true;

    try {
      final response = await apiService.getEntregasSelecionadas();

      if (response['success']) {
        final List<dynamic> data = response['data'];
        final todas = data.map((item) => EntregaModel.fromJson(item)).toList();

        entregasEmRota.value = todas.where((e) => e.isEmRota).toList();
        entregasSelecionadas.value =
            todas.where((e) => e.isSelecionada).toList();

        // Log para debug — remover após confirmar os status da API
        for (final e in todas) {
          debugPrint(
            '📦 Entrega #${e.idEntrega} status: "${e.statusEntrega}"'
            ' | isEmRota: ${e.isEmRota} | isSelecionada: ${e.isSelecionada}',
          );
        }
      } else {
        entregasSelecionadas.clear();
        entregasEmRota.clear();
      }
    } catch (e) {
      Get.snackbar(
        'Erro',
        'Erro ao buscar entregas: ${e.toString()}',
        snackPosition: SnackPosition.BOTTOM,
      );
      entregasSelecionadas.clear();
      entregasEmRota.clear();
    } finally {
      isLoading.value = false;
    }
  }

  void abrirDetalhes(EntregaModel entrega) {
    Get.toNamed(
      AppPages.entregasDetalhes,
      arguments: entrega,
    )?.then((_) => buscarEntregasSelecionadas());
  }

  Future<void> atualizar() async {
    await buscarEntregasSelecionadas();
  }
}
