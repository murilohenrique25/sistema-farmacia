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
        .report-title {
            font-size: 15px; /* Diminui o tamanho da fonte do título */
            margin-top: 20px;
            text-align:center;
        }
        .content {
            margin-top: 30px;
        }
        .data-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            font-size: 10px; /* Diminui o tamanho da fonte na tabela */
        }
        .data-table th, .data-table td {
            border: 1px solid #000;
            padding: 2px; /* Diminui o padding */
            text-align: left;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap; /* Garante que o texto não quebre */
        }
        .data-table th {
            background-color: #f0f0f0;
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
                         <strong>Data: ${dataAtual}</strong>
                        </td>
                    </tr>
        </table>
        <div class="report-title">
            <p><strong>${nomeRelatorio} -  Total de entregas avulsas: ${totalEntregas}</strong></p>
        </div>
    </div>
    <div class="content">
        <table class="data-table">
            <thead>
                <tr>
                    <th>Entregador</th>
                    <th>Data Inicio</th>
                    <th>Data Finalização</th>
                    <th>Descrição Serviço</th>
                    <th>Observação</th>
                </tr>
            </thead>
            <tbody>
                <#list listaDados as dado>
                <tr>
                    <td>${dado.nome}</td>
                    <td>${dado.dataHoraInicioServico}</td>
                    <td>${dado.dataHoraTerminoServico}</td>
                    <td>${dado.descricaoServico}</td>
                    <td>${dado.observacaoServico}</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>
</body>
</html>
