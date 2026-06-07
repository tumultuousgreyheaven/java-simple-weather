// import java.io.*;
import java.net.URI;
import java.net.http.*;

public class DataFetcher {

    private String city;
    private Coordinates coords;
    private double temperature;
    private HttpClient client;

    DataFetcher(String city) {
        this.city = city;
        client = HttpClient.newHttpClient();
    }

    public void execute() {

        fetchCoordinates();
        fetchTemperature();

    }

    public void fetchCoordinates() {

        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + city + "&count=1&language=en";
        HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(urlString))
			.build();
        
        try {
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                coords = new Coordinates(
                    JsonParser.findDoubleField(response.body(), "latitude"),
                    JsonParser.findDoubleField(response.body(), "longitude")
                );
            } else {

            }

        } catch (Exception e) {
        }

    }

    public void fetchTemperature() {

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