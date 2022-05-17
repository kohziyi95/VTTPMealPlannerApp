package vttp2022.mealplannerapp;

import static org.junit.jupiter.api.Assertions.assertTrue;

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

	// @Test
	// void generateUserIdShouldAssertTrue(){
	// 	User user = new User();

		

	// }

}
