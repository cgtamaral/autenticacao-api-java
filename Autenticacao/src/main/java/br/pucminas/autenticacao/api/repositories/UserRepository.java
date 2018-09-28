package br.pucminas.autenticacao.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.pucminas.autenticacao.api.entities.User;

public interface UserRepository extends JpaRepository<User, Long>
{

}
