package br.pucminas.autenticacao.api.services;

import java.util.Optional;

import br.pucminas.autenticacao.api.entities.User;

public interface UserService {
	
	/**
	 * Retorna um usuário a partir do email informado via parâmetro
	 * 
	 * @return Optional<User>
	 */
	Optional<User> findUserByEmail(String email);
}
