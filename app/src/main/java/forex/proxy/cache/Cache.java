package forex.proxy.cache;

public interface Cache {
    public String get(String key);
    public void set(String key, String value);
}
