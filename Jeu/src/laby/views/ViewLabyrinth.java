package laby.views;

import entites.defenses.Defense;
import entites.enemies.Ennemy;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import laby.ModeleLabyrinth;
import laby.Observer;
import laby.Subject;
import moteur.MoteurJeu;
import steering_astar.Steering.Behavior;
import steering_astar.Steering.Vector2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.Element;
import javax.swing.text.html.ImageView;

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
        images.put(ModeleLabyrinth.TREE, new Image("/tree3.png"));
        images.put(ModeleLabyrinth.ROAD, new Image("/tiles3.png"));
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
            if (defense.isHit()){
                applyRedFilter(spriteDefense);
            }
            double x = defense.getPosition().getX() * getTailleCase() - getTailleCase() / 2.0;
            double y = defense.getPosition().getY() * getTailleCase() - getTailleCase() / 2.0;

            if (defense instanceof entites.defenses.Canon) {
                gc.drawImage(images.get(ModeleLabyrinth.ROAD), x, y, getTailleCase(), getTailleCase());
                if (!defense.getIsDead()) {
                    gc.drawImage(spriteDefense, x, y, getTailleCase(), getTailleCase());
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
        Color colorPath = Color.rgb(15, 175, 252);
        for (Ennemy ennemi : laby.enemies) {
            ArrayList<Vector2D> path = new ArrayList<>();
            if(ModeleLabyrinth.getLabyrinth().getUseAstar()){
                path = ennemi.calculerChemin(ModeleLabyrinth.getCases(), ModeleLabyrinth.getStart());
            }
            renderEnnemi(gc, ennemi, path, colorPath);
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

    private void renderEnnemi(GraphicsContext gc, Ennemy ennemi, ArrayList<Vector2D> checkpoint, Color pathColor) {
        double radius = Behavior.getTargetRadius();
        int tCase = getTailleCase();
        double xCoordEnnemi = ennemi.getPosition().getX();
        double yCoordEnnemi = ennemi.getPosition().getY();
        double xCoordVelocity = ennemi.getVelocity().getX();
        double yCoordVelocity = ennemi.getVelocity().getY();
        double range = ennemi.getRange() * tCase;

        double ennemiSize = 20;
        double waypointsSize = 10;
        double velocityPointSize = 10;
        double velocityPointMultiplier = 20;

        // points de passage
        /*gc.setFill(pathColor);
        gc.setStroke(pathColor);
        if (laby.getUseAstar()) {
            for (Vector2D point : checkpoint) {
                gc.fillOval(point.getX() - waypointsSize / 2, point.getY() - waypointsSize / 2, waypointsSize, waypointsSize);
                gc.strokeOval(point.getX() - radius / 2, point.getY() - radius / 2, radius, radius);
            }
        }*/

        // vélocité de l'ennemi
        // Si on est en mode simple, on ne dessine pas la vélocité de l'ennemi
        if(!MoteurJeu.getSimpleMode()) {
            gc.setFill(Color.RED);
            gc.setStroke(Color.RED);
            double xCoord = xCoordEnnemi + xCoordVelocity * velocityPointMultiplier;
            double yCoord = yCoordEnnemi + yCoordVelocity * velocityPointMultiplier;
            gc.strokeLine(xCoordEnnemi, yCoordEnnemi, xCoord, yCoord);
            gc.fillOval(xCoord - velocityPointSize / 2, yCoord - velocityPointSize / 2, velocityPointSize, velocityPointSize);
        }

        Image image = ennemi.getImage();

        // Si l'ennemi est touché on met l'image en rouge
        if (ennemi.isHit()){
            image = applyRedFilter(image);
        }

        // ennemi
        gc.drawImage(image,
                xCoordEnnemi - getTailleCase() / 2.0,
                yCoordEnnemi - getTailleCase() / 2.0,
                getTailleCase(), getTailleCase());



        // range des ennemis
        // Si on est en mode simple, on ne dessine pas la range des ennemis
        if(!MoteurJeu.getSimpleMode()){
            gc.setStroke(Color.BLACK);
            gc.strokeOval(xCoordEnnemi - range, yCoordEnnemi - range, 2 * range, 2 * range);
        }
    }

    public void setImages(Map<Character, Image> newImages) {
        images.clear();
        images.putAll(newImages);
    }

    public Map<Character, Image> getImages() {return images;}
    private Image applyRedFilter(Image originalImage) {
        int width = (int) originalImage.getWidth();
        int height = (int) originalImage.getHeight();
        WritableImage redFilteredImage = new WritableImage(width, height);

        PixelReader pixelReader = originalImage.getPixelReader();
        PixelWriter pixelWriter = redFilteredImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color originalColor = pixelReader.getColor(x, y);

                // Mélange la couleur originale avec du rouge
                double red = Math.min(1.0, originalColor.getRed() + 0.5); // Augmente le rouge
                double green = originalColor.getGreen() * 0.5; // Diminue le vert
                double blue = originalColor.getBlue() * 0.5; // Diminue le bleu

                Color newColor = new Color(red, green, blue, originalColor.getOpacity());
                pixelWriter.setColor(x, y, newColor);
            }
        }
        return redFilteredImage;
    }


}