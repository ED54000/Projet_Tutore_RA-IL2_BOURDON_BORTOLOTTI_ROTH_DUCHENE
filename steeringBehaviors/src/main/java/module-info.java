module com.example.proto_test {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.proto_test to javafx.fxml;
    exports com.example.proto_test;
}