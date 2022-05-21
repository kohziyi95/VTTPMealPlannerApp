package vttp2022.mealplannerapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import vttp2022.mealplannerapp.controller.MainController;

@SpringBootTest
@AutoConfigureMockMvc

class MainControllerTests {

	@Autowired
	private MockMvc mockMvc;

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
