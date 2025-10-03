    # PROJET-JAVA

    Əmmvnuel👾, [03/10/2025 09:48]
    # 🌤 Mini Serveur Météo

    Projet Java orienté réseau - TCP Socket + API OpenWeatherMap

    ## 📋 Description

    Serveur météo TCP permettant à plusieurs clients (max 10) de consulter simultanément les données météorologiques de différentes villes via l'API OpenWeatherMap.

    ### ✨ Fonctionnalités
    - ✅ Serveur TCP multi-clients (max 10 simultanés)
    - ✅ Récupération des données météo via API OpenWeatherMap
    - ✅ Cache intelligent (évite les appels répétés)
    - ✅ Interface client interactive
    - ✅ Gestion des erreurs et déconnexions propres

    ### 📊 Données météo affichées
    - 🌡 Température actuelle et ressentie
    - ☁️ Conditions météorologiques
    - 💧 Taux d'humidité
    - 🌬 Vitesse du vent
    - 🔽 Pression atmosphérique

    ## 🚀 Installation et Compilation

    ### Prérequis
    - Java 11+ installé
    - Maven 3.6+ installé
    - Clé API OpenWeatherMap (gratuite)

    ### 1. Configuration de l'API
    1. Inscrivez-vous sur [OpenWeatherMap](https://openweathermap.org/api)
    2. Récupérez votre clé API gratuite
    3. Modifiez WeatherService.java ligne 9 :
    private static final String API_KEY = "VOTRE_CLE_API_ICI";

    ### 2. Compilation
    # Compiler le projet
    mvn clean compile

    # Ou compilation manuelle si pas Maven
    javac -cp ".:lib/*" *.java

    ## ▶️ Exécution

    ### Méthode 1 : Avec Maven
    # Terminal 1 - Démarrer le serveur
    mvn exec:java@run-server

    # Terminal 2,3,4... - Démarrer les clients
    mvn exec:java@run-client

    ### Méthode 2 : Compilation manuelle
    # Terminal 1 - Serveur
    java -cp ".:lib/*" WeatherServer

    # Terminal 2,3,4... - Clients
    java -cp ".:lib/*" WeatherClient

    ### Méthode 3 : Scripts fournis
    # Démarrer le serveur
    ./start-server.sh

    # Démarrer un client
    ./start-client.sh

    ## 🎯 Utilisation

    ### Côté Serveur
    1. Le serveur démarre sur le port 8080
    2. Affiche les connexions/déconnexions clients
    3. Log des requêtes météo
    4. Ctrl+C pour arrêter proprement

    ### Côté Client
    1. Connexion automatique au serveur
    2. Saisissez le nom d'une ville (ex: "Paris", "London", "Tokyo")
    3. Tapez help pour l'aide
    4. Tapez quit pour quitter

    ### Exemple d'utilisation
    🌤 === SERVEUR MÉTÉO ===
    Tapez le nom d'une ville pour obtenir la météo
    Tapez 'quit' pour quitter

    > Paris

    🌍 ==========================================
    📍 MÉTÉO POUR PARIS, FR
    ==========================================
    🌡 Température: 15.3°C (ressenti 14.8°C)
    ☁️ Conditions: Nuageux
    💧 Humidité: 73%
    🌬 Vent: 3.2 m/s
    🔽 Pression: 1013 hPa
    ==========================================
    ⏰ Données récupérées: 14:32:15
    ==========================================

    ## 🧪 Tests

    ### Tests manuels recommandés
    1. Test connexion multiple : Lancer 3+ clients simultanés
    2. Test villes diverses : Paris, London, Tokyo, New York
    3. Test ville inexistante : "VilleInexistante123"
    4. Test cache : Même ville 2x rapidement
    5. Test déconnexion : quit et Ctrl+C

    ### Captures d'écran attendues
    - Serveur avec multiple clients connectés
    - Client affichant météo de différentes villes
    - Gestion d'erreur (ville non trouvée)

    ## 🏗 Architecture

    ### Schéma réseau
    [Client 1] ────┐
    [Client 2] ────┤
    [Client 3] ────┼─── TCP:8080 ──► [Serveur] ──── HTTP ──► [API OpenWeatherMap]
        ...        │                     ↑
    [Client 10]────┘              Cache local (5min)

    ### Classes principales
    | Classe | Responsabilité | Méthodes clés |
    |--------|---------------|---------------|
    | WeatherServer | Serveur TCP principal | start(), stop() |
    | ClientHandler | Gestion client individuel | run() |
    | WeatherService | Interface API + Cache | getWeatherData() |
    | WeatherClient | Client TCP interactif | connect(), handleUserInput() |

    ## 🔧 Configuration

    ### Paramètres modifiables
    - Port serveur : WeatherServer.PORT (défaut: 8080)
    - Max clients : WeatherServer.MAX_CLIENTS (défaut: 10)
    - Cache durée : WeatherService.CACHE_DURATION (défaut: 5min)
    - Timeouts API : WeatherService.fetchWeatherFromAPI() (défaut: 5s)

    ## 📝 Commits Git

    Əmmvnuel👾, [03/10/2025 09:48]
    Le projet suit cette progression de commits :
    1. feat: initialisation projet + structure Maven
    2. feat: serveur TCP basique + multi-threading
    3. feat: intégration API OpenWeatherMap + parsing JSON
    4. feat: client interactif + gestion erreurs
    5. feat: cache système + optimisations
    6. docs: README complet + documentation

    ## 🚧 Limitations et améliorations

    ### Limitations actuelles
    - API gratuite limitée à 1000 calls/jour
    - Cache en mémoire (perdu au redémarrage)
    - Pas d'authentification clients
    - Interface textuelle uniquement

    ### Pistes d'amélioration
    - 🔄 Cache persistant (base de données)
    - 🔐 Authentification et sessions
    - 🌍 Support géolocalisation
    - 📱 Interface graphique (JavaFX)
    - 📈 Historique et tendances
    - 🌦 Prévisions étendues

    ## 👥 Équipe

    Groupe de 2 étudiants
    - Développement collaboratif via Git
    - Répartition : Serveur/API + Client/Interface

    ---
    *Projet réalisé dans le cadre du cours Java orienté réseau*