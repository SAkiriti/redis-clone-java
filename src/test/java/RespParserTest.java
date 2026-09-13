import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;

public class RespParserTest {

    @Test
    void parsesSingleElementCommand() throws Exception {
        BufferedReader reader = new BufferedReader(new StringReader("*1\r\n$4\r\nPING\r\n"));
        List<String> result = RespParser.parse(reader);

        assertEquals(1, result.size());
        assertEquals("PING", result.get(0));
    }
        @Test
    void parsesMultiElementCommand() throws Exception {
        BufferedReader reader = new BufferedReader(new StringReader("*3\r\n$3\r\nSET\r\n$3\r\nfoo\r\n$3\r\nbar\r\n"));
        List<String> result = RespParser.parse(reader);

        assertEquals(3, result.size());
        assertEquals("SET", result.get(0));
        assertEquals("foo", result.get(1));
        assertEquals("bar", result.get(2));
    }
        @Test
void parsesNullOnEmptyInput() throws Exception {
    BufferedReader reader = new BufferedReader(new StringReader(""));
    List<String> result = RespParser.parse(reader);

    assertNull(result);
}

        @Test
void parsesValueWithSpaces() throws Exception {
    BufferedReader reader = new BufferedReader(new StringReader("*3\r\n$3\r\nSET\r\n$8\r\ngreeting\r\n$11\r\nhello world\r\n"));
    List<String> result = RespParser.parse(reader);

        assertEquals(3, result.size());
        assertEquals("SET", result.get(0));
        assertEquals("greeting", result.get(1));
        assertEquals("hello world", result.get(2));
}

}
