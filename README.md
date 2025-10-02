📘 Cadastro de Usuários - API REST

Esta é uma API REST desenvolvida em Java com Spring Boot para gerenciar o cadastro de usuários. 
Ela permite criar, listar, atualizar e excluir usuários, além de validar campos
como e-mail e nome de usuário únicos.

🚀 Funcionalidades

✅ Criar novo usuário POST /api/v1/usuarios

🔍 Listar todos os usuários GET /api/v1/usuarios

📝 Atualizar dados de um usuário PUT /api/v1/usuarios/{id}

❌ Remover usuário DELETE /api/v1/usuarios/{id}

🔎 Buscar usuário por ID GET /api/v1/usuarios/{id} Exemplo: GET /api/v1/usuarios/100

🔍 Buscar usuários por nome (filtro parcial) GET /api/v1/usuarios/BuscarPorNome?nome={nome} Exemplo: GET /api/v1/usuarios/BuscarPorNome?nome=bo

📄 Buscar usuários com paginação e filtro por nome GET /api/v1/usuarios/BuscarPorNome?nome={nome}&page={page}&size={size} Exemplo: GET /api/v1/usuarios/BuscarPorNome?nome=maria&page=0&size=1

Sugestão de melhoria backend: Considere integrar o Swagger para documentar e testar a API de forma interativa e eficiente. 

🔐 Validação de campos obrigatórios e únicos

🧪 Testes Unitários: Motivação e Cobertura
Durante o desenvolvimento da aplicação, foram implementados testes unitários para classes e métodos que apresentam lógica relevante, manipulação de dados ou tratamento de exceções. A motivação principal foi garantir a confiabilidade dos principais fluxos da aplicação, especialmente:

Controller de Usuário: Validar o retorno correto de status HTTP e mensagens após operações como cadastro e atualização de dados.

Service de Usuário: Testamos o método atualizar, que contém regras de negócio e persistência, assegurando que o comportamento esperado ocorra mesmo em cenários de exceções.

Service de Email: EmailService: Validamos que os métodos responsáveis pelo envio de e-mails são corretamente acionados após operações de cadastro e atualização de usuários. Observação: O envio de e-mails é simulado apenas com saída no console, sem integração com servidores SMTP.

Mapper de Usuário: Garantimos que o mapeamento entre DTOs e entidades seja realizado corretamente, evitando inconsistências nos dados trafegados.

GlobalExceptionHandler: Cobertura completa dos métodos de tratamento de exceções, simulando diferentes erros como EntityNotFoundException, MissingServletRequestParameterException e MethodArgumentNotValidException, para assegurar respostas padronizadas e informativas ao cliente.

Teste de integração: Teste de integração verifica o comportamento real da API de usuários, simulando requisições HTTP e validando as respostas. Ele garante que os endpoints funcionam corretamente para listar, buscar, criar, atualizar e remover usuários, utilizando dados pré-carregados via scripts SQL.

Esses testes foram escolhidos por representarem pontos críticos da aplicação.

🛠️ Tecnologias Utilizadas
Java 17+ — linguagem principal do projeto

Spring Boot — framework para criação de aplicações Java modernas

Spring Data JPA — abstração para persistência de dados com JPA

Hibernate — provedor JPA padrão usado por trás do Spring Data

Spring Web — para construção de APIs RESTful

Spring WebFlux — suporte reativo (caso use WebTestClient nos testes)

Bean Validation (JSR-380) — validação de dados com anotações como @NotNull, @Email, etc.

ModelMapper — mapeamento automático entre DTOs e entidades

Lombok — geração automática de getters, setters, construtores, etc.

H2 Database — banco de dados em memória para testes e desenvolvimento

Mockito + JUnit Jupiter — testes unitários com simulação de dependências

Spring Boot DevTools — facilita o desenvolvimento com hot reload

Spring Boot Starter Test — suporte completo para testes com Spring

Maven — ferramenta de build e gerenciamento de dependências



Sugestão de front-end 

ANGULAR 2+

Angular é um framework moderno para desenvolvimento de interfaces web dinâmicas e escaláveis onde podemos criar uma aplicação front-end rica, responsiva e organizada, utilizando componentes reutilizáveis, integração nativa com APIs REST e ter o uso nativo de TypeScript.

Ao conectar o Angular a nossa API Spring Boot recém-criada, podemos separar claramente o backend da interface, permitindo que cada parte evolua de forma independente. Isso facilita a manutenção, melhora a experiência do usuário e abre espaço para recursos avançados como autenticação JWT, formulários reativos, paginação visual e dashboards interativos.

Segue uma collection no Postman para facilitar a realização de testes nos endpoints da API de forma prática e estruturada.
