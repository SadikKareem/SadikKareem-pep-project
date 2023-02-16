package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDao {
     Connection conn = ConnectionUtil.getConnection();

    public MessageDao(Connection conn){
        this.conn = conn;
    }

    /**
     * @param message
     * @return
     */

    public boolean addMessage(Message message) {
        Connection conn = ConnectionUtil.getConnection();
    
        try {
            String query = "INSERT INTO message (posted_by, message_text, time_posted_epoch) " +
                "VALUES (?, ?, ?)";
            PreparedStatement st = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            st.setInt(1, message.getPosted_by());
            st.setString(2, message.getMessage_text());
            st.setLong(3, message.getTime_posted_epoch());
            st.executeUpdate();
            ResultSet resultSet = st.getGeneratedKeys();
            if (resultSet.next()) {
                int theMessage_id = (int) resultSet.getLong(1);
                message.setMessage_id(theMessage_id);
                return true;
            } 
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    
        return false;
    }
    
    
    //to get the message in the database using a specific message_id

    public Message getMessageById(int messageId) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        
        try{
            String sql = "Select * from message where message_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
                statement.setInt(1, messageId);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    int postedBy = rs.getInt("posted_by");
                    String messageText = rs.getString("message_text");
                    long timePostedEpoch = rs.getLong("time_posted_epoch");
                    //messages.add(new Message(messageId, postedBy, messageText, timePostedEpoch));
                    Message message = new Message(messageId, postedBy, messageText, timePostedEpoch);
                    return message;
                }else{
                    return null;
                }
         
            
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;

    }
    public List<Message> getMessagesbyAccountId(int account_id) {
        Connection conn = ConnectionUtil.getConnection();

        List<Message> messages = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM message WHERE account_id = ?");
            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int message_id = rs.getInt("message_id");
                String message_text = rs.getString("message_text");
                messages.add(new Message(message_id, message_text, account_id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

   //to get all the messages in the database
   public List<Message> getAllMessages() throws SQLException {
    Connection conn = ConnectionUtil.getConnection();

    String sql = "SELECT * FROM message";
    try (PreparedStatement statement = conn.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<Message> messages = new ArrayList<>();
            while (resultSet.next()) {
                int messageId = resultSet.getInt("message_id");
                int postedBy = resultSet.getInt("posted_by");
                String messageText = resultSet.getString("message_text");
                long timePostedEpoch = resultSet.getLong("time_posted_epoch");
                 messages.add(new Message(messageId, postedBy, messageText, timePostedEpoch));
            }
            return messages;
        }
    }

    //to delete a specific message using message_id
    public Message deleteMessagebyId(int messageId) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();

        String sql = "DELETE FROM message WHERE message_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, messageId);
            statement.executeUpdate();
        }
        return null;
    }

    //To update a message text identified by message_id
  

    
    public Message updateMessageById (int message_id, Message messageText) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
        //String sql = "Update message Set message_text ? where message_id = ?";
        try(PreparedStatement st = conn.prepareStatement(sql)){
            st.setString(1, messageText.getMessage_text());
            st.setInt(2, message_id);
            st.executeUpdate();
        }
        return messageText;

    }
 
}


    

