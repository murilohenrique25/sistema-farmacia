import { MapaEntrega } from "./mapa-entrega";

export interface EntregaResponse {

    entregas: MapaEntrega[];
    quantidadePorData: { [key: string]: number };
}
