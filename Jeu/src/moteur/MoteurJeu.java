package moteur;
//squelette adapté et modifié du moteur de Zeldiablo inspiré de :
//https://github.com/zarandok/megabounce/blob/master/MainCanvas.java

import entites.enemies.Ennemy;
import evolution.EnnemyEvolution;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import laby.ModeleLabyrinth;

import laby.views.ViewLabyrinth;
import laby.views.ViewLogs;

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
        //enregistrement des observateurs
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
        labyrinthMap.put("test1", "Ressources/Laby_test.txt");
        labyrinthMap.put("test2", "Ressources/Laby_test2.txt");
        labyrinthMap.put("testSteering", "Ressources/Laby_testSteering.txt");

        // Initialisation de la ComboBox avec les noms lisibles
        ComboBox<String> labyrinthComboBox = new ComboBox<>();
        labyrinthComboBox.getItems().addAll("Petit", "Grand", "Large", "test1", "test2","testSteering");
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
                switch (labyrinthComboBox.getValue()) {
                    case "Petit":
                        break;
                    case "Grand":
                        break;
                    case "Large":
                        break;
                }
                dialogStage.close();
                try {
                    laby.setUseAstar(avecAstarBox.isSelected());
                    ArrayList<Ennemy> ennemies = laby.createEnnemies(Integer.parseInt(enemiesField.getText()));
                    System.out.println("Les ennemies : " + ennemies.size());
                    laby.creerLabyrinthe(labyrinthMap.get(labyrinthComboBox.getValue()), ennemies, Integer.parseInt(roundsField.getText()), Integer.parseInt(nbEnnemiesToWinField.getText()));
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
        title.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        VBox logs = new VBox();
        logs.setPrefWidth(getScreenSize().width/7.0);
        logs.setPadding(new Insets(10));
        logs.setSpacing(10);
        logs.getChildren().add(new Label("Manche 1"));

        //Ajout d'un scrollPane
        ScrollPane scrollPane = new ScrollPane();
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

    private void createDialog(Stage primaryStage) {

    }

}