package DAL;

import BE.Countries;
import DAL.DBConnector.DBConnector;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Country_DAO implements ICountryDataAccess {

    private final DBConnector dbConnector;

    public Country_DAO() throws IOException {
        dbConnector = new DBConnector();
    }

    @Override
    public List<Countries> getAllCountries() throws Exception {

        List<Countries> allCountries = new ArrayList<>();
        try(Connection conn = dbConnector.getConnection();
            Statement stmt = conn.createStatement()){

            String sql = "SELECT * FROM Country";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                Countries countries = new Countries(
                        rs.getInt("CountryId"),
                        rs.getString("CountryName")
                );
                allCountries.add(countries);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return allCountries;
    }
}
