import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class WeatherServer {
    private static final int PORT = 8080;
    private static final int MAX_CLIENTS = 10;
    private ServerSocket serverSocket;
    private ExecutorService clientHandler;
    private WeatherService weatherService;
    
    public WeatherServer() {
        clientHandler = Executors.newFixedThreadPool(MAX_CLIENTS);
        weatherService = new WeatherService();
    }
    
    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("🌤  Serveur météo démarré sur le port " + PORT);
            System.out.println("En attente de clients...");
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("✅ Nouveau client connecté: " + clientSocket.getInetAddress());
                clientHandler.submit(new ClientHandler(clientSocket, weatherService));
            }
        } catch (IOException e) {
            System.err.println("Erreur serveur: " + e.getMessage());
        }
    }
    
    public void stop() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            clientHandler.shutdown();
            System.out.println("🛑 Serveur arrêté");
        } catch (IOException e) {
            System.err.println("Erreur lors de l'arrêt: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        WeatherServer server = new WeatherServer();
        
        // Gestion propre de l'arrêt
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
        
        server.start();
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private WeatherService weatherService;
    private BufferedReader in;
    private PrintWriter out;
    
    public ClientHandler(Socket socket, WeatherService weatherService) {
        this.clientSocket = socket;
        this.weatherService = weatherService;
    }
    
    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            
            // Message de bienvenue
            out.println("🌤 === SERVEUR MÉTÉO ===");
            out.println("Tapez le nom d'une ville pour obtenir la météo");
            out.println("Tapez 'quit' pour quitter");
            out.println("===========================");
            
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if ("quit".equalsIgnoreCase(inputLine.trim())) {
                    out.println("👋 Au revoir !");
                    break;
                }
                
                if (!inputLine.trim().isEmpty()) {
                    String city = inputLine.trim();
                    System.out.println("📍 Demande météo pour: " + city);
                    
                    try {
                        String weatherData = weatherService.getWeatherData(city);
                        out.println(weatherData);
                    } catch (Exception e) {
                        out.println("❌ Erreur: " + e.getMessage());
                    }
                }
                
                out.println("\n➡️ Entrez une autre ville (ou 'quit'):");
            }
            
        } catch (IOException e) {
            System.err.println("Erreur client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("🔌 Client déconnecté");
            } catch (IOException e) {
                System.err.println("Erreur fermeture client: " + e.getMessage());
            }
        }
    }
}