package DAL;

import BE.Country;
import BE.Geography;
import DAL.DBConnector.DBConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Geography_DAO implements IGeographyDataAccess {

    private final DBConnector dbConnector;

    public Geography_DAO() throws Exception{
        dbConnector = new DBConnector();
    }


    @Override
    public List<Geography> getAllGeographies() throws Exception {
        List<Geography> allGeographies = new ArrayList<>();
        try(Connection conn = dbConnector.getConnection();
            Statement stmt = conn.createStatement()){

            String sql = "SELECT * FROM Geography";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                Geography geography = new Geography(
                        rs.getInt("GeographyId"),
                        rs.getString("GeographyName")
                );
                allGeographies.add(geography);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return allGeographies;
    }





    @Override
    public List<Geography> getSumsAndAveragesForGeographies() {
        List<Geography> allGeographies = new ArrayList<>();
        try(Connection conn = dbConnector.getConnection();
            Statement stmt = conn.createStatement()){

            String sql = """
                        SELECT
                                g.GeographyId,
                                g.GeographyName,
                                SUM(p.HourlySalary) AS TotalHourlyRate,
                                AVG(p.HourlySalary) AS AvgHourlyRate,
                                SUM(p.DailyRate) AS TotalDailyRate,
                                AVG(p.DailyRate) AS AvgDailyRate,
                                COUNT(p.ProfileId) AS ProfileCount
                            FROM
                                Geography g
                            JOIN
                                GeographyCountry gc ON g.GeographyId = gc.GeographyId
                            JOIN
                                Country c ON gc.CountryId = c.CountryId
                            JOIN
                                Profile p ON c.CountryId = p.Country
                            GROUP BY
                                g.GeographyId, g.GeographyName;
                            
                    """;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                Geography geography = new Geography(
                        rs.getInt("GeographyId"),
                        rs.getString("GeographyName"),
                        rs.getDouble("TotalHourlyRate"),
                        rs.getDouble("AvgHourlyRate"),
                        rs.getDouble("TotalDailyRate"),
                        rs.getDouble("AvgDailyRate"),
                        rs.getInt("ProfileCount")
                );
                allGeographies.add(geography);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return allGeographies;
    }
}
