# Desafio Lógica Super Trunfo em Java

Este repositório apresenta a implementação da **lógica central** de um jogo Super Trunfo em Java. O foco principal deste desafio é demonstrar as regras de comparação de cartas, o fluxo de uma rodada e o gerenciamento do estado do jogo (mãos dos jogadores, pilhas de empate), sem uma interface de usuário interativa complexa no console.

## Sumário

* [Visão Geral](#visão-geral)
* [Funcionalidades Implementadas (Foco na Lógica)](#funcionalidades-implementadas-foco-na-lógica)
* [Estrutura do Projeto](#estrutura-do-projeto)
* [Como Compilar e Executar](#como-compilar-e-executar)
* [Requisitos](#requisitos)
* [Tecnologias Utilizadas](#tecnologias-utilizadas)
* [Autor](#autor)
* [Licença](#licença)

## Visão Geral

O projeto simula rodadas de um jogo Super Trunfo entre múltiplos jogadores. A classe `GameLogic` encapsula todas as regras do jogo: embaralhar, distribuir cartas, processar rodadas, determinar vencedores de rodada (e gerenciar empates), e verificar o fim do jogo. A execução principal (`Program.java`) serve como uma demonstração dessa lógica, simulando as escolhas de atributos.

## Funcionalidades Implementadas (Foco na Lógica)

* **Definição de Cartas:** Classe `Card` com nome e múltiplos atributos numéricos personalizáveis.
* **Gerenciamento de Baralho:** Classe `Deck` para adicionar cartas, embaralhar e distribuir.
* **Gerenciamento de Jogadores:** Classe `Player` para manter a mão de cartas de cada participante.
* **Lógica de Rodada (`GameLogic`):**
    * Setup inicial do jogo (distribuição de cartas).
    * Identificação do jogador atual.
    * Comparação de cartas baseada em um atributo escolhido.
    * Determinação do vencedor da rodada.
    * Gerenciamento de empates, onde as cartas são adicionadas a um "pote" e o vencedor da próxima rodada leva tudo.
    * Passagem de turno para o jogador vencedor (ou o mesmo jogador em caso de empate).
    * Verificação da condição de fim de jogo (quando apenas um jogador tem cartas).
    * Identificação do vencedor final do jogo.

## Estrutura do Projeto

O projeto está organizado em pacotes para melhor modularidade e separação de responsabilidades:

desafio-logica-super-trunfo/
├── src/
│   ├── application/      # Contém a classe principal para execução e demonstração da lógica.
│   │   └── Program.java
│   └── game/             # Contém as classes que implementam a lógica central do jogo.
│       ├── Card.java     # Define as cartas e seus atributos.
│       ├── Deck.java     # Gerencia o baralho de cartas.
│       ├── Player.java   # Representa um jogador e sua mão.
│       └── GameLogic.java # A classe principal com a lógica do jogo.


## Como Compilar e Executar

Certifique-se de ter o **Java Development Kit (JDK)** instalado em sua máquina.

1.  **Baixe ou clone este repositório:**
    ```bash
    git clone [https://github.com/seu-usuario/desafio-logica-super-trunfo-java.git](https://github.com/seu-usuario/desafio-logica-super-trunfo-java.git)
    cd desafio-logica-super-trunfo
    ```

2.  **Navegue até a pasta `src`:**
    ```bash
    cd src
    ```

3.  **Compile todos os arquivos Java:**
    ```bash
    javac game/*.java application/*.java
    ```
    *Se você estiver usando uma IDE (como IntelliJ IDEA ou Eclipse), a compilação é geralmente automática.*

4.  **Execute a demonstração da lógica do jogo:**
    Permaneça na pasta `src` e execute a classe `Program` do pacote `application`:
    ```bash
    java application.Program
    ```

## Requisitos

* Java Development Kit (JDK) 11 ou superior.

## Tecnologias Utilizadas

* Java

## Autor

[Darcimarcos Valerio Leite]
[www.linkedin.com/in/darcimarcos]

## Licença

Este projeto está licenciado sob a Licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.
