# Plano de Implementação: selecionar-iniciar-entrega

## Visão Geral

Separar o fluxo de seleção e início de entrega em duas etapas distintas: `PUT /selecionar/{userId}` passa a gravar status `SELECIONADA`, e o novo `PUT /iniciar/{userId}` grava coordenadas GPS e status `EM_ROTA`.

## Tarefas

- [x] 1. Criar o DTO `IniciarEntregaDTO`
  - Criar `src/main/java/br/com/starkstecnologia/control_api/dto/IniciarEntregaDTO.java`
  - Campos: `List<Long> idsEntrega` com `@NotNull`, `String latitude` com `@NotBlank`, `String longitude` com `@NotBlank`
  - Incluir getters e setters (padrão do projeto — sem Lombok ou com Lombok conforme convenção existente)
  - _Requirements: 2.1, 2.6_

- [x] 2. Modificar `EntregaService.selecionarAtribuirAlterarEntrega` (sobrecarga com `List<DadosEntregaDTO>`)
  - [x] 2.1 Trocar `StatusEntregaEnum.EM_ROTA` por `StatusEntregaEnum.SELECIONADA` no loop da sobrecarga que recebe `List<DadosEntregaDTO>`
    - Arquivo: `src/main/java/br/com/starkstecnologia/control_api/services/EntregaService.java`
    - Apenas a sobrecarga `selecionarAtribuirAlterarEntrega(List<DadosEntregaDTO>, String)` deve ser alterada
    - A sobrecarga `selecionarAtribuirAlterarEntrega(DadosAtribuirFinalizarEntregaDTO, Long)` NÃO deve ser alterada
    - _Requirements: 1.1, 1.2, 1.3, 3.1, 3.2_

  - [ ]* 2.2 Escrever teste de propriedade para `selecionarAtribuirAlterarEntrega` — Propriedade 1
    - **Property 1: Seleção grava status SELECIONADA**
    - Gerar lista aleatória de IDs de entrega + userId válido → verificar `statusEntrega == "Selecionada"` para todas as entregas
    - Usar jqwik: `@Property(tries = 100)`
    - Comentário obrigatório: `// Feature: selecionar-iniciar-entrega, Property 1: Seleção grava status SELECIONADA`
    - **Validates: Requirements 1.1, 3.2**

  - [ ]* 2.3 Escrever teste de propriedade para `selecionarAtribuirAlterarEntrega` — Propriedade 2
    - **Property 2: Seleção grava dataSelecaoEntrega e associa entregador**
    - Gerar lista aleatória + userId válido → verificar `dataSelecaoEntrega != null` e `entregador` associado corresponde ao userId
    - Usar jqwik: `@Property(tries = 100)`
    - Comentário obrigatório: `// Feature: selecionar-iniciar-entrega, Property 2: Seleção grava dataSelecaoEntrega e associa entregador`
    - **Validates: Requirements 1.2, 1.3**

- [x] 3. Adicionar método `iniciarEntrega` no `EntregaService`
  - [x] 3.1 Implementar `iniciarEntrega(List<Long> idsEntrega, String userId, String latitude, String longitude)` em `EntregaService`
    - Validar lista nula/vazia: retornar sem alterações
    - Buscar `Entregador` via `entregadorRepository.findEntregadorByUsuario(userId)`; lançar `ServiceException` se nulo
    - Buscar entregas via `entregaRepository.buscarEntregasPorId(idsEntrega)`
    - Para cada entrega: validar `statusEntrega == SELECIONADA`, lançar `ServiceException` com mensagem descritiva se inválido
    - Gravar `latitude`, `longitude` e `statusEntrega = EM_ROTA` em cada entrega
    - Anotar com `@Transactional`
    - _Requirements: 2.2, 2.3, 2.4, 2.5, 2.7_

  - [ ]* 3.2 Escrever teste de propriedade para `iniciarEntrega` — Propriedade 3
    - **Property 3: Iniciar entrega — transição de status e gravação de coordenadas**
    - Gerar lista de entregas com status SELECIONADA + coordenadas aleatórias → verificar `statusEntrega == "Em rota"`, `latitude` e `longitude` gravadas com os valores fornecidos
    - Usar jqwik: `@Property(tries = 100)`
    - Comentário obrigatório: `// Feature: selecionar-iniciar-entrega, Property 3: Iniciar entrega — transição de status e gravação de coordenadas`
    - **Validates: Requirements 2.2, 2.3, 2.4**

  - [ ]* 3.3 Escrever teste de propriedade para `iniciarEntrega` — Propriedade 4
    - **Property 4: Rejeição de entrega com status inválido no iniciar**
    - Gerar lista com ao menos uma entrega cujo `statusEntrega` seja diferente de `SELECIONADA` → verificar que `ServiceException` é lançada e nenhuma entrega é alterada
    - Usar jqwik: `@Property(tries = 100)`
    - Comentário obrigatório: `// Feature: selecionar-iniciar-entrega, Property 4: Rejeição de entrega com status inválido no iniciar`
    - **Validates: Requirements 2.5**

  - [ ]* 3.4 Escrever teste de propriedade para `iniciarEntrega` — Propriedade 5
    - **Property 5: userId inválido lança ServiceException**
    - Gerar userId aleatório não cadastrado → verificar que `ServiceException` é lançada em `iniciarEntrega`
    - Usar jqwik: `@Property(tries = 100)`
    - Comentário obrigatório: `// Feature: selecionar-iniciar-entrega, Property 5: userId inválido lança ServiceException`
    - **Validates: Requirements 1.6, 2.7**

- [x] 4. Checkpoint — Verificar lógica de serviço
  - Garantir que todos os testes passam. Perguntar ao usuário se houver dúvidas antes de prosseguir.

- [x] 5. Adicionar endpoint `PUT /iniciar/{userId}` no `EntregaAppEndpoint`
  - [x] 5.1 Adicionar método `iniciarEntrega` no `EntregaAppEndpoint`
    - Arquivo: `src/main/java/br/com/starkstecnologia/control_api/web/controller/app/EntregaAppEndpoint.java`
    - Anotação `@PutMapping("/iniciar/{userId}")`
    - Parâmetros: `@Valid @RequestBody IniciarEntregaDTO dto`, `@PathVariable("userId") String userId`
    - Chamar `entregaService.iniciarEntrega(dto.getIdsEntrega(), userId, dto.getLatitude(), dto.getLongitude())`
    - Retornar `ResponseEntity.ok().build()`
    - Adicionar import de `IniciarEntregaDTO` e `jakarta.validation.Valid`
    - _Requirements: 2.1, 2.6_

  - [ ]* 5.2 Escrever testes unitários para o endpoint `PUT /iniciar/{userId}`
    - Testar HTTP 400 quando `latitude` está em branco
    - Testar HTTP 400 quando `longitude` está em branco
    - Testar HTTP 400 quando `idsEntrega` é nulo
    - Testar HTTP 200 com body válido e status SELECIONADA
    - _Requirements: 2.1, 2.6_

  - [ ]* 5.3 Escrever teste unitário de retrocompatibilidade do endpoint `PUT /selecionar/{userId}`
    - Verificar que o endpoint retorna HTTP 200 após a alteração de comportamento
    - _Requirements: 3.1, 3.3_

- [x] 6. Checkpoint final — Garantir que todos os testes passam
  - Garantir que todos os testes passam. Perguntar ao usuário se houver dúvidas.

## Notas

- Tarefas marcadas com `*` são opcionais e podem ser puladas para um MVP mais rápido
- Cada tarefa referencia requisitos específicos para rastreabilidade
- Os testes de propriedade usam jqwik (`net.jqwik:jqwik`) — verificar se a dependência já está no `pom.xml` antes de implementar
- Nenhuma migration de banco é necessária (campos `latitude`, `longitude`, `statusEntrega` já existem na entidade `Entrega`)
