package vttp2022.mealplannerapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import vttp2022.mealplannerapp.controller.LoginController;
import vttp2022.mealplannerapp.service.LoginService;

@SpringBootTest
@AutoConfigureMockMvc

class LoginControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JdbcTemplate jdbcTemplate;

    @Autowired
    private LoginController controller;

	@Autowired 
	private LoginService loginSvc;

	@BeforeEach
	void init(){
		try {
			loginSvc.createUser("loginTest1", "12345678", "loginTest@test.com");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    @Test
	public void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
    }

	@Test
	public void getLoginTest() throws Exception {
		mockMvc.perform(get("/user/login"))
			// .andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	public void postLoginTestPass() throws Exception {
		String username = "loginTest1";
		String password = "12345678";

		// Mockito.when(loginSvc.authenticateUser(username, password)).thenReturn("4b19d09b");
		
		mockMvc.perform(post("/user/login")
			.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
			.param("username", username)
			.param("password", password))
      		// .andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	public void postLoginTestFailUsername() throws Exception {
		String username = "userTest1";
		String password = "password1";

		// Mockito.when(loginSvc.authenticateUser(username, password)).thenReturn(null);

		mockMvc.perform(post("/user/login")
			.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
			.param("username", username)
			.param("password", password))
      		// .andDo(print())
			.andExpect(status().isUnauthorized());
	}

	@Test
	public void postLoginTestFailPassword() throws Exception {
		String username = "test1";
		String password = "password1";

		// Mockito.when(loginSvc.authenticateUser(username, password)).thenReturn(null);

		mockMvc.perform(post("/user/login")
			.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
			.param("username", username)
			.param("password", password))
      		// .andDo(print())
			.andExpect(status().isUnauthorized());
	}

	@Test
	public void getRegisterTest() throws Exception {
		mockMvc.perform(get("/user/register"))
			// .andDo(print())
			.andExpect(status().isOk());
	}


	@Test
	public void postRegisterTestPass() throws Exception {
		if (jdbcTemplate.queryForObject("select count(*) from user where username = 'registerTest1'",Integer.class) > 0){
			jdbcTemplate.update("delete from user where username = 'registerTest1'");
		};

		String username = "registerTest1";
		String password = "password";
		String verifyPassword = password;
		String email = "registerTest1@test.com";

		mockMvc.perform(post("/user/register")
			.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
			.param("username", username)
			.param("password", password)
			.param("verify_password", verifyPassword)
			.param("email", email))
      		// .andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	public void postRegisterTestFailPasswordVerify() throws Exception {
		String username = "registerTest2";
		String password = "password1";
		String verifyPassword = "password2";
		String email = "registerTest2@test.com";

		mockMvc.perform(post("/user/register")
			.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
			.param("username", username)
			.param("password", password)
			.param("verifyPassword", verifyPassword)
			.param("email", email))
      		// .andDo(print())
			.andExpect(status().isBadRequest());
	}

	@AfterEach
	void destroy(){
		jdbcTemplate.update("delete from user where username = 'loginTest1'");
	}






}
