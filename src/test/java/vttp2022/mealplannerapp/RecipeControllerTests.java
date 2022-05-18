package vttp2022.mealplannerapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import vttp2022.mealplannerapp.controller.RecipeController;
import vttp2022.mealplannerapp.model.Recipe;
import vttp2022.mealplannerapp.service.LoginService;
import vttp2022.mealplannerapp.service.RecipeService;

@SpringBootTest
@AutoConfigureMockMvc

class RecipeControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JdbcTemplate jdbcTemplate;

    @Autowired
    private RecipeController controller;

	@Autowired
	private RecipeService recipeSvc;

	@Autowired
	private LoginService loginSvc;


    @Test
	public void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
    }

	@Test
	public void getSearchRecipeTest() throws Exception {
		mockMvc.perform(get("/recipe/search")
			.param("query", "chicken")
			.param("cuisineType", "asian")
			.param("mealType", "dinner"))
			// .andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	public void postSearchRecipeTestPass() throws Exception {
		String query = "chicken";
		String cuisineType = "";
		String mealType = "dinner";
		String url = recipeSvc.getUrlStringByQuery(query, cuisineType, mealType);

		mockMvc.perform(post("/recipe/search")
			.param("query", "chicken")
			.param("cuisineType", "")
			.param("mealType", "dinner")
			.sessionAttr("currentUrl", url)
			.param("nextPage", "true")
			.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
			// .andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	public void getRecipeDetails() throws Exception {
		String query = "chicken";
		String cuisineType = "";
		String mealType = "dinner";
		String url = recipeSvc.getUrlStringByQuery(query, cuisineType, mealType);
		List<Recipe> recipeList = recipeSvc.searchRecipes(url);
		int recipeIndex = 1;
		

		mockMvc.perform(get("/recipe/search/details")
			.param("recipeIndex", String.valueOf(recipeIndex))
			.sessionAttr("recipeList", recipeList)
			.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
			// .andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	public void postRecipeDetailsPass() throws Exception {

		
		String query = "chicken";
		String cuisineType = "";
		String mealType = "dinner";
		String url = recipeSvc.getUrlStringByQuery(query, cuisineType, mealType);
		List<Recipe> recipeList = recipeSvc.searchRecipes(url);
		String username = "postRecipeTest1";

		if (jdbcTemplate.queryForObject("select count(*) from user where username = 'postRecipeTest1'",Integer.class) > 0){
			int updated = jdbcTemplate.update("delete from user where username = 'postRecipeTest1'");
		};

		loginSvc.createUser(username, "12345678", "postRecipeTest1@test.com");
		String userId = loginSvc.authenticateUser(username, "12345678");
		int recipeIndex = 10;
		boolean saveRecipe = true;

		mockMvc.perform(post("/recipe/search/details")
			.param("recipeIndex", String.valueOf(recipeIndex))
			.param("saveRecipe", String.valueOf(saveRecipe))
			.sessionAttr("recipeList", recipeList)
			.sessionAttr("username", username)
			.sessionAttr("userId", userId)
			.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
			// .andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	public void postRecipeDetailsFail() throws Exception {

		
		String query = "chicken";
		String cuisineType = "";
		String mealType = "dinner";
		String url = recipeSvc.getUrlStringByQuery(query, cuisineType, mealType);
		List<Recipe> recipeList = recipeSvc.searchRecipes(url);
		String username = "postRecipeTest2";

		if (jdbcTemplate.queryForObject("select count(*) from user where username = 'postRecipeTest2'",Integer.class) > 0){
			int updated = jdbcTemplate.update("delete from user where username = 'postRecipeTest2'");
		};

		loginSvc.createUser(username, "12345678", "postRecipeTest2@test.com");
		String userId = loginSvc.authenticateUser(username, "12345678");
		int recipeIndex = 11;
		boolean saveRecipe = true;

		recipeSvc.saveRecipe(recipeList.get(recipeIndex), userId);

		mockMvc.perform(post("/recipe/search/details")
			.param("recipeIndex", String.valueOf(recipeIndex))
			.param("saveRecipe", String.valueOf(saveRecipe))
			.sessionAttr("recipeList", recipeList)
			.sessionAttr("username", username)
			.sessionAttr("userId", userId)
			.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
			// .andDo(print())
			.andExpect(status().isBadRequest());
	}


}
