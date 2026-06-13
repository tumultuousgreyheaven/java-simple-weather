// import java.io.*;
// import java.net.URI;
// import java.net.http.*;
import java.util.Scanner;
import java.util.concurrent.*;

public class Main {

	public static void main(String[] args) {

		try ( ExecutorService executor = Executors.newCachedThreadPool() ) {

			Cache cache = new Cache();

			do {

				Scanner in = new Scanner(System.in);
				String input = in.nextLine();

				if (input.equals("close"))
					break;
			
				DataFetcher fetcher = new DataFetcher(input);
				executor.submit( () -> {
					try {
						Coordinates coords = cache.getGeocode(input);
						fetcher.setCoordinates(coords);
					} catch (Exception ex) {
						try {
							fetcher.fetchCoordinates();
							cache.addGeocode(input, fetcher.getCoordinates());
						} catch (Exception fetchEx) {
							fetchEx.getMessage();
						}
					}
					if (fetcher.getCoordinates() == null)
						return;
					// TODO: implement Future handling in the main thread

					try {
						double temp = cache.getWeather(fetcher.getCoordinates());
						fetcher.setTemperature(temp);
					} catch (Exception ex) {
						try {
							fetcher.fetchTemperature();
							cache.addWeather(fetcher.getCoordinates(), fetcher.getTemperature());
						} catch (Exception fetchEx) {
							fetchEx.getMessage();
						}
					}
				} );
			
			} while (true);

		}

	}

}