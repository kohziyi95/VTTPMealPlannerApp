package vttp2022.mealplannerapp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import vttp2022.mealplannerapp.model.User;
import vttp2022.mealplannerapp.repository.LoginRepository;
import vttp2022.mealplannerapp.service.LoginService;



@SpringBootTest
class LoginServiceTests {

	@Autowired
	private LoginService loginSvc;

	@Autowired
	private LoginRepository LoginRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void createUserShouldReturnTrue(){
		if (jdbcTemplate.queryForObject("select count(*) from user where username = 'test1'",Integer.class) > 0){
			int updated = jdbcTemplate.update("delete from user where username = 'test1'");
		};
        User user = new User();
		// Mockito.when(mockLoginRepo.insertUser(user)).thenReturn(true);
		boolean added = false;
		try {
			added = loginSvc.createUser("test1", "12345678", "test@test.com");
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(added);
	}

	@Test
	void usernameShouldThrowException(){
		if (jdbcTemplate.queryForObject("select count(*) from user where username = 'test2'",Integer.class) > 0){
			int updated = jdbcTemplate.update("delete from user where username = 'test2'");
		};

        User user = new User();
		boolean added = false;
		try {
			added = loginSvc.createUser("test2", "12345678", "test2@test.com");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			added = loginSvc.createUser("test2", "12345678", "test3@test.com");
		} catch (Exception e) {
			e.printStackTrace();
			assertEquals("Username already exists.", e.getMessage());
		}
	}

	@Test
	void emailShouldThrowException(){
		if (jdbcTemplate.queryForObject("select count(*) from user where username = 'test3'",Integer.class) > 0){
			int updated = jdbcTemplate.update("delete from user where username = 'test3'");
		};

        User user = new User();
		boolean added = false;
		try {
			added = loginSvc.createUser("test3", "12345678", "test3@test.com");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			added = loginSvc.createUser("test4", "12345678", "test3@test.com");
		} catch (Exception e) {
			e.printStackTrace();
			assertEquals("Email already exists.", e.getMessage());
		}
	}

	@Test
	void authenticateUserShouldReturnTrue() throws Exception{
		if (jdbcTemplate.queryForObject("select count(*) from user where username = 'test5'",Integer.class) > 0){
			int updated = jdbcTemplate.update("delete from user where username = 'test5'");
		};
		User user = new User();
		boolean added = false;
		String username = "test5";
		String password = "12345678";
		String email = "test5@test.com";
		try {
			added = loginSvc.createUser(username, password, email);
		} catch (Exception e) {
			e.printStackTrace();
		String userId = loginSvc.authenticateUser(username, password);
		assertNotEquals(userId,null);
		}
	}
}
