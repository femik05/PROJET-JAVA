import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class WeatherClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8080;
    
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner scanner;
    
    public WeatherClient() {
        scanner = new Scanner(System.in);
    }
    
    public void connect() {
        try {
            System.out.println("🔌 Connexion au serveur météo...");
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            
            System.out.println("✅ Connecté au serveur !");
            
            // Thread pour recevoir les messages du serveur
            Thread serverListener = new Thread(this::listenToServer);
            serverListener.start();
            
            // Thread principal pour envoyer les messages
            handleUserInput();
            
        } catch (IOException e) {
            System.err.println("❌ Erreur de connexion: " + e.getMessage());
        }
    }
    
    private void listenToServer() {
        try {
            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                System.out.println(serverMessage);
            }
        } catch (IOException e) {
            if (!socket.isClosed()) {
                System.err.println("❌ Connexion perdue avec le serveur");
            }
        }
    }
    
    private void handleUserInput() {
        try {
            System.out.println("\n💬 Tapez vos commandes (ou 'help' pour l'aide):");
            
            String userInput;
            while ((userInput = scanner.nextLine()) != null) {
                if ("help".equalsIgnoreCase(userInput.trim())) {
                    showHelp();
                    continue;
                }
                
                if ("quit".equalsIgnoreCase(userInput.trim())) {
                    out.println("quit");
                    break;
                }
                
                if (!userInput.trim().isEmpty()) {
                    out.println(userInput);
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
        } finally {
            disconnect();
        }
    }
    
    private void showHelp() {
        System.out.println("\n📖 === AIDE ===");
        System.out.println("• Tapez le nom d'une ville pour obtenir la météo");
        System.out.println("• Exemples: 'Paris', 'London', 'Tokyo'");
        System.out.println("• 'quit' - Quitter l'application");
        System.out.println("• 'help' - Afficher cette aide");
        System.out.println("===============\n");
    }
    
    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("🔌 Déconnecté du serveur");
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la déconnexion: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        System.out.println("🌤 === CLIENT MÉTÉO ===");
        System.out.println("Démarrage du client...\n");
        
        WeatherClient client = new WeatherClient();
        
        // Gestion propre de l'arrêt
        Runtime.getRuntime().addShutdownHook(new Thread(client::disconnect));
        
        client.connect();
    }
}