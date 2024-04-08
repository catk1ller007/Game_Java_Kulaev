module com.example.labajavaupdate {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.labajavaupdate to javafx.fxml;
    exports com.example.labajavaupdate;
}