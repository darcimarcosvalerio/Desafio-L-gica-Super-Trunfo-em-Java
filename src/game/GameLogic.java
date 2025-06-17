// game/GameLogic.java
package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GameLogic {
    private List<Player> players;
    private Deck deck;
    private int currentPlayerIndex;
    private List<Card> tiePile; // Pilha de cartas para empates (o pote)

    public GameLogic(List<Player> players, Deck deck) {
        if (players == null || players.isEmpty()) {
            throw new IllegalArgumentException("Lista de jogadores não pode ser nula ou vazia.");
        }
        if (deck == null || deck.isEmpty()) {
            throw new IllegalArgumentException("Baralho não pode ser nulo ou vazio.");
        }
        this.players = players;
        this.deck = deck;
        this.currentPlayerIndex = 0; // O primeiro jogador começa
        this.tiePile = new ArrayList<>();
    }

    /**
     * Configura o jogo: embaralha o baralho e distribui as cartas.
     * @param cardsPerPlayer Número de cartas para cada jogador.
     */
    public void setupGame(int cardsPerPlayer) {
        deck.shuffle();
        for (Player player : players) {
            player.addCards(deck.deal(cardsPerPlayer));
        }
    }

    /**
     * Retorna o jogador atual cujo turno é de escolher o atributo.
     * @return O objeto Player do jogador atual.
     */
    public Player getCurrentPlayer() {
        // Encontra o próximo jogador com cartas
        int originalIndex = currentPlayerIndex;
        while (!players.get(currentPlayerIndex).hasCards()) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            if (currentPlayerIndex == originalIndex) {
                // Deu a volta em todos os jogadores e nenhum tem cartas
                return null;
            }
        }
        return players.get(currentPlayerIndex);
    }

    /**
     * Simula uma rodada do jogo.
     * @param chosenAttribute O nome do atributo escolhido pelo jogador atual para comparação.
     * @return O jogador vencedor da rodada, ou null se for um empate no pote.
     * @throws IllegalStateException se o jogo já deveria ter terminado ou não há cartas.
     * @throws IllegalArgumentException se o atributo escolhido for inválido.
     */
    public Player playRound(String chosenAttribute) {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null || !currentPlayer.hasCards()) {
            // Este cenário deveria ser capturado pelo loop principal do jogo
            throw new IllegalStateException("Nenhum jogador ativo para iniciar a rodada.");
        }

        Card playerCard = currentPlayer.getHand().get(0); // A carta do topo do jogador atual
        if (!playerCard.getAttributes().containsKey(chosenAttribute.toLowerCase())) {
            throw new IllegalArgumentException("Atributo inválido: " + chosenAttribute);
        }

        // Coleta as cartas de todos os jogadores ativos para esta rodada
        List<Card> cardsInRound = new ArrayList<>();
        List<Player> activePlayers = new ArrayList<>();
        for (Player p : players) {
            if (p.hasCards()) {
                activePlayers.add(p);
                cardsInRound.add(p.getHand().get(0)); // Pega a carta do topo sem remover ainda
            }
        }

        if (activePlayers.size() < 2 && !isGameOver()) {
            // Isso pode acontecer se houver apenas um jogador restante
            // ou se o último jogador acabou de ficar sem cartas, mas o loop principal ainda não detectou o fim.
            return null; // ou um sinal específico para fim de jogo
        }

        // Simula o descarte das cartas para o jogo
        for (Player p : activePlayers) {
            p.playCard(); // Remove a carta do topo da mão
        }

        Card winningCard = cardsInRound.get(0);
        Player roundWinner = activePlayers.get(0);
        List<Card> currentRoundTieBreakers = new ArrayList<>(); // Para verificar se há empate entre os melhores valores

        // Encontra a carta vencedora e o jogador vencedor
        for (int i = 0; i < activePlayers.size(); i++) {
            Card currentCard = cardsInRound.get(i);
            Player player = activePlayers.get(i);

            int comparisonResult = currentCard.compareTo(winningCard, chosenAttribute);

            if (comparisonResult == 1) { // currentCard ganha
                winningCard = currentCard;
                roundWinner = player;
                currentRoundTieBreakers.clear(); // Reinicia lista de empate para o novo vencedor
                currentRoundTieBreakers.add(currentCard);
            } else if (comparisonResult == 0) { // Empate com a winningCard atual
                currentRoundTieBreakers.add(currentCard);
            }
        }

        // Verifica se houve um empate entre as melhores cartas na rodada
        if (currentRoundTieBreakers.size() > 1) {
            System.out.println("DEBUG: Empate na rodada detectado."); // Mensagem de debug
            tiePile.addAll(cardsInRound); // Todas as cartas da rodada vão para o pote de empate
            // O jogador atual (que escolheu o atributo) permanece o mesmo para a próxima rodada para desempatar.
            return null; // Sinaliza que foi um empate no pote
        } else {
            // O vencedor coleta as cartas da rodada e, se houver, as do pote
            roundWinner.addCards(cardsInRound);
            if (!tiePile.isEmpty()) {
                System.out.println("DEBUG: Pote resgatado!"); // Mensagem de debug
                roundWinner.addCards(tiePile);
                tiePile.clear();
            }
            // O vencedor da rodada se torna o próximo a escolher o atributo
            this.currentPlayerIndex = players.indexOf(roundWinner);
            return roundWinner;
        }
    }

    /**
     * Avança para o próximo jogador que tem cartas.
     * Deve ser chamado após uma rodada, a menos que haja um empate no pote.
     */
    public void advanceTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        // Garante que o próximo jogador tem cartas
        int originalIndex = currentPlayerIndex;
        while (!players.get(currentPlayerIndex).hasCards()) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            if (currentPlayerIndex == originalIndex) {
                // Deu a volta em todos os jogadores e nenhum tem cartas, ou só um tem cartas
                break;
            }
        }
    }

    /**
     * Verifica se o jogo terminou (apenas um jogador com cartas).
     * @return true se o jogo terminou, false caso contrário.
     */
    public boolean isGameOver() {
        return players.stream().filter(Player::hasCards).count() <= 1;
    }

    /**
     * Retorna o vencedor do jogo, se houver.
     * @return O objeto Player vencedor, ou null se não houver vencedor claro (ex: jogo não terminou).
     */
    public Player getWinner() {
        List<Player> playersWithCards = players.stream()
                                            .filter(Player::hasCards)
                                            .collect(Collectors.toList());
        if (playersWithCards.size() == 1) {
            return playersWithCards.get(0);
        }
        return null; // Jogo não terminou ou é um estado de empate (sem vencedores)
    }

    /**
     * Retorna uma lista dos nomes dos atributos disponíveis em qualquer carta do baralho (ou um exemplo).
     * @return Set de String com os nomes dos atributos.
     */
    public Set<String> getAvailableAttributes() {
        if (!deck.isEmpty()) {
            // Pega uma carta qualquer para extrair os atributos disponíveis
            // (Assumindo que todas as cartas têm os mesmos atributos para escolha)
            return deck.getCards().get(0).getAttributes().keySet();
        } else if (!players.isEmpty() && players.get(0).hasCards()) {
            return players.get(0).getHand().get(0).getAttributes().keySet();
        }
        return Set.of(); // Retorna um set vazio se não houver cartas
    }
}
