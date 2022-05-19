package vttp2022.mealplannerapp;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import vttp2022.mealplannerapp.service.LoginService;



@SpringBootTest
class LoginServiceTests {

	@Autowired
	private LoginService loginSvc;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private boolean userAdded = false;

	@BeforeEach
	void init(){
		try {
			userAdded = loginSvc.createUser("loginTest1", "12345678", "loginTest@test.com");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void createUserShouldReturnTrue(){
		assertTrue(userAdded);
	}

	@Test
	void usernameShouldThrowException(){
		assertThrows(Exception.class, () -> loginSvc.createUser("loginTest1", "12345678", "test1@test.com"));
	}

	@Test
	void emailShouldThrowException(){
		assertThrows(Exception.class, () -> loginSvc.createUser("test2", "12345678", "loginTest@test.com"));
	}

	@Test
	void authenticateUserShouldReturnTrue() throws Exception{
		String username = "loginTest1";
		String password = "12345678";
		String userId = loginSvc.authenticateUser(username, password);
		assertTrue(!userId.equals(null));
	}

	@AfterEach
	void destroy(){
		jdbcTemplate.update("delete from user where username = 'loginTest1'");
	}
}
