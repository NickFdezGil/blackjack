import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    int port;
    String clientName = " ";
    Scanner scanner;
    ServerSocket server;
    DataInputStream DIS;
    DataOutputStream DOS;
    boolean roundOnGoing;

    public Server(int port, Scanner scanner){
        this.port = port;
        this.scanner = scanner;
        try{
            this.server = new ServerSocket(port);
            System.out.println("Server listening to port: "+port);
            System.out.println("Please, wait while we start the game");
            tableTry obj = new tableTry();
            obj.initialize(scanner);
            while(true){
                Socket s = null;
                try{
                    s = this.server.accept();
                    System.out.println("A new client is connected: " + s);
                    DIS = new DataInputStream(s.getInputStream());
                    DOS = new DataOutputStream(s.getOutputStream());
                    clientName = DIS.readUTF();
                    System.out.println("Assigning new thread for this client");
                    System.out.println("New player: " + clientName);
                    DOS.writeUTF("Welcome to the Blackjack");
                    //Thread t = new Conexion(s, DIS, DOS);
                    //t.start();
                    obj.addPlayer(new Player(clientName));
                    for(int i = 0; i < obj.getRounds(); i++){
                        roundOnGoing = true;
                        DOS.writeUTF("This is round " + (i+1) + " of " + obj.getRounds());
                        obj.firstDeal();
                        DOS.writeUTF(obj.gameToString());
                        while(roundOnGoing == true) {
                            DOS.writeUTF("Write 'Deal' for new card, Write 'Stay' to fold");
                            String answer = DIS.readUTF();
                            if(answer.equals("Deal")){
                                obj.deal(0);
                                DOS.writeUTF(obj.gameToString());
                                System.out.println(obj.gameToString());
                            }else if(answer.equals("Stay")){
                                DOS.writeUTF(obj.gameToString());
                                System.out.println(obj.gameToString());
                                roundOnGoing = false;
                            }
                            if(obj.check21(0)==true){
                                roundOnGoing = false;
                            }
                        }
                        DOS.writeUTF("Turn for the dealer");
                        DOS.writeUTF(obj.showDealerHand());
                        obj.dealDealerUnit16();
                        DOS.writeUTF(obj.showDealerHand());
                        DOS.writeUTF("Let's select the winner");
                        DOS.writeUTF(obj.selectWinner());
                        DOS.writeUTF(obj.showTotalWins());
                        obj.clearHands();
                        obj.shuffle();
                        System.out.println("Round Over");
                        DOS.writeUTF("Round over");
                    }
                    DOS.writeUTF(obj.getFinalWinner());
                }catch(Exception e){
                    s.close();
                    e.printStackTrace();
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException{
        Scanner newScanner = new Scanner(System.in);
        System.out.println("Select port");
        int port = newScanner.nextInt();
        Server server = new Server(port,newScanner);
    }
}
