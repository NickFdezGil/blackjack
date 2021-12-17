import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Conexion extends Thread{
    final DataInputStream DIS;
    final DataOutputStream DOS;
    final Socket s;

    String message;

    public Conexion(Socket s, DataInputStream DIS, DataOutputStream DOS){
        this.s = s;
        this.DIS = DIS;
        this.DOS = DOS;
    }

    public void run(){
        while(true){
            try{

                message = DIS.readUTF();
                if(message.equals("Exit")) {
                    System.out.println("Closing");
                    this.s.close();
                    break;
                }


            }catch(IOException e){
                e.printStackTrace();
            }
        }
        try{
            this.DIS.close();
            this.DOS.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
