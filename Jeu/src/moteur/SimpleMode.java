package moteur;

import entites.defenses.Defense;
import entites.enemies.Ennemy;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import laby.ModeleLabyrinth;
import laby.views.ViewLabyrinth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SimpleMode {
    private static boolean simpleMode = false;

    /**
     * @param vue vue du labyrinthe
     */
    public void enableSimpleMode(ViewLabyrinth vue, ModeleLabyrinth laby) {
        setSimpleMode(true);
        // On crée les sprites Images du jeu
        Image tree = new Image("/blackSquare.png");
        Image tile = new Image("/whiteSquare.png");

        // On applique les sprites aux cases (sol, murs)
        Map<Character, Image> newImages = new HashMap<>();
        for (Map.Entry<Character, Image> entry : vue.getImages().entrySet()) {
            newImages.put(entry.getKey(), entry.getValue());
        }
        newImages.put(ModeleLabyrinth.TREE, tree);
        newImages.put(ModeleLabyrinth.ROAD, tile);
        vue.setImages(newImages);

        // On applique les sprites aux ennemis
        ArrayList<Ennemy> allEnnemies = new ArrayList<>(); // Liste des ennemis vivants et morts
        allEnnemies.addAll(laby.enemies);
        allEnnemies.addAll(laby.deadEnemies);
        for (Ennemy ennemy : allEnnemies) {
            switch (ennemy.getBehaviorString()) {
                case "Normal":
                    ennemy.setSprite(addTextToImage("" + (int) ennemy.getHealth(), new Image("/gray.png")));
                    break;
                case "Kamikaze":
                    ennemy.setSprite(addTextToImage("" + (int) ennemy.getHealth(), new Image("/red.png")));
                    break;
                case "Healer":
                    ennemy.setSprite(addTextToImage("" + (int) ennemy.getHealth(), new Image("/green.png")));
                    break;
                case "Fugitive":
                    ennemy.setSprite(addTextToImage("" + (int) ennemy.getHealth(), new Image("/blue.png")));
                    break;
            }
        }

        // On applique les sprites aux defenses
        ArrayList<Defense> allDefenses = new ArrayList<>();
        allDefenses.addAll(laby.defenses);
        allDefenses.addAll(laby.deadDefenses);
        for (Defense defense : allDefenses) {
            if (defense.getName().contains("Archer")) {
                defense.setSprite(addTextToImage("" + (int) defense.getHealth(), new Image("/cross_purple.png")));
            } else if(defense.getName().contains("Canon")) {
                defense.setSprite(addTextToImage("" + (int) defense.getHealth(), new Image("/cross_yellow.png")));;
            }
        }
    }

    /**
     * Méthode permettant de désactiver le mode simple
     *
     * @param vue vue du labyrinthe
     */
    public void disableSimpleMode(ViewLabyrinth vue, ModeleLabyrinth laby) {
        setSimpleMode(false);
        // On crée les sprites Images du jeu
        Image tree = new Image("/tree3.png");
        Image tile = new Image("/tiles2.png");

        // On applique les sprites aux entités
        Map<Character, Image> newImages = new HashMap<>();
        for (Map.Entry<Character, Image> entry : vue.getImages().entrySet()) {
            newImages.put(entry.getKey(), entry.getValue());
        }
        newImages.put(ModeleLabyrinth.TREE, tree);
        newImages.put(ModeleLabyrinth.ROAD, tile);
        vue.setImages(newImages);

        // On applique les sprites aux ennemis
        ArrayList<Ennemy> allEnnemies = new ArrayList<>(); // Liste des ennemis vivants et morts
        allEnnemies.addAll(laby.enemies);
        allEnnemies.addAll(laby.deadEnemies);
        for (Ennemy ennemy : allEnnemies) {
            switch (ennemy.getBehaviorString()) {
                case "Normal":
                    ennemy.setSprite(new Image("/giant.png"));
                    break;
                case "Kamikaze":
                    ennemy.setSprite(new Image("/berserker.png"));
                    break;
                case "Healer":
                    ennemy.setSprite(new Image("/druide.png"));
                    break;
                case "Fugitive":
                    ennemy.setSprite(new Image("/ninja.png"));
                    break;
            }
        }

        // On applique les sprites aux defenses
        ArrayList<Defense> allDefenses = new ArrayList<>();
        allDefenses.addAll(laby.defenses);
        allDefenses.addAll(laby.deadDefenses);
        for (Defense defense : allDefenses) {
            if (defense.getName().contains("Archer")) {
                defense.setSprite(new Image("/tower1.png"));
            } else if(defense.getName().contains("Canon")) {
                defense.setSprite(new Image("/canon1.png"));
            }
        }
    }

    public void setSimpleMode(boolean mode) {
        simpleMode = mode;
    }

    public static boolean getSimpleMode() {
        return simpleMode;
    }

    /**
     * Méthode pour afficher du texte sur une image
     *
     * @param text  Texte à afficher
     * @param image Image sur laquelle afficher le texte
     * @return Image avec le texte
     */
    public static Image addTextToImage(String text, Image image) {
        WritableImage writableImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        Canvas canvas = new Canvas(image.getWidth(), image.getHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(image, 0, 0);

        // calcule la taille du texte en fonction de la taille de l'image
        double fontSize = image.getHeight() / 5; // ajuste la taille de la police en fonction de la taille de l'image
        Font font = Font.font("Arial", FontWeight.BOLD, fontSize);
        javafx.scene.text.Text tempText = new javafx.scene.text.Text(text);
        tempText.setFont(font);

        gc.setFont(font);
        gc.setFill(Color.WHITE);

        // calcule la position du texte (au centre)
        double textWidth = tempText.getLayoutBounds().getWidth();
        double textHeight = tempText.getLayoutBounds().getHeight();
        double x = (image.getWidth() - textWidth) / 2;
        double y = (image.getHeight() + textHeight) / 2;

        // dessine le texte sur l'image
        gc.fillText(text, x, y);

        // capture le canvas dans l'image
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        canvas.snapshot(params, writableImage);
        return writableImage;
    }

}
