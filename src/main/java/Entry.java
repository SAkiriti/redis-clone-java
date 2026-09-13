public class Entry {
    public String value;
    public long expiresAt;

    public Entry(String value, long expiresAt) {
        this.value = value;
        this.expiresAt = expiresAt;

    }

    public boolean isExpired(){
    
        
        return expiresAt != -1 && System.currentTimeMillis() > expiresAt;
    }
}
