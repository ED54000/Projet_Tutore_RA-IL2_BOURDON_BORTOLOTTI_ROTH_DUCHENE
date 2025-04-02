package moteur;

import entites.enemies.Ennemy;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import laby.ModeleLabyrinth;
import laby.views.ViewLabyrinth;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConfigWindow {

    ModeleLabyrinth laby;
    Stage stage;
    Canvas canvasStage;
    MoteurJeu moteur;

    public ConfigWindow(ModeleLabyrinth laby, Stage stage, Canvas canvas, MoteurJeu moteurJeu) {
        this.laby = laby;
        this.stage = stage;
        this.moteur = moteurJeu;
        this.canvasStage = canvas;
    }

    public void show(Stage primaryStage) {
        {
            MoteurJeu.primaryStage = primaryStage;
            // Création des vues
            ViewLabyrinth viewLabyrinth = new ViewLabyrinth(laby, canvasStage);
            laby.registerObserver(viewLabyrinth);

            // Configuration de la fenêtre de dialogue
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Configuration du Labyrinthe");

            // Conteneur principal avec style moderne
            VBox root = new VBox(20);
            root.setPadding(new Insets(30));
            root.setStyle("-fx-background-color: #f5f5f5; "
                    + "-fx-background-radius: 10; "
                    + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

            // En-tête
            Label titleLabel = new Label("Configuration de la partie");
            titleLabel.setStyle("-fx-font-size: 24px; "
                    + "-fx-font-weight: bold; "
                    + "-fx-text-fill: #2c3e50; "
                    + "-fx-padding: 0 0 20 0;");
            titleLabel.setAlignment(Pos.CENTER);
            titleLabel.setMaxWidth(Double.MAX_VALUE);

            // Section de sélection du labyrinthe préfait
            ComboBox<String> configPrefaiteComboBox = new ComboBox<>();
            configPrefaiteComboBox.getItems().addAll(
                    "Aucune",
                    "Chemin safe",
                    "Aucun chemin safe",
                    "Une tour unique",
                    "Nombreuses tours centrales",
                    "Tours inaccessibles"
            );
            configPrefaiteComboBox.setValue("Aucune");
            configPrefaiteComboBox.setId("configPrefaiteComboBox");
            configPrefaiteComboBox.setStyle("-fx-background-color: #f8f9fa; "
                    + "-fx-background-radius: 5; "
                    + "-fx-border-color: #dee2e6; "
                    + "-fx-border-radius: 5; "
                    + "-fx-padding: 5; ");

            VBox configPrefaiteSection = createSection("Choisir une configuration de test", configPrefaiteComboBox);

            // Map des labyrinthes
            Map<String, String> labyrinthMap = new HashMap<>();
            labyrinthMap.put("Petit", "Ressources/Labyrinthe1.txt");
            labyrinthMap.put("Grand", "Ressources/Labyrinthe2.txt");
            labyrinthMap.put("Large", "Ressources/Labyrinthe3.txt");
            labyrinthMap.put("Large sans safe", "Ressources/Labyrinthe3_bis.txt");
            labyrinthMap.put("Ligne", "Ressources/Ligne.txt");
            labyrinthMap.put("Nombreuses tours centrales", "Ressources/LignePlusieursTours.txt");
            labyrinthMap.put("Tours inaccessibles", "Ressources/ToursInaccessibles.txt");

            // Section de sélection du labyrinthe
            ComboBox<String> labyrinthComboBox = new ComboBox<>();
            labyrinthComboBox.getItems().addAll(
                    "Petit",
                    "Grand",
                    "Large",
                    "Large sans safe",
                    "Ligne",
                    "Nombreuses tours centrales",
                    "Tours inaccessibles",
                    "Plus"
            );
            labyrinthComboBox.setValue("Large");
            labyrinthComboBox.setId("labyrinthComboBox");
            labyrinthComboBox.setStyle("-fx-background-color: #f8f9fa; "
                    + "-fx-background-radius: 5; "
                    + "-fx-border-color: #dee2e6; "
                    + "-fx-border-radius: 5; "
                    + "-fx-padding: 5;");

            VBox labyrinthSection = createSection("Choisir le labyrinthe", labyrinthComboBox);

            // Section configuration des ennemis
            TextField enemiesField = createStyledTextField("30", "enemiesField");
            VBox enemiesSection = createSection("Nombre d'ennemis", enemiesField);

            TextField nbEnnemiesToWinField = createStyledTextField("28", "ennemiesToWinField");
            VBox objectifSection = createSection("Objectif d'ennemis à l'arrivée", nbEnnemiesToWinField);

            // Section nombre de manches
            TextField roundsField = createStyledTextField("20", "roundsField");
            VBox roundsSection = createSection("Nombre de manches", roundsField);

            // Section A*
            CheckBox avecAstarBox = new CheckBox();
            avecAstarBox.setSelected(true);
            avecAstarBox.setId("avecOuSansAstarBox");
            avecAstarBox.setStyle("-fx-padding: 5;");

            VBox astarSection = createSection("Utiliser l'algorithme A*", avecAstarBox);

            // Bouton de démarrage
            Button startButton = new Button("Démarrer la partie");
            startButton.setStyle("-fx-background-color: #4CAF50; "
                    + "-fx-text-fill: white; "
                    + "-fx-font-size: 14px; "
                    + "-fx-font-weight: bold; "
                    + "-fx-padding: 12 24; "
                    + "-fx-background-radius: 5; "
                    + "-fx-cursor: hand;");
            startButton.setAlignment(Pos.CENTER);
            startButton.setMaxWidth(Double.MAX_VALUE);

            // Effet hover pour le bouton
            startButton.setOnMouseEntered(e ->
                    startButton.setStyle("-fx-background-color: #45a049; "
                            + "-fx-text-fill: white; "
                            + "-fx-font-size: 14px; "
                            + "-fx-font-weight: bold; "
                            + "-fx-padding: 12 24; "
                            + "-fx-background-radius: 5; "
                            + "-fx-cursor: hand; "
                            + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 0);")
            );

            startButton.setOnMouseExited(e ->
                    startButton.setStyle("-fx-background-color: #4CAF50; "
                            + "-fx-text-fill: white; "
                            + "-fx-font-size: 14px; "
                            + "-fx-font-weight: bold; "
                            + "-fx-padding: 12 24; "
                            + "-fx-background-radius: 5; "
                            + "-fx-cursor: hand;")
            );

            configPrefaiteComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == "Aucune") {
                    // Réactiver les autres ComboBox pour permettre la modification
                    labyrinthComboBox.setDisable(false);
                    enemiesField.setDisable(false);
                    nbEnnemiesToWinField.setDisable(false);
                    roundsField.setDisable(false);
                    avecAstarBox.setDisable(false);
                } else {
                    labyrinthComboBox.setDisable(true);
                    enemiesField.setDisable(true);
                    nbEnnemiesToWinField.setDisable(true);
                    roundsField.setDisable(true);
                    avecAstarBox.setDisable(true);
                    switch (newValue) {
                        case "Chemin safe":
                            labyrinthComboBox.setValue("Large");
                            enemiesField.setText("10");
                            nbEnnemiesToWinField.setText("11");
                            roundsField.setText("10");
                            avecAstarBox.setSelected(true);
                            break;
                        case "Aucun chemin safe":
                            labyrinthComboBox.setValue("Large sans safe");
                            enemiesField.setText("40");
                            nbEnnemiesToWinField.setText("37");
                            roundsField.setText("20");
                            avecAstarBox.setSelected(true);
                            break;
                        case "Une tour unique":
                            labyrinthComboBox.setValue("Ligne");
                            enemiesField.setText("5");
                            nbEnnemiesToWinField.setText("5");
                            roundsField.setText("40");
                            avecAstarBox.setSelected(true);
                            break;
                        case "Nombreuses tours centrales":
                            labyrinthComboBox.setValue("Nombreuses tours centrales");
                            enemiesField.setText("20");
                            nbEnnemiesToWinField.setText("15");
                            roundsField.setText("10");
                            avecAstarBox.setSelected(true);
                            break;
                        case "Tours inaccessibles":
                            labyrinthComboBox.setValue("Tours inaccessibles");
                            enemiesField.setText("20");
                            nbEnnemiesToWinField.setText("15");
                            roundsField.setText("10");
                            avecAstarBox.setSelected(true);
                            break;
                        default:
                            break;
                    }
                }
            });

            startButton.setOnMouseClicked(event -> {
                laby.setStartTime();
                String labyrinthString;
                switch (labyrinthComboBox.getValue()) {
                    case "Plus":
                        labyrinthString = openLaby();
                        break;
                    default:
                        labyrinthString = labyrinthMap.get(labyrinthComboBox.getValue());
                        break;
                }
                dialogStage.close();
                try {
                    laby.setUseAstar(avecAstarBox.isSelected());
                    ArrayList<Ennemy> ennemies = ModeleLabyrinth.createEnnemies(
                            Integer.parseInt(enemiesField.getText())
                    );
                    laby.creerLabyrinthe(
                            labyrinthString,
                            ennemies,
                            Integer.parseInt(roundsField.getText()),
                            Integer.parseInt(nbEnnemiesToWinField.getText())
                    );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                moteur.startJeu(primaryStage);
                MoteurJeu.labyFile = labyrinthString;
            });

            // Assemblage de l'interface
            HBox sectionsContainer = new HBox(20);
            VBox leftColumn = new VBox(20, configPrefaiteSection, enemiesSection, roundsSection);
            VBox rightColumn = new VBox(20, labyrinthSection, objectifSection, astarSection);
            sectionsContainer.getChildren().addAll(leftColumn, rightColumn);

            root.getChildren().addAll(
                    titleLabel,
                    sectionsContainer,
                    startButton
            );

            // Configuration de la scène
            Scene dialogScene = new Scene(root, 560, 525);
            dialogScene.setFill(Color.TRANSPARENT);
            dialogStage.setScene(dialogScene);
            dialogStage.initOwner(primaryStage);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            Image icon = new Image("icon.png");
            dialogStage.getIcons().add(icon);
            dialogStage.showAndWait();
        }

    }

    private String openLaby() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner un fichier de labyrinthe");
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Fichiers texte (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File("Ressources"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            MoteurJeu.labyFile = selectedFile.getAbsolutePath();
            return selectedFile.getAbsolutePath();
        } else {
            System.err.println("Aucun fichier sélectionné, utilisation du labyrinthe par défaut");
            MoteurJeu.labyFile = "Ressources/Labyrinthe1.txt";
            return "Ressources/Labyrinthe1.txt";
        }
    }

    // Méthode utilitaire pour créer une section
    private VBox createSection(String labelText, Node content) {
        VBox section = new VBox(10);
        section.setStyle("-fx-background-color: white; "
                + "-fx-padding: 15; "
                + "-fx-background-radius: 5; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 0);");

        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 14px; "
                + "-fx-font-weight: bold; "
                + "-fx-text-fill: #34495e;");

        section.getChildren().addAll(label, content);
        return section;
    }

    // Méthode utilitaire pour créer un TextField stylisé
    private TextField createStyledTextField(String defaultValue, String id) {
        TextField field = new TextField(defaultValue);
        field.setId(id);
        field.setStyle("-fx-background-color: #f8f9fa; "
                + "-fx-background-radius: 5; "
                + "-fx-border-color: #dee2e6; "
                + "-fx-border-radius: 5; "
                + "-fx-padding: 8; "
                + "-fx-font-size: 13px;");

        // Effet focus
        field.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (isFocused) {
                field.setStyle("-fx-background-color: white; "
                        + "-fx-background-radius: 5; "
                        + "-fx-border-color: #4CAF50; "
                        + "-fx-border-radius: 5; "
                        + "-fx-padding: 8; "
                        + "-fx-font-size: 13px; "
                        + "-fx-effect: dropshadow(three-pass-box, rgba(76,175,80,0.2), 5, 0, 0, 0);");
            } else {
                field.setStyle("-fx-background-color: #f8f9fa; "
                        + "-fx-background-radius: 5; "
                        + "-fx-border-color: #dee2e6; "
                        + "-fx-border-radius: 5; "
                        + "-fx-padding: 8; "
                        + "-fx-font-size: 13px;");
            }
        });

        return field;
    }
}
