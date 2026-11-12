import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { EntregaResponse } from '../interfaces/entrega-response';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MapaEntregaService {

  private apiUrl = `${environment.apiUrl}/api/v1/entrega/mapa-intervalo`;
  constructor(private http: HttpClient) { }

  listarEntregasIntervalo(dataInicial?: any, dataFinal?: any): Observable<EntregaResponse> {

    let params = new HttpParams()
      .set('dataInicio', dataInicial)
      .set('dataTermino', dataFinal);

    return this.http.get<EntregaResponse>(this.apiUrl, { params, });
  }

}
