import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int score;
    private List<Card> hand = new ArrayList<>(11);

    public Player(String name){
        this.name = name;
    }
    public String getName(){return name;}

    public void winner(){score++;}
    public int getScore(){return score;}

    public void askCard(Card e){
        hand.add(e);
    }
    public String showHand(){
        String current = "";
        for(int i = 0; i < hand.size(); i++){
            Card e = hand.get(i);
            current = current + e.toString() + ", ";
        }
        return current + " Score: " + HandScore();
    }
    public void resetHand(){
        hand.clear();
    }
    public int HandScore(){
        int handScore = 0;
        boolean isAce = false;
        for(int i=0; i < hand.size(); i++){
            String value = "";
            Card e = hand.get(i);
            value = e.getValue();
            if(value.equals("Ace")){
                if(isAce == true){ handScore = handScore + 1; }
                else{ isAce = true; }
            }
            else if(value.equals("Jack") || value.equals("Queen") || value.equals("King")){handScore = handScore + 10;}
            else{
                handScore = handScore + Integer.parseInt(value);
            }
        }
        if(isAce == true){
            if(handScore<11){ handScore = handScore + 11;}
            else{handScore = handScore + 1;}
        }
        return handScore;
    }
}
