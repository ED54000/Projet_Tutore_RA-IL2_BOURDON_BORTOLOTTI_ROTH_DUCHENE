package com.example.steering_astar.Steering;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import com.example.steering_astar.Astar.Astar;

public class Main extends Application {
    private Agent agent;
    private Behavior behavior;
    private Vector2D target;

    @Override
    public void start(Stage stage) throws Exception {
        final int width = 1500;
        final int height = 1000;

        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        target = new Vector2D(width / 2.0, height / 2.0);
//        behavior = new SeekBehavior(target);
//        behavior = new ArrivalBehavior(target, 50);

      /*  ArrayList<Vector2D> pointList = new ArrayList<>(Arrays.asList(
                new Vector2D(50, 50),
                new Vector2D(200, 200),
                new Vector2D(150, 300)
        ));*/

        char[][] grid = {
                       // 0    1    2    3    4    5    6    7    8    9   10   11   12   13   14   15
                /*0*/  { '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#' },
                /*1*/  { '#', 'S', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.', '.', 'E' },
                /*2*/  { '#', '.', '#', '.', '#', '#', '#', '#', '#', '.', '#', '.', '.', '#', '.', '#' },
                /*3*/  { '#', '.', '#', '.', '#', '.', '.', '.', '#', '.', '#', '.', '#', '.', '.', '#' },
                /*4*/  { '#', '.', '#', '.', '#', '.', '#', '#', '#', '.', '#', '.', '#', '.', '.', '#' },
                /*5*/  { '#', '.', '#', '.', '.', '.', '#', '.', '#', '.', '#', '.', '#', '.', '.', '#' },
                /*6*/  { '#', '.', '#', '#', '#', '#', '#', '.', '#', '.', '#', '.', '#', '.', '.', '#' },
                /*7*/  { '#', '.', '.', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.', '#' },
                /*8*/  { '#', '.', '#', '#', '#', '#', '#', '#', '#', '#', '.', '#', '.', '.', '.', '#' },
                /*9*/  { '#', '.', '#', '.', '.', '.', '.', '.', '.', '.', '.', '#', '.', '.', '.', '#' },
                /*10*/ { '#', '.', '#', '.', '#', '#', '#', '.', '#', '#', '.', '#', '.', '.', '.', '#' },
                /*11*/ { '#', '.', '.', '.', '#', '.', '.', '.', '#', '.', '.', '#', '.', '.', '.', '#' },
                /*12*/ { '#', '.', '#', '.', '#', '.', '#', '#', '#', '.', '#', '#', '.', '.', '.', '#' },
                /*13*/ { '#', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '#', '.', '.', '.', '#' },
                /*14*/ { '#', '.', '#', '.', '#', '#', '#', '#', '#', '.', '#', '#', '#', '.', '.', '#' },
                /*15*/ { '#', '.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#' },
                /*16*/ { '#', '.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.', '.', '#' },
                /*17*/ { '#', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.' },
                /*18*/ { '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#' }
        };
        int[] startIndex = Vector2D.getPairIndex(grid, 'S');
        Vector2D start = new Vector2D(startIndex[0], startIndex[1]);
        agent = new Agent(start, 3.0);
        int [] endIndex = Vector2D.getPairIndex(grid, 'E');
        Vector2D dest = new Vector2D(endIndex[0], endIndex[1]);
        Astar app = new Astar();
        ArrayList<Vector2D> pointList = app.aStarSearch(grid, grid.length , grid[0].length, start, dest);
        System.out.println(pointList);

        behavior = new PathfollowingBehavior(pointList);

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
        stage.setTitle("Steering Behavior + Astar");
        stage.show();
    }

    private void update() {
        agent.update();
    }

    private void render(GraphicsContext gc) {
        gc.clearRect(0, 0, 1500, 1000);

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
