package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import Model.Account;
import Util.ConnectionUtil;

public class AccountDao {

    Connection conn = ConnectionUtil.getConnection();


    public AccountDao(Connection conn){
        this.conn = conn;
    }
    /**
     * @param account
     * @return
     * @throws SQLException
     */
    public boolean addAccount(Account account) throws SQLException{
     Connection conn = ConnectionUtil.getConnection();

     try {
         if (account.getUsername().isEmpty() || account.getPassword().length() <= 4) {
             System.out.println("Invalid username or password");
             return false;
         }
 
         PreparedStatement ps = conn.prepareStatement("INSERT INTO account (username, password) VALUES (?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
         ps.setString(1, account.getUsername());
         ps.setString(2, account.getPassword());
 
         int valuesAdded = ps.executeUpdate();
 
         if (valuesAdded > 0) {
             ResultSet generatedKey = ps.getGeneratedKeys();
             if (generatedKey != null && generatedKey.next()) {
                 account.setAccount_id(generatedKey.getInt(1));
                 return true;
             }
         }
     }catch(SQLException e){
        e.printStackTrace();
     }
     return false;
 }
   

    public Account getAccountByUsernameAndPassword(String username, String password) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        Account account = null;
    
        try {
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int accountId = rs.getInt("account_id");
                String user = rs.getString("username");
                String pass = rs.getString("password");
                account = new Account(accountId, user, pass);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
    
        return account;
    }
    

}
    
    

