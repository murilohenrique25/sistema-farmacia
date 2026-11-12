import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ContaService } from '../../services/conta.service';
import { Conta } from '../../interfaces/conta';
import { ModalFormContaComponent } from './modal-form-conta/modal-form-conta.component';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-contas',
    templateUrl: './contas.component.html',
    styleUrl: './contas.component.scss'
})
export class ContasComponent implements OnInit {

    @Input() tipo: string = 'PAGAR'; // PAGAR | RECEBER

    displayedColumns = ['descricao', 'categoria', 'valor', 'dataVencimento', 'status', 'action'];
    dataSource = new MatTableDataSource<Conta>();
    carregando = true;

    @ViewChild(MatPaginator) paginator!: MatPaginator;
    @ViewChild(MatSort) sort!: MatSort;

    get titulo() { return this.tipo === 'PAGAR' ? 'Saídas' : 'Entradas'; }
    get icone() { return this.tipo === 'PAGAR' ? '💸' : '💰'; }

    constructor(
        private contaService: ContaService,
        private dialog: MatDialog,
        private snackBar: MatSnackBar,
        public authService: AuthService
    ) { }

    ngOnInit() { this.carregar(); }

    ngAfterViewInit() {
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
    }

    carregar() {
        this.carregando = true;
        this.contaService.listar(this.tipo).subscribe({
            next: (res) => {
                this.dataSource.data = res;
                this.dataSource.paginator = this.paginator;
                this.dataSource.sort = this.sort;
                this.carregando = false;
            },
            error: () => { this.carregando = false; }
        });
    }

    aplicarFiltro(event: Event) {
        const val = (event.target as HTMLInputElement).value;
        this.dataSource.filter = val.trim().toLowerCase();
        if (this.dataSource.paginator) this.dataSource.paginator.firstPage();
    }

    abrirModal(conta?: Conta) {
        const isMobile = window.innerWidth < 768;
        this.dialog.open(ModalFormContaComponent, {
            width: isMobile ? '95vw' : '600px',
            maxWidth: '95vw',
            maxHeight: '90vh',
            data: { conta, tipo: this.tipo }
        }).afterClosed().subscribe(salvo => { if (salvo) this.carregar(); });
    }

    baixar(conta: Conta) {
        this.contaService.baixar(conta.idConta!).subscribe({
            next: () => { this.snackBar.open('Baixa realizada!', 'Fechar', { duration: 3000 }); this.carregar(); },
            error: () => this.snackBar.open('Erro ao realizar baixa', 'Fechar', { duration: 3000 })
        });
    }

    cancelar(conta: Conta) {
        this.contaService.cancelar(conta.idConta!).subscribe({
            next: () => { this.snackBar.open('Conta cancelada!', 'Fechar', { duration: 3000 }); this.carregar(); },
            error: () => this.snackBar.open('Erro ao cancelar', 'Fechar', { duration: 3000 })
        });
    }

    statusClass(status: string): string {
        const map: Record<string, string> = {
            PENDENTE: 'badge-pendente',
            PAGO: 'badge-pago',
            VENCIDO: 'badge-vencido',
            CANCELADO: 'badge-cancelado'
        };
        return map[status] ?? '';
    }

    isAdmin(): boolean {
        return this.authService.getRole() === 'Administrador';
    }
}
