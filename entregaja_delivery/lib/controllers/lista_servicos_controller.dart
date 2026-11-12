import 'package:entregaja_delivery/models/entrega_avulsa.dart';
import 'package:entregaja_delivery/routes/app_pages.dart';
import 'package:entregaja_delivery/services/api_service.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:shared_preferences/shared_preferences.dart';

class ListaServicosController extends GetxController {
  final descricaoController = TextEditingController();
  final observacaoController = TextEditingController();

  final dataHoraInicioServico = ''.obs;
  final userId = ''.obs;
  final caminhoFinal = ''.obs;
  final isLoading = false.obs;

  final ApiService apiServicesEntrega = ApiService();
  final Future<SharedPreferences> _prefs = SharedPreferences.getInstance();

  @override
  void onInit() {
    super.onInit();
    verificaSeExisteServico();
  }

  @override
  void onClose() {
    descricaoController.dispose();
    observacaoController.dispose();
    super.onClose();
  }

  Future<void> verificaSeExisteServico() async {
    final SharedPreferences prefs = await _prefs;

    if (!prefs.containsKey("dataHoraInicioServico") ||
        prefs.getString("dataHoraInicioServico") == "") {
      dataHoraInicioServico.value = atualizarHorarioEntrega();
    } else {
      descricaoController.text = prefs.getString("descricaoServico") ?? "";
      dataHoraInicioServico.value =
          prefs.getString("dataHoraInicioServico") ?? "";
      observacaoController.text = prefs.getString("observacaoServico") ?? "";
      userId.value = prefs.getString("usuario") ?? "";
    }
  }

  String atualizarHorarioEntrega() {
    DateTime now = DateTime.now();
    return now.toIso8601String();
  }

  Future<void> salvarEmCache() async {
    final SharedPreferences prefs = await _prefs;
    prefs.setString("dataHoraInicioServico", dataHoraInicioServico.value);
    prefs.setString("descricaoServico", descricaoController.text);
    prefs.setString("observacaoServico", observacaoController.text);
    prefs.setString("usuario", userId.value);
  }

  EntregaAvulsa criarEntrega() {
    return EntregaAvulsa(
      dataHoraInicioServico: dataHoraInicioServico.value,
      dataHoraTerminoServico: atualizarHorarioEntrega(),
      descricaoServico: descricaoController.text,
      observacaoServico: observacaoController.text,
      userId: userId.value,
    );
  }

  Future<void> enviarEntrega() async {
    isLoading.value = true;

    try {
      final response = await apiServicesEntrega.inserirEntregaAvulsa(
        criarEntrega(),
      );

      if (response['success']) {
        await limparCache();
        chamarTelaEntregas();
      } else {
        Get.snackbar(
          'Erro',
          'Erro ao enviar entrega de serviço, verifique internet e tente novamente.',
          backgroundColor: Colors.red,
          colorText: Colors.white,
          snackPosition: SnackPosition.TOP,
        );
      }
    } catch (e) {
      Get.snackbar(
        'Erro',
        'Erro inesperado: $e',
        backgroundColor: Colors.red,
        colorText: Colors.white,
        snackPosition: SnackPosition.TOP,
      );
    } finally {
      isLoading.value = false;
    }
  }

  Future<void> limparCache() async {
    final SharedPreferences prefs = await _prefs;
    prefs.setString("dataHoraInicioServico", '');
    prefs.setString("descricaoServico", '');
    prefs.setString("observacaoServico", '');
    prefs.setString("usuario", '');
  }

  void chamarTelaEntregas() {
    Get.snackbar(
      'Sucesso',
      'Serviço incluído com sucesso!',
      backgroundColor: Colors.green,
      colorText: Colors.white,
      snackPosition: SnackPosition.TOP,
    );

    Get.toNamed(AppPages.home);
  }
}
