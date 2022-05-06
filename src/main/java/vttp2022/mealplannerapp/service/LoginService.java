package vttp2022.mealplannerapp.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp2022.mealplannerapp.model.User;
import vttp2022.mealplannerapp.repository.LoginRepository;

@Service
public class LoginService {
    private static Logger logger = Logger.getLogger(LoginService.class.getName());

    @Autowired
    private LoginRepository repo;

    public boolean createUser (String username, String password, String email) throws Exception{
        User user = new User();
        user.generateUserId();
        while (repo.getUserCountbyId(user.getUserId()) == 1) 
            user.generateUserId();

        if (repo.getUserCountbyUsername(username) == 1){
            throw new Exception("Username already exists.");
        }
        user.setUsername(username);

        if (repo.getUserCountbyEmail(email) == 1){
            throw new Exception("Email already exists.");
        }
        user.setEmail(email);
        user.setPassword(password);
    
        boolean added = repo.insertUser(user);
        if (added) {
            logger.log(Level.INFO, "Username >>>>>>>>> " + user.getUsername());
            logger.log(Level.INFO, "User ID >>>>>>>>> " + user.getUserId());
        } else {
            logger.log(Level.WARNING, "Failed to create user.");
        }

        return added;
    }

    public boolean authenticateUser(String username, String password){
        return 1 == repo.getUserCountbyUsernameAndPassword(username, password);
    }

}
