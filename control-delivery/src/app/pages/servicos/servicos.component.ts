import { Component, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { Servico } from '../../interfaces/servico';
import { ServicoService } from '../../services/servico.service';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { ModalFormEntregadorComponent } from '../entregador/modal-form-entregador/modal-form-entregador.component';
import { ModalViewServicosComponent } from './modal-view-servicos/modal-view-servicos.component';

@Component({
  selector: 'app-servicos',
  templateUrl: './servicos.component.html',
  styleUrl: './servicos.component.scss'
})
export class ServicosComponent {

  displayedColumns: string[] = ['id', 'userId', 'dataHoraInicioServico', 'dataHoraTerminoServico', 'action'];
  dataSource: any;

  listaServivos: Servico[] = [];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private servicosService: ServicoService, public dialog: MatDialog) {
    this.dataSource = new MatTableDataSource<any>(this.listaServivos);
  }

  ngOnInit() {
    this.carregarServicos();
  }

  carregarServicos() {
    this.servicosService.buscarServicos().subscribe({
      next: (res: any) => {
        this.listaServivos = res;
        this.dataSource = new MatTableDataSource<any>(this.listaServivos);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort; this.paginator._intl.itemsPerPageLabel = "Itens por página";
      },
      error: (error) => {
        console.error(error);
      }
    });
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }


  aplicarFilto(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  openModalViewServico(servico: Servico) {
    this.dialog.open(
      ModalViewServicosComponent, {
      width: '700px',
      height: '400px',
      data: servico
    }
    ).afterClosed().subscribe(() => this.carregarServicos());
  }

}
