package laby.views;

import entites.defenses.Defense;
import entites.enemies.Ennemy;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import laby.ModeleLabyrinth;
import laby.Observer;
import laby.Subject;
import moteur.MoteurJeu;
import moteur.SimpleMode;
import steering_astar.Steering.Behavior;

import java.util.HashMap;
import java.util.Map;

import static laby.ModeleLabyrinth.getTailleCase;

public class ViewLabyrinth implements Observer {
    static int tailleCase = 50;
    private static ModeleLabyrinth laby;
    private Canvas canvas;
    private final Map<Character, Image> images = new HashMap<>();

    public ViewLabyrinth(ModeleLabyrinth laby, Canvas canvas) {
        this.laby = laby;
        this.canvas = canvas;

        // Chargement des images
        images.put(ModeleLabyrinth.TREE, new Image("tree3.png"));
        images.put(ModeleLabyrinth.ROAD, new Image("tiles2.png"));
    }

    @Override
    public void update(Subject s) {
        ModeleLabyrinth laby = (ModeleLabyrinth) s;
        dessinerJeu(laby, canvas);
    }

    private void dessinerJeu(ModeleLabyrinth laby, Canvas canvas) {
        tailleCase = getTailleCase();
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

        // Dessin des défenses mortes
        for (Defense defense : laby.deadDefenses) {
            double x = defense.getPosition().getX() * getTailleCase() - getTailleCase() / 2.0;
            double y = defense.getPosition().getY() * getTailleCase() - getTailleCase() / 2.0;

            if (defense instanceof entites.defenses.Bomb) {
                gc.drawImage(images.get(ModeleLabyrinth.ROAD), x, y, getTailleCase(), getTailleCase());
            } else {
                gc.drawImage(images.get(ModeleLabyrinth.TREE), x, y, getTailleCase(), getTailleCase());
            }
        }

        // Dessin des défenses
        for (Defense defense : laby.defenses) {
            Image spriteDefense = defense.getImage();
            if (defense.getIsHit()) {
                spriteDefense = defense.getSpriteHit();
                Timeline timeline = new Timeline(new KeyFrame(
                        Duration.millis(500),
                        ae -> {
                            defense.setSprite(defense.getImage());
                            defense.setIsHit(false);
                        }
                ));
                timeline.setCycleCount(1);
                timeline.play();
            }

            double x = defense.getPosition().getX() * getTailleCase() - getTailleCase() / 2.0;
            double y = defense.getPosition().getY() * getTailleCase() - getTailleCase() / 2.0;

            if (defense instanceof entites.defenses.Canon) {
                gc.drawImage(images.get(ModeleLabyrinth.ROAD), x, y, getTailleCase(), getTailleCase());
                if (!defense.getIsDead()) {
                    gc.drawImage(spriteDefense, x-17, y-25, getTailleCase()+50, getTailleCase()+50);
                }
            }
            if (defense instanceof entites.defenses.Archer) {
                gc.drawImage(images.get(ModeleLabyrinth.TREE), x, y, getTailleCase(), getTailleCase());
                if (!defense.getIsDead()) {
                    gc.drawImage(spriteDefense, x - 12, y - 12, getTailleCase() + 25, getTailleCase() + 25);
                }
            }
            if (defense instanceof entites.defenses.Bomb) {
                gc.drawImage(images.get(ModeleLabyrinth.ROAD), x, y, getTailleCase(), getTailleCase());
                if (!defense.getIsDead()) {
                    gc.drawImage(spriteDefense, x - 12, y - 12, getTailleCase() + 25, getTailleCase() + 25);
                }
            }

            // dessiner la range des défenses
            if (!defense.getIsDead()) {
                double centerX = defense.getPosition().getX() * getTailleCase();
                double centerY = defense.getPosition().getY() * getTailleCase();
                double range = defense.getRange() * getTailleCase();

                gc.setFill(Color.color(0.0, 0.0, 0.0, 0.17));
                gc.fillOval(centerX - range, centerY - range, 2 * range, 2 * range);

                gc.setStroke(Color.BLACK);
                gc.strokeOval(centerX - range, centerY - range, 2 * range, 2 * range);
            }
        }

        // Dessin des ennemis
        for (Ennemy ennemi : laby.enemies) {
            renderEnnemi(gc, ennemi);
        }
    }

    private void dessinerCase(GraphicsContext gc, char caseType, int i, int j) {
        double x = j * getTailleCase() - getTailleCase() / 2.0;
        double y = i * getTailleCase() - getTailleCase() / 2.0;

        switch (caseType) {
            case ModeleLabyrinth.START -> {
                gc.setFill(Color.GREEN);
                gc.fillRect(x, y, getTailleCase(), getTailleCase());
            }
            case ModeleLabyrinth.END -> {
                gc.setFill(Color.RED);
                gc.fillRect(x, y, getTailleCase(), getTailleCase());
            }
            case ModeleLabyrinth.ROAD ->
                    gc.drawImage(images.get(ModeleLabyrinth.ROAD), x, y, getTailleCase(), getTailleCase());
            case ModeleLabyrinth.TREE ->
                    gc.drawImage(images.get(ModeleLabyrinth.TREE), x, y, getTailleCase(), getTailleCase());
            default -> {
            }
        }
    }

    private void renderEnnemi(GraphicsContext gc, Ennemy ennemi) {
        int tCase = getTailleCase();
        double xCoordEnnemi = ennemi.getPosition().getX();
        double yCoordEnnemi = ennemi.getPosition().getY();
        double xCoordVelocity = ennemi.getVelocity().getX();
        double yCoordVelocity = ennemi.getVelocity().getY();
        double range = ennemi.getRange() * tCase;
        double velocityPointSize = 10;
        double velocityPointMultiplier = 20;


        // vélocité de l'ennemi
        // Si on est en mode simple, on ne dessine pas la vélocité de l'ennemi
        if (!SimpleMode.getSimpleMode()) {
            gc.setFill(Color.RED);
            gc.setStroke(Color.RED);
            double xCoord = xCoordEnnemi + xCoordVelocity * velocityPointMultiplier;
            double yCoord = yCoordEnnemi + yCoordVelocity * velocityPointMultiplier;
            gc.strokeLine(xCoordEnnemi, yCoordEnnemi, xCoord, yCoord);
            gc.fillOval(xCoord - velocityPointSize / 2, yCoord - velocityPointSize / 2, velocityPointSize, velocityPointSize);
        }

        Image image = ennemi.getImage();

        // Si l'ennemi est touché on met l'image en rouge
        if (!SimpleMode.getSimpleMode()) {
            if (ennemi.getIsHit()) {
                image = ennemi.getSpriteHit();
                // Utiliser un Timeline pour réinitialiser l'image après un certain délai
                Timeline timeline = new Timeline(new KeyFrame(
                        Duration.millis(500), // Durée pendant laquelle l'image reste rouge
                        ae -> {
                            ennemi.setSprite(ennemi.getImage()); // Réinitialise l'image de l'ennemi
                            ennemi.setIsHit(false);
                        }
                ));
                timeline.setCycleCount(1);
                timeline.play();
            }
            if (ennemi.getIsHeal()) {
                image = ennemi.getSpriteHeal();
                Timeline timeline = new Timeline(new KeyFrame(
                        Duration.millis(500),
                        ae -> {
                            ennemi.setSprite(ennemi.getImage());
                            ennemi.setIsHeal(false);
                        }
                ));
                timeline.setCycleCount(1);
                timeline.play();
            }

        }
        // ennemi
        gc.drawImage(image,
                xCoordEnnemi -
                        getTailleCase() / 2.0,
                yCoordEnnemi -
                        getTailleCase() / 2.0,
                getTailleCase(), getTailleCase());


        // range des ennemis
        // Si on est en mode simple, on ne dessine pas la range des ennemis
        if (!SimpleMode.getSimpleMode()) {
            gc.setStroke(Color.BLACK);
            gc.strokeOval(xCoordEnnemi - range, yCoordEnnemi - range, 2 * range, 2 * range);
        }
    }

    public void setImages(Map<Character, Image> newImages) {
        images.clear();
        images.putAll(newImages);
    }

    public Map<Character, Image> getImages() {
        return images;
    }
}