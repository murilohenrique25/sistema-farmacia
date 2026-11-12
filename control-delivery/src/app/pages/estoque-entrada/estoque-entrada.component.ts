import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EstoqueService } from '../../services/estoque.service';
import { EntradaItem, Lote, Produto } from '../../interfaces/estoque.interfaces';

@Component({
    selector: 'app-estoque-entrada',
    templateUrl: './estoque-entrada.component.html',
    styleUrl: './estoque-entrada.component.scss'
})
export class EstoqueEntradaComponent implements OnInit {
    produtos: Produto[] = [];
    itens: EntradaItem[] = [];
    previewItens: EntradaItem[] = [];
    arquivoNfe: File | null = null;
    carregando = false;
    carregandoPreview = false;

    // Saída
    saidaIdProduto: number = 0;
    saidaLotes: Lote[] = [];
    saidaIdLote: number = 0;
    saidaQuantidade: number = 0;
    saidaMotivo: string = '';
    carregandoLotes = false;

    previewColumns = ['idProduto', 'numeroLote', 'quantidade', 'precoCusto', 'dataEntrada', 'dataValidade'];

    constructor(private estoqueService: EstoqueService, private snackBar: MatSnackBar) { }

    ngOnInit() {
        this.estoqueService.listarProdutos().subscribe(p => this.produtos = p);
        this.adicionarItem();
    }

    adicionarItem() {
        this.itens.push({ idProduto: 0, numeroLote: '', quantidade: 0, precoCusto: 0, dataEntrada: '', dataValidade: '' });
    }

    removerItem(index: number) {
        this.itens.splice(index, 1);
    }

    registrarEntrada() {
        this.carregando = true;
        this.estoqueService.registrarEntrada({ itens: this.itens }).subscribe({
            next: (res) => {
                this.snackBar.open(`${res.quantidadeItensProcessados} item(s) registrado(s)!`, 'Fechar', { duration: 4000 });
                this.itens = [];
                this.adicionarItem();
                this.carregando = false;
            },
            error: (err) => {
                this.snackBar.open(err.error?.message || 'Erro ao registrar entrada', 'Fechar', { duration: 4000 });
                this.carregando = false;
            }
        });
    }

    onProdutoSaidaChange() {
        if (!this.saidaIdProduto) return;
        this.carregandoLotes = true;
        this.saidaLotes = [];
        this.saidaIdLote = 0;
        this.estoqueService.listarLotesPorProduto(this.saidaIdProduto).subscribe({
            next: (lotes) => { this.saidaLotes = lotes.filter(l => l.status !== 'ESGOTADO'); this.carregandoLotes = false; },
            error: () => { this.carregandoLotes = false; }
        });
    }

    registrarSaida() {
        if (!this.saidaIdLote || this.saidaQuantidade <= 0) {
            this.snackBar.open('Selecione o lote e informe a quantidade', 'Fechar', { duration: 3000 });
            return;
        }
        this.carregando = true;
        this.estoqueService.registrarSaida({ idLote: this.saidaIdLote, quantidade: this.saidaQuantidade, motivo: this.saidaMotivo }).subscribe({
            next: (res) => {
                this.snackBar.open(`Saída registrada! Saldo atual: ${res.quantidadeAtual}`, 'Fechar', { duration: 4000 });
                this.saidaIdProduto = 0;
                this.saidaLotes = [];
                this.saidaIdLote = 0;
                this.saidaQuantidade = 0;
                this.saidaMotivo = '';
                this.carregando = false;
            },
            error: (err) => {
                this.snackBar.open(err.error?.message || 'Erro ao registrar saída', 'Fechar', { duration: 4000 });
                this.carregando = false;
            }
        });
    }

    onArquivoSelecionado(event: Event) {
        const input = event.target as HTMLInputElement;
        this.arquivoNfe = input.files?.[0] ?? null;
        this.previewItens = [];
    }

    previewNfe() {
        if (!this.arquivoNfe) return;
        this.carregandoPreview = true;
        this.estoqueService.previewNfe(this.arquivoNfe).subscribe({
            next: (itens) => { this.previewItens = itens; this.carregandoPreview = false; },
            error: (err) => {
                this.snackBar.open(err.error?.message || 'Erro ao processar NF-e', 'Fechar', { duration: 4000 });
                this.carregandoPreview = false;
            }
        });
    }

    confirmarImportacaoNfe() {
        if (!this.previewItens.length) return;
        this.carregando = true;
        this.estoqueService.importarNfe({ itens: this.previewItens }).subscribe({
            next: (res) => {
                this.snackBar.open(`${res.quantidadeItensProcessados} item(s) importado(s)!`, 'Fechar', { duration: 4000 });
                this.previewItens = [];
                this.arquivoNfe = null;
                this.carregando = false;
            },
            error: (err) => {
                this.snackBar.open(err.error?.message || 'Erro ao importar NF-e', 'Fechar', { duration: 4000 });
                this.carregando = false;
            }
        });
    }

    nomeProduto(idProduto: number): string {
        return this.produtos.find(p => p.idProduto === idProduto)?.nome ?? String(idProduto);
    }

    loteLabel(lote: Lote): string {
        return `${lote.numeroLote} — Saldo: ${lote.quantidadeAtual} | Validade: ${lote.dataValidade ?? 'N/A'}`;
    }
}
