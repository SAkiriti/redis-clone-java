import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(6379);
        serverSocket.setReuseAddress(true);
        System.out.println("Listening on port 6379...");
        ExecutorService pool = Executors.newCachedThreadPool();

        while (true){
            Socket client = serverSocket.accept();
            System.out.println("Client connected");
            pool.submit(new ClientHandler(client));
        }
    }
}
