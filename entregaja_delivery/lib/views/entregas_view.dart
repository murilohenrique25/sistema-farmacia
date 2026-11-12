import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:intl/intl.dart';
import '../models/entrega_model.dart';
import '../controllers/entregas_controller.dart';

class EntregasView extends StatelessWidget {
  final EntregasController controller = Get.put(EntregasController());

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Obx(
          () => Text('Entregas Disponíveis (${controller.entregas.length})'),
        ),
        backgroundColor: Theme.of(context).primaryColor,
      ),
      body: Obx(() {
        if (controller.isLoading.value && controller.entregas.isEmpty) {
          return Center(child: CircularProgressIndicator());
        }

        if (controller.entregas.isEmpty) {
          return Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Icon(Icons.inbox, size: 80, color: Colors.grey),
                SizedBox(height: 16),
                Text(
                  'Nenhuma entrega disponível',
                  style: TextStyle(fontSize: 18, color: Colors.grey),
                ),
              ],
            ),
          );
        }

        return Column(
          children: [
            Expanded(
              child: ListView.builder(
                padding: EdgeInsets.all(16),
                itemCount: controller.entregas.length,
                itemBuilder: (context, index) {
                  final entrega = controller.entregas[index];
                  return _buildEntregaCard(entrega, index);
                },
              ),
            ),

            // Rodapé com quantidade selecionada + botão confirmar
            SafeArea(
              top: false,
              child: Container(
                padding: const EdgeInsets.fromLTRB(16, 12, 16, 16),
                decoration: BoxDecoration(
                  color: Colors.white,
                  boxShadow: [
                    BoxShadow(
                      color: Colors.black.withOpacity(0.05),
                      blurRadius: 6,
                      offset: const Offset(0, -2),
                    ),
                  ],
                ),
                child: Obx(() {
                  final qtd = controller.entregasSelecionadas.length;
                  return Column(
                    mainAxisSize: MainAxisSize.min,
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: [
                      Text(
                        qtd == 0
                            ? 'Nenhuma entrega selecionada'
                            : '$qtd entrega(s) selecionada(s)',
                        style: const TextStyle(
                          fontSize: 14,
                          fontWeight: FontWeight.w500,
                        ),
                        textAlign: TextAlign.center,
                      ),
                      const SizedBox(height: 10),
                      ElevatedButton.icon(
                        onPressed:
                            (controller.isLoading.value || qtd == 0)
                                ? null
                                : controller.verEntregasSelecionadas,
                        icon:
                            controller.isLoading.value
                                ? const SizedBox(
                                  width: 16,
                                  height: 16,
                                  child: CircularProgressIndicator(
                                    strokeWidth: 2,
                                    color: Colors.white,
                                  ),
                                )
                                : const Icon(Icons.check),
                        label: const Text('Confirmar seleção'),
                        style: ElevatedButton.styleFrom(
                          backgroundColor: Theme.of(context).primaryColor,
                          foregroundColor: Colors.white,
                          disabledBackgroundColor: Colors.grey.shade300,
                          padding: const EdgeInsets.symmetric(vertical: 14),
                          textStyle: const TextStyle(
                            fontSize: 16,
                            fontWeight: FontWeight.w600,
                          ),
                        ),
                      ),
                    ],
                  );
                }),
              ),
            ),
          ],
        );
      }),
    );
  }

  Widget _buildEntregaCard(EntregaModel entrega, int index) {
    final selecionada = entrega.selecionada;

    return Card(
      margin: EdgeInsets.only(bottom: 16),
      elevation: 4,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      child: InkWell(
        onTap: () => controller.toggleEntregaSelecionada(index),
        borderRadius: BorderRadius.circular(12),
        child: Padding(
          padding: EdgeInsets.all(16),
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Checkbox(
                value: selecionada,
                onChanged: (_) => controller.toggleEntregaSelecionada(index),
              ),
              SizedBox(width: 8),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      children: [
                        Container(
                          padding: EdgeInsets.all(12),
                          decoration: BoxDecoration(
                            gradient: LinearGradient(
                              colors: [Color(0xFF6A11CB), Color(0xFF2575FC)],
                            ),
                            borderRadius: BorderRadius.circular(8),
                          ),
                          child: Icon(
                            Icons.local_shipping,
                            color: Colors.white,
                          ),
                        ),
                        SizedBox(width: 16),
                        Expanded(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                'Entrega #${entrega.idEntrega}',
                                style: TextStyle(
                                  fontSize: 18,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                              SizedBox(height: 4),
                              Container(
                                padding: EdgeInsets.symmetric(
                                  horizontal: 8,
                                  vertical: 4,
                                ),
                                decoration: BoxDecoration(
                                  color: Get.theme.primaryColor.withOpacity(
                                    0.1,
                                  ),
                                  borderRadius: BorderRadius.circular(4),
                                ),
                                child: Text(
                                  entrega.tipoEntrega ?? 'Tipo não informado',
                                  style: TextStyle(
                                    fontSize: 12,
                                    color: Get.theme.primaryColor,
                                    fontWeight: FontWeight.w500,
                                  ),
                                ),
                              ),
                            ],
                          ),
                        ),
                      ],
                    ),
                    SizedBox(height: 12),
                    Divider(),
                    SizedBox(height: 8),

                    Row(
                      children: [
                        Icon(Icons.receipt_long, size: 16, color: Colors.grey),
                        SizedBox(width: 8),
                        Text(
                          'Cupom: ${entrega.cupomOrcamento}',
                          style: TextStyle(fontSize: 14),
                        ),
                      ],
                    ),
                    SizedBox(height: 8),

                    Row(
                      children: [
                        Icon(Icons.attach_money, size: 16, color: Colors.green),
                        SizedBox(width: 8),
                        Text(
                          _formatCurrency(entrega.valorTotal),
                          style: TextStyle(
                            fontSize: 16,
                            fontWeight: FontWeight.bold,
                            color: Colors.green[700],
                          ),
                        ),
                        SizedBox(width: 16),
                        Icon(Icons.payment, size: 16, color: Colors.blue),
                        SizedBox(width: 8),
                        Expanded(
                          child: Text(
                            entrega.formaPagamento,
                            style: TextStyle(fontSize: 14),
                          ),
                        ),
                      ],
                    ),

                    if (entrega.observacao != null &&
                        entrega.observacao!.isNotEmpty)
                      Padding(
                        padding: EdgeInsets.only(top: 8),
                        child: Row(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Icon(
                              Icons.info_outline,
                              size: 16,
                              color: Colors.orange,
                            ),
                            SizedBox(width: 8),
                            Expanded(
                              child: Text(
                                entrega.observacao!,
                                style: TextStyle(
                                  fontSize: 12,
                                  color: Colors.grey[700],
                                  fontStyle: FontStyle.italic,
                                ),
                                maxLines: 2,
                                overflow: TextOverflow.ellipsis,
                              ),
                            ),
                          ],
                        ),
                      ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  String _formatCurrency(double value) {
    final formatter = NumberFormat.currency(
      locale: 'pt_BR',
      symbol: 'R\$',
      decimalDigits: 2,
    );
    return formatter.format(value);
  }
}
