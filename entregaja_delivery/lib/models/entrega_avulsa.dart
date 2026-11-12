class EntregaAvulsa {
  String dataHoraInicioServico;
  String dataHoraTerminoServico;
  String descricaoServico;
  String observacaoServico;
  String userId;

  // Construtor com parâmetros obrigatórios (não anuláveis)
  EntregaAvulsa({
    required this.dataHoraInicioServico,
    required this.dataHoraTerminoServico,
    required this.descricaoServico,
    required this.observacaoServico,
    required this.userId
  });

  Map<String, dynamic> toJson() {
    return {
      'dataHoraInicioServico': dataHoraInicioServico,
      'dataHoraTerminoServico': dataHoraTerminoServico,
      'descricaoServico': descricaoServico,
      'observacaoServico': observacaoServico,
      'userId':userId
    };
  }

}