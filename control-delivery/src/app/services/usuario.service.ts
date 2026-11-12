import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Usuario } from '../interfaces/usuario';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private apiUrl = `${environment.apiUrl}/api/v1/usuario`;

  constructor(private http: HttpClient) { }

  buscarUsuarios(): Observable<Usuario[]> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.get<Usuario[]>(this.apiUrl, { headers });
  }

  buscarUsuario(id: any): Observable<Usuario> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.get<Usuario>(`${this.apiUrl}/${id}`, { headers });
  }

  salvarUsuario(objUserForm: any): Observable<Usuario> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<Usuario>(
      this.apiUrl, objUserForm, { headers }
    );
  }

  atualizarUsuario(id: number, objUpdateUser: any) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    objUpdateUser.novaSenha = objUpdateUser.password;
    return this.http.put<Usuario>(
      `${this.apiUrl}/${id}`, // URL com o ID do usuário
      objUpdateUser,          // Dados enviados no body
      { headers }             // Configurações adicionais
    );
  }

  alterarSenha(payload: { id: number; password: string; novaSenha: string; confirmacaoSenha: string }): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put(`${this.apiUrl}/alterar-senha`, payload, { headers, responseType: 'text' });
  }
}
