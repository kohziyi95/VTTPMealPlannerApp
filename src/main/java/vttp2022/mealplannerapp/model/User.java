package vttp2022.mealplannerapp.model;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class User {
    private Logger logger = Logger.getLogger((User.class.getName()));

    private String userId;
    private String username;
    private String password;
    private String email;

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String generateUserId(){
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString().replace("-","").substring(0, 8);
        // logger.log(Level.INFO, "userId >>>>>>>> " + id);
        this.userId = id;
        return id;
    }

    
}
