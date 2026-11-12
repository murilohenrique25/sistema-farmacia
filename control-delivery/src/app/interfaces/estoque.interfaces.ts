export interface Produto {
    idProduto?: number;
    nome: string;
    codigo: string;
    categoria?: string;
    unidadeMedida: string;
    status?: string;
    idMatriz: number;
}

export interface Lote {
    idLote?: number;
    idProduto: number;
    nomeProduto?: string;
    numeroLote: string;
    quantidadeAtual: number;
    precoCusto: number;
    dataEntrada: string;
    dataValidade?: string;
    status?: string; // ATIVO | ESGOTADO | VENCIDO | PROXIMO_VENCIMENTO
}

export interface EntradaItem {
    idProduto: number;
    numeroLote: string;
    quantidade: number;
    precoCusto: number;
    dataEntrada: string;
    dataValidade?: string;
}

export interface EntradaRequest { itens: EntradaItem[]; }
export interface EntradaResponse { quantidadeItensProcessados: number; idLotesCriadosOuAtualizados: number[]; }

export interface SaidaRequest { idLote: number; quantidade: number; motivo: string; }
export interface SaidaResponse { idLote: number; quantidadeAtual: number; }

export interface Movimentacao {
    idMovimentacao: number;
    idLote: number;
    numeroLote: string;
    idProduto: number;
    nomeProduto: string;
    tipo: string;
    quantidade: number;
    precoCusto: number;
    dataHora: string;
    idUsuarioResponsavel: number;
    motivo?: string;
    origem: string;
}

export interface PosicaoEstoque {
    idProduto: number;
    nomeProduto: string;
    codigo: string;
    unidadeMedida: string;
    quantidadeTotalAtiva: number;
    numeroDeLotesAtivos: number;
    dataValidadeMaisProxima?: string;
}

export interface AlertaVencimento {
    idLote: number;
    numeroLote: string;
    idProduto: number;
    nomeProduto: string;
    quantidadeAtual: number;
    dataValidade: string;
    statusAlerta: string;
}
