module ro.ubbcluj.map.mavenfx2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    opens ro.ubbcluj.map.mavenfx2 to javafx.fxml;
    exports ro.ubbcluj.map.mavenfx2;
    exports ro.ubbcluj.map.mavenfx2.controllers;
    exports ro.ubbcluj.map.mavenfx2.domain;
    exports ro.ubbcluj.map.mavenfx2.service;
    exports ro.ubbcluj.map.mavenfx2.authentication;
    opens ro.ubbcluj.map.mavenfx2.controllers to javafx.fxml;
}