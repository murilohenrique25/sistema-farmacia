import { Component, OnInit } from '@angular/core';
import { UsuarioService } from '../../services/usuario.service';
import { PermissaoService, Permissao } from '../../services/permissao.service';
import { Usuario } from '../../interfaces/usuario';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
    selector: 'app-permissoes',
    templateUrl: './permissoes.component.html',
    styleUrl: './permissoes.component.scss'
})
export class PermissoesComponent implements OnInit {

    usuarios: Usuario[] = [];
    todasPermissoes: Permissao[] = [];
    usuarioSelecionado: Usuario | null = null;
    permissoesDoUsuario: Set<number> = new Set();
    salvando = false;
    carregando = true;
    carregandoPermissoes = false;
    filtro = '';

    constructor(
        private usuarioService: UsuarioService,
        private permissaoService: PermissaoService,
        private snackBar: MatSnackBar
    ) { }

    ngOnInit() {
        this.carregarDados();
    }

    carregarDados() {
        this.carregando = true;
        this.usuarioService.buscarUsuarios().subscribe({
            next: (res) => {
                this.usuarios = res;
                this.carregando = false;
            },
            error: () => {
                this.snackBar.open('Erro ao carregar usuários', 'Fechar', { duration: 3000 });
                this.carregando = false;
            }
        });

        this.permissaoService.listarTodas().subscribe({
            next: (res) => { this.todasPermissoes = res; },
            error: () => this.snackBar.open('Erro ao carregar permissões', 'Fechar', { duration: 3000 })
        });
    }

    selecionarUsuario(usuario: Usuario) {
        this.usuarioSelecionado = usuario;
        this.permissoesDoUsuario = new Set();
        this.carregandoPermissoes = true;

        this.permissaoService.buscarPorUsuario(usuario.id).subscribe({
            next: (res) => {
                this.permissoesDoUsuario = new Set(res.map(p => p.idPermissao));
                this.carregandoPermissoes = false;
            },
            error: () => {
                this.snackBar.open('Erro ao carregar permissões do usuário', 'Fechar', { duration: 3000 });
                this.carregandoPermissoes = false;
            }
        });
    }

    temPermissao(idPermissao: number): boolean {
        return this.permissoesDoUsuario.has(idPermissao);
    }

    togglePermissao(idPermissao: number) {
        if (this.permissoesDoUsuario.has(idPermissao)) {
            this.permissoesDoUsuario.delete(idPermissao);
        } else {
            this.permissoesDoUsuario.add(idPermissao);
        }
    }

    salvar() {
        if (!this.usuarioSelecionado) return;
        this.salvando = true;

        this.permissaoService.vincularPermissoes(
            this.usuarioSelecionado.id,
            Array.from(this.permissoesDoUsuario)
        ).subscribe({
            next: () => {
                this.salvando = false;
                this.snackBar.open('Permissões salvas com sucesso!', 'Fechar', { duration: 3000 });
            },
            error: () => {
                this.salvando = false;
                this.snackBar.open('Erro ao salvar permissões', 'Fechar', { duration: 3000 });
            }
        });
    }

    get usuariosFiltrados(): Usuario[] {
        if (!this.filtro.trim()) return this.usuarios;
        const f = this.filtro.toLowerCase();
        return this.usuarios.filter(u =>
            u.nome?.toLowerCase().includes(f) || u.username?.toLowerCase().includes(f)
        );
    }
}
