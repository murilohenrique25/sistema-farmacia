import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:intl/intl.dart';
import '../controllers/lista_servicos_controller.dart';

class ListaServicosView extends StatelessWidget {
  final ListaServicosController controller = Get.find<ListaServicosController>();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Serviço'),
        centerTitle: true,
        backgroundColor: const Color.fromARGB(255, 30, 64, 120),
      ),
      body: SingleChildScrollView(
        child: Padding(
          padding: const EdgeInsets.all(8.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const SizedBox(height: 16.0),
              Obx(() => Text(
                'Inicio: ${controller.dataHoraInicioServico.value.isNotEmpty
                  ? DateFormat('dd/MM/yyyy HH:mm').format(DateTime.parse(controller.dataHoraInicioServico.value))
                  : 'Não definido'}',
              )),
              const SizedBox(height: 16.0),
              TextField(
                controller: controller.descricaoController,
                decoration: const InputDecoration(labelText: 'Descrição'),
                onChanged: (value) => controller.salvarEmCache(),
              ),
              const SizedBox(height: 16.0),
              TextField(
                controller: controller.observacaoController,
                decoration: const InputDecoration(labelText: 'Observação'),
                onChanged: (value) => controller.salvarEmCache(),
              ),
              const SizedBox(height: 16.0),
              const SizedBox(height: 16.0),
              Row(
                children: [
                  Expanded(
                    child: ElevatedButton(
                      onPressed: () {
                        controller.salvarEmCache();
                        Get.snackbar(
                          'Sucesso',
                          'Dados salvos no celular, ao finalizar serviço clique em "Enviar"',
                          backgroundColor: Colors.green,
                          colorText: Colors.white,
                          snackPosition: SnackPosition.TOP,
                        );
                      },
                      child: const Text('Salvar'),
                    ),
                  ),
                  const SizedBox(width: 16.0),
                  Expanded(
                    child: Obx(() => ElevatedButton(
                      onPressed: controller.isLoading.value
                        ? null
                        : () => controller.enviarEntrega(),
                      style: ButtonStyle(
                        backgroundColor: MaterialStateProperty.all<Color>(
                          const Color.fromARGB(255, 30, 64, 120),
                        ),
                        foregroundColor: MaterialStateProperty.all<Color>(
                          Colors.white,
                        ),
                      ),
                      child: controller.isLoading.value
                        ? const SizedBox(
                            height: 16,
                            width: 16,
                            child: CircularProgressIndicator(
                              color: Colors.white,
                              strokeWidth: 2,
                            ),
                          )
                        : const Text('Enviar'),
                    )),
                  ),
                ],
              ),
              const SizedBox(height: 16.0),
            ],
          ),
        ),
      ),
    );
  }
}