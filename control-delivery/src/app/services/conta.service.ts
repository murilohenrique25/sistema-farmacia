import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Conta, ContaAlerta } from '../interfaces/conta';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ContaService {

    private apiUrl = `${environment.apiUrl}/api/v1/conta`;

    constructor(private http: HttpClient) { }

    listar(tipo: string): Observable<Conta[]> {
        return this.http.get<Conta[]>(this.apiUrl, { params: { tipo } });
    }

    listarPorPeriodo(tipo: string, inicio: string, fim: string): Observable<Conta[]> {
        const params = new HttpParams().set('tipo', tipo).set('inicio', inicio).set('fim', fim);
        return this.http.get<Conta[]>(`${this.apiUrl}/periodo`, { params });
    }

    salvar(conta: Conta): Observable<Conta> {
        return this.http.post<Conta>(this.apiUrl, conta);
    }

    atualizar(id: number, conta: Conta): Observable<Conta> {
        return this.http.put<Conta>(`${this.apiUrl}/${id}`, conta);
    }

    baixar(id: number): Observable<Conta> {
        return this.http.put<Conta>(`${this.apiUrl}/${id}/baixar`, {});
    }

    cancelar(id: number): Observable<Conta> {
        return this.http.put<Conta>(`${this.apiUrl}/${id}/cancelar`, {});
    }

    buscarAlertas(dias: number = 7): Observable<ContaAlerta[]> {
        return this.http.get<ContaAlerta[]>(`${this.apiUrl}/alertas`, { params: { dias } });
    }
}
