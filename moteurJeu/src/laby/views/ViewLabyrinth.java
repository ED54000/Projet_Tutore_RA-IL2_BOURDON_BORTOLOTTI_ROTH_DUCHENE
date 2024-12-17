package laby.views;

import entites.enemies.Ennemy;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import laby.ModeleLabyrinth;
import laby.Observer;
import laby.Subject;

public class ViewLabyrinth implements Observer {
    static int tailleCase = 50;
    private ModeleLabyrinth laby;
    private Canvas canvas;
    private Image tree, canon, archer, bomb, road, start, end;

    public ViewLabyrinth(ModeleLabyrinth laby, Canvas canvas) {
        this.laby = laby;
        this.canvas = canvas;
        tree = new Image("/tree.png");
        road = new Image("/tiles.png");
        archer = new Image("/archer.png");
        bomb = new Image("/bomb.png");
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
                        gc.drawImage(archer, j * tailleCase, i * tailleCase, tailleCase, tailleCase); //a chnager
                        break;
                    case ModeleLabyrinth.BOMB:
                        gc.drawImage(road, j * tailleCase, i * tailleCase, tailleCase, tailleCase);
                        gc.drawImage(bomb, j * tailleCase-3, i * tailleCase-5, tailleCase+5, tailleCase+5);
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
                    default:
                        break;
                }
            }
        }

        //dessiner les ennemis
        for (int i = 0; i < laby.enemies.size(); i++) {
            Ennemy ennemy = laby.enemies.get(i);

            gc.setFill(Color.RED);
            gc.fillOval(ennemy.getX() * tailleCase, ennemy.getY() * tailleCase, tailleCase/3, tailleCase/3);
        }

        //dessiner la range des defenses
        for (int i = 0; i < laby.defenses.size(); i++) {
            double x = laby.defenses.get(i).getX() * tailleCase;
            double y = laby.defenses.get(i).getY() * tailleCase;
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
}
