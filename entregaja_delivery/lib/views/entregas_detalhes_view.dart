import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:intl/intl.dart';
import '../controllers/entregas_detalhes_controller.dart';
import '../models/entrega_model.dart';

class EntregasDetalhesView extends StatelessWidget {
  final controller = Get.put(EntregasDetalhesController());

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Detalhes da Entrega'),
        backgroundColor: Theme.of(context).primaryColor,
      ),
      body: Obx(() {
        final entrega = controller.entregaAtual.value;

        if (entrega == null) {
          return Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Icon(Icons.error_outline, size: 60, color: Colors.grey),
                SizedBox(height: 16),
                Text('Entrega não encontrada'),
              ],
            ),
          );
        }

        return SingleChildScrollView(
          child: Column(
            children: [
              // Header com gradiente
              Container(
                width: double.infinity,
                padding: EdgeInsets.all(24),
                decoration: BoxDecoration(
                  gradient: LinearGradient(
                    colors: [Color(0xFF6A11CB), Color(0xFF2575FC)],
                  ),
                ),
                child: Column(
                  children: [
                    Icon(Icons.local_shipping, size: 60, color: Colors.white),
                    SizedBox(height: 16),
                    Text(
                      'Entrega #${entrega.idEntrega}',
                      style: TextStyle(
                        color: Colors.white,
                        fontSize: 24,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    SizedBox(height: 8),
                    if (entrega.tipoEntrega != null)
                      Container(
                        padding: EdgeInsets.symmetric(
                          horizontal: 12,
                          vertical: 6,
                        ),
                        decoration: BoxDecoration(
                          color: Colors.white.withOpacity(0.2),
                          borderRadius: BorderRadius.circular(20),
                        ),
                        child: Text(
                          entrega.tipoEntrega!,
                          style: TextStyle(
                            color: Colors.white,
                            fontSize: 14,
                            fontWeight: FontWeight.w500,
                          ),
                        ),
                      ),
                  ],
                ),
              ),

              // Informações da entrega
              Padding(
                padding: EdgeInsets.all(16),
                child: Column(
                  children: [
                    // Cupom/Orçamento
                    _buildInfoCard(
                      icon: Icons.receipt_long,
                      title: 'Cupom/Orçamento',
                      value: entrega.cupomOrcamento,
                      color: Colors.blue,
                    ),
                    SizedBox(height: 12),

                    // Valores
                    _buildValoresCard(entrega),
                    SizedBox(height: 12),

                    // Forma de Pagamento
                    _buildInfoCard(
                      icon: Icons.payment,
                      title: 'Forma de Pagamento',
                      value: entrega.formaPagamento,
                      color: Colors.green,
                    ),
                    SizedBox(height: 12),

                    // Troco
                    if (entrega.troco > 0)
                      _buildInfoCard(
                        icon: Icons.attach_money,
                        title: 'Troco para',
                        value: _formatCurrency(entrega.troco),
                        color: Colors.orange,
                      ),

                    if (entrega.troco > 0) SizedBox(height: 12),

                    // Observação
                    if (entrega.observacao != null &&
                        entrega.observacao!.isNotEmpty)
                      _buildInfoCard(
                        icon: Icons.info_outline,
                        title: 'Observação',
                        value: entrega.observacao!,
                        color: Colors.purple,
                      ),
                  ],
                ),
              ),

              // Status da conexão — só exibe quando rastreamento está ativo
              Obx(
                () =>
                    controller.isRastreando.value
                        ? Padding(
                          padding: EdgeInsets.symmetric(horizontal: 16),
                          child: Container(
                            padding: EdgeInsets.all(12),
                            decoration: BoxDecoration(
                              color:
                                  controller.wsService.isConnected.value
                                      ? Colors.green.withValues(alpha: 0.1)
                                      : Colors.orange.withValues(alpha: 0.1),
                              borderRadius: BorderRadius.circular(8),
                              border: Border.all(
                                color:
                                    controller.wsService.isConnected.value
                                        ? Colors.green
                                        : Colors.orange,
                              ),
                            ),
                            child: Row(
                              children: [
                                Icon(
                                  controller.wsService.isConnected.value
                                      ? Icons.check_circle
                                      : Icons.warning,
                                  color:
                                      controller.wsService.isConnected.value
                                          ? Colors.green
                                          : Colors.orange,
                                ),
                                SizedBox(width: 12),
                                Expanded(
                                  child: Text(
                                    controller.wsService.isConnected.value
                                        ? 'Conectado ao servidor'
                                        : 'Aguardando conexão',
                                    style: TextStyle(
                                      fontWeight: FontWeight.w500,
                                    ),
                                  ),
                                ),
                              ],
                            ),
                          ),
                        )
                        : SizedBox.shrink(),
              ),

              SizedBox(height: 24),

              // Botões de ação
              Padding(
                padding: EdgeInsets.all(16),
                child: Obx(() {
                  final isRastreando = controller.isRastreando.value;
                  final isFinalizando = controller.isFinalizando.value;
                  final isIniciando = controller.isIniciando.value;
                  final temOutraEntregaAtiva =
                      controller.entregaAtivaCtrl.temEntregaAtiva &&
                      !controller.isEntregaAtiva;

                  return Column(
                    children: [
                      // Botão Iniciar Entrega
                      if (!isRastreando)
                        SizedBox(
                          width: double.infinity,
                          child: ElevatedButton.icon(
                            onPressed:
                                (temOutraEntregaAtiva || isIniciando)
                                    ? null
                                    : controller.iniciarEntrega,
                            icon:
                                isIniciando
                                    ? SizedBox(
                                      width: 20,
                                      height: 20,
                                      child: CircularProgressIndicator(
                                        color: Colors.white,
                                        strokeWidth: 2,
                                      ),
                                    )
                                    : Icon(Icons.play_arrow, size: 28),
                            label: Text(
                              isIniciando ? 'Iniciando...' : 'Iniciar Entrega',
                              style: TextStyle(fontSize: 18),
                            ),
                            style: ElevatedButton.styleFrom(
                              backgroundColor: Colors.green,
                              foregroundColor: Colors.white,
                              padding: EdgeInsets.symmetric(vertical: 16),
                              shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(12),
                              ),
                              disabledBackgroundColor: Colors.grey,
                            ),
                          ),
                        ),

                      // Mensagem quando há outra entrega ativa
                      if (temOutraEntregaAtiva)
                        Padding(
                          padding: EdgeInsets.only(top: 8),
                          child: Container(
                            padding: EdgeInsets.all(12),
                            decoration: BoxDecoration(
                              color: Colors.orange.withOpacity(0.1),
                              borderRadius: BorderRadius.circular(8),
                              border: Border.all(color: Colors.orange),
                            ),
                            child: Row(
                              children: [
                                Icon(
                                  Icons.warning,
                                  color: Colors.orange,
                                  size: 20,
                                ),
                                SizedBox(width: 8),
                                Expanded(
                                  child: Text(
                                    'Você já tem uma entrega em andamento. Finalize-a antes de iniciar outra.',
                                    style: TextStyle(
                                      color: Colors.orange[900],
                                      fontSize: 12,
                                    ),
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ),

                      // Botão Finalizar
                      if (isRastreando)
                        SizedBox(
                          width: double.infinity,
                          child: ElevatedButton.icon(
                            onPressed:
                                isFinalizando
                                    ? null
                                    : controller.finalizarEntrega,
                            icon:
                                isFinalizando
                                    ? const SizedBox(
                                      width: 20,
                                      height: 20,
                                      child: CircularProgressIndicator(
                                        color: Colors.white,
                                        strokeWidth: 2,
                                      ),
                                    )
                                    : const Icon(Icons.check_circle, size: 28),
                            label: Text(
                              isFinalizando
                                  ? 'Aguarde...'
                                  : 'Finalizar Entrega',
                              style: const TextStyle(fontSize: 18),
                            ),
                            style: ElevatedButton.styleFrom(
                              backgroundColor: Colors.red,
                              foregroundColor: Colors.white,
                              padding: const EdgeInsets.symmetric(vertical: 16),
                              shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(12),
                              ),
                            ),
                          ),
                        ),

                      // Indicador de rastreamento ativo
                      if (isRastreando)
                        Padding(
                          padding: EdgeInsets.only(top: 16),
                          child: Container(
                            padding: EdgeInsets.all(12),
                            decoration: BoxDecoration(
                              color: Colors.blue.withOpacity(0.1),
                              borderRadius: BorderRadius.circular(8),
                              border: Border.all(color: Colors.blue),
                            ),
                            child: Column(
                              children: [
                                Row(
                                  mainAxisAlignment: MainAxisAlignment.center,
                                  children: [
                                    Icon(Icons.my_location, color: Colors.blue),
                                    SizedBox(width: 8),
                                    Text(
                                      'Rastreamento ativo',
                                      style: TextStyle(
                                        color: Colors.blue,
                                        fontWeight: FontWeight.w500,
                                      ),
                                    ),
                                  ],
                                ),
                                SizedBox(height: 8),
                                Obx(() {
                                  final pos = controller.currentPosition.value;
                                  if (pos != null) {
                                    return Text(
                                      'Última posição: ${pos.latitude.toStringAsFixed(6)}, ${pos.longitude.toStringAsFixed(6)}',
                                      style: TextStyle(
                                        color: Colors.blue[700],
                                        fontSize: 11,
                                      ),
                                    );
                                  }
                                  return SizedBox.shrink();
                                }),
                              ],
                            ),
                          ),
                        ),
                    ],
                  );
                }),
              ),

              SizedBox(height: 16),
            ],
          ),
        );
      }),
    );
  }

  Widget _buildValoresCard(EntregaModel entrega) {
    return Container(
      padding: EdgeInsets.all(16),
      decoration: BoxDecoration(
        gradient: LinearGradient(
          colors: [Color(0xFFFF416C), Color(0xFFFF4B2B)],
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
        ),
        borderRadius: BorderRadius.circular(12),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.1),
            blurRadius: 8,
            offset: Offset(0, 2),
          ),
        ],
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(
            'Valor Total',
            style: TextStyle(color: Colors.white, fontSize: 14),
          ),
          Text(
            _formatCurrency(entrega.valorTotal),
            style: TextStyle(
              color: Colors.white,
              fontSize: 18,
              fontWeight: FontWeight.bold,
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildInfoCard({
    required IconData icon,
    required String title,
    required String value,
    required Color color,
  }) {
    return Container(
      padding: EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(12),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.05),
            blurRadius: 8,
            offset: Offset(0, 2),
          ),
        ],
      ),
      child: Row(
        children: [
          Container(
            padding: EdgeInsets.all(12),
            decoration: BoxDecoration(
              color: color.withOpacity(0.1),
              borderRadius: BorderRadius.circular(8),
            ),
            child: Icon(icon, color: color),
          ),
          SizedBox(width: 16),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  title,
                  style: TextStyle(fontSize: 12, color: Colors.grey[600]),
                ),
                SizedBox(height: 4),
                Text(
                  value,
                  style: TextStyle(fontSize: 16, fontWeight: FontWeight.w500),
                ),
              ],
            ),
          ),
        ],
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
