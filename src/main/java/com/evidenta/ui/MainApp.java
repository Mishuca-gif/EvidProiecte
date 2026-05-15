package com.evidenta.ui;

import javafx.scene.chart.PieChart;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.util.Duration;
import com.evidenta.dao.ExecutantDAO;
import com.evidenta.dao.ProiectDAO;
import com.evidenta.dao.SarcinaDAO;
import com.evidenta.model.*;
import com.evidenta.service.ExportService;
import com.evidenta.service.RaportService;

import java.io.File;
import java.sql.SQLException;

public class MainApp extends Application {

    private ProiectDAO proiectDAO = new ProiectDAO();
    private SarcinaDAO sarcinaDAO = new SarcinaDAO();
    private ExecutantDAO executantDAO = new ExecutantDAO();
    private RaportService raportService = new RaportService();
    private ExportService exportService = new ExportService();

    private TableView<Proiect> tblProiecte = new TableView<>();
    private ObservableList<Proiect> dataProiecte = FXCollections.observableArrayList();
    private Label lblCountProiecte = new Label();

    private TableView<Sarcina> tblSarcini = new TableView<>();
    private ObservableList<Sarcina> dataSarcini = FXCollections.observableArrayList();
    private Label lblCountSarcini = new Label();

    private TableView<Executant> tblExecutanti = new TableView<>();
    private ObservableList<Executant> dataExecutanti = FXCollections.observableArrayList();
    private Label lblCountExecutanti = new Label();

    private TextArea txtRaport = new TextArea();
    private StackPane contentArea = new StackPane();
    private Label statusBar = new Label("  Bun venit în Evidenta Proiectelor!");

    private Button btnDashboard = new Button();
    private Button btnProiecte = new Button();
    private Button btnSarcini = new Button();
    private Button btnExecutanti = new Button();
    private Button btnRapoarte = new Button();
    private Button btnGrafice = new Button();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Evidenta Proiectelor");
        primaryStage.getIcons().add(new Image("https://cdn-icons-png.flaticon.com/512/1055/1055687.png"));

        Label appTitle = new Label("Evidenta\nProiectelor");
        appTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; " +
                "-fx-text-fill: #fbbf24; -fx-font-family: 'Georgia'; " +
                "-fx-text-alignment: center;");
        appTitle.setAlignment(Pos.CENTER);

        initMenuButton(btnDashboard, "🏠  Dashboard");
        initMenuButton(btnProiecte, "📁  Proiecte");
        initMenuButton(btnSarcini, "✅  Sarcini");
        initMenuButton(btnExecutanti, "👥  Executanti");
        initMenuButton(btnRapoarte, "📊  Rapoarte");
        initMenuButton(btnGrafice, "📈  Grafice");
        btnGrafice.setOnAction(e -> {
            switchPanel(buildGraficePanel());
            setActive(btnGrafice, btnDashboard, btnProiecte, btnSarcini, btnExecutanti, btnRapoarte, btnGrafice);
        });
        btnDashboard.setOnAction(e -> {
            switchPanel(buildDashboardPanel());
            setActive(btnDashboard, btnProiecte, btnSarcini, btnExecutanti, btnRapoarte, btnGrafice);
        });
        btnProiecte.setOnAction(e -> {
            switchPanel(buildProiectePanel());
            setActive(btnProiecte, btnDashboard, btnSarcini, btnExecutanti, btnRapoarte, btnGrafice);
        });
        btnSarcini.setOnAction(e -> {
            switchPanel(buildSarciniPanel());
            setActive(btnSarcini, btnDashboard, btnProiecte, btnExecutanti, btnRapoarte, btnGrafice);
        });
        btnExecutanti.setOnAction(e -> {
            switchPanel(buildExecutantiPanel());
            setActive(btnExecutanti, btnDashboard, btnProiecte, btnSarcini, btnRapoarte, btnGrafice);
        });
        btnRapoarte.setOnAction(e -> {
            switchPanel(buildRaportePanel());
            setActive(btnRapoarte, btnDashboard, btnProiecte, btnSarcini, btnExecutanti, btnGrafice);
        });

        Button btnExit = new Button("🚪  Iesire");
        btnExit.setMaxWidth(Double.MAX_VALUE);
        btnExit.setPrefHeight(45);
        btnExit.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
                "-fx-font-size: 13px; -fx-font-weight: bold; " +
                "-fx-background-radius: 8; -fx-font-family: 'Georgia'; " +
                "-fx-alignment: CENTER_LEFT; -fx-padding: 0 0 0 10;");
        btnExit.setOnMouseEntered(e -> btnExit.setStyle(
                "-fx-background-color: derive(#e74c3c, -15%); -fx-text-fill: white; " +
                        "-fx-font-size: 13px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 8; -fx-font-family: 'Georgia'; " +
                        "-fx-alignment: CENTER_LEFT; -fx-padding: 0 0 0 10;"));
        btnExit.setOnMouseExited(e -> btnExit.setStyle(
                "-fx-background-color: #e74c3c; -fx-text-fill: white; " +
                        "-fx-font-size: 13px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 8; -fx-font-family: 'Georgia'; " +
                        "-fx-alignment: CENTER_LEFT; -fx-padding: 0 0 0 10;"));
        btnExit.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Esti sigur ca vrei sa inchizi aplicatia?",
                    ButtonType.YES, ButtonType.NO);
            confirm.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.YES)
                    primaryStage.close();
            });
        });

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox sidebar = new VBox(15, appTitle, btnDashboard, btnProiecte, btnSarcini, btnExecutanti, btnRapoarte,
                btnGrafice, spacer,
                btnExit);
        sidebar.setPadding(new Insets(25, 15, 25, 15));
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setStyle("-fx-background-color: #111111;");
        sidebar.setPrefWidth(180);

        statusBar.setStyle("-fx-background-color: #111111; -fx-text-fill: #aaaaaa; " +
                "-fx-font-size: 12px; -fx-padding: 5 10 5 10;");
        statusBar.setMaxWidth(Double.MAX_VALUE);

        contentArea.setStyle("-fx-background-color: #1a1a1a;");

        HBox root = new HBox(sidebar, contentArea);
        HBox.setHgrow(contentArea, Priority.ALWAYS);

        VBox mainLayout = new VBox(root, statusBar);
        VBox.setVgrow(root, Priority.ALWAYS);

        switchPanel(buildDashboardPanel());
        setActive(btnDashboard, btnProiecte, btnSarcini, btnExecutanti, btnRapoarte);

        Scene scene = new Scene(mainLayout, 1100, 650);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(500);
        primaryStage.show();
    }

    // ── DASHBOARD ──
    private VBox buildDashboardPanel() {
        Label title = new Label("🏠  Dashboard");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; " +
                "-fx-text-fill: #fbbf24; -fx-font-family: 'Georgia';");

        int totalProiecte = 0, totalSarcini = 0, totalExecutanti = 0;
        int activ = 0, finalizat = 0, noua = 0, inProgres = 0, finalizata = 0, anulata = 0;

        try {
            totalProiecte = proiectDAO.getAll().size();
            totalExecutanti = executantDAO.getAll().size();
            var sarcini = sarcinaDAO.getAll();
            totalSarcini = sarcini.size();
            for (var p : proiectDAO.getAll()) {
                if (p.getStatus().equals("ACTIV"))
                    activ++;
                else if (p.getStatus().equals("FINALIZAT"))
                    finalizat++;
            }
            for (var s : sarcini) {
                switch (s.getStatus().name()) {
                    case "NOUA" -> noua++;
                    case "IN_PROGRES" -> inProgres++;
                    case "FINALIZATA" -> finalizata++;
                    case "ANULATA" -> anulata++;
                }
            }
        } catch (SQLException e) {
            showError(e.getMessage());
        }

        HBox row1 = new HBox(20,
                buildCard("📁 Proiecte", String.valueOf(totalProiecte), "#fbbf24"),
                buildCard("✅ Sarcini", String.valueOf(totalSarcini), "#f59e0b"),
                buildCard("👥 Executanti", String.valueOf(totalExecutanti), "#ea580c"));
        row1.setAlignment(Pos.CENTER);

        Label lblProiecteStatus = new Label("Status Proiecte");
        lblProiecteStatus.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 14px; -fx-font-weight: bold;");

        HBox row2 = new HBox(20,
                buildCard("🟢 Activ", String.valueOf(activ), "#2ecc71"),
                buildCard("🔵 Finalizat", String.valueOf(finalizat), "#3498db"));
        row2.setAlignment(Pos.CENTER);

        Label lblSarciniStatus = new Label("Status Sarcini");
        lblSarciniStatus.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 14px; -fx-font-weight: bold;");

        HBox row3 = new HBox(20,
                buildCard("🟡 Noua", String.valueOf(noua), "#fbbf24"),
                buildCard("🔵 In Progres", String.valueOf(inProgres), "#3498db"),
                buildCard("🟢 Finalizata", String.valueOf(finalizata), "#2ecc71"),
                buildCard("🔴 Anulata", String.valueOf(anulata), "#e74c3c"));
        row3.setAlignment(Pos.CENTER);

        VBox panel = new VBox(25, title, row1, lblProiecteStatus, row2, lblSarciniStatus, row3);
        panel.setPadding(new Insets(30));
        panel.setAlignment(Pos.TOP_LEFT);
        panel.setStyle("-fx-background-color: #1a1a1a;");
        return panel;
    }

    private VBox buildCard(String label, String value, String color) {
        Label lblValue = new Label(value);
        lblValue.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; " +
                "-fx-text-fill: " + color + "; -fx-font-family: 'Georgia';");
        Label lblLabel = new Label(label);
        lblLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #aaaaaa; -fx-font-family: 'Georgia';");
        VBox card = new VBox(5, lblValue, lblLabel);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setPrefWidth(160);
        card.setPrefHeight(100);
        card.setStyle("-fx-background-color: #2d2d2d; -fx-background-radius: 12; " +
                "-fx-border-color: " + color + "; -fx-border-radius: 12; -fx-border-width: 1;");
        return card;
    }

    // ── PANEL PROIECTE ──
    @SuppressWarnings("unchecked")
    private VBox buildProiectePanel() {
        Label title = new Label("📁  Gestionare Proiecte");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; " +
                "-fx-text-fill: #fbbf24; -fx-font-family: 'Georgia';");

        lblCountProiecte.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 12px;");

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

        tblProiecte.getColumns().setAll(colId, colTitlu, colStatus, colStart, colEnd, colDescriere);
        tblProiecte.setItems(dataProiecte);
        tblProiecte.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tblProiecte.getSortOrder().clear();
        loadProiecte();

        TextField search = new TextField();
        search.setPromptText("Cauta dupa titlu sau status...");
        search.setStyle("-fx-background-color: white; -fx-text-fill: black; " +
                "-fx-prompt-text-fill: #aaaaaa; -fx-border-color: #fbbf24; " +
                "-fx-border-radius: 4; -fx-background-radius: 4;");
        search.textProperty().addListener((obs, old, val) -> {
            try {
                dataProiecte.setAll(proiectDAO.search(val));
                lblCountProiecte.setText(dataProiecte.size() + " proiecte");
            } catch (SQLException e) {
                showError(e.getMessage());
            }
        });

        Label lblCautare = new Label("Cautare:");
        lblCautare.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Button btnAdd = new Button("➕  Adauga");
        Button btnEdit = new Button("✎  Editeaza");
        Button btnDelete = new Button("🗑  Sterge");
        styleButton(btnAdd, "#fbbf24");
        styleButton(btnEdit, "#f59e0b");
        styleButton(btnDelete, "#ea580c");

        btnAdd.setOnAction(e -> new ProiectFormDialog(null).showAndWait().ifPresent(p -> {
            try {
                proiectDAO.add(p);
                loadProiecte();
                showInfo("Proiect adaugat!");
                setStatus("Proiect adaugat cu succes!");
            } catch (SQLException ex) {
                showError(ex.getMessage());
            }
        }));
        btnEdit.setOnAction(e -> {
            Proiect sel = tblProiecte.getSelectionModel().getSelectedItem();
            if (sel == null) {
                showError("Selecteaza un proiect!");
                return;
            }
            new ProiectFormDialog(sel).showAndWait().ifPresent(p -> {
                try {
                    proiectDAO.update(p);
                    loadProiecte();
                    showInfo("Actualizat!");
                    setStatus("Proiect actualizat!");
                } catch (SQLException ex) {
                    showError(ex.getMessage());
                }
            });
        });
        btnDelete.setOnAction(e -> {
            Proiect sel = tblProiecte.getSelectionModel().getSelectedItem();
            if (sel == null) {
                showError("Selecteaza un proiect!");
                return;
            }
            new Alert(Alert.AlertType.CONFIRMATION, "Stergi: " + sel.getTitlu() + "?",
                    ButtonType.YES, ButtonType.NO).showAndWait().ifPresent(b -> {
                        if (b == ButtonType.YES) {
                            try {
                                proiectDAO.delete(sel.getId());
                                loadProiecte();
                                showInfo("Sters!");
                                setStatus("Proiect sters!");
                            } catch (SQLException ex) {
                                showError(ex.getMessage());
                            }
                        }
                    });
        });

        HBox searchBar = new HBox(10, lblCautare, search, lblCountProiecte);
        searchBar.setAlignment(Pos.CENTER_LEFT);

        HBox buttons = new HBox(15, btnAdd, btnEdit, btnDelete);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(10, 0, 5, 0));

        VBox panel = new VBox(10, title, searchBar, tblProiecte, buttons);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: #1a1a1a;");
        VBox.setVgrow(tblProiecte, Priority.ALWAYS);
        return panel;
    }

    // ── PANEL SARCINI ──
    @SuppressWarnings("unchecked")
    private VBox buildSarciniPanel() {
        Label title = new Label("✅  Gestionare Sarcini");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; " +
                "-fx-text-fill: #fbbf24; -fx-font-family: 'Georgia';");

        lblCountSarcini.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 12px;");

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

        TableColumn<Sarcina, Integer> colPrio = new TableColumn<>("Prioritate");
        colPrio.setCellValueFactory(new PropertyValueFactory<>("prioritate"));
        colPrio.setPrefWidth(80);

        TableColumn<Sarcina, String> colProiect = new TableColumn<>("Proiect");
        colProiect.setCellValueFactory(new PropertyValueFactory<>("numeProiect"));
        colProiect.setPrefWidth(150);

        TableColumn<Sarcina, String> colExec = new TableColumn<>("Executant");
        colExec.setCellValueFactory(new PropertyValueFactory<>("numeExecutant"));
        colExec.setPrefWidth(150);

        tblSarcini.getColumns().setAll(colId, colTitlu, colStatus, colPrio, colProiect, colExec);
        tblSarcini.setItems(dataSarcini);
        tblSarcini.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tblSarcini.getSortOrder().clear();
        loadSarcini();

        TextField txtSearch = new TextField();
        txtSearch.setPromptText("Cauta dupa titlu...");
        txtSearch.setStyle("-fx-background-color: white; -fx-text-fill: black; " +
                "-fx-prompt-text-fill: #aaaaaa; -fx-border-color: #fbbf24; " +
                "-fx-border-radius: 4; -fx-background-radius: 4;");

        ComboBox<String> cmbFilter = new ComboBox<>();
        cmbFilter.getItems().addAll("Toate", "NOUA", "IN_PROGRES", "FINALIZATA", "ANULATA");
        cmbFilter.setValue("Toate");

        txtSearch.textProperty().addListener((obs, old, val) -> filterSarcini(txtSearch, cmbFilter));
        cmbFilter.setOnAction(e -> filterSarcini(txtSearch, cmbFilter));

        Label lblCauta = new Label("Cauta:");
        lblCauta.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        Label lblStatus = new Label("Status:");
        lblStatus.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Button btnAdd = new Button("➕  Adauga");
        Button btnEdit = new Button("✎  Editeaza");
        Button btnDelete = new Button("🗑  Sterge");
        styleButton(btnAdd, "#fbbf24");
        styleButton(btnEdit, "#f59e0b");
        styleButton(btnDelete, "#ea580c");

        btnAdd.setOnAction(e -> {
            try {
                new SarcinaFormDialog(null, proiectDAO.getAll(), executantDAO.getAll())
                        .showAndWait().ifPresent(s -> {
                            try {
                                sarcinaDAO.add(s);
                                loadSarcini();
                                showInfo("Sarcina adaugata!");
                                setStatus("Sarcina adaugata!");
                            } catch (SQLException ex) {
                                showError(ex.getMessage());
                            }
                        });
            } catch (SQLException ex) {
                showError(ex.getMessage());
            }
        });
        btnEdit.setOnAction(e -> {
            Sarcina sel = tblSarcini.getSelectionModel().getSelectedItem();
            if (sel == null) {
                showError("Selecteaza o sarcina!");
                return;
            }
            try {
                new SarcinaFormDialog(sel, proiectDAO.getAll(), executantDAO.getAll())
                        .showAndWait().ifPresent(s -> {
                            try {
                                sarcinaDAO.update(s);
                                loadSarcini();
                                showInfo("Actualizat!");
                                setStatus("Sarcina actualizata!");
                            } catch (SQLException ex) {
                                showError(ex.getMessage());
                            }
                        });
            } catch (SQLException ex) {
                showError(ex.getMessage());
            }
        });
        btnDelete.setOnAction(e -> {
            Sarcina sel = tblSarcini.getSelectionModel().getSelectedItem();
            if (sel == null) {
                showError("Selecteaza o sarcina!");
                return;
            }
            new Alert(Alert.AlertType.CONFIRMATION, "Stergi: " + sel.getTitlu() + "?",
                    ButtonType.YES, ButtonType.NO).showAndWait().ifPresent(b -> {
                        if (b == ButtonType.YES) {
                            try {
                                sarcinaDAO.delete(sel.getId());
                                loadSarcini();
                                showInfo("Stearsa!");
                                setStatus("Sarcina stearsa!");
                            } catch (SQLException ex) {
                                showError(ex.getMessage());
                            }
                        }
                    });
        });

        HBox searchBar = new HBox(10, lblCauta, txtSearch, lblStatus, cmbFilter, lblCountSarcini);
        searchBar.setAlignment(Pos.CENTER_LEFT);

        HBox buttons = new HBox(15, btnAdd, btnEdit, btnDelete);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(10, 0, 5, 0));

        VBox panel = new VBox(10, title, searchBar, tblSarcini, buttons);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: #1a1a1a;");
        VBox.setVgrow(tblSarcini, Priority.ALWAYS);
        return panel;
    }

    // ── PANEL EXECUTANTI ──
    @SuppressWarnings({ "deprecation", "unchecked" })
    private VBox buildExecutantiPanel() {
        Label title = new Label("👥  Gestionare Executanti");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; " +
                "-fx-text-fill: #fbbf24; -fx-font-family: 'Georgia';");

        lblCountExecutanti.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 12px;");

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

        tblExecutanti.getColumns().setAll(colId, colNume, colPrenume, colEmail, colRol);
        tblExecutanti.setItems(dataExecutanti);
        tblExecutanti.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tblExecutanti.getSortOrder().clear();
        loadExecutanti();

        Button btnAdd = new Button("➕  Adauga");
        Button btnEdit = new Button("✎  Editeaza");
        Button btnDelete = new Button("🗑  Sterge");
        styleButton(btnAdd, "#fbbf24");
        styleButton(btnEdit, "#f59e0b");
        styleButton(btnDelete, "#ea580c");

        btnAdd.setOnAction(e -> new ExecutantFormDialog(null).showAndWait().ifPresent(ex -> {
            try {
                executantDAO.add(ex);
                loadExecutanti();
                showInfo("Adaugat!");
                setStatus("Executant adaugat!");
            } catch (SQLException ex2) {
                showError(ex2.getMessage());
            }
        }));
        btnEdit.setOnAction(e -> {
            Executant sel = tblExecutanti.getSelectionModel().getSelectedItem();
            if (sel == null) {
                showError("Selecteaza un executant!");
                return;
            }
            new ExecutantFormDialog(sel).showAndWait().ifPresent(ex -> {
                try {
                    executantDAO.update(ex);
                    loadExecutanti();
                    showInfo("Actualizat!");
                    setStatus("Executant actualizat!");
                } catch (SQLException ex2) {
                    showError(ex2.getMessage());
                }
            });
        });
        btnDelete.setOnAction(e -> {
            Executant sel = tblExecutanti.getSelectionModel().getSelectedItem();
            if (sel == null) {
                showError("Selecteaza un executant!");
                return;
            }
            new Alert(Alert.AlertType.CONFIRMATION, "Stergi: " + sel.toString() + "?",
                    ButtonType.YES, ButtonType.NO).showAndWait().ifPresent(b -> {
                        if (b == ButtonType.YES) {
                            try {
                                executantDAO.delete(sel.getId());
                                loadExecutanti();
                                showInfo("Sters!");
                                setStatus("Executant sters!");
                            } catch (SQLException ex2) {
                                showError(ex2.getMessage());
                            }
                        }
                    });
        });

        HBox topBar = new HBox(10, lblCountExecutanti);
        topBar.setAlignment(Pos.CENTER_LEFT);

        HBox buttons = new HBox(15, btnAdd, btnEdit, btnDelete);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(10, 0, 5, 0));

        VBox panel = new VBox(10, title, topBar, tblExecutanti, buttons);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: #1a1a1a;");
        VBox.setVgrow(tblExecutanti, Priority.ALWAYS);
        return panel;
    }

    private VBox buildGraficePanel() {
        Label title = new Label("📈  Grafice");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; " +
                "-fx-text-fill: #fbbf24; -fx-font-family: 'Georgia';");

        PieChart pieChart = new PieChart();
        pieChart.setTitle("");
        Label chartTitle = new Label("Distributie Sarcini dupa Status");
        chartTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; " +
                "-fx-text-fill: white; -fx-font-family: 'Georgia';");
        pieChart.setStyle("-fx-background-color: #1a1a1a, ");
        pieChart.setLabelLineLength(10);
        pieChart.setLabelsVisible(true);
        pieChart.setStyle("-fx-background-color: #1a1a1a; " +
                "-fx-pie-label-visible: true;");

        pieChart.lookupAll(".chart-legend-item").forEach(node -> node.setStyle("-fx-text-fill: white;"));

        pieChart.titleProperty().addListener((obs, old, val) -> pieChart.lookup(".chart-title")
                .setStyle("-fx-text-fill: #fbbf24; -fx-font-size: 16px; -fx-font-family: 'Georgia';"));

        try {
            var sarcini = sarcinaDAO.getAll();
            int noua = 0, inProgres = 0, finalizata = 0, anulata = 0;
            for (var s : sarcini) {
                switch (s.getStatus().name()) {
                    case "NOUA" -> noua++;
                    case "IN_PROGRES" -> inProgres++;
                    case "FINALIZATA" -> finalizata++;
                    case "ANULATA" -> anulata++;
                }
            }
            pieChart.getData().addAll(
                    new PieChart.Data("Noua (" + noua + ")", noua),
                    new PieChart.Data("In progres (" + inProgres + ")", inProgres),
                    new PieChart.Data("Finalizata (" + finalizata + ")", finalizata),
                    new PieChart.Data("Anulata (" + anulata + ")", anulata));
            pieChart.setStyle(
                    "-fx-background-color: #1a1a1a;" +
                            "-fx-pie-label-visible: true;");

            javafx.application.Platform.runLater(() -> {
                pieChart.lookupAll(".chart-legend-item-text")
                        .forEach(node -> node.setStyle("-fx-fill: white; -fx-font-size: 13px;"));
                pieChart.lookupAll(".chart-title").forEach(
                        node -> node.setStyle("-fx-fill: #fbbf24; -fx-font-size: 16px; -fx-font-family: 'Georgia';"));
                pieChart.lookupAll(".chart-pie-label")
                        .forEach(node -> node.setStyle("-fx-fill: white; -fx-font-size: 12px;"));
            });
        } catch (SQLException e) {
            showError(e.getMessage());
        }
        VBox panel = new VBox(15, title,chartTitle, pieChart);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: #1a1a1a;");
        VBox.setVgrow(pieChart, Priority.ALWAYS);
        return panel;
    }

    // ── PANEL RAPOARTE ──
    private VBox buildRaportePanel() {
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
            setStatus("Raport generat!");
        });

        txtRaport.setEditable(false);
        txtRaport.setStyle("-fx-font-family: monospace; -fx-font-size: 13px; " +
                "-fx-control-inner-background: #2d2d2d; -fx-text-fill: white;");

        Button btnExportTxt = new Button("💾  Export TXT");
        Button btnExportCsv = new Button("📄  Export CSV");
        styleButton(btnExportTxt, "#fbbf24");
        styleButton(btnExportCsv, "#ea580c");

        btnExportTxt.setOnAction(e -> {
            if (txtRaport.getText().isEmpty()) {
                showError("Genereaza mai intai un raport!");
                return;
            }
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text", "*.txt"));
            File file = fc.showSaveDialog(null);
            if (file != null) {
                try {
                    exportService.exportRaportTxt(txtRaport.getText(), file.getAbsolutePath());
                    showInfo("Export reusit!");
                    setStatus("Raport exportat: " + file.getName());
                } catch (Exception ex) {
                    showError(ex.getMessage());
                }
            }
        });

        btnExportCsv.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
            File file = fc.showSaveDialog(null);
            if (file != null) {
                try {
                    exportService.exportCsv(proiectDAO.getAll(),
                            "id,titlu,descriere,dataStart,dataEnd,status", file.getAbsolutePath());
                    showInfo("Export CSV reusit!");
                    setStatus("CSV exportat: " + file.getName());
                } catch (Exception ex) {
                    showError(ex.getMessage());
                }
            }
        });

        HBox topBar = new HBox(10, cmbRaport, btnGenereaza);
        topBar.setAlignment(Pos.CENTER_LEFT);

        HBox exportBar = new HBox(15, btnExportTxt, btnExportCsv);
        exportBar.setAlignment(Pos.CENTER);
        exportBar.setPadding(new Insets(10, 0, 5, 0));

        VBox panel = new VBox(10, title, topBar, txtRaport, exportBar);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: #1a1a1a;");
        VBox.setVgrow(txtRaport, Priority.ALWAYS);
        return panel;
    }

    // ── SWITCH PANEL CU ANIMATIE ──
    private void switchPanel(VBox panel) {
        panel.setTranslateX(40);
        panel.setOpacity(0);
        panel.setScaleY(0.97);
        contentArea.getChildren().setAll(panel);

        TranslateTransition slide = new TranslateTransition(Duration.millis(300), panel);
        slide.setFromX(40);
        slide.setToX(0);
        slide.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

        FadeTransition fade = new FadeTransition(Duration.millis(300), panel);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);

        ScaleTransition scale = new ScaleTransition(Duration.millis(300), panel);
        scale.setFromY(0.97);
        scale.setToY(1.0);
        scale.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

        new ParallelTransition(slide, fade, scale).play();
    }

    // ── HELPERS ──
    private void loadProiecte() {
        try {
            dataProiecte.setAll(proiectDAO.getAll());
            lblCountProiecte.setText(dataProiecte.size() + " proiecte");
            setStatus("Proiecte incarcate: " + dataProiecte.size());
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    private void loadSarcini() {
        try {
            dataSarcini.setAll(sarcinaDAO.getAll());
            lblCountSarcini.setText(dataSarcini.size() + " sarcini");
            setStatus("Sarcini incarcate: " + dataSarcini.size());
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    private void loadExecutanti() {
        try {
            dataExecutanti.setAll(executantDAO.getAll());
            lblCountExecutanti.setText(dataExecutanti.size() + " executanti");
            setStatus("Executanti incarcati: " + dataExecutanti.size());
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    private void filterSarcini(TextField txt, ComboBox<String> cmb) {
        try {
            String status = cmb.getValue().equals("Toate") ? "" : cmb.getValue();
            dataSarcini.setAll(sarcinaDAO.search(txt.getText(), status));
            lblCountSarcini.setText(dataSarcini.size() + " sarcini");
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    private void initMenuButton(Button btn, String text) {
        btn.setText(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(45);
        btn.setStyle("-fx-background-color: #222222; -fx-text-fill: #aaaaaa; " +
                "-fx-font-size: 13px; -fx-font-weight: bold; " +
                "-fx-background-radius: 8; -fx-font-family: 'Georgia'; " +
                "-fx-alignment: CENTER_LEFT; -fx-padding: 0 0 0 10;");
    }

    @SuppressWarnings("unused")
    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(45);
        btn.setStyle("-fx-background-color: #222222; -fx-text-fill: #aaaaaa; " +
                "-fx-font-size: 13px; -fx-font-weight: bold; " +
                "-fx-background-radius: 8; -fx-font-family: 'Georgia'; " +
                "-fx-alignment: CENTER_LEFT; -fx-padding: 0 0 0 10;");
        return btn;
    }

    private void setActive(Button active, Button... others) {
        active.setDisable(true);
        active.setStyle("-fx-background-color: #fbbf24; -fx-text-fill: #1a1a1a; " +
                "-fx-font-size: 13px; -fx-font-weight: bold; " +
                "-fx-background-radius: 8; -fx-font-family: 'Georgia'; " +
                "-fx-alignment: CENTER_LEFT; -fx-padding: 0 0 0 10;");
        for (Button btn : others) {
            btn.setDisable(false);
            btn.setStyle("-fx-background-color: #222222; -fx-text-fill: #aaaaaa; " +
                    "-fx-font-size: 13px; -fx-font-weight: bold; " +
                    "-fx-background-radius: 8; -fx-font-family: 'Georgia'; " +
                    "-fx-alignment: CENTER_LEFT; -fx-padding: 0 0 0 10;");
        }
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

    private void setStatus(String msg) {
        statusBar.setText("  " + msg);
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}