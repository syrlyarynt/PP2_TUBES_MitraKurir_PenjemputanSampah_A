package kelompok4.praktikumpemrograman2.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class DataCache {
    private static final ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private static final long CACHE_DURATION = TimeUnit.MINUTES.toMillis(5);

    private static class CacheEntry {
        private final Object data;
        private final long timestamp;

        public CacheEntry(Object data) {
            this.data = data;
            this.timestamp = System.currentTimeMillis();
        }

        public boolean isExpired() {
            return System.currentTimeMillis() - timestamp > CACHE_DURATION;
        }
    }

    public static void put(String key, Object value) {
        if (value != null) {
            cache.put(key, new CacheEntry(value));
        }
    }

    public static Optional<Object> get(String key) {
        CacheEntry entry = cache.get(key);
        if (entry != null && !entry.isExpired()) {
            return Optional.of(entry.data);
        }
        cache.remove(key);
        return Optional.empty();
    }

    public static void clear() {
        cache.clear();
    }

    public static void remove(String key) {
        cache.remove(key);
    }

    public static void invalidateKeysByPrefix(String prefix) {
        cache.keySet().removeIf(key -> key.startsWith(prefix));
    }
}