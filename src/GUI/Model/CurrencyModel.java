package GUI.Model;

import BE.Currency;
import BLL.CurrencyManager;
import CustomExceptions.ApplicationWideException;
import GUI.Utility.ExceptionHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class CurrencyModel {


    private final CurrencyManager currencyManager;
    private ObservableList<Currency> currencies;


    public CurrencyModel() throws ApplicationWideException {
        currencyManager = new CurrencyManager();
        loadCurrencies();

    }

    private void loadCurrencies() {
        try {
            List<Currency> currencyList = currencyManager.getAllCurrencies();
            this.currencies = FXCollections.observableArrayList(currencyList);
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
            this.currencies = FXCollections.observableArrayList();
        }
    }

    public ObservableList<Currency> getCurrencies() {
        return this.currencies;
    }


    public void setCurrency(Currency selectedCurrency) {
        try {
            currencyManager.setCurrency(selectedCurrency);
        } catch (ApplicationWideException e) {
            ExceptionHandler.handleException(e);
        }
    }
}
