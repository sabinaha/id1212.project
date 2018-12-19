package server.integration;

import server.exceptions.GetStatsException;
import server.exceptions.IncorrectCredentialsException;
import server.exceptions.UpdateStatsException;
import server.exceptions.UserAlreadyExistsException;
import shared.UserCredential;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;

public class DB {
    private static final DB database = new DB();
    private Connection connection;

    private DB(){
        String osName = System.getProperty("os.name");
        final String URL;
        final String driver;
        if (osName.equals("Windows 10")) {
            URL = "jdbc:mysql://localhost:3306/RPS?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            driver = "com.mysql.cj.jdbc.Driver";
        } else {
            URL = "jdbc:mysql://localhost:8889/RPS?autoReconnect=true&useSSL=false";
            driver = "com.mysql.jdbc.Driver";
        }

        String[] DB_Credentials = get_DBCredentials();
        final String MYSQL_USERNAME = DB_Credentials[0];
        final String MYSQL_PASSWORD = DB_Credentials[1];

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(URL, MYSQL_USERNAME, MYSQL_PASSWORD);
            connection.setAutoCommit(true);
        } catch (Exception e) {
            System.out.println("Database connection error!");
            e.printStackTrace();
        }
    }
    private String[] get_DBCredentials() {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(".."+ File.separator+ "db_credentials.txt"));
        } catch (IOException e) {
            System.out.println("Could not read read word from file.");
            e.printStackTrace();
        }
        return new String[] {lines.get(0), lines.get(1)};
    }

    public boolean userExists(UserCredential usr) {
        ResultSet rs;
        PreparedStatement ps;

        String nameParam = usr.getUsername();


        String query = "SELECT id FROM user WHERE username = ?";
        boolean exists = false;
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, nameParam);

            rs = ps.executeQuery();
            exists = rs.next();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (exists) {
            return true;
        }
        return false;
    }

    public void registerUser(UserCredential usr) throws UserAlreadyExistsException{
        if(userExists(usr))
            throw new UserAlreadyExistsException();

        String nameParam = usr.getUsername();
        String passParam = usr.getPassword();

        String query = "INSERT INTO user (id, username, password) VALUES (NULL, ?, ?);";

        int rs = 0;
        PreparedStatement ps;
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, nameParam);
            ps.setString(2, passParam);
            rs = ps.executeUpdate();
            if (rs <= 0)
                throw new UserAlreadyExistsException();
        }catch (SQLException e){
            throw new UserAlreadyExistsException(e);
        }
        try {
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void login(UserCredential usr) throws IncorrectCredentialsException {
        PreparedStatement ps;

        String nameParam = usr.getUsername();
        String passParam = usr.getPassword();

        String query = ("SELECT id FROM user WHERE username = ? AND password = ?");

        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, nameParam);
            ps.setString(2, passParam);
            rs = ps.executeQuery();
            if (!rs.next())
                throw new IncorrectCredentialsException();
        }catch (SQLException e){
            e.printStackTrace();
            throw new IncorrectCredentialsException(e);
        }
    }
    public void updateWin(String username) throws UpdateStatsException {
        updateStats(username, true);
    }
    public void updateLoss(String username) throws UpdateStatsException {
        updateStats(username, false);
    }

    private void updateStats(String username, Boolean isWin) throws UpdateStatsException {
        PreparedStatement ps;
        String query;
        if (isWin) {
            query = ("UPDATE stats SET wins = wins + 1 WHERE userID = (SELECT id FROM user WHERE username = ?)");
        }else{
            query = ("UPDATE stats SET losses = losses + 1 WHERE userID = (SELECT id FROM user WHERE username = ?)");
        }
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UpdateStatsException(e);
        }

    }

    public int getWins(String username) throws GetStatsException {
        return getStats(username, true);
    }
    public int getloss(String username) throws GetStatsException {
        return getStats(username, false);
    }

    private int getStats(String username, boolean isWin) throws GetStatsException {
        PreparedStatement ps;
        ResultSet rs;
        String query;
        if (isWin) {
            query = ("SELECT wins FROM stats WHERE userID = (SELECT id FROM user WHERE username = ?");
        }else{
            query = ("SELECT losses FROM stats WHERE userID = (SELECT id FROM user WHERE username = ?");
        }

        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, username);
            rs = ps.executeQuery();

            if (!rs.next()){
                throw new GetStatsException();
            }
            return rs.getInt(0);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GetStatsException(e);
        }

    }
}
