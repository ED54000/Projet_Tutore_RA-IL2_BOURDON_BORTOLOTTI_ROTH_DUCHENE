module com.example.steeringbehaviors {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.steeringbehaviors to javafx.fxml;
    exports com.example.steeringbehaviors;
}