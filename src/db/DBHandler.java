package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 *
 * @author marufur
 */
public class DBHandler {
    Connection db_con;

    public DBHandler() {
        try {
            createMySqlConnection();	
	} catch (Exception e) {
            System.out.println("--------XXXXXXX--------");
            System.out.println("error in " + getClass().getName() + " : error is :" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void createMySqlConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            db_con = DriverManager.getConnection("jdbc:mysql://localhost/mydb", "root", "nopass");

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }	
    
    public Connection getDb_con() {
            return db_con;
    }
        
    public boolean closeConnection(){
        try {
            System.out.println("Closing Database Connection:");
            db_con.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
