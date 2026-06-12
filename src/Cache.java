import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {

    private static final long cacheTTLsecs = 60;
    private final ConcurrentHashMap<String, GeocodeCacheEntry> geocodeCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Coordinates, WeatherCacheEntry> weatherCache = new ConcurrentHashMap<>();

    public Coordinates getGeocode(String city) throws Exception {
        GeocodeCacheEntry geocodeCacheEntry = geocodeCache.compute(city, (key, cachedCoords) -> {
            if (cachedCoords != null && !cachedCoords.isExpired(cacheTTLsecs))
                return cachedCoords;
            return null;
        });
        if (geocodeCacheEntry == null)
            throw new Exception("Cache miss for geocode");
        else
            return geocodeCacheEntry.getCoords();
    }

    public double getWeather(Coordinates coords) throws Exception {
        WeatherCacheEntry weatherCacheEntry = weatherCache.compute(coords, (key, cachedTemp) -> {
            if (cachedTemp != null && !cachedTemp.isExpired(cacheTTLsecs))
                return cachedTemp;
            return null;
        });
        if (weatherCacheEntry == null)
            throw new Exception("Cache miss for weather");
        else
            return weatherCacheEntry.getTemp();
    }

    private static class GeocodeCacheEntry {
        private final Coordinates coords;
        private final Instant createdAt;

        GeocodeCacheEntry(double latitude, double longitude) {
            this.coords = new Coordinates(latitude, longitude);
            this.createdAt = Instant.now();
        }

        public Coordinates getCoords() {
            return this.coords;
        }
        
        public boolean isExpired(long cacheTTLsecs) {
            return Instant.now().getEpochSecond() - this.createdAt.getEpochSecond() > cacheTTLsecs;
        }
    }

    private static class WeatherCacheEntry {
        private final double temperature;
        private final Instant createdAt;

        WeatherCacheEntry(double temperature) {
            this.temperature = temperature;
            this.createdAt = Instant.now();
        }

        public double getTemp() {
            return this.temperature;
        }

        public boolean isExpired(long cacheTTLsecs) {
            return Instant.now().getEpochSecond() - this.createdAt.getEpochSecond() > cacheTTLsecs;
        }
    }

}
