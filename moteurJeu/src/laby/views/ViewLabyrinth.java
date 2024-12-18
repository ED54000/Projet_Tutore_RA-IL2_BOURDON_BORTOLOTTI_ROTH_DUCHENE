package laby.views;

import entites.enemies.Ennemy;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import laby.ModeleLabyrinth;
import laby.Observer;
import laby.Subject;
import steering_astar.Steering.Behavior;
import steering_astar.Steering.Vector2D;

import java.util.ArrayList;

public class ViewLabyrinth implements Observer {
    static int tailleCase = 50;
    private ModeleLabyrinth laby;
    private Canvas canvas;
    private Image tree, canon, archer, bomb, road, start, end;

    public ViewLabyrinth(ModeleLabyrinth laby, Canvas canvas) {
        this.laby = laby;
        this.canvas = canvas;
        tree = new Image("/tree4.png");
        road = new Image("/tiles4.png");
        bomb = new Image("/bomb.png");
        archer = new Image("/archerClash.png");
        canon = new Image("/canon.png");
    }

    @Override
    public void update(Subject s) {
        ModeleLabyrinth laby = (ModeleLabyrinth) s;
        dessinerJeu(laby, canvas);
    }

    private void dessinerJeu(ModeleLabyrinth laby, Canvas canvas) {
        // recupere un pinceau pour dessiner
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        //netoyer le canvas
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (int i = 0; i < laby.getLength(); i++) {
            for (int j = 0; j < laby.getLengthY(); j++) {
                switch (laby.getCase(i, j)) {
                    case ModeleLabyrinth.CANON:
                        /*
                        gc.setFill(Color.CORAL);
                        gc.fillRect(j * tailleCase, i * tailleCase, tailleCase, tailleCase);
                        gc.setFill(Color.GREY);
                        gc.fillOval(j * tailleCase, i * tailleCase, tailleCase, tailleCase);
                         */
                        gc.drawImage(canon, j * tailleCase, i * tailleCase, tailleCase, tailleCase); //a chnager
                        break;
                    case ModeleLabyrinth.BOMB:
                        gc.drawImage(road, j * tailleCase, i * tailleCase, tailleCase, tailleCase);
                        gc.drawImage(bomb, j * tailleCase+5, i * tailleCase+5, tailleCase-10, tailleCase-10);
                        break;
                    case ModeleLabyrinth.START:
                        gc.setFill(Color.GREEN);
                        gc.fillRect(j * tailleCase, i * tailleCase, tailleCase, tailleCase);
                        break;
                    case ModeleLabyrinth.END:
                        gc.setFill(Color.RED);
                        gc.fillRect(j * tailleCase, i * tailleCase, tailleCase, tailleCase);
                        break;
                    case ModeleLabyrinth.ROAD:
                        gc.drawImage(road, j * tailleCase, i * tailleCase, tailleCase, tailleCase);
                        break;
                    case ModeleLabyrinth.TREE:
                        gc.drawImage(tree, j * tailleCase, i * tailleCase, tailleCase, tailleCase);
                        break;
                    case ModeleLabyrinth.ARCHER:
                        gc.drawImage(tree, j * tailleCase, i * tailleCase, tailleCase, tailleCase);
                        gc.drawImage(archer, j * tailleCase-12, i * tailleCase-12, tailleCase+25, tailleCase+25);
                        break;
                    default:
                        break;
                }
            }
        }

        //dessiner les ennemis
        System.out.println("Nombre d'ennemis : " + laby.enemies.size());
        Color colorPath = Color.rgb((15), (175), (252));
        for (int i = 0; i < laby.enemies.size(); i++) {
            for (String behaviour : laby.getBehaviours()) {
                renderEnnemi(gc, laby.enemies.get(i), laby.getBehavioursMap().get(behaviour), colorPath, Color.RED);
            }
        }

        //dessiner la range des defenses
        for (int i = 0; i < laby.defenses.size(); i++) {
            double x = laby.defenses.get(i).getPosition().getX() * tailleCase;
            double y = laby.defenses.get(i).getPosition().getY() * tailleCase;
            double range = laby.defenses.get(i).getRange() * tailleCase;

            gc.setFill(Color.color(0.0, 0.0, 0.0, 0.17));
            gc.fillOval(x - range + (tailleCase/2), y - range + tailleCase/2, 2 * range, 2 * range);

            // Dessiner le contour
            gc.setStroke(Color.BLACK);
            gc.strokeOval(x - range + (tailleCase/2), y - range + tailleCase/2, 2 * range, 2 * range);
        }




        /*if (jeu.etreFini()) {
            if (laby.getPj().getPv() == 0) {
                gameover(canvas, laby);
            } else {
                win(canvas, laby);
            }
        }
         */
    }
    private void renderEnnemi(GraphicsContext gc, Ennemy ennemi, ArrayList<Vector2D> checkpoint, Color pathColor, Color agentColor) {

        gc.setFill(pathColor);
        double radius = Behavior.getTargetRadius();
        for (Vector2D point : checkpoint){
            gc.fillOval(point.getX() + 20, point.getY() + 20, 10, 10);
            gc.strokeOval(point.getX() - radius/2 + 25, point.getY() - radius/2 + 25, radius, radius);
        }

//        gc.fillOval(n.getX() + 20, n.getY() + 20, 10, 10)

        Vector2D position = ennemi.getPosition();
        gc.setFill(agentColor);
        gc.fillOval(position.getX() + 10, position.getY() + 10, 20, 20);

        gc.setFill(Color.RED);
        gc.setStroke(Color.RED);
        double xCoord = position.getX() + ennemi.getVelocity().getX() * 20;
        double yCoord = position.getY() + ennemi.getVelocity().getY() * 20;
        gc.strokeLine(position.getX() + 20, position.getY() + 20, xCoord, yCoord);
        gc.fillOval(xCoord - 5, yCoord - 5, 10, 10);

    }

    public static int getTailleCase(){
        return tailleCase;
    }
}
