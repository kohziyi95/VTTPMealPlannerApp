package vttp2022.mealplannerapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ModelAndView getSearchRecipe (@RequestParam String query, @RequestParam String cuisineType, @RequestParam String mealType) {
        ModelAndView mvc = new ModelAndView();
        // String query = payload.getFirst("query");
        // String mealType = payload.getFirst("mealType");
        // String cuisineType = payload.getFirst("cuisineType");

        List<Recipe> recipeList = new ArrayList<>();
        recipeList = svc.searchRecipes(query, cuisineType, mealType);
        mvc.addObject("recipeList", recipeList);
        mvc.setViewName("recipeSearch");

        return mvc;
    }
}
