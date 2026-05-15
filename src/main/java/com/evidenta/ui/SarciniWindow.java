package com.evidenta.ui;

import com.evidenta.dao.ExecutantDAO;
import com.evidenta.dao.ProiectDAO;
import com.evidenta.dao.SarcinaDAO;
import com.evidenta.model.Sarcina;
import com.evidenta.model.StatusSarcina;
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

public class SarciniWindow {

    private SarcinaDAO dao = new SarcinaDAO();
    private ProiectDAO proiectDAO = new ProiectDAO();
    private ExecutantDAO execDAO = new ExecutantDAO();
    private TableView<Sarcina> table = new TableView<>();
    private ObservableList<Sarcina> data = FXCollections.observableArrayList();
    private Label lblCount = new Label();

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Sarcini");
        stage.getIcons().add(new Image("https://cdn-icons-png.flaticon.com/512/3176/3176394.png"));

        Label title = new Label("✅  Gestionare Sarcini");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; " +
                "-fx-text-fill: #fbbf24; -fx-font-family: 'Georgia';");

        lblCount.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 12px;");

        // Coloane tabel
        TableColumn<Sarcina, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(40);

        TableColumn<Sarcina, String> colTitlu = new TableColumn<>("Titlu");
        colTitlu.setCellValueFactory(new PropertyValueFactory<>("titlu"));
        colTitlu.setPrefWidth(150);

        TableColumn<Sarcina, StatusSarcina> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStatus.setPrefWidth(100);
        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(StatusSarcina item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }
                setText(item.toString());
                switch (item.toString()) {
                    case "FINALIZATA" -> setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold;");
                    case "IN_PROGRES" -> setStyle("-fx-text-fill: #3498db; -fx-font-weight: bold;");
                    case "NOUA" -> setStyle("-fx-text-fill: #fbbf24; -fx-font-weight: bold;");
                    case "ANULATA" -> setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                }
            }
        });

        TableColumn<Sarcina, Integer> colPrioritate = new TableColumn<>("Prioritate");
        colPrioritate.setCellValueFactory(new PropertyValueFactory<>("prioritate"));
        colPrioritate.setPrefWidth(80);

        TableColumn<Sarcina, String> colProiect = new TableColumn<>("Proiect");
        colProiect.setCellValueFactory(new PropertyValueFactory<>("numeProiect"));
        colProiect.setPrefWidth(150);

        TableColumn<Sarcina, String> colExec = new TableColumn<>("Executant");
        colExec.setCellValueFactory(new PropertyValueFactory<>("numeExecutant"));
        colExec.setPrefWidth(150);

        table.getColumns().addAll(colId, colTitlu, colStatus, colPrioritate, colProiect, colExec);
        table.setItems(data);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        loadData();

        // Cautare
        TextField txtSearch = new TextField();
        txtSearch.setPromptText("Cauta dupa titlu...");
        txtSearch.setPrefWidth(200);
        txtSearch.setStyle("-fx-background-color: white; -fx-text-fill: black; " +
                "-fx-prompt-text-fill: #aaaaaa; -fx-border-color: #fbbf24; " +
                "-fx-border-radius: 4; -fx-background-radius: 4;");

        ComboBox<String> cmbFilter = new ComboBox<>();
        cmbFilter.getItems().addAll("Toate", "NOUA", "IN_PROGRES", "FINALIZATA", "ANULATA");
        cmbFilter.setValue("Toate");

        txtSearch.textProperty().addListener((obs, old, val) -> filterData(txtSearch, cmbFilter));
        cmbFilter.setOnAction(e -> filterData(txtSearch, cmbFilter));

        Label lblCauta = new Label("Cauta:");
        lblCauta.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Label lblStatus = new Label("Status:");
        lblStatus.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        // Butoane
        Button btnAdd = new Button("➕  Adauga");
        Button btnEdit = new Button("🖍  Editeaza");
        Button btnDelete = new Button("🗑  Sterge");

        styleButton(btnAdd, "#fbbf24");
        styleButton(btnEdit, "#f59e0b");
        styleButton(btnDelete, "#ea580c");

        btnAdd.setOnAction(e -> {
            try {
                new SarcinaFormDialog(null, proiectDAO.getAll(), execDAO.getAll())
                        .showAndWait().ifPresent(s -> {
                            try {
                                dao.add(s);
                                loadData();
                                showInfo("Sarcina adaugata!");
                            } catch (SQLException ex) {
                                showError("Eroare: " + ex.getMessage());
                            }
                        });
            } catch (SQLException ex) {
                showError("Eroare: " + ex.getMessage());
            }
        });

        btnEdit.setOnAction(e -> {
            Sarcina sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                showError("Selecteaza o sarcina!");
                return;
            }
            try {
                new SarcinaFormDialog(sel, proiectDAO.getAll(), execDAO.getAll())
                        .showAndWait().ifPresent(s -> {
                            try {
                                dao.update(s);
                                loadData();
                                showInfo("Sarcina actualizata!");
                            } catch (SQLException ex) {
                                showError("Eroare: " + ex.getMessage());
                            }
                        });
            } catch (SQLException ex) {
                showError("Eroare: " + ex.getMessage());
            }
        });

        btnDelete.setOnAction(e -> {
            Sarcina sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                showError("Selecteaza o sarcina!");
                return;
            }
            new Alert(Alert.AlertType.CONFIRMATION, "Stergi sarcina: " + sel.getTitlu() + "?",
                    ButtonType.YES, ButtonType.NO).showAndWait().ifPresent(btn -> {
                        if (btn == ButtonType.YES) {
                            try {
                                dao.delete(sel.getId());
                                loadData();
                                showInfo("Sarcina stearsa!");
                            } catch (SQLException ex) {
                                showError("Eroare: " + ex.getMessage());
                            }
                        }
                    });
        });

        HBox searchBar = new HBox(10, lblCauta, txtSearch, lblStatus, cmbFilter, lblCount);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        searchBar.setPadding(new Insets(5, 0, 5, 0));

        HBox buttons = new HBox(15, btnAdd, btnEdit, btnDelete);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(10, 0, 5, 0));

        VBox root = new VBox(10, title, searchBar, table, buttons);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #1a1a1a;");
        VBox.setVgrow(table, Priority.ALWAYS);

        Scene scene = new Scene(root, 850, 580);
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.show();
    }

    private void filterData(TextField txt, ComboBox<String> cmb) {
        try {
            String status = cmb.getValue().equals("Toate") ? "" : cmb.getValue();
            data.setAll(dao.search(txt.getText(), status));
            lblCount.setText(data.size() + " sarcini gasite");
        } catch (SQLException e) {
            showError("Eroare: " + e.getMessage());
        }
    }

    private void loadData() {
        try {
            data.setAll(dao.getAll());
            lblCount.setText(data.size() + " sarcini");
        } catch (SQLException e) {
            showError("Eroare: " + e.getMessage());
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