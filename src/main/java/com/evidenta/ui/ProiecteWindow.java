package com.evidenta.ui;

import com.evidenta.dao.ProiectDAO;
import com.evidenta.model.Proiect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.sql.SQLException;

public class ProiecteWindow {

    private ProiectDAO dao = new ProiectDAO();
    private TableView<Proiect> table = new TableView<>();
    private ObservableList<Proiect> data = FXCollections.observableArrayList();

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Proiecte");
        stage.getIcons().add(new Image("https://cdn-icons-png.flaticon.com/512/716/716830.png"));

        // Titlu fereastra
        Label title = new Label("📁  Gestionare Proiecte");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; " +
                "-fx-text-fill: #fbbf24; -fx-font-family: 'Georgia';");

        // Contor
        Label lblCount = new Label();
        lblCount.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 12px;");

        // Tabel
        table.setStyle("-fx-background-color: #2d2d2d; " +
                "-fx-text-fill: white; " +
                "-fx-border-color: #fbbf24; " +
                "-fx-border-width: 1;");

        TableColumn<Proiect, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(50);

        TableColumn<Proiect, String> colTitlu = new TableColumn<>("Titlu");
        colTitlu.setCellValueFactory(new PropertyValueFactory<>("titlu"));
        colTitlu.setPrefWidth(200);

        TableColumn<Proiect, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStatus.setPrefWidth(100);
        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }
                setText(item);
                if (item.equals("ACTIV"))
                    setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold;");
                else if (item.equals("FINALIZAT"))
                    setStyle("-fx-text-fill: #3498db; -fx-font-weight: bold;");
                else
                    setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
            }
        });

        TableColumn<Proiect, String> colStart = new TableColumn<>("Data Start");
        colStart.setCellValueFactory(new PropertyValueFactory<>("dataStart"));
        colStart.setPrefWidth(110);

        TableColumn<Proiect, String> colEnd = new TableColumn<>("Data End");
        colEnd.setCellValueFactory(new PropertyValueFactory<>("dataEnd"));
        colEnd.setPrefWidth(110);

        TableColumn<Proiect, String> colDescriere = new TableColumn<>("Descriere");
        colDescriere.setCellValueFactory(new PropertyValueFactory<>("descriere"));
        colDescriere.setPrefWidth(200);

        table.getColumns().addAll(colId, colTitlu, colStatus, colStart, colEnd, colDescriere);
        table.setItems(data);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        loadData(lblCount);

        // Cautare
        TextField search = new TextField();
        search.setPromptText("Cauta dupa titlu sau status...");
        search.setPrefWidth(300);
        search.setStyle("-fx-background-color: white; -fx-text-fill: black; " +
                "-fx-prompt-text-fill: #aaaaaa; -fx-border-color: #fbbf24; " +
                "-fx-border-radius: 4; -fx-background-radius: 4;");
        search.textProperty().addListener((obs, old, val) -> {
            try {
                data.setAll(dao.search(val));
                lblCount.setText(data.size() + " proiecte gasite");
            } catch (SQLException e) {
                showError("Eroare cautare: " + e.getMessage());
            }
        });

        Label lblCautare = new Label("Cautare:");
        lblCautare.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        // Butoane
        Button btnAdd = new Button("➕  Adauga");
        Button btnEdit = new Button("🖍  Editeaza");
        Button btnDelete = new Button("🗑  Sterge");

        styleButton(btnAdd, "#fbbf24");
        styleButton(btnEdit, "#f59e0b");
        styleButton(btnDelete, "#ea580c");

        btnAdd.setOnAction(e -> {
            new ProiectFormDialog(null).showAndWait().ifPresent(p -> {
                try {
                    dao.add(p);
                    loadData(lblCount);
                    showInfo("Proiect adaugat cu succes!");
                } catch (SQLException ex) {
                    showError("Eroare: " + ex.getMessage());
                }
            });
        });

        btnEdit.setOnAction(e -> {
            Proiect sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                showError("Selecteaza un proiect!");
                return;
            }
            new ProiectFormDialog(sel).showAndWait().ifPresent(p -> {
                try {
                    dao.update(p);
                    loadData(lblCount);
                    showInfo("Proiect actualizat!");
                } catch (SQLException ex) {
                    showError("Eroare: " + ex.getMessage());
                }
            });
        });

        btnDelete.setOnAction(e -> {
            Proiect sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                showError("Selecteaza un proiect!");
                return;
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Stergi proiectul: " + sel.getTitlu() + "?",
                    ButtonType.YES, ButtonType.NO);
            confirm.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.YES) {
                    try {
                        dao.delete(sel.getId());
                        loadData(lblCount);
                        showInfo("Proiect sters!");
                    } catch (SQLException ex) {
                        showError("Eroare: " + ex.getMessage());
                    }
                }
            });
        });

        HBox searchBar = new HBox(10, lblCautare, search, lblCount);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        searchBar.setPadding(new Insets(5, 0, 5, 0));

        HBox buttons = new HBox(15, btnAdd, btnEdit, btnDelete);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(10, 0, 5, 0));

        VBox root = new VBox(10, title, searchBar, table, buttons);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #1a1a1a;");
        VBox.setVgrow(table, Priority.ALWAYS);

        Scene scene = new Scene(root, 800, 550);
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.show();
    }

    private void loadData(Label lblCount) {
        try {
            data.setAll(dao.getAll());
            lblCount.setText(data.size() + " proiecte");
        } catch (SQLException e) {
            showError("Eroare incarcare: " + e.getMessage());
        }
    }

    private void styleButton(Button btn, String color) {
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: #1a1a1a; " +
                "-fx-font-weight: bold; -fx-background-radius: 6; " +
                "-fx-font-family: 'Georgia';");
        btn.setPrefHeight(38);
        btn.setPrefWidth(130);
        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: derive(" + color + ", -15%); -fx-text-fill: #1a1a1a; " +
                        "-fx-font-weight: bold; -fx-background-radius: 6; " +
                        "-fx-font-family: 'Georgia';"));
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: " + color + "; -fx-text-fill: #1a1a1a; " +
                        "-fx-font-weight: bold; -fx-background-radius: 6; " +
                        "-fx-font-family: 'Georgia';"));
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}