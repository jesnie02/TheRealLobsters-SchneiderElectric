package BE;

public class Currency {

    private String currencyCode;
    private double currencyRate;
    private int CurrencyId;



    public Currency(String currencyCode, double currencyRate) {
        this.currencyCode = currencyCode;
        this.currencyRate = currencyRate;
    }

    public Currency(int CurrencyId, String currencyCode, double currencyRate) {
        this.CurrencyId = CurrencyId;
        this.currencyCode = currencyCode;
        this.currencyRate = currencyRate;

    }

    public Currency(int CurrencyId, double currencyRate) {
        this.CurrencyId = CurrencyId;
        this.currencyRate = currencyRate;
    }


    public int getCurrencyId() {
        return CurrencyId;
    }

    private void setCurrencyId(int currencyId) {
        CurrencyId = currencyId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public double getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(double currencyRate) {
        this.currencyRate = currencyRate;
    }

    @Override
    public String toString() {
        return currencyCode;
    }
}
