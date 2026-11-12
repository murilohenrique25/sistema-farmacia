package br.com.starkstecnologia.control_api.config;

import br.com.starkstecnologia.control_api.entity.Permissao;
import br.com.starkstecnologia.control_api.repository.PermissaoRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

//mvn package -P prod
@Component
public class PermissaoDataInitializer implements ApplicationRunner {

    private final PermissaoRepository permissaoRepository;

    public PermissaoDataInitializer(PermissaoRepository permissaoRepository) {
        this.permissaoRepository = permissaoRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        List<Permissao> permissoesDefault = List.of(
                new Permissao("ACESSAR_ENTREGADORES", "Acessar tela de Entregadores"),
                new Permissao("ACESSAR_VENDEDORES", "Acessar tela de Vendedores"),
                new Permissao("ACESSAR_RELATORIOS", "Acessar Relatórios"),
                new Permissao("ACESSAR_MAPA", "Acessar Mapa de Entregas"),
                new Permissao("ACESSAR_FORMA_PAGAMENTO", "Acessar Formas de Pagamento"),
                new Permissao("ACESSAR_MATRIZ", "Acessar Configuração de Matriz"),
                new Permissao("ACESSAR_SERVICOS", "Acessar Serviços"),
                new Permissao("INSERIR_ENTREGA", "Inserir Entrega"),
                new Permissao("ASSINAR_ENTREGA", "Assinar Entregas"),
                new Permissao("ACESSAR_ENTREGAS", "Acessar tela de Entregas"),
                new Permissao("ACESSAR_CONTAS_PAGAR", "Acessar Contas a Pagar"),
                new Permissao("ACESSAR_CONTAS_RECEBER", "Acessar Contas a Receber"),
                new Permissao("GERENCIAR_ENTREGAS", "Gerenciar Entregas - Dashboard completo"),
                new Permissao("ACESSAR_ESTOQUE", "Acessar módulo de Estoque"),
                new Permissao("GERENCIAR_ESTOQUE", "Gerenciar Estoque - criar, editar, excluir e movimentar"));

        for (Permissao p : permissoesDefault) {
            if (!permissaoRepository.existsByChave(p.getChave())) {
                permissaoRepository.save(p);
            }
        }
    }
}
