package DAL;

import BE.Currency;
import CustomExceptions.ApplicationWideException;

import java.util.List;

public interface ICurrencyDataAccess {
    List<Currency> getAllCurrencies() throws ApplicationWideException;

    void setCurrency(Currency selectedCurrency) throws ApplicationWideException;
}
