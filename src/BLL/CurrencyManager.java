package BLL;

import BE.Country;
import BE.Currency;
import CustomExceptions.ApplicationWideException;
import DAL.ICurrencyDataAccess;

import java.io.IOException;
import java.util.List;

public class CurrencyManager {

    private final ICurrencyDataAccess currencyDAO;

    public CurrencyManager() throws IOException {
        currencyDAO = new DAL.Currency_DAO();
    }


    public List<Currency> getAllCurrencies() throws ApplicationWideException {
        return currencyDAO.getAllCurrencies();
    }

    public void setCurrency(Currency selectedCurrency) throws ApplicationWideException {
        currencyDAO.setCurrency(selectedCurrency);
    }

}
