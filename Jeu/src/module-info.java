module com.example.moteurjeu {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens moteur to javafx.fxml;
    exports moteur;

    exports mains;
    opens mains to javafx.fxml;

}