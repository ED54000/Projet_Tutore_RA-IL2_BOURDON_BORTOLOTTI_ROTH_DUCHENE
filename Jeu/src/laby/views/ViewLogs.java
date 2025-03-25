package laby.views;

import entites.enemies.Ennemy;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import laby.ModeleLabyrinth;
import laby.Observer;
import laby.Subject;
import laby.controllers.ControllerLearn;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewLogs implements Observer {
    private ModeleLabyrinth laby;
    private VBox vbox;
    private ScrollPane scrollPane;
    private ControllerLearn controllerLearn;
    private ViewGraphiqueDirect graphique;

    private VBox ennemiesBox; // Zone pour afficher les ennemis
    private HashMap<Ennemy, Label> ennemiesLabels; // Associer chaque ennemi à son label

    public ViewLogs(ModeleLabyrinth laby, VBox logs, ViewGraphiqueDirect graphique) {
        this.laby = laby;
        this.graphique = graphique;
        //this.logs = (VBox)(logs.getChildren().get(1));
        //this.logs = logs;
        this.controllerLearn = new ControllerLearn(laby);

        this.scrollPane = (ScrollPane) logs.getChildren().get(1);
        this.vbox = (VBox) scrollPane.getContent();


        //ennemiesBox = new VBox();
        ennemiesLabels = new HashMap<>();

        //this.logs.getChildren().add(ennemiesBox);
    }

    @Override
    public void update(Subject s) {
        if (!laby.getLogs().isEmpty()) {
            Label label = new Label(laby.getLogs());

            // Style de base pour tous les logs
            String baseStyle = """
            -fx-padding: 3 8;
            -fx-background-color: white;
            -fx-background-radius: 3;
            -fx-border-color: #e0e0e0;
            -fx-border-radius: 3;
            -fx-font-family: 'Segoe UI', sans-serif;
            -fx-font-size: 11px;
        """;
            label.setStyle(baseStyle);
            label.setMaxWidth(Double.MAX_VALUE);
            vbox.setStyle("""
            -fx-background-color: #f5f5f5;
            -fx-padding: 5;
            -fx-spacing: 3;
        """);
            //vbox.getChildren().add(label);
            scrollPane.setStyle("""
            -fx-background: #f5f5f5;
            -fx-background-color: transparent;
            -fx-padding: 0;
        """);

            if (laby.getPauseManche() &&  laby.getLogs().matches("Manche \\d+ terminée")) {
                label.setStyle(baseStyle + """
                -fx-background-color: #e8f5e9;
                -fx-border-color: #81c784;
                -fx-font-weight: bold;
                -fx-text-fill: #2e7d32;
            """);

                Button button = new Button("Learn");
                button.setStyle("""
                -fx-background-color: #4CAF50;
                -fx-text-fill: white;
                -fx-font-size: 11px;
                -fx-padding: 3 10;
                -fx-background-radius: 3;
                -fx-cursor: hand;
            """);

                button.setOnMouseEntered(e ->
                        button.setStyle(button.getStyle() + "-fx-background-color: #45a049;"));
                button.setOnMouseExited(e ->
                        button.setStyle(button.getStyle() + "-fx-background-color: #4CAF50;"));

                button.setOnMouseClicked(controllerLearn);
                vbox.getChildren().addAll(label, button);
            } else if (laby.getLogs().matches("Manche \\d+")) {
                ennemiesBox = new VBox();
                vbox.getChildren().add(label);
                // Initialisation de la liste des labels des ennemis
                for (Ennemy ennemy : laby.enemies) {
                    // Création d'un nouveau label si l'ennemi n'existe pas encore
                    Label ennemyLabel = new Label(formatterEnnemy(ennemy));
                    ennemyLabel.setStyle("""
                -fx-padding: 3 8;
                -fx-background-color: white;
                -fx-border-color: #e0e0e0;
                -fx-border-radius: 3;
                -fx-font-size: 11px;
                                """);
                    ennemiesLabels.put(ennemy, ennemyLabel);
                    ennemiesBox.getChildren().add(ennemyLabel);
                }
                vbox.getChildren().add(ennemiesBox);

            } else {
                mettreAJourEnnemies(label.getText());
            }

            //Vérifie si la vbox contient déjà le graphique
            if (!vbox.getChildren().contains(graphique.getGraphique())) {
                vbox.getChildren().add(graphique.getGraphique());
            }

        }
    }

    private void mettreAJourEnnemies(String name) {
        // Chercher d'abord dans la liste des ennemis vivants
        Ennemy ennemy = laby.enemies.stream()
                .filter(e -> e.getName().equals(name))
                .findFirst()
                .orElse(null);

        // Si non trouvé, chercher dans les ennemis morts
        if (ennemy == null) {
            ennemy = laby.deadEnemies.stream()
                    .filter(e -> e.getName().equals(name))
                    .findFirst()
                    .orElse(null);
        }

        // Si non trouvé, chercher dans les ennemis arrivés
        if (ennemy == null) {
            ennemy = laby.ennemiesArrived.stream()
                    .filter(e -> e.getName().equals(name))
                    .findFirst()
                    .orElse(null);
        }

        // Mise à jour si l'ennemi est trouvé
        if (ennemy != null) {
            if (ennemiesLabels.containsKey(ennemy)) {
                Label label = ennemiesLabels.get(ennemy);
                label.setText(formatterEnnemy(ennemy));

                // Appliquer le style en fonction de l'état
                String baseStyle = """
                -fx-padding: 3 8;
                -fx-background-color: white;
                -fx-border-color: #e0e0e0;
                -fx-border-radius: 3;
                -fx-font-size: 11px;
            """;

                if (ennemy.getIsDead()) {
                    label.setStyle(baseStyle + "-fx-text-fill: red;");
                } else if (ennemy.getIsArrived()) {
                    label.setStyle(baseStyle + "-fx-text-fill: green;");
                } else {
                    label.setStyle(baseStyle);
                }
            }
        }
        else {
            System.out.println("[logs] L'ennemie est introuvable");
        }
    }


    private String formatterEnnemy(Ennemy ennemy) {
        // Retourne uniquement le texte sans style CSS
        StringBuilder info = new StringBuilder();
        //Le bloc de code si l'on veut afficher la vie aussi quand l'ennemi est mort
        /*info.append(ennemy.getName()).append(" - Vie : ").append(String.format("%.2f", ennemy.getHealth()));

        // Ajouter d'autres informations si nécessaires
        if (ennemy.getIsDead()) {
            info.append(" (Mort)");
        } else if (ennemy.getIsArrived()) {
            info.append(" (Arrivé)");
        }
         */
        info.append(ennemy.getName());
        // Ajouter les informations sur la vie uniquement si l'ennemi n'est pas mort
        if (ennemy.getIsDead()) {
            info.append(" (Mort)");
        } else if (ennemy.getIsArrived()) {
            info.append(" - Vie : ").append(String.format("%.2f", ennemy.getHealth()));
            info.append(" (Arrivé)");
        } else {
            info.append(" - Vie : ").append(String.format("%.2f", ennemy.getHealth()));
        }
        return info.toString();
    }
}
