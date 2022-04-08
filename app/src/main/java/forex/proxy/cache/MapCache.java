package forex.proxy.cache;

import java.util.HashMap;
import java.util.Map;

public class MapCache<K,V> implements Cache<K,V> {

    private final Map<K, V> map = new HashMap<>();

    public MapCache() {
    }

    @Override
    public V get(K key) {
        return map.get(key);
    }

    @Override
    public void set(K key, V value) {
        map.put(key, value);
    }
}
