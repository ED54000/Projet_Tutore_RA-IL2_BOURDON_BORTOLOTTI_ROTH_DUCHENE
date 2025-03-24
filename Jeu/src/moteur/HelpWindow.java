package moteur;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class HelpWindow {
    private Stage helpStage;
    private boolean isOpen = false;
    private static HelpWindow instance = null;

    public static HelpWindow getHelpWindow() {
        if (instance == null) {
            instance = new HelpWindow();
        }
        return instance;
    }

    public void show() {
        if (isOpen) {
            helpStage.close();
            isOpen = false;
            return;
        }

        helpStage = new Stage();

        helpStage.setTitle("Informations sur les ennemis");

        // Création des onglets
        TabPane tabPane = new TabPane();

        Tab tabExplication = new Tab("Explication", createTextTab());
        tabExplication.setClosable(false);


        Tab tabTableau = new Tab("Tableau", createTableTab());
        tabTableau.setClosable(false);


        Tab tabImage = new Tab("Types", createImageTab("Ressources/Explication_types.png"));
        tabImage.setClosable(false);

        tabPane.getTabs().addAll(tabExplication, tabTableau, tabImage);

        Scene scene = new Scene(tabPane, 700, 400);
        helpStage.setScene(scene);
        isOpen = true;
        helpStage.show();
    }

    private VBox createTableTab() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));

        // En-têtes
        Label genreLabel = new Label("Genre");
        Label comportementLabel = new Label("Comportement");
        Label normalLabel = new Label("Sprite en mode \"normal\"");
        Label simpleLabel = new Label("Sprite en mode \"simple\"");

        // Style des en-têtes
        Font headerFont = Font.font("System", FontWeight.BOLD, 14);
        genreLabel.setFont(headerFont);
        comportementLabel.setFont(headerFont);
        normalLabel.setFont(headerFont);
        simpleLabel.setFont(headerFont);

        // Ajouter les en-têtes à la grille
        grid.addRow(0, genreLabel, comportementLabel, normalLabel, simpleLabel);

        // Définition des colonnes
        ColumnConstraints col1 = new ColumnConstraints(100);
        ColumnConstraints col2 = new ColumnConstraints(120);
        ColumnConstraints col3 = new ColumnConstraints(180);
        ColumnConstraints col4 = new ColumnConstraints(180);
        grid.getColumnConstraints().addAll(col1, col2, col3, col4);

        // Données des ennemis
        String[][] data = {
                {"Géant", "Normal", "/giant.png", "/gray.png"},
                {"Ninja", "Fuyard", "/ninja.png", "/blue.png"},
                {"Druide", "Healer", "/druide.png", "/green.png"},
                {"Berserker", "Kamikaze", "/berserker.png", "/red.png"}
        };

        for (int row = 0; row < data.length; row++) {
            Label genreText = new Label(data[row][0]);
            Label comportementText = new Label(data[row][1]);

            grid.addRow(row + 1, genreText, comportementText);

            try {
                ImageView normalView = new ImageView(new Image(getClass().getResourceAsStream(data[row][2])));
                normalView.setFitHeight(50);
                normalView.setFitWidth(50);

                ImageView simpleView = new ImageView(new Image(getClass().getResourceAsStream(data[row][3])));
                simpleView.setFitHeight(50);
                simpleView.setFitWidth(50);

                grid.add(normalView, 2, row + 1);
                grid.add(simpleView, 3, row + 1);
            } catch (Exception e) {
                grid.add(new Label("[Image non trouvée]"), 2, row + 1);
                grid.add(new Label("[Image non trouvée]"), 3, row + 1);
                System.err.println("Erreur de chargement d'image: " + e.getMessage());
            }
        }

        Label titleLabel = new Label("Types d'ennemis et leurs comportements");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        titleLabel.setPadding(new Insets(0, 0, 15, 0));

        VBox vbox = new VBox(10, titleLabel, grid);
        vbox.setPadding(new Insets(20));
        return vbox;
    }

    private VBox createTextTab() {
        Label textLabel = new Label("Ce jeu contient plusieurs types d'ennemis, chacun ayant un comportement unique.\n\n" +
                "- Le Géant est un ennemi classique avec un comportement normal.\n" +
                "- Le Ninja est un fuyard, il tente d'éviter le combat.\n" +
                "- Le Druide agit comme un soigneur, régénérant ses alliés et suivant le groupe le plus nombreux.\n" +
                "- Le Berserker est un kamikaze, fonçant sur la défense la plus proche sans réfléchir.\n\n" +
                "Chacun possède une apparence en mode 'normal' et une en mode 'simple' détaillée dans le tableau suivant.\n"+
                "Chaque ennemi et défense possède un type qui est expliqué dans le dernier onglet.");
        textLabel.setWrapText(true);
        textLabel.setFont(Font.font("System", 14));

        VBox vbox = new VBox(10, textLabel);
        vbox.setPadding(new Insets(20));
        return vbox;
    }

    private VBox createImageTab(String imagePath) {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        try {
            File file = new File(imagePath);
            Image image = new Image(new FileInputStream(file));
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(500);

            vbox.getChildren().add(imageView);
        } catch (FileNotFoundException e) {
            Label errorLabel = new Label("Erreur : image non trouvée !");
            vbox.getChildren().add(errorLabel);
        }

        return vbox;
    }

}
