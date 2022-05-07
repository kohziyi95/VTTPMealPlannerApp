package vttp2022.mealplannerapp.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.mealplannerapp.model.Recipe;
import vttp2022.mealplannerapp.repository.RecipeRepository;

@Service
public class RecipeService {
    private static Logger logger = Logger.getLogger(RecipeService.class.getName());

    @Autowired
    private RecipeRepository recipeRepo;

    @Value("${edamam.api.id}")
    private String edamamId;

    @Value("${edamam.api.key}")
    private String edamamKey;

    private final String EDAMAM = "https://api.edamam.com/api/recipes/v2";

    public List<Recipe> searchRecipes(String query, String cuisineType, String mealType){
        List<Recipe> result = new LinkedList<>();
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(EDAMAM)
                .queryParam("type", "public")
                .queryParam("app_id", edamamId)
                .queryParam("app_key", edamamKey)
                .queryParam("q", query)
                .queryParam("imageSize", "REGULAR");
        
        if (cuisineType!="") {
            uri = uri.queryParam("cuisineType", cuisineType);
        }

        if (mealType!="") {
            uri = uri.queryParam("mealType", mealType);
        }
                
        String url = uri.toUriString();
        logger.log(Level.INFO,url);
        
        RequestEntity<Void> req = RequestEntity
        .get(url)
        .accept(MediaType.APPLICATION_JSON)
        .build();

        RestTemplate template = new RestTemplate();

        ResponseEntity<String> resp = template.exchange(req,String.class);  
        InputStream is = new ByteArrayInputStream(resp.getBody().getBytes());
        JsonReader reader = Json.createReader(is);
        JsonObject data = reader.readObject();
        JsonArray array = data.getJsonArray("hits");
        for (int i = 0; i < array.size(); i++){
            JsonObject obj = (JsonObject) array.get(i);
            JsonObject recipeData = obj.getJsonObject("recipe");
            result.add(Recipe.createRecipe(recipeData));
            logger.log(Level.INFO, (i+1) + " >>>>>> " + result.get(i).getRecipeName() );
            
        }
        return result;
    }

    public boolean saveRecipe(Recipe recipe, String userId){
        try {
            recipeRepo.sqlInsertRecipes(recipe, userId);
        } catch (Exception e) {
            logger.log(Level.INFO, "Recipe has already been saved.");
            return false;
        }
        
        int recipeId = recipeRepo.sqlGetRecipeId(recipe, userId);
        recipe.setId(recipeId);
        recipeRepo.redisPutRecipe(recipe, userId);
        return true;
    }

}
