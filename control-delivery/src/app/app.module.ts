import { LOCALE_ID, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'
import { ButtonComponent } from './components/button/button.component';
import { LoginComponent } from './pages/login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HomeComponent } from './pages/home/home.component';
import { MenuComponent } from './components/menu/menu.component';

import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatCheckboxModule } from '@angular/material/checkbox';

import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { AuthInterceptor } from './services/auth.interceptor.service';
import { PermissaoGuard } from './services/permissao.guard.service';
import { UsuarioComponent } from './pages/usuario/usuario.component';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatSelectModule } from '@angular/material/select';
import { registerLocaleData } from '@angular/common';
import localePt from '@angular/common/locales/pt';

import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { ModalViewUserComponent } from './pages/usuario/modal-view-user/modal-view-user.component';
import { ModalFormUserComponent } from './pages/usuario/modal-form-user/modal-form-user.component';
import { MatrizComponent } from './pages/matriz/matriz.component';
import { EntregadorComponent } from './pages/entregador/entregador.component';
import { ModalFormEntregadorComponent } from './pages/entregador/modal-form-entregador/modal-form-entregador.component';
import { ModalViewEntregadorComponent } from './pages/entregador/modal-view-entregador/modal-view-entregador.component';
import { RelatoriosComponent } from './pages/relatorios/relatorios.component';
import { DatePickerComponent } from './components/date-picker/date-picker.component';
import { MAT_DATE_LOCALE, MatNativeDateModule } from '@angular/material/core';
import { ServicosComponent } from './pages/servicos/servicos.component';
import { ModalViewServicosComponent } from './pages/servicos/modal-view-servicos/modal-view-servicos.component';
import { DateTimeFormatPipe } from './pipes/date-time-format.pipe';
import { EntregaComponent } from './pages/entrega/entrega.component';
import { AssinarEntregaComponent } from './pages/entrega/assinar-entrega/assinar-entrega.component';
import { ModalFormEntregaComponent } from './pages/entrega/modal-form-entrega/modal-form-entrega.component';
import { ModalPesquisaEntregaComponent } from './pages/entrega/modal-pesquisa-entrega/modal-pesquisa-entrega.component';
import { ModalAtribuirEntregadorComponent } from './pages/entrega/modal-atribuir-entregador/modal-atribuir-entregador.component';
import { ModalVisualizarEntregaComponent } from './pages/entrega/modal-visualizar-entrega/modal-visualizar-entrega.component';
import { MatTabsModule } from '@angular/material/tabs';
import { MoedaPipe } from './pipes/moeda.pipe';
import { FormaPagamentoComponent } from './pages/forma-pagamento/forma-pagamento.component';
import { ModalFormFormapagamentoComponent } from './pages/forma-pagamento/modal-form-formapagamento/modal-form-formapagamento.component';
import { MapaEntregaComponent } from './pages/mapa-entrega/mapa-entrega.component';
import { GoogleMapsModule } from '@angular/google-maps';
import { ModalAlterarEntregadorComponent } from './pages/entrega/modal-alterar-entregador/modal-alterar-entregador.component';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { InserirEntregaComponent } from './pages/inserir-entrega/inserir-entrega.component';
import { PermissoesComponent } from './pages/permissoes/permissoes.component';
import { ContasComponent } from './pages/contas/contas.component';
import { ModalFormContaComponent } from './pages/contas/modal-form-conta/modal-form-conta.component';
import { ContasPagarComponent } from './pages/contas-pagar/contas-pagar.component';
import { ContasReceberComponent } from './pages/contas-receber/contas-receber.component';
import { RelatorioContasComponent } from './pages/relatorio-contas/relatorio-contas.component';
import { FluxoCaixaComponent } from './pages/fluxo-caixa/fluxo-caixa.component';
import { EstoqueProdutosComponent, ModalFormProdutoComponent } from './pages/estoque-produtos/estoque-produtos.component';
import { EstoqueMovimentacoesComponent } from './pages/estoque-movimentacoes/estoque-movimentacoes.component';
import { EstoqueEntradaComponent } from './pages/estoque-entrada/estoque-entrada.component';
import { EstoqueRelatorioComponent } from './pages/estoque-relatorio/estoque-relatorio.component';
import { CategoriasComponent } from './pages/categorias/categorias.component';
import { ModalAlterarSenhaComponent } from './components/modal-alterar-senha/modal-alterar-senha.component';
import { FiliaisComponent } from './pages/filiais/filiais.component';
import { ModalFormFilialComponent } from './pages/filiais/modal-form-filial/modal-form-filial.component';
registerLocaleData(localePt);

@NgModule({
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    { provide: LOCALE_ID, useValue: 'pt-BR' },
    { provide: MAT_DATE_LOCALE, useValue: 'pt-BR' },
    DateTimeFormatPipe,
    PermissaoGuard,
  ],
  declarations: [
    AppComponent,
    ButtonComponent,
    LoginComponent,
    HomeComponent,
    MenuComponent,
    UsuarioComponent,
    ModalViewUserComponent,
    ModalFormUserComponent,
    MatrizComponent,
    EntregadorComponent,
    ModalFormEntregadorComponent,
    ModalViewEntregadorComponent,
    RelatoriosComponent,
    DatePickerComponent,
    ServicosComponent,
    ModalViewServicosComponent,
    DateTimeFormatPipe,
    EntregaComponent,
    AssinarEntregaComponent,
    ModalFormEntregaComponent,
    ModalPesquisaEntregaComponent,
    ModalAtribuirEntregadorComponent,
    ModalVisualizarEntregaComponent,
    MoedaPipe,
    FormaPagamentoComponent,
    ModalFormFormapagamentoComponent,
    MapaEntregaComponent,
    ModalAlterarEntregadorComponent,
    InserirEntregaComponent,
    PermissoesComponent,
    ContasComponent,
    ModalFormContaComponent,
    ContasPagarComponent,
    ContasReceberComponent,
    RelatorioContasComponent,
    FluxoCaixaComponent,
    EstoqueProdutosComponent,
    ModalFormProdutoComponent,
    EstoqueMovimentacoesComponent,
    EstoqueEntradaComponent,
    EstoqueRelatorioComponent,
    CategoriasComponent,
    ModalAlterarSenhaComponent,
    FiliaisComponent,
    ModalFormFilialComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    MatIconModule,
    MatProgressSpinnerModule,
    HttpClientModule,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    MatDialogModule,
    ReactiveFormsModule,
    MatNativeDateModule,
    MatSelectModule,
    MatCheckboxModule,
    MatDatepickerModule,
    MatTabsModule,
    GoogleMapsModule,
    MatMenuModule,
    MatButtonModule,
    MatSnackBarModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
