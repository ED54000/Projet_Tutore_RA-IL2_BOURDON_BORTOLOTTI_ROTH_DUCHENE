package laby.views;

import entites.enemies.Ennemy;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import laby.ModeleLabyrinth;
import laby.Observer;
import laby.Subject;

import java.util.*;
import java.util.stream.Collectors;

public class ViewGraphique implements Observer {
    ModeleLabyrinth laby;
    private Map<String, XYChart.Series<Number, Number>> series;
    private Map<String, List<Double>> donnees;
    private int manche = 0;
    private static boolean mancheTerminee = true;
    private LineChart<Number, Number> graphique;
    private NumberAxis axeX;

    private static final List<String> TYPES_ENNEMIS = Arrays.asList("Giant", "Ninja", "Berserker", "Druide");

    public ViewGraphique(ModeleLabyrinth laby) {
        this.laby = laby;
        this.donnees = new HashMap<>();
        this.series = new HashMap<>();

        // Initialiser les données et créer le graphique initial
        TYPES_ENNEMIS.forEach(type -> {
            donnees.put(type, new ArrayList<>());
            XYChart.Series<Number, Number> serie = new XYChart.Series<>();
            serie.setName(type);
            series.put(type, serie);
        });

        // Créer le graphique une seule fois
        graphique = createEmptyGraph();

        // Ajouter toutes les séries au graphique
        series.values().forEach(serie -> graphique.getData().add(serie));
    }

    @Override
    public void update(Subject s) {
        if (laby.getNbManches() > manche) {
            updateDonneesGraphique();
            manche = laby.getNbManches();
            updateXAxisBounds(); // Mettre à jour les bornes de l'axe X
        }
    }

    private void updateXAxisBounds() {
        Platform.runLater(() -> {
            axeX.setUpperBound(manche-1); // Mettre à jour la borne supérieure
        });
    }

    private void updateDonneesGraphique() {
        try {
            // Grouper les ennemis par type
            Map<Class<? extends Ennemy>, List<Ennemy>> ennemisParType = laby.enemies.stream()
                    .collect(Collectors.groupingBy(Ennemy::getClass));

            // Mettre à jour les données pour chaque type d'ennemi
            Platform.runLater(() -> {
                for (String type : TYPES_ENNEMIS) {
                    // Compter les ennemis de ce type
                    long count = ennemisParType.entrySet().stream()
                            .filter(e -> e.getKey().getSimpleName().equals(type))
                            .map(e -> e.getValue().size())
                            .findFirst()
                            .orElse(0);

                    // Mettre à jour les données
                    donnees.get(type).add((double) count);

                    // Mettre à jour la série correspondante
                    XYChart.Series<Number, Number> serie = series.get(type);
                    serie.getData().add(new XYChart.Data<>(manche-1, count));
                }
            });

        } catch (Exception e) {
            System.err.println("Erreur dans updateDonneesGraphique: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private LineChart<Number, Number> createEmptyGraph() {
        // Configurer l'axe X
        axeX = new NumberAxis(0, 1, 1);
        axeX.setLabel("Nombre de manches");
        axeX.setForceZeroInRange(false);
        axeX.setAutoRanging(false);
        axeX.setTickUnit(1); // Utiliser des entiers pour le nombre de manches

        // Configurer l'axe Y
        NumberAxis axeY = new NumberAxis();
        axeY.setLabel("Nombre d'ennemis");
        axeY.setTickUnit(1); // Utiliser des entiers pour le nombre d'ennemis
        axeY.setMinorTickCount(0);

        LineChart<Number, Number> graph = new LineChart<>(axeX, axeY);
        graph.setTitle("Évolution du nombre d'ennemis par type");
        graph.setAnimated(false); // Désactiver l'animation pour de meilleures performances

        return graph;
    }

    public LineChart<Number, Number> getGraphique() {
        return graphique;
    }

    public static void setMancheTerminee(boolean termine) {
        mancheTerminee = termine;
    }
}