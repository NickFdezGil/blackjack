public class Card {
    private String value;
    private String type;

    //Create the object Card to be use in Table, Player and Dealer
    public Card(String v, String t){
        this.value = v;
        this.type = t;
    }

    public String getValue(){
        return value;
    }
    @Override
    public String toString() {
        return "Card{" +
                "value='" + value + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
