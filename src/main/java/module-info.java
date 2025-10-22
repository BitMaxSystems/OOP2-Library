module org.bitmaxsystems.oop2library {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.bitmaxsystems.oop2library to javafx.fxml;
    exports org.bitmaxsystems.oop2library;
}