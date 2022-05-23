package vttp2022.mealplannerapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

	private String url;
	private List<Recipe> recipeList;
	private int recipeIndex;

	@BeforeAll
	void init(){
		String query = "chicken";
		String cuisineType = "";
		String mealType = "dinner";
		url = recipeSvc.getUrlStringByQuery(query, cuisineType, mealType);
		recipeList = recipeSvc.searchRecipes(url);
		recipeIndex = 1;
		
	}

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

		mockMvc.perform(get("/recipe/search/details")
			.param("recipeIndex", String.valueOf(recipeIndex))
			.sessionAttr("recipeList", recipeList)
			.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
			// .andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	public void postRecipeDetailsPass() throws Exception {

		String username = "postRecipeTest1";

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

		String username = "postRecipeTest2";

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

	@AfterAll
	void destroy(){
		String test1UserId = jdbcTemplate.queryForObject("select user_id from user where username = 'postRecipeTest1'",String.class);
		String test2UserId = jdbcTemplate.queryForObject("select user_id from user where username = 'postRecipeTest2'",String.class);

		jdbcTemplate.update("delete from user where user_id = ? ", test1UserId);
		jdbcTemplate.update("delete from user where user_id = ? ", test2UserId);
		jdbcTemplate.update("delete from recipes where user_id = ? ", test1UserId);
		jdbcTemplate.update("delete from recipes where user_id = ? ", test2UserId);
	}
}
