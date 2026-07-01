# Simple weather HTTP-client on Java
This simple console HTTP-client fetches weather data from Open Meteo API.

After start user needs to input area name (it can be a city or a town, a village, a district in a city, etc). Open Meteo Geocoding API "converts" it into coordinates, and, after that, weather API is used and temperature information from json-formatted response is obtained.

Two main features:
- Asynchronous programming: each city is processed in a distinct asynchronous task, which is managed by `ExecutorService`. `CompletableFuture` is used in order to process exceptions and print temperature without main thread blocking.
- Cache usage: lazily cleared cache, based on `ConcurrentHashMap` is used. Cache is impemented both for geocoding (mapping city name to coordinates) and for weather (mapping coordinates to temperature).

Also, simplified json-parser with all of neccesary functionality for this app is implemented. It uses regexp search for finding double-valued fields. For finding object-valued fields it uses brace-validation algorithm with stack.
