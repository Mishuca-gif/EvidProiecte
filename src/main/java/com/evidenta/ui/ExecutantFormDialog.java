package com.evidenta.ui;

import com.evidenta.model.Executant;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class ExecutantFormDialog extends Dialog<Executant> {

    public ExecutantFormDialog(Executant executant) {
        setTitle(executant == null ? "Adauga Executant" : "Editeaza Executant");

        ButtonType saveBtn = new ButtonType("Salveaza", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        TextField txtNume    = new TextField();
        TextField txtPrenume = new TextField();
        TextField txtEmail   = new TextField();
        ComboBox<String> cmbRol = new ComboBox<>();
        cmbRol.getItems().addAll("Developer", "Tester", "Designer", "Manager", "DevOps", "Analyst");

        if (executant != null) {
            txtNume.setText(executant.getNume());
            txtPrenume.setText(executant.getPrenume());
            txtEmail.setText(executant.getEmail());
            cmbRol.setValue(executant.getRol());
        } else {
            cmbRol.setValue("Developer");
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.addRow(0, new Label("Nume:"),    txtNume);
        grid.addRow(1, new Label("Prenume:"), txtPrenume);
        grid.addRow(2, new Label("Email:"),   txtEmail);
        grid.addRow(3, new Label("Rol:"),     cmbRol);

        getDialogPane().setContent(grid);

        setResultConverter(btn -> {
            if (btn == saveBtn) {
                if (txtNume.getText().trim().isEmpty() || txtPrenume.getText().trim().isEmpty()) {
                    new Alert(Alert.AlertType.ERROR, "Numele si prenumele nu pot fi goale!").showAndWait();
                    return null;
                }
                if (!txtEmail.getText().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                    new Alert(Alert.AlertType.ERROR, "Email invalid!").showAndWait();
                    return null;
                }
                int id = executant != null ? executant.getId() : 0;
                return new Executant(id, txtNume.getText().trim(),
                    txtPrenume.getText().trim(), txtEmail.getText().trim(), cmbRol.getValue());
            }
            return null;
        });
    }
}