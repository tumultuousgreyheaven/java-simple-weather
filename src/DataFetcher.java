// import java.io.*;
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
            // TODO: find "current" field in response as string, then find "temperature_2m" as double
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

}