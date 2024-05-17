package DAL;

import BE.Currency;
import BE.Geography;
import CustomExceptions.ApplicationWideException;
import DAL.DBConnector.DBConnector;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Currency_DAO implements ICurrencyDataAccess {

    private final DBConnector dbConnector;

    public Currency_DAO() throws ApplicationWideException {
        try {
            dbConnector = new DBConnector();
        } catch (IOException e) {
            throw new ApplicationWideException("Failed to initialize the database connector",e);
        }
    }

    @Override
    public List<Currency> getAllCurrencies() throws ApplicationWideException {
        List<Currency> allCurrencies = new ArrayList<>();
        String sql = "SELECT * FROM Currency;";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Currency currency = new Currency(
                        rs.getInt("CurrencyId"),
                        rs.getString("CurrencyCode"),
                        rs.getDouble("CurrencyRate")
                );
                allCurrencies.add(currency);
            }
        } catch (SQLException e) {
            throw new ApplicationWideException("Failed to get currencies", e);
        }
        return allCurrencies;
    }

    public void setCurrency(Currency selectedCurrency) throws ApplicationWideException {
        String sql = "UPDATE Currency SET CurrencyRate = ? WHERE CurrencyId = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, selectedCurrency.getCurrencyRate());
            pstmt.setInt(2, selectedCurrency.getCurrencyId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ApplicationWideException("Failed to update currency", e);
        }
    }
}
