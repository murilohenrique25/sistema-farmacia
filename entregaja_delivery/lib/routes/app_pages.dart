import 'package:entregaja_delivery/controllers/entregas_selecionadas_controller.dart';
import 'package:entregaja_delivery/controllers/lista_servicos_controller.dart';
import 'package:entregaja_delivery/views/entregas_selecionadas_view.dart';
import 'package:entregaja_delivery/views/lista_servico_view.dart';
import 'package:get/get.dart';
import '../views/login_view.dart';
import '../views/home_view.dart';
import '../views/entregas_view.dart';
import '../views/entregas_detalhes_view.dart';
import '../controllers/home_controller.dart';
import '../controllers/entregas_controller.dart';
import '../controllers/entregas_detalhes_controller.dart';
import '../services/api_service.dart';

class AppPages {
  static const initial = '/login';
  static const home = '/home';
  static const entregas = '/entregas';
  static const entregasDetalhes = '/entregas/detalhes';
  static const entregasSelecionadas = '/entregas-selecionadas'; // 🆕
  static const servicos = '/servicos';

  static final routes = [
    GetPage(
      name: initial,
      page: () => LoginView(),
      transition: Transition.fadeIn,
      binding: BindingsBuilder(() {
        Get.put(ApiService());
      }),
    ),
    GetPage(
      name: entregasSelecionadas,
      page: () => EntregasSelecionadasView(),
      binding: BindingsBuilder(() {
        Get.lazyPut(() => ApiService());
        Get.put(EntregasSelecionadasController());
      }),
      transition: Transition.rightToLeft,
    ),
    GetPage(
      name: home,
      page: () => HomeView(),
      binding: BindingsBuilder(() {
        Get.put(HomeController());
      }),
      transition: Transition.cupertino,
    ),
    GetPage(
      name: entregas,
      page: () => EntregasView(),
      binding: BindingsBuilder(() {
        Get.lazyPut(() => ApiService());
        Get.put(EntregasController());
      }),
      transition: Transition.rightToLeft,
    ),
    GetPage(
      name: entregasDetalhes,
      page: () => EntregasDetalhesView(),
      binding: BindingsBuilder(() {
        Get.lazyPut(() => ApiService());
        Get.put(EntregasDetalhesController());
      }),
      transition: Transition.rightToLeft,
    ),
    GetPage(
      name: servicos,
      page: () => ListaServicosView(),
      binding: BindingsBuilder(() {
        Get.lazyPut(() => ApiService());
        Get.put(ListaServicosController());
      }),
      transition: Transition.rightToLeft,
    ),
  ];
}
