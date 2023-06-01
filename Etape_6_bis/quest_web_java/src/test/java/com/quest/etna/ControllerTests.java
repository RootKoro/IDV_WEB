package com.quest.etna;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.quest.etna.model.AddressDetails;
import com.quest.etna.model.User;
import com.quest.etna.model.UserMoreDetailed;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTests {

	@Autowired protected MockMvc mockMvc;
	
	@Test
	public void testAuthenticate() throws Exception{
		User newUser = new User("testuser", "pass");
		User user2 = new User("user2", "pass");
		User newAdmin = new User("testadmin", "pass", "ROLE_ADMIN");
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String userToJson = ow.writeValueAsString(newUser);
		String adminToJson = ow.writeValueAsString(newAdmin);
		String user2ToJson = ow.writeValueAsString(user2);
		String token = "";
		
		this.mockMvc
			.perform(
					MockMvcRequestBuilders.post("/register")
					.contentType(MediaType.APPLICATION_JSON)
					.content(userToJson)
			)
			.andDo(print())
			.andExpect(status().isCreated());
		
		this.mockMvc.perform(
			MockMvcRequestBuilders.post("/register")
			.contentType(MediaType.APPLICATION_JSON)
			.content(user2ToJson)
		);

		this.mockMvc
			.perform(
					MockMvcRequestBuilders.post("/register")
					.contentType(MediaType.APPLICATION_JSON)
					.content(userToJson)
			)
			.andDo(print())
			.andExpect(status().isConflict());

		this.mockMvc
			.perform(
					MockMvcRequestBuilders.post("/register")
					.contentType(MediaType.APPLICATION_JSON)
					.content(adminToJson)
			)
			.andExpect(status().isCreated());

		MvcResult result = this.mockMvc
			.perform(
					MockMvcRequestBuilders.post("/authenticate")
					.contentType(MediaType.APPLICATION_JSON)
					.content(userToJson)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();
		
		String jsonToken = result.getResponse().getContentAsString();
		Token tokenObject = new ObjectMapper().readValue(jsonToken, Token.class);
		token = tokenObject.token;

		this.mockMvc
			.perform(
					MockMvcRequestBuilders.get("/me")
					.header("authorization", "Bearer " + token)
			)
			.andDo(print())
			.andExpect(status().isOk());
	}
	
	@Test
	public void testUser() throws Exception {
		User newUser = new User("testuser", "pass");
		User newAdmin = new User("testAdmin", "pass", "ROLE_ADMIN");
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String userToJson = ow.writeValueAsString(newUser);
		String adminToJson = ow.writeValueAsString(newAdmin);
		String token = "";
		
		MvcResult userResult = this.mockMvc
				.perform(
						MockMvcRequestBuilders.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(userToJson)
				)
				.andExpect(status().isOk())
				.andReturn();
			
		String jsonToken = userResult.getResponse().getContentAsString();
		Token tokenObject = new ObjectMapper().readValue(jsonToken, Token.class);
		token = tokenObject.token;
		
		this.mockMvc
			.perform(MockMvcRequestBuilders.get("/user"))
			.andDo(print())
			.andExpect(status().isUnauthorized());
		
		MvcResult userList = this.mockMvc
			.perform(
					MockMvcRequestBuilders.get("/user")
					.header("authorization", "Bearer " + token)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();
		
		String userListStringified = userList.getResponse().getContentAsString();
		List<UserMoreDetailed> userDetailsList = new ObjectMapper().readValue(userListStringified, new TypeReference<List<UserMoreDetailed>>() {});
		UserMoreDetailed userToDelete = new UserMoreDetailed();
		
		for (UserMoreDetailed userDetails : userDetailsList)
			if ("testuser".equals(userDetails.getUsername()))
				userToDelete = userDetails;

		this.mockMvc
			.perform(
					MockMvcRequestBuilders.delete("/user/" + String.valueOf(userToDelete.getId()))
					.header("authorization", "Bearer " + token)
			)
			.andDo(print())
			.andExpect(status().isForbidden());
		
		MvcResult adminResult = this.mockMvc
				.perform(
						MockMvcRequestBuilders.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(adminToJson)
				)
				.andExpect(status().isOk())
				.andReturn();
		
		jsonToken = adminResult.getResponse().getContentAsString();
		tokenObject = new ObjectMapper().readValue(jsonToken, Token.class);
		token = tokenObject.token;
		
		this.mockMvc
		.perform(
				MockMvcRequestBuilders.delete("/user/" + String.valueOf(userToDelete.getId()))
				.header("authorization", "Bearer " + token))
		.andDo(print())
		.andExpect(status().isOk());
	}
	
	@Test
	public void testAddress() throws Exception {
		User newUser = new User("testuser", "pass");
		User user2 = new User("user2", "pass");
		User newAdmin = new User("testadmin", "pass", "ROLE_ADMIN");
		AddressDetails newAddress = new AddressDetails(0, "street", "75001", "city", "country", "");
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String userToJson = ow.writeValueAsString(newUser);
		String user2ToJson = ow.writeValueAsString(user2);
		String adminToJson = ow.writeValueAsString(newAdmin);
		String addressToJson = ow.writeValueAsString(newAddress);
		String token = "";
		String superToken = "";
		
		this.mockMvc
			.perform(
					MockMvcRequestBuilders.post("/register")
					.contentType(MediaType.APPLICATION_JSON)
					.content(userToJson)
			);
		
		MvcResult userResult = this.mockMvc
			.perform(
				MockMvcRequestBuilders.post("/authenticate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(userToJson)
			)
			.andExpect(status().isOk())
			.andReturn();
			
		String userToken = userResult.getResponse().getContentAsString();
		Token userTokenObject = new ObjectMapper().readValue(userToken, Token.class);
		token = userTokenObject.token;
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/address"))
			.andDo(print())
			.andExpect(status().isUnauthorized());
		
		this.mockMvc.perform(
				MockMvcRequestBuilders.get("/address")
				.header("authorization", "Bearer " + token)
			).andDo(print())
			.andExpect(status().isOk());
		
		userResult = this.mockMvc
			.perform(
				MockMvcRequestBuilders.post("/authenticate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(user2ToJson)
			)
			.andExpect(status().isOk())
			.andReturn();

		userToken = userResult.getResponse().getContentAsString();
		userTokenObject = new ObjectMapper().readValue(userToken, Token.class);
		token = userTokenObject.token;
		
		MvcResult addressResult = this.mockMvc
			.perform(
				MockMvcRequestBuilders.post("/address")
				.contentType(MediaType.APPLICATION_JSON)
				.header("authorization", "Bearer " + token)
				.content(addressToJson)
			).andDo(print())
			.andExpect(status().isCreated())
			.andReturn();
		
		userResult = this.mockMvc
				.perform(
						MockMvcRequestBuilders.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(userToJson)
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
			
		userToken = userResult.getResponse().getContentAsString();
		userTokenObject = new ObjectMapper().readValue(userToken, Token.class);
		token = userTokenObject.token;
		
		String addressStringified = addressResult.getResponse().getContentAsString();
		AddressDetails address = new ObjectMapper().readValue(addressStringified, AddressDetails.class);
		
		this.mockMvc.perform(
				MockMvcRequestBuilders.delete("/address/" + address.getId())
				.header("authorization", "Bearer " + token)
			).andDo(print())
			.andExpect(status().isForbidden());
		
		MvcResult adminResult = this.mockMvc
				.perform(
						MockMvcRequestBuilders.post("/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(adminToJson)
				)
				.andExpect(status().isOk())
				.andReturn();
		

		String adminToken = adminResult.getResponse().getContentAsString();
		Token adminTokenObject = new ObjectMapper().readValue(adminToken, Token.class);
		superToken = adminTokenObject.token;
		
		this.mockMvc.perform(
				MockMvcRequestBuilders.delete("/address/" + address.getId())
				.header("authorization", "Bearer " + superToken)
			).andDo(print())
			.andExpect(status().isOk());
	}
}
