package vttp2022.mealplannerapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import vttp2022.mealplannerapp.model.Recipe;
import vttp2022.mealplannerapp.service.IngredientService;
import vttp2022.mealplannerapp.service.RecipeService;

@Controller
@RequestMapping(path = "/recipe")
public class RecipeController {
    private Logger logger = Logger.getLogger(RecipeController.class.getName());

    @Autowired
    private RecipeService recipeSvc;

    @Autowired
    private IngredientService ingredientSvc;

    @GetMapping(path = "/search")
    public ModelAndView getSearchRecipe (
            @RequestParam String query, 
            @RequestParam String cuisineType, 
            @RequestParam String mealType,
            HttpSession sess) {

        ModelAndView mvc = new ModelAndView();

        List<Recipe> recipeList = new ArrayList<>();
        String url = recipeSvc.getUrlStringByQuery(query, cuisineType, mealType);
        // if (nextPage) {
        //     url = svc.getNextPage(url);
        // }
        if (recipeSvc.getNextPage(url) == null) {
            mvc.addObject("lastPage", true);
        }

        recipeList = recipeSvc.searchRecipes(url);

        mvc.addObject("query", query);
        mvc.addObject("cuisineType", cuisineType);
        mvc.addObject("mealType", mealType);
        mvc.addObject("recipeList", recipeList);
        mvc.setViewName("recipeSearch");
        sess.setAttribute("recipeList", recipeList);
        sess.setAttribute("currentUrl", url);
        mvc.addObject("user", sess.getAttribute("username"));
        mvc.addObject("userId", sess.getAttribute("userId"));
        logger.log(Level.INFO, "URL >>>> " + sess.getAttribute("currentUrl"));
        return mvc;
    }
    
    @PostMapping(path = "/search")
    public ModelAndView postSearchRecipe (
            
            @RequestBody MultiValueMap<String, String> payload,
            HttpSession sess) {

        String query = payload.getFirst("query");
        String cuisineType = payload.getFirst("cuisineType"); 
        String mealType = payload.getFirst("mealType");
        boolean nextPage = false;
        // boolean prevPage = false;
        if (payload.containsKey("nextPage")) {
            nextPage = Boolean.valueOf(payload.getFirst("nextPage")) ;
        };
        
        ModelAndView mvc = new ModelAndView();

        List<Recipe> recipeList = new ArrayList<>();
        String url = (String) sess.getAttribute("currentUrl");
        logger.log(Level.INFO, "Current Url >>>>>> " + url);

        if (nextPage) {
            sess.setAttribute("previousUrl", url);
            url = recipeSvc.getNextPage(url);
            sess.setAttribute("currentUrl", url);
        }

        if (url == null) {
            mvc.addObject("lastPage", true);
        }

        recipeList = recipeSvc.searchRecipes(url);

        mvc.addObject("query", query);
        mvc.addObject("cuisineType", cuisineType);
        mvc.addObject("mealType", mealType);
        mvc.addObject("recipeList", recipeList);
        mvc.setViewName("recipeSearch");
        sess.setAttribute("recipeList", recipeList);
        mvc.addObject("user", sess.getAttribute("username"));
        mvc.addObject("userId", sess.getAttribute("userId"));
        
        return mvc;
    }


    @GetMapping(path = "/search/details")
    public ModelAndView getRecipeDetails (@RequestParam int recipeIndex, HttpSession sess) {
        List<Recipe> recipeList = (List<Recipe>) sess.getAttribute("recipeList");
        Recipe recipe = recipeList.get(recipeIndex);

        ModelAndView mvc = new ModelAndView("recipeDetails");
        mvc.addObject("recipeIndex", recipeIndex);
        mvc.addObject("recipe", recipe);
        mvc.addObject("user", sess.getAttribute("username"));
        mvc.addObject("userId", sess.getAttribute("userId"));

        return mvc;
    }

    @PostMapping(path = "/search/details")
    public ModelAndView postSaveRecipeDetails (@RequestBody MultiValueMap<String,String> payload, HttpSession sess) {
        List<Recipe> recipeList = (List<Recipe>) sess.getAttribute("recipeList");
        ModelAndView mvc = new ModelAndView("recipeDetails");
        String userId = (String) sess.getAttribute("userId");

        int recipeIndex = Integer.parseInt(payload.getFirst("recipeIndex"));
        Recipe recipe = recipeList.get(recipeIndex);
        // boolean saveRecipe = false;
        // boolean saveIngredients = false;

        if (payload.containsKey("saveRecipe")){
            // boolean saveRecipe = Boolean.valueOf(payload.getFirst("saveRecipe"));
            try {
                if (recipeSvc.saveRecipe(recipe, userId)){
                    mvc.addObject("message", "Recipe Saved.");
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, e.getMessage());
                mvc.addObject("message", "Recipe has already been saved.");
                mvc.setStatus(HttpStatus.BAD_REQUEST);
            };
        }
        // } else if (payload.containsKey("saveIngredients")){
        //     // saveIngredients = Boolean.valueOf(payload.getFirst("saveIngredients"));
        //     try {
        //         if (ingredientSvc.saveIngredients(recipe, userId)){
        //             mvc.addObject("message", "Ingredients Saved.");
        //         }
        //     } catch (Exception e1) {
        //         logger.log(Level.WARNING, e1.getMessage());
        //         e1.printStackTrace();
        //         mvc.addObject("message", e1.getMessage());
        //         mvc.setStatus(HttpStatus.BAD_REQUEST);
        //     }
        // }

        mvc.addObject("recipeIndex", recipeIndex);
        mvc.addObject("recipe", recipe);
        mvc.addObject("user", sess.getAttribute("username"));
        mvc.addObject("userId", sess.getAttribute("userId"));
        return mvc;
    }
}
