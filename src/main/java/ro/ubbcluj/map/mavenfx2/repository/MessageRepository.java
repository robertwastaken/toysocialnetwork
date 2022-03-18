package ro.ubbcluj.map.mavenfx2.repository;


import ro.ubbcluj.map.mavenfx2.domain.Message;
import ro.ubbcluj.map.mavenfx2.domain.validators.Validator;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MessageRepository implements Repository<Long, Message>{
    private final String url;
    private final String username;
    private final String password;
    private final Validator<Message> validator;

    public MessageRepository(String url, String username, String password, Validator<Message> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Message findOne(Long aLong) {
        Message m = null;
        String sql = "select * from messages where id_message = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            //Get message
            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            //Get id, id_from, id_to, message, dateTime
            long id = resultSet.getLong("id_message");
            long idFrom = resultSet.getLong("id_from");
            long idTo = resultSet.getLong("id_to");
            long idReply = resultSet.getLong("id_reply");
            String message = resultSet.getString("message");
            LocalDate date = resultSet.getDate("date").toLocalDate();
            LocalTime time = resultSet.getTime("time").toLocalTime();
            LocalDateTime dateTime = LocalDateTime.of(date, time);

            //Create the Message and return it
            m = new Message(idFrom, idTo, message, dateTime, idReply);
            m.setId(id);
            return m;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return m;
    }

    @Override
    public Iterable<Message> findAll() {

        List<Message> messages = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from messages");
            ResultSet resultSet = statement.executeQuery()){

            while(resultSet.next()){
                long id = resultSet.getLong("id_message");
                messages.add(findOne(id));
            }
            return messages;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public void save(Message entity) {
        String sql = "insert into messages(id_from, id_to, id_reply, message, date, time) values(?, ?, ?, ?, ?, ?)";

        validator.validate(entity);

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setLong(1, entity.getIdFrom());
            statement.setLong(2, entity.getIdTo());
            if(entity.getIdReply() != null)
                statement.setLong(3, entity.getIdReply());
            else
                statement.setNull(3, Types.INTEGER);
            statement.setString(4, entity.getMessage());
            statement.setDate(5, Date.valueOf(entity.getDate().toLocalDate()));
            statement.setTime(6, Time.valueOf(entity.getDate().toLocalTime()));

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long aLong) {
        String sql = "delete from messages where id_message = ?";

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
            PreparedStatement statement = connection.prepareStatement("select count(id_message) from messages");
            ResultSet resultSet = statement.executeQuery()) {

            resultSet.next();
            numberOfElem = resultSet.getLong("count");

            return numberOfElem;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numberOfElem;
    }
}