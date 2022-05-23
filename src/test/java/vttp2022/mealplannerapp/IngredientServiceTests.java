package vttp2022.mealplannerapp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import vttp2022.mealplannerapp.model.Ingredient;
import vttp2022.mealplannerapp.model.Recipe;
import vttp2022.mealplannerapp.repository.IngredientRepository;
import vttp2022.mealplannerapp.service.IngredientService;
import vttp2022.mealplannerapp.service.RecipeService;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IngredientServiceTests {
	Logger logger = Logger.getLogger(IngredientServiceTests.class.getName());

	@Autowired
	private RecipeService recipeSvc;

	@Autowired
	private IngredientRepository ingredientRepo;

	@Autowired
	private IngredientService ingredientSvc;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private Recipe recipe = new Recipe();

	private List<Recipe> recipeList = new ArrayList<>();

	private boolean ingredientSaved;

	private List<Ingredient> ingredientList = new ArrayList<>();

	private int recipeId;

	@BeforeAll
	void init() throws Exception{
		recipeList = recipeSvc.searchRecipes("chicken", "", "dinner");
		recipe = recipeList.get(0);
		// ingredientList = recipe.getIngredientList();
		recipe.setId(999);
		recipeId = recipe.getId();
		ingredientSaved = ingredientSvc.saveIngredients(recipe, "iTest001");
	}

	@Test
	void saveIngredientsTestReturnTrue() throws Exception{
		logger.log(Level.INFO, "Recipe ID >>>>> " + recipeId);

		ingredientList = ingredientRepo.sqlGetIngredientList("iTest001", recipe.getId());
		logger.log(Level.INFO, ingredientList.toString());
		assertTrue(ingredientSaved && !ingredientList.isEmpty());
	}

	// @Test
	// void saveIngredientsTestThrowException() throws Exception{
	// 	when(ingredientRepo.sqlInsertIngredients(any(), any(), any(),any(Float.class), any(), any())).thenReturn(2);

	// 	assertThrows(Exception.class, () -> ingredientSvc.saveIngredients(ingredientList, "iTest002"));
	// }

	@Test
	void deleteIngredients() throws Exception{
		ingredientSvc.saveIngredients(recipe, "iTest002");

		boolean deleted = ingredientSvc.deleteIngredients("iTest002", recipeId);
		ingredientList = ingredientRepo.sqlGetIngredientList("iTest002", recipe.getId());
		logger.log(Level.INFO, ingredientList.toString());
		assertTrue(deleted && ingredientList.isEmpty());
	}
	

	@AfterAll
	void destroy(){
		jdbcTemplate.update("delete from ingredient_list where user_id = 'iTest001'");
	}

}
