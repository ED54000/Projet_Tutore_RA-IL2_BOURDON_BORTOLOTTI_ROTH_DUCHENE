package laby.views;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import laby.ModeleLabyrinth;
import laby.Observer;
import laby.Subject;

public class ViewLabyrinth implements Observer {
    static int tailleCase = 50;
    private ModeleLabyrinth laby;
    private Canvas canvas;

    public ViewLabyrinth(ModeleLabyrinth laby, Canvas canvas) {
        this.laby = laby;
        this.canvas = canvas;
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
                        gc.setFill(Color.CORAL);
                        gc.fillRect(j * tailleCase, i * tailleCase, tailleCase, tailleCase);
                        gc.setFill(Color.GREY);
                        gc.fillOval(j * tailleCase, i * tailleCase, tailleCase, tailleCase);
                        break;
                    case ModeleLabyrinth.BOMB:
                        gc.setFill(Color.BROWN);
                        gc.fillRect(j * tailleCase, i * tailleCase, tailleCase, tailleCase);
                        gc.setFill(Color.BLACK);
                        gc.fillOval(j * tailleCase, i * tailleCase, tailleCase, tailleCase);
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
                        gc.setFill(Color.BROWN);
                        gc.fillRect(j * tailleCase, i * tailleCase, tailleCase, tailleCase);
                        break;
                    case ModeleLabyrinth.TREE:
                        gc.setFill(Color.CORAL);
                        gc.fillRect(j * tailleCase, i * tailleCase, tailleCase, tailleCase);
                        break;
                    default:
                        break;
                }
            }
        }

        //dessiner les ennemis
        for (int i = 0; i < laby.enemies.size(); i++) {
            gc.setFill(Color.RED);
            gc.fillOval(laby.enemies.get(i).getX() * tailleCase, laby.enemies.get(i).getY() * tailleCase, tailleCase, tailleCase);
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
