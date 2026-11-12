import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { AuthService } from './auth.service';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService, private router: Router) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.authService.getToken();

    // Clona a requisição e adiciona o token no cabeçalho, se disponível
    const cloned = token
      ? req.clone({ headers: req.headers.set('Authorization', `Bearer ${token}`) })
      : req;

    return next.handle(cloned).pipe(
      catchError((error) => {
        if (error instanceof HttpErrorResponse) {
          // Verifica se o erro é 403 (Unauthorized)
         // if (error.status === 403) {
            // Redireciona para a tela de login
        //    this.authService.logout(); // Opcional: Limpa dados do usuário
        //    this.router.navigate(['/login']);
        //  }
        }

        // Propaga outros erros
        return throwError(() => error);
      })
    );
  }
}
