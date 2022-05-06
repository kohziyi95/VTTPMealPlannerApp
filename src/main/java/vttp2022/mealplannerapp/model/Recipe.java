package vttp2022.mealplannerapp.model;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.json.JsonObject;

public class Recipe implements Serializable {
    private int id;
    private String recipeName;
    private String imgUrl;
    private List<String> ingredients;
    private List<String> cuisineType;
    private List<String> mealType;

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public String getRecipeName() {
        return recipeName;
    }
    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }
    public String getImgUrl() {
        return imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public List<String> getIngredients() {
        return ingredients;
    }
    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(List<String> cuisineType) {
        this.cuisineType = cuisineType;
    }

    public List<String> getMealType() {
        return mealType;
    }

    public void setMealType(List<String> mealType) {
        this.mealType = mealType;
    }

    public static Recipe createRecipe(JsonObject object){
        Recipe recipe = new Recipe();
        List<String> ingredientList = object.getJsonArray("ingredientLines")
                .stream()
                .map(ingredient -> ingredient.toString())
                .collect(Collectors.toList());
        List<String> cuisineType = object.getJsonArray("cuisineType")
                .stream()
                .map(cuisine -> cuisine.toString())
                .collect(Collectors.toList());
        List<String> mealType = object.getJsonArray("mealType")
                .stream()
                .map(meal -> meal.toString())
                .collect(Collectors.toList());
        
        recipe.setRecipeName(object.getString("label"));
        recipe.setIngredients(ingredientList);
        recipe.setCuisineType(cuisineType);
        recipe.setMealType(mealType);
        return recipe;
    }

    
}
