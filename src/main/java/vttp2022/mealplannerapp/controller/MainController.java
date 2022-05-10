package vttp2022.mealplannerapp.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/index")
    public String getIndex(HttpSession sess, Model model){
        
        model.addAttribute("user", sess.getAttribute("username"));
        model.addAttribute("userId", sess.getAttribute("userId"));
        return "index";
    }
    
}
