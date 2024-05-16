package DAL;

import BE.Currency;

import java.util.List;

public interface ICurrencyDataAccess {
    List<Currency> getAllCurrencies();

    void setCurrency(Currency selectedCurrency);
}
