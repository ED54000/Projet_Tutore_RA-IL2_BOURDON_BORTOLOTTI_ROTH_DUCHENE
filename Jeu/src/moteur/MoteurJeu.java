package moteur;
//squelette adapté et modifié du moteur de Zeldiablo inspiré de :
//https://github.com/zarandok/megabounce/blob/master/MainCanvas.java

import entites.enemies.Ennemy;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import laby.ModeleLabyrinth;
import laby.controllers.ControllerSimpleMode;
import laby.views.ViewGraphique;
import laby.views.ViewGraphiqueDirect;
import laby.views.ViewLabyrinth;
import laby.views.ViewLogs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static laby.ModeleLabyrinth.getScreenSize;


// copied from: https://gist.github.com/james-d/8327842
// and modified to use canvas drawing instead of shapes

public class MoteurJeu extends Application {

    /**
     * gestion du temps : nombre de frame par secondes et temps par iteration
     */
    private static double FPS = 100;
    private static double dureeFPS = 1000 / (FPS + 1);

    private static boolean simpleMode = false;

    private static final int BASE_FPS = 100;
    private Timeline timeline;

    /**
     * statistiques sur les frames
     */
    private final FrameStats frameStats = new FrameStats();

    /**
     * jeu en Cours et renderer du jeu
     */
    private static Jeu jeu = null;
    ModeleLabyrinth laby = (ModeleLabyrinth) MoteurJeu.jeu;
    //Labyrinthe laby = labyJeu.getLabyrinthe();
    //private static DessinJeu dessin = null;

    // initialisation du canvas de dessin et du container
    final Canvas canvas = new Canvas();
    final Pane canvasContainer = new Pane(canvas);
    //final VBox logs = new VBox();

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

    /**
     * frame par secondes
     *
     * @param FPSSouhaitees nombre de frames par secondes souhaitees
     */
    public static void setFPS(int FPSSouhaitees) {
        FPS = FPSSouhaitees;
        dureeFPS = 1000 / (FPS + 1);
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
        // création des vues
        ViewLabyrinth viewLabyrinth = new ViewLabyrinth(laby, canvas);
        laby.registerObserver(viewLabyrinth);

        // Crée une nouvelle fenêtre (Stage)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Labyrinthe");
        // Conteneur principal
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        // Map des labyrinthes
        Map<String, String> labyrinthMap = new HashMap<>();
        labyrinthMap.put("Petit", "Ressources/Labyrinthe1.txt");
        labyrinthMap.put("Grand", "Ressources/Labyrinthe2.txt");
        labyrinthMap.put("Large", "Ressources/Labyrinthe3.txt");

        // Initialisation de la ComboBox avec les noms lisibles
        ComboBox<String> labyrinthComboBox = new ComboBox<>();
        labyrinthComboBox.getItems().addAll("Petit", "Grand", "Large", "Plus");
        labyrinthComboBox.setValue("Large");

        // Définit "Petit" comme valeur par défaut
        HBox labyrinthBox = new HBox(10, new Label("Choisir le labyrinthe :"), labyrinthComboBox);
        // Champ pour le nombre d'ennemis
        TextField enemiesField = new TextField();
        enemiesField.setPromptText("Nombre d'ennemis");
        enemiesField.setText("30");

        // Champ pour le nombre d'ennemis qui doivent atteindre la ligne d'arrivée
        TextField nbEnnemiesToWinField = new TextField();
        nbEnnemiesToWinField.setPromptText("Nombre d'ennemis qui doivent atteindre la ligne d'arrivée");
        nbEnnemiesToWinField.setText("70");

        // CheckBox avec ou sans Astar
        CheckBox avecAstarBox = new CheckBox();
        avecAstarBox.setSelected(true);

        HBox enemiesBox = new HBox(10, new Label("Nombre d'ennemis :"), enemiesField);
        // Champ pour le nombre de manches
        TextField roundsField = new TextField();
        roundsField.setPromptText("Nombre de manches");
        roundsField.setText("10");
        labyrinthComboBox.setId("labyrinthComboBox");
        enemiesField.setId("enemiesField");
        roundsField.setId("roundsField");
        nbEnnemiesToWinField.setId("ennemiesToWinField");
        avecAstarBox.setId("avecOuSansAstarBox");

        // Création du contrôleur avec les références des champs
        //ControllerStart controllerStart = new ControllerStart(laby, labyrinthComboBox, enemiesField, roundsField, nbEnnemiesToWinField);


        HBox roundsBox = new HBox(10, new Label("Nombre de manches :"), roundsField);
        HBox ennemiesToWinBox = new HBox(10, new Label("Objectif :"), nbEnnemiesToWinField);
        HBox noAstar = new HBox(10, new Label("Avec Astar ? "), avecAstarBox);
        // Bouton Start
        Button startButton = new Button("Start");
        startButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent MouseEvent) {
                laby.setStartTime();
                String labyrinthString;
                switch (labyrinthComboBox.getValue()) {
                    case "Plus":
                        labyrinthString = openLaby();
                        break;
                    default:
                        labyrinthString = labyrinthMap.get(labyrinthComboBox.getValue());
                        break;
                }
                dialogStage.close();
                try {
                    laby.setUseAstar(avecAstarBox.isSelected());
                    ArrayList<Ennemy> ennemies = laby.createEnnemies(Integer.parseInt(enemiesField.getText()));
                    System.out.println("Les ennemies : " + ennemies.size());
                    laby.creerLabyrinthe(labyrinthString, ennemies, Integer.parseInt(roundsField.getText()), Integer.parseInt(nbEnnemiesToWinField.getText()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                startJeu(primaryStage);
            }
        });
        // Ajout des composants au conteneur principal
        root.getChildren().addAll(labyrinthBox, enemiesBox, roundsBox, ennemiesToWinBox, noAstar, startButton);

        //startButton.setOnMouseClicked(controllerStart);

        // Configure la scène de la fenêtre
        Scene dialogScene = new Scene(root, 400, 230);
        dialogStage.setScene(dialogScene);
        // Configure la fenêtre en tant que modale
        dialogStage.initOwner(primaryStage);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        // Affiche la fenêtre
        dialogStage.showAndWait();
    }

    public void startJeu(Stage primaryStage) {
        canvas.setWidth((getScreenSize().width / 7.0) * 6);
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
        //logs.getChildren().add(new Label("Manche 1"));

        //Ajout d'un scrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setFitToWidth(false); // Permet au ScrollPane de scroller horizontalement
        scrollPane.setPrefSize(getScreenSize().width / 5, getScreenSize().height);
        scrollPane.setContent(logs);

        ContainerLogs.getChildren().addAll(title, scrollPane);

        //TODO : création des controleurs
        //ControllerStart controllerStart = new ControllerStart(laby);
        //ControllerLearn controllerLearn = new ControllerLearn(laby);
        // création des vues
        ViewGraphiqueDirect viewGraphiqueDirect = new ViewGraphiqueDirect(laby);
        //ContainerLogs.getChildren().add(viewGraphiqueDirect.getGraphique());
        ViewLabyrinth viewLabyrinth = new ViewLabyrinth(laby, canvas);
        ViewLogs viewLogs = new ViewLogs(laby, ContainerLogs, viewGraphiqueDirect);
        ViewGraphique viewGraphique = new ViewGraphique(laby);
        //enregistrement des observateurs
        laby.registerObserver(viewLabyrinth);
        laby.registerObserver(viewLogs);
        laby.registerObserver(viewGraphique);
        laby.registerObserver(viewGraphiqueDirect);

        final BorderPane root = new BorderPane();
        root.setCenter(canvasContainer);
        //ajout des logs
        root.setRight(ContainerLogs);

        // Ajout du bouton simple mode
        // Création d'un bouton radio au top
        ToggleButton switchMode = new ToggleButton("Mode simple");
        ControllerSimpleMode controllerSimpleMode = new ControllerSimpleMode(laby, viewLabyrinth, switchMode, mode);
        switchMode.setOnMouseClicked(controllerSimpleMode);


        // MODIFICATION VITESSE JEU

        ToggleButton speedUpButton = new ToggleButton("Accélérer");
        ToggleButton slowDownButton = new ToggleButton("Ralentir");
        ToggleButton pauseButton = new ToggleButton("Pause");

        speedUpButton.setOnAction(e -> {
            if (speedUpButton.isSelected()) {
                modifyFPS(BASE_FPS * 2);
                speedUpButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

                slowDownButton.setSelected(false);
                pauseButton.setSelected(false);

                slowDownButton.setStyle("");
                pauseButton.setStyle("");

                laby.setPause(false);
            } else {
                modifyFPS(BASE_FPS);
                speedUpButton.setStyle("");
            }
        });

        slowDownButton.setOnAction(e -> {
            if (slowDownButton.isSelected()) {
                modifyFPS(BASE_FPS / 2);
                slowDownButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

                speedUpButton.setSelected(false);
                pauseButton.setSelected(false);

                speedUpButton.setStyle("");
                pauseButton.setStyle("");

                laby.setPause(false);
            } else {
                modifyFPS(BASE_FPS);
                slowDownButton.setStyle("");
            }
        });

        pauseButton.setOnAction(e -> {
            if (pauseButton.isSelected()) {
                laby.setPause(true);
                pauseButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

                speedUpButton.setSelected(false);
                slowDownButton.setSelected(false);

                speedUpButton.setStyle("");
                slowDownButton.setStyle("");
            } else {
                laby.setPause(false);
                pauseButton.setStyle("");
            }
            setFPS(BASE_FPS);
        });

        ToggleButton helpButton = new ToggleButton("Aide");

        helpButton.setOnAction(e -> openHelpWindow());


        HBox controls = new HBox(10, switchMode, slowDownButton, pauseButton, speedUpButton, helpButton);
        root.setTop(controls);

        // creation de la scene
        final Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
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

    public void modifyFPS(double newFPS) {
        if (newFPS <= 0) return; // Sécurité : éviter une division par zéro

        dureeFPS = 1000.0 / newFPS; // Convertir FPS en durée (ms)

        // Si le timeline existe déjà, on l'arrête avant de le recréer
        if (timeline != null) {
            timeline.stop();
        }

        // Création d'un nouveau Timeline avec le nouvel intervalle
        timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.millis(dureeFPS), event -> {
            if (jeu.etreFini()) {
                timeline.stop();
                return;
            }

            jeu.update(dureeFPS / 1000.0);

            // ViewLabyrinth.dessinerJeu(jeu, canvas);
            // Notifier les observateurs

            frameStats.addFrame((long) (dureeFPS * 1_000_000)); // Convertir en ns
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }



    private String openLaby() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner un fichier de labyrinthe");
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Fichiers texte (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File("Ressources"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            return selectedFile.getAbsolutePath();
        } else {
            System.err.println("Aucun fichier sélectionné, utilisation du labyrinthe par défaut");
            return "Ressources/Labyrinthe1.txt";
        }
    }





    private void openHelpWindow() {
        HelpWindow helpWindow = HelpWindow.getHelpWindow();
        helpWindow.show();
    }
}