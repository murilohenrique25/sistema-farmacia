import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { HomeComponent } from './pages/home/home.component';
import { AuthGuardService } from './services/auth.guard.service';
import { PermissaoGuard } from './services/permissao.guard.service';
import { UsuarioComponent } from './pages/usuario/usuario.component';
import { EntregadorComponent } from './pages/entregador/entregador.component';
import { RelatoriosComponent } from './pages/relatorios/relatorios.component';
import { MatrizComponent } from './pages/matriz/matriz.component';
import { ServicosComponent } from './pages/servicos/servicos.component';
import { AssinarEntregaComponent } from './pages/entrega/assinar-entrega/assinar-entrega.component';
import { EntregaComponent } from './pages/entrega/entrega.component';
import { FormaPagamentoComponent } from './pages/forma-pagamento/forma-pagamento.component';
import { MapaEntregaComponent } from './pages/mapa-entrega/mapa-entrega.component';
import { PermissoesComponent } from './pages/permissoes/permissoes.component';
import { ContasPagarComponent } from './pages/contas-pagar/contas-pagar.component';
import { ContasReceberComponent } from './pages/contas-receber/contas-receber.component';
import { RelatorioContasComponent } from './pages/relatorio-contas/relatorio-contas.component';
import { FluxoCaixaComponent } from './pages/fluxo-caixa/fluxo-caixa.component';
import { EstoqueProdutosComponent } from './pages/estoque-produtos/estoque-produtos.component';
import { EstoqueMovimentacoesComponent } from './pages/estoque-movimentacoes/estoque-movimentacoes.component';
import { EstoqueEntradaComponent } from './pages/estoque-entrada/estoque-entrada.component';
import { EstoqueRelatorioComponent } from './pages/estoque-relatorio/estoque-relatorio.component';
import { CategoriasComponent } from './pages/categorias/categorias.component';

import { InserirEntregaComponent } from './pages/inserir-entrega/inserir-entrega.component';
import { FiliaisComponent } from './pages/filiais/filiais.component';

const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: '*', component: LoginComponent },
  { path: 'login', component: LoginComponent },
  { path: 'home', component: HomeComponent, canActivate: [AuthGuardService] },
  {
    path: 'usuario',
    component: UsuarioComponent,
    canActivate: [AuthGuardService, PermissaoGuard],
    data: { permissao: 'ACESSAR_VENDEDORES' }
  },
  {
    path: 'entregador',
    component: EntregadorComponent,
    canActivate: [AuthGuardService, PermissaoGuard],
    data: { permissao: 'ACESSAR_ENTREGADORES' }
  },
  {
    path: 'matriz',
    component: MatrizComponent,
    canActivate: [AuthGuardService, PermissaoGuard],
    data: { permissao: 'ACESSAR_MATRIZ' }
  },
  {
    path: 'filiais',
    component: FiliaisComponent,
    canActivate: [AuthGuardService, PermissaoGuard],
    data: { permissao: 'ACESSAR_MATRIZ' }
  },
  {
    path: 'relatorios',
    component: RelatoriosComponent,
    canActivate: [AuthGuardService, PermissaoGuard],
    data: { permissao: 'ACESSAR_RELATORIOS' }
  },
  {
    path: 'servicos',
    component: ServicosComponent,
    canActivate: [AuthGuardService, PermissaoGuard],
    data: { permissao: 'ACESSAR_SERVICOS' }
  },
  {
    path: 'inserir-entrega',
    component: InserirEntregaComponent,
    canActivate: [AuthGuardService, PermissaoGuard],
    data: { permissao: 'INSERIR_ENTREGA' }
  },
  {
    path: 'assinar-entregas',
    component: AssinarEntregaComponent,
    canActivate: [AuthGuardService, PermissaoGuard],
    data: { permissao: 'ASSINAR_ENTREGA' }
  },
  {
    path: 'entregas',
    component: EntregaComponent,
    canActivate: [AuthGuardService, PermissaoGuard],
    data: { permissao: 'ACESSAR_ENTREGAS' }
  },
  {
    path: 'forma-pagamento',
    component: FormaPagamentoComponent,
    canActivate: [AuthGuardService, PermissaoGuard],
    data: { permissao: 'ACESSAR_FORMA_PAGAMENTO' }
  },
  {
    path: 'mapa-entrega',
    component: MapaEntregaComponent,
    canActivate: [AuthGuardService, PermissaoGuard],
    data: { permissao: 'ACESSAR_MAPA' }
  },
  {
    path: 'permissoes',
    component: PermissoesComponent,
    canActivate: [AuthGuardService, PermissaoGuard],
    data: { permissao: 'SUPER_ADMIN' }
  },
  {
    path: 'contas-pagar',
    component: ContasPagarComponent,
    canActivate: [AuthGuardService, PermissaoGuard],
    data: { permissao: 'ACESSAR_CONTAS_PAGAR' }
  },
  {
    path: 'contas-receber',
    component: ContasReceberComponent,
    canActivate: [AuthGuardService, PermissaoGuard],
    data: { permissao: 'ACESSAR_CONTAS_RECEBER' }
  },
  {
    path: 'relatorio-contas',
    component: RelatorioContasComponent,
    canActivate: [AuthGuardService, PermissaoGuard],
    data: { permissao: 'ACESSAR_RELATORIOS' }
  },
  {
    path: 'fluxo-caixa',
    component: FluxoCaixaComponent,
    canActivate: [AuthGuardService, PermissaoGuard],
    data: { permissao: 'ACESSAR_CONTAS_PAGAR' }
  },
  {
    path: 'estoque/produtos',
    component: EstoqueProdutosComponent,
    canActivate: [AuthGuardService, PermissaoGuard],
    data: { permissao: 'ACESSAR_ESTOQUE' }
  },
  {
    path: 'estoque/movimentacoes',
    component: EstoqueMovimentacoesComponent,
    canActivate: [AuthGuardService, PermissaoGuard],
    data: { permissao: 'ACESSAR_ESTOQUE' }
  },
  {
    path: 'estoque/entrada',
    component: EstoqueEntradaComponent,
    canActivate: [AuthGuardService, PermissaoGuard],
    data: { permissao: 'GERENCIAR_ESTOQUE' }
  },
  {
    path: 'estoque/relatorio',
    component: EstoqueRelatorioComponent,
    canActivate: [AuthGuardService, PermissaoGuard],
    data: { permissao: 'ACESSAR_ESTOQUE' }
  },
  {
    path: 'categorias',
    component: CategoriasComponent,
    canActivate: [AuthGuardService, PermissaoGuard],
    data: { permissao: 'ACESSAR_CONTAS_PAGAR' }
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
