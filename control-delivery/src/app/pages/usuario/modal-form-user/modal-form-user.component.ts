import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { UsuarioService } from '../../../services/usuario.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Matriz } from '../../../interfaces/matriz';
import { EmpresaService } from '../../../services/empresa.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-modal-form-user',
  templateUrl: './modal-form-user.component.html',
  styleUrl: './modal-form-user.component.scss'
})
export class ModalFormUserComponent {

  listaTurnos = [{ id: 1, descricao: 'Manhã' },
  { id: 1, descricao: 'Tarde' },
  { id: 1, descricao: 'Noite' }
  ];

  labelAtivo: string = 'Ativo';

  editUser: boolean = false;

  listaMatriz: Matriz[] = [];

  formUser: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<ModalFormUserComponent>,
    private formBuilder: FormBuilder,
    private usuarioService: UsuarioService,
    private matrizService: EmpresaService,
    private snackBar: MatSnackBar,
    public authService: AuthService,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) { }

  ngOnInit() {
    this.matrizService.buscarMatrizes().subscribe({
      next: (res) => {
        this.listaMatriz = res;
      }, error: (error) => {
        console.error("Erro ao buscar matrizes", error);
      }
    });

    if (this.data) {
      this.editUser = true;
    }
    this.buildForm();

    this.formUser.controls['nome'].valueChanges.subscribe((nome: string) => {
      if (nome) {
        const username = this.gerarUsername(nome);
        this.formUser.controls['username'].setValue(username, { emitEvent: false });
      }
    });
  }

  saveUser() {
    const objUserForm = this.formUser.getRawValue();

    if (this.data) {
      this.usuarioService.atualizarUsuario(this.data.id, objUserForm).subscribe({
        next: () => {
          this.closeModal();
          this.snackBar.open('Usuário editado com sucesso!', 'Fechar', { duration: 3000 });
        },
        error: (erro) => {
          console.error('Erro ao editar usuário:', erro);
          this.snackBar.open('Erro ao editar o usuário. Verifique os dados e tente novamente.', 'Fechar', { duration: 3000 });
        },
      });
    } else {
      this.usuarioService.salvarUsuario(objUserForm).subscribe({
        next: () => {
          this.closeModal();
          this.snackBar.open('Usuário salvo com sucesso!', 'Fechar', { duration: 3000 });
        },
        error: (erro) => {
          console.error('Erro ao salvar usuário:', erro);
          this.snackBar.open('Erro ao salvar o usuário. Verifique os dados e tente novamente.', 'Fechar', { duration: 3000 });
        },

      });
    }
  }

  buildForm() {
    this.formUser = this.formBuilder.group({
      nome: [null, [Validators.required, Validators.minLength(5)]],
      username: [null, [Validators.required]],
      turno: [null, [Validators.required]],
      telefone: [''],
      ativo: [true],
      idMatriz: [null, [Validators.required]],
      cpf: [''],
      password: ['user123']
    });
    if (this.data && this.data.nome) {
      this.fillForm();
    }
  }

  fillForm() {
    this.formUser.patchValue(
      {
        nome: this.data.nome,
        username: this.data.username,
        turno: this.data.turno,
        telefone: this.data.telefone,
        ativo: this.data.ativo,
        idMatriz: this.data.idMatriz,
        cpf: this.data.cpf,
        password: this.data.password,
      }
    );
  }

  atualizarLabel() {
    this.labelAtivo = this.formUser.controls['ativo'].value ? 'Ativo' : 'Desativado';
  }

  gerarUsername(nome: string): string {
    const nomes = nome.trim().split(' ');
    if (nomes.length > 1) {
      return `${nomes[0].toLowerCase()}.${nomes[nomes.length - 1].toLowerCase()}`;
    }
    return nomes[0].toLowerCase();
  }

  closeModal() { this.dialogRef.close(); }

  isAdmin(): boolean {
    return this.authService.getRole() === 'Administrador';
  }
}
