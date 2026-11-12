package br.com.starkstecnologia.control_api.types;

public enum StatusEntregaEnum {

    ABERTA("Aberta"),
    EM_ROTA("Em rota"),
    SELECIONADA("Selecionada"),
    CANCELADA("Cancelada"),
    FINALIZADA("Finalizada"),
    NAO_ENTREGUE("Não Entregue"),
    FINALIZADA_MANUAL("Finalizada Manualmente"),
    FINALIZADA_ASSINADA("Finalizada e Assinada");

    private final String descricao;

    public static StatusEntregaEnum fromDescricao(StatusEntregaEnum descricao) {
        for (StatusEntregaEnum status : StatusEntregaEnum.values()) {
            if (status.equals(descricao)) {
                return status;
            }
        }
        // Retornar nulo ou lançar uma exceção caso a descrição não seja encontrada
        throw new IllegalArgumentException("Status desconhecido: " + descricao);
    }

    StatusEntregaEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
