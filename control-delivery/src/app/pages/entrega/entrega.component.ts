import { Component, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ModalFormEntregaComponent } from './modal-form-entrega/modal-form-entrega.component';
import { ModalPesquisaEntregaComponent } from './modal-pesquisa-entrega/modal-pesquisa-entrega.component';
import { EntregaService } from '../../services/entrega.service';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Entrega } from '../../interfaces/entrega';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ModalAtribuirEntregadorComponent } from './modal-atribuir-entregador/modal-atribuir-entregador.component';
import { AuthService } from '../../services/auth.service';
import { ModalVisualizarEntregaComponent } from './modal-visualizar-entrega/modal-visualizar-entrega.component';
import { filter } from 'rxjs';
import { ModalAlterarEntregadorComponent } from './modal-alterar-entregador/modal-alterar-entregador.component';

@Component({
  selector: 'app-entrega',
  templateUrl: './entrega.component.html',
  styleUrl: './entrega.component.scss'
})
export class EntregaComponent {

  displayedColumns: string[] = ['statusEntrega', 'idEntrega', 'cupomOrcamento', 'dataCadastro', 'entregador', 'vendedor', 'action'];
  dataSource: any;
  filters: any = {};
  existeDado: boolean;
  dataInicial: any = '';
  dataFinal: any = '';
  entregaSelecionada: any;

  selectedRow: any;

  showModalCancelar: boolean = false;
  selectedStatus: string[] = [];
  selectedEntregadores: number[] = [];
  listaEntregas: Entrega[] = [];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private dialog: MatDialog,
    private entregaService: EntregaService,
    private authService: AuthService,
    private snackbar: MatSnackBar) {
    this.dataSource = new MatTableDataSource<any>(this.listaEntregas);
  }

  ngOnInit() {
    this.openFilterModal();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;

    this.dataSource.sort = this.sort;
  }

  openMenu(entrega: any) {
    this.entregaSelecionada = entrega;
  }

  definirClasseStatus(status: string): { [key: string]: boolean } {
    // Lógica para determinar a classe
    return {
      'classe-cancelada': status === 'Cancelada',
      'classe-em-rota': status === 'Em rota',
      'classe-finaliza-assinada': status === 'Finaliza e Assinada',
      'classe-finalizada-manualmente': status === 'Finalizada Manualmente',
      'classe-nao-entregue': status === 'Não Entregue',
      'classe-aberta': status === 'Aberta',
      'classe-finalizada': status === 'Finaliza'
    };
  }

  aplicarFilto(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  addModalEntrega() {
    this.dialog.open(ModalFormEntregaComponent, {
      width: '700px',
      height: '680px',
    }).afterClosed().subscribe({
      next: () => {
        this.loadData();
      }
    }

    );
  }


  openFilterModal() {
    const dialogRef = this.dialog.open(ModalPesquisaEntregaComponent, {
      width: '700px',
    });

    // Manipular os dados após fechar a modal
    dialogRef.afterClosed().subscribe((result) => {
      if (result != null) {
        this.filters = result; // Atualiza os filtros aplicados
      }
      this.loadData(); // Recarrega os dados com os filtros selecionados
    });

  }

  loadData() {
    this.existeDado = true;
    let startDate;
    let endDate;
    let formattedStart;
    let formattedEnd;
    if (!this.filters || !this.filters.dateRange) {
      let dataAtual = new Date(Date.now());
      formattedStart = this.formatDate(dataAtual, 'start');
      formattedEnd = this.formatDate(dataAtual, 'end');
    } else {
      startDate = this.filters.dateRange.start;
      endDate = this.filters.dateRange.end;
      formattedStart = this.formatDate(startDate, 'start');
      formattedEnd = this.formatDate(endDate, 'end');
    }

    this.selectedStatus = this.filters.status;
    this.selectedEntregadores = this.filters.entregadores;
    console.log(this.selectedEntregadores);
    this.dataInicial = formattedStart;
    this.dataFinal = formattedEnd;

    this.entregaService.listarTodasEntregas(formattedStart, formattedEnd, this.selectedStatus, this.selectedEntregadores)
      .subscribe({
        next: (res: any) => {
          this.listaEntregas = res.body.sort((a: any, b: any) =>
            new Date(b.dataCadastroEntrega).getTime() - new Date(a.dataCadastroEntrega).getTime()
          );
          this.dataSource = new MatTableDataSource<any>(this.listaEntregas);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort; this.paginator._intl.itemsPerPageLabel = "Itens por página";
          this.existeDado = false;
        },
        error: (error) => {
          console.error(error);
        }
      });
  }

  selecionarLinha(row: any) {
    this.selectedRow = row; // Armazena a linha selecionada
  }

  editarEntrega(entrega: Entrega) {
    if (this.selectedRow === null || this.selectedRow === undefined) {
      this.snackbar.open('Selecione uma entrega em aberta para editar', 'Fechar', { duration: 3000 });
      return;
    }

    let statusEntrega = this.selectedRow.statusEntrega;
    let retorno = this.verificaEntregaAberta(statusEntrega, 'Só é permitido editar entrega com status igual à Aberta');

    if (retorno) {
      this.openModalEditEntrega(this.selectedRow);
    }
  }

  openModalEditEntrega(entrega: any) {


    this.dialog.open(ModalFormEntregaComponent, {
      width: '700px',
      height: '590px',
      data: entrega
    }).afterClosed().subscribe(() => this.loadData());
  }

  visualizarEntrega(entrega: Entrega) {
    if (this.selectedRow === null || this.selectedRow === undefined) {
      this.snackbar.open('Selecione uma entrega para visualizar os dados', 'Fechar', { duration: 3000 });
      return;
    }

    this.dialog.open(ModalVisualizarEntregaComponent, {
      width: '800px',
      height: '580px',
      data: this.selectedRow
    })
  }

  addModalEntregaAlterar(entrega: Entrega) {
    if (this.selectedRow === null || this.selectedRow === undefined) {
      this.snackbar.open('Selecione uma entrega para alterar o entregador', 'Fechar', { duration: 3000 });
      return;
    }

    this.dialog.open(ModalAlterarEntregadorComponent, {
      width: '400px',
      height: '250px',
      data: this.selectedRow
    }).afterClosed().subscribe(() => this.loadData());

  }

  addModalEntregaAtribuir(entrega: Entrega) {
    if (this.selectedRow === null || this.selectedRow === undefined) {
      this.snackbar.open('Selecione uma entrega para atribui entregador', 'Fechar', { duration: 3000 });
      return;
    }
    let statusEntrega = this.selectedRow.statusEntrega;
    let retorno = this.verificaEntregaAberta(statusEntrega, 'Só é permitido vincular entregador a entrega com status igual à Aberta');

    if (retorno) {
      this.dialog.open(ModalAtribuirEntregadorComponent, {
        width: '400px',
        height: '200px',
        data: this.selectedRow
      }).afterClosed().subscribe(() => this.loadData());
    }
  }

  verificaEntregaAberta(statusEntrega: any, mensagem: string): boolean {
    if (statusEntrega !== 'Aberta') {
      this.snackbar.open(mensagem, 'Fechar', { duration: 3000 });
      return false;
    }
    return true;
  }

  cancelarEntrega(entrega: Entrega) {
    if (this.selectedRow === null || this.selectedRow === undefined) {
      this.snackbar.open('Selecione uma entrega para cancelar', 'Fechar', { duration: 3000 });
      return;
    }
    let statusEntrega = this.selectedRow.statusEntrega;

    if (statusEntrega === 'Cancelada' || statusEntrega === 'Finalizada Manualmente' || statusEntrega === 'Finalizada e Assinada') {
      this.snackbar.open('Não é permitido cancelar entrega com status igual à ' + statusEntrega, 'Fechar', { duration: 3000 });
      return;
    }
    this.showModalCancelar = true;
  }
  confirmDelete() {
    this.showModalCancelar = false;
    let idEntrega = this.selectedRow.idEntrega;
    this.entregaService.cancelarEntregar(idEntrega).subscribe({
      next: () => {
        this.snackbar.open('Entrega cancelada.', 'Fechar', { duration: 3000 });
        this.loadData();
      },
      error: (erro) => {
        console.error(erro)
        this.snackbar.open('Erro ao cancelar entrega.', 'Fechar', { duration: 3000 });
      }
    });
  }

  finalizarEntregaManual(entrega: any) {
    this.entregaService.finalizarEntregar(entrega.idEntrega).subscribe({
      next: () => {
        this.loadData();
        this.snackbar.open('Entrega assinada com sucesso!', 'Fechar', { duration: 3000 });
      }
    });
  }

  assinarEntrega(entrega: any) {
    let idEntregas: number[] = [entrega.idEntrega];
    let idUser = this.authService.getUserId();

    this.entregaService.assinarEntregas(idEntregas, idUser).subscribe({
      next: () => {
        this.loadData();
        this.snackbar.open('Entrega assinada com sucesso!', 'Fechar', { duration: 3000 });
      }
    });
  }

  abrirGoogleMaps(entrega: any) {
    let latitude = entrega.latitude;
    let longitude = entrega.longitude;
    if (latitude != null) {

      const url = `https://www.google.com/maps/search/?api=1&query=${latitude},${longitude}`;
      window.open(url, '_blank');
    }
  }

  closeModal() {
    this.showModalCancelar = false;
  }

  entregaComGPS(entrega: any): boolean {
    if (!entrega) return false;

    return (
      entrega.latitude != null &&
      entrega.longitude != null
    );
  }

  entregaFinalizada(entrega: any): any {
    return entrega.statusEntrega === 'Finalizada' || entrega.statusEntrega === 'Finalizada Manualmente';
  }

  entregaEmRota(entrega: any): any {
    return entrega.statusEntrega === 'Em rota';
  }

  formatDate(date: Date, type: 'start' | 'end'): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = type === 'start' ? '00' : '23';
    const minutes = type === 'start' ? '00' : '59';
    const seconds = type === 'start' ? '00' : '59';

    return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
  }
}
