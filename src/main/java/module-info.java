module com.example.final2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;

    opens com.example.final2 to javafx.fxml;
    opens com.example.final2.controller to javafx.fxml;
    opens com.example.final2.model to javafx.base;

    exports com.example.final2;
    exports com.example.final2.controller;
    exports com.example.final2.model;
    exports com.example.final2.service;
    exports com.example.final2.repository;
    exports com.example.final2.config;
}
