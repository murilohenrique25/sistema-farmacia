import { Component } from '@angular/core';
import { FormaPagamento } from '../../interfaces/forma-pagamento';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Matriz } from '../../interfaces/matriz';
import { AuthService } from '../../services/auth.service';
import { DateTimeFormatPipe } from '../../pipes/date-time-format.pipe';
import { EntregaService } from '../../services/entrega.service';
import { EmpresaService } from '../../services/empresa.service';
import { FormaPagamentoService } from '../../services/forma-pagamento.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-inserir-entrega',
  templateUrl: './inserir-entrega.component.html',
  styleUrl: './inserir-entrega.component.scss'
})
export class InserirEntregaComponent {

  listTipoEntrega: String[] = ['Venda Balcão', 'Manipulação'];
  listFormaPagamento: FormaPagamento[] = [];
  nomeUsuario: String | null;
  idUsuario: any;
  formEntrega: FormGroup;
  dataCadastroEntregaAtual: any;
  editarEntrega: boolean = false;
  troco: number = 0;
  telefoneFormatado: string = '';
  listaMatriz: Matriz[] = [];
  isSaving: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private dateTimeFormatPipe: DateTimeFormatPipe,
    private entregaService: EntregaService,
    private matrizService: EmpresaService,
    private formaPagamentoService: FormaPagamentoService,
    private snackBar: MatSnackBar
  ) { }


  ngOnInit() {

    this.nomeUsuario = this.authService.getUserName();
    this.idUsuario = this.authService.getUserId();
    this.dataCadastroEntregaAtual = new Date();

    this.matrizService.buscarMatrizes().subscribe({
      next: (res) => {
        this.listaMatriz = res;
        this.buildForm();
      }, error: (error) => {
        console.error("Erro ao buscar matrizes", error);
      }
    });

    this.formaPagamentoService.carregarTodasFormasPagamentoAtivos().subscribe({
      next: (res: any) => {
        this.listFormaPagamento = res;
      }
    });
    this.buildForm();
  }




  buildForm() {
    this.formEntrega = this.formBuilder.group({
      cupomOrcamento: [null, [Validators.required]],
      valorTotal: [0.00],
      troco: [this.troco],
      valorReceber: [0.00],
      dataCadastroEntrega: [this.dateTimeFormatPipe.transform(this.dataCadastroEntregaAtual)],
      usuario: [this.nomeUsuario],
      tipoEntrega: [this.listTipoEntrega[0]],
      formaPagamento: [null, [Validators.required]],
      idMatriz: [this.listaMatriz.length === 1 ? this.listaMatriz[0].idMatriz : null, [Validators.required]],
      observacao: [null],
      telefone: [null, [Validators.required]],
      idUser: [this.idUsuario]
    });

    if (this.listaMatriz.length === 1) {
      this.formEntrega.controls['idMatriz'].disable();
    }
  }

  calcularTroco() {
    const valorTotal = parseFloat(this.formEntrega.controls['valorTotal'].value) || 0;
    const valorRecebido = parseFloat(this.formEntrega.controls['valorReceber'].value) || 0;

    let troco = Math.max(0, valorRecebido - valorTotal);

    this.formEntrega.controls['troco'].setValue(parseFloat(troco.toFixed(2)));
  }

  saveEntrega() {
    if (this.isSaving) return;

    let objEntregaForm = this.formEntrega.getRawValue();
    if (objEntregaForm.telefone.trim() !== '' && objEntregaForm.telefone.length !== 11) {
      this.snackBar.open("Número Whatsapp incorreto!", 'Fechar', { duration: 3000 });
      return;
    }

    this.isSaving = true;
    this.entregaService.salvarEntrega(objEntregaForm).subscribe({
      next: () => {
        this.snackBar.open('Entrega salva com sucesso!', 'Fechar', { duration: 3000 });
        this.buildForm();
        this.isSaving = false;
      },
      error: (erro) => {
        console.error('Erro ao salvar entrega:', erro);
        this.snackBar.open('Erro ao salvar o entregador', 'Fechar', { duration: 3000 });
        this.isSaving = false;
      },
    });
  }

  onInputWhatsapp(event: Event): void {
    const input = event.target as HTMLInputElement;
    let raw = input.value.replace(/\D/g, ''); // Remove tudo que não é número

    if (raw.length > 11) raw = raw.substring(0, 11);

    // Armazena apenas os dígitos no FormControl (sem formatação)
    this.formEntrega.get('telefone')?.setValue(raw, { emitEvent: false });

    // Aplica a máscara visual
    if (raw.length >= 2 && raw.length <= 6) {
      this.telefoneFormatado = `(${raw.slice(0, 2)}) ${raw.slice(2)}`;
    } else if (raw.length > 6 && raw.length <= 10) {
      this.telefoneFormatado = `(${raw.slice(0, 2)}) ${raw.slice(2, 7)}-${raw.slice(7)}`;
    } else if (raw.length === 11) {
      this.telefoneFormatado = `(${raw.slice(0, 2)}) ${raw.slice(2, 7)}-${raw.slice(7, 11)}`;
    } else {
      this.telefoneFormatado = raw;
    }
  }
}
