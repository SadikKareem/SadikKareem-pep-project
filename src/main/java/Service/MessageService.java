package Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import DAO.MessageDao;
import Model.Message;

public class MessageService {
    private MessageDao messageDao;
    private List<Message> theMessages;
    //a constructor
    /**
     * 
     */
    public MessageService(){
        messageDao = new MessageDao(null);
        this.theMessages = new ArrayList<>();

    }
    public Message addMessages(Message newMessage){
        boolean messageAdded = messageDao.addMessage(newMessage);
        if (newMessage.getMessage_text() == null || newMessage.getMessage_text().isEmpty()) {
            return null; // Return null if message is empty or null
        }
        if(messageAdded){
            theMessages.add(newMessage);
            return newMessage;
        }
        //this.theMessages.add(newMessage);
        return null;
        

    }
    public List<Message> getMessages(){
        return this.theMessages;
    }

    public Message getMessageById(int id) throws SQLException {
        Message message = messageDao.getMessageById(id);

        if(message == null){
            return null;
        }
        return message;
    }

    
    public  Message deleteMessageById(int messageId) throws SQLException {
            Message deletedMessage = messageDao.deleteMessagebyId(messageId);
            if(deletedMessage != null){
            messageDao.deleteMessagebyId(messageId);
            theMessages.remove(deletedMessage);
            return deletedMessage;
            }
            return null;
        }
        

        /*for (int i = 0; i <theMessages.size(); i++) {
        Message message = theMessages.get(i);
        if (message.getMessage_id() == messageId) {
        theMessages.remove(i);
        return message;
        }
        }
        return null;
        }*/

        public Message updateMessageById(int messageId, Message message) throws SQLException {
            if(getMessageById(messageId) != null && message.message_text != "" && message.message_text.length()<= 255){
                return messageDao.updateMessageById(messageId, message);
            }
            //return ((MessageService) theMessages).updateMessageById(messageId, message);
            return null;
        }
        public List<Message> getMessagesByAccountId(int accountId) throws SQLException {
            List<Message> messages = messageDao.getMessagesbyAccountId(accountId);
            if (messages == null || messages.isEmpty()) {
                return Collections.emptyList(); // Return an empty list
            }
            return messages;
        }
    

}



