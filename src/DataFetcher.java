import java.net.URI;
import java.net.http.*;

public class DataFetcher {

    private final String city;
    private Coordinates coords;
    private double temperature;
    private final HttpClient client;

    DataFetcher(String city) {
        this.city = city;
        client = HttpClient.newHttpClient();

        // necessary to recognize whether fetch procedure have failed
        this.coords = null;
        this.temperature = Double.NaN;
    }

    public void fetchCoordinates() throws Exception {

        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + this.city + "&count=1&language=en";
        HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(urlString))
			.build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            this.coords = new Coordinates(
                JsonParser.findDoubleField(response.body(), "latitude"),
                JsonParser.findDoubleField(response.body(), "longitude")
            );
        } else {
            throw new RuntimeException("Error while fetching from API: " + response.statusCode());
        }

    }

    public void fetchTemperature() throws Exception {

        String urlString =
            "https://api.open-meteo.com/v1/forecast?latitude=" +
            coords.getLatitude() + "&longitude=" +
            coords.getLongitude() + "&current=temperature_2m";
        HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(urlString))
			.build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            String current = JsonParser.findFieldAsString(response.body(), "current");
            this.temperature = JsonParser.findDoubleField(current, "temperature_2m");
        } else {
            throw new RuntimeException("Error while fetching from API: " + response.statusCode());
        }

    }

    public void setCoordinates(Coordinates coords) {
        this.coords = coords;
    }

    public void setTemperature(double temp) {
        this.temperature = temp;
    }

    public Coordinates getCoordinates() {
        return this.coords;
    }

    public double getTemperature() {
        return this.temperature;
    }

}