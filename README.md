# Projeto Prático - Mensageria em Java com Apache Kafka

**Disciplina:** Software Concorrente e Distribuído  
**Curso:** Bacharelado em Engenharia de Software  
**Professor:** ELIAS BATISTA FERREIRA

## Componentes do Grupo

* Arthur Felipe
* João Victor Lemes
* Matheus Augusto

## Visão Geral do Projeto

Este projeto implementa um sistema distribuído em Java para uma plataforma de e-commerce, utilizando Apache Kafka como backbone de mensageria. A solução é composta por três microsserviços que processam pedidos de forma assíncrona.

Para a demonstração, foi desenvolvida uma interface de frontend (um "Dashboard") que permite a criação de novos pedidos e visualiza, em tempo real, todos os eventos e logs gerados pelos serviços no backend através de uma conexão WebSocket.

## Arquitetura da Solução

O sistema é modelado em uma arquitetura de microsserviços orientada a eventos.

### Fluxo de Negócio

1.  **Frontend (`index.html`):** Uma página web permite que o usuário insira os itens de um pedido e o envie.
2.  **Order-Service (Produtor):**
    * Expõe uma API REST (`POST /orders`) que recebe os pedidos do frontend.
    * Publica o objeto do pedido em formato JSON no tópico `orders` do Kafka.
3.  **Inventory-Service (Consumidor e Produtor):**
    * Consome as mensagens de novos pedidos do tópico `orders`.
    * Simula a lógica de negócio de verificar o estoque.
    * Publica o resultado da operação (sucesso ou falha) no tópico `inventory-events`.
4.  **Notification-Service (Consumidor):**
    * Consome as mensagens de eventos de inventário do tópico `inventory-events`.
    * Finaliza o fluxo, simulando o envio de uma notificação para o usuário.

### Fluxo de Logs em Tempo Real (WebSocket)

Para permitir a visualização no frontend, um fluxo de logging centralizado foi implementado:
1.  Todos os três serviços (`Order`, `Inventory`, `Notification`) publicam seus logs de operação mais importantes em um tópico central do Kafka chamado `log-events`.
2.  O `Order-Service` possui um consumidor adicional que escuta o tópico `log-events`.
3.  Ao receber uma mensagem de log, o `Order-Service` a transmite imediatamente para todos os clientes conectados via WebSocket.
4.  O frontend (`index.html`) estabelece uma conexão WebSocket com o `Order-Service` e exibe cada log recebido em um painel de "Stream de Logs em Tempo Real".

## Tecnologias Utilizadas

* **Linguagem:** Java 17
* **Framework:** Spring Boot 3
* **Mensageria:** Apache Kafka
* **Comunicação Real-time:** Spring for WebSocket
* **Containerização:** Docker e Docker Compose
* **Build Tool:** Maven
* **Frontend:** HTML5, CSS3, JavaScript (sem frameworks)

## Pré-requisitos

* Java Development Kit (JDK) 17 ou superior.
* Docker e Docker Compose instalados e em execução.
* Um navegador web moderno (Chrome, Firefox, etc.).

## Como Executar a Solução

Siga os passos abaixo para executar o sistema completo.

### 1. Iniciar a Infraestrutura (Kafka)

Na raiz do projeto, execute o seguinte comando para iniciar os contêineres do Zookeeper e Kafka em segundo plano:
```bash
docker-compose up -d
````

### 2\. Criar os Tópicos no Kafka

Com os contêineres rodando, abra um novo terminal e crie os **três** tópicos necessários:

```bash
# Tópico para pedidos
docker exec -it kafka kafka-topics --create --topic orders --bootstrap-server localhost:9092

# Tópico para eventos de inventário
docker exec -it kafka kafka-topics --create --topic inventory-events --bootstrap-server localhost:9092

# Tópico para logs em tempo real
docker exec -it kafka kafka-topics --create --topic log-events --bootstrap-server localhost:9092
```

### 3\. Executar os Microsserviços

Inicie cada um dos três serviços Java usando sua IDE (como o VS Code), executando a classe `*Application.java` de cada um. A ordem recomendada é:

1.  Inicie o **`notification-service`**.
2.  Inicie o **`inventory-service`**.
3.  Inicie o **`order-service`**.

### 4\. Testar o Fluxo pelo Frontend

1.  Após os três serviços estarem rodando, localize o arquivo `index.html` na raiz do seu projeto.
2.  Abra o arquivo `index.html` diretamente no seu navegador (ex: Google Chrome).
3.  A interface web será exibida. Digite os itens desejados no campo de texto e clique em "Enviar Pedido".
4.  Observe o painel "Stream de Logs em Tempo Real" ser preenchido com os eventos de cada serviço à medida que o pedido é processado.

-----

## Respostas aos Requisitos Não-Funcionais

### Escalabilidade

A escalabilidade da solução é alcançada através do design do Apache Kafka, que utiliza **partições** e **grupos de consumidores**.

  * **Partições:** Cada tópico pode ser dividido em múltiplas partições. Isso permite que os dados de um tópico sejam paralelizados.
  * **Grupos de Consumidores:** Para escalar o processamento, podemos iniciar múltiplas instâncias de um mesmo serviço consumidor (ex: `Inventory-Service`). Se todas as instâncias pertencerem ao mesmo `group.id`, o Kafka distribui automaticamente as partições do tópico entre elas, permitindo o processamento paralelo e aumentando a vazão.

### Tolerância à Falha

A tolerância à falha é a capacidade do sistema de continuar operando mesmo com a falha de um ou mais componentes. No Kafka, isso é garantido pelo mecanismo de **replicação**.

  * **Fator de Replicação:** Ao criar um tópico, podemos definir um fator de replicação (ex: 3). Isso significa que cada partição terá 3 cópias idênticas (réplicas), distribuídas em diferentes servidores (brokers) do cluster.
  * **Líder e Seguidores:** Para cada partição, uma réplica é eleita a "líder" e as outras são "seguidoras". Toda a comunicação ocorre com a líder.
  * **Cenário de Falha:** Se o broker que hospeda a partição líder falhar, o Kafka automaticamente promove uma das seguidoras a nova líder, de forma transparente para as aplicações, garantindo alta disponibilidade.

### Idempotência

Idempotência é a propriedade que garante que uma operação, mesmo se executada múltiplas vezes, produz o mesmo resultado que se fosse executada uma única vez.

  * **Produtor Idempotente:** O produtor do Kafka pode ser configurado com `enable.idempotence=true` para evitar que novas tentativas de envio (em caso de falha de rede) resultem em mensagens duplicadas no tópico.
  * **Consumidor Idempotente (Lógica de Negócio):** A garantia mais robusta é no consumidor. O `Inventory-Service`, ao receber um pedido, deveria primeiro verificar em um banco de dados se o ID daquele pedido já foi processado. Se já foi, a mensagem é descartada; caso contrário, o estoque é processado e o ID do pedido é marcado como concluído.

## Vídeo de Demonstração

[LINK PARA O SEU VÍDEO NO YOUTUBE/GOOGLE DRIVE AQUI]

````
---

### Parte 2: Como Subir o Projeto para o GitHub

[cite_start]Agora que o `README.md` está pronto, siga estes passos para colocar seu projeto em um repositório público no GitHub, conforme solicitado no trabalho[cite: 12].

**Pré-requisitos:**
* Você precisa ter o **Git** instalado no seu computador.
* Você precisa ter uma **conta no GitHub**.

**Passo 1: Criar um Repositório Vazio no GitHub**

1.  Acesse [github.com](https://github.com) e faça login.
2.  No canto superior direito, clique no ícone de **"+"** e selecione **"New repository"**.
3.  Dê um nome ao seu repositório (ex: `projeto-mensageria-kafka`).
4.  Deixe a opção **"Public"** marcada (isso é um requisito do trabalho).
5.  **MUITO IMPORTANTE:** **NÃO** marque as caixas para adicionar um `README`, `.gitignore` ou licença. O repositório precisa ser criado completamente vazio, pois você já tem esses arquivos no seu computador.
6.  Clique em **"Create repository"**.

**Passo 2: Conectar seu Projeto Local ao GitHub**

O GitHub irá te mostrar uma página com alguns comandos. Nós vamos usar os comandos da seção "...or push an existing repository from the command line".

1.  Abra um terminal **na raiz do seu projeto** (a pasta `software-concorrente-distribuido`).
2.  Execute os comandos abaixo, um por um:

    ```bash
    # Inicializa o git no seu projeto e define o nome da branch principal como "main"
    git init -b main

    # Adiciona todos os arquivos do seu projeto para serem enviados
    git add .

    # Cria o primeiro "pacote" de envio com uma mensagem descritiva
    git commit -m "Commit inicial do projeto de mensageria"

    # Conecta sua pasta local ao repositório que você criou no GitHub
    # ATENÇÃO: Substitua a URL pela URL do SEU repositório!
    git remote add origin https://github.com/SEU_USUARIO/SEU_REPOSITORIO.git

    # Envia os arquivos para o GitHub
    git push -u origin main
    ```

**Passo 3: Verificar**

Após executar o último comando (`git push`), atualize a página do seu repositório no GitHub. Todos os seus arquivos, incluindo os 3 serviços e o `README.md` que acabamos de criar, devem aparecer lá.

Pronto! Seu projeto está no GitHub e pronto para ser entregue.
````
