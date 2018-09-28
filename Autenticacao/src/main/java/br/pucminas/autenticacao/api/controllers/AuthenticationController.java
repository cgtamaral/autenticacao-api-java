package br.pucminas.autenticacao.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.pucminas.autenticacao.api.Response;
import br.pucminas.autenticacao.api.dtos.TokenDTO;
import br.pucminas.autenticacao.api.dtos.UserAuthenticationDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/public")
@CrossOrigin(origins = "*")

@Api(value = "authentications", description = "Recurso para autenticação de usuários na Livraria Virtual", tags={ "authentications"})
public class AuthenticationController {

    @ApiOperation(value = "Retorna um token para autentição de um usuário na Livraria Virtual", nickname = "generateToken", notes = "", tags={ "authentications"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Operação bem sucessida!"),
    		  			   @ApiResponse(code = 400, message = "O objeto da requisição possui valores inválidos!"),
    					   @ApiResponse(code = 404, message = "O usuário informado não foi encontrado!") })
	@PostMapping(value = "/authentications", produces = "application/json")
	public ResponseEntity<Response<TokenDTO>> generateToken(UserAuthenticationDTO userAuthenticationDTO,
			BindingResult result)
	{
		Response<TokenDTO> response = new Response<TokenDTO>();
/*	@Valid @RequestBody JwtAuthenticationDto authenticationDto ,
	BindingResult result )
	throws AuthenticationException {
	Response < TokenDto > response = new Response < TokenDto >();
	if ( result . hasErrors ()) {
	log . error ( "Erro validando lançamento: {}" , result . getAllErrors ());
	result . getAllErrors (). forEach ( error -> response . getErrors ()
	. add ( error . getDefaultMessage ()));
	return ResponseEntity . badRequest (). body ( response );
	}
	log . info ( "Gerando token para o email {}." , authenticationDto . getEmail ());
	Authentication authentication = authenticationManager . authenticate (
	new UsernamePasswordAuthenticationToken (
	authenticationDto . getEmail (), authenticationDto . getSenha ()));
	SecurityContextHolder . getContext (). setAuthentication ( authentication );
	UserDetails userDetails = userDetailsService . loadUserByUsername (
	authenticationDto . getEmail ());
	String token = jwtTokenUtil . obterToken ( userDetails );
	response . setData ( new TokenDto ( token ));
	88
	API RESTful com Spring Boot e Java 8 - http://kazale.com*/
		return ResponseEntity.ok(response);
	}
}
