import 'package:flutter/material.dart';
import '../models/entrega_model.dart';

class EntregaItemWidget extends StatelessWidget {
  final EntregaModel entrega;
  final VoidCallback? onTap;
  final bool readOnly;

  const EntregaItemWidget({
    Key? key,
    required this.entrega,
    required this.onTap,
    this.readOnly = false,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.only(bottom: 12),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(16),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.1),
            blurRadius: 8,
            offset: Offset(0, 2),
          ),
        ],
      ),
      child: Material(
        color: Colors.transparent,
        child: InkWell(
          onTap: onTap,
          borderRadius: BorderRadius.circular(16),
          splashColor: Colors.blue.withOpacity(0.1),
          highlightColor: Colors.blue.withOpacity(0.05),
          child: Padding(
            padding: const EdgeInsets.all(16.0),
            child: Row(
              children: [
                // ID e círculo de cor
                Container(
                  width: 80,
                  height: 80,
                  decoration: BoxDecoration(
                    shape: BoxShape.circle,
                    color: Color(0xFF1488CC).withOpacity(0.2),
                  ),
                  child: Center(
                    child: Text(
                      '${entrega.idEntrega}',
                      style: TextStyle(
                        fontSize: 16,
                        fontWeight: FontWeight.bold,
                        color: Color(0xFF1488CC),
                      ),
                    ),
                  ),
                ),
                SizedBox(width: 16),

                // Informações da entrega
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        'Cupom: ${entrega.cupomOrcamento}',
                        style: TextStyle(
                          fontSize: 16,
                          fontWeight: FontWeight.bold,
                          color: Colors.black87,
                        ),
                      ),
                      SizedBox(height: 4),
                      Row(
                        children: [
                          Expanded(
                            child: Text.rich(
                              TextSpan(
                                children: [
                                  TextSpan(
                                    text: 'Total: ',
                                    style: TextStyle(
                                      fontWeight:
                                          FontWeight
                                              .bold, // Negrito para "Troco"
                                      fontSize: 14,
                                      color: Colors.black54,
                                    ),
                                  ),
                                  TextSpan(
                                    text: entrega.valorTotal.toStringAsFixed(2),
                                    style: TextStyle(
                                      fontWeight: FontWeight.normal,
                                      fontSize: 14,
                                      color: Colors.black54,
                                    ),
                                  ),
                                ],
                              ),
                              overflow: TextOverflow.ellipsis,
                            ),
                          ),
                          SizedBox(width: 4),
                          Expanded(
                            child: Text.rich(
                              TextSpan(
                                children: [
                                  TextSpan(
                                    text: 'Troco: ',
                                    style: TextStyle(
                                      fontWeight:
                                          FontWeight
                                              .bold, // Negrito para "Troco"
                                      fontSize: 14,
                                      color: Colors.black54,
                                    ),
                                  ),
                                  TextSpan(
                                    text: entrega.troco.toStringAsFixed(2),
                                    style: TextStyle(
                                      fontWeight: FontWeight.normal,
                                      fontSize: 14,
                                      color: Colors.black54,
                                    ),
                                  ),
                                ],
                              ),
                              overflow: TextOverflow.ellipsis,
                            ),
                          ),
                        ],
                      ),
                      SizedBox(height: 4),
                      Text.rich(
                        TextSpan(
                          children: [
                            TextSpan(
                              text: 'Forma Pagamento: ',
                              style: TextStyle(
                                fontWeight:
                                    FontWeight.bold, // Negrito para "Troco"
                                fontSize: 14,
                                color: Colors.black54,
                              ),
                            ),
                            TextSpan(
                              text: entrega.formaPagamento,
                              style: TextStyle(
                                fontWeight: FontWeight.normal,
                                fontSize: 14,
                                color: Colors.black54,
                              ),
                            ),
                          ],
                        ),
                      ),
                      SizedBox(height: 4),
                      entrega.observacao != null
                          ? Text.rich(
                            TextSpan(
                              children: [
                                TextSpan(
                                  text: 'Observação: ',
                                  style: TextStyle(
                                    fontWeight:
                                        FontWeight.bold, // Negrito para "Troco"
                                    fontSize: 14,
                                    color: Colors.red,
                                  ),
                                ),
                                TextSpan(
                                  text: entrega.observacao,
                                  style: TextStyle(
                                    fontWeight: FontWeight.normal,
                                    fontSize: 14,
                                    color: Colors.black54,
                                  ),
                                ),
                              ],
                            ),
                          )
                          : Text(''),
                    ],
                  ),
                ),

                // Ícone de seleção
                if (!readOnly)
                  Container(
                    width: 36,
                    height: 36,
                    decoration: BoxDecoration(
                      shape: BoxShape.circle,
                      color:
                          entrega.selecionada
                              ? Color(0xFF4CAF50)
                              : Colors.grey.shade200,
                      border: Border.all(
                        color:
                            entrega.selecionada
                                ? Color(0xFF4CAF50)
                                : Colors.grey.shade400,
                        width: 2,
                      ),
                    ),
                    child:
                        entrega.selecionada
                            ? Icon(Icons.check, color: Colors.white, size: 20)
                            : null,
                  ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
