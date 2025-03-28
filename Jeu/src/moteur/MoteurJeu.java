package moteur;
//squelette adapté et modifié du moteur de Zeldiablo inspiré de :
//https://github.com/zarandok/megabounce/blob/master/MainCanvas.java

import entites.enemies.Ennemy;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    final Canvas canvas = new Canvas();
    final Pane canvasContainer = new Pane(canvas);
    private Label objectifLabel;

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
        MoteurJeu.primaryStage = primaryStage;
        // Création des vues
        ViewLabyrinth viewLabyrinth = new ViewLabyrinth(laby, canvas);
        laby.registerObserver(viewLabyrinth);

        // Configuration de la fenêtre de dialogue
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Configuration du Labyrinthe");

        // Conteneur principal avec style moderne
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #f5f5f5; "
                + "-fx-background-radius: 10; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        // En-tête
        Label titleLabel = new Label("Configuration de la partie");
        titleLabel.setStyle("-fx-font-size: 24px; "
                + "-fx-font-weight: bold; "
                + "-fx-text-fill: #2c3e50; "
                + "-fx-padding: 0 0 20 0;");
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setMaxWidth(Double.MAX_VALUE);

        // Section de sélection du labyrinthe préfait
        ComboBox<String> configPrefaiteComboBox = new ComboBox<>();
        configPrefaiteComboBox.getItems().addAll(
                "Aucune",
                "Chemin safe",
                "Aucun chemin safe",
                "Une tour unique",
                "Nombreuses tours centrales",
                "Tours inaccessibles"
        );
        configPrefaiteComboBox.setValue("Aucune");
        configPrefaiteComboBox.setId("configPrefaiteComboBox");
        configPrefaiteComboBox.setStyle("-fx-background-color: #f8f9fa; "
                + "-fx-background-radius: 5; "
                + "-fx-border-color: #dee2e6; "
                + "-fx-border-radius: 5; "
                + "-fx-padding: 5; ");

        VBox configPrefaiteSection = createSection("Choisir une configuration de test", configPrefaiteComboBox);

        // Map des labyrinthes
        Map<String, String> labyrinthMap = new HashMap<>();
        labyrinthMap.put("Petit", "Ressources/Labyrinthe1.txt");
        labyrinthMap.put("Grand", "Ressources/Labyrinthe2.txt");
        labyrinthMap.put("Large", "Ressources/Labyrinthe3.txt");
        labyrinthMap.put("Large sans safe", "Ressources/Labyrinthe3_bis.txt");
        labyrinthMap.put("Ligne", "Ressources/Ligne.txt");
        labyrinthMap.put("Nombreuses tours centrales", "Ressources/LignePlusieursTours.txt");
        labyrinthMap.put("Tours inaccessibles", "Ressources/ToursInaccessibles.txt");

        // Section de sélection du labyrinthe
        ComboBox<String> labyrinthComboBox = new ComboBox<>();
        labyrinthComboBox.getItems().addAll(
                "Petit",
                "Grand",
                "Large",
                "Large sans safe",
                "Ligne",
                "Nombreuses tours centrales",
                "Tours inaccessibles",
                "Plus"
        );
        labyrinthComboBox.setValue("Large");
        labyrinthComboBox.setId("labyrinthComboBox");
        labyrinthComboBox.setStyle("-fx-background-color: #f8f9fa; "
                + "-fx-background-radius: 5; "
                + "-fx-border-color: #dee2e6; "
                + "-fx-border-radius: 5; "
                + "-fx-padding: 5;");

        VBox labyrinthSection = createSection("Choisir le labyrinthe", labyrinthComboBox);

        // Section configuration des ennemis
        TextField enemiesField = createStyledTextField("30", "enemiesField");
        VBox enemiesSection = createSection("Nombre d'ennemis", enemiesField);

        TextField nbEnnemiesToWinField = createStyledTextField("70", "ennemiesToWinField");
        VBox objectifSection = createSection("Objectif d'ennemis à l'arrivée", nbEnnemiesToWinField);

        // Section nombre de manches
        TextField roundsField = createStyledTextField("10", "roundsField");
        VBox roundsSection = createSection("Nombre de manches", roundsField);

        // Section A*
        CheckBox avecAstarBox = new CheckBox();
        avecAstarBox.setSelected(true);
        avecAstarBox.setId("avecOuSansAstarBox");
        avecAstarBox.setStyle("-fx-padding: 5;");

        VBox astarSection = createSection("Utiliser l'algorithme A*", avecAstarBox);

        // Bouton de démarrage
        Button startButton = new Button("Démarrer la partie");
        startButton.setStyle("-fx-background-color: #4CAF50; "
                + "-fx-text-fill: white; "
                + "-fx-font-size: 14px; "
                + "-fx-font-weight: bold; "
                + "-fx-padding: 12 24; "
                + "-fx-background-radius: 5; "
                + "-fx-cursor: hand;");
        startButton.setAlignment(Pos.CENTER);
        startButton.setMaxWidth(Double.MAX_VALUE);

        // Effet hover pour le bouton
        startButton.setOnMouseEntered(e ->
                startButton.setStyle("-fx-background-color: #45a049; "
                        + "-fx-text-fill: white; "
                        + "-fx-font-size: 14px; "
                        + "-fx-font-weight: bold; "
                        + "-fx-padding: 12 24; "
                        + "-fx-background-radius: 5; "
                        + "-fx-cursor: hand; "
                        + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 0);")
        );

        startButton.setOnMouseExited(e ->
                startButton.setStyle("-fx-background-color: #4CAF50; "
                        + "-fx-text-fill: white; "
                        + "-fx-font-size: 14px; "
                        + "-fx-font-weight: bold; "
                        + "-fx-padding: 12 24; "
                        + "-fx-background-radius: 5; "
                        + "-fx-cursor: hand;")
        );

        configPrefaiteComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == "Aucune") {
                // Réactiver les autres ComboBox pour permettre la modification
                labyrinthComboBox.setDisable(false);
                enemiesField.setDisable(false);
                nbEnnemiesToWinField.setDisable(false);
                roundsField.setDisable(false);
                avecAstarBox.setDisable(false);
            } else {
                labyrinthComboBox.setDisable(true);
                enemiesField.setDisable(true);
                nbEnnemiesToWinField.setDisable(true);
                roundsField.setDisable(true);
                avecAstarBox.setDisable(true);
                switch (newValue) {
                    case "Chemin safe":
                        labyrinthComboBox.setValue("Large");
                        enemiesField.setText("15");
                        nbEnnemiesToWinField.setText("12");
                        roundsField.setText("10");
                        avecAstarBox.setSelected(true);
                        break;
                    case "Aucun chemin safe":
                        labyrinthComboBox.setValue("Large sans safe");
                        enemiesField.setText("50");
                        nbEnnemiesToWinField.setText("35");
                        roundsField.setText("20");
                        avecAstarBox.setSelected(true);
                        break;
                    case "Une tour unique":
                        labyrinthComboBox.setValue("Ligne");
                        enemiesField.setText("5");
                        nbEnnemiesToWinField.setText("5");
                        roundsField.setText("40");
                        avecAstarBox.setSelected(true);
                        break;
                    case "Nombreuses tours centrales":
                        labyrinthComboBox.setValue("Nombreuses tours centrales");
                        enemiesField.setText("20");
                        nbEnnemiesToWinField.setText("15");
                        roundsField.setText("10");
                        avecAstarBox.setSelected(true);
                        break;
                    case "Tours inaccessibles":
                        labyrinthComboBox.setValue("Tours inaccessibles");
                        enemiesField.setText("20");
                        nbEnnemiesToWinField.setText("15");
                        roundsField.setText("10");
                        avecAstarBox.setSelected(true);
                        break;
                    default:
                        break;
                }
            }
        });

        startButton.setOnMouseClicked(event -> {
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
                ArrayList<Ennemy> ennemies = ModeleLabyrinth.createEnnemies(
                        Integer.parseInt(enemiesField.getText())
                );
                laby.creerLabyrinthe(
                        labyrinthString,
                        ennemies,
                        Integer.parseInt(roundsField.getText()),
                        Integer.parseInt(nbEnnemiesToWinField.getText())
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            startJeu(primaryStage);
            MoteurJeu.labyFile = labyrinthString;
        });

        // Assemblage de l'interface
        HBox sectionsContainer = new HBox(20);
        VBox leftColumn = new VBox(20, configPrefaiteSection, enemiesSection, roundsSection);
        VBox rightColumn = new VBox(20, labyrinthSection, objectifSection, astarSection);
        sectionsContainer.getChildren().addAll(leftColumn, rightColumn);

        root.getChildren().addAll(
                titleLabel,
                sectionsContainer,
                startButton
        );

        // Configuration de la scène
        Scene dialogScene = new Scene(root, 560, 525);
        dialogScene.setFill(Color.TRANSPARENT);
        dialogStage.setScene(dialogScene);
        dialogStage.initOwner(primaryStage);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.showAndWait();
    }

    // Méthode utilitaire pour créer une section
    private VBox createSection(String labelText, Node content) {
        VBox section = new VBox(10);
        section.setStyle("-fx-background-color: white; "
                + "-fx-padding: 15; "
                + "-fx-background-radius: 5; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 0);");

        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 14px; "
                + "-fx-font-weight: bold; "
                + "-fx-text-fill: #34495e;");

        section.getChildren().addAll(label, content);
        return section;
    }

    // Méthode utilitaire pour créer un TextField stylisé
    private TextField createStyledTextField(String defaultValue, String id) {
        TextField field = new TextField(defaultValue);
        field.setId(id);
        field.setStyle("-fx-background-color: #f8f9fa; "
                + "-fx-background-radius: 5; "
                + "-fx-border-color: #dee2e6; "
                + "-fx-border-radius: 5; "
                + "-fx-padding: 8; "
                + "-fx-font-size: 13px;");

        // Effet focus
        field.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (isFocused) {
                field.setStyle("-fx-background-color: white; "
                        + "-fx-background-radius: 5; "
                        + "-fx-border-color: #4CAF50; "
                        + "-fx-border-radius: 5; "
                        + "-fx-padding: 8; "
                        + "-fx-font-size: 13px; "
                        + "-fx-effect: dropshadow(three-pass-box, rgba(76,175,80,0.2), 5, 0, 0, 0);");
            } else {
                field.setStyle("-fx-background-color: #f8f9fa; "
                        + "-fx-background-radius: 5; "
                        + "-fx-border-color: #dee2e6; "
                        + "-fx-border-radius: 5; "
                        + "-fx-padding: 8; "
                        + "-fx-font-size: 13px;");
            }
        });

        return field;
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

        //Ajout d'un scrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setFitToWidth(false); // Permet au ScrollPane de scroller horizontalement
        scrollPane.setPrefSize(getScreenSize().width / 5, getScreenSize().height);
        scrollPane.setContent(logs);

        ContainerLogs.getChildren().addAll(title, scrollPane);

        // création des vues
        ViewGraphiqueDirect viewGraphiqueDirect = new ViewGraphiqueDirect(laby);
        ViewLabyrinth viewLabyrinth = new ViewLabyrinth(laby, canvas);
        ViewLogs viewLogs = new ViewLogs(laby, ContainerLogs, viewGraphiqueDirect);
        ViewGraphique viewGraphique = new ViewGraphique(laby);
        //enregistrement des observateurs
        laby.registerObserver(viewLabyrinth);
        laby.registerObserver(viewLogs);
        laby.registerObserver(viewGraphique);
        laby.registerObserver(viewGraphiqueDirect);
        laby.registerObserver(obj -> updateObjectiveLabel());

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

        TimeManagement timeManagement = new TimeManagement(jeu, laby, speedUpButton, slowDownButton, pauseButton);

        speedUpButton.setOnAction(e -> {
            TimeManagement.modifySpeed((BASE_FPS * 8), speedUpButton, slowDownButton);
        });

        slowDownButton.setOnAction(e -> {
            TimeManagement.modifySpeed((BASE_FPS / 2), slowDownButton, speedUpButton);
        });

        pauseButton.setOnAction(e -> {
            TimeManagement.pause();
        });

        ToggleButton helpButton = new ToggleButton("Aide");

        helpButton.setOnAction(e -> openHelpWindow());

        ToggleButton graphicsButton = new ToggleButton("Graphiques");
        ViewGraphicsWindow graphicsWindow = new ViewGraphicsWindow(laby, viewGraphiqueDirect, viewGraphique);
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


    private String openLaby() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner un fichier de labyrinthe");
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Fichiers texte (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File("Ressources"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            MoteurJeu.labyFile = selectedFile.getAbsolutePath();
            return selectedFile.getAbsolutePath();
        } else {
            System.err.println("Aucun fichier sélectionné, utilisation du labyrinthe par défaut");
            MoteurJeu.labyFile = "Ressources/Labyrinthe1.txt";
            return "Ressources/Labyrinthe1.txt";
        }
    }

    private void openHelpWindow() {
        HelpWindow helpWindow = HelpWindow.getHelpWindow();
        helpWindow.show();
    }

    public static void showEndGameScreen(Stage stage, Boolean victoire) {
        // Charger l'image
        ImageView Image;
        if(victoire){
            Image = new ImageView(new Image("victory.png"));
        } else{
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
        restartButton.setOnAction(e -> restartApp());

        // Texte explicatif
        Label messageLabel ;
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

    private static void restartApp() {
        // Utilisez Platform.runLater pour exécuter le redémarrage dans le thread JavaFX
        Platform.runLater(() -> {
            try {
                // Créez une nouvelle instance de votre application
                Application.launch(MoteurJeu.class);
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
