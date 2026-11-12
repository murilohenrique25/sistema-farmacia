import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { AuthService } from '../../../services/auth.service';
import { DateTimeFormatPipe } from '../../../pipes/date-time-format.pipe';
import { Matriz } from '../../../interfaces/matriz';
import { EmpresaService } from '../../../services/empresa.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EntregaService } from '../../../services/entrega.service';
import { FormaPagamento } from '../../../interfaces/forma-pagamento';
import { FormaPagamentoService } from '../../../services/forma-pagamento.service';

@Component({
  selector: 'app-modal-form-entrega',
  templateUrl: './modal-form-entrega.component.html',
  styleUrl: './modal-form-entrega.component.scss'
})
export class ModalFormEntregaComponent {

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
    public dialogRef: MatDialogRef<ModalFormEntregaComponent>,
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private dateTimeFormatPipe: DateTimeFormatPipe,
    private entregaService: EntregaService,
    private matrizService: EmpresaService,
    private formaPagamentoService: FormaPagamentoService,
    @Inject(MAT_DIALOG_DATA) public data: any,
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
    if (this.data) {
      this.editarEntrega = true;
    }
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
      valorFrete: [null],
      telefone: [null, [Validators.required]],
      idUser: [this.idUsuario]
    });

    if (this.listaMatriz.length === 1) {
      this.formEntrega.controls['idMatriz'].disable();
    }

    if (this.data && this.data.cupomOrcamento) {
      this.fillForm();
    }
  }

  calcularTroco() {
    const valorTotal = parseFloat(this.formEntrega.controls['valorTotal'].value) || 0;
    const valorRecebido = parseFloat(this.formEntrega.controls['valorReceber'].value) || 0;

    let troco = Math.max(0, valorRecebido - valorTotal);

    this.formEntrega.controls['troco'].setValue(parseFloat(troco.toFixed(2)));
  }

  fillForm() {
    this.formEntrega.patchValue(
      {
        cupomOrcamento: this.data.cupomOrcamento,
        valorTotal: this.data.valorTotal,
        troco: this.data.troco,
        valorReceber: this.data.valorReceber,
        tipoEntrega: this.data.tipoEntrega,
        formaPagamento: this.data.formaPagamento,
        idMatriz: this.data.idMatriz,
        observacao: this.data.observacao,
        telefone: this.data.telefone,
        valorFrete: this.data.valorFrete
      }
    );
  }

  /**
   * Indica se a matriz/filial selecionada possui taxa de frete fixa.
   * Quando true, o frete é definido no cadastro da empresa e o campo na entrega fica oculto.
   */
  get freteFixoSelecionado(): boolean {
    const idMatriz = this.formEntrega?.getRawValue()?.idMatriz;
    const matriz = this.listaMatriz.find(m => m.idMatriz === idMatriz);
    return !!matriz?.freteFixo;
  }

  saveEntrega() {
    if (this.isSaving) return;

    let objEntregaForm = this.formEntrega.getRawValue();
    if (objEntregaForm.telefone.trim() !== '' && objEntregaForm.telefone.length !== 11) {
      this.snackBar.open("Número Whatsapp incorreto!", 'Fechar', { duration: 3000 });
      return;
    }

    this.isSaving = true;

    if (this.data) {
      this.entregaService.atualizarEntrega(this.data.idEntrega, objEntregaForm).subscribe({
        next: () => {
          this.isSaving = false;
          this.closeModal();
          this.snackBar.open('Entrega editada com sucesso!', 'Fechar', { duration: 3000 });
        },
        error: (erro) => {
          this.isSaving = false;
          console.error('Erro ao editar entrega:', erro);
          this.snackBar.open('Erro ao editar o entrega. Verifique os dados e tente novamente.', 'Fechar', { duration: 3000 });
        },
      });
    } else {
      this.entregaService.salvarEntrega(objEntregaForm).subscribe({
        next: () => {
          this.isSaving = false;
          this.closeModal();
          this.snackBar.open('Entrega salva com sucesso!', 'Fechar', { duration: 3000 });
        },
        error: (erro) => {
          this.isSaving = false;
          console.error('Erro ao salvar entrega:', erro);
          this.snackBar.open('Erro ao salvar o entregador', 'Fechar', { duration: 3000 });
        },
      });
    }
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

  closeModal() { this.dialogRef.close(); }
}
