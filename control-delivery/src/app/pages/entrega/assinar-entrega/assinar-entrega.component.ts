import { Component, EventEmitter, Input, Output } from '@angular/core';
import { EntregaService } from '../../../services/entrega.service';
import { EntregadorService } from '../../../services/entregador.service';
import { Entregador } from '../../../interfaces/entregador';
import { EntregaAssinar } from '../../../interfaces/entrega-assinar';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-assinar-entrega',
  templateUrl: './assinar-entrega.component.html',
  styleUrl: './assinar-entrega.component.scss'
})
export class AssinarEntregaComponent {
  showModal: boolean = false;
  entregas: EntregaAssinar[] = []; // Lista de entregas (pode ser substituída pelo tipo real)
  entregadores: Entregador[] = [];

  filtroEntregador: number[] = [];

   get entregasSelecionadas(): EntregaAssinar[] {
    return this.entregas.filter(e => e.selecionada);
  }

  selectedRange: { start: Date | null; end: Date | null } | null = null;

  onDateRangeChange(range: { start: Date | null; end: Date | null }) {
    this.showModal = false;
    this.selectedRange = range;
  }

  selecionarTodas: boolean = false;

  constructor(private entregaService: EntregaService,
    private entregadorService: EntregadorService,
    private authService: AuthService,
    private snackBar: MatSnackBar) { }

  ngOnInit() {
    this.carregarEntregadores();
  }

  carregarEntregadores() {
    this.entregadorService.carregarEntregadores()
      .subscribe({
        next: (res: any) => {
          this.entregadores = res.body;
        },
        error: (error) => {
          console.error(error);
        }
      });
  }

  buscarDados() {
    if (!this.selectedRange || !this.selectedRange.start || !this.selectedRange.end) {
      this.snackBar.open('Por favor, selecione o intervalo de datas.', 'Fechar', { duration: 3000 });
      return;
    }
    const startDate = this.selectedRange.start;
    const endDate = this.selectedRange.end;

    const formattedStart = this.formatDate(startDate, 'start');
    const formattedEnd = this.formatDate(endDate, 'end');

    this.carregar(formattedStart, formattedEnd);
  }

  carregar(formattedStart: any, formattedEnd: any) {
    this.entregaService.carregarEntregasAssinar(formattedStart, formattedEnd, this.filtroEntregador).subscribe({
      next: (res) => {
        this.entregas = res;
        if (this.entregas.length === 0) {
          this.showModal = true;
        } else {
          this.showModal = false;
        }
      },
      error: (erro) => {
        console.error(erro);
      }
    });
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

  toggleSelecionarTodas(): void {
    this.entregas.forEach(entrega => (entrega.selecionada = this.selecionarTodas));
  }

  atualizarSelecionados(): void {
    this.selecionarTodas = this.entregas.every(entrega => entrega.selecionada);
  }



  assinarSelecionados() {
    const entregasSelecionadas = this.entregas.filter(entrega => entrega.selecionada);
    if (entregasSelecionadas.length === 0) {
      this.snackBar.open('Nenhuma entrega selecionada para assinatura.', 'Fechar', { duration: 3000 });
      return;
    }

    let idEntregas: number[] = entregasSelecionadas.map(entrega => entrega.idEntrega);
    let idUser = this.authService.getUserId();
    this.entregaService.assinarEntregas(idEntregas, idUser).subscribe({
      next: () => {
        this.snackBar.open("Entregas assinadas com sucesso", "Fechar", { duration: 3000 });

        const startDate = this.selectedRange!.start;
        const endDate = this.selectedRange!.end;

        const formattedStart = this.formatDate(startDate!, 'start');
        const formattedEnd = this.formatDate(endDate!, 'end');
        this.carregar(formattedStart, formattedEnd);
      },
      error(erro) {
        console.error(erro);
      }
    });
    console.log('Assinando entregas:', entregasSelecionadas);
    // Lógica para processar assinaturas
  }

  closeModal() {
    this.showModal = false;
  }

}
