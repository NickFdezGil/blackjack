
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author thanuja
 *
 * A simple server class, listen to a port and serves clients when they connect..
 *
 */
public class Server
{
    int portnumber;
    ServerSocket server_socket;

    // reference variable to store IO streams
    DataInputStream dis;
    DataOutputStream dos;

    /**
     * Constructor to run the server..
     * @param port
     */
    public Server(int port) {

        this.portnumber = port;

        try {
            this.server_socket = new ServerSocket(port);

            System.out.println("## server is listening to port: " + port + " waiting for client connections..");

            while (true) //infinite while loop
            {
                Socket s = null; //Declare a variable s of type socket and set it to null

                try
                {
                    // socket object to receive incoming client requests
                    s = this.server_socket.accept();

                    System.out.println("A new client is connected : " + s);

                    // obtaining input and out streams
                    dis = new DataInputStream(s.getInputStream());
                    dos = new DataOutputStream(s.getOutputStream());

                    System.out.println("Assigning new thread for this client");

                    // create a new thread to handle the connected client
                    Thread t = new SampleClientHandler(s, dis, dos); //declare a new thread t of type ClientHandler

                    // Invoking the start() method
                    t.start(); //Start the client handler

                } // End try part
                catch (Exception e){
                    s.close();
                    e.printStackTrace();
                } // End catch
            } // End while

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    /**
     * Main program...
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException
    {

        // Get port from command line argument
        // int port = Integer.parseInt(args[0]);

        // server is listening on port 5056
        int port = 5056;
        Server server = new Server(port);

    } // End Main
} // End Server Class


