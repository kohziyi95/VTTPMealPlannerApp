// package vttp2022.mealplannerapp;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertTrue;

// import java.util.ArrayList;
// import java.util.List;

// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

// import vttp2022.mealplannerapp.controller.LoginController;
// import vttp2022.mealplannerapp.model.Recipe;
// import vttp2022.mealplannerapp.repository.RecipeRepository;
// import vttp2022.mealplannerapp.service.LoginService;
// import vttp2022.mealplannerapp.service.RecipeService;

// @WebMvcTest(LoginController.class)
// class LoginControllerTests {

// 	@Autowired
// 	private MockMvc mvc;

// 	@MockBean
// 	private RecipeService recipeSvc;

// 	@MockBean
// 	private RecipeRepository recipeRepo;

// 	@MockBean
// 	private LoginService loginSvc;

// 	@Autowired
// 	private JdbcTemplate jdbcTemplate;



// // 	@Test
// // 	public void postLoginTest() throws Exception {
// // 		String username = "user1";
// // 		String password = "password1";

// // 		Mockito.when(loginSvc.authenticateUser(username, password)).thenReturn("09a7a8cd");
// // 		mvc.perform(MockMvcRequestBuilders
// // 			.post("/login"))
// // 			.contentType(MediaType.APPLICATION_JSON)
// //       		.accept(MediaType.APPLICATION_JSON))
// // 			.andExpect(status().isCreated());
// // 	}
// // }
