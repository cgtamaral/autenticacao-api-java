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
import br.pucminas.autenticacao.api.dtos.UserTokenDTO;
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

@Api(value = "validations", description = "Recurso para validação de Tokens de usuários das Livraria Virtual", tags={ "validations"})
public class ValidationController {
	
	private static final Logger log = LoggerFactory.getLogger(ValidationController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JWTTokenUtil jwtTokenUtil;
	
    @ApiOperation(value = "Verifica se o token informado por um usuário da Livraria Virtual é válido", nickname = "validateToken", notes = "", tags={ "validations"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Operação bem sucessida!"),
						   @ApiResponse(code = 400, message = "O objeto da requisição possui valores inválidos!"),
						   @ApiResponse(code = 403, message = "O usuário informado não possui acesso!") })
	@PostMapping(value = "/validations", produces = "application/json")
    private ResponseEntity<Response<String>> validateToken(@ApiParam(value = "Objeto com informações do usuário e token para validação", required = true) 
    @Valid @RequestBody UserTokenDTO userTokenDTO, BindingResult result)
    {
      log.info("Validando Token: {}" + userTokenDTO.getToken());
	  Response<String> response = new Response<String>();

	  if (result.hasErrors ())
	  {
		log.error( "Erro validar informações para o usuário informado: {}" , result.getAllErrors());
		result.getAllErrors().forEach ( error -> response.getErrors().add(error.getDefaultMessage()));
		
		return ResponseEntity.badRequest().body(response);
	  }
	  
	  Optional<User> user = userService.findUserByEmail(userTokenDTO.getEmail());
	  if(!user.isPresent())
	  {
		  log.error( "Usuário informado não encontrado: {}" , userTokenDTO.getEmail());
		  
		  response.getErrors().add("Usuário informado não encontrado!");
		  
		  return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	  }
	
	  try 
	  {
		jwtTokenUtil.validateToken(userTokenDTO.getToken(), userTokenDTO.getEmail());
	  } 
	  catch (Exception e) 
	  {
		  log.error(e.getMessage());
		  response.getErrors().add(e.getMessage());
		  
		  return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	  }

	  return ResponseEntity.ok(response);
	}
}
