import java.util.Scanner;
import java.util.concurrent.*;

public class Main {

	public static void main(String[] args) {

		try ( ExecutorService executor = Executors.newCachedThreadPool() ) {

			Cache cache = new Cache();

			do {

				Scanner in = new Scanner(System.in);
				String input = in.nextLine();

				if (input.equals("close") || input.equals("exit") || input.equals("quit"))
					break;
			
				input = input.replace(" ", "%20");
				DataFetcher fetcher = new DataFetcher(input);

				CompletableFuture<Double> future = CompletableFuture.supplyAsync(() -> {
					try {
						Coordinates coords = cache.getGeocode(fetcher.getCity());
						fetcher.setCoordinates(coords);
					} catch (Exception ex) {
						try {
							fetcher.fetchCoordinates();
							cache.addGeocode(fetcher.getCity(), fetcher.getCoordinates());
						} catch (Exception fetchEx) {
							System.out.println(fetchEx.getMessage());
						}
					}
					if (fetcher.getCoordinates() == null)
						throw new RuntimeException("Failed to fetch city coordinates from geocoding API");

					try {
						double temp = cache.getWeather(fetcher.getCoordinates());
						fetcher.setTemperature(temp);
					} catch (Exception ex) {
						try {
							fetcher.fetchTemperature();
							cache.addWeather(fetcher.getCoordinates(), fetcher.getTemperature());
						} catch (Exception fetchEx) {
							System.out.println(fetchEx.getMessage());
						}
					}
					if (fetcher.getTemperature() == Double.NaN)
						throw new RuntimeException("Failed to fetch temperature from meteo API");
					
					return fetcher.getTemperature();
				}, executor);

				future
					.exceptionally(ex -> {
						System.out.println(ex.getMessage());
						throw new RuntimeException(ex.getMessage());
					})
					.thenAcceptAsync(temp -> System.out.printf("Temperature in %s: %.1f\n", fetcher.getCity(), temp));
			
			} while (true);

		}

	}

}