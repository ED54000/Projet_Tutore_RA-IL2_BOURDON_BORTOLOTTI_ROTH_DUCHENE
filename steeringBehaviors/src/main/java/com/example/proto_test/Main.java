package com.example.proto_test;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    private Agent agent;
    private Behavior seekBehavior;
    private Behavior arriveBehavior;
    private Vector2D target;

    @Override
    public void start(Stage stage) {
        final int width = 800;
        final int height = 600;

        // Créer un canvas pour le rendu
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Initialisation de l'agent et du comportement
        agent = new Agent(new Vector2D(width / 2.0, height / 2.0), 3.0);
        target = new Vector2D(width / 2.0, height / 2.0); // Position initiale de la cible
        seekBehavior = new SeekBehavior(target);
        arriveBehavior = new ArrivalBehavior(target, 50);

        agent.setBehavior(arriveBehavior);

        // Détection des clics de souris pour déplacer la cible
        canvas.setOnMouseMoved((MouseEvent e) -> {
            target = new Vector2D(e.getX(), e.getY());
            seekBehavior.setTarget(target);
        });

        // Boucle d'animation
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render(gc);
            }
        };
        timer.start();

        // Configurer la scène et la fenêtre
        stage.setScene(new Scene(new javafx.scene.layout.Pane(canvas)));
        stage.setTitle("Steering Behavior - Seek");
        stage.show();
    }

    private void update() {
        System.out.println("app update");
        // Calculer la force de direction et mettre à jour l'agent
        Vector2D force = seekBehavior.calculateForce(agent);
        agent.update();
    }

    private void render(GraphicsContext gc) {
        System.out.println("render");
        // Effacer l'écran
        gc.clearRect(0, 0, 800, 600);

        // Dessiner l'agent
        Vector2D position = agent.getPosition();
        gc.setFill(Color.BLUE);
        gc.fillOval(position.x - 10, position.y - 10, 20, 20); // Recentrer autour de (x, y)

        // Dessiner la cible
        gc.setFill(Color.RED);
        gc.fillOval(target.x - 5, target.y - 5, 10, 10);
    }

    public static void main(String[] args) {
        launch();
    }
}
