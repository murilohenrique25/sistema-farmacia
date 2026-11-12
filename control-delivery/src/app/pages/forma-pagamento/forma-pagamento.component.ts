import { Component, ViewChild } from '@angular/core';
import { FormaPagamentoService } from '../../services/forma-pagamento.service';
import { FormaPagamento } from '../../interfaces/forma-pagamento';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { ModalFormFormapagamentoComponent } from './modal-form-formapagamento/modal-form-formapagamento.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-forma-pagamento',
  templateUrl: './forma-pagamento.component.html',
  styleUrl: './forma-pagamento.component.scss'
})
export class FormaPagamentoComponent {

  displayedColumns: string[] = ['id', 'nome', 'ativo', 'action'];
  dataSource: any;
  listFormaPagamento: FormaPagamento[] = [];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private formaPagamentoService: FormaPagamentoService, public dialog: MatDialog) { }

  ngOnInit() {
    this.carregarFormasPagamento();
  }

  carregarFormasPagamento() {
    this.formaPagamentoService.carregarTodasFormasPagamento().subscribe({
      next: (res: any) => {
        this.listFormaPagamento = res;
        this.dataSource = new MatTableDataSource<any>(this.listFormaPagamento);

        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort; this.paginator._intl.itemsPerPageLabel = "Itens por página";
      },
      error: (error) => {
        console.error(error);
      }
    });
  }

  openModalEditForma(formaPagamento: any) {
    this.dialog.open(ModalFormFormapagamentoComponent, {
      width: '700px',
      height: '250px',
      data: formaPagamento
    }).afterClosed().subscribe(() => this.carregarFormasPagamento());
  }

  addModalFP() {
    this.dialog.open(ModalFormFormapagamentoComponent, {
      width: '700px',
      height: '250px',
    }).afterClosed().subscribe(() => this.carregarFormasPagamento());
  }

  aplicarFilto(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

}
