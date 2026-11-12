import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EmpresaService } from '../../../services/empresa.service';

@Component({
    selector: 'app-modal-form-filial',
    templateUrl: './modal-form-filial.component.html',
    styleUrl: './modal-form-filial.component.scss'
})
export class ModalFormFilialComponent {

    formFilial: FormGroup;
    editarFilial: boolean = false;
    isSaving: boolean = false;

    constructor(
        public dialogRef: MatDialogRef<ModalFormFilialComponent>,
        private formBuilder: FormBuilder,
        private empresaService: EmpresaService,
        @Inject(MAT_DIALOG_DATA) public data: any,
        private snackBar: MatSnackBar
    ) { }

    ngOnInit() {
        this.editarFilial = !!(this.data && this.data.idFilial);
        this.buildForm();
    }

    buildForm() {
        this.formFilial = this.formBuilder.group({
            idFilial: [this.data?.idFilial ?? null],
            idMatriz: [this.data?.idMatriz ?? null, [Validators.required]],
            nome: [this.data?.nome ?? null, [Validators.required]],
            logradouro: [this.data?.logradouro ?? null],
            telefone: [this.data?.telefone ?? null],
            cnpj: [this.data?.cnpj ?? null],
            email: [this.data?.email ?? null],
            latitude: [this.data?.latitude ?? null],
            longitude: [this.data?.longitude ?? null],
            freteFixo: [this.data?.freteFixo ?? false],
            valorFrete: [this.data?.valorFrete ?? null]
        });

        this.atualizarValidacaoFrete(this.formFilial.controls['freteFixo'].value);

        this.formFilial.controls['freteFixo'].valueChanges.subscribe((freteFixo: boolean) => {
            this.atualizarValidacaoFrete(freteFixo);
        });
    }

    atualizarValidacaoFrete(freteFixo: boolean) {
        const valorFrete = this.formFilial.controls['valorFrete'];
        if (freteFixo) {
            valorFrete.setValidators([Validators.required, Validators.min(0)]);
        } else {
            valorFrete.clearValidators();
            valorFrete.setValue(null);
        }
        valorFrete.updateValueAndValidity();
    }

    salvarFilial() {
        if (this.isSaving || this.formFilial.invalid) {
            this.formFilial.markAllAsTouched();
            return;
        }

        this.isSaving = true;
        const objFilial = this.formFilial.getRawValue();

        this.empresaService.salvarFilial(objFilial).subscribe({
            next: () => {
                this.isSaving = false;
                this.closeModal();
                this.snackBar.open(
                    this.editarFilial ? 'Filial editada com sucesso!' : 'Filial salva com sucesso!',
                    'Fechar', { duration: 3000 });
            },
            error: (erro) => {
                this.isSaving = false;
                console.error('Erro ao salvar filial:', erro);
                this.snackBar.open('Erro ao salvar a filial. Verifique os dados e tente novamente.', 'Fechar', { duration: 3000 });
            }
        });
    }

    closeModal() { this.dialogRef.close(); }
}
