package vttp2022.mealplannerapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import vttp2022.mealplannerapp.controller.MainController;

@SpringBootTest
@AutoConfigureMockMvc

class MainControllerTests {

	@Autowired
	private MockMvc mockMvc;

	// @MockBean
	// private RecipeService recipeSvc;

	// @MockBean
	// private RecipeRepository recipeRepo;

	// @MockBean
	// private LoginService loginSvc;

	@Autowired
	private JdbcTemplate jdbcTemplate;

    @Autowired
    private MainController controller;

    @Test
	public void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
    }

	@Test
	public void getIndexTest() throws Exception {
		mockMvc.perform(get("/index"))
			// .andDo(print())
			.andExpect(status().isOk());
	}

}
