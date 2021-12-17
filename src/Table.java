import java.io.*;
import java.sql.Array;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

public class Table {
    private Dealer Dealer;
    private List<Player> Players;
    private List<Card> Deck = new ArrayList<>(52);
    private String [] Types = {"hearts", "Diamonds", "Clubs", "Spades"};
    private String [] Values = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
    private Integer initialPlayers = null;
    private Integer rounds;
    private int [] handScores;
    private int [] finalScores;
    private String [] names = {"Paco", "Maria", "Alvaro", "James"};

    // Creates a new deck
    public void generateDeck(){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 13; j ++){
                Deck.add(new Card(Values[j],Types[i]));
            }
        }
    }

    // Method to check if generateDeck() and methods for deal work properly (not currently in use)
    public void showRemainingDeck(){
        for(int i = 0; i < Deck.size(); i++){
            System.out.println(Deck.get(i).toString());
        }
    }

    // We initialize the table by asking the players and rounds
    public void initialize(String[] args){
        Scanner console = new Scanner(System.in);
        generateDeck();
        while(true){
            try {
                System.out.println("How many players? (Only integers, max. 4 players)");
                initialPlayers = console.nextInt();
                if(initialPlayers.intValue() > 4 || initialPlayers.intValue() < 1){
                    throw new ArithmeticException();
                }
                break;
            }catch(ArithmeticException e){
                System.out.println("Please, keep the number between 1 and 4 players");
                initialPlayers =  null;
                continue;
            }catch(InputMismatchException i){
                System.out.println("Not a valid value");
                initialPlayers = null;
                console.next();
                continue;
            }
        }
        while(true){
            try {
                System.out.println("Please, select number of rounds (Only integers, at least 1 round)");
                rounds = console.nextInt();
                if(rounds.intValue() < 1){
                    throw new ArithmeticException();
                }
                break;
            }catch(ArithmeticException e){
                System.out.println("Please, input a valid number of rounds");
                rounds =  null;
                continue;
            }catch(InputMismatchException i){
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
    public void createPlayerBase(String[] args){
        for(int i = 0; i< initialPlayers; i++){
            Players.add(new Player(names [i]));
            System.out.println("Player " + Players.get(i).getName() +" Created");
        }
    }

    //Methods for the players
    public void deal(String[] args, int player){
        Card e = Deck.get(new Random().nextInt(Deck.size()));
        Players.get(player).askCard(e);
        Deck.remove(e);
    }
    public void showHand(String[] args, int player){
        System.out.println("Player " + Players.get(player).getName() + ": " + Players.get(player).showHand());
    }

    //Methods for the dealer
    public void dealDealer(String[] args){
        Card e = Deck.get(new Random().nextInt(Deck.size()));
        Dealer.askCard(e);
        Deck.remove(e);
    }
    public void showInitialDealerHand(String[] args){
        System.out.println("Dealer: " + Dealer.showInitialHand());
    }
    public void showDealerHand(String[] args){System.out.println("Dealer: "+ Dealer.showHand());}
    public void dealDealerUnit16(String[] args){
        while(Dealer.HandScore()<16){
            Card e = Deck.get(new Random().nextInt(Deck.size()));
            Dealer.askCard(e);
            Deck.remove(e);
            showDealerHand(args);
        }
    }

    //Methods for the endgame
    public void selectWinner(String[] args){
        // Dealer's score over 21, all remaining players win
        if(Dealer.HandScore() > 21){
            System.out.println("Dealer bust, all remaining players win");
            for(int i=0; i<initialPlayers; i++){
                if(Players.get(i).HandScore()<=21){
                    Players.get(i).winner();
                    System.out.println("Player " + Players.get(i).getName() + " has won a point");
                } else {
                    System.out.println("Player " + Players.get(i).getName() + " had bust");
                }
            }
            return;
        }
        // Getting the players scores
        handScores = new int[initialPlayers];
        // Show the hand score of each player
        for(int i=0; i < initialPlayers; i++){
            handScores [i] = Players.get(i).HandScore();
            System.out.println("Player " + Players.get(i).getName() + " has a score of " + handScores [i]);
        }
        int dealersScore = Dealer.HandScore();
        System.out.println("Dealer has a score of " + dealersScore);
        int maxScore = 0;
        int currentScore;
        String [] winner = new String[initialPlayers];
        // Compare the scores of the players
        for(int i = 0; i < initialPlayers; i++) {
            currentScore = handScores[i];
            if (currentScore >= maxScore && currentScore >= dealersScore && currentScore<22) {
                maxScore = currentScore;
                winner [i] = Players.get(i).getName();
            }
            else{
                System.out.println("Player " + Players.get(i).getName() + " has bust ");
            }
        }
        // If dealer scores same or more than maxScore, dealer wins
        if(dealersScore >= maxScore){
            Dealer.winner();
            System.out.println("Dealer has won the round");
        }
        // Show all the winners
        else{
            for(int j = 0; j < initialPlayers; j++){
                if(winner [j] == Players.get(j).getName()) {
                    Players.get(j).winner();
                    System.out.println("Player " + Players.get(j).getName() + " has won a point");
                }
            }
        }
    }
    public void showTotalWins(String[] args){
        for(int i = 0; i < initialPlayers; i++){
            System.out.println("Player " + Players.get(i).getName() + " has won " + Players.get(i).getScore() + " rounds");
        }
        System.out.println("Dealer has won " + Dealer.getScore() + " rounds");
    }
    public void clearHands(String[] args){
        for(int i = 0; i < initialPlayers; i++){
            Players.get(i).resetHand();
        }
        Dealer.resetHand();
    }
    public void shuffle(String[] args){
        Deck.clear();
        generateDeck();
    }

    // Select player o players with the highest score
    public void getFinalWinner(String[] args){
        finalScores = new int[initialPlayers];
        for(int i=0; i < initialPlayers; i++){
            finalScores [i] = Players.get(i).getScore();
            System.out.println("Player " + Players.get(i).getName() + " has a score of " + finalScores [i]);
        }
        int dealersScore = Dealer.getScore();
        System.out.println("Dealer has a score of " + dealersScore);
        int maxScore = finalScores [0];
        int currentScore;
        String [] winner = new String[initialPlayers];
        for(int i = 1; i < initialPlayers; i++) {
            currentScore = finalScores[i];
            if (currentScore >= maxScore && currentScore >= dealersScore) {
                maxScore = currentScore;
            }
        }
        // Include in array of winners the players with their score greater or equal to maxScore
        for(int i = 0; i < initialPlayers; i++){
            if(Players.get(i).getScore() >= maxScore){
                winner [i] = Players.get(i).getName();
            }
        }
        // If dealer scores same or more than maxScore, dealer wins
        if(dealersScore >= maxScore){
            Dealer.winner();
            System.out.println("Dealer has won the game");
        }
        // Show all the winners
        else{
            for(int i = 0; i < initialPlayers; i++){
                if(winner [i] == Players.get(i).getName()) {
                    Players.get(i).winner();
                    System.out.println("Player " + Players.get(i).getName() + " has won the game");
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            Table obj = new Table();
            obj.initialize(args);
            System.out.println(" ");
            obj.createPlayerBase(args);
            System.out.println(" ");
            System.out.println("Welcome to this game, please wait while we deal to everyone");
            for(int i = obj.rounds; i > 0; i--) {
                for (int j = 0; j < obj.initialPlayers; j++) {
                    obj.deal(args, j);
                }
                obj.dealDealer(args);
                for (int j = 0; j < obj.initialPlayers; j++) {
                    obj.deal(args, j);
                }
                obj.dealDealer(args);
                for (int j = 0; j < obj.initialPlayers; j++) {
                    obj.showHand(args, j);
                }

                for(int j = 0; j < obj.initialPlayers; j++){
                    obj.deal(args,j);
                }

                obj.showInitialDealerHand(args);
                System.out.println(" ");
                System.out.println("Now that everyone has been dealt, we will choose a winner");
                obj.showDealerHand(args);
                obj.dealDealerUnit16(args);
                System.out.println(" ");
                obj.selectWinner(args);
                System.out.println(" ");
                obj.showTotalWins(args);
                System.out.println(" ");
                if(i>1) {
                    if (i > 2) {
                        System.out.println("End of round, we will deal again and play the " + (i - 1) + " remaining rounds");
                    } else {
                        System.out.println("End of round, ready for the final round");
                    }
                }
                System.out.println("Please, press enter for next round");
                obj.clearHands(args);
                obj.shuffle(args);
                Scanner out = new Scanner(System.in);
                out.nextLine();
            }
            obj.getFinalWinner(args);
        } catch (Exception e) {

        }
    }

}

