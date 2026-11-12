import { Component, OnInit } from '@angular/core';
import { ContaService } from '../../services/conta.service';
import { Conta } from '../../interfaces/conta';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { EmpresaService } from '../../services/empresa.service';
import { Matriz } from '../../interfaces/matriz';
import jsPDF from 'jspdf';

interface LinhaCaixa {
    data: string;
    descricao: string;
    tipo: string;
    valor: number;
    status: string;
    saldoAcumulado: number;
    observacao?: string;
    formaPagamento?: string;
}

@Component({
    selector: 'app-fluxo-caixa',
    templateUrl: './fluxo-caixa.component.html',
    styleUrl: './fluxo-caixa.component.scss'
})
export class FluxoCaixaComponent implements OnInit {

    inicio: string = '';
    fim: string = '';
    linhas: LinhaCaixa[] = [];
    carregando = false;

    totalEntradas = 0;
    totalSaidas = 0;
    totaisPorFormaPagamento: { forma: string; total: number }[] = [];
    entradasPorForma: { forma: string; total: number }[] = [];
    saidasPorForma: { forma: string; total: number }[] = [];

    inicioDate: Date | null = null;
    fimDate: Date | null = null;

    get saldo(): number {
        return this.totalEntradas - this.totalSaidas;
    }

    constructor(private contaService: ContaService, private empresaService: EmpresaService) { }

    ngOnInit() {
        const hoje = new Date();
        const primeiroDia = new Date(hoje.getFullYear(), hoje.getMonth(), 1);
        this.inicioDate = primeiroDia;
        this.fimDate = hoje;
        this.inicio = this.toDateStr(primeiroDia);
        this.fim = this.toDateStr(hoje);
        this.carregar();
    }

    carregar() {
        if (!this.inicioDate || !this.fimDate) return;
        this.inicio = this.toDateStr(this.inicioDate);
        this.fim = this.toDateStr(this.fimDate);
        this.carregando = true;
        this.linhas = [];
        this.totalEntradas = 0;
        this.totalSaidas = 0;
        this.totaisPorFormaPagamento = [];
        this.entradasPorForma = [];
        this.saidasPorForma = [];

        forkJoin({
            receber: this.contaService.listarPorPeriodo('RECEBER', this.inicio, this.fim).pipe(catchError(() => of([]))),
            pagar: this.contaService.listarPorPeriodo('PAGAR', this.inicio, this.fim).pipe(catchError(() => of([])))
        }).subscribe({
            next: ({ receber, pagar }) => {
                const todas: Conta[] = [...receber, ...pagar];
                todas.sort((a, b) => {
                    const toStr = (d: any) => Array.isArray(d)
                        ? `${d[0]}-${String(d[1]).padStart(2, '0')}-${String(d[2]).padStart(2, '0')}`
                        : String(d);
                    return toStr(a.dataVencimento).localeCompare(toStr(b.dataVencimento));
                });

                let saldoAcum = 0;
                this.linhas = todas.map(c => {
                    const isEntrada = c.tipo === 'RECEBER';
                    const valor = Number(c.valor);
                    if (isEntrada) {
                        this.totalEntradas += valor;
                        saldoAcum += valor;
                    } else {
                        this.totalSaidas += valor;
                        saldoAcum -= valor;
                    }
                    return {
                        data: Array.isArray(c.dataVencimento)
                            ? `${c.dataVencimento[0]}-${String(c.dataVencimento[1]).padStart(2, '0')}-${String(c.dataVencimento[2]).padStart(2, '0')}`
                            : c.dataVencimento,
                        descricao: c.descricao,
                        tipo: c.tipo,
                        valor,
                        status: c.status,
                        saldoAcumulado: saldoAcum,
                        observacao: c.observacao,
                        formaPagamento: (c as any).formaPagamento
                    };
                });

                // Agrupar por forma de pagamento (apenas itens com status PAGO)
                const pagos = todas.filter(c => c.status === 'PAGO');
                const agrupado: Record<string, number> = {};
                const agrupadoEntradas: Record<string, number> = {};
                const agrupadoSaidas: Record<string, number> = {};
                pagos.forEach(c => {
                    const forma = (c as any).formaPagamento || 'Não informado';
                    agrupado[forma] = (agrupado[forma] || 0) + Number(c.valor);
                    if (c.tipo === 'RECEBER') {
                        agrupadoEntradas[forma] = (agrupadoEntradas[forma] || 0) + Number(c.valor);
                    } else {
                        agrupadoSaidas[forma] = (agrupadoSaidas[forma] || 0) + Number(c.valor);
                    }
                });
                this.totaisPorFormaPagamento = Object.entries(agrupado).map(([forma, total]) => ({ forma, total }));
                this.entradasPorForma = Object.entries(agrupadoEntradas).map(([forma, total]) => ({ forma, total }));
                this.saidasPorForma = Object.entries(agrupadoSaidas).map(([forma, total]) => ({ forma, total }));

                this.carregando = false;
            },
            error: () => { this.carregando = false; }
        });
    }

    private toDateStr(d: Date): string {
        const year = d.getFullYear();
        const month = String(d.getMonth() + 1).padStart(2, '0');
        const day = String(d.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    }

    exportarPDF() {
        this.empresaService.buscarMatrizes().subscribe({
            next: (matrizes) => this.gerarPDF(matrizes[0] ?? null),
            error: () => this.gerarPDF(null)
        });
    }

    private gerarPDF(matriz: Matriz | null) {
        const doc = new jsPDF({ orientation: 'landscape', unit: 'mm', format: 'a4' });
        const pw = doc.internal.pageSize.getWidth();
        let y = 15;

        // Cabeçalho empresa
        doc.setFillColor(108, 43, 217);
        doc.rect(0, 0, pw, 28, 'F');
        doc.setTextColor(255, 255, 255);
        doc.setFontSize(14);
        doc.setFont('helvetica', 'bold');
        doc.text(String(matriz?.nome ?? 'Empresa'), 14, 11);
        doc.setFontSize(9);
        doc.setFont('helvetica', 'normal');
        const info = [
            matriz?.cnpj ? `CNPJ: ${matriz.cnpj}` : '',
            matriz?.logradouro ?? '',
            matriz?.telefone ? `Tel: ${matriz.telefone}` : '',
            matriz?.email ?? ''
        ].filter(Boolean).join('   |   ');
        doc.text(info, 14, 18);

        // Data de geração
        const agora = new Date();
        const dataGeracao = agora.toLocaleDateString('pt-BR') + ' ' + agora.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
        doc.text(`Gerado em: ${dataGeracao}`, pw - 14, 18, { align: 'right' });

        y = 36;

        // Título e período
        doc.setTextColor(40, 40, 40);
        doc.setFontSize(13);
        doc.setFont('helvetica', 'bold');
        doc.text('Fluxo de Caixa', 14, y);
        doc.setFontSize(9);
        doc.setFont('helvetica', 'normal');
        doc.setTextColor(100, 100, 100);
        const periodo = `Período: ${this.fmtDisplay(this.inicio)} a ${this.fmtDisplay(this.fim)}`;
        doc.text(periodo, 14, y + 6);
        y += 14;

        // KPIs
        const kpis = [
            { label: 'Total Entradas', valor: this.totalEntradas, cor: [22, 163, 74] as [number, number, number] },
            { label: 'Total Saídas', valor: this.totalSaidas, cor: [220, 38, 38] as [number, number, number] },
            { label: 'Saldo do Período', valor: this.saldo, cor: this.saldo >= 0 ? [108, 43, 217] as [number, number, number] : [220, 38, 38] as [number, number, number] }
        ];
        const kpiW = 60;
        kpis.forEach((k, i) => {
            const x = 14 + i * (kpiW + 6);
            doc.setFillColor(248, 248, 248);
            doc.roundedRect(x, y, kpiW, 16, 2, 2, 'F');
            doc.setFontSize(8);
            doc.setTextColor(120, 120, 120);
            doc.setFont('helvetica', 'normal');
            doc.text(k.label, x + 4, y + 6);
            doc.setFontSize(11);
            doc.setFont('helvetica', 'bold');
            doc.setTextColor(...k.cor);
            doc.text(this.fmtMoeda(k.valor), x + 4, y + 13);
        });
        y += 24;

        // Cabeçalho tabela
        const cols = ['Data', 'Descrição', 'Tipo', 'Status', 'Valor', 'Saldo Acum.', 'Forma Pgto', 'Observação'];
        const colW = [22, 55, 20, 18, 30, 30, 28, 50];
        doc.setFillColor(108, 43, 217);
        doc.rect(14, y, pw - 28, 8, 'F');
        doc.setTextColor(255, 255, 255);
        doc.setFontSize(7);
        doc.setFont('helvetica', 'bold');
        let cx = 14;
        cols.forEach((c, i) => { doc.text(c, cx + 2, y + 5.5); cx += colW[i]; });
        y += 8;

        // Linhas
        doc.setFont('helvetica', 'normal');
        this.linhas.forEach((l, idx) => {
            if (y > 185) {
                doc.addPage();
                y = 15;
            }
            const bg = idx % 2 === 0 ? [255, 255, 255] : [248, 246, 255];
            doc.setFillColor(...(bg as [number, number, number]));
            doc.rect(14, y, pw - 28, 7, 'F');

            doc.setTextColor(60, 60, 60);
            doc.setFontSize(7);
            cx = 14;
            const obs = l.observacao || '—';
            const forma = l.formaPagamento || '—';
            const cells = [
                this.fmtDisplay(l.data),
                l.descricao.length > 28 ? l.descricao.substring(0, 28) + '…' : l.descricao,
                l.tipo === 'RECEBER' ? 'Entrada' : 'Saída',
                l.status,
                (l.tipo === 'RECEBER' ? '+' : '-') + ' ' + this.fmtMoeda(l.valor),
                this.fmtMoeda(l.saldoAcumulado),
                forma.length > 14 ? forma.substring(0, 14) + '…' : forma,
                obs.length > 25 ? obs.substring(0, 25) + '…' : obs
            ];
            cells.forEach((cell, i) => {
                if (i === 2) doc.setTextColor(l.tipo === 'RECEBER' ? 22 : 220, l.tipo === 'RECEBER' ? 163 : 38, l.tipo === 'RECEBER' ? 74 : 38);
                else if (i === 5) doc.setTextColor(l.saldoAcumulado >= 0 ? 108 : 220, l.saldoAcumulado >= 0 ? 43 : 38, l.saldoAcumulado >= 0 ? 217 : 38);
                else doc.setTextColor(60, 60, 60);
                doc.text(cell, cx + 2, y + 5);
                cx += colW[i];
            });
            y += 7;
        });

        // Totais por forma de pagamento
        if (this.entradasPorForma.length > 0 || this.saidasPorForma.length > 0) {
            y += 6;
            if (y > 170) { doc.addPage(); y = 15; }

            doc.setFontSize(10);
            doc.setFont('helvetica', 'bold');
            doc.setTextColor(40, 40, 40);
            doc.text('Totais por Forma de Pagamento (baixados)', 14, y);
            y += 7;

            if (this.entradasPorForma.length > 0) {
                doc.setFontSize(8);
                doc.setFont('helvetica', 'bold');
                doc.setTextColor(22, 163, 74);
                doc.text('Entradas:', 14, y);
                y += 5;
                doc.setFont('helvetica', 'normal');
                this.entradasPorForma.forEach(item => {
                    doc.setTextColor(80, 80, 80);
                    doc.text(`${item.forma}: `, 18, y);
                    doc.setTextColor(22, 163, 74);
                    doc.text(this.fmtMoeda(item.total), 60, y);
                    y += 5;
                });
                y += 3;
            }

            if (this.saidasPorForma.length > 0) {
                doc.setFontSize(8);
                doc.setFont('helvetica', 'bold');
                doc.setTextColor(220, 38, 38);
                doc.text('Saídas:', 14, y);
                y += 5;
                doc.setFont('helvetica', 'normal');
                this.saidasPorForma.forEach(item => {
                    doc.setTextColor(80, 80, 80);
                    doc.text(`${item.forma}: `, 18, y);
                    doc.setTextColor(220, 38, 38);
                    doc.text(this.fmtMoeda(item.total), 60, y);
                    y += 5;
                });
            }
        }

        doc.save(`fluxo-caixa-${this.inicio}-${this.fim}.pdf`);
    }

    private fmtDisplay(data: string): string {
        if (!data) return '';
        const [y, m, d] = data.split('-');
        return `${d}/${m}/${y}`;
    }

    private fmtMoeda(valor: number): string {
        return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
    }
}
