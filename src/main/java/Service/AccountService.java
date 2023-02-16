package Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DAO.AccountDao;
import Model.Account;

public class AccountService {
    private AccountDao accountDao ;
    private List<Account> theAccounts;

    //a constractor
    public AccountService(){
        accountDao = new AccountDao(null);
        theAccounts =  new ArrayList<>();

    }

    public Account addAccount(Account account) throws SQLException {

        boolean accountAdded = accountDao.addAccount(account);

        if (accountAdded) {
            theAccounts.add(account);
            return account;
        }
    
        return null;
    }
 

    public Account processLogin(Account account) throws SQLException {
        String username = account.getUsername();
        String password = account.getPassword();
        Account authenticatedAccount = accountDao.getAccountByUsernameAndPassword(username, password);
    
        if (authenticatedAccount != null) {
            return authenticatedAccount;
        } else {
            return null;
        }
        
    }

    
    
}

