package app.web.pavelk.native1;


import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Native1 {
    private static Connection connection;
    private static Statement statement;

    public static void main(String[] args) throws SQLException {
        connect();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String next = scanner.next();
            if (next.equals("exit")) {
                try {
                    statement.close();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            } else if (next.equals("get")) {
                getAll();
            } else if (next.equals("add")) {
                add(scanner.next());
                getAll();
            } else {
                System.out.println(next);
            }

        }
    }

    public static void add(String s) throws SQLException {
        String query = "INSERT INTO nat (text) VALUES (?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, s);
            preparedStatement.executeUpdate();
        }
    }

    public static void getAll() throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT n.* FROM nat n ");
        while (resultSet.next()) {
            long id = resultSet.getLong(1);
            String string = resultSet.getString(2);
            System.out.println(id + " " + string);
        }
    }

    public static void connect() {
        try {
            String dbUrl = System.getenv("DB_URL");
            if (dbUrl == null) {
                dbUrl = System.getProperty("db");
                if (dbUrl == null) {
                    try (InputStream is = Native1.class.getClassLoader().getResourceAsStream("properties-from-pom.properties")) {
                        Properties properties = new Properties();
                        if (is != null) {
                            properties.load(is);
                            dbUrl = properties.getProperty("db");
                        }
                        if (dbUrl == null) {
                            dbUrl = "../native1.sqlite";
                        }
                    }
                }
            }
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbUrl);
            statement = connection.createStatement();
            System.out.println("connect db " + dbUrl);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                statement.close();
                connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

}
