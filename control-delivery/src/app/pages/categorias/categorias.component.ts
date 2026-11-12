import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Categoria, CategoriaService } from '../../services/categoria.service';

@Component({
    selector: 'app-categorias',
    templateUrl: './categorias.component.html',
    styleUrl: './categorias.component.scss'
})
export class CategoriasComponent implements OnInit {

    categorias: Categoria[] = [];
    carregando = true;

    // Form
    descricao = '';
    tipo = 'PAGAR';
    editandoId: number | null = null;

    constructor(
        private categoriaService: CategoriaService,
        private snackBar: MatSnackBar
    ) { }

    ngOnInit() {
        this.carregar();
    }

    carregar() {
        this.carregando = true;
        this.categoriaService.listarTodas().subscribe({
            next: (res) => {
                this.categorias = res;
                this.carregando = false;
            },
            error: () => { this.carregando = false; }
        });
    }

    salvar() {
        if (!this.descricao.trim()) return;

        const payload: Categoria = {
            descricao: this.descricao.trim(),
            tipo: this.tipo,
            ativo: true
        };

        if (this.editandoId) {
            this.categoriaService.atualizar(this.editandoId, payload).subscribe({
                next: () => {
                    this.snackBar.open('Categoria atualizada!', 'Fechar', { duration: 3000 });
                    this.limpar();
                    this.carregar();
                },
                error: () => this.snackBar.open('Erro ao atualizar', 'Fechar', { duration: 3000 })
            });
        } else {
            this.categoriaService.salvar(payload).subscribe({
                next: () => {
                    this.snackBar.open('Categoria cadastrada!', 'Fechar', { duration: 3000 });
                    this.limpar();
                    this.carregar();
                },
                error: () => this.snackBar.open('Erro ao cadastrar', 'Fechar', { duration: 3000 })
            });
        }
    }

    editar(cat: Categoria) {
        this.editandoId = cat.idCategoria!;
        this.descricao = cat.descricao;
        this.tipo = cat.tipo;
    }

    excluir(cat: Categoria) {
        if (!confirm(`Deseja desativar a categoria "${cat.descricao}"?`)) return;
        this.categoriaService.excluir(cat.idCategoria!).subscribe({
            next: () => {
                this.snackBar.open('Categoria desativada!', 'Fechar', { duration: 3000 });
                this.carregar();
            },
            error: () => this.snackBar.open('Erro ao desativar', 'Fechar', { duration: 3000 })
        });
    }

    limpar() {
        this.descricao = '';
        this.tipo = 'PAGAR';
        this.editandoId = null;
    }

    get categoriasFiltradas(): Categoria[] {
        return this.categorias.filter(c => c.ativo);
    }
}
