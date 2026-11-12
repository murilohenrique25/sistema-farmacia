import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { AuthService } from '../../services/auth.service';
import { ModalAlterarSenhaComponent } from '../../components/modal-alterar-senha/modal-alterar-senha.component';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  username: string = '';
  password: string = '';

  constructor(
    private authService: AuthService,
    private route: Router,
    private dialog: MatDialog
  ) { }

  login() {
    this.authService.login(this.username, this.password).subscribe(
      () => {
        if (this.authService.isPrimeiroAcesso()) {
          this.abrirModalAlterarSenha();
        } else {
          this.route.navigate(['home']);
        }
      },
      (error) => {
        console.log(error);
        alert('Usuário ou senha inválidos');
      }
    );
  }

  private abrirModalAlterarSenha() {
    const idUser = Number(this.authService.getUserId());
    this.dialog.open(ModalAlterarSenhaComponent, {
      width: '400px',
      maxWidth: '95vw',
      disableClose: true,
      data: { idUser }
    }).afterClosed().subscribe((alterou) => {
      if (alterou) {
        this.route.navigate(['home']);
      }
    });
  }
}
