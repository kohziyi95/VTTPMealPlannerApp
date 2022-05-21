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

import vttp2022.mealplannerapp.model.Recipe;
import vttp2022.mealplannerapp.service.RecipeService;

@Controller
@RequestMapping(path = "/list")
public class ListController {
    private Logger logger = Logger.getLogger(ListController.class.getName());

    @Autowired
    private RecipeService svc;

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

        // return "redirect:/list/" + userId + "/myrecipes";
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


    // @GetMapping(path = "/search/details")
    // public ModelAndView getRecipeDetails (@RequestParam int recipeIndex, HttpSession sess) {
    //     List<Recipe> recipeList = (List<Recipe>) sess.getAttribute("recipeList");
    //     Recipe recipe = recipeList.get(recipeIndex);

    //     ModelAndView mvc = new ModelAndView("recipeDetails");
    //     mvc.addObject("recipeIndex", recipeIndex);
    //     mvc.addObject("recipe", recipe);
    //     mvc.addObject("user", sess.getAttribute("username"));
    //     mvc.addObject("userId", sess.getAttribute("userId"));

    //     return mvc;
    // }

    // @PostMapping(path = "/search/details")
    // public ModelAndView postRecipeDetails (@RequestBody MultiValueMap<String,String> payload, HttpSession sess) {
    //     List<Recipe> recipeList = (List<Recipe>) sess.getAttribute("recipeList");
    //     ModelAndView mvc = new ModelAndView("recipeDetails");
    //     String userId = (String) sess.getAttribute("userId");

    //     int recipeIndex = Integer.parseInt(payload.getFirst("recipeIndex"));
    //     Recipe recipe = recipeList.get(recipeIndex);
    //     boolean saveRecipe = false;
    //     boolean saveIngredients = false;

    //     if (payload.containsKey("saveRecipe")){
    //         saveRecipe = Boolean.valueOf(payload.getFirst("saveRecipe"));
    //         try {
    //             if (svc.saveRecipe(recipe, userId)){
    //                 mvc.addObject("message", "Recipe Saved.");
    //             }
    //         } catch (Exception e) {
    //             logger.log(Level.WARNING, e.getMessage());
    //             mvc.addObject("message", "Recipe has already been saved.");
    //         };
    //     } else if (payload.containsKey("saveIngredients")){
    //         saveIngredients = Boolean.valueOf(payload.getFirst("saveIngredients"));
    //     }

    //     mvc.addObject("recipeIndex", recipeIndex);
    //     mvc.addObject("recipe", recipe);
    //     mvc.addObject("user", sess.getAttribute("username"));
    //     mvc.addObject("userId", sess.getAttribute("userId"));
    //     return mvc;
    // }
}
