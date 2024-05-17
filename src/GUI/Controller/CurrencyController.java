package GUI.Controller;

import BE.Currency;
import CustomExceptions.ApplicationWideException;
import GUI.Model.CurrencyModel;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class CurrencyController implements Initializable {

    @FXML
    private MFXLegacyTableView<Currency> tblCurrency;
    @FXML
    private TableColumn<Currency, String> colCurrencyCodes;
    @FXML
    private TableColumn<Currency, Double> colCurrencyRate;
    @FXML
    private TableColumn<Currency, Integer> colCurrencyId;
    @FXML
    private TextField txtCurrency;
    @FXML
    private Button btnCurrencySave;

    private CurrencyModel currencyModel;
    @FXML
    private TextField txtSearchCurrency;
    @FXML
    private Label lblMessageCurrency;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            currencyModel = new CurrencyModel();
        } catch (ApplicationWideException e) {
            displayMessage("Failed to load currencies: " + e.getMessage(), true);
        }

        setupTable();
        bindSelectionToTextField();

        btnCurrencySave.setDisable(true);
        tblCurrency.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            btnCurrencySave.setDisable(newSelection == null);
        });
    }


    public void setupTable() {
        try {
            colCurrencyId.setCellValueFactory(new PropertyValueFactory<>("currencyId"));
            colCurrencyCodes.setCellValueFactory(new PropertyValueFactory<>("currencyCode"));
            colCurrencyRate.setCellValueFactory(new PropertyValueFactory<>("currencyRate"));
            tblCurrency.setItems(currencyModel.getCurrencies());
        } catch (Exception e) {
            displayMessage("Error setting up the currency table: " + e.getMessage(), true);
        }

    }

    private void bindSelectionToTextField() {
        tblCurrency.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtCurrency.setText(String.format("%.2f", newSelection.getCurrencyRate()));
            }
        });
    }

    @FXML
    private void saveCurrency(ActionEvent actionEvent) {
        Currency selectedCurrency = tblCurrency.getSelectionModel().getSelectedItem();
        if (selectedCurrency == null) {
            displayMessage("No currency selected.", true);
            return;
        }
        try {
            String txtCurrencyValue = txtCurrency.getText().replace(',', '.');
            double newRate = Double.parseDouble(txtCurrencyValue);
            selectedCurrency.setCurrencyRate(newRate);
            currencyModel.setCurrency(selectedCurrency);
            displayMessage("Currency rate updated successfully.", false);
        } catch (NumberFormatException e) {
            displayMessage("Invalid currency rate entered.", true);
        }
    }


    private void displayMessage(String message, boolean isError) {
        lblMessageCurrency.setText(message);
        lblMessageCurrency.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");


        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> lblMessageCurrency.setText("")));
        timeline.play();
    }


}
