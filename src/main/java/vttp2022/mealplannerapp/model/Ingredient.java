package vttp2022.mealplannerapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

public class Ingredient implements Serializable {
    private String ingredientId;
    private String itemName;
    private Float quantity;
    private String measure;
    private String imgUrl;
    private Integer recipeId;

    public String getIngredientId() {
        return ingredientId;
    }
    public void setIngredientId(String ingredientId) {
        this.ingredientId = ingredientId;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public Float getQuantity() {
        return quantity;
    }
    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }
    public String getMeasure() {
        return measure;
    }
    public void setMeasure(String measure) {
        this.measure = measure;
    }
    public String getImgUrl() {
        return imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public Integer getRecipeId() {
        return recipeId;
    }
    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }
    public static List<Ingredient> getList(JsonArray jsonArray){
        List<Ingredient> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++){
            JsonObject obj = (JsonObject) jsonArray.get(i);
            Ingredient ingredient = new Ingredient();
            ingredient.setIngredientId(obj.getString("foodId"));
            ingredient.setItemName(obj.getString("food"));
            ingredient.setQuantity(Float.parseFloat(obj.get("quantity").toString()));

            String measure = null;
            try {
                measure = obj.getString("measure");
            } catch (ClassCastException e){
                measure = null;
            }
            ingredient.setMeasure(measure);

            String imgUrl = null;
            try {
                imgUrl = obj.getString("image");
            } catch (ClassCastException ex) {
                imgUrl = "/images/imgNotAvailable.png";
            }
            ingredient.setImgUrl(imgUrl);
            list.add(ingredient);
        }

        return list;
    }
    
    public static Ingredient createTestIngredient(int recipeId){
         Ingredient i = new Ingredient();
         i.setImgUrl("testImgUrl");
         i.setIngredientId("testId");
         i.setItemName("testName");
         i.setMeasure("testMeasure");
         i.setQuantity(2F);
         i.setRecipeId(recipeId);
        return i;
    }
}
