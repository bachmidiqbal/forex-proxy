package forex.proxy.cache;

public interface Cache<K,V> {
    public V get(K key);
    public void set(K key, V value);
    public void delete(K key);
}
