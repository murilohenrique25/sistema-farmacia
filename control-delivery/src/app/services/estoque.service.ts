import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import {
    Produto, Lote, EntradaItem, EntradaRequest, EntradaResponse,
    SaidaRequest, SaidaResponse, Movimentacao, PosicaoEstoque, AlertaVencimento
} from '../interfaces/estoque.interfaces';

@Injectable({ providedIn: 'root' })
export class EstoqueService {
    private base = `${environment.apiUrl}/api/v1/estoque`;

    constructor(private http: HttpClient) { }

    // Produtos
    listarProdutos(): Observable<Produto[]> {
        return this.http.get<Produto[]>(`${this.base}/produto`);
    }

    criarProduto(dto: Produto): Observable<Produto> {
        return this.http.post<Produto>(`${this.base}/produto`, dto);
    }

    atualizarProduto(id: number, dto: Produto): Observable<Produto> {
        return this.http.put<Produto>(`${this.base}/produto/${id}`, dto);
    }

    excluirProduto(id: number): Observable<void> {
        return this.http.delete<void>(`${this.base}/produto/${id}`);
    }

    // Lotes
    listarLotesPorProduto(idProduto: number): Observable<Lote[]> {
        return this.http.get<Lote[]>(`${this.base}/lote/produto/${idProduto}`);
    }

    alertasVencimento(dias: number = 30): Observable<AlertaVencimento[]> {
        return this.http.get<AlertaVencimento[]>(`${this.base}/lote/alertas`, { params: { dias } });
    }

    // Movimentações
    registrarEntrada(req: EntradaRequest): Observable<EntradaResponse> {
        return this.http.post<EntradaResponse>(`${this.base}/movimentacao/entrada`, req);
    }

    previewNfe(arquivo: File): Observable<EntradaItem[]> {
        const formData = new FormData();
        formData.append('arquivo', arquivo);
        return this.http.post<EntradaItem[]>(`${this.base}/movimentacao/nfe/preview`, formData);
    }

    importarNfe(req: EntradaRequest): Observable<EntradaResponse> {
        return this.http.post<EntradaResponse>(`${this.base}/movimentacao/entrada/nfe`, req);
    }

    registrarSaida(req: SaidaRequest): Observable<SaidaResponse> {
        return this.http.post<SaidaResponse>(`${this.base}/movimentacao/saida`, req);
    }

    listarHistorico(inicio: string, fim: string, idProduto?: number): Observable<Movimentacao[]> {
        let params = new HttpParams().set('inicio', inicio).set('fim', fim);
        if (idProduto) params = params.set('idProduto', idProduto);
        return this.http.get<Movimentacao[]>(`${this.base}/movimentacao/historico`, { params });
    }

    posicaoEstoque(): Observable<PosicaoEstoque[]> {
        return this.http.get<PosicaoEstoque[]>(`${this.base}/movimentacao/posicao`);
    }

    exportarHistoricoCsv(inicio: string, fim: string): Observable<Blob> {
        const params = new HttpParams().set('inicio', inicio).set('fim', fim);
        return this.http.get(`${this.base}/movimentacao/historico/csv`, { params, responseType: 'blob' });
    }

    exportarPosicaoCsv(): Observable<Blob> {
        return this.http.get(`${this.base}/movimentacao/posicao/csv`, { responseType: 'blob' });
    }
}
