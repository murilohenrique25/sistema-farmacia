import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormaPagamento } from '../interfaces/forma-pagamento';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class FormaPagamentoService {

  private apiUrl = `${environment.apiUrl}/api/v1/forma-pagamento`;
  constructor(private http: HttpClient) { }


  carregarTodasFormasPagamento(): Observable<FormaPagamento[]> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.get<FormaPagamento[]>(this.apiUrl, { headers });
  }

  carregarTodasFormasPagamentoAtivos(): Observable<FormaPagamento[]> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.get<FormaPagamento[]>(`${this.apiUrl}/ativos`, { headers });
  }

  salvarFormaPagamento(dadosSalvar: any): Observable<void> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<void>(`${this.apiUrl}`, dadosSalvar, { headers });
  }

  editarFormaPagamento(idFormaPagamento: any, dadosSalvar: any): Observable<void> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put<void>(`${this.apiUrl}/alterar/${idFormaPagamento}`, dadosSalvar, { headers });
  }
}
