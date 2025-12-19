# Sistema Distribuído de Pedidos e Pagamentos  
### Quiosque de Praça de Alimentação com Múltiplos Pontos de Pagamento

O sistema foi desenvolvido em **Java**, utilizando **sockets TCP**, e segue o modelo arquitetural **Cliente-Servidor**.

---

## 📌 Descrição Geral

O sistema permite que diferentes pontos de pagamento realizem pedidos simultaneamente para um único quiosque. Cada ponto de pagamento se conecta a um servidor central, responsável por:

- Gerenciar o cardápio
- Processar pedidos
- Calcular o valor total
- Solicitar confirmação do pagamento
- Liberar ou cancelar o pedido

---

## 🏗️ Arquitetura do Sistema

- **Modelo Arquitetural:** Cliente-Servidor  
- **Servidor Central:**  
  - Mantém o cardápio
  - Processa pedidos
  - Gerencia pagamentos
  - Atende múltiplos clientes simultaneamente por meio de threads
- **Clientes (Pontos de Pagamento):**  
  - Representam terminais utilizados por atendentes
  - Enviam pedidos ao servidor
  - Confirmam ou cancelam pagamentos

A comunicação é **síncrona, bidirecional e confiável**, utilizando o protocolo **TCP**.

---

## 🔧 Implementação

### Tecnologias Utilizadas

- Linguagem: **Java**
- Comunicação: **Sockets TCP**
- Concorrência: **ExecutorService (Pool de Threads)**
- Entrada e saída de dados: `BufferedReader` e `PrintWriter`

### Estrutura do Cardápio

O cardápio é mantido no servidor e composto por itens identificados por códigos numéricos:

| Código | Item            | Preço (R$) |
|------|-----------------|------------|
| 1    | Batata frita    | 8,00       |
| 2    | Refrigerante    | 5,00       |
| 3    | Hambúrguer      | 15,00      |
| 4    | Sorvete         | 6,00       |

### Protocolo de Comunicação

Para garantir a leitura correta de mensagens com múltiplas linhas, foi definido um protocolo simples baseado em texto, utilizando marcadores de início e fim:

- `CARDAPIO` / `FIM_CARDAPIO`
- `RESUMO_PEDIDO` / `FIM_RESUMO`


---

## 🔄 Funcionamento do Sistema

1. O servidor é iniciado e permanece aguardando conexões.
2. Um ponto de pagamento se conecta ao servidor.
3. O servidor envia o cardápio disponível.
4. O atendente seleciona os itens informando seus códigos numéricos.
5. O servidor calcula o valor total do pedido.
6. O servidor envia um resumo detalhado do pedido.
7. O ponto de pagamento confirma ou cancela o pagamento.
8. O servidor retorna a liberação ou o cancelamento do pedido.

Cada cliente é atendido por uma **thread independente**, permitindo múltiplos pedidos simultâneos.

---

## ▶️ Como Executar o Projeto

### Pré-requisitos
- Java JDK 8 ou superior
- Terminal ou prompt de comando

### Compilação
```bash
javac ServidorPedidos.java
javac ClienteQuiosque.java
