//package com.example.chemin_interface.steering_astar.Steering;
//
//import javafx.animation.AnimationTimer;
//import javafx.application.Application;
//import javafx.geometry.Rectangle2D;
//import javafx.scene.Scene;
//import javafx.scene.canvas.Canvas;
//import javafx.scene.canvas.GraphicsContext;
//import javafx.scene.paint.Color;
//import javafx.stage.Screen;
//import javafx.stage.Stage;
//
//import java.awt.*;
//import java.util.ArrayList;
//import com.example.chemin_interface.steering_astar.Astar.Astar;
//
//public class Main extends Application {
//    private ArrayList<Agent> agents;
//    private Behavior behavior, behavior2, behavior3;
//    private ArrayList<Vector2D> checkpoints, checkpoints2, checkpoints3;
//    double windowWidth, windowHeight = 0;
//    char[][] grid;
//
//    @Override
//    public void start(Stage stage) throws Exception {
//
//        char[][] grid1 = {
//                       // 0    1    2    3    4    5    6    7    8    9   10   11   12   13   14   15
//                /*0*/  { '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#' },
//                /*1*/  { '#', 'S', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.', '.', 'E' },
//                /*2*/  { '#', '.', '#', '.', '#', '#', '#', '#', '#', '.', '#', '.', '.', '#', '.', '#' },
//                /*3*/  { '#', '.', 'T', '.', '#', '.', '.', '.', '#', '.', '#', '.', '#', '.', '.', '#' },
//                /*4*/  { '#', '.', '#', '.', '#', '.', '#', '#', '#', '.', '#', '.', '#', '.', '.', '#' },
//                /*5*/  { '#', '.', '#', '.', '.', '.', '#', '.', '#', '.', '#', '.', '#', '.', '.', '#' },
//                /*6*/  { '#', '.', '#', '#', '#', '#', '#', '.', '#', '.', '#', '.', '#', '.', '.', '#' },
//                /*7*/  { '#', '.', '.', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.', '#' },
//                /*8*/  { '#', '.', '#', '#', '#', 'T', '#', '#', '#', '#', '.', '#', '.', '.', '.', '#' },
//                /*9*/  { '#', '.', '#', '.', '.', '.', '.', '.', '.', '.', '.', '#', '.', '.', '.', '#' },
//                /*10*/ { '#', '.', '#', '.', '#', '#', '#', '.', '#', '#', '.', '#', '.', '.', '.', '#' },
//                /*11*/ { '#', '.', '.', '.', '#', '.', '.', '.', '#', '.', '.', '#', '.', '.', '.', '#' },
//                /*12*/ { '#', '.', '#', '.', '#', '.', '#', '#', '#', '.', '#', '#', '.', '.', '.', '#' },
//                /*13*/ { '#', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '#', '.', '.', '.', '#' },
//                /*14*/ { '#', '.', '#', '.', '#', '#', '#', '#', '#', '.', '#', '#', '#', '.', '.', '#' },
//                /*15*/ { '#', '.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#' },
//                /*16*/ { '#', '.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.', '.', '#' },
//                /*17*/ { '#', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.' },
//                /*18*/ { '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#' }
//        };
//        char[][] grid2 = {
//                // 0    1    2    3    4    5    6    7    8    9   10   11   12   13   14   15
//                /*0*/  { '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#' },
//                /*1*/  { '#', 'S', '.', '.', '.', '.', '.', '#', '.', '.', '#', '.', '.', '.', '.', 'E' },
//                /*2*/  { '#', '.', '#', '.', '#', '#', '#', '#', '#', '.', '#', '.', '.', '#', '.', '#' },
//                /*3*/  { '#', '.', '#', '.', '#', '.', '.', '.', '#', '.', '#', '#', '#', '.', '.', '#' },
//                /*4*/  { '#', '.', 'T', '.', '#', '.', '#', '#', '#', '.', '#', '.', '#', '.', '.', '#' },
//                /*5*/  { '#', '.', '#', '.', '.', '.', '#', '.', '#', '.', '#', '.', '#', '.', '.', '#' },
//                /*6*/  { '#', '.', '#', '#', '#', '#', '#', '.', '#', '.', '#', '.', '#', '#', '.', '#' },
//                /*7*/  { '#', '.', '.', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '#', '.', '#' },
//                /*8*/  { '#', '.', '#', '#', '#', 'T', '#', '#', '#', '#', '.', '#', '.', '#', '.', '#' },
//                /*9*/  { '#', '.', '#', '.', '.', '.', '.', '.', '.', '.', '.', '#', '.', '#', '.', '#' },
//                /*10*/ { '#', '.', '#', '.', '#', '#', '#', '.', '#', '#', '.', '#', '.', '#', '.', '#' },
//                /*11*/ { '#', '.', '.', '.', '#', '.', '.', '.', '#', '.', '.', '#', '.', '#', '.', '#' },
//                /*12*/ { '#', '.', '#', '.', '#', '.', '#', '#', '#', '.', '#', '#', '.', '#', '.', '#' },
//                /*13*/ { '#', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '#', '.', '#', '.', '#' },
//                /*14*/ { '#', '.', '#', '.', '#', '#', '#', '#', '#', '.', '#', '#', '.', '#', '.', '#' },
//                /*15*/ { '#', '.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.', '.', '#', '.', '#' },
//                /*16*/ { '#', '.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.', '#', '.', '#' },
//                /*17*/ { '#', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.' },
//                /*18*/ { '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#' }
//        };
//
//        //GRILLE UTILISEE
//        grid = grid2;
//
//        // COMPORTEMENT UTILISEE
//        String[] comportements = {"Normal","Fugitive","Kamikaze"};
//
//        int[] startIndex = Vector2D.getPairIndex(grid, 'S');
//        Vector2D start = new Vector2D(startIndex[0], startIndex[1]);
//        int [] endIndex = Vector2D.getPairIndex(grid, 'E');
//        Vector2D dest = new Vector2D(endIndex[0], endIndex[1]);
//        Astar app = new Astar();
//
//        // CHEMINS
//        checkpoints = app.aStarSearch(grid, grid.length , grid[0].length, start, dest,comportements[0]);
//        checkpoints2 = app.aStarSearch(grid, grid.length , grid[0].length, start, dest,comportements[1]);
//        checkpoints3 = app.aStarSearch(grid, grid.length , grid[0].length, start, dest,comportements[2]);
//
//        // COURBES
//        ArrayList<Vector2D> listBezier = BezierCurveFromAStarPoints.getBezierCurve(checkpoints);
//        ArrayList<Vector2D> listBezier2 = BezierCurveFromAStarPoints.getBezierCurve(checkpoints2);
//        ArrayList<Vector2D> listBezier3 = BezierCurveFromAStarPoints.getBezierCurve(checkpoints3);
//
//        // BEHAVIORS
//        behavior = new PathfollowingBehavior(listBezier);
//        behavior2 = new PathfollowingBehavior(listBezier2);
//        behavior3 = new PathfollowingBehavior(listBezier3);
//
//        // AGENTS
//        agents = new ArrayList<>();
//        agents.add(new Agent(checkpoints.getFirst(), 3));
//        agents.add(new Agent(checkpoints.getFirst(), 2));
//        agents.add(new Agent(checkpoints.getFirst(), 1));
//
//        // BEHAVIORS AGENTS
//        agents.get(0).setBehavior(behavior);
//        agents.get(1).setBehavior(behavior2);
//        agents.get(2).setBehavior(behavior3);
//
//        // PARAMETRES FENETRE
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        Rectangle2D fullBounds = Screen.getPrimary().getBounds();
//        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
//        double taskbarHeight = fullBounds.getHeight() - visualBounds.getHeight();
//
//        windowWidth = screenSize.getWidth();
//        windowHeight = screenSize.getHeight()-taskbarHeight/2;
//
//        Canvas canvas = new Canvas(windowWidth, windowHeight);
//        GraphicsContext gc = canvas.getGraphicsContext2D();
//
//        AnimationTimer timer = new AnimationTimer() {
//            @Override
//            public void handle(long now) {
//                update();
//                render(gc);
//            }
//        };
//        timer.start();
//
//        stage.setX(-7);
//        stage.setY(0);
//        stage.setAlwaysOnTop(true);
//        stage.setScene(new Scene(new javafx.scene.layout.Pane(canvas)));
//        stage.setTitle("Steering Behavior + Astar");
//        stage.show();
//    }
//
//    private void update() {
//        for (Agent agent : agents){
//            agent.update();
//        }
//    }
//
//    private void render(GraphicsContext gc) {
//        gc.clearRect(0, 0, windowWidth, windowHeight);
//
//        renderAgent(gc,agents.get(0),checkpoints,Color.BLACK,Color.BLUE);
//        renderAgent(gc,agents.get(1),checkpoints2,Color.GREEN,Color.PINK);
//        renderAgent(gc,agents.get(2),checkpoints3,Color.ORANGE,Color.PURPLE);
//
//    }
//
//    private void renderAgent(GraphicsContext gc, Agent agent, ArrayList<Vector2D> checkpoint, Color pathColor, Color agentColor) {
//
//        gc.setFill(pathColor);
//        checkpoint.forEach((n) ->
//                gc.fillOval(n.x - 10, n.y - 10, 20, 20)
//        );
//
//        Vector2D position = agent.getPosition();
//        gc.setFill(agentColor);
//        gc.fillOval(position.x - 10, position.y - 10, 20, 20);
//
//        gc.setFill(Color.RED);
//        gc.setStroke(Color.RED);
//        double xCoord = position.x + agent.getVelocity().x*20;
//        double yCoord = position.y + agent.getVelocity().y*20;
//        gc.strokeLine(position.x, position.y, xCoord, yCoord);
//        gc.fillOval(xCoord -5, yCoord -5, 10, 10);
//
//    }
//
//    public static void main(String[] args) {
//        launch();
//    }
//}
