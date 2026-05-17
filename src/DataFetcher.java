// import java.io.*;
// import java.net.URI;
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

    }

    private void fetchTemperature() {

    }

}