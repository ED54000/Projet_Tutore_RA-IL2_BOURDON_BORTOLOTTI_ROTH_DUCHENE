module com.example.moteurjeu {
    requires javafx.controls;
    requires javafx.fxml;


    opens moteur to javafx.fxml;
    exports moteur;
}