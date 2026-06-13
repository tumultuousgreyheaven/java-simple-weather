import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {

    private static final long CACHE_TTL_SECS = 60;
    private final ConcurrentHashMap<String, GeocodeCacheEntry> geocodeCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Coordinates, WeatherCacheEntry> weatherCache = new ConcurrentHashMap<>();

    public Coordinates getGeocode(String city) throws Exception {
        GeocodeCacheEntry geocodeCacheEntry;
        if (
            geocodeCache.containsKey(city) &&
            !(geocodeCacheEntry = geocodeCache.get(city)).isExpired(CACHE_TTL_SECS)
        ) return geocodeCacheEntry.getCoords();
        throw new Exception("Cache miss for geocode");
    }

    public double getWeather(Coordinates coords) throws Exception { 
        WeatherCacheEntry weatherCacheEntry;
        if (
            weatherCache.containsKey(coords) &&
            !(weatherCacheEntry = weatherCache.get(coords)).isExpired(CACHE_TTL_SECS)
        ) return weatherCacheEntry.getTemp();
        throw new Exception("Cache miss for weather");
    }

    public void addGeocode(String city, Coordinates coords) {
        geocodeCache.put(
            city,
            new GeocodeCacheEntry(coords)
        );
    }

    public void addWeather(Coordinates coords, double temp) {
        weatherCache.put(
            coords,
            new WeatherCacheEntry(temp)
        );
    }

    private static class GeocodeCacheEntry {
        private final Coordinates coords;
        private final Instant createdAt;

        GeocodeCacheEntry(Coordinates coords) {
            this.coords = coords;
            this.createdAt = Instant.now();
        }

        public Coordinates getCoords() {
            return this.coords;
        }
        
        public boolean isExpired(long CACHE_TTL_SECS) {
            return Instant.now().getEpochSecond() - this.createdAt.getEpochSecond() > CACHE_TTL_SECS;
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

        public boolean isExpired(long CACHE_TTL_SECS) {
            return Instant.now().getEpochSecond() - this.createdAt.getEpochSecond() > CACHE_TTL_SECS;
        }
    }

}
