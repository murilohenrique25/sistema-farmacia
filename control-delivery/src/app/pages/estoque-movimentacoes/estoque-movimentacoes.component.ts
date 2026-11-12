import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EstoqueService } from '../../services/estoque.service';
import { Movimentacao, Produto } from '../../interfaces/estoque.interfaces';

@Component({
    selector: 'app-estoque-movimentacoes',
    templateUrl: './estoque-movimentacoes.component.html',
    styleUrl: './estoque-movimentacoes.component.scss'
})
export class EstoqueMovimentacoesComponent implements OnInit {
    displayedColumns = ['dataHora', 'tipo', 'nomeProduto', 'numeroLote', 'quantidade', 'precoCusto', 'idUsuarioResponsavel'];
    dataSource = new MatTableDataSource<Movimentacao>();
    carregando = false;
    produtos: Produto[] = [];

    // Filtros — usar strings ISO para LocalDateTime
    filtroInicio: string = '';
    filtroFim: string = '';
    filtroIdProduto: number | undefined;

    @ViewChild(MatPaginator) paginator!: MatPaginator;
    @ViewChild(MatSort) sort!: MatSort;

    constructor(private estoqueService: EstoqueService, private snackBar: MatSnackBar) { }

    ngOnInit() {
        // Definir período padrão: mês atual
        const agora = new Date();
        const inicio = new Date(agora.getFullYear(), agora.getMonth(), 1);
        this.filtroInicio = inicio.toISOString().slice(0, 16);
        this.filtroFim = agora.toISOString().slice(0, 16);

        this.estoqueService.listarProdutos().subscribe(p => this.produtos = p);
        this.buscar();
    }

    ngAfterViewInit() {
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
    }

    buscar() {
        if (!this.filtroInicio || !this.filtroFim) return;
        this.carregando = true;
        this.estoqueService.listarHistorico(this.filtroInicio, this.filtroFim, this.filtroIdProduto).subscribe({
            next: (res) => {
                this.dataSource.data = res;
                this.dataSource.paginator = this.paginator;
                this.dataSource.sort = this.sort;
                this.carregando = false;
            },
            error: () => { this.carregando = false; }
        });
    }

    exportarCsv() {
        this.estoqueService.exportarHistoricoCsv(this.filtroInicio, this.filtroFim).subscribe({
            next: (blob) => {
                const url = URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url; a.download = 'historico.csv'; a.click();
                URL.revokeObjectURL(url);
            },
            error: () => this.snackBar.open('Erro ao exportar CSV', 'Fechar', { duration: 3000 })
        });
    }

    tipoClass(tipo: string): string {
        return tipo === 'ENTRADA' ? 'badge-pago' : 'badge-vencido';
    }
}
