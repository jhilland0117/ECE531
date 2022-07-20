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

    public Console getConsole(String id) {
        Console console = null;

        String select = "select " + id + " from console";

        try ( Connection conn = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/consoles", "root", "Brady#2019");  PreparedStatement preparedStatement = conn.prepareStatement(select)) {

            ResultSet result = preparedStatement.executeQuery();
            Console obj = new Console();
            console.setId(result.getLong("id"));
            console.setName(result.getString("name"));
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return console;
    }

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

    public String addConsole(String name) {
        String insert = "insert into consoles (name) values (" + name + ")";
        Connection conn = null;
        Statement statement = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/consoles", "root", "Brady#2019");
            statement = (Statement) conn.createStatement();
            statement.execute(insert);
            System.out.println("adding console to database");
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            return "FAILED POST\n";
        } catch (Exception e) {
            return "FAILED POST\n";
        }
        return "SUCCESS POST\n";
    }

    public void updateConsole(String id, String name) {

    }

    public void removeConsole(String id) {

    }

}
