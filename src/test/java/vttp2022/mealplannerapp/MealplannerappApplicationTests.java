package vttp2022.mealplannerapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import vttp2022.mealplannerapp.model.Recipe;
import vttp2022.mealplannerapp.repository.RecipeRepository;
import vttp2022.mealplannerapp.service.LoginService;
import vttp2022.mealplannerapp.service.RecipeService;

@SpringBootTest
class MealplannerappApplicationTests {

	@Autowired
	private RecipeService recipeSvc;

	@Autowired
	private RecipeRepository recipeRepo;

	@Autowired
	private LoginService loginSvc;

	// @Autowired
    // private RedisTemplate<String,Object> redisTemplate;

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
		recipeSvc.saveRecipe(recipe, "test0001");
		String result = recipeRepo.redisGetRecipe("test0001", recipe.getId()).get().getRecipeName();
				
		assertEquals("TEST RECIPE", result);
	}

	@Test
	void createUserShouldReturnTrue(){
		if (jdbcTemplate.queryForObject("select count(*) from user where username = 'test1'",Integer.class) > 0){
			int updated = jdbcTemplate.update("delete from user where username = 'test1'");
		};
		boolean added = false;
		try {
			added = loginSvc.createUser("test1", "12345678", "test@test.com");
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(added);
	}

}
