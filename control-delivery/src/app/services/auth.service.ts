import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/api/v1/auth`;

  constructor(private http: HttpClient) { }

  login(username: string, password: string): Observable<any> {
    const body = { username, password };
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    return this.http.post<any>(`${this.apiUrl}`, body, { headers }).pipe(
      tap(response => {
        localStorage.setItem('token', response.token);
        localStorage.setItem('nome', response.nome);
        localStorage.setItem('idUser', response.idUser);
        localStorage.setItem('role', response.role ?? '');
        localStorage.setItem('permissoes', JSON.stringify(response.permissoes ?? []));
        localStorage.setItem('primeiroAcesso', response.primeiroAcesso ? 'true' : 'false');
      })
    );
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('nome');
    localStorage.removeItem('idUser');
    localStorage.removeItem('role');
    localStorage.removeItem('permissoes');
    localStorage.removeItem('primeiroAcesso');
  }

  isLoggedIn(): boolean {
    return localStorage.getItem('token') !== null;
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getUserName(): string | null {
    return localStorage.getItem('nome');
  }

  getUserId(): string | null {
    return localStorage.getItem('idUser');
  }

  getRole(): string | null {
    return localStorage.getItem('role');
  }

  getPermissoes(): string[] {
    const raw = localStorage.getItem('permissoes');
    return raw ? JSON.parse(raw) : [];
  }

  temPermissao(chave: string): boolean {
    return this.getPermissoes().includes(chave);
  }

  isPrimeiroAcesso(): boolean {
    return localStorage.getItem('primeiroAcesso') === 'true';
  }

  clearPrimeiroAcesso() {
    localStorage.setItem('primeiroAcesso', 'false');
  }
}
