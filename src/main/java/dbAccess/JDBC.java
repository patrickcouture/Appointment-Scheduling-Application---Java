package dbAccess;

import java.sql.Connection;
import java.sql.DriverManager;

/** This class is used to establish a connection to a MySQL database containing information for the scheduler program. */
public abstract class JDBC {

private static final String protocol = "jdbc";
private static final String vendor = ":mysql:";
private static final String location = "//localhost/";
private static final String databaseName = "client_schedule";
private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
private static final String userName = "sqlUser"; // Username
private static final String password = "Passw0rd!"; // Password
public static Connection connection;  // Connection Interface

        /** Opens a connection to the MySQL database.
         * @return connection to the specified database */
        public static Connection openConnection() {
                try {
                        Class.forName(driver); // Locate Driver
                        connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
                        System.out.println("Connection successful!");
                } catch(Exception e) {
                System.out.println("Error:" + e.getMessage());
                }
                return connection;
        }

        /** Closes the previously established MySQL connection. */
        public static void closeConnection() {
                try {
                        connection.close();
                        System.out.println("Connection closed!");
                } catch(Exception e) {
                        System.out.println("Error:" + e.getMessage());
                }
        }

        /** Gets the established MySQL connection.
         * @return connection to the specified database. */
        public static Connection getConnection() {

                return connection;
        }

}
