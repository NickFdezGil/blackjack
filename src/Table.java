import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

public class Table {
    private List<Player> Players;
    private List<Card> Deck = new ArrayList<>(52);
    private String [] Types = {"hearts", "Diamonds", "Clubs", "Spades"};
    private String [] Values = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
    private Integer initialPlayers = null;
    private Integer rounds;

    public void generateDeck(){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 13; j ++){
                Deck.add(new Card(Values[j],Types[i]));
            }
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
    }
    public void createPlayerBase(String[] args){
        for(int i = 0; i< initialPlayers; i++){
            Players.add(new Player("player " + (i+1)));
            System.out.println("Player " +(i+1) +" Created");
        }
    }
    public void deal(String[] args, int player){
        Card e = Deck.get(new Random().nextInt(Deck.size()));
        Players.get(player).askCard(e);
        Deck.remove(e);
    }
    public void showHand(String[] args, int player){
        System.out.println("Player " + (player+1) + ": " + Players.get(player).showHand());
    }
    public static void main(String[] args) {
        try {
            Table obj = new Table();
            obj.initialize(args);
            obj.createPlayerBase(args);
            System.out.println("Welcome to this game, please wait while we deal to everyone");
            for(int i = 0; i < obj.initialPlayers; i++){
                obj.deal(args, i);
            }
            for(int i = 0; i < obj.initialPlayers; i++){
                obj.deal(args, i);
            }
            for(int i = 0; i< obj.initialPlayers; i++){
                obj.showHand(args, i);
            }
        } catch (Exception e) {

        }
    }

}

