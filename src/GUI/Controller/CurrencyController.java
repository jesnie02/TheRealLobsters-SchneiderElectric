package GUI.Controller;

import BE.Currency;
import CustomExceptions.ApplicationWideException;
import GUI.Model.CurrencyModel;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
    private TextField txtCurrency,txtSearchCurrency;
    @FXML
    private Button btnCurrencySave;
    @FXML
    private Label lblMessageCurrency;


    private CurrencyModel currencyModel;
    private FilteredList<Currency> filteredData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            currencyModel = new CurrencyModel();

            ObservableList<Currency> data = currencyModel.getCurrencies();
            filteredData = new FilteredList<>(data, p -> true);
            searchInCurrencyCodes();

            tblCurrency.setItems(filteredData);

        } catch (ApplicationWideException e) {
            displayMessage("Failed to load currencies: " + e.getMessage(), true);
        }

        setRegexValidationForTextFields(txtCurrency);
        setupTable();
        bindSelectionToTextField();

        btnCurrencySave.setDisable(true);
        tblCurrency.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            btnCurrencySave.setDisable(newSelection == null);
        });
    }

    public void searchInCurrencyCodes() {
        txtSearchCurrency.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(currency -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return currency.getCurrencyCode().toLowerCase().startsWith(lowerCaseFilter);
            });
        });
    }


    public void setupTable() {
        try {
            colCurrencyId.setCellValueFactory(new PropertyValueFactory<>("currencyId"));
            colCurrencyCodes.setCellValueFactory(new PropertyValueFactory<>("currencyCode"));
            colCurrencyRate.setCellValueFactory(new PropertyValueFactory<>("currencyRate"));
        } catch (Exception e) {
            displayMessage("Error setting up the currency table: " + e.getMessage(), true);
        }

    }

    private void setRegexValidationForTextFields(TextField textField){
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*([\\.,]\\d*)?")) {
                textField.setText(oldValue);
            }
        });
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
            tblCurrency.refresh();
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
