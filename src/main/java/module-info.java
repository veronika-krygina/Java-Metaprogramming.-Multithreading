module com.example.lab6 {
    exports com.example.lab6.task1;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.scripting;
    requires org.graalvm.sdk;

    opens com.example.lab6 to javafx.fxml;
    exports com.example.lab6;
}
