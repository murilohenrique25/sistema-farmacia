import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { Entregador } from '../../../interfaces/entregador';
import { EntregadorService } from '../../../services/entregador.service';

@Component({
  selector: 'app-modal-pesquisa-entrega',
  templateUrl: './modal-pesquisa-entrega.component.html',
  styleUrl: './modal-pesquisa-entrega.component.scss'
})
export class ModalPesquisaEntregaComponent {
  selectedFilters: any = {}; // Objeto para armazenar os filtros selecionados

  selectedRange: { start: Date | null; end: Date | null } | null = null;
  listEntregador: Entregador[] = [];
  selectedEntregadores: number[] = [];
  selectedStatus: string[] = [];

  onDateRangeChange(range: { start: Date | null; end: Date | null }) {
    this.selectedRange = range;
  }

  listStatus = [
    { valor: 'ABERTA', descricao: 'Abertas' },
    { valor: 'EM_ROTA', descricao: 'Em Rota' },
    { valor: 'CANCELADA', descricao: 'Canceladas' },
    { valor: 'FINALIZADA', descricao: 'Finalizadas' },
    { valor: 'FINALIZADA_MANUAL', descricao: 'Finalizadas Manualmente' },
    { valor: 'FINALIZADA_ASSINADA', descricao: 'Finalizadas e Assinadas' },
    { valor: 'NAO_ENTREGUE', descricao: 'Não Entregues' },
  ];

  constructor(private dialogRef: MatDialogRef<ModalPesquisaEntregaComponent>, private entregadorService: EntregadorService) { }

  // Fecha a modal e retorna os filtros selecionados
  applyFilters() {
    if (this.selectedRange) {
      this.selectedFilters.dateRange = this.selectedRange; // Armazena o intervalo de datas
    }
    if (this.selectedEntregadores.length > 0) {
      this.selectedFilters.entregadores = this.selectedEntregadores; // Armazena os entregadores
    }
    if (this.selectedStatus.length > 0) {
      this.selectedFilters.status = this.selectedStatus; // Armazena os status
    }
    this.dialogRef.close(this.selectedFilters);
  }

  // Fecha a modal sem retornar nada
  cancel() {
    this.dialogRef.close(null);
  }

  ngOnInit() {
    this.entregadorService.carregarEntregadores().subscribe({
      next: (res: any) => {
        this.listEntregador = res.body;
      },
      error: (error) => {
        console.error(error);
      }
    });
  }
}
