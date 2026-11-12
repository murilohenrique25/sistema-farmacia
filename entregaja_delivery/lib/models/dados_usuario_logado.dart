import 'dados_entregador_app.dart';

class DadosUsuarioLogado {
  final String usuario;
  final DadosEntregadorApp dadosEntregadorApp;
  final String token;

  DadosUsuarioLogado({
    required this.usuario,
    required this.dadosEntregadorApp,
    required this.token,
  });

  factory DadosUsuarioLogado.fromJson(Map<String, dynamic> json) {
    return DadosUsuarioLogado(
      usuario: json['usuario'] ?? '',
      dadosEntregadorApp: DadosEntregadorApp.fromJson(
        json['dadosEntregadorApp'],
      ),
      token: json['token'] ?? '',
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'usuario': usuario,
      'dadosEntregadorApp': dadosEntregadorApp.toJson(),
      'token': token,
    };
  }
}
