package com.hilland;

import com.hilland.domain.Console;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jhilland
 */
public class JDBCConnection {

    public List<Console> getConsoles() {
        List<Console> consoles = new ArrayList<>();
        String select = "select * from console";

        try ( Connection conn = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/consoles", "root", "Brady#2019");  PreparedStatement preparedStatement = conn.prepareStatement(select)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                long id = resultSet.getLong("ID");
                String name = resultSet.getString("NAME");

                Console obj = new Console();
                obj.setId(id);
                obj.setName(name);

                consoles.add(obj);

            }
            
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return consoles;
    }

}
