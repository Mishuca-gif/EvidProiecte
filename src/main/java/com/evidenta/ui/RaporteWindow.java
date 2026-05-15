package com.evidenta.ui;

import com.evidenta.dao.ProiectDAO;
import com.evidenta.dao.SarcinaDAO;
import com.evidenta.service.ExportService;
import com.evidenta.service.RaportService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.File;
import java.sql.SQLException;

public class RaporteWindow {

    private RaportService raportService = new RaportService();
    private ExportService exportService = new ExportService();
    private ProiectDAO proiectDAO = new ProiectDAO();
    private SarcinaDAO sarcinaDAO = new SarcinaDAO();
    private TextArea txtRaport = new TextArea();

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Rapoarte si Export");
        stage.getIcons().add(new Image("https://cdn-icons-png.flaticon.com/512/2920/2920244.png"));

        Label title = new Label("📊  Rapoarte & Export");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; " +
                "-fx-text-fill: #fbbf24; -fx-font-family: 'Georgia';");

        ComboBox<String> cmbRaport = new ComboBox<>();
        cmbRaport.getItems().addAll(
                "Raport 1 - Sarcini per proiect",
                "Raport 2 - Executanti activi",
                "Raport 3 - Sarcini dupa status");
        cmbRaport.setValue("Raport 1 - Sarcini per proiect");
        cmbRaport.setPrefWidth(280);

        Button btnGenereaza = new Button("📋  Genereaza");
        styleButton(btnGenereaza, "#f59e0b");

        btnGenereaza.setOnAction(e -> {
            String sel = cmbRaport.getValue();
            if (sel.contains("1"))
                txtRaport.setText(raportService.raport1SarciniPerProiect());
            else if (sel.contains("2"))
                txtRaport.setText(raportService.raport2ExecutantiActivi());
            else
                txtRaport.setText(raportService.raport3SarciniDupaStatus());
        });

        txtRaport.setEditable(false);
        txtRaport.setStyle("-fx-font-family: monospace; -fx-font-size: 13px; " +
                "-fx-background-color: #2d2d2d; -fx-text-fill: white; " +
                "-fx-control-inner-background: #2d2d2d;");

        Button btnExportTxt = new Button("💾  Export TXT");
        Button btnExportCsv = new Button("📄  Export CSV");
        styleButton(btnExportTxt, "#fbbf24");
        styleButton(btnExportCsv, "#ea580c");

        btnExportTxt.setOnAction(e -> {
            if (txtRaport.getText().isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Genereaza mai intai un raport!").showAndWait();
                return;
            }
            FileChooser fc = new FileChooser();
            fc.setTitle("Salveaza raport");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text", "*.txt"));
            File file = fc.showSaveDialog(stage);
            if (file != null) {
                try {
                    exportService.exportRaportTxt(txtRaport.getText(), file.getAbsolutePath());
                    new Alert(Alert.AlertType.INFORMATION, "Export reusit!").showAndWait();
                } catch (Exception ex) {
                    new Alert(Alert.AlertType.ERROR, "Eroare: " + ex.getMessage()).showAndWait();
                }
            }
        });

        btnExportCsv.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Salveaza CSV");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
            File file = fc.showSaveDialog(stage);
            if (file != null) {
                try {
                    exportService.exportCsv(proiectDAO.getAll(),
                            "id,titlu,descriere,dataStart,dataEnd,status", file.getAbsolutePath());
                    new Alert(Alert.AlertType.INFORMATION, "Export CSV reusit!").showAndWait();
                } catch (Exception ex) {
                    new Alert(Alert.AlertType.ERROR, "Eroare: " + ex.getMessage()).showAndWait();
                }
            }
        });

        HBox topBar = new HBox(10, cmbRaport, btnGenereaza);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(5, 0, 5, 0));

        HBox exportBar = new HBox(15, btnExportTxt, btnExportCsv);
        exportBar.setAlignment(Pos.CENTER);
        exportBar.setPadding(new Insets(10, 0, 5, 0));

        VBox root = new VBox(10, title, topBar, txtRaport, exportBar);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #1a1a1a;");
        VBox.setVgrow(txtRaport, Priority.ALWAYS);

        Scene scene = new Scene(root, 700, 550);
        stage.setScene(scene);
        stage.setMinWidth(500);
        stage.setMinHeight(400);
        stage.show();
    }

    private void styleButton(Button btn, String color) {
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: #1a1a1a; " +
                "-fx-font-weight: bold; -fx-background-radius: 6; -fx-font-family: 'Georgia';");
        btn.setPrefHeight(38);
        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: derive(" + color + ", -15%); -fx-text-fill: #1a1a1a; " +
                        "-fx-font-weight: bold; -fx-background-radius: 6; -fx-font-family: 'Georgia';"));
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: " + color + "; -fx-text-fill: #1a1a1a; " +
                        "-fx-font-weight: bold; -fx-background-radius: 6; -fx-font-family: 'Georgia';"));
    }
}