export interface Entregador {
    idEntregador: number;
    usuario: string;
    nome: string;
    turno: string;
    idMatriz: number;
    telefone?: string;
    nomeMatriz?: string;
    ativo: boolean;
    cpf?: string;
    senha?: string;
}
