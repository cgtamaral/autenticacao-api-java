package br.pucminas.autenticacao.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.pucminas.autenticacao.api.entities.User;

public interface UserRepository extends JpaRepository<User, Long>
{

	Optional<User> findUserByEmail(String email);

}
