package BLL;

import BE.Country;
import BE.Currency;
import DAL.ICurrencyDataAccess;

import java.io.IOException;
import java.util.List;

public class CurrencyManager {

    private final ICurrencyDataAccess currencyDAO;

    public CurrencyManager() throws IOException {
        currencyDAO = new DAL.Currency_DAO();
    }


    public List<Currency> getAllCurrencies() throws Exception {
        return currencyDAO.getAllCurrencies();
    }

    public void setCurrency(Currency selectedCurrency) {
        currencyDAO.setCurrency(selectedCurrency);
    }
}
