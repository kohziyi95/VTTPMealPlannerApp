package vttp2022.mealplannerapp.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import vttp2022.mealplannerapp.repository.LoginRepository;
import vttp2022.mealplannerapp.service.LoginService;

@Controller
@RequestMapping("/user")
public class LoginController {
    
    @Autowired
    private LoginService svc;

    @Autowired
    private LoginRepository repo;

    @GetMapping("/login")
    public String getLogin(){
        return "loginUser";
    }
    
    @PostMapping(path="/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView postLogin(@RequestBody MultiValueMap<String, String> payload, HttpSession sess){
        ModelAndView mvc = new ModelAndView();

        String username = payload.getFirst("username");
        String password = payload.getFirst("password");
        String userId = null;
        try {
            userId = svc.authenticateUser(username, password);
            // if(userId == null){
            //     throw new Exception();
            // }
        } catch (Exception e) {
            
            mvc.setViewName("loginUser");
            mvc.setStatus(HttpStatus.UNAUTHORIZED);
            mvc.addObject("error", "Error.");
            String errorMessage = e.getMessage();
            if(repo.getUserCountbyUsername(username) == 0){
                errorMessage = "Username not found. Please try again.";
            } else {
                errorMessage = "Wrong password. Please try again.";
            }

            mvc.addObject("message", "Unable to log in. " +  errorMessage);
            return mvc;    
        }
        mvc.setViewName("loginStatus");
        mvc.addObject("username", "Welcome " + username + "!");
        mvc.addObject("message", "Log In Successful! Redirecting in ...");
        sess.setAttribute("username", username);
        sess.setAttribute("userId", userId);
        return mvc;
    }


    @GetMapping("/logout")
    public String getLogout(HttpSession sess){
        sess.invalidate();
        return "index";
    }

    @GetMapping(path="/register")
    public String getRegister(){
        return "registerUser";
    }


    @PostMapping(path="/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView postRegister(@RequestBody MultiValueMap<String, String> payload){
        ModelAndView mvc = new ModelAndView();

        String username = payload.getFirst("username");
        String password = payload.getFirst("password");
        String verifyPassword = payload.getFirst("verify_password");
        String email = payload.getFirst("email");
        try {
            if(!password.equals(verifyPassword)){
                throw new Exception("Passwords do not match. Please try again.");
            }
            boolean registerStatus = svc.createUser(username, password, email);
            if (!registerStatus) {
                throw new Exception("Unknown error occured. Please try again.");
            }
        } catch (Exception e) {
            mvc.setStatus(HttpStatus.BAD_REQUEST);
            mvc.setViewName("registerUser");
            mvc.addObject("error", "Error.");
            String errorMessage = e.getMessage();
            mvc.addObject("message", "Registeration failed. " +  errorMessage);
            return mvc;    
        }
        mvc.setViewName("registerStatus");
        mvc.addObject("message", "You have successfully registered. Please log in to continue.");
        return mvc;
    }
}
