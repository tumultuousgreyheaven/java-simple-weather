// import java.io.*;
// import java.net.URI;
// import java.net.http.*;
import java.util.Scanner;
import java.util.concurrent.*;

public class Main {

	public static void main(String[] args) {

		/*
		try (FileWriter outFile = new FileWriter("temp.json", false)) {

			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://api.open-meteo.com/v1/forecast?latitude=60.00&longitude=30.00&current=temperature_2m"))
				.build();
			
			try {
				outFile.write(
					client.send(request, HttpResponse.BodyHandlers.ofString()).body()
				);
			} catch (InterruptedException ex) {
				System.out.println(ex.getMessage());
			}

			outFile.flush();

		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
		*/

		try ( ExecutorService executor = Executors.newCachedThreadPool() ) {

			Cache cache = new Cache();

			do {

				Scanner in = new Scanner(System.in);
				String input = in.next();

				if (input.equals("close"))
					break;
			
				DataFetcher fetcher = new DataFetcher(input);
				executor.submit( () -> {
					try {
						Coordinates coords = cache.getGeocode(input);
						fetcher.setCoordinates(coords);
					} catch (IllegalArgumentException ex) {
						fetcher.fetchCoordinates();
					}

					try {
						double temp = cache.getWeather(fetcher.getCoordinates());
						fetcher.setTemperature(temp);
					} catch (IllegalArgumentException ex) {
						fetcher.fetchTemperature();
					}
				} );
			
			} while (true);

		}

	}

}