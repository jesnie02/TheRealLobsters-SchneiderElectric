package GUI.Model;

import BE.Currency;
import BLL.CurrencyManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;

public class CurrencyModel {


    private final CurrencyManager currencyManager;
    private ObservableList<Currency> currencies;


    public CurrencyModel() throws IOException {
        currencyManager = new CurrencyManager();
        loadCurrencies();

    }

    private void loadCurrencies() {
        try {
            List<Currency> currencyList = currencyManager.getAllCurrencies();
            this.currencies = FXCollections.observableArrayList(currencyList);
        } catch (Exception e) {
            // Handle errors more appropriately, possibly logging them or notifying the user
            System.err.println("Error loading currencies: " + e.getMessage());
            this.currencies = FXCollections.observableArrayList();
        }
    }

    public ObservableList<Currency> getCurrencies() {
        return this.currencies;
    }


    public void setCurrencyDatabase(Currency selectedCurrency) {
        for (Currency currency : getCurrencies()) {
            if (currency.getCurrencyId() == selectedCurrency.getCurrencyId()) {
                // Update the rate of the existing currency
                currency.setCurrencyRate(selectedCurrency.getCurrencyRate());
                break;
            }
        }

        // Update the currency in the database
        currencyManager.setCurrency(selectedCurrency);
    }


}
