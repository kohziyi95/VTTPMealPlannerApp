package vttp2022.mealplannerapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import vttp2022.mealplannerapp.controller.IngredientController;
import vttp2022.mealplannerapp.controller.ListController;
import vttp2022.mealplannerapp.model.Recipe;
import vttp2022.mealplannerapp.service.IngredientService;
import vttp2022.mealplannerapp.service.LoginService;
import vttp2022.mealplannerapp.service.RecipeService;

@SpringBootTest
@AutoConfigureMockMvc

class IngredientControllerTests {
	private Logger logger = Logger.getLogger(IngredientControllerTests.class.getName());


	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JdbcTemplate jdbcTemplate;

    @Autowired
    private IngredientController controller;

	@Autowired
	private RecipeService recipeSvc;

	@Autowired
	private IngredientService ingredientSvc;

	@Autowired LoginService loginSvc;

	private String username = null;
	private String userId = null;
	private Recipe recipe = new Recipe();
	private int recipeId = 0;


	@BeforeEach
	void init() throws Exception{
		// String query = "chicken";
		// String cuisineType = "";
		// String mealType = "dinner";
		// String url = recipeSvc.getUrlStringByQuery(query, cuisineType, mealType);
		// List<Recipe> recipeList = recipeSvc.searchRecipes(url);

		username = "ingredientTest1";
		loginSvc.createUser(username, "12345678", "ingredientTest1@test.com");
		userId = loginSvc.authenticateUser(username, "12345678");
		// int recipeIndex = 5;
		// recipe = recipeList.get(recipeIndex);
		recipe = recipe.createTestRecipe(999);
		recipeSvc.saveRecipe(recipe, userId);
		recipeId = recipe.getId();
		logger.log(Level.INFO, "Test User Id >>>>>>> " + userId);
	}
	
    @Test
	public void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
    }

	@Test
	public void getSavedIngredientsTest() throws Exception {

		mockMvc.perform(get("/ingredient/" + userId )
			.sessionAttr("userId", userId)
			.sessionAttr("username", username))
			// .andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	public void postDeleteIngredientTest() throws Exception {
		mockMvc.perform(post("/ingredient/" + userId + "/delete")
			.param("recipeId", String.valueOf(recipeId))
			.param("username", username)
			.sessionAttr("userId", userId)
			.sessionAttr("username", username))
			// .andDo(print())
			.andExpect(status().isOk());
	}

	@AfterEach
	void destroy() throws Exception {
		jdbcTemplate.update("delete from user where username = 'ingredientTest1'");
		recipeSvc.redisDeleteAllFromUser(userId);
		recipeSvc.deleteSavedRecipes(recipeId, userId);
	}

}
