package br.pucminas.autenticacao.api.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import br.pucminas.autenticacao.api.dtos.UserAuthenticationDTO;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthenticationControllerTest
{

	@Autowired
	private MockMvc mockMvc;
	
	private static final String GET_TOKEN = "/v1/public/authentications";

	@Test
	public void testErrorAuthenticateUserEmptyEmail() throws Exception 
	{
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(GET_TOKEN)
				.accept(MediaType.APPLICATION_JSON).content(getJsonForPost(null, "1234"))
				.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(requestBuilder)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").isNotEmpty())
				.andExpect(jsonPath("$.data").isEmpty());
	}
	
	@Test
	public void testErrorAuthenticateUserWithoutPermission() throws Exception 
	{
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(GET_TOKEN)
				.accept(MediaType.APPLICATION_JSON).content(getJsonForPost("joao@gmail.com","1234"))
				.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(requestBuilder)
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.errors").isNotEmpty())
				.andExpect(jsonPath("$.data").isEmpty());
	}
	
	@Test
	public void testErrorAuthenticateUserWrongPassword() throws Exception 
	{
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(GET_TOKEN)
				.accept(MediaType.APPLICATION_JSON).content(getJsonForPost("cgtamaral@gmail.com","abcd"))
				.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(requestBuilder)
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.errors").isNotEmpty())
				.andExpect(jsonPath("$.data").isEmpty());
	}
	
	@Test
	public void testSucessAuthenticateUser() throws Exception 
	{
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(GET_TOKEN)
				.accept(MediaType.APPLICATION_JSON).content(getJsonForPost("cgtamaral@gmail.com", "1234"))
				.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.errors").isEmpty())
				.andExpect(jsonPath("$.data").isNotEmpty());
	}
	
	private String getJsonForPost(String email, String senha) throws JsonProcessingException
	{
		UserAuthenticationDTO userAuthenticationDTO = new UserAuthenticationDTO(email, senha);
		ObjectMapper mapper = new ObjectMapper();
		
		return mapper.writeValueAsString(userAuthenticationDTO);
	}
}
