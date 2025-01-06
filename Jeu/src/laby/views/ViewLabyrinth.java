package laby.views;

import entites.defenses.Defense;
import entites.enemies.Ennemy;
import entites.enemies.Ninja;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import laby.ModeleLabyrinth;
import laby.Observer;
import laby.Subject;
import moteur.FrameStats;
import steering_astar.Steering.Behavior;
import steering_astar.Steering.Vector2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewLabyrinth implements Observer {
    static Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    static int tailleCase = 50;
    private static ModeleLabyrinth laby;
    private Canvas canvas;
    private Image tree, canon, archer, bomb, road, start, end;
    private final Map<Character, Image> images = new HashMap<>();

    public ViewLabyrinth(ModeleLabyrinth laby, Canvas canvas) {
        this.laby = laby;
        this.canvas = canvas;
        Canvas canvasEnnemis = new Canvas(canvas.getWidth(), canvas.getHeight());


        // Chargement des images
        images.put(ModeleLabyrinth.TREE, new Image("/tree3.png"));
        images.put(ModeleLabyrinth.ROAD, new Image("/tiles3.png"));
        images.put(ModeleLabyrinth.BOMB, new Image("/bomb.png"));
        images.put(ModeleLabyrinth.ARCHER, new Image("/archerClash.png"));
        images.put(ModeleLabyrinth.CANON, new Image("/canon.png"));
    }

    @Override
    public void update(Subject s) {
        ModeleLabyrinth laby = (ModeleLabyrinth) s;
        dessinerJeu(laby, canvas);
    }

    private void dessinerJeu(ModeleLabyrinth laby, Canvas canvas) {
        //on définit la taille des cases selon la taille de l'écran
        tailleCase = getTailleCase();

        // recupere un pinceau pour dessiner
        final GraphicsContext gc = canvas.getGraphicsContext2D();

        // Nettoyage du canvas
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Dessin du labyrinthe
            for (int i = 0; i < laby.getLength(); i++) {
                for (int j = 0; j < laby.getLengthY(); j++) {
                    dessinerCase(gc, laby.getCase(i, j), i, j);
                }
            }

        // Dessin des ennemis
        Color colorPath = Color.rgb(15, 175, 252);
        for (Ennemy ennemi : laby.enemies) {
            for (String behaviour : laby.getBehaviours()) {
                renderEnnemi(gc, ennemi, laby.getBehavioursMap().get(behaviour), colorPath);
            }
        }

        // Dessin des défenses
        for(Defense defense : laby.defenses){
            double x = defense.getPosition().getX() * getTailleCase();
            double y = defense.getPosition().getY() * getTailleCase();

            if (defense.getHealth() <= 0 && defense.isDead()) {
                gc.drawImage(images.get(ModeleLabyrinth.ROAD), x, y, getTailleCase(), getTailleCase());
                defense.setDead(true);
            }
            else {
                if (defense instanceof entites.defenses.Canon) {
                    gc.drawImage(images.get(ModeleLabyrinth.ROAD), x, y, getTailleCase(), getTailleCase());
                    gc.drawImage(images.get(ModeleLabyrinth.CANON), x, y, getTailleCase(), getTailleCase());
                } else if (defense instanceof entites.defenses.Archer) {
                    gc.drawImage(images.get(ModeleLabyrinth.TREE), x, y, getTailleCase(), getTailleCase());
                    gc.drawImage(images.get(ModeleLabyrinth.ARCHER), x - 12, y - 12, getTailleCase() + 25, getTailleCase() + 25);
                } else if (defense instanceof entites.defenses.Bomb) {
                    gc.drawImage(images.get(ModeleLabyrinth.ROAD), x, y, getTailleCase(), getTailleCase());
                    gc.drawImage(images.get(ModeleLabyrinth.BOMB), x - 12, y - 12, getTailleCase() + 25, getTailleCase() + 25);
                }

                // dessiner la range des défenses
                x = defense.getPosition().getX() * getTailleCase();;
                y = defense.getPosition().getY() * getTailleCase();
                double range = defense.getRange() * getTailleCase();

                gc.setFill(Color.color(0.0, 0.0, 0.0, 0.17));
                gc.fillOval(x - range + (getTailleCase() / 2.0), y - range + getTailleCase() / 2.0, 2 * range, 2 * range);

                gc.setStroke(Color.BLACK);
                gc.strokeOval(x - range + (getTailleCase() / 2.0), y - range + getTailleCase() / 2.0, 2 * range, 2 * range);
            }
        }
    }

    private void dessinerCase(GraphicsContext gc, char caseType, int i, int j) {
        int x = j * getTailleCase();
        int y = i * getTailleCase();

        switch (caseType) {
            //case ModeleLabyrinth.CANON -> gc.drawImage(images.get(ModeleLabyrinth.CANON), x, y, getTailleCase(), getTailleCase());
            /*case ModeleLabyrinth.BOMB -> {
                gc.drawImage(images.get(ModeleLabyrinth.ROAD), x, y, getTailleCase(), getTailleCase());
                gc.drawImage(images.get(ModeleLabyrinth.BOMB), x + 5, y + 5, getTailleCase() - 10, getTailleCase() - 10);
            }*/
            case ModeleLabyrinth.START -> {
                gc.setFill(Color.GREEN);
                gc.fillRect(x, y, getTailleCase(), getTailleCase());
            }
            case ModeleLabyrinth.END -> {
                gc.setFill(Color.RED);
                gc.fillRect(x, y, getTailleCase(), getTailleCase());
            }
            case ModeleLabyrinth.ROAD -> gc.drawImage(images.get(ModeleLabyrinth.ROAD), x, y, getTailleCase(), getTailleCase());
            case ModeleLabyrinth.TREE -> gc.drawImage(images.get(ModeleLabyrinth.TREE), x, y, getTailleCase(), getTailleCase());
            /*case ModeleLabyrinth.ARCHER -> {
                gc.drawImage(images.get(ModeleLabyrinth.TREE), x, y, getTailleCase(), getTailleCase());
                gc.drawImage(images.get(ModeleLabyrinth.ARCHER), x - 12, y - 12, getTailleCase() + 25, getTailleCase() + 25);
            }*/
            default -> {
            }
        }
    }

    private void renderEnnemi(GraphicsContext gc, Ennemy ennemi, ArrayList<Vector2D> checkpoint, Color pathColor) {
        //variables
        double radius = Behavior.getTargetRadius();
        Vector2D position = ennemi.getPosition();
        double xCoordEnnemi = position.getX();
        double yCoordEnnemi = position.getY();
        double xCoordVelocity = ennemi.getVelocity().getX();
        double yCoordVelocity = ennemi.getVelocity().getY();

        //constantes
        double ennemiSize = 20;
        double waypointsSize = 10;
        double velocityPointSize = 10;
        double velocityPointMultiplier = 20;

        //points de passage
        gc.setFill(pathColor);
        gc.setStroke(pathColor);
        for (Vector2D point : checkpoint){
            gc.fillOval(point.getX(), point.getY(), waypointsSize, waypointsSize);
            gc.strokeOval(point.getX() - radius/2 + waypointsSize/2, point.getY() - radius/2 + waypointsSize/2, radius, radius);
        }

        //vélocité de l'ennemi
        gc.setFill(Color.RED);
        gc.setStroke(Color.RED);
        double xCoord = xCoordEnnemi + xCoordVelocity * velocityPointMultiplier;
        double yCoord = yCoordEnnemi + yCoordVelocity * velocityPointMultiplier;
        gc.strokeLine(xCoordEnnemi + ennemiSize/2, yCoordEnnemi + ennemiSize/2, xCoord + ennemiSize/2, yCoord + ennemiSize/2);
        gc.fillOval(xCoord + ennemiSize/2 - velocityPointSize/2, yCoord + ennemiSize/2 - velocityPointSize/2, velocityPointSize, velocityPointSize);

        //ennemi
        gc.drawImage(ennemi.getImage(), xCoordEnnemi, yCoordEnnemi, getTailleCase(), getTailleCase());
    }

    public static int getTailleCase() {
        int largeurDisponible = (screenSize.width / 7) * 6;
        int taskbarSize = 2 ;
        int tailleCaseHorizontale = largeurDisponible / laby.getLengthY();
        int tailleCaseVerticale = screenSize.height / laby.getLength();
        int tailleCase = Math.min(tailleCaseHorizontale, tailleCaseVerticale);

        // Vérifier les débordements
        if (tailleCase * laby.getLengthY() > screenSize.width || tailleCase * laby.getLength() > screenSize.height) {
            tailleCase /= 2;
        }

        return tailleCase - taskbarSize ;
    }


    public static Dimension getScreenSize(){
        return screenSize;
    }
}

