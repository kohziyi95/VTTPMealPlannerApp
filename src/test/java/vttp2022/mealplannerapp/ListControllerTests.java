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

import vttp2022.mealplannerapp.controller.ListController;
import vttp2022.mealplannerapp.model.Recipe;
import vttp2022.mealplannerapp.service.LoginService;
import vttp2022.mealplannerapp.service.RecipeService;

@SpringBootTest
@AutoConfigureMockMvc

class ListControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JdbcTemplate jdbcTemplate;

    @Autowired
    private ListController controller;

	@Autowired
	private RecipeService recipeSvc;

	@Autowired
	private LoginService loginSvc;
	
    @Test
	public void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
    }

	@Test
	public void getMyRecipeTest() throws Exception {
		String query = "chicken";
		String cuisineType = "";
		String mealType = "dinner";
		String url = recipeSvc.getUrlStringByQuery(query, cuisineType, mealType);
		List<Recipe> recipeList = recipeSvc.searchRecipes(url);
		String username = "myRecipeTest1";

		if (jdbcTemplate.queryForObject("select count(*) from user where username = 'myRecipeTest1'",Integer.class) > 0){
			int updated = jdbcTemplate.update("delete from user where username = 'myRecipeTest1'");
		};

		loginSvc.createUser(username, "12345678", "myRecipeTest1@test.com");
		String userId = loginSvc.authenticateUser(username, "12345678");
		int recipeIndex = 5;

		recipeSvc.saveRecipe(recipeList.get(recipeIndex), userId);

		mockMvc.perform(get("/list/" + userId + "/myrecipes")
			// .param("userId", userId)
			.sessionAttr("userId", userId)
			.sessionAttr("username", username))
			.andDo(print())
			.andExpect(status().isOk());
	}

	

}
