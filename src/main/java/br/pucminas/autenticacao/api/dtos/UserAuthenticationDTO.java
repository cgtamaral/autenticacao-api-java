package br.pucminas.autenticacao.api.dtos;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class UserAuthenticationDTO 
{
	private String email;
	private String password;
	
	public UserAuthenticationDTO() {}
	
	public UserAuthenticationDTO(String email, String password) {
		this.email = email;
		this.password = password;
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
	
	@NotEmpty(message = "A senha não pode ser vazia.")
	@Length(min = 4, max = 200, message = "A senha deve conter entre 5 e 12 caracteres.")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
