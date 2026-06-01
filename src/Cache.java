import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {

    private static final double cacheTTLsecs = 60.0;
    private final ConcurrentHashMap<String, GeocodeCacheEntry> geocodeCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Coordinates, WeatherCacheEntry> weatherCache = new ConcurrentHashMap<>();

    public Coordinates getGeocode(String city) throws IllegalArgumentException {
        // geocodeCache.compute
        throw new IllegalArgumentException("No relevant cache entry");
    }

    private static class GeocodeCacheEntry {
        private final Coordinates coords;
        private final Instant createdAt;

        GeocodeCacheEntry(double latitude, double longitude) {
            this.coords = new Coordinates(latitude, longitude);
            this.createdAt = Instant.now();
        }
    }

    private static class WeatherCacheEntry {
        private final double temperature;
        private final Instant createdAt;

        WeatherCacheEntry(double temperature) {
            this.temperature = temperature;
            this.createdAt = Instant.now();
        }
    }

}
