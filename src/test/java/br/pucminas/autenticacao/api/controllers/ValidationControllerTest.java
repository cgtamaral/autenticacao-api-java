package br.pucminas.autenticacao.api.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.pucminas.autenticacao.api.dtos.UserTokenDTO;
import br.pucminas.autenticacao.api.entities.User;
import br.pucminas.autenticacao.api.security.utils.JWTTokenUtil;
import br.pucminas.autenticacao.api.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ValidationControllerTest 
{
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private JWTTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserService userService;
	
	private static final String VALIDATE_TOKEN = "/v1/public/validations";

	@Test
	public void testErrorValidateTokenUserEmptyEmail() throws Exception 
	{
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(VALIDATE_TOKEN)
				.accept(MediaType.APPLICATION_JSON).content(getJsonForPost(null, "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjZ3RhbWFyYWxAZ21haWwuY29tIiwicm9sZSI6IlJPTEVfQURNSU4iLCJleHAiOjE1Mzg1NDAxMTksImlhdCI6MTUzODUzODMxOSwianRpIjoiMSJ9.t7JY_1xO8IcHl4cRtz2pgSFuhih2irq9wfPzafZsDIw"))
				.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(requestBuilder)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").isNotEmpty())
				.andExpect(jsonPath("$.data").isEmpty());
	}
	
	@Test
	public void testErrorValidateTokenUserWithoutPermission() throws Exception 
	{
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(VALIDATE_TOKEN)
				.accept(MediaType.APPLICATION_JSON).content(getJsonForPost("joao@gmail.com", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjZ3RhbWFyYWxAZ21haWwuY29tIiwicm9sZSI6IlJPTEVfQURNSU4iLCJleHAiOjE1Mzg1NDAxMTksImlhdCI6MTUzODUzODMxOSwianRpIjoiMSJ9.t7JY_1xO8IcHl4cRtz2pgSFuhih2irq9wfPzafZsDIw"))
				.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(requestBuilder)
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.errors").isNotEmpty())
				.andExpect(jsonPath("$.data").isEmpty());
	}
	
	@Test
	public void testErrorValidateTokenExpired() throws Exception 
	{
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(VALIDATE_TOKEN)
				.accept(MediaType.APPLICATION_JSON).content(getJsonForPost("cgtamaral@gmail.com", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjZ3RhbWFyYWxAZ21haWwuY29tIiwicm9sZSI6IlJPTEVfQURNSU4iLCJleHAiOjE1Mzg1NDAxMTksImlhdCI6MTUzODUzODMxOSwianRpIjoiMSJ9.t7JY_1xO8IcHl4cRtz2pgSFuhih2irq9wfPzafZsDIw"))
				.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(requestBuilder)
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.errors").isNotEmpty())
				.andExpect(jsonPath("$.data").isEmpty());
	}
	
	@Test
	public void testErrorValidateTokenInvalidUserForToken() throws Exception 
	{
		Optional<User> user = userService.findUserByEmail("cgtamaral@gmail.com");
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(VALIDATE_TOKEN)
				.accept(MediaType.APPLICATION_JSON).content(getJsonForPost("francisco@gmail.com", jwtTokenUtil.generateToken(user.get())))
				.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(requestBuilder)
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.errors").isNotEmpty())
				.andExpect(jsonPath("$.data").isEmpty());
	}
	
	@Test
	public void testSucessValidateToken() throws Exception 
	{
		Optional<User> user = userService.findUserByEmail("cgtamaral@gmail.com");
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(VALIDATE_TOKEN)
				.accept(MediaType.APPLICATION_JSON).content(getJsonForPost("cgtamaral@gmail.com", jwtTokenUtil.generateToken(user.get())))
				.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.errors").isEmpty())
				.andExpect(jsonPath("$.data").isEmpty());
	}
	
	private String getJsonForPost(String email, String token) throws JsonProcessingException
	{
		UserTokenDTO userAuthenticationDTO = new UserTokenDTO(email, token);
		ObjectMapper mapper = new ObjectMapper();
		
		return mapper.writeValueAsString(userAuthenticationDTO);
	}
}
