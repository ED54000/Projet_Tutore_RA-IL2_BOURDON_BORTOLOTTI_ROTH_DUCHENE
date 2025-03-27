package laby.views;

import entites.enemies.Ennemy;
import javafx.application.Platform;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import laby.ModeleLabyrinth;
import laby.Observer;
import laby.Subject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static laby.ModeleLabyrinth.getScreenSize;

public class ViewGraphiqueDirect implements Observer {

    private final ModeleLabyrinth laby;
    private final BarChart<String, Number> graphique;
    private XYChart.Series<String, Number> serie;
    private final CategoryAxis axeX;
    private final NumberAxis axeY;

    // Stocke les références des ennemis affichés
    private final Map<String, XYChart.Data<String, Number>> ennemiesGraphiques;
    // Associe chaque ennemi à une couleur unique
    private final Map<String, String> couleursEnnemis;

    private static final Pattern PATTERN_MANCHE = Pattern.compile("Manche \\d+");
    private static final String[] COULEURS = {
            "#FF5733", "#33FF57", "#3357FF", "#F1C40F", "#8E44AD", "#E74C3C", "#3498DB", "#1ABC9C"
    };
    private int couleurIndex = 0;

    public ViewGraphiqueDirect(ModeleLabyrinth laby) {
        this.laby = laby;

        // Création des axes
        axeX = new CategoryAxis();
        axeX.setLabel("Ennemis");
        axeX.setVisible(false);
        // Masquer les tick marks et personnaliser le label de l'axe X
        axeX.setTickMarkVisible(false);

        axeY = new NumberAxis();
        axeY.setLabel("Points de vie");

        // Création du graphique
        graphique = new BarChart<>(axeX, axeY);
        graphique.setTitle("Vie des Ennemis");
        graphique.setPrefWidth(getScreenSize().width / 6.7);

        // Initialisation des structures de données
        ennemiesGraphiques = new HashMap<>();
        couleursEnnemis = new HashMap<>();
    }

    @Override
    public void update(Subject s) {
        String log = laby.getLogs();
        if (PATTERN_MANCHE.matcher(log).matches()) {
            initialiserNouvelleManche();
        } else {
            mettreAJourGraphique(log);
        }

        ModeleLabyrinth.setLogs("");
    }

    private void initialiserNouvelleManche() {
        Platform.runLater(() -> {
            // Réinitialiser la série et la map des ennemis
            graphique.getData().clear();
            serie = new XYChart.Series<>();
            graphique.getData().add(serie);
            ennemiesGraphiques.clear();

            // Ajouter tous les nouveaux ennemis au graphique avec leur couleur
            for (Ennemy ennemi : laby.enemies) {
                String nomEnnemi = "Ennemi " + ennemi.getName();
                XYChart.Data<String, Number> data = new XYChart.Data<>(nomEnnemi, ennemi.getHealth());

                // Appliquer la couleur immédiatement après l'ajout
                appliquerCouleur(ennemi, data);

                // Ajouter les données à la série sans générer un marqueur de légende
                serie.getData().add(data);
                ennemiesGraphiques.put(nomEnnemi, data);
            }

            // Assurez-vous que la légende est désactivée ici aussi
            graphique.setLegendVisible(false);
        });
    }

    public void mettreAJourGraphique(String name) {
        Ennemy ennemi = laby.enemies.stream()
                .filter(e -> e.getName().equals(name))
                .findFirst()
                .orElseGet(() -> laby.deadEnemies.stream()
                        .filter(e -> e.getName().equals(name))
                        .findFirst()
                        .orElse(null));

        if (ennemi != null) {
            Platform.runLater(() -> {
                String nomEnnemi = "Ennemi " + ennemi.getName();
                double vie = ennemi.getHealth();

                if (ennemiesGraphiques.containsKey(nomEnnemi)) {
                    // Met à jour la vie de l'ennemi
                    ennemiesGraphiques.get(nomEnnemi).setYValue(vie);
                } else {
                    // Ajoute un nouvel ennemi en cas de retard d'affichage
                    XYChart.Data<String, Number> data = new XYChart.Data<>(nomEnnemi, vie);
                    serie.getData().add(data);
                    ennemiesGraphiques.put(nomEnnemi, data);

                    // Récupérer ou attribuer une couleur unique
                    String couleur = couleursEnnemis.computeIfAbsent(nomEnnemi, k -> COULEURS[couleurIndex++ % COULEURS.length]);

                    // Appliquer la couleur à la barre
                    Platform.runLater(() -> data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                        if (newNode != null) {
                            newNode.setStyle("-fx-bar-fill: " + couleur + ";");
                        }
                    }));
                }
            });
        }
    }

    private void appliquerCouleur(Ennemy ennemi, XYChart.Data<String, Number> data) {
        String couleur = switch (ennemi.getClass().getSimpleName()) {
            case "Giant" -> "gray";
            case "Druide" -> "green";
            case "Ninja" -> "blue";
            case "Berserker" -> "red";
            default -> "black";
        };

        // Si le nœud est déjà créé, on applique la couleur immédiatement
        if (data.getNode() != null) {
            data.getNode().setStyle("-fx-bar-fill: " + couleur + ";");
        }

        // Ajouter un listener pour appliquer la couleur dès que le nœud est créé
        data.nodeProperty().addListener((obs, oldNode, newNode) -> {
            if (newNode != null) {
                newNode.setStyle("-fx-bar-fill: " + couleur + ";");
            }
        });
    }



    public BarChart<String, Number> getGraphique() {
        return graphique;
    }
}
