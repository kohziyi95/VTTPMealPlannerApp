package vttp2022.mealplannerapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import vttp2022.mealplannerapp.model.Recipe;
import vttp2022.mealplannerapp.service.RecipeService;

@Controller
@RequestMapping(path = "/recipe")
public class RecipeController {
    private Logger logger = Logger.getLogger(RecipeController.class.getName());

    @Autowired
    private RecipeService svc;

    @GetMapping(path = "/search")
    public ModelAndView getSearchRecipe (
            @RequestParam String query, 
            @RequestParam String cuisineType, 
            @RequestParam String mealType,
            HttpSession sess) {

        ModelAndView mvc = new ModelAndView();

        List<Recipe> recipeList = new ArrayList<>();
        String url = svc.getUrlStringByQuery(query, cuisineType, mealType);
        // if (nextPage) {
        //     url = svc.getNextPage(url);
        // }
        if (svc.getNextPage(url) == null) {
            mvc.addObject("lastPage", true);
        }
        
        recipeList = svc.searchRecipes(url);

        mvc.addObject("query", query);
        mvc.addObject("cuisineType", cuisineType);
        mvc.addObject("mealType", mealType);
        mvc.addObject("recipeList", recipeList);
        mvc.setViewName("recipeSearch");
        sess.setAttribute("recipeList", recipeList);
        sess.setAttribute("currentUrl", url);
        logger.log(Level.INFO, "URL >>>> " + sess.getAttribute("currentUrl"));
        return mvc;
    }
    
    @PostMapping(path = "/search")
    public ModelAndView getSearchRecipe (
            
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
            url = svc.getNextPage(url);
            sess.setAttribute("currentUrl", url);
        }

        if (url == null) {
            mvc.addObject("lastPage", true);
        }

        recipeList = svc.searchRecipes(url);

        mvc.addObject("query", query);
        mvc.addObject("cuisineType", cuisineType);
        mvc.addObject("mealType", mealType);
        mvc.addObject("recipeList", recipeList);
        mvc.setViewName("recipeSearch");
        sess.setAttribute("recipeList", recipeList);
        
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
        return mvc;
    }

    @PostMapping(path = "/search/details")
    public ModelAndView postRecipeDetails (@RequestParam int recipeIndex, HttpSession sess) {
        List<Recipe> recipeList = (List<Recipe>) sess.getAttribute("recipeList");
        Recipe recipe = recipeList.get(recipeIndex);

        ModelAndView mvc = new ModelAndView("recipeDetails");
        mvc.addObject("recipeIndex", recipeIndex);
        mvc.addObject("recipe", recipe);
        mvc.addObject("user", sess.getAttribute("username"));
        return mvc;
    }
}
