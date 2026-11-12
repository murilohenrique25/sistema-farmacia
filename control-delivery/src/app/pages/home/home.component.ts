import { Component } from '@angular/core';
import { EntregaService } from '../../services/entrega.service';
import { EntregadorService } from '../../services/entregador.service';
import { ContaService } from '../../services/conta.service';
import { Entregador } from '../../interfaces/entregador';
import { DadosEntrega } from '../../interfaces/dados-entrega';
import { Conta, ContaAlerta } from '../../interfaces/conta';
import { AuthService } from '../../services/auth.service';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  nomeUser: string | null;
  cidade: string = '';
  dataAtual: Date = new Date();
  dadosTelaInicial: DadosEntrega | null;
  porcentagem: number;
  existeDado: boolean = false;
  entregadores: Entregador[] = [];
  alertasContas: ContaAlerta[] = [];
  entradasHoje: Conta[] = [];
  saidasHoje: Conta[] = [];
  totalEntradasHoje = 0;
  totalSaidasHoje = 0;

  constructor(
    private entregaService: EntregaService,
    private entregadorService: EntregadorService,
    private contaService: ContaService,
    public authService: AuthService
  ) { }

  ngOnInit() {
    this.carregarEntregadores();
    this.nomeUser = localStorage.getItem('nome');
    this.entregaService.carregarDadosTelaInicial().subscribe({
      next: (res: any) => {
        this.dadosTelaInicial = res[0];
        this.existeDado = true;
        if (this.dadosTelaInicial !== null) {
          this.porcentagem = Number(
            (
              (this.dadosTelaInicial.totalEntregaMesFinalizadas! /
                this.dadosTelaInicial.totalEntregasMes!) * 100
            ).toFixed(2)
          );
        }
      },
      error(err) { console.error(err); },
    });
    this.contaService.buscarAlertas(7).subscribe({
      next: (res) => { this.alertasContas = res; },
      error: () => { }
    });
    this.carregarMovimentacoesHoje();
  }

  carregarEntregadores() {
    this.entregadorService.carregarEntregadores().subscribe({
      next: (res: any) => {
        this.entregadores = res.body;
      },
      error: (erro) => {
        console.error(erro);
      }
    });
  }

  get entregasPendentes(): number {
    if (!this.dadosTelaInicial) return 0;
    return this.dadosTelaInicial.totalEntregasMes! - this.dadosTelaInicial.totalEntregaMesFinalizadas!;
  }

  get alertasVencidos(): ContaAlerta[] {
    return this.alertasContas.filter(a => a.diasParaVencer < 0);
  }

  get alertasProximos(): ContaAlerta[] {
    return this.alertasContas.filter(a => a.diasParaVencer >= 0);
  }

  temPermissaoContas(): boolean {
    return this.authService.getRole() === 'Administrador'
      || this.authService.temPermissao('ACESSAR_CONTAS_PAGAR')
      || this.authService.temPermissao('ACESSAR_CONTAS_RECEBER');
  }

  temDashboardCompleto(): boolean {
    return this.authService.temPermissao('GERENCIAR_ENTREGAS');
  }

  carregarMovimentacoesHoje() {
    const hoje = new Date();
    const dataStr = `${hoje.getFullYear()}-${String(hoje.getMonth() + 1).padStart(2, '0')}-${String(hoje.getDate()).padStart(2, '0')}`;
    this.contaService.listarPorPeriodo('RECEBER', dataStr, dataStr).subscribe({
      next: (res) => {
        this.entradasHoje = res;
        this.totalEntradasHoje = res.reduce((s, c) => s + Number(c.valor), 0);
      },
      error: () => { }
    });
    this.contaService.listarPorPeriodo('PAGAR', dataStr, dataStr).subscribe({
      next: (res) => {
        this.saidasHoje = res;
        this.totalSaidasHoje = res.reduce((s, c) => s + Number(c.valor), 0);
      },
      error: () => { }
    });
  }
}
