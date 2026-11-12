import { Component } from '@angular/core';
import { Entregador } from '../../interfaces/entregador';
import { EntregadorService } from '../../services/entregador.service';
import { RelatorioService } from '../../services/relatorio.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-relatorios',
  templateUrl: './relatorios.component.html',
  styleUrl: './relatorios.component.scss'
})
export class RelatoriosComponent {

  loading: boolean = false;

  selectedEntregadores: number[] = [];
  selectedStatus: string[] = [];

  listEntregador: Entregador[] = [];

  listStatus = [
    { valor: 'ABERTA', descricao: 'Abertas' },
    { valor: 'EM_ROTA', descricao: 'Em Rota' },
    { valor: 'CANCELADA', descricao: 'Canceladas' },
    { valor: 'FINALIZADA', descricao: 'Finalizadas' },
    { valor: 'FINALIZADA_MANUAL', descricao: 'Finalizadas Manualmente' },
    { valor: 'FINALIZADA_ASSINADA', descricao: 'Finalizadas e Assinadas' },
    { valor: 'NAO_ENTREGUE', descricao: 'Não Entregues' },
  ];

  selectedRange: { start: Date | null; end: Date | null } | null = null;

  onDateRangeChange(range: { start: Date | null; end: Date | null }) {
    this.selectedRange = range;
  }

  constructor(
    private entregadorService: EntregadorService,
    private relatorioService: RelatorioService,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit() {
    this.carregarEntregadores();
  }

  carregarEntregadores() {
    this.entregadorService.carregarEntregadores().subscribe(
      {
        next: (res: any) => {
          this.listEntregador = res.body;
        }
      }
    );
  }

  buscarRelatorioTodasEntregas() {
    if (!this.selectedRange || !this.selectedRange.start || !this.selectedRange.end) {
      this.snackBar.open('Por favor, selecione o intervalo de datas.');
      return;
    }
    if(this.selectedStatus == null || this.selectedStatus.length == 0){
      this.snackBar.open('Por favor, selecione pelo menos um status');
      return;
    }
    this.loading = true;
    const startDate = this.selectedRange.start;
    const endDate = this.selectedRange.end;

    if (startDate && endDate) {
      const formattedStart = this.formatDate(startDate, 'start');
      const formattedEnd = this.formatDate(endDate, 'end');

      this.relatorioService
        .generatePdf(formattedStart, formattedEnd, 'geral', this.selectedStatus, this.selectedEntregadores)
        .subscribe({
          next: (response) => { this.downloadPdf(response, 'relatorio-geral.pdf'), this.loading = false; },
          error: (err) => { console.error('Erro ao gerar o relatório:', err), this.loading = false; },

        });
    }
    this.selectedEntregadores = [];
    this.selectedStatus = [];
  }

  buscarRelatorioMediaEntregas() {
    if (!this.selectedRange || !this.selectedRange.start || !this.selectedRange.end) {
      this.snackBar.open('Por favor, selecione o intervalo de datas.');
      return;
    }
    if(this.selectedStatus == null || this.selectedStatus.length == 0){
      this.snackBar.open('Por favor, selecione pelo menos um status');
      return;
    }
    this.loading = true;
    const startDate = this.selectedRange.start;
    const endDate = this.selectedRange.end;

    if (startDate && endDate) {
      const formattedStart = this.formatDate(startDate, 'start');
      const formattedEnd = this.formatDate(endDate, 'end');

      this.relatorioService
        .generatePdf(formattedStart, formattedEnd, 'totalizador', this.selectedStatus, this.selectedEntregadores)
        .subscribe({
          next: (response) => { this.downloadPdf(response, 'relatorio-totalizador.pdf'), this.loading = false; },
          error: (err) => { console.error('Erro ao gerar o relatório:', err), this.loading = false; },

        });
    }
    this.selectedEntregadores = [];
    this.selectedStatus = [];
  }

  buscarRelatorioServicos() {
    if (!this.selectedRange || !this.selectedRange.start || !this.selectedRange.end) {
      this.snackBar.open('Por favor, selecione o intervalo de datas.');
      return;
    }
    this.loading = true;
    const startDate = this.selectedRange.start;
    const endDate = this.selectedRange.end;

    if (startDate && endDate) {
      const formattedStart = this.formatDate(startDate, 'start');
      const formattedEnd = this.formatDate(endDate, 'end');

      this.relatorioService
        .generatePdfServices(formattedStart, formattedEnd, this.selectedStatus, this.selectedEntregadores)
        .subscribe({
          next: (response) => { this.downloadPdf(response, 'relatorio-servicos.pdf'), this.loading = false; },
          error: (err) => { console.error('Erro ao gerar o relatório:', err), this.loading = false; },

        });
    }
    this.selectedEntregadores = [];
    this.selectedStatus = [];
  }

  downloadPdf(data: Blob, filename: string) {
    const url = window.URL.createObjectURL(data);
    const link = document.createElement('a');
    link.href = url;
    link.download = filename;
    link.click();
    window.URL.revokeObjectURL(url);
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
