package laby.views;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.layout.StackPane;
import laby.ModeleLabyrinth;
import laby.Observer;
import laby.Subject;

import java.util.*;

public class ViewGraphiqueObjectif implements Observer {
    private final ModeleLabyrinth laby;
    private int manche = 0;

    private final StackPane chartContainer;
    private final NumberAxis yAxis;
    private final NumberAxis xAxis;

    private final LineChart<Number, Number> lineChart;

    private final XYChart.Series<Number, Number> serieEnnemisParManche;
    private final XYChart.Series<Number, Number> serieObjectif;

    private final Map<Integer, Integer> historiqueEnnemisParManche = new HashMap<>();
    private final Map<Integer, Integer> historiqueObjectifs = new HashMap<>();

    private int totalEnnemisArrives = 0;

    public ViewGraphiqueObjectif(ModeleLabyrinth laby) {
        this.laby = laby;

        // Initialiser les axes
        xAxis = new NumberAxis(0, 1, 1);
        xAxis.setLabel("Nombre de manches");
        xAxis.setForceZeroInRange(true);
        xAxis.setAutoRanging(false);
        xAxis.setTickUnit(1);

        yAxis = new NumberAxis(0, laby.nbEnnemiesToWin + 3, 1);
        yAxis.setLabel("Nombre d'ennemis");
        yAxis.setAutoRanging(false);

        // Initialiser les séries
        serieEnnemisParManche = new XYChart.Series<>();
        serieEnnemisParManche.setName("Ennemis Arrivés");

        serieObjectif = new XYChart.Series<>();
        serieObjectif.setName("Objectif");

        // Initialiser le graphique
        lineChart = createLineChart();
        lineChart.getData().addAll(serieEnnemisParManche, serieObjectif);

        // Créer le conteneur pour le graphique
        chartContainer = new StackPane();
        StackPane.setAlignment(lineChart, Pos.CENTER);
        chartContainer.getChildren().add(lineChart);

        // Appliquer les couleurs personnalisées
        applyCustomColors();

        // Activer la légende
        lineChart.setLegendVisible(true);

        // Initialiser avec les données de départ
        updateDonneesGraphique();
    }

    private LineChart<Number, Number> createLineChart() {
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setAnimated(false);
        chart.setCreateSymbols(true);
        chart.setStyle("-fx-background-color: transparent;");
        chart.setHorizontalGridLinesVisible(false);

        Platform.runLater(() -> {
            Node plotArea = chart.lookup(".chart-plot-background");
            if (plotArea != null) {
                plotArea.setStyle("-fx-background-color: transparent;");
            }
        });

        return chart;
    }

    private void applyCustomColors() {
        Platform.runLater(() -> {
            if (serieEnnemisParManche.getNode() != null) {
                serieEnnemisParManche.getNode().setStyle("-fx-stroke: #e74c3c; -fx-stroke-width: 3px;");
            }
            for (XYChart.Data<Number, Number> data : serieEnnemisParManche.getData()) {
                if (data.getNode() != null) {
                    data.getNode().setStyle("-fx-background-color: #e74c3c, white; -fx-background-radius: 5px; -fx-padding: 5px;");
                }
            }

            // Configurer la série de l'objectif (vert)
            if (serieObjectif.getNode() != null) {
                serieObjectif.getNode().setStyle("-fx-stroke: #2ecc71; -fx-stroke-width: 3px; -fx-stroke-dash-array: 5 5;");
            }
            for (XYChart.Data<Number, Number> data : serieObjectif.getData()) {
                if (data.getNode() != null) {
                    data.getNode().setStyle("-fx-background-color: #2ecc71, white; -fx-background-radius: 5px; -fx-padding: 5px;");
                }
            }
            Node legend = lineChart.lookup(".chart-legend");
            if (legend != null) {
                for (Node legendItem : legend.lookupAll(".chart-legend-item")) {
                    if (legendItem.toString().contains("Objectif")) {
                        Node symbol = legendItem.lookup(".chart-legend-item-symbol");
                        if (symbol != null) {
                            symbol.setStyle("-fx-background-color: #2ecc71, white;");
                        }
                    } else if (legendItem.toString().contains("Ennemis Arrivés")) {
                        Node symbol = legendItem.lookup(".chart-legend-item-symbol");
                        if (symbol != null) {
                            symbol.setStyle("-fx-background-color: #e74c3c, white;");
                        }
                    }
                }
            }
        });
    }


    private void updateXAxisBounds() {
        Platform.runLater(() -> {
            xAxis.setUpperBound(Math.max(1, manche));
        });
    }

    @Override
    public void update(Subject s) {
        int mancheActuelle = laby.getNbManches() - 1;
        if (mancheActuelle > manche) {
            totalEnnemisArrives = laby.ennemiesArrived.size();
            historiqueEnnemisParManche.put(mancheActuelle, totalEnnemisArrives);
            historiqueObjectifs.put(mancheActuelle, laby.nbEnnemiesToWin);
            manche = mancheActuelle;
            updateXAxisBounds();
        } else {
            totalEnnemisArrives = laby.ennemiesArrived.size();
            if (!laby.ennemiesArrived.isEmpty()) {
                historiqueEnnemisParManche.put(manche, totalEnnemisArrives);
                historiqueObjectifs.put(manche, laby.nbEnnemiesToWin);
            }
        }
        updateDonneesGraphique();
    }

    private void updateDonneesGraphique() {
        try {
            Platform.runLater(() -> {
                // Effacer les données existantes
                serieEnnemisParManche.getData().clear();
                serieObjectif.getData().clear();

                // Ajouter les nouvelles données
                for (Map.Entry<Integer, Integer> entry : historiqueEnnemisParManche.entrySet()) {
                    serieEnnemisParManche.getData().add(
                            new XYChart.Data<>(entry.getKey(), entry.getValue())
                    );
                }

                for (Map.Entry<Integer, Integer> entry : historiqueObjectifs.entrySet()) {
                    serieObjectif.getData().add(
                            new XYChart.Data<>(entry.getKey(), entry.getValue())
                    );
                }

                // Appliquer les couleurs personnalisées aux nouveaux points
                applyCustomColors();
            });
        } catch (Exception e) {
            System.err.println("Erreur dans updateDonneesGraphique: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Node getGraphiqueCombine() {
        return chartContainer;
    }
}
