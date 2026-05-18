package com.evidenta.ui;

import com.evidenta.dao.ExecutantDAO;
import com.evidenta.model.Executant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.sql.SQLException;

public class ExecutantiWindow {

    private ExecutantDAO dao = new ExecutantDAO();
    private TableView<Executant> table = new TableView<>();
    private ObservableList<Executant> data = FXCollections.observableArrayList();
    private Label lblCount = new Label();

    @SuppressWarnings("unchecked")
    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Executanti");
        stage.getIcons().add(new Image("https://cdn-icons-png.flaticon.com/512/3135/3135715.png"));

        Label title = new Label("👥  Gestionare Executanti");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; " +
                "-fx-text-fill: #fbbf24; -fx-font-family: 'Georgia';");

        lblCount.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 12px;");

        TableColumn<Executant, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(50);

        TableColumn<Executant, String> colNume = new TableColumn<>("Nume");
        colNume.setCellValueFactory(new PropertyValueFactory<>("nume"));
        colNume.setPrefWidth(130);

        TableColumn<Executant, String> colPrenume = new TableColumn<>("Prenume");
        colPrenume.setCellValueFactory(new PropertyValueFactory<>("prenume"));
        colPrenume.setPrefWidth(130);

        TableColumn<Executant, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEmail.setPrefWidth(200);

        TableColumn<Executant, String> colRol = new TableColumn<>("Rol");
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colRol.setPrefWidth(100);
        colRol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }
                setText(item);
                switch (item) {
                    case "Developer" -> setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold;");
                    case "Tester" -> setStyle("-fx-text-fill: #3498db; -fx-font-weight: bold;");
                    case "Manager" -> setStyle("-fx-text-fill: #fbbf24; -fx-font-weight: bold;");
                    case "Designer" -> setStyle("-fx-text-fill: #9b59b6; -fx-font-weight: bold;");
                    case "DevOps" -> setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    default -> setStyle("-fx-text-fill: #aaaaaa; -fx-font-weight: bold;");
                }
            }
        });

        table.getColumns().addAll(colId, colNume, colPrenume, colEmail, colRol);
        table.setItems(data);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        loadData();

        Button btnAdd = new Button("➕  Adauga");
        Button btnEdit = new Button("🖍  Editeaza");
        Button btnDelete = new Button("🗑  Sterge");

        styleButton(btnAdd, "#fbbf24");
        styleButton(btnEdit, "#f59e0b");
        styleButton(btnDelete, "#ea580c");

        btnAdd.setOnAction(e -> {
            new ExecutantFormDialog(null).showAndWait().ifPresent(ex -> {
                try {
                    dao.add(ex);
                    loadData();
                    showInfo("Executant adaugat!");
                } catch (SQLException ex2) {
                    showError("Eroare: " + ex2.getMessage());
                }
            });
        });

        btnEdit.setOnAction(e -> {
            Executant sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                showError("Selecteaza un executant!");
                return;
            }
            new ExecutantFormDialog(sel).showAndWait().ifPresent(ex -> {
                try {
                    dao.update(ex);
                    loadData();
                    showInfo("Executant actualizat!");
                } catch (SQLException ex2) {
                    showError("Eroare: " + ex2.getMessage());
                }
            });
        });

        btnDelete.setOnAction(e -> {
            Executant sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                showError("Selecteaza un executant!");
                return;
            }
            new Alert(Alert.AlertType.CONFIRMATION,
                    "Stergi executantul: " + sel.toString() + "?",
                    ButtonType.YES, ButtonType.NO).showAndWait().ifPresent(btn -> {
                        if (btn == ButtonType.YES) {
                            try {
                                dao.delete(sel.getId());
                                loadData();
                                showInfo("Executant sters!");
                            } catch (SQLException ex2) {
                                showError("Eroare: " + ex2.getMessage());
                            }
                        }
                    });
        });

        HBox topBar = new HBox(10, lblCount);
        topBar.setAlignment(Pos.CENTER_LEFT);

        HBox buttons = new HBox(15, btnAdd, btnEdit, btnDelete);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(10, 0, 5, 0));

        VBox root = new VBox(10, title, topBar, table, buttons);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #1a1a1a;");
        VBox.setVgrow(table, Priority.ALWAYS);

        Scene scene = new Scene(root, 750, 550);
        stage.setScene(scene);
        stage.setMinWidth(500);
        stage.setMinHeight(400);
        stage.show();
    }

    private void loadData() {
        try {
            data.setAll(dao.getAll());
            lblCount.setText(data.size() + " executanti");
        } catch (SQLException e) {
            showError("Eroare: " + e.getMessage());
        }
    }

    private void styleButton(Button btn, String color) {
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: #1a1a1a; " +
                "-fx-font-weight: bold; -fx-background-radius: 6; -fx-font-family: 'Georgia';");
        btn.setPrefHeight(38);
        btn.setPrefWidth(130);
        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: derive(" + color + ", -30%); -fx-text-fill: #1a1a1a; " +
                        "-fx-font-weight: bold; -fx-background-radius: 6; -fx-font-family: 'Georgia';"));
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: " + color + "; -fx-text-fill: #1a1a1a; " +
                        "-fx-font-weight: bold; -fx-background-radius: 6; -fx-font-family: 'Georgia';"));
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}