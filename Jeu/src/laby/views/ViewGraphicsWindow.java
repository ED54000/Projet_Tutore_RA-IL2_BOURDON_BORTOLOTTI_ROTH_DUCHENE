package laby.views;

import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import laby.ModeleLabyrinth;
import laby.Observer;
import laby.Subject;

public class ViewGraphicsWindow implements Observer {
    private Stage stage = new Stage();
    private ViewGraphiqueDirect graphiqueDirect;
    private ViewGraphique graphiqueEvolution;
    private TabPane tabPane = new TabPane();
    private VBox root = new VBox(10);
    private VBox VboxGraphiqueDirect = new VBox(10);
    private VBox VBoxGraphiqueEvolution = new VBox(10);;
    ModeleLabyrinth laby;
    Tab tabDirect, tabEvolution;

    public ViewGraphicsWindow(ModeleLabyrinth laby, ViewGraphiqueDirect graphiqueDirect, ViewGraphique graphiqueEvolution) {
        stage.setTitle("Graphiques");
        this.graphiqueDirect = graphiqueDirect;
        this.graphiqueEvolution = graphiqueEvolution;
        this.laby = laby;

        // Créer le premier onglet pour le graphique direct
        tabDirect = new Tab("Graphique Direct");
        tabDirect.setClosable(false);
        tabDirect.setContent(VboxGraphiqueDirect);

        // Créer le deuxième onglet pour l'évolution
        tabEvolution = new Tab("Évolution");
        tabEvolution.setClosable(false);
        tabEvolution.setContent(VBoxGraphiqueEvolution);

        // Ajouter les onglets au TabPane
        tabPane.getTabs().addAll(tabDirect, tabEvolution);

        root.getChildren().add(tabPane);
        Scene scene = new Scene(root, 1200, 400);
        stage.setScene(scene);
    }

    public void show() {
        stage.show();
    }

    public void hide() {
        stage.hide();
    }

    @Override
    public void update(Subject s) {
        if (!VboxGraphiqueDirect.getChildren().contains(graphiqueDirect.getGraphique())){
            VboxGraphiqueDirect.getChildren().add(graphiqueDirect.getGraphique());
        }
        if (!VBoxGraphiqueEvolution.getChildren().contains(graphiqueEvolution.getGraphique())){
            VBoxGraphiqueEvolution.getChildren().add(graphiqueEvolution.getGraphique());
        }
    }
}