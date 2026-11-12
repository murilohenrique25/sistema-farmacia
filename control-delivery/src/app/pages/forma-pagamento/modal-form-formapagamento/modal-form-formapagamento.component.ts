import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormaPagamentoService } from '../../../services/forma-pagamento.service';

@Component({
  selector: 'app-modal-form-formapagamento',
  templateUrl: './modal-form-formapagamento.component.html',
  styleUrl: './modal-form-formapagamento.component.scss'
})
export class ModalFormFormapagamentoComponent {

  formPG: FormGroup;

  labelAtivo: string = 'Ativo';

  constructor(public dialogRef: MatDialogRef<ModalFormFormapagamentoComponent>,
    private formBuilder: FormBuilder,
    private formaPagamentoService: FormaPagamentoService,
    @Inject(MAT_DIALOG_DATA) public data: any,) { }

  ngOnInit() {
    this.buildForm();
  }

  buildForm() {
    this.formPG = this.formBuilder.group({
      descricao: [null, [Validators.required, Validators.minLength(3)]],
      ativo: [true],
    });
    if (this.data && this.data.descricao) {
      this.fillForm();
    }
  }

  fillForm() {
    this.formPG.patchValue(
      {
        descricao: this.data.descricao,
        ativo: this.data.ativo,
      }
    );
  }

  closeModal() {
    this.dialogRef.close();
  }

  atualizarLabel() {
    this.labelAtivo = this.formPG.controls['ativo'].value ? 'Ativo' : 'Desativado';
  }

  savePG() {
    const objUserForm = this.formPG.getRawValue();
    if (this.data) {
      this.formaPagamentoService.editarFormaPagamento(this.data.idFormaPagamento, objUserForm).subscribe({
        next: () => {
          this.closeModal();
        }, error(err) {
          console.error(err);
        },
      });
    } else {
      this.formaPagamentoService.salvarFormaPagamento(objUserForm).subscribe({
        next: () => {
          this.closeModal();
        }, error(err) {
          console.error(err);
        },
      });
    }
  }

}
