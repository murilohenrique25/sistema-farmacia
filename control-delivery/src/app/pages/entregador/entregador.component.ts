import { Component, ViewChild } from '@angular/core';
import { Entregador } from '../../interfaces/entregador';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { EntregadorService } from '../../services/entregador.service';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { ModalFormEntregadorComponent } from './modal-form-entregador/modal-form-entregador.component';
import { ModalViewEntregadorComponent } from './modal-view-entregador/modal-view-entregador.component';

@Component({
  selector: 'app-entregador',
  templateUrl: './entregador.component.html',
  styleUrl: './entregador.component.scss'
})
export class EntregadorComponent {
  displayedColumns: string[] = ['idEntregador', 'nome', 'usuario', 'ativo', 'turno', 'action'];
  dataSource: any;
  existeDado: boolean = false;
  listaEntregadores: Entregador[] = [];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private entregadorService: EntregadorService, public dialog: MatDialog) {
    this.dataSource = new MatTableDataSource<any>(this.listaEntregadores);
  }

  ngOnInit() {
    this.carregarEntregadores();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;

    this.dataSource.sort = this.sort;
  }

  carregarEntregadores() {
    this.entregadorService.carregarEntregadores()
      .subscribe({
        next: (res: any) => {
          this.listaEntregadores = res.body;

          this.dataSource = new MatTableDataSource<any>(this.listaEntregadores);
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

  addModalEntregador() {
    this.dialog.open(ModalFormEntregadorComponent, {
      width: '700px',
      height: '500px',
    }).afterClosed().subscribe(() => this.carregarEntregadores());
  }

  openModalViewEntregador(entregador: Entregador) {
    this.dialog.open(ModalViewEntregadorComponent, {
      width: '700px',
      height: '400px',
      data: entregador
    }).afterClosed().subscribe(() => this.carregarEntregadores());
  }

  openModalEditEntregador(entregador: Entregador) {
    this.dialog.open(ModalFormEntregadorComponent, {
      width: '700px',
      height: '500px',
      data: entregador
    }).afterClosed().subscribe(() => this.carregarEntregadores());
  }


}
