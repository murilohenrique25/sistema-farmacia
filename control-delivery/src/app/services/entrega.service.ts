import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { EntregaAssinar } from '../interfaces/entrega-assinar';
import { Entrega } from '../interfaces/entrega';
import { EntregadorMes } from '../interfaces/entregador-mes';
import { DadosEntrega } from '../interfaces/dados-entrega';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EntregaService {

  private apiUrl = `${environment.apiUrl}/api/v1/entrega`;
  constructor(private http: HttpClient) { }

  listarTodasEntregas(dataInicial?: any, dataFinal?: any, statusEntrega?: string[],
    idsEntregador?: number[]): Observable<Entrega[]> {

    if (dataFinal != null) {
      let params = new HttpParams()
        .set('dataInicio', dataInicial)
        .set('dataTermino', dataFinal);

      if (statusEntrega && statusEntrega.length > 0) {
        params = params.set('statusEntrega', statusEntrega.join(','));
      }

      if (idsEntregador && idsEntregador.length > 0) {
        params = params.set('idsEntregador', idsEntregador.join(','));
      }
      return this.http.get<Entrega[]>(this.apiUrl, { params, });
    } else {
      return this.http.get<Entrega[]>(this.apiUrl);
    }
  }

  entregadorMes(): Observable<EntregadorMes> {
    return this.http.get<EntregadorMes>(`${this.apiUrl}/entregador-mes`);
  }

  carregarEntregasAssinar(
    dataInicio: string,
    dataFim: string,
    idsEntregador?: number[]
  ): Observable<EntregaAssinar[]> {

    // Configurar parâmetros da requisição
    let params = new HttpParams()
      .set('dataInicio', dataInicio)
      .set('dataFim', dataFim);

    if (idsEntregador && idsEntregador.length > 0) {
      params = params.set('idsEntregador', idsEntregador.join(','));
    }

    return this.http.get<EntregaAssinar[]>(`${this.apiUrl}/entregas-assinar/${dataInicio}/${dataFim}`,
      {
        params,
      });
  }

  assinarEntregas(entregaIds: number[], idUsuario: any): Observable<void> {
    const url = `${this.apiUrl}/assinar/${idUsuario}`;
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    return this.http.put<void>(url, entregaIds, { headers });
  }

  salvarEntrega(objectSalvar: any) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<Entrega>(
      this.apiUrl, objectSalvar, { headers }
    );
  }

  atualizarEntrega(id: Number, objUpdateEntrega: any) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put(`${this.apiUrl}/alterar/${id}`, objUpdateEntrega, { headers });
  }

  atribuirEntrega(idEntrega: number, object: Object) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put(`${this.apiUrl}/atribuir/${idEntrega}`, object, { headers });
  }

  cancelarEntregar(idEntrega: number) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put(`${this.apiUrl}/cancelar/${idEntrega}`, null, { headers });
  }

  finalizarEntregar(idEntrega: number) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put(`${this.apiUrl}/finalizar/${idEntrega}`, { headers });
  }

  carregarDadosTelaInicial(): Observable<DadosEntrega> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    return this.http.get<DadosEntrega>(this.apiUrl + '/total-entregas', { headers });
  }

  alterarEntrega(idEntrega: number, idEntregador: number,) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put(`${this.apiUrl}/alterar/${idEntrega}/entregador/${idEntregador}`, { headers });
  }
}
