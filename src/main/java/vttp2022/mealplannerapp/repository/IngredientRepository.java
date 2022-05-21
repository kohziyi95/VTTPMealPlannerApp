package vttp2022.mealplannerapp.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2022.mealplannerapp.model.Ingredient;


@Repository
public class IngredientRepository {
    private static Logger logger = Logger.getLogger(IngredientRepository.class.getName());
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //JDBC Methods
    private final String SQL_INSERT_INGREDIENT = 
        "insert into ingredient_list (ingredient_id, user_id, item_name, quantity, measure, img_url, recipe_id) values (? , ?, ? , ? , ? , ? ,?)";

    private final String SQL_SELECT_INGREDIENT = 
        "select * from ingredient_list where recipe_id = ?";

    private final String SQL_DELETE_INGREDIENT_BY_ID = 
        "delete from ingredient_list where ingredient_id = ?";

    public int sqlInsertIngredients(String ingredientId, String userId, String itemName, 
                float quantity, String measure, String imgUrl, int recipeId){
        int added = jdbcTemplate.update(SQL_INSERT_INGREDIENT, ingredientId, userId, itemName, quantity, measure, imgUrl, recipeId);
        return added;
    }

    public List<Ingredient> sqlGetIngredientList(int recipeId){
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_SELECT_INGREDIENT, recipeId);
        List<Ingredient> ingredientList = new ArrayList<>();
        while (rs.next()){
            Ingredient i = new Ingredient();
            i.setIngredientId(rs.getString("ingredient_id"));
            i.setItemName(rs.getString("item_name"));
            i.setQuantity(rs.getFloat("quantity"));
            try {
                i.setMeasure(rs.getString("measure"));
            } catch (Exception e) {
                i.setMeasure(null);
            }
            i.setImgUrl(rs.getString("img_url"));
            i.setRecipeId(recipeId);
            ingredientList.add(i);
        }
        return ingredientList;
    }

    public int sqlDeleteIngredients(String ingredientId){
        int deleted = jdbcTemplate.update(SQL_DELETE_INGREDIENT_BY_ID, ingredientId);
        return deleted;
    }

}
