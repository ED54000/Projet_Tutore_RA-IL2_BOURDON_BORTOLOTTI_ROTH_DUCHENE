package moteur;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class HelpWindow {
    // Constants
    private static final int WINDOW_WIDTH = 950;
    private static final int WINDOW_HEIGHT = 500;
    private static final int PADDING = 20;
    private static final int SPACING = 15;
    private static final int IMAGE_SIZE = 64;
    private static final int GRID_HGAP = 25;
    private static final int GRID_VGAP = 20;
    private static final int HEADER_FONT_SIZE = 16;
    private static final int TEXT_FONT_SIZE = 14;
    private static final int TITLE_FONT_SIZE = 22;
    private static final int IMAGE_MAX_WIDTH = 600;

    // Colors
    private static final String PRIMARY_COLOR = "#3498db";
    private static final String SECONDARY_COLOR = "#2c3e50";
    private static final String BACKGROUND_COLOR = "#ecf0f1";
    private static final String HEADER_BG_COLOR = "#34495e";
    private static final String HEADER_TEXT_COLOR = "white";

    private Stage helpStage;
    private boolean isOpen = false;
    private static HelpWindow instance = null;

    // Singleton pattern
    public static HelpWindow getHelpWindow() {
        if (instance == null) {
            instance = new HelpWindow();
        }
        return instance;
    }

    public void show() {
        if (isOpen && helpStage != null) {
            helpStage.toFront(); // Ramène la fenêtre au premier plan au lieu de la fermer
            return;
        }

        helpStage = new Stage();
        helpStage.setTitle("Guide des Ennemis");

        // Create tabs
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE); // Désactive la fermeture de tous les onglets

        // Appliquer un style moderne aux onglets
        tabPane.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";" +
                "-fx-tab-min-width: 120px;" +
                "-fx-tab-max-width: 120px;" +
                "-fx-tab-min-height: 40px;");

        Tab tabExplication = new Tab("Vue d'ensemble", createTextTab());
        Tab tabTableau = new Tab("Ennemis", createTableTab());
        Tab tabTableauDefense = new Tab("Défenses", createDefenseTableTab());
        Tab tabImage = new Tab("Types", createImageTab("Ressources/Explication_types.png"));

        tabPane.getTabs().addAll(tabExplication, tabTableau, tabTableauDefense, tabImage);

        // Ajouter un conteneur principal avec bordure
        BorderPane mainPane = new BorderPane();
        mainPane.setCenter(tabPane);
        mainPane.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";" +
                "-fx-border-color: " + PRIMARY_COLOR + ";" +
                "-fx-border-width: 2px;");

        // Ajouter une barre de titre personnalisée
        HBox titleBar = createTitleBar("Guide des Ennemis");
        mainPane.setTop(titleBar);

        Scene scene = new Scene(mainPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        helpStage.setScene(scene);

        // Update isOpen flag when window is closed
        helpStage.setOnCloseRequest((WindowEvent event) -> {
            isOpen = false;
        });

        isOpen = true;
        helpStage.show();
    }

    private HBox createTitleBar(String title) {
        HBox titleBar = new HBox();
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.setPadding(new Insets(15, 20, 15, 20));
        titleBar.setStyle("-fx-background-color: " + HEADER_BG_COLOR + ";");

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button closeButton = new Button("×");
        closeButton.setStyle("-fx-background-color: transparent;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 20px;" +
                "-fx-cursor: hand;");
        closeButton.setOnAction(e -> helpStage.close());

        titleBar.getChildren().addAll(titleLabel, spacer, closeButton);
        return titleBar;
    }

    private VBox createTableTab() {
        // Conteneur principal avec un dégradé de fond
        VBox container = new VBox(SPACING);
        container.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        container.setPadding(new Insets(PADDING));

        // Titre de la section
        Label titleLabel = new Label("Types d'ennemis et leurs comportements");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, TITLE_FONT_SIZE));
        titleLabel.setTextFill(Color.web(SECONDARY_COLOR));
        titleLabel.setPadding(new Insets(0, 0, 15, 0));

        // Description
        Label descriptionLabel = new Label("Chaque ennemi possède des caractéristiques uniques qui influencent son comportement en jeu.");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setFont(Font.font("System", FontWeight.NORMAL, TEXT_FONT_SIZE));

        // Tableau des ennemis avec une bordure et un fond
        GridPane grid = new GridPane();
        grid.setHgap(GRID_HGAP);
        grid.setVgap(GRID_VGAP);
        grid.setPadding(new Insets(PADDING));
        grid.setStyle("-fx-background-color: white;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);" +
                "-fx-background-radius: 5px;");

        // En-têtes avec fond coloré
        String[] headers = {"Genre", "Comportement", "Sprite en mode \"normal\"", "Sprite en mode \"simple\""};
        for (int i = 0; i < headers.length; i++) {
            Label headerLabel = new Label(headers[i]);
            headerLabel.setFont(Font.font("System", FontWeight.BOLD, HEADER_FONT_SIZE));
            headerLabel.setTextFill(Color.web(HEADER_TEXT_COLOR));
            headerLabel.setPadding(new Insets(8, 10, 8, 10));
            headerLabel.setMaxWidth(Double.MAX_VALUE);
            headerLabel.setAlignment(Pos.CENTER);

            // Conteneur pour l'en-tête avec style
            StackPane headerContainer = new StackPane(headerLabel);
            headerContainer.setStyle("-fx-background-color: " + HEADER_BG_COLOR + ";" +
                    "-fx-background-radius: 4px;");

            grid.add(headerContainer, i, 0);
            GridPane.setFillWidth(headerContainer, true);
        }

        // Définition des colonnes
        ColumnConstraints col1 = new ColumnConstraints(120);
        ColumnConstraints col2 = new ColumnConstraints(140);
        ColumnConstraints col3 = new ColumnConstraints(250);
        ColumnConstraints col4 = new ColumnConstraints(250);
        grid.getColumnConstraints().addAll(col1, col2, col3, col4);

        // Données des ennemis avec alternance de couleurs
        String[][] data = {
                {"Géant", "Normal", "/giant.png", "/gray.png"},
                {"Ninja", "Fuyard", "/ninja.png", "/blue.png"},
                {"Druide", "Healer", "/druide.png", "/green.png"},
                {"Berserker", "Kamikaze", "/berserker.png", "/red.png"}
        };

        for (int row = 0; row < data.length; row++) {
            // Style d'alternance pour les lignes
            String rowStyle = (row % 2 == 0) ?
                    "-fx-background-color: #f8f9fa;" :
                    "-fx-background-color: #e9ecef;";

            HBox genreBox = createStyledCell(data[row][0], rowStyle);
            HBox comportementBox = createStyledCell(data[row][1], rowStyle);

            genreBox.setAlignment(Pos.CENTER);
            comportementBox.setAlignment(Pos.CENTER);

            grid.add(genreBox, 0, row + 1);
            grid.add(comportementBox, 1, row + 1);

            try {
                // Cellules pour les images avec fond
                HBox normalBox = new HBox();
                normalBox.setAlignment(Pos.CENTER);
                normalBox.setStyle(rowStyle);
                normalBox.setPadding(new Insets(5));

                HBox simpleBox = new HBox();
                simpleBox.setAlignment(Pos.CENTER);
                simpleBox.setStyle(rowStyle);
                simpleBox.setPadding(new Insets(5));

                // Chargement des images avec effet
                ImageView normalView = loadImageFromResource(data[row][2]);
                normalView.setEffect(new javafx.scene.effect.DropShadow(10, Color.rgb(0, 0, 0, 0.2)));

                ImageView simpleView = loadImageFromResource(data[row][3]);
                simpleView.setEffect(new javafx.scene.effect.DropShadow(10, Color.rgb(0, 0, 0, 0.2)));

                normalBox.getChildren().add(normalView);
                simpleBox.getChildren().add(simpleView);

                grid.add(normalBox, 2, row + 1);
                grid.add(simpleBox, 3, row + 1);
            } catch (Exception e) {
                grid.add(createStyledCell("[Image non trouvée]", rowStyle), 2, row + 1);
                grid.add(createStyledCell("[Image non trouvée]", rowStyle), 3, row + 1);
                System.err.println("Erreur de chargement d'image: " + e.getMessage());
            }
        }

        // Ajouter tout au conteneur principal
        container.getChildren().addAll(titleLabel, descriptionLabel, grid);

        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        VBox wrapperBox = new VBox(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return wrapperBox;
    }

    private VBox createDefenseTableTab() {
        // Conteneur principal avec un dégradé de fond
        VBox container = new VBox(SPACING);
        container.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        container.setPadding(new Insets(PADDING));

        // Titre de la section
        Label titleLabel = new Label("Types de défenses");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, TITLE_FONT_SIZE));
        titleLabel.setTextFill(Color.web(SECONDARY_COLOR));
        titleLabel.setPadding(new Insets(0, 0, 15, 0));

        // Tableau des ennemis avec une bordure et un fond
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(GRID_HGAP);
        grid.setVgap(GRID_VGAP);
        grid.setPadding(new Insets(PADDING));
        grid.setStyle("-fx-background-color: white;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);" +
                "-fx-background-radius: 5px;");

        // En-têtes avec fond coloré
        String[] headers = {"Genre", "Sprite en mode \"normal\"", "Sprite en mode \"simple\""};
        for (int i = 0; i < headers.length; i++) {
            Label headerLabel = new Label(headers[i]);
            headerLabel.setFont(Font.font("System", FontWeight.BOLD, HEADER_FONT_SIZE));
            headerLabel.setTextFill(Color.web(HEADER_TEXT_COLOR));
            headerLabel.setPadding(new Insets(8, 10, 8, 10));
            headerLabel.setMaxWidth(Double.MAX_VALUE);
            headerLabel.setAlignment(Pos.CENTER);

            // Conteneur pour l'en-tête avec style
            StackPane headerContainer = new StackPane(headerLabel);
            headerContainer.setStyle("-fx-background-color: " + HEADER_BG_COLOR + ";" +
                    "-fx-background-radius: 4px;");

            grid.add(headerContainer, i, 0);
            GridPane.setFillWidth(headerContainer, true);
        }

        // Définition des colonnes
        ColumnConstraints col1 = new ColumnConstraints(120);
        ColumnConstraints col2 = new ColumnConstraints(250);
        ColumnConstraints col3 = new ColumnConstraints(250);
        grid.getColumnConstraints().addAll(col1, col2, col3);

        // Données des ennemis avec alternance de couleurs
        String[][] data = {
                {"Archer", "/tower1.png", "/cross_purple.png"},
                {"Canon", "/canon1.png", "/cross_yellow.png"}
        };

        for (int row = 0; row < data.length; row++) {
            // Style d'alternance pour les lignes
            String rowStyle = (row % 2 == 0) ?
                    "-fx-background-color: #f8f9fa;" :
                    "-fx-background-color: #e9ecef;";

            HBox genreBox = createStyledCell(data[row][0], rowStyle);

            genreBox.setAlignment(Pos.CENTER);

            grid.add(genreBox, 0, row + 1);

            try {
                // Cellules pour les images avec fond
                HBox normalBox = new HBox();
                normalBox.setAlignment(Pos.CENTER);
                normalBox.setStyle(rowStyle);
                normalBox.setPadding(new Insets(5));

                HBox simpleBox = new HBox();
                simpleBox.setAlignment(Pos.CENTER);
                simpleBox.setStyle(rowStyle);
                simpleBox.setPadding(new Insets(5));

                // Chargement des images avec effet
                ImageView normalView = loadImageFromResource(data[row][1]);
                normalView.setEffect(new javafx.scene.effect.DropShadow(10, Color.rgb(0, 0, 0, 0.2)));

                ImageView simpleView = loadImageFromResource(data[row][2]);
                simpleView.setEffect(new javafx.scene.effect.DropShadow(10, Color.rgb(0, 0, 0, 0.2)));

                normalBox.getChildren().add(normalView);
                simpleBox.getChildren().add(simpleView);

                grid.add(normalBox, 1, row + 1);
                grid.add(simpleBox, 2, row + 1);
            } catch (Exception e) {
                grid.add(createStyledCell("[Image non trouvée]", rowStyle), 1, row + 1);
                grid.add(createStyledCell("[Image non trouvée]", rowStyle), 2, row + 1);
                System.err.println("Erreur de chargement d'image: " + e.getMessage());
            }
        }

        // Ajouter tout au conteneur principal
        container.getChildren().addAll(titleLabel, grid);

        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        VBox wrapperBox = new VBox(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return wrapperBox;
    }

    private HBox createStyledCell(String text, String style) {
        Label label = new Label(text);
        label.setFont(Font.font("System", TEXT_FONT_SIZE));

        HBox box = new HBox(label);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setStyle(style);
        box.setPadding(new Insets(10));

        return box;
    }

    private ImageView loadImageFromResource(String resourcePath) {
        Image image = new Image(getClass().getResourceAsStream(resourcePath));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(IMAGE_SIZE);
        imageView.setFitWidth(IMAGE_SIZE);
        return imageView;
    }

    private VBox createTextTab() {
        // Conteneur principal
        VBox container = new VBox(SPACING * 1.5);
        container.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        container.setPadding(new Insets(PADDING));

        // Titre principal
        Label titleLabel = new Label("Guide des ennemis");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, TITLE_FONT_SIZE));
        titleLabel.setTextFill(Color.web(SECONDARY_COLOR));

        // Introduction
        Label introLabel = new Label(
                "Ce guide vous présente les différents types d'ennemis que vous rencontrerez dans le jeu, " +
                        "ainsi que leurs comportements spécifiques.");
        introLabel.setWrapText(true);
        introLabel.setFont(Font.font("System", FontWeight.NORMAL, TEXT_FONT_SIZE));

        // Sous-titre
        Label subTitleLabel = new Label("Types d'ennemis");
        subTitleLabel.setFont(Font.font("System", FontWeight.BOLD, HEADER_FONT_SIZE));
        subTitleLabel.setTextFill(Color.web(PRIMARY_COLOR));
        subTitleLabel.setPadding(new Insets(10, 0, 5, 0));

        // Liste des ennemis avec panneaux stylisés
        VBox enemiesContainer = new VBox(15);

        // Les quatre types d'ennemis dans des panneaux
        String[][] enemyInfo = {
                {"Géant", "Comportement normal", "Le Géant est un ennemi classique avec un comportement standard. Il avance directement vers votre défense."},
                {"Ninja", "Comportement fuyard", "Le Ninja tente d'éviter le combat direct. Il cherchera toujours les chemins les moins protégés."},
                {"Druide", "Comportement de guérisseur", "Le Druide agit comme un soigneur, régénérant ses alliés à proximité et suivant toujours le groupe le plus nombreux."},
                {"Berserker", "Comportement kamikaze", "Le Berserker fonce sans réfléchir sur la défense la plus proche, sans considération pour sa propre survie."}
        };

        // Couleurs pour chaque ennemi
        String[] colors = {"#6b6e70", "#251fdb", "#2ecc71", "#e74c3c"};

        for (int i = 0; i < enemyInfo.length; i++) {
            VBox enemyPanel = createEnemyPanel(enemyInfo[i][0], enemyInfo[i][1], enemyInfo[i][2], colors[i]);
            enemiesContainer.getChildren().add(enemyPanel);
        }

        // Note finale
        Label noteLabel = new Label(
                "Chaque ennemi possède une apparence en mode 'normal' et une en mode 'simple' détaillée dans l'onglet 'Ennemis'. " +
                        "Les types et leurs interactions sont expliqués dans l'onglet 'Types'.");
        noteLabel.setWrapText(true);
        noteLabel.setFont(Font.font("System", FontWeight.NORMAL, TEXT_FONT_SIZE));
        noteLabel.setPadding(new Insets(10, 0, 0, 0));
        noteLabel.setStyle("-fx-font-style: italic;");

        // Ajouter tous les éléments au conteneur
        container.getChildren().addAll(titleLabel, introLabel, subTitleLabel, enemiesContainer, noteLabel);

        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        VBox wrapperBox = new VBox(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return wrapperBox;
    }

    private VBox createEnemyPanel(String name, String type, String description, String color) {
        // Titre de l'ennemi
        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        nameLabel.setTextFill(Color.WHITE);

        // Type d'ennemi
        Label typeLabel = new Label(type);
        typeLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        typeLabel.setTextFill(Color.WHITE);
        typeLabel.setStyle("-fx-font-style: italic;");

        // En-tête avec nom et type
        VBox header = new VBox(2, nameLabel, typeLabel);
        header.setPadding(new Insets(10, 15, 10, 15));
        header.setStyle("-fx-background-color: " + color + ";" +
                "-fx-background-radius: 5 5 0 0;");

        // Description
        Label descLabel = new Label(description);
        descLabel.setWrapText(true);
        descLabel.setFont(Font.font("System", FontWeight.NORMAL, TEXT_FONT_SIZE));

        VBox descBox = new VBox(descLabel);
        descBox.setPadding(new Insets(15));
        descBox.setStyle("-fx-background-color: white;" +
                "-fx-background-radius: 0 0 5 5;");

        // Assemblage du panneau
        VBox panel = new VBox(header, descBox);
        panel.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 3);" +
                "-fx-background-radius: 5;");

        return panel;
    }

    private VBox createImageTab(String imagePath) {
        // Conteneur principal
        VBox container = new VBox(SPACING);
        container.setAlignment(Pos.CENTER);
        container.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        container.setPadding(new Insets(PADDING));

        // Titre
        Label titleLabel = new Label("Système de types et avantages");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, TITLE_FONT_SIZE));
        titleLabel.setTextFill(Color.web(SECONDARY_COLOR));

        // Description
        Label descriptionLabel = new Label(
                "Ce diagramme explique les relations entre les différents types d'ennemis et de défenses. " +
                        "Certains types sont plus efficaces contre d'autres, créant un système de forces et faiblesses.");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setFont(Font.font("System", FontWeight.NORMAL, TEXT_FONT_SIZE));
        descriptionLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        descriptionLabel.setMaxWidth(600);

        // Panneau pour l'image
        VBox imagePanel = new VBox();
        imagePanel.setAlignment(Pos.CENTER);
        imagePanel.setPadding(new Insets(PADDING));
        imagePanel.setStyle("-fx-background-color: white;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);" +
                "-fx-background-radius: 5px;");

        try {
            File file = new File(imagePath);
            if (!file.exists()) {
                throw new FileNotFoundException("Le fichier n'existe pas: " + imagePath);
            }

            Image image = new Image(new FileInputStream(file));
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(IMAGE_MAX_WIDTH);
            // Ajouter un effet léger à l'image
            imageView.setEffect(new javafx.scene.effect.DropShadow(3, Color.rgb(0, 0, 0, 0.3)));

            imagePanel.getChildren().add(imageView);
        } catch (FileNotFoundException e) {
            Label errorLabel = new Label("Erreur : image non trouvée ! (" + imagePath + ")");
            errorLabel.setStyle("-fx-text-fill: #e74c3c;" +
                    "-fx-font-weight: bold;");
            imagePanel.getChildren().add(errorLabel);
            System.err.println("Erreur de chargement d'image: " + e.getMessage());
        }

        // Légende
        Label legendLabel = new Label(
                "Utilisez ces informations pour optimiser votre stratégie de défense contre les différents types d'ennemis.");
        legendLabel.setWrapText(true);
        legendLabel.setFont(Font.font("System", FontWeight.NORMAL, TEXT_FONT_SIZE - 1));
        legendLabel.setStyle("-fx-font-style: italic;");
        legendLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        legendLabel.setMaxWidth(600);

        // Ajouter tous les éléments au conteneur
        container.getChildren().addAll(titleLabel, descriptionLabel, imagePanel, legendLabel);

        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        VBox wrapperBox = new VBox(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return wrapperBox;
    }
}