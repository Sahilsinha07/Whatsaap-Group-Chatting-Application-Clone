
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server implements Runnable { // basically in multi threading we have 2 concepts first os extending the
                                          // thread class and second is we can implement the runnable interface (using
                                          // this)
    Socket socket; // global declaration of socket

    Server(Socket socket) { // constructor of server class
        try {
            this.socket = socket;
        } catch (Exception e) {
            e.printStackTrace();
        } // basicaly we made a client and passed it through a constructor and stored the
          // value globally
          // here we will be using the concept of multi- threading through which we will
          // be broadcasting our message to different clients in real time.
    }

    public static Vector client = new Vector(); // for making different users

    @Override
    public void run() { // this is basically we are overriding the run method as we have implemented a
                        // interface name runnable and according to java concepts if we implement a
                        // interface then we either have to override the abstract function or make the
                        // class abstract in which we are implementing the interface.
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // here socket is the client as the message would be send by the user and the
            // message would be recieved using socket so we will take socket as client and
            // read message from there - server
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            client.add(writer);
            while (true) { // inifinte loop as we wont stop recieving messages
                String data = reader.readLine().trim(); // this will read the message from the socket and trim for any
                                                        // extra spaces.
                System.out.println("RECIEVED " + data);
                for (int i = 0; i < client.size(); i++) {
                    try {
                        BufferedWriter bw = (BufferedWriter) client.get(i); // ye message nikale hae and type cast kia h
                                                                            // in buffered writter
                        bw.write(data);
                        bw.write("\r\n");
                        bw.flush();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) { // used for exceptional handling
            e.printStackTrace();
        }

    }

    public static void main(String args[]) throws Exception {
        ServerSocket s = new ServerSocket(2003);
        while (true) {
            // infinte loop for n number of clients
            Socket socket = s.accept();
            Server server = new Server(socket); // object of server class so constructor will be called
            Thread thread = new Thread(server); // object of thread class with argument server because on threads the
                                                // client will work and we have to connect the client with the server
            thread.start(); // this will call the function run function internally as it is a multi thread
                            // function
        }
    }
}