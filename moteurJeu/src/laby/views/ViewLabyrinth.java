package laby.views;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import laby.Labyrinth;
import laby.Observer;
import laby.Subject;
import moteur.Jeu;

public class ViewLabyrinth implements Observer {
    static int tailleCase = 50;
    private Labyrinth laby;
    private Canvas canvas;

    public ViewLabyrinth(Labyrinth laby, Canvas canvas) {
        this.laby = laby;
        this.canvas = canvas;
    }

    @Override
    public void update(Subject s) {
        Labyrinth laby = (Labyrinth) s;

        // recupere un pinceau pour dessiner
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        //netoyer le canvas
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (int i = 0; i < laby.getLength(); i++) {
            for (int j = 0; j < laby.getLengthY(); j++) {
                switch (laby.getCase(i, j)) {
                    case Labyrinth.CANON:
                        gc.setFill(Color.CORAL);
                        gc.fillRect(j * tailleCase, i * tailleCase, tailleCase, tailleCase);
                        gc.setFill(Color.BLACK);
                        gc.fillOval(j * tailleCase, i * tailleCase, tailleCase, tailleCase);
                        break;
                    case Labyrinth.BOMB:
                        //met le fond en Brun
                        gc.setFill(Color.CORAL);
                        gc.fillRect(j * tailleCase, i * tailleCase, tailleCase, tailleCase);
                        gc.setFill(Color.BLACK);
                        gc.fillOval(j * tailleCase, i * tailleCase, tailleCase, tailleCase);
                        break;
                    case Labyrinth.START:
                        gc.setFill(Color.GREEN);
                        gc.fillRect(j * tailleCase, i * tailleCase, tailleCase, tailleCase);
                        break;
                    case Labyrinth.END:
                        gc.setFill(Color.RED);
                        gc.fillRect(j * tailleCase, i * tailleCase, tailleCase, tailleCase);
                        break;
                    case Labyrinth.ROAD:
                        gc.setFill(Color.BROWN);
                        gc.fillRect(j * tailleCase, i * tailleCase, tailleCase, tailleCase);
                        break;
                    case Labyrinth.TREE:
                        gc.setFill(Color.CORAL);
                        gc.fillRect(j * tailleCase, i * tailleCase, tailleCase, tailleCase);
                        break;
                    default:
                        break;
                }
            }
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
