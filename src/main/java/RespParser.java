import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RespParser {

    public static List<String> parse(BufferedReader reader) throws IOException {
String line = reader.readLine();
if (line == null) return null;
int count = Integer.parseInt(line.substring(1));
List<String> parts = new ArrayList<>();
for (int i = 0; i < count; i++) {
    reader.readLine();
    parts.add(reader.readLine());
}
return parts;
    }
}