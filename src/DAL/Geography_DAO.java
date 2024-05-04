package DAL;

import BE.Geography;
import DAL.DBConnector.DBConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Geography_DAO implements IGeography {

    private final DBConnector dbConnector;

    public Geography_DAO(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
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
                        rs.getString("Geography")
                );
                allGeographies.add(geography);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return allGeographies;
    }

}
