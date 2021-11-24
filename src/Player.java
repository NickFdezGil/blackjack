public class Player {
    private String name;
    private int score;

    public Player(String name){
        this.name = name;
    }
    public void winner(){
        score++;
    }
    public int getScore(){
        return score;
    }
}
