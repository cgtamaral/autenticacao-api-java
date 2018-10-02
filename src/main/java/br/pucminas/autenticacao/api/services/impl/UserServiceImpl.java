package br.pucminas.autenticacao.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.pucminas.autenticacao.api.entities.User;
import br.pucminas.autenticacao.api.repositories.UserRepository;
import br.pucminas.autenticacao.api.services.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	UserRepository userRepository;
	
	@Override
	public Optional<User> findUserByEmail(String email) {

		log.info("Buscando usuário a partir de email: {}" + email);
		
		return userRepository.findUserByEmail(email);
	}
}
