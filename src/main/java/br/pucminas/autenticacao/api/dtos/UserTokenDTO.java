package br.pucminas.autenticacao.api.dtos;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class UserTokenDTO {
	
	private String email;
	private String token;
	public UserTokenDTO() {}
	
	public UserTokenDTO(String email, String token) 
	{
		this.email = email;
		this.token = token;
	}
	
	@NotEmpty(message = "Email não pode ser vazio.")
	@Length(min = 5, max = 50, message = "Email deve conter entre 5 e 50 caracteres.")
	@Email(message="Email inválido.")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@NotEmpty(message = "Token não pode ser vazio.")
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
