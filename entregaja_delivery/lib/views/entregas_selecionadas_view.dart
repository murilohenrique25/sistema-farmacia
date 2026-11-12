import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:intl/intl.dart';
import '../models/entrega_model.dart';
import '../controllers/entregas_selecionadas_controller.dart';

class EntregasSelecionadasView extends StatelessWidget {
  const EntregasSelecionadasView({super.key});

  @override
  Widget build(BuildContext context) {
    final controller = Get.find<EntregasSelecionadasController>();

    return Scaffold(
      appBar: AppBar(
        backgroundColor: const Color(0xFF6A11CB),
        elevation: 0,
        title: Obx(
          () => Text(
            'Minhas Entregas (${controller.entregasSelecionadas.length + controller.entregasEmRota.length})',
            style: const TextStyle(
              color: Colors.white,
              fontSize: 20,
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: Colors.white),
          onPressed: () => Get.back(),
        ),
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh, color: Colors.white),
            onPressed: controller.atualizar,
            tooltip: 'Atualizar lista',
          ),
        ],
      ),
      body: Container(
        decoration: const BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
            colors: [Color(0xFF6A11CB), Color(0xFF2575FC)],
          ),
        ),
        child: Obx(() {
          if (controller.isLoading.value) {
            return const Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  CircularProgressIndicator(
                    color: Colors.white,
                    strokeWidth: 3,
                  ),
                  SizedBox(height: 16),
                  Text(
                    'Carregando entregas...',
                    style: TextStyle(color: Colors.white, fontSize: 16),
                  ),
                ],
              ),
            );
          }

          final temEmRota = controller.entregasEmRota.isNotEmpty;
          final temSelecionadas = controller.entregasSelecionadas.isNotEmpty;

          if (!temEmRota && !temSelecionadas) {
            return Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Container(
                    padding: const EdgeInsets.all(20),
                    decoration: BoxDecoration(
                      color: Colors.white.withValues(alpha: 0.1),
                      shape: BoxShape.circle,
                    ),
                    child: const Icon(
                      Icons.inbox,
                      size: 80,
                      color: Colors.white,
                    ),
                  ),
                  const SizedBox(height: 24),
                  const Text(
                    'Nenhuma entrega encontrada',
                    style: TextStyle(
                      fontSize: 20,
                      color: Colors.white,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  const SizedBox(height: 8),
                  const Text(
                    'Selecione entregas na tela anterior',
                    style: TextStyle(fontSize: 14, color: Colors.white70),
                  ),
                  const SizedBox(height: 24),
                  ElevatedButton.icon(
                    onPressed: controller.atualizar,
                    icon: const Icon(Icons.refresh),
                    label: const Text('Atualizar'),
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.white,
                      foregroundColor: const Color(0xFF6A11CB),
                      padding: const EdgeInsets.symmetric(
                        horizontal: 24,
                        vertical: 12,
                      ),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(25),
                      ),
                    ),
                  ),
                ],
              ),
            );
          }

          return RefreshIndicator(
            onRefresh: controller.atualizar,
            color: const Color(0xFF6A11CB),
            backgroundColor: Colors.white,
            child: ListView(
              padding: const EdgeInsets.all(16),
              physics: const AlwaysScrollableScrollPhysics(),
              children: [
                // Seção Em Rota
                if (temEmRota) ...[
                  _buildSectionHeader(
                    icon: Icons.directions_run,
                    label: 'Em Rota',
                    color: Colors.orange,
                  ),
                  const SizedBox(height: 8),
                  ...controller.entregasEmRota.map(
                    (e) => _buildEntregaCard(e, controller, isEmRota: true),
                  ),
                  const SizedBox(height: 16),
                ],

                // Seção Selecionadas
                if (temSelecionadas) ...[
                  _buildSectionHeader(
                    icon: Icons.check_circle_outline,
                    label: 'Selecionadas',
                    color: Colors.greenAccent,
                  ),
                  const SizedBox(height: 8),
                  ...controller.entregasSelecionadas.map(
                    (e) => _buildEntregaCard(e, controller, isEmRota: false),
                  ),
                ],
              ],
            ),
          );
        }),
      ),
    );
  }

  Widget _buildSectionHeader({
    required IconData icon,
    required String label,
    required Color color,
  }) {
    return Row(
      children: [
        Icon(icon, color: color, size: 20),
        const SizedBox(width: 8),
        Text(
          label,
          style: TextStyle(
            color: color,
            fontSize: 16,
            fontWeight: FontWeight.bold,
            letterSpacing: 0.5,
          ),
        ),
      ],
    );
  }

  Widget _buildEntregaCard(
    EntregaModel entrega,
    EntregasSelecionadasController controller, {
    required bool isEmRota,
  }) {
    return Card(
      margin: const EdgeInsets.only(bottom: 12),
      elevation: 6,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
      child: InkWell(
        onTap: () => controller.abrirDetalhes(entrega),
        borderRadius: BorderRadius.circular(16),
        child: Container(
          decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(16),
            gradient: LinearGradient(
              begin: Alignment.topLeft,
              end: Alignment.bottomRight,
              colors:
                  isEmRota
                      ? [Colors.orange.shade50, Colors.orange.shade100]
                      : [Colors.white, Colors.grey.shade50],
            ),
          ),
          child: Padding(
            padding: const EdgeInsets.all(16),
            child: Row(
              children: [
                // Ícone
                Container(
                  width: 60,
                  height: 60,
                  decoration: BoxDecoration(
                    gradient: LinearGradient(
                      colors:
                          isEmRota
                              ? [Colors.orange, Colors.deepOrange]
                              : [
                                const Color(0xFF6A11CB),
                                const Color(0xFF2575FC),
                              ],
                      begin: Alignment.topLeft,
                      end: Alignment.bottomRight,
                    ),
                    borderRadius: BorderRadius.circular(14),
                  ),
                  child: Icon(
                    isEmRota ? Icons.directions_run : Icons.local_shipping,
                    color: Colors.white,
                    size: 30,
                  ),
                ),
                const SizedBox(width: 14),

                // Informações
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        'Entrega #${entrega.idEntrega}',
                        style: const TextStyle(
                          fontSize: 17,
                          fontWeight: FontWeight.bold,
                          color: Color(0xFF1F2937),
                        ),
                      ),
                      const SizedBox(height: 4),
                      if (entrega.tipoEntrega != null)
                        Text(
                          entrega.tipoEntrega!,
                          style: TextStyle(
                            fontSize: 12,
                            color:
                                isEmRota
                                    ? Colors.orange.shade800
                                    : const Color(0xFF6A11CB),
                            fontWeight: FontWeight.w600,
                          ),
                        ),
                      const SizedBox(height: 6),
                      Row(
                        children: [
                          Icon(
                            Icons.receipt_long,
                            size: 14,
                            color: Colors.grey[600],
                          ),
                          const SizedBox(width: 4),
                          Expanded(
                            child: Text(
                              entrega.cupomOrcamento,
                              style: TextStyle(
                                fontSize: 13,
                                color: Colors.grey[700],
                              ),
                              overflow: TextOverflow.ellipsis,
                            ),
                          ),
                          const SizedBox(width: 8),
                          Icon(
                            Icons.attach_money,
                            size: 14,
                            color: Colors.green[700],
                          ),
                          Text(
                            _formatCurrency(entrega.valorTotal),
                            style: TextStyle(
                              fontSize: 13,
                              fontWeight: FontWeight.bold,
                              color: Colors.green[700],
                            ),
                          ),
                        ],
                      ),
                    ],
                  ),
                ),

                // Badge de status + seta
                Column(
                  crossAxisAlignment: CrossAxisAlignment.end,
                  children: [
                    Container(
                      padding: const EdgeInsets.symmetric(
                        horizontal: 8,
                        vertical: 4,
                      ),
                      decoration: BoxDecoration(
                        color: isEmRota ? Colors.orange : Colors.green,
                        borderRadius: BorderRadius.circular(8),
                      ),
                      child: Text(
                        isEmRota ? 'Em Rota' : 'Selecionada',
                        style: const TextStyle(
                          color: Colors.white,
                          fontSize: 11,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                    const SizedBox(height: 8),
                    Icon(
                      Icons.chevron_right,
                      color: isEmRota ? Colors.orange : const Color(0xFF6A11CB),
                    ),
                  ],
                ),
              ],
            ),
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
