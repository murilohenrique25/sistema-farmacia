import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { EntregadorService } from '../../../services/entregador.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EmpresaService } from '../../../services/empresa.service';
import { Matriz } from '../../../interfaces/matriz';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-modal-form-entregador',
  templateUrl: './modal-form-entregador.component.html',
  styleUrl: './modal-form-entregador.component.scss'
})
export class ModalFormEntregadorComponent {

  listaTurnos = [{ id: 1, descricao: 'Manhã' },
  { id: 1, descricao: 'Tarde' },
  { id: 1, descricao: 'Noite' }
  ];

  listaMatriz: Matriz[] = [];

  labelAtivo: string = 'Ativo';

  editEntregador: boolean = false;

  formEntregador: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<ModalFormEntregadorComponent>,
    private formBuilder: FormBuilder,
    private entregadorService: EntregadorService,
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
      this.editEntregador = true;
    }

    this.buildForm();

    this.formEntregador.controls['nome'].valueChanges.subscribe((nome: string) => {
      if (nome) {
        const username = this.gerarUsername(nome);
        this.formEntregador.controls['usuario'].setValue(username, { emitEvent: false });
      }
    });
  }

  saveEntregador() {
    const objUserForm = this.formEntregador.getRawValue();

    if (this.data) {
      this.entregadorService.atualizarEntregador(this.data.idEntregador, objUserForm).subscribe({
        next: () => {
          this.closeModal();
          this.snackBar.open('Entregador editado com sucesso!', 'Fechar', { duration: 3000 });
        },
        error: (erro) => {
          console.error('Erro ao editar entregador:', erro);
          this.snackBar.open('Erro ao editar o entregador. Verifique os dados e tente novamente.', 'Fechar', { duration: 3000 });
        },
      });
    } else {
      this.entregadorService.salvarEntregador(objUserForm).subscribe({
        next: () => {
          this.closeModal();
          this.snackBar.open('Entregador salvo com sucesso!', 'Fechar', { duration: 3000 });
        },
        error: (erro) => {
          console.error('Erro ao salvar entregador:', erro);
          this.snackBar.open(`'Erro ao salvar o entregador. ${erro.error.message}: ${objUserForm.usuario}`, 'Fechar', { duration: 3000 });
        },

      });
    }
  }



  buildForm() {
    this.formEntregador = this.formBuilder.group({
      nome: [null, [Validators.required, Validators.minLength(5)]],
      usuario: [null, [Validators.required]],
      turno: [null, [Validators.required]],
      telefone: [''],
      ativo: [true],
      cpf: [''],
      idMatriz: [null, [Validators.required]],
      nomeMatriz: [''],
      senha: ['user123', this.data ? '' : [Validators.required, Validators.minLength(5)]]
    });
    if (this.data && this.data.nome) {
      this.fillForm();
    }
  }

  fillForm() {
    this.formEntregador.patchValue(
      {
        nome: this.data.nome,
        usuario: this.data.usuario,
        turno: this.data.turno,
        telefone: this.data.telefone,
        ativo: this.data.ativo,
        cpf: this.data.cpf,
        senha: this.data.senha,
        idMatriz: this.data.idMatriz,
        nomeMatriz: this.data.nomeMatriz,
      }
    );
  }
  atualizarLabel() {
    this.labelAtivo = this.formEntregador.controls['ativo'].value ? 'Ativo' : 'Desativado';
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