import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Matriz } from '../interfaces/matriz';
import { Filial } from '../interfaces/filial';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EmpresaService {

  private apiUrl = `${environment.apiUrl}/api/v1/empresa`;

  constructor(private http: HttpClient) { }

  salvarMatriz(matriz: any): Observable<Matriz> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<Matriz>(`${this.apiUrl}/matriz`, matriz, { headers });
  }

  buscarMatrizes(): Observable<Matriz[]> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.get<Matriz[]>(`${this.apiUrl}/matriz`, { headers });
  }

  buscarMatriz(id: any): Observable<Matriz> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.get<Matriz>(`${this.apiUrl}/${id}`, { headers });
  }

  salvarFilial(filial: any): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(`${this.apiUrl}/filial`, filial, { headers });
  }

  buscarFiliais(idMatriz: any): Observable<Filial[]> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.get<Filial[]>(`${this.apiUrl}/filial/${idMatriz}`, { headers });
  }

}
