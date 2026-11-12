import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Permissao {
    idPermissao: number;
    chave: string;
    descricao: string;
}

@Injectable({
    providedIn: 'root'
})
export class PermissaoService {

    private apiUrl = `${environment.apiUrl}/api/v1/permissao`;

    constructor(private http: HttpClient) { }

    listarTodas(): Observable<Permissao[]> {
        return this.http.get<Permissao[]>(this.apiUrl);
    }

    buscarPorUsuario(idUsuario: number): Observable<Permissao[]> {
        return this.http.get<Permissao[]>(`${this.apiUrl}/usuario/${idUsuario}`);
    }

    vincularPermissoes(idUsuario: number, idsPermissoes: number[]): Observable<void> {
        const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
        return this.http.put<void>(`${this.apiUrl}/usuario/${idUsuario}`, idsPermissoes, { headers });
    }
}
