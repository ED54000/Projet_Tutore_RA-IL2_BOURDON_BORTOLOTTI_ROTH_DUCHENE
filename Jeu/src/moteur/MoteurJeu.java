package moteur;
//squelette adapté et modifié du moteur de Zeldiablo inspiré de :
//https://github.com/zarandok/megabounce/blob/master/MainCanvas.java

import entites.enemies.Ennemy;
import entites.enemies.Giant;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import laby.ModeleLabyrinth;
import laby.controllers.ControllerSimpleMode;
import laby.views.*;
import steering_astar.Steering.PathfollowingBehavior;
import steering_astar.Steering.Vector2D;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

import static laby.ModeleLabyrinth.getScreenSize;
import static moteur.TimeManagement.modifyFPS;


// copied from: https://gist.github.com/james-d/8327842
// and modified to use canvas drawing instead of shapes

public class MoteurJeu extends Application {

    /**
     * gestion du temps : nombre de frame par secondes et temps par iteration
     */
    static double FPS = 100;
    static final int BASE_FPS = 100;


    /**
     * jeu en Cours et renderer du jeu
     */
    private static Jeu jeu = null;
    public static String labyFile;
    public static Stage primaryStage;
    ModeleLabyrinth laby = (ModeleLabyrinth) MoteurJeu.jeu;
    // initialisation du canvas de dessin et du container
    Canvas canvas = new Canvas();
    final Pane canvasContainer = new Pane(canvas);
    private Label objectifLabel;
    ConfigWindow configWindow = new ConfigWindow(laby,primaryStage,canvas,this);
    ViewGraphiqueDirect viewGraphiqueDirect ;
    ViewGraphiqueObjectif viewGraphiqueObjectif;
    ViewLabyrinth viewLabyrinth;
    ViewLogs viewLogs ;
    ViewGraphique viewGraphique;

    /**
     * lancement d'un jeu
     *
     * @param jeu jeu a lancer
     */
    public static void launch(Jeu jeu) {
        // le jeu en cours et son afficheur
        MoteurJeu.jeu = jeu;

        // si le jeu existe, on lance le moteur de jeu
        if (jeu != null)
            launch();
    }


    public static void setLaby(ModeleLabyrinth laby) {
        MoteurJeu.jeu = laby;
    }

    //#################################
    // SURCHARGE Application
    //#################################

    /**
     * creation de l'application avec juste un canvas et des statistiques
     */
    public void start(Stage primaryStage) throws IOException {
        configWindow.show(primaryStage);
    }


    public void startJeu(Stage primaryStage) {
        canvas.widthProperty().bind(canvasContainer.widthProperty());
        canvas.heightProperty().bind(canvasContainer.heightProperty());
        SimpleMode mode = new SimpleMode();

        VBox ContainerLogs = new VBox();
        Label title = new Label("Logs");
        title.setStyle("-fx-font-weight: bold");
        title.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        VBox logs = new VBox();
        logs.setMinWidth(350);
        logs.setPrefWidth(getScreenSize().width / 5);
        logs.setPadding(new Insets(10));
        logs.setSpacing(10);
        ModeleLabyrinth.setLogs("Manche 1");

        //Ajout d'un scrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setFitToWidth(false); // Permet au ScrollPane de scroller horizontalement
        scrollPane.setPrefSize(getScreenSize().width / 5, getScreenSize().height);
        scrollPane.setContent(logs);

        ContainerLogs.getChildren().addAll(title, scrollPane);

        // création des vues
        ViewGraphiqueDirect viewGraphiqueDirect = new ViewGraphiqueDirect(laby);
        ViewGraphiqueObjectif viewGraphiqueObjectif = new ViewGraphiqueObjectif(laby);
        ViewLabyrinth viewLabyrinth = new ViewLabyrinth(laby, canvas);
        ViewLogs viewLogs = new ViewLogs(laby, ContainerLogs, viewGraphiqueDirect);
        ViewGraphique viewGraphique = new ViewGraphique(laby);
        //enregistrement des observateurs
        laby.registerObserver(viewLabyrinth);
        laby.registerObserver(viewLogs);
        laby.registerObserver(viewGraphique);
        laby.registerObserver(viewGraphiqueDirect);
        laby.registerObserver(viewGraphiqueObjectif);
        laby.registerObserver(obj -> updateObjectiveLabel());

        final BorderPane root = new BorderPane();
        root.setCenter(canvasContainer);
        //ajout des logs
        root.setRight(ContainerLogs);

        String buttonStyle = """
    -fx-background-color: #4CAF50;
    -fx-text-fill: white;
    -fx-font-size: 14px;
    -fx-padding: 8px 16px;
    -fx-background-radius: 5px;
    -fx-cursor: hand;
    -fx-font-weight: bold;
    -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);
""";

        String buttonSelectedStyle = """
    -fx-background-color: #34495e;
    -fx-text-fill: #ecf0f1;
""";

// Création des boutons avec style uniformisé
        ToggleButton switchMode = new ToggleButton("Mode simple");
        ToggleButton speedUpButton = new ToggleButton("▶▶ x2");
        ToggleButton slowDownButton = new ToggleButton("▶▶ x0.5");
        ToggleButton pauseButton = new ToggleButton("️ || Pause");
        ToggleButton helpButton = new ToggleButton("❔ Aide");
        ToggleButton graphicsButton = new ToggleButton("📊 Graphiques");

// Application du style de base
        List<ToggleButton> buttons = Arrays.asList(
                switchMode, speedUpButton, slowDownButton,
                pauseButton, helpButton, graphicsButton
        );

        buttons.forEach(button -> {
            button.setStyle(buttonStyle);
            button.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                button.setStyle(isSelected ? buttonStyle + buttonSelectedStyle : buttonStyle);
            });
        });

// Configuration des contrôleurs
        ControllerSimpleMode controllerSimpleMode = new ControllerSimpleMode(laby, viewLabyrinth, switchMode, mode);
        switchMode.setOnAction(e -> {
            controllerSimpleMode.handle(null); // Gestion du mode
            if (!switchMode.isSelected()) {
                switchMode.setStyle(buttonStyle);
            } else {
                switchMode.setStyle(buttonStyle + buttonSelectedStyle);
            }
        });

        TimeManagement timeManagement = new TimeManagement(jeu, laby, speedUpButton, slowDownButton, pauseButton);

        speedUpButton.setOnAction(e -> {
            TimeManagement.modifySpeed((BASE_FPS * 2), speedUpButton, slowDownButton);
        });

        slowDownButton.setOnAction(e -> {
            TimeManagement.modifySpeed((BASE_FPS / 2), slowDownButton, speedUpButton);
        });

        pauseButton.setOnAction(e -> {
            TimeManagement.pause();
        });

        helpButton.setOnAction(e -> openHelpWindow());


        ViewGraphicsWindow graphicsWindow = new ViewGraphicsWindow(laby, viewGraphiqueDirect, viewGraphique, viewGraphiqueObjectif);
        laby.registerObserver(graphicsWindow);

        graphicsButton.setOnAction(e -> {
            if (graphicsButton.isSelected()) {
                graphicsWindow.show();
            } else {
                graphicsWindow.hide();
            }
        });

        objectifLabel = new Label();
        updateObjectiveLabel();

        HBox controls = new HBox(10, switchMode, slowDownButton, pauseButton, speedUpButton, helpButton, graphicsButton, objectifLabel);
        root.setTop(controls);

        // creation de la scene
        final Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        Image icon = new Image("icon.png");
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Tower Defense");
        primaryStage.show();

        // lance la boucle de jeu
        jeu.init(canvas);
        startAnimation(canvas, FPS);
    }

    /**
     * gestion de l'animation (boucle de jeu)
     *
     * @param canvas le canvas sur lequel on est synchronise
     */
    private void startAnimation(final Canvas canvas, double fps) {
        modifyFPS(fps); // Initialise l'animation avec un FPS donné
    }


    private void openHelpWindow() {
        HelpWindow helpWindow = HelpWindow.getHelpWindow();
        helpWindow.show();
    }

    public void showEndGameScreen(Stage stage, Boolean victoire) {
        // Charger l'image
        ImageView Image;
        if (victoire) {
            Image = new ImageView(new Image("victory.png"));
        } else {
            Image = new ImageView(new Image("gameover1.png"));
        }
        Image.setPreserveRatio(true);
        Image.fitWidthProperty().bind(stage.widthProperty());
        Image.fitHeightProperty().bind(stage.heightProperty());

        // Bouton Quitter
        Button quitButton = new Button("Quitter");
        quitButton.setStyle(
                "-fx-background-color: #FF6347;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-background-radius: 10px;"
        );
        quitButton.setOnAction(e -> System.exit(0));

        // Bouton Rejouer
        Button restartButton = new Button("Rejouer");
        restartButton.setStyle(
                "-fx-background-color: #4CAF50;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-background-radius: 10px;"
        );
        restartButton.setOnAction(e -> {
            // On ferme la fenêtre actuelle
            restartApp();
        });

        // Texte explicatif
        Label messageLabel;
        if (victoire) {
            messageLabel = new Label("Les ennemis ont réussi à passer !");
        } else {
            messageLabel = new Label("Le nombre max de manches est atteint.");
        }


        messageLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;"
        );

        // Conteneur pour les boutons et le texte
        HBox buttonBox = new HBox(20); // Espacement de 20 pixels entre les éléments
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(restartButton, messageLabel, quitButton);

        // Mise en page
        StackPane root = new StackPane();
        root.getChildren().addAll(Image, buttonBox);
        root.setStyle("-fx-background-color: black;");
        StackPane.setAlignment(buttonBox, Pos.BOTTOM_CENTER);

        // Remplacement de la scène actuelle
        Scene endScene = new Scene(root);
        stage.setScene(endScene);
        stage.show();
    }

    public void restartApp() {
        Platform.runLater(() -> {
            try {
                // Fermer l'ancienne fenêtre
                primaryStage.close();

                // Créer une nouvelle instance de Stage
                Stage newStage = new Stage();
                primaryStage = newStage;

                // Créer un nouveau modèle de labyrinthe
                ModeleLabyrinth newLaby = new ModeleLabyrinth();
                this.laby = newLaby;

                // Mettre à jour l'instance du moteur avec le nouveau labyrinthe
                MoteurJeu.setLaby(newLaby);

                // Créer un nouveau canvas
                Canvas newCanvas = new Canvas();
                newCanvas.setWidth((getScreenSize().width / 7.0) * 6);
                newCanvas.widthProperty().bind(canvasContainer.widthProperty());
                newCanvas.heightProperty().bind(canvasContainer.heightProperty());

                // Créer une nouvelle vue de labyrinthe pour le nouveau modèle
                ViewLabyrinth newViewLabyrinth = new ViewLabyrinth(newLaby, newCanvas);
                newLaby.registerObserver(newViewLabyrinth);

                // Ajouter le nouveau Canvas au Pane
                canvasContainer.getChildren().clear();  // Supprimer l'ancien Canvas s'il existe
                canvasContainer.getChildren().add(newCanvas);  // Ajouter le nouveau Canvas

                // Réinitialiser FPS
                FPS = BASE_FPS;

                // Créer une nouvelle fenêtre de configuration avec le labyrinthe réinitialisé
                ConfigWindow newConfigWindow = new ConfigWindow(newLaby, newStage, newCanvas, this);

                // Enregistrer les nouvelles références
                this.canvas = newCanvas;
                this.configWindow = newConfigWindow;

                // Réinitialiser le temps et les statistiques du jeu
                newLaby.resetStats();

                // Ajouter l'icône à la nouvelle fenêtre
                Image icon = new Image("icon.png");
                newStage.getIcons().add(icon);
                newStage.setTitle("Tower Defense");

                // Afficher la nouvelle fenêtre de configuration
                newConfigWindow.show(newStage);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    private void updateObjectiveLabel() {
        int obj = laby.nbEnnemiesToWin - laby.ennemiesArrived.size();
        objectifLabel.setText("Il reste " + obj + " ennemis à passer sur " + laby.nbEnnemiesToWin);
        objectifLabel.setStyle(
                "-fx-background-color: #f8f9fa;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-background-radius: 5px;" +
                        "-fx-border-color: #dee2e6;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;" +
                        "-fx-text-fill: #2c3e50;" +
                        "-fx-margin-left: 20px"
        );
        // Ajout d'une marge à gauche dans le HBox
        HBox.setMargin(objectifLabel, new Insets(0, 0, 0, 20));
    }
}

