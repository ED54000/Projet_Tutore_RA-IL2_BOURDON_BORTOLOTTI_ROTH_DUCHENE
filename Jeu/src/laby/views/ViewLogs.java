package laby.views;

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

public class ViewLogs implements Observer {
    private ModeleLabyrinth laby;
    private VBox logs;
    private ControllerLearn controllerLearn;

    public ViewLogs(ModeleLabyrinth laby, VBox logs) {
        this.laby = laby;
        //this.logs = (VBox)(logs.getChildren().get(1));
        this.logs = logs;
        this.controllerLearn = new ControllerLearn(laby);
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

            // Style spécial pour "Manche X terminée"
            if (laby.getLogs().matches("Manche \\d+ terminée")) {
                label.setStyle(baseStyle + """
                -fx-background-color: #e8f5e9;
                -fx-border-color: #81c784;
                -fx-font-weight: bold;
                -fx-text-fill: #2e7d32;
            """);
            } else {
                label.setStyle(baseStyle);
            }

            label.setMaxWidth(Double.MAX_VALUE);

            ScrollPane scrollPane = (ScrollPane) logs.getChildren().get(1);
            VBox vbox = (VBox) scrollPane.getContent();

            vbox.setStyle("""
            -fx-background-color: #f5f5f5;
            -fx-padding: 5;
            -fx-spacing: 3;
        """);
            vbox.getChildren().add(label);

            scrollPane.setStyle("""
            -fx-background: #f5f5f5;
            -fx-background-color: transparent;
            -fx-padding: 0;
        """);

            if (laby.getPause() && laby.getLogs().matches("Manche \\d+ terminée")) {
                System.out.println("Simulation ? : " + laby.getSimulation());
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
                vbox.getChildren().add(button);
            }
            laby.setLogs("");
        }
    }
}
