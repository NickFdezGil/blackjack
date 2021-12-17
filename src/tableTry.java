import java.io.*;
import java.sql.Array;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

public class tableTry {
    private Dealer Dealer;
    private List<Player> Players;
    private List<Card> Deck = new ArrayList<>(52);
    private String[] Types = {"hearts", "Diamonds", "Clubs", "Spades"};
    private String[] Values = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
    private Integer initialPlayers = null;
    private Integer rounds;
    private int[] handScores;
    private int[] finalScores;
    private String[] names = {"Paco", "Maria", "Alvaro", "James"};

    public Integer getRounds() {
        return rounds;
    }

    public Integer getInitialPlayers() {
        return initialPlayers;
    }

    // Creates a new deck
    public void generateDeck() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++) {
                Deck.add(new Card(Values[j], Types[i]));
            }
        }
    }

    // We initialize the table by asking the players and rounds
    public void initialize(Scanner console) {
        console = new Scanner(System.in);
        generateDeck();
        while (true) {
            try {
                System.out.println("How many players? (Only integers, max. 4 players)");
                initialPlayers = console.nextInt();
                if (initialPlayers.intValue() > 4 || initialPlayers.intValue() < 1) {
                    throw new ArithmeticException();
                }
                break;
            } catch (ArithmeticException e) {
                System.out.println("Please, keep the number between 1 and 4 players");
                initialPlayers = null;
                continue;
            } catch (InputMismatchException i) {
                System.out.println("Not a valid value");
                initialPlayers = null;
                console.next();
                continue;
            }
        }
        while (true) {
            try {
                System.out.println("Please, select number of rounds (Only integers, at least 1 round)");
                rounds = console.nextInt();
                if (rounds.intValue() < 1) {
                    throw new ArithmeticException();
                }
                break;
            } catch (ArithmeticException e) {
                System.out.println("Please, input a valid number of rounds");
                rounds = null;
                continue;
            } catch (InputMismatchException i) {
                System.out.println("Not a valid value");
                rounds = null;
                console.next();
                continue;
            }
        }
        System.out.println("We'll be playing with " + initialPlayers.intValue() + " players for a total of " + rounds.intValue() + " rounds");
        Players = new ArrayList<Player>(initialPlayers.intValue());
        Dealer = new Dealer();
    }

    // Method to create the player base ,so we have them in a list for easy management
    public void addPlayer(Player player) {
        Players.add(player);
    }

    public String gameToString() {
        String game = "";
        for (int i = 0; i < initialPlayers; i++) {
            if (check21(i) == false) {
                game = game + "Player " + Players.get(i).getName() + ": " + Players.get(i).showHand() + "\n";
            } else {
                game = game + "Player " + Players.get(i).getName() + " has burst" + "\n";
            }
        }
        game = game + "Dealer: " + Dealer.showInitialHand();
        return game;
    }

    //Methods for the players
    public void firstDeal() {
        for (int j = 0; j < initialPlayers; j++) {
            deal(j);
        }
        dealDealer();
        for (int j = 0; j < initialPlayers; j++) {
            deal(j);
        }
        dealDealer();
        for (int j = 0; j < initialPlayers; j++) {
            showHand(j);
        }
        showInitialDealerHand();
    }

    public void deal(int player) {
        if (Players.get(player).HandScore() < 21) {
            Card e = Deck.get(new Random().nextInt(Deck.size()));
            Players.get(player).askCard(e);
            Deck.remove(e);
        }
    }

    public void showHand(int player) {
        System.out.println("Player " + Players.get(player).getName() + ": " + Players.get(player).showHand());
    }

    public boolean check21(int player) {
        int check = Players.get(player).HandScore();
        if (check <= 21) {
            return false;
        } else {
            return true;
        }
    }

    //Methods for the dealer
    public void dealDealer() {
        Card e = Deck.get(new Random().nextInt(Deck.size()));
        Dealer.askCard(e);
        Deck.remove(e);
    }

    public void showInitialDealerHand() {
        System.out.println("Dealer: " + Dealer.showInitialHand());
    }

    public String showDealerHand() {
        String hand = " ";
        hand = "Dealer: " + Dealer.showHand();
        System.out.println("Dealer: " + Dealer.showHand());
        return hand;
    }

    public void dealDealerUnit16() {
        while (Dealer.HandScore() < 16) {
            Card e = Deck.get(new Random().nextInt(Deck.size()));
            Dealer.askCard(e);
            Deck.remove(e);
            showDealerHand();
        }
    }

    //Methods for the endgame
    public String selectWinner() {
        // Dealer's score over 21, all remaining players win
        String winnerScreen = "";
        if (Dealer.HandScore() > 21) {
            System.out.println("Dealer bust, all remaining players win");
            winnerScreen = winnerScreen + "Dealer bust, all remaining players win\n";
            for (int i = 0; i < initialPlayers; i++) {
                if (Players.get(i).HandScore() <= 21) {
                    Players.get(i).winner();
                    System.out.println("Player " + Players.get(i).getName() + " has won a point");
                    winnerScreen = winnerScreen + "Player " + Players.get(i).getName() + " has won a point" + "\n";
                } else {
                    System.out.println("Player " + Players.get(i).getName() + " had bust");
                    winnerScreen = winnerScreen + "Player " + Players.get(i).getName() + " had bust" + "\n";
                }
            }
        } else {
            // Getting the players scores
            handScores = new int[initialPlayers];
            // Show the hand score of each player
            for (int i = 0; i < initialPlayers; i++) {
                handScores[i] = Players.get(i).HandScore();
                System.out.println("Player " + Players.get(i).getName() + " has a score of " + handScores[i]);
                winnerScreen = winnerScreen + "Player " + Players.get(i).getName() + " has a score of " + handScores[i] + "\n";
            }
            int dealersScore = Dealer.HandScore();
            System.out.println("Dealer has a score of " + dealersScore);
            winnerScreen = winnerScreen + "Dealer has a score of " + dealersScore + "\n";
            int maxScore = 0;
            int currentScore;
            String[] winner = new String[initialPlayers];
            // Compare the scores of the players
            for (int i = 0; i < initialPlayers; i++) {
                currentScore = handScores[i];
                if (currentScore >= maxScore && currentScore >= dealersScore && currentScore<22) {
                    maxScore = currentScore;
                    winner [i] = Players.get(i).getName();
                }else{
                    System.out.println("Player " + Players.get(i).getName() + " has bust ");
                    winnerScreen = winnerScreen + "Player " + Players.get(i).getName() + " has bust "+"\n";
                }
            }
            // If dealer scores same or more than maxScore, dealer wins
            if (dealersScore >= maxScore) {
                Dealer.winner();
                System.out.println("Dealer has won the round");
                winnerScreen = winnerScreen + "Dealer has won the round\n";
            }
            // Show all the winners
            else {
                for (int j = 0; j < initialPlayers; j++) {
                    if (winner[j] == Players.get(j).getName()) {
                        Players.get(j).winner();
                        System.out.println("Player " + Players.get(j).getName() + " has won a point");
                        winnerScreen = winnerScreen + "Player " + Players.get(j).getName() + " has won a point" + "\n";
                    }
                }
            }
        }
        return winnerScreen;
    }

    public String showTotalWins() {
        String totalWins = "";
        for (int i = 0; i < initialPlayers; i++) {
            System.out.println("Player " + Players.get(i).getName() + " has won " + Players.get(i).getScore() + " rounds");
            totalWins = totalWins + "Player " + Players.get(i).getName() + " has won " + Players.get(i).getScore() + " rounds" + "\n";
        }
        System.out.println("Dealer has won " + Dealer.getScore() + " rounds");
        totalWins = totalWins + "Dealer has won " + Dealer.getScore() + " rounds" + "\n";
        return totalWins;
    }

    public void clearHands() {
        for (int i = 0; i < initialPlayers; i++) {
            Players.get(i).resetHand();
        }
        Dealer.resetHand();
    }

    public void shuffle() {
        Deck.clear();
        generateDeck();
    }

    // Select player o players with the highest score
    public String getFinalWinner() {
        String FSScreen = "";
        finalScores = new int[initialPlayers];
        for (int i = 0; i < initialPlayers; i++) {
            finalScores[i] = Players.get(i).getScore();
            System.out.println("Player " + Players.get(i).getName() + " has a score of " + finalScores[i]);
            FSScreen = FSScreen + "Player " + Players.get(i).getName() + " has a score of " + finalScores[i] + "\n";
        }
        int dealersScore = Dealer.getScore();
        System.out.println("Dealer has a score of " + dealersScore);
        FSScreen = FSScreen + "Dealer has a score of " + dealersScore + "\n";
        int maxScore = finalScores[0];
        int currentScore;
        String[] winner = new String[initialPlayers];
        for (int i = 1; i < initialPlayers; i++) {
            currentScore = finalScores[i];
            if (currentScore >= maxScore && currentScore >= dealersScore) {
                maxScore = currentScore;
            }
        }
        // Include in array of winners the players with their score greater or equal to maxScore
        for (int i = 0; i < initialPlayers; i++) {
            if (Players.get(i).getScore() >= maxScore) {
                winner[i] = Players.get(i).getName();
            }
        }
        // If dealer scores same or more than maxScore, dealer wins
        if (dealersScore >= maxScore) {
            Dealer.winner();
            System.out.println("Dealer has won the game");
            FSScreen = FSScreen + "Dealer has won the game" + "\n";
        }
        // Show all the winners
        else {
            for (int i = 0; i < initialPlayers; i++) {
                if (winner[i] == Players.get(i).getName()) {
                    Players.get(i).winner();
                    System.out.println("Player " + Players.get(i).getName() + " has won the game");
                    FSScreen = FSScreen + "Player " + Players.get(i).getName() + " has won the game" + "\n";
                }
            }
        }
        return FSScreen;
    }
}
