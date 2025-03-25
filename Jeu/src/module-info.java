module com.example.moteurjeu {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.unsupported.desktop;

    opens moteur to javafx.fxml;
    exports moteur;

    exports mains;
    opens mains to javafx.fxml;
    exports laby.views;
    opens laby.views to javafx.fxml;

}