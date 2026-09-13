import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClientHandlerTest {

    @BeforeEach
    void clearStore() {
        ClientHandler.store.clear();
    }

    @Test
    void freshKeyReturnsValue() {
        Entry entry = new Entry("bar", System.currentTimeMillis() + 10000);
        ClientHandler.store.put("foo", entry);

        assertEquals("bar", ClientHandler.lookup("foo"));
    }

    @Test
    void expiredKeyReturnsNullAndIsRemoved() {
        Entry entry = new Entry("bar", System.currentTimeMillis() - 1000);
        ClientHandler.store.put("foo", entry);

        assertNull(ClientHandler.lookup("foo"));
        assertFalse(ClientHandler.store.containsKey("foo"));
    }
}