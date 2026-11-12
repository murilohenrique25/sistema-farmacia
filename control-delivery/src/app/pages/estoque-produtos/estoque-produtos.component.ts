import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Inject } from '@angular/core';
import { EstoqueService } from '../../services/estoque.service';
import { AuthService } from '../../services/auth.service';
import { Produto, Lote } from '../../interfaces/estoque.interfaces';

// ─── Modal de Criar/Editar Produto ───────────────────────────────────────────

@Component({
    selector: 'app-modal-form-produto',
    template: `
    <h2 mat-dialog-title>{{ data.produto ? 'Editar Produto' : 'Novo Produto' }}</h2>
    <mat-dialog-content>
      <form [formGroup]="form" class="form-grid">
        <mat-form-field appearance="outline">
          <mat-label>Nome</mat-label>
          <input matInput formControlName="nome" maxlength="150" />
          <mat-error *ngIf="form.get('nome')?.hasError('required')">Campo obrigatório</mat-error>
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Código</mat-label>
          <input matInput formControlName="codigo" maxlength="50" />
          <mat-error *ngIf="form.get('codigo')?.hasError('required')">Campo obrigatório</mat-error>
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Categoria</mat-label>
          <input matInput formControlName="categoria" maxlength="100" />
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Unidade de Medida</mat-label>
          <input matInput formControlName="unidadeMedida" maxlength="20" />
          <mat-error *ngIf="form.get('unidadeMedida')?.hasError('required')">Campo obrigatório</mat-error>
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Status</mat-label>
          <mat-select formControlName="status">
            <mat-option value="ATIVO">ATIVO</mat-option>
            <mat-option value="INATIVO">INATIVO</mat-option>
          </mat-select>
        </mat-form-field>
      </form>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button mat-dialog-close>Cancelar</button>
      <button class="botao-primario" (click)="salvar()" [disabled]="form.invalid || salvando">
        {{ salvando ? 'Salvando...' : 'Salvar' }}
      </button>
    </mat-dialog-actions>
  `,
    styles: [`
    .form-grid { display: flex; flex-direction: column; gap: 8px; min-width: 400px; padding-top: 8px; }
    mat-form-field { width: 100%; }
    .botao-primario {
      background: var(--primary-color); color: white; border: none;
      border-radius: 8px; padding: 8px 18px; font-size: 14px; cursor: pointer;
    }
    .botao-primario:disabled { opacity: 0.6; cursor: not-allowed; }
  `]
})
export class ModalFormProdutoComponent {
    form: FormGroup;
    salvando = false;

    constructor(
        private fb: FormBuilder,
        private estoqueService: EstoqueService,
        private dialogRef: MatDialogRef<ModalFormProdutoComponent>,
        @Inject(MAT_DIALOG_DATA) public data: { produto?: Produto; idMatriz: number }
    ) {
        this.form = this.fb.group({
            nome: [data.produto?.nome ?? '', Validators.required],
            codigo: [data.produto?.codigo ?? '', Validators.required],
            categoria: [data.produto?.categoria ?? ''],
            unidadeMedida: [data.produto?.unidadeMedida ?? '', Validators.required],
            status: [data.produto?.status ?? 'ATIVO']
        });
    }

    salvar() {
        if (this.form.invalid) return;
        this.salvando = true;
        const payload: Produto = { ...this.form.value, idMatriz: this.data.idMatriz };

        const op = this.data.produto
            ? this.estoqueService.atualizarProduto(this.data.produto.idProduto!, payload)
            : this.estoqueService.criarProduto(payload);

        op.subscribe({
            next: () => this.dialogRef.close(true),
            error: () => { this.salvando = false; }
        });
    }
}

// ─── EstoqueProdutosComponent ─────────────────────────────────────────────────

@Component({
    selector: 'app-estoque-produtos',
    templateUrl: './estoque-produtos.component.html',
    styleUrl: './estoque-produtos.component.scss'
})
export class EstoqueProdutosComponent implements OnInit {
    displayedColumns = ['nome', 'codigo', 'categoria', 'unidadeMedida', 'status', 'action'];
    dataSource = new MatTableDataSource<Produto>();
    carregando = true;
    produtoSelecionado: Produto | null = null;
    lotes: Lote[] = [];
    carregandoLotes = false;

    lotesColumns = ['numeroLote', 'quantidadeAtual', 'precoCusto', 'dataEntrada', 'dataValidade', 'statusLote'];

    @ViewChild(MatPaginator) paginator!: MatPaginator;
    @ViewChild(MatSort) sort!: MatSort;

    constructor(
        private estoqueService: EstoqueService,
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
        this.estoqueService.listarProdutos().subscribe({
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

    selecionarProduto(produto: Produto) {
        if (this.produtoSelecionado?.idProduto === produto.idProduto) {
            this.produtoSelecionado = null;
            this.lotes = [];
            return;
        }
        this.produtoSelecionado = produto;
        this.carregandoLotes = true;
        this.estoqueService.listarLotesPorProduto(produto.idProduto!).subscribe({
            next: (lotes) => { this.lotes = lotes; this.carregandoLotes = false; },
            error: () => { this.carregandoLotes = false; }
        });
    }

    abrirModal(produto?: Produto) {
        const idMatriz = Number(localStorage.getItem('idMatriz') ?? 0);
        this.dialog.open(ModalFormProdutoComponent, {
            width: '520px',
            data: { produto, idMatriz }
        }).afterClosed().subscribe(salvo => { if (salvo) this.carregar(); });
    }

    excluir(produto: Produto) {
        if (!confirm(`Excluir o produto "${produto.nome}"?`)) return;
        this.estoqueService.excluirProduto(produto.idProduto!).subscribe({
            next: () => {
                this.snackBar.open('Produto excluído!', 'Fechar', { duration: 3000 });
                if (this.produtoSelecionado?.idProduto === produto.idProduto) {
                    this.produtoSelecionado = null;
                    this.lotes = [];
                }
                this.carregar();
            },
            error: (err) => this.snackBar.open(err.error?.message || 'Erro ao excluir', 'Fechar', { duration: 4000 })
        });
    }

    isSuperAdmin(): boolean {
        return this.authService.getRole() === 'Administrador';
    }

    statusClass(status: string): string {
        return status === 'ATIVO' ? 'badge-pago' : 'badge-cancelado';
    }

    loteStatusClass(status: string): string {
        const map: Record<string, string> = {
            ATIVO: 'badge-pago',
            ESGOTADO: 'badge-cancelado',
            VENCIDO: 'badge-vencido',
            PROXIMO_VENCIMENTO: 'badge-pendente'
        };
        return map[status] ?? '';
    }
}
