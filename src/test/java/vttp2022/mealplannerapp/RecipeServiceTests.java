package vttp2022.mealplannerapp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import vttp2022.mealplannerapp.model.Recipe;
import vttp2022.mealplannerapp.repository.RecipeRepository;
import vttp2022.mealplannerapp.service.RecipeService;

@SpringBootTest
class RecipeServiceTests {

	@Autowired
	private RecipeService recipeSvc;

	@Mock
	private RecipeRepository recipeRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private Recipe recipe = new Recipe();

	@BeforeEach
	void init(){
		
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
	
		when(recipeRepo.sqlInsertRecipes(any(Recipe.class),anyString())).thenReturn(true);
		when(recipeRepo.sqlGetRecipeId(any(Recipe.class),anyString())).thenReturn(99);

		try {
			recipeSvc.saveRecipe(recipe, "test0001");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

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
	void saveRecipeShouldReturnTrue() throws Exception{
		when (recipeRepo.redisGetRecipe(anyString(), anyInt())).thenReturn(recipe);

		String result = recipeRepo.redisGetRecipe("test0001", recipe.getId()).getRecipeName();
		assertEquals("TEST RECIPE", result);
	}

	@Test
	void saveRecipeShouldReturnFalse() throws Exception{
		Recipe r = new Recipe();
		r.setRecipeName("TEST RECIPE");
		List<String> mealType = new ArrayList<>();
		mealType.add("lunch/dinner");
		r.setMealType(mealType);
		List<String> cuisineType = new ArrayList<>();
		cuisineType.add("asian");
		r.setCuisineType(cuisineType);
		List<String> ingredientList = new ArrayList<>();
		ingredientList.add("apple");
		r.setIngredientLines(ingredientList);

		assertFalse(recipeSvc.saveRecipe(r, "test0001")); 
	}

	@Test
	void getSavedRecipeShouldReturn1() throws Exception{
		List<Integer> idList = new ArrayList<>();
		idList.add(1);
		when (recipeRepo.sqlGetAllRecipeId(anyString())).thenReturn(idList);
		when (recipeRepo.redisGetRecipe(anyString(), anyInt())).thenReturn(recipe);

		List<Recipe> recipeList = recipeSvc.getSavedRecipes("test0001");
		assertTrue(recipeList.size() == 1);
	}

	@Test
	void deleteRecipes() throws Exception{
		Recipe r = new Recipe();
		r.setRecipeName("TEST RECIPE");
		List<String> mealType = new ArrayList<>();
		mealType.add("lunch/dinner");
		r.setMealType(mealType);
		List<String> cuisineType = new ArrayList<>();
		cuisineType.add("asian");
		r.setCuisineType(cuisineType);
		List<String> ingredientList = new ArrayList<>();
		ingredientList.add("apple");
		r.setIngredientLines(ingredientList);
		recipeSvc.saveRecipe(r, "test0002"); 

		recipeSvc.deleteSavedRecipes(r, "test0002");
		when(recipeRepo.sqlGetRecipeId(r, "test0002")).thenThrow(new EmptyResultDataAccessException(null, 1));
		assertThrows(Exception.class, ()-> recipeRepo.sqlGetRecipeId(r, "test0002"));

	}

	@AfterEach
	void destroy(){
		recipeSvc.redisDeleteAllFromUser("test0001");
		jdbcTemplate.update("delete from recipes where recipe_name = 'TEST RECIPE'");
	}

}
