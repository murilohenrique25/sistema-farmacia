import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EstoqueService } from '../../services/estoque.service';
import { PosicaoEstoque, AlertaVencimento } from '../../interfaces/estoque.interfaces';

@Component({
    selector: 'app-estoque-relatorio',
    templateUrl: './estoque-relatorio.component.html',
    styleUrl: './estoque-relatorio.component.scss'
})
export class EstoqueRelatorioComponent implements OnInit, AfterViewInit {
    posicaoColumns = ['nomeProduto', 'codigo', 'unidadeMedida', 'quantidadeTotalAtiva', 'numeroDeLotesAtivos', 'dataValidadeMaisProxima'];
    alertasColumns = ['nomeProduto', 'numeroLote', 'quantidadeAtual', 'dataValidade', 'statusAlerta'];

    posicaoDataSource = new MatTableDataSource<PosicaoEstoque>();
    alertasDataSource = new MatTableDataSource<AlertaVencimento>();
    carregando = true;

    @ViewChild('posicaoPaginator') posicaoPaginator!: MatPaginator;
    @ViewChild('alertasPaginator') alertasPaginator!: MatPaginator;

    constructor(private estoqueService: EstoqueService, private snackBar: MatSnackBar) { }

    ngOnInit() { this.carregar(); }

    ngAfterViewInit() {
        this.posicaoDataSource.paginator = this.posicaoPaginator;
        this.alertasDataSource.paginator = this.alertasPaginator;
    }

    carregar() {
        this.carregando = true;
        this.estoqueService.posicaoEstoque().subscribe({
            next: (res) => {
                this.posicaoDataSource.data = res;
                this.posicaoDataSource.paginator = this.posicaoPaginator;
                this.carregando = false;
            },
            error: () => { this.carregando = false; }
        });
        this.estoqueService.alertasVencimento(30).subscribe({
            next: (res) => {
                this.alertasDataSource.data = res;
                this.alertasDataSource.paginator = this.alertasPaginator;
            }
        });
    }

    exportarPosicaoCsv() {
        this.estoqueService.exportarPosicaoCsv().subscribe({
            next: (blob) => {
                const url = URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url; a.download = 'posicao.csv'; a.click();
                URL.revokeObjectURL(url);
            },
            error: () => this.snackBar.open('Erro ao exportar CSV', 'Fechar', { duration: 3000 })
        });
    }

    alertaClass(status: string): string {
        const map: Record<string, string> = {
            VENCIDO: 'badge-vencido',
            PROXIMO_VENCIMENTO: 'badge-pendente'
        };
        return map[status] ?? 'badge-pago';
    }
}
