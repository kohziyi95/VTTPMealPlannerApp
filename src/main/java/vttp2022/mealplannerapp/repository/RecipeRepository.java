package vttp2022.mealplannerapp.repository;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import vttp2022.mealplannerapp.model.Recipe;

@Repository
public class RecipeRepository {
    private static Logger logger = Logger.getLogger(RecipeRepository.class.getName());
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    //JDBC Methods
    private final String SQL_INSERT_RECIPE = 
        "insert into recipes (recipe_name, user_id) values (? , ?)";

        private final String SQL_SELECT_RECIPE_ID = 
        "select id from recipes where recipe_name = ? and user_id = ?";    


    public boolean sqlInsertRecipes(Recipe recipe, String userId){
        int added = jdbcTemplate.update(SQL_INSERT_RECIPE, recipe.getRecipeName(), userId);
        return added > 0;
    }

    public int sqlGetRecipeId(Recipe recipe, String userId){
        int recipeId = jdbcTemplate.queryForObject(SQL_SELECT_RECIPE_ID, int.class, recipe.getRecipeName(), userId);
        logger.log(Level.INFO, "Recipe id >>>>>>> " + recipeId);
        return recipeId;
        // SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_SELECT_RECIPE_ID, recipe.getRecipeName(), userId);
        // if (!rs.next()) return -1;
        // int recipeId = rs.getInt("id");
        // return recipeId;
    }


    //REDIS Methods
    public void redisPutRecipe(Recipe recipe, String userId){
        redisTemplate.opsForList().leftPush(userId, String.valueOf(recipe.getId()));
        redisTemplate.opsForHash().put(userId + "_Map", String.valueOf(recipe.getId()), recipe);
    }

    public Optional<Recipe> redisGetRecipe(String userId, int recipeId){
        Optional<Recipe> opt = Optional.of((Recipe)redisTemplate.opsForHash().get(userId + "_Map", String.valueOf(recipeId)));
        return opt;
    }
}
