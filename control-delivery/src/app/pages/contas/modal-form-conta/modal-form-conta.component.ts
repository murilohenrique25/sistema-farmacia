import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ContaService } from '../../../services/conta.service';
import { EmpresaService } from '../../../services/empresa.service';
import { AuthService } from '../../../services/auth.service';
import { Categoria, CategoriaService } from '../../../services/categoria.service';

@Component({
    selector: 'app-modal-form-conta',
    templateUrl: './modal-form-conta.component.html',
    styleUrl: './modal-form-conta.component.scss'
})
export class ModalFormContaComponent implements OnInit {

    form!: FormGroup;
    editando = false;
    idMatriz: number = 0;

    categoriasList: Categoria[] = [];
    formasPagamento = ['Dinheiro', 'PIX', 'Cartão Débito', 'Cartão Crédito', 'Cheque', 'Outros'];

    get categorias(): string[] {
        return this.categoriasList.map(c => c.descricao);
    }

    constructor(
        public dialogRef: MatDialogRef<ModalFormContaComponent>,
        private fb: FormBuilder,
        private contaService: ContaService,
        private empresaService: EmpresaService,
        private authService: AuthService,
        private categoriaService: CategoriaService,
        private snackBar: MatSnackBar,
        @Inject(MAT_DIALOG_DATA) public data: { conta: any; tipo: string }
    ) { }

    ngOnInit() {
        this.editando = !!this.data.conta;
        const userId = this.authService.getUserId();

        // Busca categorias da API
        this.categoriaService.listarPorTipo(this.data.tipo).subscribe({
            next: (cats) => this.categoriasList = cats
        });

        // Busca idMatriz do usuário logado
        if (userId) {
            this.empresaService.buscarMatrizes().subscribe({
                next: (matrizes) => { if (matrizes.length > 0) this.idMatriz = Number(matrizes[0].idMatriz); }
            });
        }

        this.form = this.fb.group({
            descricao: [this.data.conta?.descricao ?? '', Validators.required],
            valor: [this.data.conta?.valor ?? null, [Validators.required, Validators.min(0.01)]],
            dataVencimento: [this.data.conta?.dataVencimento ?? null, Validators.required],
            categoria: [this.data.conta?.categoria ?? ''],
            formaPagamento: [this.data.conta?.formaPagamento ?? ''],
            observacao: [this.data.conta?.observacao ?? ''],
            status: [this.data.conta?.status ?? 'PENDENTE']
        });
    }

    salvar() {
        if (this.form.invalid) return;
        const payload = {
            ...this.form.value,
            tipo: this.data.tipo,
            idMatriz: this.idMatriz,
            dataVencimento: this.formatDate(this.form.value.dataVencimento)
        };

        const req = this.editando
            ? this.contaService.atualizar(this.data.conta.idConta, payload)
            : this.contaService.salvar(payload);

        req.subscribe({
            next: () => {
                this.snackBar.open('Conta salva com sucesso!', 'Fechar', { duration: 3000 });
                this.dialogRef.close(true);
            },
            error: () => this.snackBar.open('Erro ao salvar conta', 'Fechar', { duration: 3000 })
        });
    }

    private formatDate(val: any): string {
        if (!val) return '';
        if (typeof val === 'string') return val;
        const d = new Date(val);
        return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
    }

    fechar() { this.dialogRef.close(false); }
}
