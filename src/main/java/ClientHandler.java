import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler implements Runnable {

    private Socket socket;
    private static final Map<String, String> store = new ConcurrentHashMap<>();

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream out = socket.getOutputStream();

            while (true) {
                List<String> command = RespParser.parse(reader);
                if (command == null || command.isEmpty()) {
                    System.out.println("Client disconnected");
                    break;
                }

                String name = command.get(0).toUpperCase();

                if (name.equals("PING")) {
                    out.write("+PONG\r\n".getBytes(StandardCharsets.UTF_8));
                } else if (name.equals("ECHO")) {
                    String value = command.get(1);
                    String response = "$" + value.length() + "\r\n" + value + "\r\n";
                    out.write(response.getBytes(StandardCharsets.UTF_8));
                } else if (name.equals("SET")) {
                    store.put(command.get(1), command.get(2));
                    out.write("+OK\r\n".getBytes(StandardCharsets.UTF_8));
                } else if (name.equals("GET")) {
                    String value = store.get(command.get(1));
                    if (value == null) {
                        out.write("$-1\r\n".getBytes(StandardCharsets.UTF_8));
                    } else {
                        String response = "$" + value.length() + "\r\n" + value + "\r\n";
                        out.write(response.getBytes(StandardCharsets.UTF_8));
                    }
                } else {
                    out.write("-ERR unknown command\r\n".getBytes(StandardCharsets.UTF_8));
                }

                out.flush();
            }

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
