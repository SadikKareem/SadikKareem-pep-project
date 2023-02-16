package Controller;

import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */

     AccountService accountService = new AccountService();
     MessageService messageService = new MessageService() ;
     
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        //app.start(8080);
        app.get("example-endpoint", this::exampleHandler);
      

        app.post("/register", this:: NewUserHandler);
        app.post("/login", this:: LoginHandler);

        app.get("/messages", this::AllMessagesHandler); 
        app.post("/messages", this::addedMessageHandler);
        app.get("/messages/{message_id}", this:: getMessageBymessageIdHandler);

        app.get("/accounts{account_id}/message", this::messageByAccountIdHandler);
        
        app.patch("/messages/{message_id}", this::updateMessageByMessageIdHandler);
        app.delete("/messages/{message_id}", this:: deleteMessageHandler);





        /* 
        
        
        */
        
       // app.start(8080);
        

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void NewUserHandler(Context ctx) throws  JsonProcessingException, SQLException  {
        //System.out.println("we are here in register");

        ObjectMapper om = new ObjectMapper();
        Account myAccount = om.readValue(ctx.body(), Account.class);
        Account createdAccount = accountService.addAccount(myAccount);
        if(createdAccount != null){
            //System.out.println("new user handler");
            //ctx.json("register handler here");
            ctx.json("register handler here");

            ctx.json(om.writeValueAsString(createdAccount));
            ctx.status(200);
        }else{

            ctx.status(400);

        }
    }
    private void LoginHandler(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(ctx.body(), Account.class);
        Account accountFound = accountService.processLogin(account);
        if(accountFound != null){
            ctx.json(om.writeValueAsString(accountFound));
            ctx.status(200);
        }else{
            ctx.json(om.writeValueAsString(accountFound));
            ctx.status(401);
        }
        
    }

    //to get all messages from a specific account
     private void messageByAccountIdHandler(Context ctx) throws SQLException {
        int accountId = ctx.pathParamAsClass("account_id",Integer.class).get();
        List<Message> allMessages = messageService.getMessagesByAccountId(accountId);
        ctx.json(allMessages);
        ctx.status(200);
        /*if(allMessages != null){
            ctx.json(allMessages);
            ctx.status(200);
        }else{
            ctx.status(400);
        }*/
    }

    private void addedMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Message message = om.readValue(ctx.body(), Message.class);
        Message newMessage = messageService.addMessages(message);
        if(newMessage != null){
            ctx.status(200);
            ctx.json(om.writeValueAsString(newMessage));

        }else{
            ctx.status(400);
        }
        
    }

    //to get all the messages

    private void AllMessagesHandler(Context ctx) throws SQLException, JsonMappingException, JsonProcessingException { 
        List<Message> messages = messageService.getMessages();
            ctx.status(200);
            ctx.json(messages);
        
    }



    private void getMessageBymessageIdHandler(Context ctx) throws JsonMappingException, JsonProcessingException, SQLException {
       //int messageId = ctx.pathParamAsClass(ctx.body(), Integer.class).get();
       //List<Message> allMessages = messageService.getMessageById(messageId);
       //ctx.json(allMessages);
       int messageId = Integer.parseInt(ctx.pathParam("message_id"));
       ctx.json(messageService.getMessageById(messageId));
       ctx.status(200);



    }
    //## 7: Our API should be able to update a message text identified by a message ID.

     private void updateMessageByMessageIdHandler(Context ctx) throws JsonMappingException, JsonProcessingException, SQLException {


        ObjectMapper om = new ObjectMapper();
        Message message = om.readValue(ctx.body(), Message.class);
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        if(messageService.getMessageById(messageId) == null){
            ctx.status(400);
        }
        Message updateMessage = messageService.updateMessageById(messageId, message);
        if(updateMessage != null){
            ctx.status(200);
            ctx.json(om.writeValueAsString(updateMessage));

        }else{
            ctx.status(400);
        }
        


    }

    private void deleteMessageHandler(Context ctx) throws SQLException {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deleteMessage = messageService.deleteMessageById(messageId);
        if(deleteMessage != null){
            ctx.status(200);
            ctx.json(deleteMessage);
        }else{
            ctx.status(200);
        }
    }

  
}

    





    
    

