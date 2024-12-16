module com.example.steering_astar {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.steering_astar to javafx.fxml;

    exports com.example.steering_astar.Steering;
    opens com.example.steering_astar.Steering to javafx.fxml;
}