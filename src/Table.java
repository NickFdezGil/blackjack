import java.io.*;
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

    //Initialize the table
    public void generateDeck(){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 13; j ++){
                Deck.add(new Card(Values[j],Types[i]));
            }
        }
    }
    public void showRemainingDeck(){
        for(int i = 0; i < Deck.size(); i++){
            System.out.println(Deck.get(i).toString());
        }
    }
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
    public void createPlayerBase(String[] args){
        for(int i = 0; i< initialPlayers; i++){
            Players.add(new Player("player " + (i+1)));
            System.out.println("Player " +(i+1) +" Created");
        }
    }

    //Methods for the players
    public void deal(String[] args, int player){
        Card e = Deck.get(new Random().nextInt(Deck.size()));
        Players.get(player).askCard(e);
        Deck.remove(e);
    }
    public void showHand(String[] args, int player){
        System.out.println("Player " + (player+1) + ": " + Players.get(player).showHand());
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

    //Methods for the endgame
    public void selectWinner(String[] args){
        int maxScore = Dealer.HandScore();
        int currentScore = 0;
        String winner = "Dealer";
        Integer selected = null;
        for(int i = 0; i < initialPlayers; i++){
            currentScore = Players.get(i).HandScore();
            if(currentScore > maxScore) {
                maxScore = currentScore;
                winner = "Player " + (i+1);
                selected = i;
            }
        }
        if(selected == null){
            Dealer.winner();
        }
        else{Players.get(selected).winner();}
        System.out.println("The winner of this round was " + winner);
    }
    public void showTotalWins(String[] args){
        for(int i = 0; i < initialPlayers; i++){
            System.out.println("Player " + (i+1) + " has won " + Players.get(i).getScore() + " rounds");
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
    public void getFinalWinner(String[] args){
        int maxScore = Dealer.getScore();
        int currentScore = 0;
        String winner = "Dealer";
        Integer selected = null;
        for(int i = 0; i < initialPlayers; i++){
            currentScore = Players.get(i).getScore();
            if(currentScore > maxScore){
                maxScore = currentScore;
                winner = "Player " + (i+1);
                selected = i;
            }
        }
        if (selected == null) {System.out.println("The dealer won the game");}
        else{System.out.println(winner + " has won the game, congratulations");}
    }

    public static void main(String[] args) {
        try {
            Table obj = new Table();
            obj.initialize(args);
            obj.createPlayerBase(args);
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
                obj.showInitialDealerHand(args);
                System.out.println("Now that everyone has been dealt, we will choose a winner");
                obj.showDealerHand(args);
                obj.selectWinner(args);
                obj.showTotalWins(args);
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

