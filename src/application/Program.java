// application/Program.java
package application;

import game.Card;
import game.Deck;
import game.GameLogic;
import game.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Program {

    public static void main(String[] args) {
        // --- 1. Preparar Cartas e Baralho ---
        Deck gameDeck = new Deck();

        // Exemplo de Cartas (carros) - Adapte conforme seu tema
        Card card1 = new Card("Ferrari F40");
        card1.addAttribute("Velocidade", 324.0); // km/h
        card1.addAttribute("Potencia", 471.0); // hp
        card1.addAttribute("Peso", 1100.0); // kg
        gameDeck.addCard(card1);

        Card card2 = new Card("Porsche 911 GT3 RS");
        card2.addAttribute("Velocidade", 312.0);
        card2.addAttribute("Potencia", 520.0);
        card2.addAttribute("Peso", 1430.0);
        gameDeck.addCard(card2);

        Card card3 = new Card("Lamborghini Aventador");
        card3.addAttribute("Velocidade", 350.0);
        card3.addAttribute("Potencia", 740.0);
        card3.addAttribute("Peso", 1575.0);
        gameDeck.addCard(card3);

        Card card4 = new Card("Bugatti Chiron");
        card4.addAttribute("Velocidade", 420.0);
        card4.addAttribute("Potencia", 1500.0);
        card4.addAttribute("Peso", 1996.0);
        gameDeck.addCard(card4);

        Card card5 = new Card("McLaren P1");
        card5.addAttribute("Velocidade", 350.0);
        card5.addAttribute("Potencia", 916.0);
        card5.addAttribute("Peso", 1490.0);
        gameDeck.addCard(card5);

        Card card6 = new Card("Toyota Supra (Mk4)");
        card6.addAttribute("Velocidade", 250.0);
        card6.addAttribute("Potencia", 320.0);
        card6.addAttribute("Peso", 1490.0);
        gameDeck.addCard(card6);

        Card card7 = new Card("Nissan GT-R R35");
        card7.addAttribute("Velocidade", 315.0);
        card7.addAttribute("Potencia", 565.0);
        card7.addAttribute("Peso", 1750.0);
        gameDeck.addCard(card7);

        Card card8 = new Card("Ford GT (2017)");
        card8.addAttribute("Velocidade", 348.0);
        card8.addAttribute("Potencia", 647.0);
        card8.addAttribute("Peso", 1385.0);
        gameDeck.addCard(card8);

        // Adicione mais cartas para um jogo mais completo
        Card card9 = new Card("Koenigsegg Jesko Absolut");
        card9.addAttribute("Velocidade", 531.0); // Teórica
        card9.addAttribute("Potencia", 1600.0);
        card9.addAttribute("Peso", 1320.0);
        gameDeck.addCard(card9);

        Card card10 = new Card("Rimac Nevera");
        card10.addAttribute("Velocidade", 412.0);
        card10.addAttribute("Potencia", 1914.0); // HP Elétrico
        card10.addAttribute("Peso", 2300.0); // Carro elétrico, muito pesado
        gameDeck.addCard(card10);


        // --- 2. Criar Jogadores ---
        List<Player> gamePlayers = new ArrayList<>();
        Player player1 = new Player("Jogador Humano");
        Player player2 = new Player("Computador");
        gamePlayers.add(player1);
        gamePlayers.add(player2);

        // --- 3. Inicializar a Lógica do Jogo ---
        int totalCards = gameDeck.size();
        int numPlayers = gamePlayers.size();
        int cardsPerPlayer = totalCards / numPlayers;
        if (totalCards % numPlayers != 0) {
            System.out.println("Atenção: Total de cartas (" + totalCards + ") não é divisível uniformemente pelos jogadores (" + numPlayers + ").");
        }

        GameLogic gameLogic = new GameLogic(gamePlayers, gameDeck);
        gameLogic.setupGame(cardsPerPlayer);

        System.out.println("--- DESAFIO LÓGICA SUPER TRUNFO ---");
        System.out.println("Jogo iniciado com " + gamePlayers.size() + " jogadores e " + cardsPerPlayer + " cartas cada.");

        // --- 4. Simular Rodadas ---
        int round = 1;
        Random rand = new Random();
        Set<String> attributes = gameLogic.getAvailableAttributes();
        List<String> attributeList = new ArrayList<>(attributes); // Para escolher aleatoriamente

        while (!gameLogic.isGameOver()) {
            System.out.println("\n--- Rodada " + round + " ---");
            Player currentPlayer = gameLogic.getCurrentPlayer();
            if (currentPlayer == null) { // Caso não haja mais jogadores com cartas (edge case)
                System.out.println("Nenhum jogador tem cartas restantes. Fim da simulação.");
                break;
            }

            Card playerCard = currentPlayer.getHand().get(0); // Pega a carta do topo
            System.out.println(currentPlayer.getName() + " é o jogador atual.");
            System.out.println("Carta do " + currentPlayer.getName() + ": " + playerCard.getName());
            playerCard.display();

            // Lógica para escolher um atributo (aqui é aleatório para simulação)
            String chosenAttribute = attributeList.get(rand.nextInt(attributeList.size()));
            System.out.println(currentPlayer.getName() + " escolhe o atributo: " + chosenAttribute.toUpperCase());

            try {
                Player roundWinner = gameLogic.playRound(chosenAttribute);

                // Exibir as cartas dos outros jogadores para contextualizar a simulação
                System.out.println("\nCartas reveladas na rodada (para comparação):");
                for (Player p : gamePlayers) {
                    if (p.hasCards() && p != currentPlayer) { // A carta do currentPlayer já foi exibida
                        Card otherCard = p.getHand().get(0);
                        System.out.println("- " + p.getName() + " jogou: " + otherCard.getName() + " (" + chosenAttribute + ": " + otherCard.getAttributeValue(chosenAttribute) + ")");
                    }
                }


                if (roundWinner != null) {
                    System.out.println("\n>>> " + roundWinner.getName() + " venceu a rodada!");
                } else {
                    System.out.println("\n>>> Rodada empatou! Cartas no pote.");
                    // Se empatou, o jogador atual (currentPlayer) não muda, ele continua para a próxima rodada
                }

                // Atualizar status
                for (Player p : gamePlayers) {
                    System.out.println(p.getName() + " tem " + p.getHandSize() + " cartas.");
                }

                if (roundWinner != null) { // Se não foi empate, avança o turno para o vencedor
                    // gameLogic.currentPlayerIndex já foi atualizado em playRound
                    // Não precisa de gameLogic.advanceTurn() aqui
                } else {
                    // Em caso de empate, o mesmo jogador continua (GameLogic não avançou o turno)
                    // Não chame advanceTurn()
                }


            } catch (IllegalArgumentException | IllegalStateException e) {
                System.err.println("Erro na lógica da rodada: " + e.getMessage());
                break;
            }
            round++;

            // Pequena pausa para acompanhar a simulação
            try {
                Thread.sleep(1500); // 1.5 segundos
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // --- 5. Fim do Jogo ---
        System.out.println("\n--- Fim da Simulação do Jogo ---");
        Player winner = gameLogic.getWinner();
        if (winner != null) {
            System.out.println("O VENCEDOR É: " + winner.getName() + " com " + winner.getHandSize() + " cartas!");
        } else {
            System.out.println("O jogo terminou sem um vencedor claro (possível empate ou problema na lógica).");
        }
    }
}
