package vttp2022.mealplannerapp.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.mealplannerapp.model.Ingredient;
import vttp2022.mealplannerapp.model.Recipe;
import vttp2022.mealplannerapp.repository.IngredientRepository;
import vttp2022.mealplannerapp.repository.RecipeRepository;

@Service
public class IngredientService {
    private static Logger logger = Logger.getLogger(IngredientService.class.getName());

    @Autowired
    private IngredientRepository ingredientRepo;

    @Transactional
    public boolean saveIngredients (Recipe recipe, String userId) throws Exception {
        List<Ingredient> ingredientList = recipe.getIngredientList();
        int recipeId = recipe.getId();
        return saveIngredients(ingredientList, userId, recipeId);
    }

    @Transactional
    public boolean saveIngredients (List<Ingredient> ingredientList, String userId, int recipeId) throws Exception{
        int ingredientAmount = ingredientList.size();
        int added = 0;

        try {
            if (!ingredientRepo.sqlGetIngredientList(userId, recipeId).isEmpty()){
                throw new Exception("Ingredients has already been saved.");
            }
        } catch (Exception e) {
            throw e;
        }
        for (Ingredient ingredient : ingredientList) {
            ingredient.setRecipeId(recipeId);
            added += ingredientRepo.sqlInsertIngredients(ingredient.getIngredientId(), userId, 
                ingredient.getItemName(), ingredient.getQuantity(), 
                ingredient.getMeasure(), ingredient.getImgUrl(),
                ingredient.getRecipeId());
            logger.log(Level.INFO, added + ". " + userId + ": Ingredient: >>>> " + ingredient.getItemName());
        }
        try {
            if (added != ingredientAmount){
                throw new Exception("Ingredients saved failed. Expected: " + ingredientAmount + ", Actual: " + added + ".");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        logger.log(Level.INFO, "Ingredients saved successfully.");
        return true;
    }

    public boolean deleteIngredients (String userId, int recipeId){
        int deleted = ingredientRepo.sqlDeleteIngredients(userId, recipeId);
        return deleted > 0;
    }

    public List<Ingredient> getIngredients(String userId, int recipeId) throws Exception{
        List<Ingredient> ingredientList = ingredientRepo.sqlGetIngredientList(userId, recipeId);
        if (ingredientList.isEmpty()) throw new Exception("Ingredient List Not Found.");
        return ingredientList;
    }

    public List<Ingredient> getAllIngredients(String userId) throws Exception{
        List<Ingredient> ingredientList = ingredientRepo.sqlGetAllIngredients(userId);
        if (ingredientList.isEmpty()) throw new Exception("Ingredient List Not Found.");
        return ingredientList;

    }

    

    


}
