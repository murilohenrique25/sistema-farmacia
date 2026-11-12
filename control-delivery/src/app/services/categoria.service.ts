import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Categoria {
    idCategoria?: number;
    descricao: string;
    tipo: string; // PAGAR | RECEBER
    ativo: boolean;
}

@Injectable({ providedIn: 'root' })
export class CategoriaService {

    private apiUrl = `${environment.apiUrl}/api/v1/categoria`;

    constructor(private http: HttpClient) { }

    listarTodas(): Observable<Categoria[]> {
        return this.http.get<Categoria[]>(this.apiUrl);
    }

    listarPorTipo(tipo: string): Observable<Categoria[]> {
        return this.http.get<Categoria[]>(`${this.apiUrl}/tipo/${tipo}`);
    }

    salvar(categoria: Categoria): Observable<Categoria> {
        return this.http.post<Categoria>(this.apiUrl, categoria);
    }

    atualizar(id: number, categoria: Categoria): Observable<Categoria> {
        return this.http.put<Categoria>(`${this.apiUrl}/${id}`, categoria);
    }

    excluir(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}
