package com.example.chemin_interface.moteur;

//https://github.com/zarandok/megabounce/blob/master/MainCanvas.java

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;
import com.example.chemin_interface.laby.ModeleLabyrinth;
import com.example.chemin_interface.laby.controllers.ControllerStart;
import com.example.chemin_interface.laby.views.ViewLabyrinth;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


// copied from: https://gist.github.com/james-d/8327842
// and modified to use canvas drawing instead of shapes

public class MoteurJeu extends Application {

    /**
     * gestion du temps : nombre de frame par secondes et temps par iteration
     */
    private static double FPS = 100;
    private static double dureeFPS = 1000 / (FPS + 1);

    /**
     * taille par defaut
     */
    private static double WIDTH;
    private static double HEIGHT;


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


    /**
     * lancement d'un jeu
     *
     * @param jeu jeu a lancer
     */
    public static void launch(Jeu jeu, int fps) {
        // le jeu en cours et son afficheur
        MoteurJeu.jeu = jeu;
        MoteurJeu.setFPS(fps);

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



//    public static void setLaby(ModeleLabyrinth laby) {
//        MoteurJeu.jeu = laby;
//    }


    //#################################
    // SURCHARGE Application
    //#################################
    /**
     * creation de l'application avec juste un canvas et des statistiques
     */
    public void start(Stage primaryStage) throws IOException {
        //ControllerStart controllerStart = new ControllerStart(laby);
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
        labyrinthMap.put("Test", "Ressources/Laby_test.txt");

        // Initialisation de la ComboBox avec les noms lisibles
        ComboBox<String> labyrinthComboBox = new ComboBox<>();
        labyrinthComboBox.getItems().addAll("Petit", "Grand", "Large", "Test");
        labyrinthComboBox.setValue("Test");

        // Définit "Petit" comme valeur par défaut
        HBox labyrinthBox = new HBox(10, new Label("Choisir le labyrinthe :"), labyrinthComboBox);
        // Champ pour le nombre d'ennemis
        TextField enemiesField = new TextField();
        enemiesField.setPromptText("Nombre d'ennemis");
        enemiesField.setText("5");

        HBox enemiesBox = new HBox(10, new Label("Nombre d'ennemis :"), enemiesField);
        // Champ pour le nombre de manches
        TextField roundsField = new TextField();
        roundsField.setPromptText("Nombre de manches");
        roundsField.setText("10");
        labyrinthComboBox.setId("labyrinthComboBox");
        enemiesField.setId("enemiesField");
        roundsField.setId("roundsField");

        // Création du contrôleur avec les références des champs
        ControllerStart controllerStart = new ControllerStart(laby, labyrinthComboBox, enemiesField, roundsField);


        HBox roundsBox = new HBox(10, new Label("Nombre de manches :"), roundsField);
        // Bouton Start
        Button startButton = new Button("Start");
        startButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent MouseEvent) {
                dialogStage.close();
                try {
                    laby.creerLabyrinthe(labyrinthMap.get(labyrinthComboBox.getValue()), Integer.parseInt(enemiesField.getText()), Integer.parseInt(roundsField.getText()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                startJeu(primaryStage);
            }
        });
        // Ajout des composants au conteneur principal
        root.getChildren().addAll(labyrinthBox, enemiesBox, roundsBox, startButton);

        //startButton.setOnMouseClicked(controllerStart);

        // Configure la scène de la fenêtre
        Scene dialogScene = new Scene(root, 400, 200);
        dialogStage.setScene(dialogScene);
        // Configure la fenêtre en tant que modale
        dialogStage.initOwner(primaryStage);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        // Affiche la fenêtre
        dialogStage.showAndWait();

        primaryStage.setX(-7);
        primaryStage.setY(0);
        primaryStage.setAlwaysOnTop(true);
    }

    public void startJeu(Stage primaryStage) {
        canvas.widthProperty().bind(canvasContainer.widthProperty());
        canvas.heightProperty().bind(canvasContainer.heightProperty());

        // affichage des stats
        final Label stats = new Label();
        stats.textProperty().bind(frameStats.textProperty());

        // ajout des statistiques en bas de la fenetre
        final BorderPane root = new BorderPane();
        root.setCenter(canvasContainer);
        root.setBottom(stats);

        // creation de la scene
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle2D fullBounds = Screen.getPrimary().getBounds();
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double taskbarHeight = fullBounds.getHeight() - visualBounds.getHeight();

        WIDTH = screenSize.getWidth();
        HEIGHT = screenSize.getHeight()-taskbarHeight/2;
        final Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
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
                if (jeu.etreFini()) {
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