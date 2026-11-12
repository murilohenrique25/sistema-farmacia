import 'package:entregaja_delivery/models/storage_service.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'routes/app_pages.dart';
import 'controllers/auth_controller.dart';
import 'package:get_storage/get_storage.dart';

import 'services/location_service.dart';
import 'services/websocket_service.dart';
import 'controllers/entrega_ativa_controller.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await GetStorage.init();

  final storage = Get.put(StorageService(), permanent: true);
  storage.loadUsuarioLogado();

  Get.put(LocationService(), permanent: true);
  Get.put(WebSocketService(), permanent: true);
  Get.put(EntregaAtivaController(), permanent: true);

  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return GetMaterialApp(
      title: 'EntregaJá Delivery',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        primarySwatch: Colors.blue,
        brightness: Brightness.light,
        visualDensity: VisualDensity.adaptivePlatformDensity,
        fontFamily: 'Poppins',
      ),
      darkTheme: ThemeData(
        brightness: Brightness.dark,
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
        fontFamily: 'Poppins',
      ),
      themeMode: ThemeMode.system,
      initialBinding: BindingsBuilder(() {
        Get.put(AuthController());
      }),
      // 🔧 Verificar login AQUI
      initialRoute: _getInitialRoute(),
      getPages: AppPages.routes,
    );
  }

  // 🔧 Determinar rota inicial baseado no login
  String _getInitialRoute() {
    final storage = Get.find<StorageService>();

    if (storage.isLogado) {
      debugPrint('✅ Usuário já logado, indo para home');
      return AppPages.home;
    }

    debugPrint('ℹ️ Usuário não logado, indo para login');
    return AppPages.initial;
  }
}
