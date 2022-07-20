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

    // get request based on ID
    public Console getConsole(String id) {

        String select = "select * from console where id = '" + id + "'";
        try ( Connection conn = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/consoles", "root", "Brady#2019")) {

            Statement statement = conn.createStatement();

            ResultSet resultSet = statement.executeQuery(select);
            Console console = new Console();
            while (resultSet.next()) {
                console.setId(resultSet.getLong("ID"));
                console.setName(resultSet.getString("NAME"));
            }
            return console;
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
        }
        return null;
    }

    // get list of objects to fill a table
    public List<Console> getConsoles() {
        List<Console> consoles = new ArrayList<>();
        String select = "select * from console";

        try ( Connection conn = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/consoles", "root", "Brady#2019");  PreparedStatement preparedStatement = conn.prepareStatement(select)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                Console obj = new Console();
                obj.setId(resultSet.getLong("ID"));
                obj.setName(resultSet.getString("NAME"));

                consoles.add(obj);

            }

        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
        }
        return consoles;
    }

    // add a console to the database
    public String addConsole(String name) {
        String insert = "insert into console (name) values ('" + name + "')";
        System.out.println(insert);
        System.out.println("\n");
        try ( Connection conn = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/consoles", "root", "Brady#2019")) {
            Statement statement = (Statement) conn.createStatement();
            statement.execute(insert);
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
            return "FAILED POST\n";
        }
        return "SUCCESS POST\n";
    }

    // update an already existing console
    public void updateConsole(String id, String name) {

    }

    // delete console from database
    public void deleteConsoles(String id) {

    }

}
