package ro.ubbcluj.map.mavenfx2.authentication;


import java.sql.*;

public class PasswordRepository {
    private final String url;
    private final String username;
    private final String password;
    private final PasswordAuthentication passwordAuthentication = new PasswordAuthentication();

    public PasswordRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public boolean authenticate(String username1, String password1) {
        String sql = "select * from passwords where username = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username1);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            return passwordAuthentication.authenticate(password1.toCharArray(),
                    resultSet.getString("password"));

        } catch (SQLException e) {
            //e.printStackTrace();
        }
        return false;
    }

    public void save(String username1, String password1, Long id_user) {
        String sql = "insert into passwords(username, password, id_user) values(?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {


            statement.setString(1, username1);
            statement.setString(2, password1);
            statement.setLong(3, id_user);

            statement.executeUpdate();
        } catch (SQLException e) {
            //e.printStackTrace();
        }
    }

    public Long getId(String username1) {
        String sql = "select * from passwords where username = ?";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, username1);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            return resultSet.getLong("id_user");
        }catch (SQLException e){
            //e.printStackTrace();
        }
        return null;
    }
}