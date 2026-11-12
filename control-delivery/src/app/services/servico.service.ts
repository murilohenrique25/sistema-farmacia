import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Servico } from '../interfaces/servico';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ServicoService {

  private apiUrl = `${environment.apiUrl}/api/v1/entrega/avulsas`;

  constructor(private http: HttpClient) { }

  buscarServicos(): Observable<Servico[]> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.get<Servico[]>(this.apiUrl, { headers });
  }
}
