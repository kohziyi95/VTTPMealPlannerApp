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
import org.springframework.jdbc.core.JdbcTemplate;

import vttp2022.mealplannerapp.model.Ingredient;
import vttp2022.mealplannerapp.model.Recipe;
import vttp2022.mealplannerapp.repository.IngredientRepository;
import vttp2022.mealplannerapp.service.IngredientService;
import vttp2022.mealplannerapp.service.RecipeService;

@SpringBootTest
class IngredientServiceTests {

	@Autowired
	private RecipeService recipeSvc;

	@Mock
	private IngredientRepository ingredientRepo;

	@Autowired
	private IngredientService ingredientSvc;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private Recipe recipe = new Recipe();
	
	private List<Ingredient> ingredientList = new ArrayList<>();


	@BeforeEach
	void init(){
		recipe = recipeSvc.searchRecipes("chicken", "", "dinner").get(0);
		// ingredientList = recipe.getIngredientList();
	}

	@Test
	void saveIngredientsTestReturnTrue() throws Exception{
		
		assertTrue(ingredientSvc.saveIngredients(recipe, "iTest001"));
	}

	// @Test
	// void saveIngredientsTestThrowException() throws Exception{
	// 	when(ingredientRepo.sqlInsertIngredients(any(), any(), any(),any(Float.class), any(), any())).thenReturn(2);

	// 	assertThrows(Exception.class, () -> ingredientSvc.saveIngredients(ingredientList, "iTest002"));
	// }
	

	@AfterEach
	void destroy(){
		jdbcTemplate.update("delete from ingredient_list where user_id = 'iTest001'");
	}

}
