package laby.views;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.layout.StackPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import laby.ModeleLabyrinth;
import laby.Observer;
import laby.Subject;

import java.util.*;

public class ViewGraphiqueObjectif implements Observer {
    ModeleLabyrinth laby;
    private int manche = 0;

    private StackPane chartContainer;
    private NumberAxis yAxis;
    private CategoryAxis xAxis;

    private BarChart<String, Number> barChart;
    private LineChart<String, Number> lineChart;

    private XYChart.Series<String, Number> serieEnnemisParManche;
    private XYChart.Series<String, Number> serieObjectif;

    private Map<Integer, Integer> historiqueEnnemisParManche = new HashMap<>();
    private Map<Integer, Integer> historiqueObjectifs = new HashMap<>();

    private int totalEnnemisArrives = 0; // Variable pour cumuler le nombre d'ennemis arrivés

    public ViewGraphiqueObjectif(ModeleLabyrinth laby) {
        this.laby = laby;

        historiqueEnnemisParManche.put(0, 0); // Initialisation de l'historique des ennemis
        historiqueObjectifs.put(0, laby.nbEnnemiesToWin); // Initialisation de l'historique des objectifs

        // Création d'un axe X commun
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        yAxis.setLabel("Nombre d'ennemis");
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(laby.nbEnnemiesToWin + 3);
        yAxis.setTickUnit(1);

        serieEnnemisParManche = new XYChart.Series<>();
        serieEnnemisParManche.setName("Ennemis Arrivés (par manche)");

        serieObjectif = new XYChart.Series<>();
        serieObjectif.setName("Objectif");

        createCharts();
        updateDonneesGraphique();
    }

    private void createCharts() {
        // Bar chart pour les ennemis
        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Évolution du nombre d'ennemis par manche");
        barChart.setAnimated(false);
        barChart.setLegendVisible(true);
        barChart.getData().add(serieEnnemisParManche);
        barChart.setStyle("-fx-background-color: transparent;");

        // Line chart pour l'objectif
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(true);
        lineChart.setLegendVisible(false);
        lineChart.getData().add(serieObjectif);
        lineChart.setStyle("-fx-background-color: transparent;");

        // Style des graphiques
        Platform.runLater(() -> {
            Node lineChartPlotArea = lineChart.lookup(".chart-plot-background");
            if (lineChartPlotArea != null) {
                lineChartPlotArea.setStyle("-fx-background-color: transparent;");
            }

            Node yAxisLines = lineChart.lookup(".chart-horizontal-grid-lines");
            if (yAxisLines != null) {
                yAxisLines.setStyle("-fx-stroke: transparent;");
            }
        });

        // Container pour les graphiques
        chartContainer = new StackPane();
        chartContainer.getChildren().addAll(barChart, lineChart);
    }

    @Override
    public void update(Subject s) {
        int mancheActuelle = laby.getNbManches();
        if (mancheActuelle > manche) {
            totalEnnemisArrives = laby.ennemiesArrived.size();
            historiqueEnnemisParManche.put(mancheActuelle, totalEnnemisArrives);
            historiqueObjectifs.put(mancheActuelle, laby.nbEnnemiesToWin);
            manche = mancheActuelle;
        } else {
            totalEnnemisArrives = laby.ennemiesArrived.size(); // issues
            if (!laby.ennemiesArrived.isEmpty()) {
                historiqueEnnemisParManche.put(manche, totalEnnemisArrives);
                historiqueObjectifs.put(manche, laby.nbEnnemiesToWin);
            }
        }
        updateDonneesGraphique();
    }

    private void updateDonneesGraphique() {
        try {
            // Mise à jour des données pour le bar chart
            ObservableList<XYChart.Data<String, Number>> barChartData = FXCollections.observableArrayList();
            for (Integer key : historiqueEnnemisParManche.keySet()) {
                barChartData.add(new XYChart.Data<>(String.valueOf(key), historiqueEnnemisParManche.get(key)));
            }
            serieEnnemisParManche.getData().setAll(barChartData);

            // Mise à jour des données pour le line chart
            ObservableList<XYChart.Data<String, Number>> lineChartData = FXCollections.observableArrayList();
            for (Integer key : historiqueObjectifs.keySet()) {
                lineChartData.add(new XYChart.Data<>(String.valueOf(key), historiqueObjectifs.get(key)));
            }
            serieObjectif.getData().setAll(lineChartData);
        } catch (Exception e) {
            System.err.println("Erreur dans updateDonneesGraphique: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Node getGraphiqueCombine() {
        return chartContainer;
    }
}
