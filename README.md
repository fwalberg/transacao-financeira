# Sistema de Transações Financeiras - DIO Challenge

## Descrição do Projeto

Este projeto é um sistema de transações financeiras desenvolvido como parte do desafio DIO. O sistema simula operações bancárias básicas incluindo contas correntes, investimentos e transações entre contas.

### Funcionalidades Principais

- **Gestão de Contas**: Criação e gerenciamento de contas bancárias com chaves PIX
- **Transações Financeiras**: Depósitos, saques e transferências entre contas
- **Sistema de Investimentos**: Criação e gestão de produtos de investimento
- **Carteiras de Investimento**: Aplicação e resgate de investimentos
- **Histórico de Transações**: Auditoria completa de todas as operações
- **Atualização de Rendimentos**: Simulação de rendimentos em investimentos

### Arquitetura Atual

O projeto está organizado nos seguintes pacotes:

src/main/java/dio/desafio/
├── model/ # Entidades do domínio
│ ├── AccountWallet.java
│ ├── InvestmentWallet.java
│ ├── Investment.java
│ ├── Money.java
│ ├── MoneyAudit.java
│ └── BankService.java
├── repository/ # Camada de persistência (em memória)
│ ├── AccountRepository.java
│ ├── InvestmentRepository.java
│ └── CommonsRepository.java
├── exception/ # Exceções customizadas
└── App.java # Interface de usuário (console)

### Fluxo de Uso

1. **Criar Conta**: Configure chaves PIX e valor inicial
2. **Criar Investimento**: Defina taxa de rendimento e valor mínimo
3. **Criar Carteira de Investimento**: Associe conta a um produto de investimento
4. **Realizar Transações**: Depósitos, saques e transferências
5. **Investir/Resgatar**: Movimentação entre conta e investimentos
6. **Consultar Histórico**: Visualize todas as transações realizadas

### Tecnologias Utilizadas

- **Java 24**
- **Lombok**
- **Gradle**
- **JUnit 5**: Framework de testes (configurado, mas sem implementação)

---

## Melhorias a Implementar

### **Arquitetura e Estrutura**

#### 1. Implementar Camada de Serviços
- [ ] Criar `AccountService` para lógica de negócio de contas
- [ ] Criar `InvestmentService` para operações de investimento
- [ ] Criar `TransactionService` para coordenar transações complexas
- [ ] Mover regras de negócio dos repositories para services

#### 2. Reestruturar Pacotes
src/main/java/dio/desafio/
├── controller/ # Controladores/Handlers
├── service/ # Lógica de negócio
├── repository/ # Acesso a dados
├── model/
│ ├── entity/ # Entidades de domínio
│ └── dto/ # Data Transfer Objects
├── config/ # Configurações
├── exception/ # Exceções customizadas
└── util/ # Utilitários

#### 3. Implementar Injeção de Dependência
- [ ] Migrar para Spring Framework
- [ ] Eliminar dependências hardcoded
- [ ] Implementar Factory patterns para criação de objetos

### **Persistência e Dados**

#### 4. Corrigir Modelo de Money
- [ ] Substituir `List<Money>` por `BigDecimal` ou classe `Money` com valor numérico

#### 5. Implementar Persistência Real
- [ ] Adicionar suporte a banco de dados (H2 ou MaraiaDB)
- [ ] Implementar JPA/Hibernate

#### 6. Implementar Padrão Repository adequado
- [ ] Criar interfaces Repository
- [ ] Implementar Repository pattern corretamente
- [ ] Separar concerns de persistência e lógica de negócio

### **Segurança e Validações**

#### 7. Implementar Validações Robustas
- [ ] Validação de formato de chaves PIX
- [ ] Validação de valores monetários
- [ ] Validação de regras de negócio
- [ ] Usar Bean Validation (JSR-303)

#### 8. Adicionar Tratamento de Concorrência
- [ ] Implementar locks para operações críticas
- [ ] Usar estruturas de dados thread-safe
- [ ] Implementar transações ACID

#### 9. Implementar Logging
- [ ] Adicionar SLF4J + Logback
- [ ] Log de todas as operações financeiras
- [ ] Diferentes níveis de log (DEBUG, INFO, WARN, ERROR)

### **Qualidade de Código**

#### 10. Implementar Testes
- [ ] Testes unitários para todas as classes
- [ ] Testes de integração

#### 11. Refatorar Interface de Usuário
- [ ] Separar lógica de apresentação
- [ ] Implementar Command pattern para operações
- [ ] Adicionar validação de entrada do usuário
- [ ] Melhorar experiência do usuário

#### 12. Implementar Design Patterns
- [ ] Strategy pattern para diferentes tipos de investimento
- [ ] Observer pattern para notificações
- [ ] Builder pattern para criação de objetos complexos
- [ ] Facade pattern para simplificar APIs

### **Funcionalidades Adicionais**

#### 13. Melhorar Sistema de Auditoria
- [ ] Implementar rastreabilidade completa
- [ ] Adicionar timestamps mais precisos

#### 14. Implementar Configurações Externalizadas
- [ ] Arquivo de propriedades para configurações
- [ ] Configuração de taxas e limites externalizadas

###  **Priorização das Melhorias**

#### **Alta Prioridade (Crítico)**
1. Implementar testes unitários
2. Corrigir modelo de Money
3. Adicionar validações básicas
4. Implementar camada de serviços

####  **Média Prioridade (Importante)**
1. Separar camadas adequadamente
2. Implementar persistência real
3. Adicionar logging
4. Melhorar tratamento de erros

#### **Baixa Prioridade (Desejável)**
1. Containerização
2. Pipeline CI/CD
3. Métricas e monitoramento
4. Interface web

---

## Contribuindo

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request