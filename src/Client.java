import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client{
    String ip;
    int port;
    String name;
    DataOutputStream DOS;
    DataInputStream DIS;

    public Client(String ip, String name, int port){
        this.ip = ip;
        this.name = name;
        this.port = port;

        Scanner scn = new Scanner(System.in);
        try{
            Socket s = new Socket(this.ip, this.port);
            DIS = new DataInputStream(s.getInputStream());
            DOS = new DataOutputStream(s.getOutputStream());
            DOS.writeUTF(name);
            while(true){
                System.out.println(DIS.readUTF());
                String tosend = scn.nextLine();
                DOS.writeUTF(tosend);

                if(tosend.equals("Exit")){
                    System.out.println("Closing");
                    s.close();
                    break;
                }
                String recieved = DIS.readUTF();
                System.out.println(recieved);
            }
            scn.close();
            DIS.close();
            DOS.close();
        }catch(UnknownHostException e) {
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException{
        String ipAdd, CName;
        int SPort;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please, state your name");
        CName = scanner.nextLine();
        System.out.println("Input Server IP");
        ipAdd = scanner.nextLine();
        System.out.println("Input Server port");
        SPort = scanner.nextInt();

        Client client = new Client(ipAdd, CName, SPort);

    }
}
