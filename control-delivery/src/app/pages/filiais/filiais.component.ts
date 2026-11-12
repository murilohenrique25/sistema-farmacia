import { Component, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { Filial } from '../../interfaces/filial';
import { EmpresaService } from '../../services/empresa.service';
import { AuthService } from '../../services/auth.service';
import { UsuarioService } from '../../services/usuario.service';
import { ModalFormFilialComponent } from './modal-form-filial/modal-form-filial.component';

@Component({
    selector: 'app-filiais',
    templateUrl: './filiais.component.html',
    styleUrl: './filiais.component.scss'
})
export class FiliaisComponent {
    displayedColumns: string[] = ['idFilial', 'nome', 'cnpj', 'telefone', 'freteFixo', 'valorFrete', 'action'];
    dataSource: any;
    existeDado: boolean = false;
    listaFiliais: Filial[] = [];
    idMatriz: any;

    @ViewChild(MatPaginator) paginator: MatPaginator;
    @ViewChild(MatSort) sort: MatSort;

    constructor(
        private empresaService: EmpresaService,
        private authService: AuthService,
        private usuarioService: UsuarioService,
        public dialog: MatDialog) {
        this.dataSource = new MatTableDataSource<any>(this.listaFiliais);
    }

    ngOnInit() {
        this.usuarioService.buscarUsuario(this.authService.getUserId()).subscribe({
            next: (usuario) => {
                this.idMatriz = usuario.idMatriz;
                this.carregarFiliais();
            },
            error: (erro) => {
                console.error('Erro ao obter usuário logado', erro);
            }
        });
    }

    ngAfterViewInit() {
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
    }

    carregarFiliais() {
        if (!this.idMatriz) {
            return;
        }
        this.empresaService.buscarFiliais(this.idMatriz).subscribe({
            next: (res) => {
                this.listaFiliais = res;
                this.dataSource = new MatTableDataSource<any>(this.listaFiliais);
                this.dataSource.paginator = this.paginator;
                this.dataSource.sort = this.sort;
                if (this.paginator) {
                    this.paginator._intl.itemsPerPageLabel = 'Itens por página';
                }
                this.existeDado = true;
            },
            error: (error) => {
                console.error('Erro ao carregar filiais', error);
                this.existeDado = true;
            }
        });
    }

    aplicarFiltro(event: Event) {
        const filterValue = (event.target as HTMLInputElement).value;
        this.dataSource.filter = filterValue.trim().toLowerCase();
        if (this.dataSource.paginator) {
            this.dataSource.paginator.firstPage();
        }
    }

    addModalFilial() {
        this.dialog.open(ModalFormFilialComponent, {
            width: '800px',
            data: { idMatriz: this.idMatriz }
        }).afterClosed().subscribe(() => this.carregarFiliais());
    }

    openModalEditFilial(filial: Filial) {
        this.dialog.open(ModalFormFilialComponent, {
            width: '800px',
            data: { ...filial, idMatriz: this.idMatriz }
        }).afterClosed().subscribe(() => this.carregarFiliais());
    }
}
