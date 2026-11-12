export interface Entrega {
    idEntrega: number;
    dataCadastroEntrega?: string;
    dataAssinaturaEntrega?: string;
    dataSelecaoEntrega?: string;
    dataFinalizacaoEntrega?: string;
    idUser?: number;
    idUserAssinatura?: number;
    nomeUser?: string;
    nomeUserAssinatura?: string;
    idEntregador?: number;
    nomeEntregador?: string;
    latitude?: string;
    longitude?: string;
    valorTotal?: number;
    valorReceber?: number;
    troco?: number;
    cupomOrcamento?: string;
    tipoEntrega?: string;
    formaPagamento?: string;
    observacao?: string;
    statusEntrega?: string;
    idMatriz?: number;
    telefone?:string;
}
