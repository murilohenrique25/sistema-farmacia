export interface Conta {
    idConta?: number;
    tipo: string;        // PAGAR | RECEBER
    descricao: string;
    valor: number;
    dataVencimento: string;
    dataPagamento?: string;
    status: string;      // PENDENTE | PAGO | VENCIDO | CANCELADO
    categoria?: string;
    observacao?: string;
    formaPagamento?: string; // DINHEIRO | PIX | CARTAO_DEBITO | CARTAO_CREDITO | CHEQUE | OUTROS
    idMatriz: number;
}

export interface ContaAlerta {
    idConta: number;
    tipo: string;
    descricao: string;
    valor: number;
    dataVencimento: string;
    status: string;
    diasParaVencer: number;
}
