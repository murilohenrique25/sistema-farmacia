import { Component } from '@angular/core';
import { Matriz } from '../../interfaces/matriz';
import { Usuario } from '../../interfaces/usuario';
import { EmpresaService } from '../../services/empresa.service';
import { AuthService } from '../../services/auth.service';
import { UsuarioService } from '../../services/usuario.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-matriz',
  templateUrl: './matriz.component.html',
  styleUrl: './matriz.component.scss'
})
export class MatrizComponent {

  matriz: Matriz;
  usuario: Usuario;

  permiteAlterarDados: boolean = true;
  constructor(
    private empresaService: EmpresaService,
    private authService: AuthService,
    private snackBar: MatSnackBar,
    private usuarioService: UsuarioService) { }

  ngOnInit() {
    this.usuarioService.buscarUsuario(this.authService.getUserId()).subscribe({
      next: (response) => {
        this.usuario = response;
        this.permiteAlterarDados = this.usuario.role === 'Administrador' ? false : true;

        this.empresaService.buscarMatriz(this.usuario.idMatriz).subscribe({
          next: (res) => {
            this.matriz = res;
          },
          error: (erro) => {
            console.error("Erro ao buscar empresa", erro);
          }
        });
      },
      error: (erro) => {
        console.error("Erro ao obter usuario logado ", erro);
      }
    });


  }

  salvar() {
    this.empresaService.salvarMatriz(this.matriz).subscribe({
      next: (res) => {
        this.snackBar.open("Dados da empresa salvo com sucesso!", "Fechar", { duration: 3000 });
      },
      error: (erro) => {
        this.snackBar.open("Ocorreu um erro ao salvar", "Fechar", { duration: 3000 });
      }
    });
  };
}
