package vttp2022.mealplannerapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

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

	@Autowired
    RedisTemplate<String,Object> redisTemplate;


	@Test
	void recipeListCountShouldAssertEquals20(){
		List<Recipe> recipeList= recipeSvc.searchRecipes("chicken", "asian", "dinner");
		assertEquals(20, recipeList.size());
	}

	@Test
	void saveRecipeShouldReturnTrue(){
		Recipe recipe = new Recipe();
		recipe.setRecipeName("Chicken Noodle & Pea Soup");
		List<String> mealType = new ArrayList<>();
		mealType.add("lunch/dinner");
		recipe.setMealType(mealType);
		List<String> cuisineType = new ArrayList<>();
		cuisineType.add("asian");
		recipe.setCuisineType(cuisineType);
		List<String> ingredientList = new ArrayList<>();
		ingredientList.add("apple");
		recipe.setIngredients(ingredientList);
		recipeSvc.saveRecipe(recipe, "12345678");
		String result = recipeRepo.redisGetRecipe("12345678", recipe.getId()).get().getRecipeName();
				
		assertEquals("Chicken Noodle & Pea Soup", result);
	}

	@Test
	void createUserShouldReturnTrue(){
		boolean added = false;
		try {
			added = loginSvc.createUser("user1", "123456", "test@test.com");
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
		assertTrue(added);
	}

}
