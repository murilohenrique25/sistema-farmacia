import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UsuarioService } from '../../services/usuario.service';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-modal-alterar-senha',
    templateUrl: './modal-alterar-senha.component.html',
    styleUrl: './modal-alterar-senha.component.scss'
})
export class ModalAlterarSenhaComponent {

    form: FormGroup;

    constructor(
        public dialogRef: MatDialogRef<ModalAlterarSenhaComponent>,
        private fb: FormBuilder,
        private usuarioService: UsuarioService,
        private authService: AuthService,
        private snackBar: MatSnackBar,
        @Inject(MAT_DIALOG_DATA) public data: { idUser: number }
    ) {
        this.form = this.fb.group({
            password: ['', [Validators.required]],
            novaSenha: ['', [Validators.required, Validators.minLength(4)]],
            confirmacaoSenha: ['', [Validators.required]]
        });
    }

    get senhasNaoConferem(): boolean {
        return this.form.value.novaSenha !== this.form.value.confirmacaoSenha
            && this.form.controls['confirmacaoSenha'].touched;
    }

    salvar() {
        if (this.form.invalid) return;
        if (this.form.value.novaSenha !== this.form.value.confirmacaoSenha) {
            this.snackBar.open('Nova senha e confirmação não conferem', 'Fechar', { duration: 3000 });
            return;
        }

        const payload = {
            id: this.data.idUser,
            password: this.form.value.password,
            novaSenha: this.form.value.novaSenha,
            confirmacaoSenha: this.form.value.confirmacaoSenha
        };

        this.usuarioService.alterarSenha(payload).subscribe({
            next: () => {
                this.snackBar.open('Senha alterada com sucesso!', 'Fechar', { duration: 3000 });
                this.authService.clearPrimeiroAcesso();
                this.dialogRef.close(true);
            },
            error: (err) => {
                const msg = err.error || 'Erro ao alterar senha';
                this.snackBar.open(msg, 'Fechar', { duration: 4000 });
            }
        });
    }
}
