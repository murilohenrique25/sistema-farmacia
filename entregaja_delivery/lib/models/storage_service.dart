import 'package:entregaja_delivery/models/dados_usuario_logado.dart';
import 'package:get/get.dart';
import 'package:get_storage/get_storage.dart';

class StorageService extends GetxService {
  final _box = GetStorage();
  static const _usuarioKey = 'usuario_logado';

  final Rxn<DadosUsuarioLogado> usuarioLogado = Rxn<DadosUsuarioLogado>();

  // SALVAR (login)
  void saveUsuarioLogado(Map<String, dynamic> json) {
    _box.write(_usuarioKey, json); // 👈 persistência
    usuarioLogado.value = DadosUsuarioLogado.fromJson(json);
  }

  // CARREGAR (startup)
  void loadUsuarioLogado() {
    final json = _box.read(_usuarioKey);

    if (json != null) {
      usuarioLogado.value = DadosUsuarioLogado.fromJson(json);
    }
  }

  // USO ATUAL (inalterado)
  DadosUsuarioLogado? getUsuarioLogado() {
    return usuarioLogado.value;
  }

  // LOGOUT
  void clear() {
    _box.remove(_usuarioKey); // 👈 limpa storage
    usuarioLogado.value = null;
  }

  bool get isLogado => usuarioLogado.value != null;
}
