import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private String current = "";
    private int score;
    private List<Card> hand = new ArrayList<>(11);

    public Player(String name){
        this.name = name;
    }

    public void winner(){score++;}
    public int getScore(){return score;}

    public void askCard(Card e){
        hand.add(e);
    }
    public String showHand(){
        for(int i = 0; i < hand.size(); i++){
            Card e = hand.get(i);
            current = current + e.toString() + ",";
        }
        return current;
    }
}
