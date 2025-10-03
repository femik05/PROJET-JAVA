import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.json.*;

public class WeatherService {
    private static final String API_KEY = "481060c154e32447b23869d8c6e863f8"; // ⚠️ REMPLACEZ par votre clé !
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";
    private static final long CACHE_DURATION = 5 * 60 * 1000; // 5 minutes en millisecondes
    
    // Cache simple pour éviter trop d'appels API
    private ConcurrentHashMap<String, CachedWeatherData> weatherCache = new ConcurrentHashMap<>();
    
    public String getWeatherData(String city) throws Exception {
        String cacheKey = city.toLowerCase();
        
        // Vérifier le cache
        CachedWeatherData cachedData = weatherCache.get(cacheKey);
        if (cachedData != null && !cachedData.isExpired()) {
            System.out.println("📦 Données depuis le cache pour: " + city);
            return cachedData.getData();
        }
        
        // Récupérer depuis l'API
        System.out.println("🌐 Récupération depuis l'API pour: " + city);
        String weatherData = fetchWeatherFromAPI(city);
        
        // Mettre en cache
        weatherCache.put(cacheKey, new CachedWeatherData(weatherData));
        
        return weatherData;
    }
    
    private String fetchWeatherFromAPI(String city) throws Exception {
        String urlString = String.format("%s?q=%s&appid=%s&units=metric&lang=fr", 
                                        BASE_URL, URLEncoder.encode(city, "UTF-8"), API_KEY);
        
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new Exception("Ville non trouvée ou erreur API (code: " + responseCode + ")");
        }
        
        // Lire la réponse JSON
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder content = new StringBuilder();
        String inputLine;
        
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        
        in.close();
        connection.disconnect();
        
        // Parser et formater les données
        return parseAndFormatWeatherData(content.toString());
    }
    
    private String parseAndFormatWeatherData(String jsonData) throws Exception {
        JSONObject json = new JSONObject(jsonData);
        
        // Extraire les données principales
        String cityName = json.getString("name");
        String country = json.getJSONObject("sys").getString("country");
        
        JSONObject main = json.getJSONObject("main");
        double temperature = main.getDouble("temp");
        int humidity = main.getInt("humidity");
        double pressure = main.getDouble("pressure");
        double feelsLike = main.getDouble("feels_like");
        
        JSONObject weather = json.getJSONArray("weather").getJSONObject(0);
        String description = weather.getString("description");
        String icon = weather.getString("icon");
        
        JSONObject wind = json.getJSONObject("wind");
        double windSpeed = wind.getDouble("speed");
        
        // Formatage élégant
        StringBuilder result = new StringBuilder();
        result.append("\n🌍 ==========================================\n");
        result.append(String.format("📍 MÉTÉO POUR %s, %s\n", cityName.toUpperCase(), country));
        result.append("==========================================\n");
        result.append(String.format("🌡  Température: %.1f°C (ressenti %.1f°C)\n", temperature, feelsLike));
        result.append(String.format("☁️  Conditions: %s\n", capitalizeFirst(description)));

Əmmvnuel👾, [03/10/2025 09:43]
result.append(String.format("💧 Humidité: %d%%\n", humidity));
        result.append(String.format("🌬  Vent: %.1f m/s\n", windSpeed));
        result.append(String.format("🔽 Pression: %.0f hPa\n", pressure));
        result.append("==========================================\n");
        result.append("⏰ Données récupérées: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        result.append("\n==========================================");
        
        return result.toString();
    }
    
    private String capitalizeFirst(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    // Classe interne pour le cache
    private static class CachedWeatherData {
        private final String data;
        private final long timestamp;
        
        public CachedWeatherData(String data) {
            this.data = data;
            this.timestamp = System.currentTimeMillis();
        }
        
        public String getData() {
            return data;
        }
        
        public boolean isExpired() {
            return (System.currentTimeMillis() - timestamp) > CACHE_DURATION;
        }
    }
}
