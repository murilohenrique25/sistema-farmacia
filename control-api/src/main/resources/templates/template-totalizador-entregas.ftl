<!DOCTYPE html>
<html>
<body>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        .header {
            text-align: left;
            border-bottom: 1px solid #000;
            margin-bottom: 20px;
            padding-bottom: 10px;
        }
        .logo {
            height: 80px;
        }
        .company-info {
          font-size: 14px; /* Diminui o tamanho da fonte */
          margin-top: 10px;
        }

        .entregador-info p {
            font-size:12px;
        }

        .report-title {
            font-size: 15px; /* Diminui o tamanho da fonte do título */
            margin-top: 20px;
            text-align:center;
        }

        .content {
                padding: 20px;
          }

            .entregador-section {
                display: flex;
                flex-direction: column;
                gap: 15px;
            }

            .entregador-card {
                background-color: #f8f8f8;
                border: 1px solid #ccc;
                padding: 15px;
                border-radius: 8px;
            }

            .entregador-info {
                display: flex;
                flex-direction: column;
                gap: 8px;
            }

            .valor-troco {
                display: flex;
                justify-content: space-between;
                gap: 10px;
            }
        }
    </style>

    <div class="header">
        <table style="width: 100%; border-collapse: collapse;">
            <tr>
                <td style="width: 80px; vertical-align: middle;">
                    <img src="${logo}" alt="Logo da Empresa" style="height: 50px; width: auto;" />
                </td>
                <td style="vertical-align: middle;">
                    <p style="margin: 0;"><strong>${nomeEmpresa}</strong></p>
                    <p style="margin: 0;">${endereco}, Telefone: ${telefone}</p>
                </td>
                <td>
                   Data: ${dataAtual}
                </td>
            </tr>
        </table>
        <div class="report-title">
            <p><strong>${nomeRelatorio} -  Entregas do pérido de  ${intervaloDataInicio} à ${intervaloDataFim}</strong></p>
        </div>
    </div>
    <div class="content">
        <div class="entregador-section">
            <#list listaDados as dado>
            <div class="entregador-card">
                <div class="entregador-info">
                    <p><strong>Entregador:</strong> ${dado.nome}</p>
                    <p><strong>Quantidade de Entregas:</strong> ${dado.quantidadeEntrega}</p>
                    <p><strong>Tempo Total:</strong> ${dado.tempoTotal}</p>
                    <p><strong>Média de Tempo:</strong> ${dado.mediaTotal}</p>
                    <div class="valor-troco">
                        <p><strong>Valor Total das Entregas:</strong> ${dado.valorTotalEntrega} - <strong>Total de Troco:</strong> ${dado.valorTotalTroco}</p>
                    </div>
                </div>
            </div>
            </#list>
        </div>
    </div>
</body>
</html>
