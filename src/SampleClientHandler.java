
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


/**
 * A client handler class.
 * an instance of this class is created when a new client client is connected..
 * Each instance acts as a separate thread.
 * @author thanuja
 *
 */
class SampleClientHandler extends Thread
{

    final DataInputStream dis; //Declare dis as DataInputStream
    final DataOutputStream dos; //Declare dos as DataOutputStream

    final Socket s; //Declare s as a Socket

    /**
     * Constructor.
     *
     * @param s
     * @param dis
     * @param dos
     */
    public SampleClientHandler(Socket s, DataInputStream dis, DataOutputStream dos)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    /**
     * run method, called when a client handler thread is starting..
     * responds to client requests
     */
    public void run()
    {
        //Declare string to receive information
        String received;

        //Infinite loop setup
        while (true)
        {
            try {

                // Ask user what he wants
                dos.writeUTF("Welcome to the server connection. Type something...");

                // receive the answer from client
                received = dis.readUTF();

                if(received.equals("Exit"))
                {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    break;
                }

                // server response is simply the upper case of client request.
                dos.writeUTF("Server response: " + received.toUpperCase());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try
        {
            // closing resources
            this.dis.close();
            this.dos.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
