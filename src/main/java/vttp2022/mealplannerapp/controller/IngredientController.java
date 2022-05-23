package vttp2022.mealplannerapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import vttp2022.mealplannerapp.model.Ingredient;
import vttp2022.mealplannerapp.model.Recipe;
import vttp2022.mealplannerapp.service.IngredientService;
import vttp2022.mealplannerapp.service.RecipeService;

@Controller
@RequestMapping(path = "/ingredient")
public class IngredientController {
    private Logger logger = Logger.getLogger(IngredientController.class.getName());

    @Autowired
    private IngredientService svc;

    @Autowired
    private RecipeService recipeSvc;

    @GetMapping(path = "/{userId}")
    public ModelAndView getSavedIngredients (
            @PathVariable String userId,
            HttpSession sess) {
        
        if (!userId.equals((String)sess.getAttribute("userId"))){
            ModelAndView mvc = new ModelAndView("index", HttpStatus.FORBIDDEN);
            return mvc;
        }

        ModelAndView mvc = new ModelAndView("savedIngredients");
        mvc.addObject("user", sess.getAttribute("username"));
        mvc.addObject("userId", userId);

        List<Ingredient> savedIngredients = new ArrayList<>();
        List<Recipe> recipeList = new ArrayList<>();

        try {
            savedIngredients = svc.getAllIngredients(userId);
            recipeList = recipeSvc.getSavedRecipes(userId);
        } catch (Exception e) {
            // e.printStackTrace();
            mvc.addObject("message", e.getMessage());
            return mvc;
        }
        mvc.addObject("recipeList", recipeList);
        mvc.addObject("savedIngredients", savedIngredients);
        return mvc;
    }
    
    @PostMapping(path = "/{userId}/delete")
    public ModelAndView postDeleteRecipe (
        @RequestParam int recipeId, 
        @RequestParam String username,
        @PathVariable String userId, 
        HttpSession sess) {
         
        try {
            svc.deleteIngredients(userId, recipeId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sess.setAttribute("userId", userId);
        sess.setAttribute("username", username);

        ModelAndView mvc = new ModelAndView("savedIngredients");
        mvc.addObject("user", sess.getAttribute("username"));
        mvc.addObject("userId", userId);

        List<Ingredient> savedIngredients = new ArrayList<>();
        try {
            savedIngredients = svc.getAllIngredients(userId);
        } catch (Exception e) {
            // e.printStackTrace();
            mvc.addObject("message", e.getMessage());
            return mvc;
        }

        mvc.addObject("savedIngredients", savedIngredients);
        return mvc;
    }

}
