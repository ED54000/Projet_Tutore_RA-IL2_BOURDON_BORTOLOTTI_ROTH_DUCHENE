package laby.views;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import laby.ModeleLabyrinth;
import laby.Observer;
import laby.Subject;

import java.util.List;
import java.util.Map;

public class ViewGraphique implements Observer {

    ModeleLabyrinth laby;

    public ViewGraphique(ModeleLabyrinth laby) {
        this.laby = laby;
    }

    @Override
    public void update(Subject s) {
        if (laby.getShowGraph()) {
            afficherGraphique(laby.getDonneesGraphique());
            laby.setGraphique(false);
        }
    }

    public void afficherGraphique(Map<String, List<Double>> courbes) {
        Platform.runLater(() -> {
            Stage fenetre = new Stage();
            fenetre.setTitle("Graphique des données");

            // Axes du graphique
            NumberAxis axeX = new NumberAxis();
            axeX.setTickLabelFormatter(new javafx.scene.chart.NumberAxis.DefaultFormatter(axeX, "%d", null));
            axeX.setLabel("Nombre de manches");

            NumberAxis axeY = new NumberAxis();
            axeY.setLabel("Score");

            // Création du graphique
            LineChart<Number, Number> graphique = new LineChart<>(axeX, axeY);
            graphique.setTitle("Évolution des valeurs");

            // Ajouter chaque courbe
            for (Map.Entry<String, List<Double>> entry : courbes.entrySet()) {
                String nomCourbe = entry.getKey();
                List<Double> valeurs = entry.getValue();

                XYChart.Series<Number, Number> serie = new XYChart.Series<>();
                serie.setName(nomCourbe);

                for (int i = 0; i < valeurs.size(); i++) {
                    serie.getData().add(new XYChart.Data<>(i, valeurs.get(i)));
                }

                graphique.getData().add(serie);
            }

            // Création de la scène et affichage
            Scene scene = new Scene(graphique, 800, 600);
            fenetre.setScene(scene);
            fenetre.show();
        });
    }

}
