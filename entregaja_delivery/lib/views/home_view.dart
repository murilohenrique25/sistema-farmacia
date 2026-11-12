import 'package:entregaja_delivery/models/storage_service.dart';
import 'package:entregaja_delivery/routes/app_pages.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../controllers/home_controller.dart';
import '../controllers/auth_controller.dart';

class HomeView extends StatelessWidget {
  final HomeController controller = Get.find<HomeController>();
  final AuthController authController = Get.find<AuthController>();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor:
            Colors
                .transparent, // Transparente para manter o visual do gradiente
        elevation: 0, // Sem sombra
        iconTheme: IconThemeData(
          color: const Color.fromARGB(255, 59, 1, 194),
        ), // Cor do ícone do menu
        title: Text(
          'EntregaJá',
          textAlign: TextAlign.center,
          style: TextStyle(color: const Color.fromARGB(255, 59, 1, 194)),
        ),
      ),
      onDrawerChanged: (isOpen) {
        if (isOpen) controller.refreshDadosEntregador();
      },
      drawer: Drawer(
        child: Column(
          children: [
            DrawerHeader(
              decoration: BoxDecoration(color: Theme.of(context).primaryColor),
              child: Container(
                width: double.infinity,
                child: Text.rich(
                  TextSpan(
                    text: 'Resumo de Entregas\n',
                    children: [
                      TextSpan(
                        text: 'Os dados são carregados ao realizar login',
                        style: TextStyle(
                          color: Colors.white,
                          fontSize: 10,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ],
                  ),

                  style: TextStyle(
                    color: Colors.white,
                    fontSize: 24,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
            ),
            // Conteúdo do drawer usando DadosEntregadorApp
            Expanded(
              child: Obx(() {
                if (controller.isLoading.value) {
                  return Center(child: CircularProgressIndicator());
                }

                var dados = Get.find<StorageService>().getUsuarioLogado();
                return Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      _buildInfoCard(
                        context,
                        title: 'Total de Entregas Finalizadas',
                        value: '${dados?.dadosEntregadorApp.quantidadeEntrega}',
                        icon: Icons.delivery_dining,
                      ),
                      SizedBox(height: 16),
                      _buildInfoCard(
                        context,
                        title: 'Média por Entrega',
                        value: '${dados?.dadosEntregadorApp.mediaTotal}',
                        icon: Icons.analytics,
                      ),
                    ],
                  ),
                );
              }),
            ),

            Padding(
              padding: const EdgeInsets.all(16.0),
              child: SizedBox(
                width: double.infinity,
                child: ElevatedButton.icon(
                  onPressed: () {
                    Get.toNamed(AppPages.servicos);
                  },
                  icon: Icon(Icons.add),
                  label: Text('Cadastrar Serviço Avulso'),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Theme.of(context).primaryColor,
                    foregroundColor: Colors.white,
                    padding: EdgeInsets.symmetric(vertical: 16),
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
      body: Container(
        decoration: BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
            colors: [Color(0xFF2B32B2), Color(0xFF1488CC)],
          ),
        ),
        child: SafeArea(
          child: Padding(
            padding: const EdgeInsets.all(24.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                // Barra superior com título e botão de logout
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text(
                      'Seja Bem-vindo',
                      style: TextStyle(
                        fontSize: 28,
                        fontWeight: FontWeight.bold,
                        color: Colors.white,
                      ),
                    ),
                    IconButton(
                      icon: Icon(Icons.logout, color: Colors.white),
                      onPressed: () => authController.logout(),
                    ),
                  ],
                ),
                SizedBox(height: 40),

                // Container principal
                Expanded(
                  child: Container(
                    width: double.infinity,
                    decoration: BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.circular(20),
                      boxShadow: [
                        BoxShadow(
                          color: Colors.black.withOpacity(0.1),
                          blurRadius: 20,
                          spreadRadius: 5,
                        ),
                      ],
                    ),
                    child: Padding(
                      padding: const EdgeInsets.all(24.0),
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Text(
                            'Escolha uma ação',
                            style: TextStyle(
                              fontSize: 24,
                              fontWeight: FontWeight.bold,
                              color: Colors.black87,
                            ),
                          ),
                          SizedBox(height: 40),

                          // Botões de ação
                          Obx(
                            () => Row(
                              children: [
                                // Botão 1
                                if (controller.getQuantidadePorTipo(
                                          'Venda Balcão',
                                        ) ==
                                        0 &&
                                    controller.getQuantidadePorTipo(
                                          'Manipulação',
                                        ) ==
                                        0)
                                  Expanded(
                                    child: Column(
                                      children: [
                                        Text(
                                          'Não existe entrega cadastrada, atualize a página e verifique novas entregas',
                                          textAlign: TextAlign.center,
                                          style: TextStyle(
                                            fontSize: 14,
                                            fontWeight: FontWeight.bold,
                                            color: Colors.black87,
                                          ),
                                        ),
                                        SizedBox(height: 10),
                                        GestureDetector(
                                          onTap:
                                              controller.isLoading.value
                                                  ? null
                                                  : () =>
                                                      controller.fetchData(),
                                          child: Container(
                                            height: 120,
                                            width: 120,
                                            decoration: BoxDecoration(
                                              gradient: LinearGradient(
                                                colors: [
                                                  Color.fromARGB(
                                                    255,
                                                    28,
                                                    95,
                                                    25,
                                                  ),
                                                  Color.fromARGB(
                                                    255,
                                                    93,
                                                    248,
                                                    2,
                                                  ),
                                                ],
                                                begin: Alignment.topLeft,
                                                end: Alignment.bottomRight,
                                              ),
                                              borderRadius:
                                                  BorderRadius.circular(20),
                                              boxShadow: [
                                                BoxShadow(
                                                  color: Color(
                                                    0xFFFF416C,
                                                  ).withOpacity(0.3),
                                                  blurRadius: 15,
                                                  offset: Offset(0, 8),
                                                ),
                                              ],
                                            ),
                                            child: Center(
                                              child:
                                                  controller.isLoading.value
                                                      ? CircularProgressIndicator(
                                                        color: Colors.white,
                                                      )
                                                      : Column(
                                                        mainAxisSize:
                                                            MainAxisSize.min,
                                                        children: [
                                                          Icon(
                                                            Icons.refresh,
                                                            size: 50,
                                                            color: Colors.white,
                                                          ),
                                                          SizedBox(height: 12),
                                                          Text(
                                                            'Atualizar',
                                                            textAlign:
                                                                TextAlign
                                                                    .center,
                                                            style: TextStyle(
                                                              fontSize: 18,
                                                              fontWeight:
                                                                  FontWeight
                                                                      .bold,
                                                              color:
                                                                  Colors.white,
                                                            ),
                                                          ),
                                                        ],
                                                      ),
                                            ),
                                          ),
                                        ),
                                      ],
                                    ),
                                  ),

                                if (controller.getQuantidadePorTipo(
                                      'Venda Balcão',
                                    ) >
                                    0)
                                  Expanded(
                                    child: GestureDetector(
                                      onTap:
                                          (controller.isLoading.value ||
                                                  controller
                                                      .isVerificando
                                                      .value)
                                              ? null
                                              : () =>
                                                  controller.executeAction1(),
                                      child: Container(
                                        height: 160,
                                        decoration: BoxDecoration(
                                          gradient: LinearGradient(
                                            colors: [
                                              Color(0xFF6A11CB),
                                              Color(0xFF2575FC),
                                            ],
                                            begin: Alignment.topLeft,
                                            end: Alignment.bottomRight,
                                          ),
                                          borderRadius: BorderRadius.circular(
                                            20,
                                          ),
                                          boxShadow: [
                                            BoxShadow(
                                              color: Color(
                                                0xFF6A11CB,
                                              ).withOpacity(0.3),
                                              blurRadius: 15,
                                              offset: Offset(0, 8),
                                            ),
                                          ],
                                        ),
                                        child: Center(
                                          child:
                                              controller.isLoading.value
                                                  ? CircularProgressIndicator(
                                                    color: Colors.white,
                                                  )
                                                  : Column(
                                                    mainAxisSize:
                                                        MainAxisSize.min,
                                                    children: [
                                                      Icon(
                                                        Icons.card_travel_sharp,
                                                        size: 50,
                                                        color: Colors.white,
                                                      ),
                                                      SizedBox(height: 12),
                                                      Text(
                                                        'Entregas\n${controller.getQuantidadePorTipo('Venda Balcão')}',
                                                        textAlign:
                                                            TextAlign.center,
                                                        style: TextStyle(
                                                          fontSize: 18,
                                                          fontWeight:
                                                              FontWeight.bold,
                                                          color: Colors.white,
                                                        ),
                                                      ),
                                                    ],
                                                  ),
                                        ),
                                      ),
                                    ),
                                  ),
                                SizedBox(width: 20),

                                // Botão 2
                                if (controller.getQuantidadePorTipo(
                                      'Manipulação',
                                    ) >
                                    0)
                                  Expanded(
                                    child: GestureDetector(
                                      onTap:
                                          (controller.isLoading.value ||
                                                  controller
                                                      .isVerificando
                                                      .value)
                                              ? null
                                              : () =>
                                                  controller.executeAction2(),
                                      child: Container(
                                        height: 160,
                                        decoration: BoxDecoration(
                                          gradient: LinearGradient(
                                            colors: [
                                              Color(0xFFFF416C),
                                              Color(0xFFFF4B2B),
                                            ],
                                            begin: Alignment.topLeft,
                                            end: Alignment.bottomRight,
                                          ),
                                          borderRadius: BorderRadius.circular(
                                            20,
                                          ),
                                          boxShadow: [
                                            BoxShadow(
                                              color: Color(
                                                0xFFFF416C,
                                              ).withOpacity(0.3),
                                              blurRadius: 15,
                                              offset: Offset(0, 8),
                                            ),
                                          ],
                                        ),
                                        child: Center(
                                          child:
                                              controller.isLoading.value
                                                  ? CircularProgressIndicator(
                                                    color: Colors.white,
                                                  )
                                                  : Column(
                                                    mainAxisSize:
                                                        MainAxisSize.min,
                                                    children: [
                                                      Icon(
                                                        Icons
                                                            .card_travel_rounded,
                                                        size: 50,
                                                        color: Colors.white,
                                                      ),
                                                      SizedBox(height: 12),
                                                      Text(
                                                        'Manipulação\n${controller.getQuantidadePorTipo('Manipulação')}',
                                                        textAlign:
                                                            TextAlign.center,
                                                        style: TextStyle(
                                                          fontSize: 18,
                                                          fontWeight:
                                                              FontWeight.bold,
                                                          color: Colors.white,
                                                        ),
                                                      ),
                                                    ],
                                                  ),
                                        ),
                                      ),
                                    ),
                                  ),
                              ],
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  // Adicione esses métodos fora do build
  Widget _buildInfoCard(
    BuildContext context, {
    required String title,
    required String value,
    required IconData icon,
  }) {
    return Card(
      elevation: 4,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Row(
          children: [
            Icon(icon, size: 40, color: Theme.of(context).primaryColor),
            SizedBox(width: 16),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    title,
                    style: TextStyle(fontSize: 16, color: Colors.grey[600]),
                  ),
                  SizedBox(height: 4),
                  Text(
                    value,
                    style: TextStyle(
                      fontSize: 22,
                      fontWeight: FontWeight.bold,
                      color: Theme.of(context).primaryColor,
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
