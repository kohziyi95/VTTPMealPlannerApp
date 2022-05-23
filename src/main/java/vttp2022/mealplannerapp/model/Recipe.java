package vttp2022.mealplannerapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.json.JsonObject;

public class Recipe implements Serializable {
    private int id;
    private String recipeName;
    private String imgUrl;
    private List<String> ingredientLines;
    private List<String> cuisineType;
    private List<String> mealType;
    private List<Ingredient> ingredientList;

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
    public List<String> getIngredientLines() {
        return ingredientLines;
    }
    public void setIngredientLines(List<String> ingredientLines) {
        this.ingredientLines = ingredientLines;
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

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public static Recipe createRecipe(JsonObject object){
        Recipe recipe = new Recipe();
        List<String> ingredientLines = object.getJsonArray("ingredientLines")
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
        List<Ingredient> ingredientList = Ingredient.getList(object.getJsonArray("ingredients"));
        
        recipe.setRecipeName(object.getString("label"));
        recipe.setIngredientLines(ingredientLines);
        recipe.setImgUrl(object.getJsonObject("images").getJsonObject("REGULAR").getString("url"));
        recipe.setCuisineType(cuisineType);
        recipe.setMealType(mealType);
        recipe.setIngredientList(ingredientList);

        return recipe;
    }

    public Recipe createTestRecipe(int recipeId){
        Recipe r = new Recipe();
        List<String> ingredientLines = new ArrayList<>();
        ingredientLines.add("test");
        List<String> cuisineType = List.copyOf(ingredientLines);
        List<String> mealType = List.copyOf(ingredientLines);
        List<Ingredient> ingredientList = new ArrayList<>();
        ingredientList.add(Ingredient.createTestIngredient(recipeId));

        r.setCuisineType(cuisineType);
        r.setId(recipeId);
        r.setImgUrl("testUrl");
        r.setIngredientLines(ingredientLines);
        r.setIngredientList(ingredientList);
        r.setMealType(mealType);
        r.setRecipeName("testRecipeName");

        return r;
    }
    
}
