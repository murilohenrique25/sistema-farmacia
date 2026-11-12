class DadosEntregadorApp {
  final int quantidadeEntrega;
  final String mediaTotal;

  DadosEntregadorApp({
    required this.quantidadeEntrega,
    required this.mediaTotal,
  });

  factory DadosEntregadorApp.fromJson(Map<String, dynamic> json) {
    return DadosEntregadorApp(
      quantidadeEntrega: json['quantidadeEntrega'] ?? 0,
      mediaTotal: json['mediaTotal'] ?? '',
    );
  }

  Map<String, dynamic> toJson() {
    return {'quantidadeEntrega': quantidadeEntrega, 'mediaTotal': mediaTotal};
  }
}
