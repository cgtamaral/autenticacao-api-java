package br.pucminas.autenticacao.api.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.pucminas.autenticacao.api.Response;
import br.pucminas.autenticacao.api.dtos.TokenDTO;
import br.pucminas.autenticacao.api.dtos.UserAuthenticationDTO;
import br.pucminas.autenticacao.api.entities.User;
import br.pucminas.autenticacao.api.security.utils.JWTTokenUtil;
import br.pucminas.autenticacao.api.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/public")
@CrossOrigin(origins = "*")

@Api(value = "authentications", description = "Recurso para autenticação de usuários na Livraria Virtual", tags={ "authentications"})
public class AuthenticationController {
	
	private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JWTTokenUtil jwtTokenUtil;
	
    @ApiOperation(value = "Retorna um token para autenticação de um usuário na Livraria Virtual", nickname = "authenticateUser", notes = "", tags={ "authentications"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Operação bem sucessida!"),
    		  			   @ApiResponse(code = 400, message = "O objeto da requisição possui valores inválidos!"),
    					   @ApiResponse(code = 403, message = "O usuário informado não possui acesso!") })
	@PostMapping(value = "/authentications", produces = "application/json")
	public ResponseEntity<Response<TokenDTO>> authenticateUser(@ApiParam(value = "Objeto com informações do usuário para obtenção de um token", required = true) @Valid @RequestBody UserAuthenticationDTO userAuthenticationDTO,
			BindingResult result) //throws AuthenticationException
	{
    	log.info("Autenticando usuário: {}" + userAuthenticationDTO.getEmail());
		Response<TokenDTO> response = new Response<TokenDTO>();	 
		if (result.hasErrors ())
		{
			log.error( "Erro validar informações para o usuário informado: {}" , result.getAllErrors());
			result.getAllErrors().forEach ( error -> response.getErrors().add(error.getDefaultMessage()));
			
			return ResponseEntity.badRequest().body(response);
		}
		
		log.info("Buscando usuário a partir de email: {}" + userAuthenticationDTO.getEmail());
		Optional<User> user = userService.findUserByEmail(userAuthenticationDTO.getEmail());
		if(!user.isPresent())
		{
			log.error( "Usuário informado não encontrado: {}" , userAuthenticationDTO.getEmail());
			response.getErrors().add("Usuário informado não encontrado!");
			
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}
		
		if(!user.get().getPassword().equals(userAuthenticationDTO.getPassword()))
		{
			log.error( "A combinação de usuário/senha está incorreta para o usuário: {}" , userAuthenticationDTO.getEmail());
			response.getErrors().add("\"A combinação de usuário/senha está incorreta para o usuário!");
			
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}
		
		log.info( "Gerando token para o e-mail {}." , userAuthenticationDTO.getEmail());

		String token = jwtTokenUtil.generateToken(user.get());
		response.setData(new TokenDTO(token));
		
		return ResponseEntity.ok(response);
	}
    

}
