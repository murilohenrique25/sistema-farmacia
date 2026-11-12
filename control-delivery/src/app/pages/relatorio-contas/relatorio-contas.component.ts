import { Component } from '@angular/core';
import { ContaService } from '../../services/conta.service';
import { Conta } from '../../interfaces/conta';
import { MatSnackBar } from '@angular/material/snack-bar';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Component({
    selector: 'app-relatorio-contas',
    templateUrl: './relatorio-contas.component.html',
    styleUrl: './relatorio-contas.component.scss'
})
export class RelatorioContasComponent {

    tipo = 'PAGAR';
    inicio: Date | null = null;
    fim: Date | null = null;
    contas: Conta[] = [];
    carregando = false;
    buscou = false;

    get totalPendente() { return this.contas.filter(c => c.status === 'PENDENTE' || c.status === 'VENCIDO').reduce((s, c) => s + Number(c.valor), 0); }
    get totalPago() { return this.contas.filter(c => c.status === 'PAGO').reduce((s, c) => s + Number(c.valor), 0); }
    get totalGeral() {
        if (this.tipo === 'AMBOS') {
            return this.totalReceber - this.totalPagar;
        }
        return this.contas.reduce((s, c) => s + Number(c.valor), 0);
    }
    get totalReceber() { return this.contas.filter(c => c.tipo === 'RECEBER').reduce((s, c) => s + Number(c.valor), 0); }
    get totalPagar() { return this.contas.filter(c => c.tipo === 'PAGAR').reduce((s, c) => s + Number(c.valor), 0); }

    constructor(private contaService: ContaService, private snackBar: MatSnackBar) { }

    buscar() {
        if (!this.inicio || !this.fim) {
            this.snackBar.open('Selecione o período', 'Fechar', { duration: 3000 });
            return;
        }
        this.carregando = true;
        this.buscou = true;
        const ini = this.fmt(this.inicio);
        const fim = this.fmt(this.fim);

        if (this.tipo === 'AMBOS') {
            forkJoin({
                pagar: this.contaService.listarPorPeriodo('PAGAR', ini, fim).pipe(catchError(() => of([]))),
                receber: this.contaService.listarPorPeriodo('RECEBER', ini, fim).pipe(catchError(() => of([])))
            }).subscribe({
                next: ({ pagar, receber }) => {
                    this.contas = [...pagar, ...receber].sort((a, b) => {
                        const toStr = (d: any) => Array.isArray(d)
                            ? `${d[0]}-${String(d[1]).padStart(2, '0')}-${String(d[2]).padStart(2, '0')}`
                            : String(d);
                        return toStr(a.dataVencimento).localeCompare(toStr(b.dataVencimento));
                    });
                    this.carregando = false;
                },
                error: () => { this.snackBar.open('Erro ao buscar dados', 'Fechar', { duration: 3000 }); this.carregando = false; }
            });
        } else {
            this.contaService.listarPorPeriodo(this.tipo, ini, fim).subscribe({
                next: (res) => { this.contas = res; this.carregando = false; },
                error: () => { this.snackBar.open('Erro ao buscar dados', 'Fechar', { duration: 3000 }); this.carregando = false; }
            });
        }
    }

    statusClass(status: string): string {
        const map: Record<string, string> = { PENDENTE: 'badge-pendente', PAGO: 'badge-pago', VENCIDO: 'badge-vencido', CANCELADO: 'badge-cancelado' };
        return map[status] ?? '';
    }

    tipoLabel(tipo: string): string {
        return tipo === 'PAGAR' ? 'A Pagar' : 'A Receber';
    }

    private fmt(d: Date): string {
        return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
    }
}
