import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(6379);
        serverSocket.setReuseAddress(true);
        System.out.println("Listening on port 6379...");

        Socket client = serverSocket.accept();
        System.out.println("Client connected");

        InputStream in = client.getInputStream();
        OutputStream out = client.getOutputStream();
        byte[] buffer = new byte[1024];

        while (true) {
            int bytesRead = in.read(buffer);
            if (bytesRead == -1) {
                System.out.println("Client disconnected");
                break;
            }
            out.write("+PONG\r\n".getBytes(StandardCharsets.UTF_8));
            out.flush();
        }

        client.close();
        serverSocket.close();
    }
}
