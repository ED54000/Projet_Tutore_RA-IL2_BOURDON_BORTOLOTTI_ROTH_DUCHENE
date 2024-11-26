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
    private Behavior behavior;
    private Vector2D target;

    @Override
    public void start(Stage stage) {
        final int width = 800;
        final int height = 600;

        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        agent = new Agent(new Vector2D(width / 2.0, height / 2.0), 3.0);
        target = new Vector2D(width / 2.0, height / 2.0); // Position initiale de la cible
        behavior = new SeekBehavior(target);
//        behavior = new ArrivalBehavior(target, 50);

        agent.setBehavior(behavior);

        canvas.setOnMouseMoved((MouseEvent e) -> {
            target = new Vector2D(e.getX(), e.getY());
            behavior.setTarget(target);
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render(gc);
            }
        };
        timer.start();

        stage.setScene(new Scene(new javafx.scene.layout.Pane(canvas)));
        stage.setTitle("Steering Behavior - Seek");
        stage.show();
    }

    private void update() {
        agent.update();
    }

    private void render(GraphicsContext gc) {
        gc.clearRect(0, 0, 800, 600);

        Vector2D position = agent.getPosition();
        gc.setFill(Color.BLUE);
        gc.fillOval(position.x - 10, position.y - 10, 20, 20);

        gc.setFill(Color.RED);
        gc.fillOval(target.x - 5, target.y - 5, 10, 10);
    }

    public static void main(String[] args) {
        launch();
    }
}
