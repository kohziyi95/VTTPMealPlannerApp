package vttp2022.mealplannerapp.repository;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import vttp2022.mealplannerapp.model.User;

@Repository
public class LoginRepository {
    private static Logger logger = Logger.getLogger(LoginRepository.class.getName());

    @Autowired
    private JdbcTemplate template;

    private final static String SQL_INSERT_USER = 
        "insert into user (user_id, username, password, email) values (?, ?, sha1(?), ?)";

    private final static String SQL_GET_USER_COUNT_BY_ID = 
        "select count(*) from user where user_id = ?";

    private final static String SQL_GET_USER_COUNT_BY_USERNAME = 
        "select count(*) from user where username = ?";

    private final static String SQL_GET_USER_COUNT_BY_EMAIL = 
        "select count(*) from user where email = ?";
    
    private final static String SQL_AUTHENTICATE_USER = 
        "select user_id from user where username = ? and password = sha1(?)";

    public boolean insertUser(User user){
        int added = template.update(SQL_INSERT_USER, 
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail());
        return added > 0;
    }

    public int getUserCountbyId(String userId){
        int userCount = template.queryForObject(SQL_GET_USER_COUNT_BY_ID, Integer.class, userId);
        return userCount;
    }

    public int getUserCountbyUsername(String username){
        int userCount = template.queryForObject(SQL_GET_USER_COUNT_BY_USERNAME, Integer.class, username);
        return userCount;
    }

    public int getUserCountbyEmail(String email){
        int userCount = template.queryForObject(SQL_GET_USER_COUNT_BY_EMAIL, Integer.class, email);
        return userCount;
    }

    public String getUserIdbyUsernameAndPassword(String username, String password){
        String userId = template.queryForObject(SQL_AUTHENTICATE_USER, String.class, username, password);
        return userId;
    }



} 
