import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
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

}
