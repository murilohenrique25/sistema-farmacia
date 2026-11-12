export interface EntregaAssinar {
    idEntrega: number;
    dataFinalizacaoEntrega: string;
    nomeUser: string;
    nomeEntregador: string;
    cupomOrcamento: string;
    statusEntrega: string;
    selecionada?: boolean;
}
