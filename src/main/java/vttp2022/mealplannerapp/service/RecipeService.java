package vttp2022.mealplannerapp.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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
   
        String url = getUrlStringByQuery(query, cuisineType, mealType);

        return searchRecipes(url);
    }

    public List<Recipe> searchRecipes(String url){
        List<Recipe> result = new LinkedList<>();

        InputStream is = null;

        try {
            is = new URL(url).openStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonReader reader = Json.createReader(is);
        JsonObject data = reader.readObject();
        JsonArray array = data.getJsonArray("hits");
        for (int i = 0; i < array.size(); i++){
            JsonObject obj = (JsonObject) array.get(i);
            JsonObject recipeData = obj.getJsonObject("recipe");
            result.add(Recipe.createRecipe(recipeData));
            logger.log(Level.INFO, (i+1) + " >>>>>> " + result.get(i).getRecipeName());
            // logger.log(Level.INFO, (i+1) + " ingredientLines  >>>>>> " + result.get(i).getIngredientLines());
        }
        
        return result;
    }

    // public List<Recipe> searchRecipes(String url){
    //     List<Recipe> result = new LinkedList<>();

    //     RequestEntity<Void> req = RequestEntity
    //     .get(url)
    //     .accept(MediaType.APPLICATION_JSON)
    //     .build();

    //     RestTemplate template = new RestTemplate();

    //     ResponseEntity<String> resp = template.exchange(req,String.class);  
    //     InputStream is = new ByteArrayInputStream(resp.getBody().getBytes());
    //     JsonReader reader = Json.createReader(is);
    //     JsonObject data = reader.readObject();
    //     JsonArray array = data.getJsonArray("hits");
    //     for (int i = 0; i < array.size(); i++){
    //         JsonObject obj = (JsonObject) array.get(i);
    //         JsonObject recipeData = obj.getJsonObject("recipe");
    //         result.add(Recipe.createRecipe(recipeData));
    //         logger.log(Level.INFO, (i+1) + " >>>>>> " + result.get(i).getRecipeName());
    //         // logger.log(Level.INFO, (i+1) + " ingredientLines  >>>>>> " + result.get(i).getIngredientLines());

    //     }

    //     return result;
    // }

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

    public String getUrlStringByQuery(String query, String cuisineType, String mealType){
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
        return url;
    }

    public String getNextPage(String url){ 
        InputStream is = null;

        try {
            is = new URL(url).openStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonReader reader = Json.createReader(is);
        JsonObject data = reader.readObject().getJsonObject("_links");
        
        JsonObject next = data.getJsonObject("next");
        if (next == null) {
            return null;
        }
        String nextPage = next.getString("href");
        logger.log(Level.INFO, "Next page url >>>>>>>>>>>>> " + nextPage);

        return nextPage;
    }

}
