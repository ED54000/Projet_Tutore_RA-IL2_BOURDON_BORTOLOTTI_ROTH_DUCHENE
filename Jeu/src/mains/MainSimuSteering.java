package mains;

import entites.enemies.Ennemy;
import entites.enemies.Giant;
import evolution.EvolutionSteering;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import laby.ModeleLabyrinth;
import steering_astar.Steering.Vector2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainSimuSteering extends Application {

    private static ModeleLabyrinth jeu;
    private static List<Ennemy> ennemies;
    private Canvas canvas;

    @Override
    public void start(Stage stage) throws Exception {
        //canvas = new Canvas(800, 600);
        //Scene scene = new Scene(new javafx.scene.layout.StackPane(canvas));
        //stage.setTitle("Simulation Evolution");
        //stage.setScene(scene);
        //stage.show();
        //startSimulation();

        //Initialisation de la simulation
        ArrayList<Ennemy> ennemies = new ArrayList<>();
        Giant giant = new Giant(new Vector2D(0, 0), "Giant");
        ennemies.add(giant);

        for (int i = 0; i < 20; i++) {

            HashMap<ArrayList<Ennemy>, Double> stats = new HashMap<>();
            stats.put(ennemies, 0.0);

            EvolutionSteering evolution = new EvolutionSteering();
            stats = evolution.evaluate(stats);

            if (stats == null) {
                System.out.println("Les ennemies ont gagné la partie");
                break;
            }

            ennemies = evolution.evolve(stats).get(0);
        }
        System.out.println("Fin bitch");
    }

    public void startSimulation() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                draw(gc);
            }
        };
        timer.start();
    }

    private void draw(GraphicsContext gc) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Dessiner le labyrinthe (placeholder)
        gc.setFill(Color.GRAY);
        gc.fillRect(50, 50, 700, 500);

        // Dessiner les ennemis
        gc.setFill(Color.RED);
        for (Ennemy e : ennemies) {
            gc.fillOval(e.getPosition().getX() * 10, e.getPosition().getX() * 10, 10, 10);
        }
    }

    private static final AtomicBoolean isLaunched = new AtomicBoolean(false);

    public static void launchUI(ModeleLabyrinth laby, List<Ennemy> ennemis) {
        jeu = laby;
        ennemies = ennemis;

        if (isLaunched.compareAndSet(false, true)) {
            new Thread(() -> Application.launch(MainSimuSteering.class)).start();
        }
    }
}
