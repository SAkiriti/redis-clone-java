import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(6379);
        serverSocket.setReuseAddress(true);
        System.out.println("Listening on port 6379...");

        Socket client = serverSocket.accept();
        System.out.println("Client connected: " + client.getRemoteSocketAddress());

        InputStream in = client.getInputStream();
        byte[] buffer = new byte[1024];
        int bytesRead = in.read(buffer);

        String raw = new String(buffer, 0, bytesRead);
        System.out.println("Raw bytes received:");
        System.out.println(raw.replace("\r", "\\r").replace("\n", "\\n"));

        client.close();
        serverSocket.close();
    }
}