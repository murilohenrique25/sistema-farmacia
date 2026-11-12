export interface Usuario {
    id: number;
    username: string;
    nome: string;
    role: string;
    turno: string;
    telefone?: string;
    ativo: boolean;
    cpf?: string;
    senha?: string;
    novaSenha?: string;
    confirmacaoSenha?: string;
    idMatriz: number;
}
