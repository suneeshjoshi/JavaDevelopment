package com.suneesh.trading.database;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connection {

    public static void main(String[] args) {

        String url = "jdbc:h2:mem:";

        try (Connection con = (Connection) DriverManager.getConnection(url);
//             var stm = con.createStatement();
//             var rs = stm.executeQuery("SELECT 1+1")
        )
        {

            if (rs.next()) {

                System.out.println(rs.getInt(1));
            }

        } catch (SQLException ex) {

            var lgr = Logger.getLogger(JavaSeH2Memory.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
