package ro.ubbcluj.map.mavenfx2.repository;


import ro.ubbcluj.map.mavenfx2.domain.FriendRequest;
import ro.ubbcluj.map.mavenfx2.domain.validators.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FriendRequestRepository implements Repository<Long, FriendRequest> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<FriendRequest> validator;

    public FriendRequestRepository(String url, String username, String password, Validator<FriendRequest> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public FriendRequest findOne(Long aLong) {
        FriendRequest friendRequest = null;
        String sql = "select * from friend_requests where id_friend_request = ?";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            Long id = resultSet.getLong("id_friend_request");
            Long from = resultSet.getLong("id_from");
            Long to = resultSet.getLong("id_to");
            String status = resultSet.getString("status");

            friendRequest = new FriendRequest(from, to, status);
            friendRequest.setId(id);

        }catch (SQLException e){
            e.printStackTrace();
        }

        return friendRequest;
    }

    @Override
    public Iterable<FriendRequest> findAll() {
        List<FriendRequest> friendRequests = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from friend_requests");
            ResultSet resultSet = statement.executeQuery()){

            while(resultSet.next()){
                Long id = resultSet.getLong("id_friend_request");
                Long from = resultSet.getLong("id_from");
                Long to = resultSet.getLong("id_to");
                String status = resultSet.getString("status");

                FriendRequest friendRequest = new FriendRequest(from, to, status);
                friendRequest.setId(id);

                friendRequests.add(friendRequest);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return friendRequests;
    }

    @Override
    public void save(FriendRequest entity) {
        String sql = "insert into friend_requests(id_from, id_to, status) values(?, ?, ?)";
        validator.validate(entity);
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setLong(1, entity.getFrom());
            statement.setLong(2, entity.getTo());
            statement.setString(3, entity.getStatus());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long aLong) {
        String sql = "delete from friend_requests where id_friend_request = ?";

        try(Connection connection = DriverManager.getConnection(url, username,password);
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setLong(1, aLong);
            statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public Long getSize() {
        Long numberOfElem = null;

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select count(id_friend_request) from friend_requests");
            ResultSet resultSet = statement.executeQuery()){

            resultSet.next();
            numberOfElem = resultSet.getLong("count");

            return numberOfElem;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numberOfElem;
    }
}
