module com.example.chemin_interface {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.example.chemin_interface to javafx.fxml;
    exports com.example.chemin_interface.moteur;
    exports com.example.chemin_interface.steering_astar.Steering;
    exports com.example.chemin_interface.entites.enemies;
}