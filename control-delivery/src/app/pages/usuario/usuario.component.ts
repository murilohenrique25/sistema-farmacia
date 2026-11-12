import { Component, OnInit, ViewChild } from '@angular/core';
import { UsuarioService } from '../../services/usuario.service';
import { Usuario } from '../../interfaces/usuario';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';
import { ModalViewUserComponent } from './modal-view-user/modal-view-user.component';
import { ModalFormUserComponent } from './modal-form-user/modal-form-user.component';

@Component({
  selector: 'app-usuario',
  templateUrl: './usuario.component.html',
  styleUrl: './usuario.component.scss'
})
export class UsuarioComponent {
  displayedColumns: string[] = ['id', 'nome', 'username', 'ativo', 'turno', 'action'];
  dataSource: any;
  existeDado: boolean = false;
  listUsuarios: Usuario[] = [];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private usuarioService: UsuarioService, public dialog: MatDialog) {
    this.dataSource = new MatTableDataSource<any>(this.listUsuarios);
  }

  ngOnInit() {
    this.carregarUsuarios();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  carregarUsuarios() {
    this.usuarioService.buscarUsuarios().subscribe({
      next: (res: any) => {
        this.listUsuarios = res;
        this.dataSource = new MatTableDataSource<any>(this.listUsuarios);

        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort; this.paginator._intl.itemsPerPageLabel = "Itens por página";
        this.existeDado = true;
      },
      error: (error) => {
        console.error(error);
      }
    });
  }

  aplicarFilto(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  openModalViewUser(user: Usuario) {
    this.dialog.open(ModalViewUserComponent, {
      width: '700px',
      height: '500px',
      data: user
    });
  }

  addModalUser() {
    this.dialog.open(ModalFormUserComponent, {
      width: '700px',
      height: '500px',
    }).afterClosed().subscribe(() => this.carregarUsuarios());
  }

  openModalEditUser(user: Usuario) {
    this.dialog.open(ModalFormUserComponent, {
      width: '700px',
      height: '500px',
      data: user
    }).afterClosed().subscribe(() => this.carregarUsuarios());
  }
}
