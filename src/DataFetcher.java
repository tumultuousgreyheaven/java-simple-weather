// import java.io.*;
import java.net.URI;
import java.net.http.*;

public class DataFetcher {

    private String city;
    private double latitude;
    private double longitude;
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

    private void fetchCoordinates() {

        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + city + "&count=1&language=en";
        HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(urlString))
			.build();
        
        try {
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                latitude = JsonParser.findDoubleField(response.body(), "latitude");
                longitude = JsonParser.findDoubleField(response.body(), "longitude");
            } else {

            }

        } catch (Exception e) {
        }

    }

    private void fetchTemperature() {

    }

}