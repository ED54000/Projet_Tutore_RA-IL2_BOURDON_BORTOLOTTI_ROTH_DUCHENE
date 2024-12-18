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
    private static final int TAILLE_CASE = 50;
    private final Map<Character, Image> images = new HashMap<>();

    public ViewLabyrinth(ModeleLabyrinth laby, Canvas canvas) {
        this.laby = laby;
        System.out.println(laby);
        this.canvas = canvas;

        // Chargement des images
        images.put(ModeleLabyrinth.TREE, new Image("/tree4.png"));
        images.put(ModeleLabyrinth.ROAD, new Image("/tiles4.png"));
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
                renderEnnemi(gc, ennemi, laby.getBehavioursMap().get(behaviour), colorPath, Color.RED);
            }
        }

        // dessiner la range des défenses
        laby.defenses.forEach(defense -> {
            double x = defense.getPosition().getX() * TAILLE_CASE;
            double y = defense.getPosition().getY() * TAILLE_CASE;
            double range = defense.getRange() * TAILLE_CASE;

            gc.setFill(Color.color(0.0, 0.0, 0.0, 0.17));
            gc.fillOval(x - range + (TAILLE_CASE / 2), y - range + TAILLE_CASE / 2, 2 * range, 2 * range);

            gc.setStroke(Color.BLACK);
            gc.strokeOval(x - range + (TAILLE_CASE / 2), y - range + TAILLE_CASE / 2, 2 * range, 2 * range);
        });
    }

    private void dessinerCase(GraphicsContext gc, char caseType, int i, int j) {
        int x = j * TAILLE_CASE;
        int y = i * TAILLE_CASE;

        switch (caseType) {
            case ModeleLabyrinth.CANON -> gc.drawImage(images.get(ModeleLabyrinth.CANON), x, y, TAILLE_CASE, TAILLE_CASE);
            case ModeleLabyrinth.BOMB -> {
                gc.drawImage(images.get(ModeleLabyrinth.ROAD), x, y, TAILLE_CASE, TAILLE_CASE);
                gc.drawImage(images.get(ModeleLabyrinth.BOMB), x + 5, y + 5, TAILLE_CASE - 10, TAILLE_CASE - 10);
            }
            case ModeleLabyrinth.START -> {
                gc.setFill(Color.GREEN);
                gc.fillRect(x, y, TAILLE_CASE, TAILLE_CASE);
            }
            case ModeleLabyrinth.END -> {
                gc.setFill(Color.RED);
                gc.fillRect(x, y, TAILLE_CASE, TAILLE_CASE);
            }
            case ModeleLabyrinth.ROAD -> gc.drawImage(images.get(ModeleLabyrinth.ROAD), x, y, TAILLE_CASE, TAILLE_CASE);
            case ModeleLabyrinth.TREE -> gc.drawImage(images.get(ModeleLabyrinth.TREE), x, y, TAILLE_CASE, TAILLE_CASE);
            case ModeleLabyrinth.ARCHER -> {
                gc.drawImage(images.get(ModeleLabyrinth.TREE), x, y, TAILLE_CASE, TAILLE_CASE);
                gc.drawImage(images.get(ModeleLabyrinth.ARCHER), x - 12, y - 12, TAILLE_CASE + 25, TAILLE_CASE + 25);
            }
            default -> {
            }
        }
    }

    private void renderEnnemi(GraphicsContext gc, Ennemy ennemi, ArrayList<Vector2D> checkpoint, Color pathColor, Color agentColor) {
        gc.setFill(pathColor);
        double radius = Behavior.getTargetRadius();

        for (Vector2D point : checkpoint) {
            gc.fillOval(point.getX() + 20, point.getY() + 20, 10, 10);
            gc.strokeOval(point.getX() - radius / 2 + 25, point.getY() - radius / 2 + 25, radius, radius);
        }

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
        if ( laby.getLengthY() >= laby.getLength()){
            if ((((screenSize.width/7)*6/laby.getLengthY())*laby.getLength()) > screenSize.height/laby.getLength()) {
                return screenSize.height/laby.getLength()-2;
            } else {
                return (screenSize.width/7)*6/laby.getLengthY();
            }
        } else {
            if (((screenSize.height/laby.getLength()*laby.getLengthY()) > screenSize.width/laby.getLengthY())) {
                return (screenSize.width/7)*6/laby.getLengthY();
            } else {
                return screenSize.height/laby.getLength()-2;
            }
        }
    }

    public static Dimension getScreenSize(){
        return screenSize;
    }
}

