package moteur;
//squelette adapté et modifié du moteur de Zeldiablo inspiré de :
//https://github.com/zarandok/megabounce/blob/master/MainCanvas.java

import entites.enemies.Ennemy;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import laby.ModeleLabyrinth;

import laby.controllers.ControllerSimpleMode;
import laby.views.ViewLabyrinth;
import laby.views.ViewLogs;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
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
     * @param jeu    jeu a lancer
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
        labyrinthComboBox.getItems().addAll("Petit", "Grand", "Large","Plus");
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
                String labyrinthString ;
                switch (labyrinthComboBox.getValue()) {
                    case "Plus" :
                        labyrinthString = openLaby();
                        break;
                    default :
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
        canvas.setWidth((getScreenSize().width/7.0)*6);
        canvas.widthProperty().bind(canvasContainer.widthProperty());
        canvas.heightProperty().bind(canvasContainer.heightProperty());

        VBox ContainerLogs = new VBox();
        Label title = new Label("Logs");
        title.setStyle("-fx-font-weight: bold");
        title.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        VBox logs = new VBox();
        logs.setMinWidth(350);
        logs.setPrefWidth(getScreenSize().width/7.0);
        logs.setPadding(new Insets(10));
        logs.setSpacing(10);
        ModeleLabyrinth.setLogs("Manche 1");
        //logs.getChildren().add(new Label("Manche 1"));

        //Ajout d'un scrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setFitToWidth(false); // Permet au ScrollPane de scroller horizontalement
        scrollPane.setPrefSize(getScreenSize().width/6.8, getScreenSize().height);
        scrollPane.setContent(logs);

        ContainerLogs.getChildren().addAll(title, scrollPane);

        //TODO : création des controleurs
        //ControllerStart controllerStart = new ControllerStart(laby);
        //ControllerLearn controllerLearn = new ControllerLearn(laby);
        // création des vues
        ViewLabyrinth viewLabyrinth = new ViewLabyrinth(laby, canvas);
        ViewLogs viewLogs = new ViewLogs(laby, ContainerLogs);
        //enregistrement des observateurs
        laby.registerObserver(viewLabyrinth);
        laby.registerObserver(viewLogs);

        final BorderPane root = new BorderPane();
        root.setCenter(canvasContainer);
        //ajout des logs
        root.setRight(ContainerLogs);

        // Ajout du bouton simple mode
        // Création d'un bouton radio au top
        ToggleButton toggleButton = new ToggleButton("Mode simple");
        ControllerSimpleMode controllerSimpleMode = new ControllerSimpleMode(laby, viewLabyrinth, toggleButton);
        toggleButton.setOnMouseClicked(controllerSimpleMode);


        // MODIFICATION VITESSE JEU

        ToggleButton speedUpButton = new ToggleButton("Accélérer");
        ToggleButton slowDownButton = new ToggleButton("Ralentir");
        ToggleButton pauseButton = new ToggleButton("Pause");

        speedUpButton.setOnAction(e -> {
            if (speedUpButton.isSelected()) {
                setFPS(BASE_FPS * 2);
                speedUpButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

                slowDownButton.setSelected(false);
                pauseButton.setSelected(false);

                slowDownButton.setStyle("");
                pauseButton.setStyle("");

                laby.setPause(false);
            } else {
                setFPS(BASE_FPS);
                speedUpButton.setStyle("");
            }
        });

        slowDownButton.setOnAction(e -> {
            if (slowDownButton.isSelected()) {
                setFPS(BASE_FPS / 2);
                slowDownButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

                speedUpButton.setSelected(false);
                pauseButton.setSelected(false);

                speedUpButton.setStyle("");
                pauseButton.setStyle("");

                laby.setPause(false);
            } else {
                setFPS(BASE_FPS);
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

        HBox controls = new HBox(10, toggleButton, slowDownButton, pauseButton, speedUpButton);
        root.setTop(controls);

        // creation de la scene
        final Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        // lance la boucle de jeu
        jeu.init(canvas);
        startAnimation(canvas);
    }

    /**
     * gestion de l'animation (boucle de jeu)
     *
     * @param canvas le canvas sur lequel on est synchronise
     */
    private void startAnimation(final Canvas canvas) {
        // stocke la derniere mise e jour
        final LongProperty lastUpdateTime = new SimpleLongProperty(0);

        // timer pour boucle de jeu
        final AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long timestamp) {
                //fin du jeu
                if (Platform.isFxApplicationThread() && jeu.etreFini()) {
                    this.stop();
                    return;
                }

                // si jamais passe dans la boucle, initialise le temps
                if (lastUpdateTime.get() == 0) {
                    lastUpdateTime.set(timestamp);
                }

                // mesure le temps ecoule depuis la derniere mise a jour
                long elapsedTime = timestamp - lastUpdateTime.get();
                double dureeEnMilliSecondes = elapsedTime / 1_000_000.0;


                // si le temps ecoule depasse le necessaire pour FPS souhaite
                if (dureeEnMilliSecondes > dureeFPS) {
                    // met a jour le jeu
                    jeu.update(dureeEnMilliSecondes / 1_000.);

                    // dessine le jeu
                    //ViewLabyrinth.dessinerJeu(jeu, canvas);
                    //notifier observateurs

                    // ajoute la duree dans les statistiques
                    frameStats.addFrame(elapsedTime);

                    // met a jour la date de derniere mise a jour
                    lastUpdateTime.set(timestamp);
                }

            }
        };
        // lance l'animation
        timer.start();
    }

    /**
     * Méthode permettant d'activer le mode simple
     * @param vue vue du labyrinthe
     */
    public void enableSimpleMode(ViewLabyrinth vue) {
        setSimpleMode(true);
        // On crée les sprites Images du jeu
        Image tree = new Image("/blackSquare.png");
        Image tile = new Image("/whiteSquare.png");

        // On applique les sprites aux cases (sol, murs)
        Map<Character, Image> newImages = new HashMap<>();
        for(Map.Entry<Character, Image> entry : vue.getImages().entrySet()) {
            newImages.put(entry.getKey(), entry.getValue());
        }
        newImages.put(ModeleLabyrinth.TREE, tree);
        newImages.put(ModeleLabyrinth.ROAD, tile);
        vue.setImages(newImages);

        // On applique les sprites aux ennemis
        ArrayList<Ennemy> allEnnemies = new ArrayList<>(); // Liste des ennemis vivants et morts
        allEnnemies.addAll(laby.enemies);
        allEnnemies.addAll(laby.deadEnemies);
        for (Ennemy ennemy : allEnnemies) {
            switch (ennemy.getBehaviorString()){
                case "Normal":
                    ennemy.setSprite(addTextToImage("" + (int)ennemy.getHealth(), new Image("/gray.png")));
                    break;
                case "Kamikaze" :
                    ennemy.setSprite(addTextToImage("" + (int)ennemy.getHealth(), new Image("/red.png")));
                    break;
                case "Healer" :
                    ennemy.setSprite(addTextToImage("" + (int)ennemy.getHealth(), new Image("/green.png")));
                    break;
                case "Fugitive" :
                    ennemy.setSprite(addTextToImage("" + (int)ennemy.getHealth(), new Image("/blue.png")));
                    break;
            }
        }
    }

    /**
     * Méthode permettant de désactiver le mode simple
     * @param vue vue du labyrinthe
     */
    public void disableSimpleMode(ViewLabyrinth vue) {
        setSimpleMode(false);
        // On crée les sprites Images du jeu
        Image tree = new Image("/tree3.png");
        Image tile = new Image("/tiles3.png");

        // On applique les sprites aux entités
        Map<Character, Image> newImages = new HashMap<>();
        for(Map.Entry<Character, Image> entry : vue.getImages().entrySet()) {
            newImages.put(entry.getKey(), entry.getValue());
        }
        newImages.put(ModeleLabyrinth.TREE, tree);
        newImages.put(ModeleLabyrinth.ROAD, tile);
        vue.setImages(newImages);

        // On applique les sprites aux ennemis
        ArrayList<Ennemy> allEnnemies = new ArrayList<>(); // Liste des ennemis vivants et morts
        allEnnemies.addAll(laby.enemies);
        allEnnemies.addAll(laby.deadEnemies);
        for (Ennemy ennemy : allEnnemies) {
            switch (ennemy.getBehaviorString()){
                case "Normal" :
                    ennemy.setSprite(new Image("/giant.png"));
                    break;
                case "Kamikaze" :
                    ennemy.setSprite(new Image("/berserker.png"));
                    break;
                case "Healer" :
                    ennemy.setSprite(new Image("/druide.png"));
                    break;
                case "Fugitive" :
                    ennemy.setSprite(new Image("/ninja.png"));
                    break;
            }
        }
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

    public void setSimpleMode(boolean mode) {
        simpleMode = mode;
    }

    public static boolean getSimpleMode() {
        return simpleMode;
    }

    /**
     * Méthode pour afficher du texte sur une image
     * @param text Texte à afficher
     * @param image Image sur laquelle afficher le texte
     * @return Image avec le texte
     */
    public static Image addTextToImage(String text, Image image) {
        WritableImage writableImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        Canvas canvas = new Canvas(image.getWidth(), image.getHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(image, 0, 0);

        // calcule la taille du texte en fonction de la taille de l'image
        double fontSize = image.getHeight() / 5; // ajuste la taille de la police en fonction de la taille de l'image
        Font font = Font.font("Arial", FontWeight.BOLD, fontSize);
        javafx.scene.text.Text tempText = new javafx.scene.text.Text(text);
        tempText.setFont(font);

        gc.setFont(font);
        gc.setFill(Color.WHITE);

        // calcule la position du texte (au centre)
        double textWidth = tempText.getLayoutBounds().getWidth();
        double textHeight = tempText.getLayoutBounds().getHeight();
        double x = (image.getWidth() - textWidth) / 2;
        double y = (image.getHeight() + textHeight) / 2;

        // dessine le texte sur l'image
        gc.fillText(text, x, y);

        // capture le canvas dans l'image
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        canvas.snapshot(params, writableImage);
        return writableImage;
    }
}