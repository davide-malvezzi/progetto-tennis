package circolo.view;

import circolo.Database;
import circolo.Giocatore;
import circolo.util.DateUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class newGiocatoreController {
    @FXML
    private TextField Nome;
    @FXML
    private TextField Cognome;
    @FXML
    private TextField Data_nascita;
    @FXML
    private TextField CF;
    @FXML
    private TextField Indirizzo;
    @FXML
    private CheckBox agonista;
    @FXML
    private CheckBox socio;
    @FXML
    private ComboBox classifica_fit;
    @FXML
    private ComboBox fascia;
    @FXML
    private ComboBox sesso;

    private Giocatore giocatore = new Giocatore();

    @FXML
    private void initialize() {
        fascia.getItems().addAll(1, 2, 3, 4, 5);
        classifica_fit.getItems().addAll("4.NC", 4.5, 4.4, 4.3, 4.2, 4.1, 4.0,
                3.9, 3.8, 3.7, 3.6, 3.5, 3.4, 3.3, 3.2, 3.1, 3.0,
                2.9, 2.8, 2.7, 2.6, 2.5, 2.4, 2.3, 2.2, 2.1, 2.0,
                1.9, 1.8, 1.7, 1.6, 1.5, 1.4, 1.3, 1.2, 1.1);
        sesso.getItems().addAll("M", "F");

    }

    @FXML
    private void handleOK() throws SQLException {
        if (isInputValid()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-uuuu");
            giocatore.setNome(Nome.getText());
            giocatore.setCognome(Cognome.getText());
            giocatore.setData_nascita(LocalDate.parse(Data_nascita.getText(), formatter));
            giocatore.setCF(CF.getText());
            if (Indirizzo.getText().length() == 0)
                giocatore.setIndirizzo(null);
            else giocatore.setIndirizzo(Indirizzo.getText());
            giocatore.setAgonista(agonista.isSelected() ? 1 : 0);
            giocatore.setSocio(socio.isSelected() ? 1 : 0);
            if (fascia.getValue() != null)
                giocatore.setFascia((Integer) fascia.getValue());
            if (classifica_fit.getValue() != null)
                giocatore.setClassifica_FIT(classifica_fit.getValue().toString());
            giocatore.setGenere((String) sesso.getValue());
            Database db = new Database();
            if (db.InserisciGiocatore(giocatore)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Operazione Effettuata");
                alert.setHeaderText(null);
                alert.setContentText("Nuovo giocatore salvato");
                alert.showAndWait();
                clearFields();
            }
        }
    }
    @FXML
    private void handleAnnulla(){
        clearFields();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (Nome.getText() == null || Nome.getText().length() == 0) {
            errorMessage += "Nome non può essere vuoto\n";
        }
        if (Cognome.getText() == null || Cognome.getText().length() == 0) {
            errorMessage += "Cognome non può essere vuoto\n";
        }

        if (CF.getText() == null || CF.getText().length() == 0) {
            errorMessage += "Codice fiscale non può essere vuoto\n";
        }
        if (sesso.getValue() == null) {
            errorMessage += "Sesso non può essere vuoto\n";
        }

        if (Data_nascita.getText() == null || Data_nascita.getText().length() == 0) {
            errorMessage += "Data di nascita non può essere vuota\n";
        } else {
            if (!DateUtil.validDate(Data_nascita.getText())) {
                errorMessage += "Data di nascita non valida. Usa il formato gg-mm-aaaa\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Alcuni campi non sono validi");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

    private void clearFields() {
        Nome.setText("");
        Cognome.setText("");
        Data_nascita.setText("");
        CF.setText("");
        Indirizzo.setText("");
        agonista.setSelected(false);
        socio.setSelected(false);
        sesso.setValue(null);
        fascia.setValue(null);
        classifica_fit.setValue(null);
    }

}
