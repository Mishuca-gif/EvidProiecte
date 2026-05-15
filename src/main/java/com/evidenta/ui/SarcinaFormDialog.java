package com.evidenta.ui;

import com.evidenta.model.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.List;

public class SarcinaFormDialog extends Dialog<Sarcina> {

    public SarcinaFormDialog(Sarcina sarcina, List<Proiect> proiecte, List<Executant> executanti) {
        setTitle(sarcina == null ? "Adauga Sarcina" : "Editeaza Sarcina");

        ButtonType saveBtn = new ButtonType("Salveaza", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        TextField txtTitlu    = new TextField();
        TextField txtDescriere = new TextField();
        TextField txtPrioritate = new TextField("1");

        ComboBox<String> cmbStatus = new ComboBox<>();
        cmbStatus.getItems().addAll("NOUA", "IN_PROGRES", "FINALIZATA", "ANULATA");
        cmbStatus.setValue("NOUA");

        ComboBox<Proiect> cmbProiect = new ComboBox<>();
        cmbProiect.getItems().addAll(proiecte);

        ComboBox<Executant> cmbExecutant = new ComboBox<>();
        cmbExecutant.getItems().addAll(executanti);

        if (sarcina != null) {
            txtTitlu.setText(sarcina.getTitlu());
            txtDescriere.setText(sarcina.getDescriere());
            txtPrioritate.setText(String.valueOf(sarcina.getPrioritate()));
            cmbStatus.setValue(sarcina.getStatus().name());
            proiecte.stream().filter(p -> p.getId() == sarcina.getIdProiect())
                    .findFirst().ifPresent(cmbProiect::setValue);
            executanti.stream().filter(e -> e.getId() == sarcina.getIdExecutant())
                    .findFirst().ifPresent(cmbExecutant::setValue);
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.addRow(0, new Label("Titlu:"),      txtTitlu);
        grid.addRow(1, new Label("Descriere:"),  txtDescriere);
        grid.addRow(2, new Label("Prioritate:"), txtPrioritate);
        grid.addRow(3, new Label("Status:"),     cmbStatus);
        grid.addRow(4, new Label("Proiect:"),    cmbProiect);
        grid.addRow(5, new Label("Executant:"),  cmbExecutant);

        getDialogPane().setContent(grid);

        setResultConverter(btn -> {
            if (btn == saveBtn) {
                if (txtTitlu.getText().trim().isEmpty()) {
                    new Alert(Alert.AlertType.ERROR, "Titlul nu poate fi gol!").showAndWait();
                    return null;
                }
                if (cmbProiect.getValue() == null) {
                    new Alert(Alert.AlertType.ERROR, "Selecteaza un proiect!").showAndWait();
                    return null;
                }
                int id = sarcina != null ? sarcina.getId() : 0;
                int prio = 1;
                try { prio = Integer.parseInt(txtPrioritate.getText()); }
                catch (NumberFormatException ignored) {}
                int idExec = cmbExecutant.getValue() != null ? cmbExecutant.getValue().getId() : 0;
                return new Sarcina(id, txtTitlu.getText().trim(), txtDescriere.getText().trim(),
                    prio, StatusSarcina.valueOf(cmbStatus.getValue()),
                    cmbProiect.getValue().getId(), idExec,
                    cmbProiect.getValue().getTitlu(),
                    cmbExecutant.getValue() != null ? cmbExecutant.getValue().toString() : "");
            }
            return null;
        });
    }
}