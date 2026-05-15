package com.evidenta.ui;

import com.evidenta.model.Proiect;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class ProiectFormDialog extends Dialog<Proiect> {

    public ProiectFormDialog(Proiect proiect) {
        setTitle(proiect == null ? "Adauga Proiect" : "Editeaza Proiect");

        ButtonType saveBtn = new ButtonType("Salveaza", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        TextField txtTitlu    = new TextField();
        TextField txtDescriere = new TextField();
        TextField txtStart    = new TextField();
        TextField txtEnd      = new TextField();
        ComboBox<String> cmbStatus = new ComboBox<>();
        cmbStatus.getItems().addAll("ACTIV", "FINALIZAT", "SUSPENDAT");

        txtStart.setPromptText("YYYY-MM-DD");
        txtEnd.setPromptText("YYYY-MM-DD (optional)");

        if (proiect != null) {
            txtTitlu.setText(proiect.getTitlu());
            txtDescriere.setText(proiect.getDescriere());
            txtStart.setText(proiect.getDataStart());
            txtEnd.setText(proiect.getDataEnd() != null ? proiect.getDataEnd() : "");
            cmbStatus.setValue(proiect.getStatus());
        } else {
            cmbStatus.setValue("ACTIV");
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.addRow(0, new Label("Titlu:"),     txtTitlu);
        grid.addRow(1, new Label("Descriere:"), txtDescriere);
        grid.addRow(2, new Label("Data Start:"), txtStart);
        grid.addRow(3, new Label("Data End:"),  txtEnd);
        grid.addRow(4, new Label("Status:"),    cmbStatus);

        getDialogPane().setContent(grid);

        setResultConverter(btn -> {
            if (btn == saveBtn) {
                if (txtTitlu.getText().trim().isEmpty()) {
                    new Alert(Alert.AlertType.ERROR, "Titlul nu poate fi gol!").showAndWait();
                    return null;
                }
                if (txtStart.getText().trim().isEmpty()) {
                    new Alert(Alert.AlertType.ERROR, "Data start nu poate fi goala!").showAndWait();
                    return null;
                }
                int id = proiect != null ? proiect.getId() : 0;
                return new Proiect(
                    id,
                    txtTitlu.getText().trim(),
                    txtDescriere.getText().trim(),
                    txtStart.getText().trim(),
                    txtEnd.getText().trim().isEmpty() ? null : txtEnd.getText().trim(),
                    cmbStatus.getValue()
                );
            }
            return null;
        });
    }
}