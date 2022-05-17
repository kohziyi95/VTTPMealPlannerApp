package vttp2022.mealplannerapp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import vttp2022.mealplannerapp.model.Recipe;
import vttp2022.mealplannerapp.repository.RecipeRepository;
import vttp2022.mealplannerapp.service.LoginService;
import vttp2022.mealplannerapp.service.RecipeService;

@SpringBootTest
class RecipeServiceTests {

	@Autowired
	private RecipeService recipeSvc;

	@Autowired
	private RecipeRepository recipeRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;


	@Test
	void recipeListCountShouldAssertEquals20(){
		List<Recipe> recipeList= recipeSvc.searchRecipes("chicken", "asian", "dinner");
		assertEquals(20, recipeList.size());
	}

	@Test
	void nextPageRecipeListCountShouldAssertEquals20(){
		String nextPage = recipeSvc.getNextPage(recipeSvc.getUrlStringByQuery("chicken", "asian", "dinner"));
		List<Recipe> recipeList= recipeSvc.searchRecipes(nextPage);
		assertEquals(20, recipeList.size());
	}

	@Test
	void saveRecipeShouldReturnTrue(){
		if (jdbcTemplate.queryForObject("select count(*) from recipes where recipe_name = 'TEST RECIPE'",Integer.class) > 0){
			int added = jdbcTemplate.update("delete from recipes where recipe_name = 'TEST RECIPE'");
		};
			
		Recipe recipe = new Recipe();
		recipe.setRecipeName("TEST RECIPE");
		List<String> mealType = new ArrayList<>();
		mealType.add("lunch/dinner");
		recipe.setMealType(mealType);
		List<String> cuisineType = new ArrayList<>();
		cuisineType.add("asian");
		recipe.setCuisineType(cuisineType);
		List<String> ingredientList = new ArrayList<>();
		ingredientList.add("apple");
		recipe.setIngredientLines(ingredientList);
		try {
			recipeSvc.saveRecipe(recipe, "test0001");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String result = null;
		
		try {
			result = recipeRepo.redisGetRecipe("test0001", recipe.getId()).getRecipeName();
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		assertEquals("TEST RECIPE", result);
	}

	@Test
	void saveRecipeShouldReturnFalse() throws Exception{

		Recipe recipe = new Recipe();
		recipe.setRecipeName("TEST RECIPE");
		List<String> mealType = new ArrayList<>();
		mealType.add("lunch/dinner");
		recipe.setMealType(mealType);
		List<String> cuisineType = new ArrayList<>();
		cuisineType.add("asian");
		recipe.setCuisineType(cuisineType);
		List<String> ingredientList = new ArrayList<>();
		ingredientList.add("apple");
		recipe.setIngredientLines(ingredientList);
		assertFalse(recipeSvc.saveRecipe(recipe, "test0001"));
	}

	@Test
	void getSavedRecipeShouldReturn1() throws Exception{
		List<Recipe> recipeList = recipeSvc.getSavedRecipes("test0001");
		assertTrue(recipeList.size() == 1);
	}

}
