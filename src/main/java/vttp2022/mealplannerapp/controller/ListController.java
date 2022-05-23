package vttp2022.mealplannerapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
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

import vttp2022.mealplannerapp.model.Recipe;
import vttp2022.mealplannerapp.service.IngredientService;
import vttp2022.mealplannerapp.service.RecipeService;

@Controller
@RequestMapping(path = "/list")
public class ListController {
    private Logger logger = Logger.getLogger(ListController.class.getName());

    @Autowired
    private RecipeService svc;

    @Autowired
    private IngredientService ingredientSvc;

    @GetMapping(path = "/{userId}/myrecipes")
    public ModelAndView getMyRecipes (
            @PathVariable String userId,
            HttpSession sess) {
        
        if (!userId.equals((String)sess.getAttribute("userId"))){
            ModelAndView mvc = new ModelAndView("index", HttpStatus.FORBIDDEN);
            return mvc;
        }

        ModelAndView mvc = new ModelAndView("savedRecipes");

        List<Recipe> savedRecipes = new ArrayList<>();
        try {
            savedRecipes = svc.getSavedRecipes(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mvc.addObject("savedRecipes", savedRecipes);
        mvc.addObject("user", sess.getAttribute("username"));
        mvc.addObject("userId", userId);
        return mvc;
    }

    @PostMapping(path = "/{userId}/myrecipes")
    public ModelAndView saveIngredients (
            @PathVariable String userId,
            @RequestParam boolean saveIngredients,
            @RequestParam String username,
            @RequestParam int recipeIndex,
            HttpSession sess) {
        
        if (!userId.equals((String)sess.getAttribute("userId"))){
            ModelAndView mvc = new ModelAndView("index", HttpStatus.FORBIDDEN);
            return mvc;
        }

        List<Recipe> savedRecipes = new ArrayList<>();
        try {
            savedRecipes = svc.getSavedRecipes(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Recipe recipe = savedRecipes.get(recipeIndex);

        ModelAndView mvc = new ModelAndView("savedRecipes");
        try {
            if (ingredientSvc.saveIngredients(recipe, userId)){
                mvc.addObject("message", "Ingredients Saved.");
            }
        } catch (Exception e1) {
            logger.log(Level.WARNING, e1.getMessage());
            e1.printStackTrace();
            mvc.addObject("message", e1.getMessage());
            mvc.setStatus(HttpStatus.BAD_REQUEST);
        }

        mvc.addObject("savedRecipes", savedRecipes);
        mvc.addObject("user", sess.getAttribute("username"));
        mvc.addObject("userId", userId);
        return mvc;
    }
    
    @PostMapping(path = "/{userId}/delete")
    public ModelAndView postDeleteRecipe (
        @RequestParam int recipeId, 
        @RequestParam String username,
        @PathVariable String userId, 
        HttpSession sess) {
         
        try {
            svc.deleteSavedRecipes(recipeId, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ModelAndView mvc = new ModelAndView("savedRecipes");

        List<Recipe> savedRecipes = new ArrayList<>();
        try {
            savedRecipes = svc.getSavedRecipes(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sess.setAttribute("userId", userId);
        sess.setAttribute("username", username);

        mvc.addObject("savedRecipes", savedRecipes);
        mvc.addObject("user", sess.getAttribute("username"));
        mvc.addObject("userId", userId);
        return mvc;
    }

}
